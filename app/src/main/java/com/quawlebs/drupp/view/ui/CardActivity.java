package com.quawlebs.drupp.view.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.quawlebs.drupp.models.TransactionResponse;
import com.quawlebs.drupp.models.TransactionVerifyModel;
import com.quawlebs.drupp.models.UserInfo;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.AppUtil;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;

import org.json.JSONException;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.exceptions.ChargeException;
import co.paystack.android.exceptions.ExpiredAccessCodeException;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import retrofit2.Response;

public class CardActivity extends MainBaseActivity {
    private static final int CARD = 1;
    //-----------------Views----------------------
    private EditText cardNumber, cardCVV, cardExpiry;
    private Button btnPay;
    private Charge charge;
    private UserInfo userInfo;
    private int paymentType;
    private Transaction transaction;
    private int WALLET = 3;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        ButterKnife.bind(this);
        PaystackSdk.initialize(this);

        findViewById(R.id.image_back).setOnClickListener(v -> onBackPressed());
        cardNumber = findViewById(R.id.et_card_number);
        cardExpiry = findViewById(R.id.et_expiry_date);
        btnPay = findViewById(R.id.btn_pay);
        cardCVV = findViewById(R.id.etCVV);
        btnPay.setOnClickListener(v -> {

            //Make a initial Charge
            if (isValidate()) {
                startAFreshCharge(true);
            }

        });

        userInfo = SessionManager.getInstance().loadUser(this).getUserInfo();
        cardNumber.addTextChangedListener(new TextWatcher() {

            private boolean spaceDeleted;

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                CharSequence charDeleted = s.subSequence(start, start + count);
                spaceDeleted = " ".equals(charDeleted.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (cardNumber.getText().length() > 0 && cardNumber.getText().charAt(0) == '3') {
                    cardNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(AppConstants.MAX_LENGTH_CARD_NUMBER_AMEX)});

                    cardNumber.removeTextChangedListener(this);
                    int cursorPosition = cardNumber.getSelectionStart();
                    String withSpaces = formatTextAmEx(editable);
                    cardNumber.setText(withSpaces);
                    cardNumber.setSelection(cursorPosition + (withSpaces.length() - editable.length()));

                    if (spaceDeleted) {
                        cardNumber.setSelection(cardNumber.getSelectionStart() - 1);
                        spaceDeleted = false;
                    }

                    cardNumber.addTextChangedListener(this);
                } else if (cardNumber.getText().length() > 0
                        && (cardNumber.getText().charAt(0) == '4' || cardNumber.getText().charAt(0) == '5')) {
                    cardNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(AppConstants.MAX_LENGTH_CARD_NUMBER_VISA_MASTERCARD)});

                    cardNumber.removeTextChangedListener(this);
                    int cursorPosition = cardNumber.getSelectionStart();
                    String withSpaces = formatTextVisaMasterCard(editable);
                    cardNumber.setText(withSpaces);
                    cardNumber.setSelection(cursorPosition + (withSpaces.length() - editable.length()));

                    if (spaceDeleted) {
                        cardNumber.setSelection(cardNumber.getSelectionStart() - 1);
                        spaceDeleted = false;
                    }

