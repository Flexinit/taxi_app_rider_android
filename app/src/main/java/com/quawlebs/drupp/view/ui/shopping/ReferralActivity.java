package com.quawlebs.drupp.view.ui.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetworkReferral;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.models.LoginDataModel;
import com.quawlebs.drupp.models.ReferModel;
import com.quawlebs.drupp.view.ui.ReferralHistory;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;

import java.util.HashMap;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class ReferralActivity extends MainBaseActivity {

    @Override
    protected void setUp() {

    }

    TextView txtCouponCode;

   @OnClick(R.id.lnrshare)
   public void onClickShare(){
       Intent intent = new Intent(android.content.Intent.ACTION_SEND);
       /*This will be the actual content you wish you share.*/
       String shareBody = "Download application by using "+txtCouponCode.getText().toString()+" code";
       /*The type of the content is text, obviously.*/
       intent.setType("text/plain");
       /*Applying information Subject and Body.*/
       intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share));
       intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
       /*Fire!*/
       startActivity(Intent.createChooser(intent, getString(R.string.share_via)));
   }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.referral_activity);

        ButterKnife.bind(this);

        LoginDataModel userInfo = SessionManager.getInstance().loadUser(this);
        getCoponCode(userInfo.getUserInfo().getId());
        //Set UP Toolbar

        txtCouponCode = findViewById(R.id.txtCouponCode);

        findViewById(R.id.image_back).setOnClickListener(v -> onBackPressed());

        findViewById(R.id.txtReferredHistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReferralActivity.this, ReferralHistory.class));

            }
        });


    }

    @Override
    public void onBackPressed() {
//        Fragment fragment = getSupportFragmentManager().findFragmentByTag()
        super.onBackPressed();
    }

//    private void getCoponCode(int id) {
//        showLoading();
//        HashMap<String, String> params = new HashMap<>();
//
//        params.put(AppConstants.Q_ID,String.valueOf(id));
//
//
//
//        DruppRequestHandler.clearInstance();
//
//        DruppRequestHandler.getInstance(new INetworkList<String>() {
//            @Override
//            public void onResponseList(Response<QualStandardResponseList<String>> response) {
//                hideLoading();
//                if (response.isSuccessful() && response.body() != null) {
//                    try {
//
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//
//            @Override
//            public void onErrorList(Response<QualStandardResponseList<String>> response) {
//                hideLoading();
//                if (response.code() == 401) {
//                    showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
//                    if (mAlertDialog != null) {
//                        mAlertDialog.setCancelable(false);
//                        TextView title = mAlertDialog.findViewById(R.id.tv_title);
//                        title.setText(getString(R.string.your_session_has_expired));
//                        Button btnOk = mAlertDialog.findViewById(R.id.btn_cancel);
//                        btnOk.setText(getString(R.string.login));
//                        btnOk.setOnClickListener(v -> {
//                            SessionManager.getInstance().removeUserData(ReferralActivity.this);
//                            UIHelper.getInstance().switchActivity(ReferralActivity.this, SplashActivity.class, null, null, null, true);
//                        });
//                    }
//                    return;
//
//                }
//                showErrorPrompt(response);
//            }
//
//            @Override
//            public void onNullListResponse() {
//                hideLoading();
//            }
//
//            @Override
//            public void onFailureList(Throwable t) {
//                hideLoading();
//                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
//                if (mAlertDialog != null) {
//                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
//                        hideAlertDialog();
//
//                    });
//                }
//
//            }
//        }, SessionManager.getAccessToken()).generate_coupon(params);
//
//    }


    private void getCoponCode(int id) {

        showLoading();
        DruppRequestHandler.clearInstance();

        HashMap<String, String> params = new HashMap<>();

        params.put(AppConstants.Q_ID, String.valueOf(id));

        DruppRequestHandler.getInstanceReferal(new INetworkReferral<String>() {
            @Override
            public void onResponse(Response<ReferModel> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {

                    ReferModel.response referModel = response.body().getResponse();

                    if (referModel.getData().size() != 0) {

                        txtCouponCode.setText(referModel.getData().get(0).getCoupon_code());
                    }

                }
            }

            @Override
            public void onError(Response<ReferModel> response) {
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

                showErrorPrompt(t.getMessage());

                //   showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
//                if (mAlertDialog != null) {
//                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
//                        hideAlertDialog();
//
//                    });
//                }

            }
        }, SessionManager.getAccessToken()).generate_coupon(params);
    }

}
