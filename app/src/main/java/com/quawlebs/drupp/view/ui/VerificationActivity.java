package com.quawlebs.drupp.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mukesh.OtpView;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.models.LoginDataModel;
import com.quawlebs.drupp.models.UserInfo;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.util.UiUtils;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;
import com.quawlebs.drupp.view.ui.ride.BottomSheetRideActivity;

import java.util.concurrent.TimeUnit;

import static com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks;
import static com.google.firebase.auth.PhoneAuthProvider.getInstance;

public class VerificationActivity extends MainBaseActivity {

    private UserInfo userInfo;
    private LoginDataModel loginDataModel;
    private FirebaseAuth mAuth;
    private OtpView otpView;
    private String verificationId;
    private TextView tvResend;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        mAuth = FirebaseAuth.getInstance();
        otpView = findViewById(R.id.otp_view);
        tvResend = findViewById(R.id.tvResend);
        if (getIntent() != null) {
            loginDataModel = ((LoginDataModel) getIntent().getSerializableExtra(AppConstants.K_USER_INFO));
            if (loginDataModel == null) {
                LoginDataModel loginDataModel = SessionManager.getInstance().getUserModel();
                userInfo = loginDataModel.getUserInfo();
            } else {
                userInfo = loginDataModel.getUserInfo();
            }

        }

        if (userInfo != null) {
            ((TextView) findViewById(R.id.tvVerific)).setText(getString(R.string.tv_please_enter_otp, userInfo.getCountryCode(), userInfo.getPhone()));
        }

        findViewById(R.id.bt_submit).setOnClickListener(v -> {
            Editable temp = otpView.getText();
            if (temp == null)
                UiUtils.showToast(getString(R.string.enter_verification_code_first));
            else {
                String code = temp.toString();
                if (code.isEmpty())
                    UiUtils.showToast(getString(R.string.enter_verification_code_first));
                else
                    verifyOtp(code);
            }
        });
        otpView.setOtpCompletionListener(this::verifyOtp);
        tvResend.setOnClickListener(view -> sendOtp());
        sendOtp();
    }


    private void sendOtp() {
        UserInfo temp = loginDataModel.getUserInfo();
        if (temp == null) return;
        showLoading();
        String number = temp.getFullNumber();
        getInstance().verifyPhoneNumber(
                number,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                getCallBacks());        // OnVerificationStateChangedCallbacks
    }

    private OnVerificationStateChangedCallbacks getCallBacks() {
        return new OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                showLoading();
                proceedWithCredentials(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                hideLoading();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    UiUtils.showLongToast(e.getLocalizedMessage());
                } else {
                    UiUtils.showLongToast(getString(R.string.msg_sms_exceeeded));
                }

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                hideLoading();
                verificationId = s;
                UiUtils.showToast(getString(R.string.code_has_been_sent_successfully));
                startResendTime();
            }
        };
    }

    private void verifyOtp(String code) {
        try {
            showLoading();
            proceedWithCredentials(PhoneAuthProvider.getCredential(verificationId, code));
        } catch (Exception ex) {
            hideLoading();
            ex.printStackTrace();
            UiUtils.showToast(ex.getLocalizedMessage());
        }

    }

    private void proceedWithCredentials(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    hideLoading();
                    if (task.isSuccessful()) {
                        Snackbar.make(otpView, getString(R.string.number_verification_successful), Snackbar.LENGTH_LONG).show();
                        new Handler().postDelayed(this::switchToNext, 800);
                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            UiUtils.showLongToast(getString(R.string.invalid_verification_code));
                        } else {
                            UiUtils.showLongToast(
                                    getString(R.string.failed_to_verify_code)
                            );
                        }
                    }
                });

    }

    private void startResendTime() {
        tvResend.setText(null);
        tvResend.setEnabled(false);
        new CountDownTimer(TimeUnit.MINUTES.toMillis(1), TimeUnit.SECONDS.toMillis(1)) {
            @Override
            public void onTick(long millisLeft) {
                long tempMin = TimeUnit.MILLISECONDS.toMinutes(millisLeft);
                long tempSec = TimeUnit.MILLISECONDS.toSeconds(millisLeft);
                String minutes = tempMin < 10 ? "0" + tempMin : String.valueOf(tempMin);
                tvResend.setText(
                        new StringBuffer()
                                .append(minutes)
                                .append(":")
                                .append(tempSec)
                );
            }

            @Override
            public void onFinish() {
                tvResend.setEnabled(true);
                tvResend.setText(getString(R.string.resend_code));
            }
        }.start();
    }

    private void switchToNext() {
        if (loginDataModel.getToken() != null && !loginDataModel.getToken().isEmpty()) {
            UIHelper.getInstance().switchActivity(VerificationActivity.this, BottomSheetRideActivity.class, null, null, null, true);
        } else {
            Intent intent = new Intent(VerificationActivity.this, UserRegistrationActivity.class);
            intent.putExtra(AppConstants.K_USER_INFO, loginDataModel);
            startActivity(intent);
            finish();
        }
    }


}
