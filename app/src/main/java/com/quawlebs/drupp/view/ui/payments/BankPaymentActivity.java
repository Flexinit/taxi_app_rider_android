package com.quawlebs.drupp.view.ui.payments;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.quawlebs.drupp.models.UserInfo;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.IActivityInteractor;
import com.quawlebs.drupp.helpers.IPaymentFlowObserver;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Response;

public class BankPaymentActivity extends MainBaseActivity implements IActivityInteractor {

    private HashMap<String, String> params = new HashMap<>();
    private UserInfo userInfo;
    private PaymentSubject paymentSubject;
    private String bankName = "";

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_payment_layout);
        userInfo = SessionManager.getInstance().getUserModel().getUserInfo();
        if (getIntent() != null) {
            bankName = getIntent().getStringExtra(AppConstants.K_BANK_NAME);
            params.put(AppConstants.K_EMAIL, userInfo.getEmail());
            params.put(AppConstants.K_ACCOUNT_NUMBER, getIntent().getStringExtra(AppConstants.K_ACCOUNT_NUMBER));
            params.put(AppConstants.K_BANK_CODE, getIntent().getStringExtra(AppConstants.K_BANK_CODE));
            params.put(AppConstants.K_AMOUNT, getIntent().getStringExtra(AppConstants.K_AMOUNT));
            params.put(AppConstants.K_BIRTHDAY, getIntent().getStringExtra(AppConstants.K_BIRTHDAY));

        }
        paymentSubject = new PaymentSubject();
        PendingFragment pendingFragment = new PendingFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.K_BANK_NAME, bankName);
        pendingFragment.setArguments(bundle);
        paymentSubject.registerObserver(pendingFragment);


        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_payment_flow, pendingFragment, PendingFragment.class.getSimpleName())
                .commit();

    }

    @Override
    public void doNetworkRequest() {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<HashMap<String, String>>() {
            @Override
            public void onResponse(Response<QualStandardResponse<HashMap<String, String>>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        if (response.body().getResponse().get(AppConstants.K_STATUS).contains(AppConstants.BANK_STATUS.SEND_OTP)) {
                            paymentSubject.onApiReponse(response.body().getResponse());
                        }
                    } catch (Exception ignored) {

                    }

                }
            }

            @Override
            public void onError(Response<QualStandardResponse<HashMap<String, String>>> response) {
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
                        doNetworkRequest();
                    });
                }
            }
        }, SessionManager.getAccessToken()).payWithBank(params);

    }

    @Override
    public PaymentSubject getSubject() {
        if (paymentSubject != null) {
            return paymentSubject;
        }
        return new PaymentSubject();
    }

    class PaymentSubject {
        private List<IPaymentFlowObserver> iPaymentFlowObserverList = new ArrayList<>();

        public void registerObserver(IPaymentFlowObserver paymentFlowObserver) {
            iPaymentFlowObserverList.add(paymentFlowObserver);
        }

        public void unregisterObserver(IPaymentFlowObserver paymentFlowObserver) {
            iPaymentFlowObserverList.remove(paymentFlowObserver);
        }

        public void notifyObservers(HashMap<String, String> data) {
            for (final IPaymentFlowObserver iPaymentFlowObserver : iPaymentFlowObserverList) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        iPaymentFlowObserver.onResponse(data);
                    }
                }).start();
            }
        }

        public void onApiReponse(HashMap<String, String> response) {
            notifyObservers(response);
        }
    }

}
