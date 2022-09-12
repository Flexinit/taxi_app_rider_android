package com.quawlebs.drupp.view.ui.payments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.libraries.places.api.model.Place;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.Helper;
import com.quawlebs.drupp.helpers.IAdapterItemClickListener;
import com.quawlebs.drupp.helpers.IDateDialogResponseObserver;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.login.StartUpActivity;
import com.quawlebs.drupp.models.Authorization;
import com.quawlebs.drupp.models.BankDetailsModel;
import com.quawlebs.drupp.models.CheckOutModel;
import com.quawlebs.drupp.models.ResponseData;
import com.quawlebs.drupp.models.RideInfo;
import com.quawlebs.drupp.models.SavedCardTVerifyModel;
import com.quawlebs.drupp.models.ShopTransactionModel;
import com.quawlebs.drupp.models.TransactionRefStoreModel;
import com.quawlebs.drupp.models.TransactionResponse;
import com.quawlebs.drupp.models.TransactionVerifyModel;
import com.quawlebs.drupp.models.UserInfo;
import com.quawlebs.drupp.models.WalletModel;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.view.adapters.SavedCardListAdapter;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;
import com.quawlebs.drupp.view.ui.dialog.DateDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.exceptions.ChargeException;
import co.paystack.android.exceptions.ExpiredAccessCodeException;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.Response;

