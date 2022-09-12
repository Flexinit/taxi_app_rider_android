package com.quawlebs.drupp.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.quawlebs.drupp.models.WalletModel;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.view.ui.base.BaseActivity;
import com.quawlebs.drupp.view.ui.payments.ActivityAddMoney;
import com.quawlebs.drupp.view.ui.payments.PaymentCashActivity;

import retrofit2.Response;

public class PaymentOptions extends BaseActivity {
    private TextView mAvailBalance;
    private String walletBalance = "0";

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_payment);
        setToolbarTitle(R.string.payments);
        getToolbarBack().setOnClickListener(v -> onBackPressed());
        mAvailBalance = findViewById(R.id.tv_avail_balance);
        findViewById(R.id.ll_add_card).setOnClickListener(v -> {

            Intent intent = new Intent(PaymentOptions.this, CardActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_drupp_wallet).setOnClickListener(v -> UIHelper.getInstance().switchActivity(PaymentOptions.this, ActivityAddMoney.class, null, walletBalance, AppConstants.K_WALLET_BALANCE, false));
        findViewById(R.id.ll_cash).setOnClickListener(v -> UIHelper.getInstance().switchActivity(PaymentOptions.this, PaymentCashActivity.class, null, null, null, false));

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_emergancy, null);
        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(PaymentOptions.this);
        final AlertDialog alertDialog = alertDialogbuilder.create();
        alertDialog.setView(alertLayout);


    }

    @Override
    protected void onResume() {
        super.onResume();
        getWalletMoney();

    }

    private void getWalletMoney() {
        try {
            DruppRequestHandler.getInstance(new INetwork<WalletModel>() {
                @Override
                public void onResponse(Response<QualStandardResponse<WalletModel>> response) {
                    hideDialogProgressBar();
                    if (response.isSuccessful() && response.body() != null) {
                        walletBalance = response.body().getResponse().getBalance();
                        mAvailBalance.setText(getString(R.string.wallet_balance, response.body().getResponse().getBalance()));
                    }
                }

                @Override
                public void onError(Response<QualStandardResponse<WalletModel>> response) {
                    hideDialogProgressBar();
                    mAvailBalance.setText(getString(R.string.wallet_balance, "0.00"));
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
        } catch (Exception e) {

        }

    }
}
