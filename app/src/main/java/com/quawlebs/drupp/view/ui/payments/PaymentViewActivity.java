package com.quawlebs.drupp.view.ui.payments;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.quawlebs.drupp.models.ResponseData;
import com.quawlebs.drupp.models.TransactionResponse;
import com.quawlebs.drupp.models.TransactionVerifyModel;
import com.quawlebs.drupp.models.UserInfo;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.Helper;
import com.quawlebs.drupp.helpers.PaymentView;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;

import org.json.JSONException;

import java.util.HashMap;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.exceptions.ChargeException;
import co.paystack.android.exceptions.ExpiredAccessCodeException;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import retrofit2.Response;

public class PaymentViewActivity extends MainBaseActivity {

    PaymentView paymentView;
    Button payButton;
    private Charge charge;
    //----------------------Views-----------------------
    private EditText mDebitCard, mDebitExpiry, mDebitCvv;
    private Button mPayNow, mPayTryAgain;
    private Transaction transaction;
    private String driverName = "";
    private ResponseData driverInfo;
    private UserInfo userInfo;
    private String userEmail = "";
    private int paymentAmount = 0;
    private int cancelType = 1;//2-TRY AGAIN 1-BACK PRESS

    @Override
    protected void setUp() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_view);

        paymentView = findViewById(R.id.paymentView);
        PaystackSdk.initialize(this);

        payButton = paymentView.getPayButton();
        if (SessionManager.getInstance().getUserModel() != null) {
            userInfo = SessionManager.getInstance().getUserModel().getUserInfo();
        }

        if (userInfo != null) {
            userEmail = userInfo.getEmail();
        }

        String[] arraySpinner = new String[]{
                "Access Bank", "Citibank", "Diamond Bank", "Dynamic Standard Bank", "Ecobank Nigeria", "Fidelity Bank Nigeria",
                "First Bank of Nigeria", "First City Monument Bank", "Guaranty Trust Bank", "Heritage Bank Plc", "Jaiz Bank",
                "Keystone Bank Limited", "Providus Bank Plc", "Skye Bank", "Stanbic IBTC Bank Nigeria Limited", "Standard Chartered Bank",
                "Sterling Bank", "Suntrust Bank Nigeria Limited", "Union Bank of Nigeria", "United Bank for Africa", "Unity Bank Plc",
                "Wema Bank", "Zenith Bank"
        };
        driverInfo = Helper.getInstance(this).readFromJson(AppConstants.DRIVER_DETAILS, ResponseData.class);
        if (getIntent() != null) {
            try {
                paymentAmount = (int) getIntent().getFloatExtra(AppConstants.K_PAY_AMOUNT, 0.0f);
            } catch (Exception e) {
                showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
            }


        }
        if (driverInfo != null) {
            driverName = driverInfo.getDriverName();
        }


        paymentView.setBanksSpinner(arraySpinner);
        paymentView.setPentecostBackgroundColor(getResources().getColor(R.color.colorWhite));

        paymentView.getHeaderContentView().setTextColor(getResources().getColor(R.color.cardview_dark_background));
        paymentView.setBillContent(getString(R.string.pay_in_dollar, String.valueOf(paymentAmount)));

        paymentView.setChargeListener(new PaymentView.ChargeListener() {
            @Override
            public void onChargeCard() {

                try {
                    startAFreshCharge(true);
                } catch (Exception e) {
                    showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);

                }
            }

            @Override
            public void onChargeBank() {

            }

            @Override
            public void onSuccess() {

            }
        });
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
        paymentView.showLoader();


        if (local) {
            // Set transaction params directly in app (note that these params
            // are only used if an access_code is not set. In debug mode,
            // setting them after setting an access code would throw an exception


            try {
                charge.setAmount(paymentAmount);
                charge.setEmail(userInfo.getEmail());
                charge.setReference("ChargedFromAndroid_" + generateReference());
                charge.putCustomField("Charged From", "Android SDK");
            } catch (JSONException e) {
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

        transaction = null;
        try {
            PaystackSdk.chargeCard(PaymentViewActivity.this, charge, new Paystack.TransactionCallback() {
                // This is called only after transaction is successful
                @Override
                public void onSuccess(Transaction transaction) {
                    paymentView.hideLoader();

                    PaymentViewActivity.this.transaction = transaction;
                    showSuccessDialog();
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
                    PaymentViewActivity.this.transaction = transaction;
//                Toast.makeText(MainActivity.this, transaction.getReference(), Toast.LENGTH_LONG).show();
//                updateTextViews();
                }

                @Override
                public void onError(Throwable error, Transaction transaction) {
                    // If an access code has expired, simply ask your server for a new one
                    // and restart the charge instead of displaying error
                    PaymentViewActivity.this.transaction = transaction;
                    paymentView.hideLoader();
                    if (error instanceof ChargeException) {
                        showCardFailureDialog();
                        return;
                    }
                    if (error instanceof ExpiredAccessCodeException) {
                        PaymentViewActivity.this.startAFreshCharge(false);
                        PaymentViewActivity.this.chargeCard();
                        return;
                    }


                    if (transaction.getReference() != null) {
                        showFailureDialog();
//                    Toast.makeText(PaymentViewActivity.this, transaction.getReference() + " concluded with error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        //  mTextError.setText(String.format("%s  concluded with error: %s %s", transaction.getReference(), error.getClass().getSimpleName(), error.getMessage()));
                        //new verifyOnServer().execute(transaction.getReference());
                        verifyOnServer(transaction.getReference());
                    } else {

                        Toast.makeText(PaymentViewActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        //    mTextError.setText(String.format("Error: %s %s", error.getClass().getSimpleName(), error.getMessage()));
                    }
                    //  updateTextViews();
                }

            });

        } catch (Exception e) {
            showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
            return;
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
            payReference.setText(getString(R.string.payment_reference, transaction.getReference()));
            mAlertDialog.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
                mAlertDialog.dismiss();

            });
        }
    }

    private void showSuccessDialog() {
        if (getIntent().getIntExtra(AppConstants.K_REQUEST_CODE, 0) == AppConstants.REQ_PAYMENT) {
            showAlertDialog(R.layout.dialog_payment_successful, AppConstants.NotificationType.APP_ERROR);
            if (mAlertDialog != null) {
                TextView dialogTitle = mAlertDialog.findViewById(R.id.tv_bill_amount);
                dialogTitle.setText(String.valueOf(paymentAmount));
                //  dialogTitle.setText(getString(R.string.you_paid_to_, String.valueOf(paymentAmount), driverName));
                mAlertDialog.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
                    switchToNext();
                });
            }
        } else {
            switchToNext();
        }

    }

    private void switchToNext() {
        cancelType = 1;
        Intent intent = new Intent();
        intent.putExtra(AppConstants.K_PAY_AMOUNT, charge.getAmount());
        intent.putExtra(AppConstants.K_TRANSACTION_REFERENCE, transaction.getReference());
        setResult(RESULT_OK, intent);
        finish();
    }

    private String generateReference() {
        String keys = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {
            int index = (int) (keys.length() * Math.random());
            sb.append(keys.charAt(index));
        }

        return sb.toString();
    }

    private Card loadCardFromForm() {
        //validate fields
        Card card;

        String cardNum = paymentView.getCardNumber();

        //build card object with ONLY the number, update the other fields later
        card = new Card.Builder(cardNum, 0, 0, "").build();
        String cvc = paymentView.getCardCCV();
        //update the cvc field of the card
        card.setCvc(cvc);

        //validate expiry month;
        String expiryDate = paymentView.getCardExpDate();
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
                    if (response.body().getResponse().getStatus().equalsIgnoreCase(AppConstants.SUCCESS)) {
                        showSuccessDialog();
                    }
                } else {
                    showFailureDialog();
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
        params.put(AppConstants.K_AMOUNT, String.valueOf(paymentAmount));
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

    @Override
    public void onBackPressed() {
        if (cancelType == 2) {
            showAlertDialog(R.layout.dialog_cancel_transaction, AppConstants.NotificationType.CANCEL_TRANSACTION);
            if (mAlertDialog != null) {
                mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> PaymentViewActivity.super.onBackPressed());
            }
        } else {
            super.onBackPressed();
        }

    }
}