public class MainPaymentActivity extends MainBaseActivity implements AdapterView.OnItemClickListener,
        IDateDialogResponseObserver, IAdapterItemClickListener {

    //--------------Views-----------------
    private RecyclerView mSavedCardRecyclerView;
    private CardView btnPayUsingDebit, btnPayUsingWallet, btnPayUsingAccount, btnPayUsingSavedCards;
    private ExpandableRelativeLayout expandableLayout, expandableLayoutWallet, expandableLayoutBank;
    private ProgressBar progressBar;
    private Button payButton, payButtonWallet, payButtonBank;
    private Button banksNameList;
    private EditText creditNumber;
    private EditText creditMonth;
    private EditText creditCCV;
    private TextView mWalletBalance;
    private EditText mAccountNumber, mAccountHolderBirthday;
    //--------------Globals&Adapters&List---------------
    private SavedCardListAdapter savedCardListAdapter;
    private Integer[] imageArray = {R.drawable.visa, R.drawable.mastercard, R.drawable.discover, R.drawable.american_express};
    private int formerLength = 0;
    private String driverName = "";
    private UserInfo userInfo;
    private ListPopupWindow banksListPopUp;
    private ArrayAdapter<String> banksNamesAdapter;
    private LinkedList<Authorization> savedCards = new LinkedList<>();
    private ArrayList<BankDetailsModel> bankDetailsModels = new ArrayList<>();
    private ArrayList<String> bankNames = new ArrayList<>();
    private Charge charge;
    private ResponseData driverInfo;
    private Transaction transaction;
    private int CARD = 1;
    private int WALLET = 3;
    private int BANK = 4;
    private int paymentType = CARD;
    private int PAYMENT_FROM = CARD;

    private float paymentAmount = 0;
    private int cancelType = 1;//2-TRY AGAIN 1-BACK PRESS
    private String walletReference = "";
    private int selectedBankPosition;
    private String mSelectedAuthorization = "";
    private int currentRideId;
    private String currentBillAmount;
    private int driverId;
    private int currentRideType;
    private RideInfo responseData;

    @Override
    protected void setUp() {

        mSavedCardRecyclerView = findViewById(R.id.rv_saved_cards);
        expandableLayout = findViewById(R.id.expandable_layout);
        expandableLayoutBank = findViewById(R.id.expandable_layout_bank);
        btnPayUsingDebit = findViewById(R.id.btn_pay_using_debit);
        creditNumber = findViewById(R.id.credit_card_number);
        btnPayUsingSavedCards = findViewById(R.id.pay_using_saved_cards);
        btnPayUsingWallet = findViewById(R.id.pay_using_wallet);
        creditCCV = findViewById(R.id.credit_card_ccv);
        creditMonth = findViewById(R.id.credit_card_expiry);
        progressBar = findViewById(R.id.progress_bar);
        mWalletBalance = findViewById(R.id.tv_wallet_balance);
        payButtonWallet = findViewById(R.id.pay_button_wallet);
        banksNameList = findViewById(R.id.bank_name);
        mAccountNumber = findViewById(R.id.tv_account_number);
        mAccountHolderBirthday = findViewById(R.id.tv_account_holder_birthday);
        btnPayUsingAccount = findViewById(R.id.pay_using_account);
        expandableLayoutWallet = findViewById(R.id.expandable_layout_wallet_payment);

        payButton = findViewById(R.id.pay_button);
        payButtonWallet = findViewById(R.id.pay_button_wallet);
        payButtonBank = findViewById(R.id.btn_pay_bank);


        btnPayUsingDebit.setOnClickListener(v -> {
            expandableLayout.toggle();
            expandableLayoutBank.collapse();
            expandableLayoutWallet.collapse();
        });
        btnPayUsingWallet.setOnClickListener(v -> {
            expandableLayoutWallet.toggle();
            expandableLayout.collapse();
            expandableLayoutBank.collapse();
            getWalletMoney();
        });
        btnPayUsingAccount.setOnClickListener(v -> {
            expandableLayoutBank.toggle();
            expandableLayoutWallet.collapse();
            expandableLayout.collapse();
            getBanksList();


        });

        btnPayUsingSavedCards.setOnClickListener(view -> {
            expandableLayout.collapse();
            expandableLayoutBank.collapse();
            expandableLayoutWallet.collapse();

        });

        creditNumber.setBackgroundResource(R.drawable.edit_text_white_bg);
        creditCCV.setBackgroundResource(R.drawable.edit_text_white_bg);
        creditMonth.setBackgroundResource(R.drawable.edit_text_white_bg);

        bankNames.add(getString(R.string.select_bank_account));
        banksNamesAdapter = new ArrayAdapter<>(this, R.layout.layout_spinner_item, bankNames);
        banksListPopUp = new ListPopupWindow(this);
        banksListPopUp.setAdapter(banksNamesAdapter);
        banksListPopUp.setOnItemClickListener(this);
        banksListPopUp.setAnchorView(banksNameList);


        payButton.setBackgroundResource(R.drawable.payment_button);
        expandableLayout.collapse();
        expandableLayoutWallet.collapse();
        expandableLayoutBank.collapse();
        setTextWatchers();

        banksNameList.setOnClickListener(v -> banksListPopUp.show());

        mAccountHolderBirthday.setOnClickListener(v -> showDateDialog());
        payButton.setOnClickListener(view -> {
            expandableLayoutWallet.collapse();
            switch (PAYMENT_FROM) {
                case 1: //This is Card
                    if (!creditNumber.getText().toString().trim().equals("") && !creditCCV.getText().toString().trim().equals("") && !creditMonth.toString().trim().equals("")) {
                        try {
                            startAFreshCharge(true);
                        } catch (Exception e) {
                            showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);

                        }
                    }
                    break;
                case 4: //This is bank
//                    if (isValidateBank()) {
//
//                    }
                    //TODO: BANK PAYMENT
                    break;
            }
        });
        payButtonBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isValidateBank()) {
                    payWithBank();
                }
            }
        });
        if (getIntent() != null) {
            if (getIntent().getIntExtra(AppConstants.K_REQUEST_CODE, -1) == AppConstants.REQ_ADD_MONEY) {
                btnPayUsingWallet.setVisibility(View.GONE);
                expandableLayoutWallet.setVisibility(View.GONE);
            }
        }
        payButtonWallet.setOnClickListener(v -> payViaWallet());
        //Saved Cards List Setup
        mSavedCardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        savedCardListAdapter = new SavedCardListAdapter(this, R.layout.item_saved_card, savedCards);
        mSavedCardRecyclerView.setAdapter(savedCardListAdapter);
        savedCardListAdapter.setiAdapterItemClickListener(this);
        savedCardListAdapter.getmItemClickSubject().observeOn(AndroidSchedulers.mainThread())
                .subscribe(position -> {

                    for (Authorization authorization : savedCards) {
                        authorization.setChecked(false);
                    }
                    mSelectedAuthorization = savedCards.get(position).getAuthorizationCode();
                    savedCards.get(position).setChecked(true);
                    savedCardListAdapter.notifyItemChanged(position);
                    savedCardListAdapter.notifyDataSetChanged();
                });
        getSavedCards();
    }

    private void showDateDialog() {
        Bundle bundle = new Bundle();
        bundle.putLong(AppConstants.K_MIN_DATE, 0);
        bundle.putLong(AppConstants.K_MAX_DATE, (System.currentTimeMillis() + 1000) - 18 * 365 * 24 * 60 * 60 * 1000L);
        DateDialog dateDialog = DateDialog.newInstance(bundle);
        dateDialog.setiDateDialogResponseObserver(this);
        dateDialog.show(getSupportFragmentManager(), DateDialog.class.getSimpleName());
    }

    private boolean isValidateBank() {
        if (banksNameList.getText().toString().equalsIgnoreCase(getString(R.string.select_bank_account))) {
            showMessage(R.string.please_select_bank_account);
            return false;
        } else if (mAccountNumber.getText().toString().trim().isEmpty()) {
            showMessage(R.string.please_enter_account_number);
            return false;
        } else if (mAccountHolderBirthday.getText().toString().trim().isEmpty()) {
            showMessage(R.string.please_enter_birthday_of_account_holder);
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_payment);
        PaystackSdk.initialize(this);

        userInfo = SessionManager.getInstance().getUserModel().getUserInfo();
        driverInfo = Helper.getInstance(this).readFromJson(AppConstants.DRIVER_DETAILS, ResponseData.class);

        if (getCallingActivity() != null) {

            paymentAmount = getIntent().getFloatExtra(AppConstants.K_PAY_AMOUNT, 0.0f);
            if (getIntent().getExtras() != null && getIntent().getAction() != null) {

                if (getIntent().getAction().equalsIgnoreCase(AppConstants.U_ADD_MONEY_TO_WALLET)) {
                    paymentAmount = getIntent().getFloatExtra(AppConstants.K_PAY_AMOUNT, 0.0f);

                } else {

                    try {
                        JSONObject jsonObject = new JSONObject(getIntent().getStringExtra(AppConstants.K_RIDE_INFO));

                        paymentAmount = (float) jsonObject.getDouble(AppConstants.K_TOTAL_FARE);
                        currentRideId = jsonObject.getInt(AppConstants.K_RIDE_ID);
                        driverId = jsonObject.getInt(AppConstants.K_DRIVER_ID);
                        currentBillAmount = jsonObject.getString(AppConstants.K_TOTAL_FARE);
                        currentRideType = jsonObject.getInt(AppConstants.K_POSTED_BY_DRIVER);

                    } catch (Exception e) {

                        responseData = Helper.getInstance(this).readFromJson(AppConstants.RIDE_DETAILS, RideInfo.class);
                        paymentAmount = Float.parseFloat(responseData.getTotalFare());
                        currentRideId = responseData.getId();
                        driverId = responseData.getDriverId();
                        currentBillAmount = responseData.getTotalFare();
                        currentRideType = responseData.getPostedByDriver();
                    }
                }


            }

        } else {
            if (getIntent().getExtras() != null) {
                try {
                    JSONObject jsonObject = new JSONObject(getIntent().getStringExtra(AppConstants.K_RIDE_INFO));

                    paymentAmount = (float) jsonObject.getDouble(AppConstants.K_TOTAL_FARE);
                    currentRideId = jsonObject.getInt(AppConstants.K_RIDE_ID);
                    driverId = jsonObject.getInt(AppConstants.K_DRIVER_ID);
                    currentBillAmount = jsonObject.getString(AppConstants.K_TOTAL_FARE);
                    currentRideType = jsonObject.getInt(AppConstants.K_POSTED_BY_DRIVER);

                } catch (Exception e) {

                    responseData = Helper.getInstance(this).readFromJson(AppConstants.RIDE_DETAILS, RideInfo.class);
                    paymentAmount = Float.parseFloat(responseData.getTotalFare());
                    currentRideId = responseData.getId();
                    driverId = responseData.getDriverId();
                    currentBillAmount = responseData.getTotalFare();
                    currentRideType = responseData.getPostedByDriver();
                }


            }
        }


        if (driverInfo != null) {
            driverName = driverInfo.getDriverName();
        }
        findViewById(R.id.image_back).setOnClickListener(v -> {
            onBackPressed();
        });

    }

    public void showLoader() {
        progressBar.setVisibility(View.VISIBLE);
        payButton.setVisibility(View.INVISIBLE);
        payButton.setEnabled(false);

        creditNumber.setEnabled(false);
        creditMonth.setEnabled(false);
        creditCCV.setEnabled(false);
        //payButton.blockTouch();
        //payButton.morphToProgress(R.color.white, R.dimen.size_2, width, R.dimen.size_14, 10, R.color.colorAccent);
    }

    public void hideLoader() {
        //payButton.unblockTouch();
        progressBar.setVisibility(View.GONE);
        payButton.setVisibility(View.VISIBLE);
        payButton.setEnabled(true);

        creditNumber.setEnabled(true);
        creditMonth.setEnabled(true);
        creditCCV.setEnabled(true);
    }

    private void setTextWatchers() {

        creditNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int j, int j1, int j2) {

                String cardNumber = charSequence.toString();

                if (cardNumber.length() > 2) {

                    for (int i = 0; i < listOfPattern().size(); i++) {

                        if (cardNumber.substring(0, 2).matches(listOfPattern().get(i))) {

                            creditNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, imageArray[i], 0);
                            //break;

                        } else {
                            //creditNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.visa, 0);
                            //break;

                        }

                    }

                }

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String cardNumber = creditNumber.getText().toString();

                if (!cardNumber.equalsIgnoreCase("")) {

                    for (int i = 0; i < listOfPattern().size(); i++) {

                        if (cardNumber.matches(listOfPattern().get(i))) {
                            creditNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, imageArray[i], 0);
                        }

                    }

                } else {

                    creditNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                }

            }
        });

        creditMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                formerLength = charSequence.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > formerLength) { //User is adding text
                    if (editable.length() == 2) {
                        editable.append("/");
                    }
                } else {
                    if (editable.length() == 2) {
                        editable.delete(editable.length() - 1, editable.length());
                    }
                }

            }
        });


    }

    public static ArrayList<String> listOfPattern() {
        ArrayList<String> listOfPattern = new ArrayList<>();

        String ptVisa = "^4[0-9]$";

        listOfPattern.add(ptVisa);

        String ptMasterCard = "^5[1-5]$";

        listOfPattern.add(ptMasterCard);

        String ptDiscover = "^6(?:011|5[0-9]{2})$";

        listOfPattern.add(ptDiscover);

        String ptAmeExp = "^3[47]$";

        listOfPattern.add(ptAmeExp);

        return listOfPattern;
    }

    private void getBanksList() {
        showDialogProgressBar();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<BankDetailsModel>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<BankDetailsModel>> response) {
                hideDialogProgressBar();
                bankNames.clear();
                bankDetailsModels.clear();
                if (response.isSuccessful() && response.body() != null) {
                    bankNames.add(getString(R.string.select_bank_account));
                    bankDetailsModels.addAll(response.body().getResponse());
                    for (BankDetailsModel bankDetailsModel : response.body().getResponse()) {
                        bankNames.add(bankDetailsModel.getName());
                        banksNamesAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<BankDetailsModel>> response) {
                hideDialogProgressBar();
                if (response.code() == 401) {
                    showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
                    if (mAlertDialog != null) {
                        mAlertDialog.setCancelable(false);
                        TextView title = mAlertDialog.findViewById(R.id.tv_title);
                        title.setText(getString(R.string.your_session_has_expired));
                        Button btnOk = mAlertDialog.findViewById(R.id.btn_cancel);
                        btnOk.setText(getString(R.string.login));
                        btnOk.setOnClickListener(v -> {
                            SessionManager.getInstance().removeUserData(MainPaymentActivity.this);
                            UIHelper.getInstance().switchActivity(MainPaymentActivity.this, StartUpActivity.class, null, null, null, true);
                        });
                    }
                    return;

                }
                showErrorPrompt(response);
            }

            @Override
            public void onNullListResponse() {
                hideDialogProgressBar();
            }

            @Override
            public void onFailureList(Throwable t) {
                hideDialogProgressBar();
                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            hideAlertDialog();
                            getBanksList();
                        }
                    });
                }

            }
        }, SessionManager.getAccessToken()).getBanksList();
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
        showLoader();
        //TODO: SIMPLIFY CHARGE

        if (local) {
            // Set transaction params directly in app (note that these params
            // are only used if an access_code is not set. In debug mode,
            // setting them after setting an access code would throw an exception


            try {
                charge.setAmount((int) paymentAmount);
                charge.setEmail(userInfo.getEmail());
                charge.setReference("ChargedFromAndroid_" + generateReference());
                charge.putCustomField("Charged From", "Android SDK");
            } catch (JSONException e) {
                hideLoader();
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
            PaystackSdk.chargeCard(MainPaymentActivity.this, charge, new Paystack.TransactionCallback() {
                // This is called only after transaction is successful
                @Override
                public void onSuccess(Transaction transaction) {
                    hideLoader();

                    MainPaymentActivity.this.transaction = transaction;
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
                    MainPaymentActivity.this.transaction = transaction;
//                Toast.makeText(MainActivity.this, transaction.getReference(), Toast.LENGTH_LONG).show();
//                updateTextViews();
                }

                @Override
                public void onError(Throwable error, Transaction transaction) {
                    // If an access code has expired, simply ask your server for a new one
                    // and restart the charge instead of displaying error
                    MainPaymentActivity.this.transaction = transaction;
                    hideLoader();
                    if (error instanceof ChargeException) {
                        showCardFailureDialog();
                        return;
                    }
                    if (error instanceof ExpiredAccessCodeException) {
                        MainPaymentActivity.this.startAFreshCharge(false);
                        return;
                    }

                    if (transaction.getReference() != null) {
                        showFailureDialog();
                        Toast.makeText(MainPaymentActivity.this, transaction.getReference() + " concluded with error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        //  mTextError.setText(String.format("%s  concluded with error: %s %s", transaction.getReference(), error.getClass().getSimpleName(), error.getMessage()));
                        //new verifyOnServer().execute(transaction.getReference());
                        verifyOnServer(transaction.getReference());
                    } else {

                        Toast.makeText(MainPaymentActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        //    mTextError.setText(String.format("Error: %s %s", error.getClass().getSimpleName(), error.getMessage()));
                    }
                    //  updateTextViews();
                }

            });

        } catch (Exception e) {
            hideLoader();
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
            } else if (paymentType == WALLET) {
                payReference.setText(getString(R.string.payment_reference, walletReference));
            }

            mAlertDialog.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
                mAlertDialog.dismiss();

            });
        }
    }


    private void showSuccessDialog(float amount, String reference) {
        if (getIntent().getIntExtra(AppConstants.K_REQUEST_CODE, 0) == AppConstants.REQ_PAYMENT) {
            Helper.removeRideId(this);
            showPaymentDialog(amount, reference);
        } else if (getIntent().getIntExtra(AppConstants.K_REQUEST_CODE, 0) == AppConstants.REQ_PAY_SHOP) {
            showPaymentDialog(amount, reference);
        } else {
            switchToNext(amount, reference);
        }

    }

    private void showPaymentDialog(float amount, String refrence) {
        hideAlertDialog();
        showAlertDialog(R.layout.dialog_payment_successful, AppConstants.NotificationType.APP_ERROR);
        if (mAlertDialog != null) {
            TextView billAmount = mAlertDialog.findViewById(R.id.tv_bill_amount);
            TextView transactionId = mAlertDialog.findViewById(R.id.tv_reference);

            //  ImageView checkMarkAnimation = mAlertDialog.findViewById(R.id.check_animation);
            //((Animatable) checkMarkAnimation.getDrawable()).start();
            transactionId.setText(refrence);
            billAmount.setText(String.valueOf(paymentAmount));

            mAlertDialog.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
                hideAlertDialog();
                switchToNext(amount, refrence);
            });
        }
    }

    private void switchToNext(float amount, String reference) {
        cancelType = 1;
        Intent intent = new Intent();

        if (paymentType == CARD) {
            intent.putExtra(AppConstants.K_PAY_AMOUNT, (int) amount);
            intent.putExtra(AppConstants.K_TRANSACTION_REFERENCE, reference);
        } else if (paymentType == WALLET) {
            intent.putExtra(AppConstants.K_PAY_AMOUNT, paymentAmount);
            intent.putExtra(AppConstants.K_TRANSACTION_REFERENCE, walletReference);
        }

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

        String cardNum = creditNumber.getText().toString().trim();

        //build card object with ONLY the number, update the other fields later
        card = new Card.Builder(cardNum, 0, 0, "").build();
        String cvc = creditCCV.getText().toString().trim();
        //update the cvc field of the card
        card.setCvc(cvc);

        //validate expiry month;
        String expiryDate = creditMonth.getText().toString().trim();
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

    //Shop Transaction
    private void saveShoppingTransaction(ShopTransactionModel shopTransactionModel) {

        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<String>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<String>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    if (shopTransactionModel.getStatus() == 1) {
                        showSuccessDialog(shopTransactionModel.getAmount(), shopTransactionModel.getReference());
                    } else {
                        showFailureDialog();
                    }

                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<String>> response) {
                hideLoading();
                showErrorPrompt(response);
            }

            @Override
            public void onNullListResponse() {
                hideLoading();
            }

            @Override
            public void onFailureList(Throwable t) {
                hideLoading();
                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        saveShoppingTransaction(shopTransactionModel);
                    });
                }
            }
        }, SessionManager.getAccessToken()).saveShoppingTransaction(shopTransactionModel);
    }

    private void verifyOnServer(String reference) {
        // showDialogProgressBar();
        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstants.K_REFERENCE, reference);
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<TransactionVerifyModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<TransactionVerifyModel>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    saveTransaction(response.body().getResponse());
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

    //TODO: CHANGE API WALLET
    private void payViaWallet() {
        walletReference = generateReference();
        paymentType = WALLET;
        showLoading();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstants.K_AMOUNT, String.valueOf(paymentAmount));
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<String>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<String>> response) {
                hideLoading();
                //TODO :// CHANGE USER RIDE DYNAMICALLY
                TransactionRefStoreModel transactionRefStoreModel = new TransactionRefStoreModel();
                transactionRefStoreModel.setTransactionId(walletReference);
                transactionRefStoreModel.setTransactionDate(String.valueOf(System.currentTimeMillis() + 1000));
                transactionRefStoreModel.setStatus(1);
                transactionRefStoreModel.setRidePostedByDriver(currentRideType);
                transactionRefStoreModel.setRideId(currentRideId);
                transactionRefStoreModel.setPaymentOption(paymentType);
                transactionRefStoreModel.setCurrency(AppConstants.CURRENCY_NIGERIA);
                transactionRefStoreModel.setAmount(paymentAmount);

                if (getIntent().getIntExtra(AppConstants.K_REQUEST_CODE, 0) == AppConstants.REQ_PAY_SHOP) {
                    TransactionVerifyModel transactionVerifyModel = new TransactionVerifyModel();
                    transactionVerifyModel.setReference(walletReference);
                    transactionVerifyModel.setCurrency(AppConstants.CURRENCY_NIGERIA);
                    transactionVerifyModel.setAmount((int) paymentAmount);
                    transactionVerifyModel.setTransactionDate(transactionRefStoreModel.getTransactionDate());
                    transactionVerifyModel.setStatus(AppConstants.SUCCESS);
                    getCartForShoppingTransaction(transactionVerifyModel);
                } else {
                    saveTransaction(transactionRefStoreModel);
                }


            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<String>> response) {
                hideLoading();
                showErrorPrompt(response);
                //  showFailureDialog();
            }

            @Override
            public void onNullListResponse() {
                hideLoader();
            }

            @Override
            public void onFailureList(Throwable t) {
                hideLoading();
                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        payViaWallet();
                    });
                }
            }
        }, SessionManager.getAccessToken()).payViaWallet(hashMap);
    }

    private void getWalletMoney() {
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<WalletModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<WalletModel>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    mWalletBalance.setText(getString(R.string.available_balance, response.body().getResponse().getBalance()));
                }
            }

            @Override
            public void onError(Response<QualStandardResponse<WalletModel>> response) {
                hideDialogProgressBar();
                mWalletBalance.setText(getString(R.string.available_balance, "0.00"));
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
                        getWalletMoney();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getWalletMoney();
    }

    private void saveTransaction(SavedCardTVerifyModel savedCardTVerifyModel) {
        TransactionVerifyModel transactionVerifyModel = new TransactionVerifyModel();
        transactionVerifyModel.setAmount(savedCardTVerifyModel.getAmount());
        transactionVerifyModel.setCurrency(savedCardTVerifyModel.getCurrency());
        transactionVerifyModel.setStatus(savedCardTVerifyModel.getStatus());
        transactionVerifyModel.setReference(savedCardTVerifyModel.getReference());
        transactionVerifyModel.setTransactionDate(savedCardTVerifyModel.getTransactionDate());
        saveTransaction(transactionVerifyModel);
    }

    private void saveTransaction(TransactionVerifyModel transactionVerifyModel) {
        if (getIntent().getIntExtra(AppConstants.K_REQUEST_CODE, 0) == AppConstants.REQ_PAY_SHOP) {
            getCartForShoppingTransaction(transactionVerifyModel);
        } else if (getIntent().getIntExtra(AppConstants.K_REQUEST_CODE, 0) == AppConstants.REQ_ADD_MONEY) {
            showSuccessDialog(transactionVerifyModel.getAmount(), transactionVerifyModel.getReference());
        } else {
            //Other Transactions
            TransactionRefStoreModel transactionRefStoreModel = new TransactionRefStoreModel();
            try {

                transactionRefStoreModel.setAmount(transactionVerifyModel.getAmount());
                transactionRefStoreModel.setCurrency(transactionVerifyModel.getCurrency());
                transactionRefStoreModel.setPaymentOption(paymentType);
                transactionRefStoreModel.setRideId(currentRideId);
                transactionRefStoreModel.setRidePostedByDriver(currentRideType);
                if (transactionVerifyModel.getStatus().equalsIgnoreCase(AppConstants.SUCCESS)) {
                    transactionRefStoreModel.setStatus(1);
                } else {
                    transactionRefStoreModel.setStatus(0);
                }

                transactionRefStoreModel.setTransactionId(transactionVerifyModel.getReference());
                transactionRefStoreModel.setTransactionDate(transactionVerifyModel.getTransactionDate());

            } catch (Exception e) {
                transactionRefStoreModel.setStatus(0);
            }
            saveTransaction(transactionRefStoreModel);


        }

    }

    private void getCartForShoppingTransaction(TransactionVerifyModel transactionVerifyModel) {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<CheckOutModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<CheckOutModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ShopTransactionModel shopTransactionModel = new ShopTransactionModel();
                    shopTransactionModel.setAmount(transactionVerifyModel.getAmount());
                    shopTransactionModel.setReference(transactionVerifyModel.getReference());
                    shopTransactionModel.setProducts(response.body().getResponse().getData());
                    shopTransactionModel.setTransactionDate(transactionVerifyModel.getTransactionDate());
                    shopTransactionModel.setCurrency(transactionVerifyModel.getCurrency());
                    shopTransactionModel.setPaymentOption(paymentType);
                    shopTransactionModel.setTransactionId(transactionVerifyModel.getReference());
                    shopTransactionModel.setShippingAddressId(getIntent().getIntExtra(AppConstants.K_SHIPPING_ADDRESS_ID, 0));
                    if (transactionVerifyModel.getStatus().equalsIgnoreCase(AppConstants.SUCCESS)) {
                        shopTransactionModel.setStatus(1);
                    } else {
                        shopTransactionModel.setStatus(0);
                    }

                    saveShoppingTransaction(shopTransactionModel);
                }


            }

            @Override
            public void onError(Response<QualStandardResponse<CheckOutModel>> response) {
                hideLoading();
                showErrorPrompt(response);
            }

            @Override
            public void onNullResponse() {
                hideLoading();
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoading();
                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(view -> {
                        hideAlertDialog();
                        getCartForShoppingTransaction(transactionVerifyModel);
                    });
                }
            }
        }, SessionManager.getAccessToken()).getCart();
    }

    private void chargeAuthorization(String authorizationCode) {
        showDialogProgressBar();
        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstants.K_EMAIL, userInfo.getEmail());
        params.put(AppConstants.K_AMOUNT, String.valueOf(paymentAmount));
        params.put(AppConstants.K_AUTHORIZATION_CODE, authorizationCode);
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<SavedCardTVerifyModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<SavedCardTVerifyModel>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    saveTransaction(response.body().getResponse());
                }
            }

            @Override
            public void onError(Response<QualStandardResponse<SavedCardTVerifyModel>> response) {
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
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(view -> {
                        hideAlertDialog();
                        chargeAuthorization(authorizationCode);
                    });
                }
            }
        }, SessionManager.getAccessToken()).chargeAuthorization(params);
    }

    private void getSavedCards() {
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<Authorization>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<Authorization>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        for (Authorization authorization : response.body().getResponse()) {
                            if (authorization.getCardType().contains(AppConstants.CARD_TYPE.VISA)) {
                                authorization.setCardImage(getResources().getDrawable(R.drawable.visa));
                            } else if (authorization.getCardType().contains(AppConstants.CARD_TYPE.AMERICAN_EXPRESS)) {
                                authorization.setCardImage(getResources().getDrawable(R.drawable.american_express));
                            } else if (authorization.getCardType().contains(AppConstants.CARD_TYPE.DISCOVER)) {
                                authorization.setCardImage(getResources().getDrawable(R.drawable.discover));
                            } else if (authorization.getCardType().contains(AppConstants.CARD_TYPE.MASTER_CARD)) {
                                authorization.setCardImage(getResources().getDrawable(R.drawable.mastercard));
                            }
                            savedCards.add(authorization);
                        }
                    } catch (Exception e) {

                    }
                    savedCardListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<Authorization>> response) {
                hideLoading();
                showErrorPrompt(response);
            }

            @Override
            public void onNullListResponse() {
                hideLoading();
            }

            @Override
            public void onFailureList(Throwable t) {
                hideLoading();

            }
        }, SessionManager.getAccessToken()).getSavedCards();
    }

    //TODO: CHANGE API PARAMETERS STATUS, RIDE ID
    private void saveTransaction(TransactionRefStoreModel transactionRefStoreModel) {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<String>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<String>> response) {
                hideLoading();
                if (transactionRefStoreModel.getStatus() == 1) {
                    showSuccessDialog(transactionRefStoreModel.getAmount(), transactionRefStoreModel.getTransactionId());
                } else {
                    showFailureDialog();

                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<String>> response) {
                showErrorPrompt(response);
                hideLoading();
            }

            @Override
            public void onNullListResponse() {
                hideLoading();
            }

            @Override
            public void onFailureList(Throwable t) {
                hideLoading();
            }
        }, SessionManager.getAccessToken()).saveTransaction(transactionRefStoreModel);
    }

    private void payWithBank() {
        paymentType = BANK;
        Intent bankPaymentIntent = new Intent(this, BankPaymentActivity.class);
        bankPaymentIntent.putExtra(AppConstants.K_BANK_NAME, bankDetailsModels.get(selectedBankPosition).getName());
        bankPaymentIntent.putExtra(AppConstants.K_ACCOUNT_NUMBER, mAccountNumber.getText().toString().trim());
        bankPaymentIntent.putExtra(AppConstants.K_BANK_CODE, bankDetailsModels.get(selectedBankPosition).getCode());
        bankPaymentIntent.putExtra(AppConstants.K_AMOUNT, String.valueOf(paymentAmount));
        bankPaymentIntent.putExtra(AppConstants.K_BIRTHDAY, mAccountHolderBirthday.getText().toString().trim());
        startActivityForResult(bankPaymentIntent, AppConstants.REQ_BANK_PAYMENT);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.REQ_BANK_PAYMENT) {
            if (resultCode == RESULT_OK && data != null) {
                verifyOnServer(data.getStringExtra(AppConstants.K_REFERENCE));
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (cancelType == 2) {
            showAlertDialog(R.layout.dialog_cancel_transaction, AppConstants.NotificationType.CANCEL_TRANSACTION);
            if (mAlertDialog != null) {
                mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> MainPaymentActivity.super.onBackPressed());
            }
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectedBankPosition = position - 1;
        banksNameList.setText(bankNames.get(position));
        banksListPopUp.dismiss();

    }

    @Override
    public void onDateSelected(int year, int month, int dayOfMonth) {
        String sday = String.valueOf(dayOfMonth);
        String smonth = String.valueOf(month);
        if (dayOfMonth < 10) {
            sday = "0" + sday;
        }
        if (month < 10) {
            smonth = "0" + smonth;
        }
        String date = year + "-" + smonth + "-" + sday;
        mAccountHolderBirthday.setText(date);

//        Calendar myCalendar = Calendar.getInstance();
//        myCalendar.set(Calendar.YEAR, year);
//        myCalendar.set(Calendar.MONTH, month);
//        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//
//        mAccountHolderBirthday.setText(Timing.getTimeInString(myCalendar.getTimeInMillis(), Timing.TimeFormats.DD_MMMM_YYYY));
    }

    @Override
    public void onAdapterItemClick(View v, int position) {
        if (v.getId() == R.id.btn_pay) {
            chargeAuthorization(savedCards.get(position).getAuthorizationCode());
        }
    }

    @Override
    public void onAdapterItemClick(Place place) {

    }
}
