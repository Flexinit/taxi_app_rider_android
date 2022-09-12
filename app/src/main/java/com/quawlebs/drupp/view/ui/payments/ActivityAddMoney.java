package com.quawlebs.drupp.view.ui.payments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.quawlebs.drupp.login.StartUpActivity;
import com.quawlebs.drupp.models.WalletModel;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;

import java.util.HashMap;

import retrofit2.Response;

public class ActivityAddMoney extends MainBaseActivity {
    private TextView mWalletBalance;
    private EditText mWalletAmount;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_wallet_money);

        mWalletBalance = findViewById(R.id.tv_wallet_balance);
        mWalletAmount = findViewById(R.id.et_wallet_amount);
        if (getIntent() != null) {
            mWalletBalance.setText(getString(R.string.available_balance, getIntent().getStringExtra(AppConstants.K_WALLET_BALANCE)));
        }

        findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.btn_add_money).setOnClickListener(v -> {
            if (mWalletAmount.getText().toString().trim().isEmpty()) {
                showMessage(R.string.please_enter_valid_amount);
                return;
            }

            Intent intentAddMoney = new Intent(this, MainPaymentActivity.class);
            intentAddMoney.setAction(AppConstants.U_ADD_MONEY_TO_WALLET);
            intentAddMoney.putExtra(AppConstants.K_REQUEST_CODE, AppConstants.REQ_ADD_MONEY);
            intentAddMoney.putExtra(AppConstants.K_PAY_AMOUNT, Float.parseFloat(mWalletAmount.getText().toString().trim()));
            startActivityForResult(intentAddMoney, AppConstants.REQ_ADD_MONEY);

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.REQ_ADD_MONEY && resultCode == RESULT_OK) {
            if (data != null) {
                addMoneyToWallet(data.getIntExtra(AppConstants.K_PAY_AMOUNT, 0));
            }
        }
    }

    private void addMoneyToWallet(int amount) {
        showDialogProgressBar();
        HashMap<String, String> moneyToAdd = new HashMap<>();
        moneyToAdd.put(AppConstants.K_AMOUNT, String.valueOf(amount));
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<WalletModel>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<WalletModel>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    showMessage(R.string.money_added_successfully);
                    onBackPressed();
                    //getWalletMoney();

                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<WalletModel>> response) {
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
                            SessionManager.getInstance().removeUserData(ActivityAddMoney.this);
                            UIHelper.getInstance().switchActivity(ActivityAddMoney.this, StartUpActivity.class, null, null, null, true);
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
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {

                        hideAlertDialog();
                        addMoneyToWallet(amount);
                    });
                }
            }

        }, SessionManager.getAccessToken()).addMoneyToWallet(moneyToAdd);
    }

    private void getWalletMoney() {
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<WalletModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<WalletModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mWalletBalance.setText(getString(R.string.available_balance, response.body().getResponse().getBalance()));
                }
            }

            @Override
            public void onError(Response<QualStandardResponse<WalletModel>> response) {
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
                        getWalletMoney();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getWalletMoney();
    }
}
