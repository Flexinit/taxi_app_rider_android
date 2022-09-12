package com.quawlebs.drupp.view.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetworkReferralHistory;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.models.CouponTransactionModel;
import com.quawlebs.drupp.models.LoginDataModel;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;

import java.util.HashMap;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class ReferralHistory extends MainBaseActivity {

    @Override
    protected void setUp() {

    }


   @BindView(R.id.tv_noData)
   TextView tv_noData;


    @BindView(R.id.lnrMainView)
    LinearLayout lnrMainView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.referral_history);

        ButterKnife.bind(this);

        LoginDataModel userInfo = SessionManager.getInstance().loadUser(this);
        getCoponCode(userInfo.getUserInfo().getId());
        //Set UP Toolbar

        findViewById(R.id.image_back).setOnClickListener(v -> onBackPressed());

    }

    @Override
    public void onBackPressed() {
//        Fragment fragment = getSupportFragmentManager().findFragmentByTag()
        super.onBackPressed();
    }






    private void getCoponCode(int id) {

        showLoading();
        DruppRequestHandler.clearInstance();

        HashMap<String, String> params = new HashMap<>();

        params.put(AppConstants.Q_ID, String.valueOf(id));

        DruppRequestHandler.getInstanceReferalHistory(new INetworkReferralHistory<String>() {
            @Override
            public void onResponse(Response<CouponTransactionModel> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {

                    com.quawlebs.drupp.models.Response referModel = response.body().getResponse();

                    if(referModel.getData().size()!=0){
                        lnrMainView.setVisibility(View.VISIBLE);
                        tv_noData.setVisibility(View.GONE);

                        for (int i=0;i<referModel.getData().size();i++){
                            LayoutInflater inflater = getLayoutInflater();
                            View view= inflater.inflate(R.layout.custom_coupon_history,null);
                            TextView txt1 =  view.findViewById(R.id.txt1);
                            TextView txt2 =  view.findViewById(R.id.txt2);
                            TextView txt3 =  view.findViewById(R.id.txt3);
                            TextView txt4 =  view.findViewById(R.id.txt4);

                            txt1.setText(referModel.getData().get(i).getU1id().toString());
                            txt2.setText(referModel.getData().get(i).getCouponNumber());
                            txt3.setText(referModel.getData().get(i).getConnectedUser());
                            txt4.setText(referModel.getData().get(i).getDate());
                            lnrMainView.addView(view);
                        }
                    }
                    else {
                        lnrMainView.setVisibility(View.GONE);
                        tv_noData.setVisibility(View.VISIBLE);
                    }

//                    if (referModel.size() != 0) {
//
//                        txtCouponCode.setText(referModel.getData().get(0).getCoupon_code());
//                    }

                }
            }

            @Override
            public void onError(Response<CouponTransactionModel> response) {
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
        }, SessionManager.getAccessToken()).coupon_history(params);
    }

}