                    cardNumber.addTextChangedListener(this);
                } else {
                    cardNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(AppConstants.MAX_LENGTH_CARD_NUMBER_VISA_MASTERCARD)});

                    cardNumber.removeTextChangedListener(this);
                    int cursorPosition = cardNumber.getSelectionStart();
                    String withSpaces = formatTextVisaMasterCard(editable);
                    cardNumber.setText(withSpaces);
                    cardNumber.setSelection(cursorPosition + (withSpaces.length() - editable.length()));

                    if (spaceDeleted) {
                        cardNumber.setSelection(cardNumber.getSelectionStart() - 1);
                        spaceDeleted = false;
                    }

                    cardNumber.addTextChangedListener(this);
                }
            }
        });

        cardExpiry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0 && (editable.length() % 3) == 0) {
                    final char c = editable.charAt(editable.length() - 1);
                    if ('/' == c) {
                        editable.delete(editable.length() - 1, editable.length());
                    }
                }
                if (editable.length() > 0 && (editable.length() % 3) == 0) {
                    char c = editable.charAt(editable.length() - 1);
                    if (Character.isDigit(c) && TextUtils.split(editable.toString(), String.valueOf("/")).length <= 2) {
                        editable.insert(editable.length() - 1, String.valueOf("/"));
                    }
                }

            }
        });
    }

    private boolean isValidate() {
        if (cardNumber.getText().toString().trim().isEmpty()) {
            showMessage(R.string.please_enter_your_card_details);
            return false;
        } else if (cardExpiry.getText().toString().trim().isEmpty()) {
            showMessage(R.string.please_enter_your_card_details);
            return false;
        } else if (cardCVV.getText().toString().trim().isEmpty()) {
            showMessage(R.string.please_enter_your_card_details);
            return false;
        }
        return true;

    }

    @OnClick(R.id.btn_cancel)
    public void onCancel() {
        onBackPressed();
    }

    private void startAFreshCharge(boolean local) {
        // initialize the charge
        charge = new Charge();

        if (loadCardFromForm().isValid()) {
            charge.setCard(loadCardFromForm());
        } else {
            showMessage(R.string.invalid_card);
            return;
        }
        showDialogProgressBar();
        //TODO: SIMPLIFY CHARGE

        if (local) {
            // Set transaction params directly in app (note that these params
            // are only used if an access_code is not set. In debug mode,
            // setting them after setting an access code would throw an exception


            try {
                charge.setAmount(AppConstants.INITIAL_CHARGE);
                charge.setEmail(userInfo.getEmail());
                charge.setReference("ChargedFromAndroid_" + AppUtil.generateReference());
                charge.putCustomField("Charged From", "Android SDK");
            } catch (JSONException e) {
                hideDialogProgressBar();
                e.printStackTrace();
                showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
            }
            chargeCard();
        } else {
            // Perform transaction/initialize on our server to get an access code
            // documentation: https://developers.paystack.co/reference#initialize-a-transaction
            //  new fetchAccessCodeFromServer().execute(backend_url + "/new-access-code");
            initTransaction();
        }
    }

    private void chargeCard() {
        paymentType = CARD;
        transaction = null;
        try {
            PaystackSdk.chargeCard(this, charge, new Paystack.TransactionCallback() {
                // This is called only after transaction is successful
                @Override
                public void onSuccess(Transaction transaction) {
                    hideDialogProgressBar();

                    CardActivity.this.transaction = transaction;
                    verifyOnServer(transaction.getReference());
                    //                mTextError.setText(" ");
//                Toast.makeText(MainActivity.this, transaction.getReference(), Toast.LENGTH_LONG).show();
//                updateTextViews();
//                new verifyOnServer().execute(transaction.getReference());
                }

                // This is called only before requesting OTP
                // Save reference so you may send to server if
                // error occurs with OTP
                // No need to dismiss dialog
                @Override
                public void beforeValidate(Transaction transaction) {
                    CardActivity.this.transaction = transaction;
//                Toast.makeText(MainActivity.this, transaction.getReference(), Toast.LENGTH_LONG).show();
//                updateTextViews();
                }

                @Override
                public void onError(Throwable error, Transaction transaction) {
                    // If an access code has expired, simply ask your server for a new one
                    // and restart the charge instead of displaying error
                    CardActivity.this.transaction = transaction;
                    hideDialogProgressBar();
                    if (error instanceof ChargeException) {
                        showCardFailureDialog();
                        return;
                    }
                    if (error instanceof ExpiredAccessCodeException) {
                        CardActivity.this.startAFreshCharge(false);
                        return;
                    }

                    if (transaction.getReference() != null) {
                        showFailureDialog();
                        Toast.makeText(CardActivity.this, transaction.getReference() + " concluded with error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        //  mTextError.setText(String.format("%s  concluded with error: %s %s", transaction.getReference(), error.getClass().getSimpleName(), error.getMessage()));
                        //new verifyOnServer().execute(transaction.getReference());
                        verifyOnServer(transaction.getReference());
                    } else {

                        Toast.makeText(CardActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        //    mTextError.setText(String.format("Error: %s %s", error.getClass().getSimpleName(), error.getMessage()));
                    }
                    //  updateTextViews();
                }

            });

        } catch (Exception e) {
            hideDialogProgressBar();
            showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
        }


    }


    private void showCardFailureDialog() {
        showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.PAYMENT_FAILED);
        if (mAlertDialog != null) {
            TextView dialogTitle = mAlertDialog.findViewById(R.id.tv_title);
            dialogTitle.setText(getString(R.string.unable_to_charge_your_card));

            mAlertDialog.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
                mAlertDialog.dismiss();

            });
        }
    }


    private void showFailureDialog() {
        showAlertDialog(R.layout.dialog_payment_failed, AppConstants.NotificationType.PAYMENT_FAILED);
        if (mAlertDialog != null) {
            TextView payReference = mAlertDialog.findViewById(R.id.tv_reference_payment);
            if (paymentType == CARD) {
                payReference.setText(getString(R.string.payment_reference, transaction.getReference()));
            }

            mAlertDialog.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
                mAlertDialog.dismiss();

            });
        }
    }

    private void verifyOnServer(String reference) {
        showDialogProgressBar();
        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstants.K_REFERENCE, reference);
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<TransactionVerifyModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<TransactionVerifyModel>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    showAnimatedtDialog(CardActivity.this, getString(R.string.card_added_successfully), getString(R.string.you_can_now_pay_with_this_saved_card), getString(R.string.go_back)
                            , null, sweetAlertDialog -> {
                                hideAnimatedDialog();
                                onBackPressed();
                            }, SweetAlertDialog.SUCCESS_TYPE);
                }
            }

            @Override
            public void onError(Response<QualStandardResponse<TransactionVerifyModel>> response) {
                hideDialogProgressBar();
                showErrorPrompt(response);
            }

            @Override
            public void onNullResponse() {
                hideDialogProgressBar();
            }

            @Override
            public void onFailure(Throwable t) {
                hideDialogProgressBar();
                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        verifyOnServer(reference);
                    });
                }
            }
        }, SessionManager.getAccessToken()).verifyTransaction(params);

    }

    private void initTransaction() {
        showDialogProgressBar();
        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstants.K_EMAIL, userInfo.getEmail());
        params.put(AppConstants.K_AMOUNT, String.valueOf(AppConstants.INITIAL_CHARGE));
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<TransactionResponse>() {
            @Override
            public void onResponse(Response<QualStandardResponse<TransactionResponse>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    charge.setAccessCode(response.body().getResponse().getData().getAccessCode());
                    chargeCard();
                }
            }

            @Override
            public void onError(Response<QualStandardResponse<TransactionResponse>> response) {
                hideDialogProgressBar();
                showErrorPrompt(response);
            }

            @Override
            public void onNullResponse() {
                hideDialogProgressBar();
            }

            @Override
            public void onFailure(Throwable t) {
                hideDialogProgressBar();
                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        initTransaction();
                    });
                }
            }
        }, SessionManager.getAccessToken()).initTransaction(params);
    }

    private Card loadCardFromForm() {
        //validate fields
        Card card;

        String cardNum = cardNumber.getText().toString().trim();

        //build card object with ONLY the number, update the other fields later
        card = new Card.Builder(cardNum, 0, 0, "").build();
        String cvc = cardCVV.getText().toString().trim();
        //update the cvc field of the card
        card.setCvc(cvc);

        //validate expiry month;
        String expiryDate = cardExpiry.getText().toString().trim();
        String sMonth = expiryDate.substring(0, expiryDate.lastIndexOf('/'));
        String sYear = expiryDate.substring(expiryDate.lastIndexOf('/') + 1);

        int month = 0;
        try {
            month = Integer.valueOf(sMonth);
        } catch (Exception ignored) {
            showMessage(R.string.something_went_wrong);
        }

        card.setExpiryMonth(month);

        int year = 0;
        try {
            year = Integer.valueOf(sYear);
        } catch (Exception ignored) {
            showMessage(R.string.something_went_wrong);
        }
        card.setExpiryYear(year);

        return card;
    }


    private String formatTextVisaMasterCard(CharSequence text) {
        StringBuilder formatted = new StringBuilder();
        int count = 0;
        for (int i = 0; i < text.length(); ++i) {
            if (Character.isDigit(text.charAt(i))) {
                if (count % 4 == 0 && count > 0)
                    formatted.append(" ");
                formatted.append(text.charAt(i));
                ++count;
            }
        }
        return formatted.toString();
    }

    private String formatTextAmEx(CharSequence text) {
        StringBuilder formatted = new StringBuilder();
        int count = 0;
        for (int i = 0; i < text.length(); ++i) {
            if (Character.isDigit(text.charAt(i))) {
                if (((count == 4) || (count == 10))) {
                    formatted.append(" ");
                }
                formatted.append(text.charAt(i));
                ++count;
            }
        }
        return formatted.toString();
    }


}
