package com.quawlebs.drupp.view.ui.payments;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.IActivityInteractor;
import com.quawlebs.drupp.helpers.IPaymentFlowObserver;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.view.ui.base.MainBaseFragment;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class OtpFragment extends MainBaseFragment implements IPaymentFlowObserver {
    @BindView(R.id.tv_reference_payment)
    TextView mReference;
    @BindView(R.id.tv_bank_name)
    TextView mBankName;
    @BindView(R.id.tv_otp_view)
    TextView mOtpView;
    @BindView(R.id.et_otp_code)
    EditText mOTP;
//    @BindView(R.id.btn_submit)
//    Button mSubmit;

    private IActivityInteractor iActivityInteractor;
    private BankPaymentActivity.PaymentSubject paymentSubject;

    @Override
    protected void initViewsForFragment(View view) {
        iActivityInteractor = (BankPaymentActivity) getmActivity();
        paymentSubject = (BankPaymentActivity.PaymentSubject) iActivityInteractor.getSubject();
        paymentSubject.registerObserver(this);
        if (getArguments() != null) {
            mBankName.setText(getArguments().getString(AppConstants.K_BANK_NAME));
            mOtpView.setText(getArguments().getString(AppConstants.K_DISPLAY_TEXT));
            mReference.setText(getArguments().getString(AppConstants.K_REFERENCE));

        }
    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.otp_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_submit)
    public void onSubmit() {
        showLoading();
        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstants.K_REFERENCE, mReference.getText().toString().trim());
        params.put(AppConstants.K_OTP, mOTP.getText().toString().trim());
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<HashMap<String, String>>() {
            @Override
            public void onResponse(Response<QualStandardResponse<HashMap<String, String>>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResponse().get(AppConstants.K_STATUS).contains(AppConstants.BANK_STATUS.SEND_OTP)) {
                        onSubmit();
                    } else {
                        //Successful transaction
                        Intent transactionData = new Intent();
                        transactionData.putExtra(AppConstants.K_STATUS, response.body().getResponse().get(AppConstants.K_STATUS));
                        transactionData.putExtra(AppConstants.K_CURRENCY, response.body().getResponse().get(AppConstants.K_CURRENCY));
                        transactionData.putExtra(AppConstants.K_AMOUNT, response.body().getResponse().get(AppConstants.K_AMOUNT));
                        transactionData.putExtra(AppConstants.K_REFERENCE, response.body().getResponse().get(AppConstants.K_REFERENCE));
                        transactionData.putExtra(AppConstants.K_TRANSACTION_DATE, response.body().getResponse().get(AppConstants.K_TRANSACTION_DATE));

                        getmActivity().setResult(Activity.RESULT_OK, transactionData);
                        getmActivity().finish();
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
                        onSubmit();
                    });
                }
            }
        }, SessionManager.getAccessToken()).submitOTP(params);
    }


    @Override
    public void onResponse(HashMap<String, String> response) {
        //wwwh HashMap<String, String> otpReponse = response.get(AppConstants.K_DATA);
    }

    @Override
    public void showAlertDialog(int resId) {

    }
}
