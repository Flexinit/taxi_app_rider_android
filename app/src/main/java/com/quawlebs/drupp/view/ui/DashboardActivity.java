package com.quawlebs.drupp.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.quawlebs.drupp.connectivity.http.INetworkCountList;
import com.quawlebs.drupp.models.DashboardInfoModel;
import com.quawlebs.drupp.models.DriverRiderCountModel;
import com.quawlebs.drupp.models.LoginDataModel;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;
import com.quawlebs.drupp.view.ui.favorites.SavedLocationActivity;
import com.quawlebs.drupp.view.ui.scheduledRides.ScheduledRidesActivity;
import com.quawlebs.drupp.view.ui.shopping.ReferralActivity;

import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

public class DashboardActivity extends MainBaseActivity {

    @BindView(R.id.iv_profile_image)
    CircleImageView mProfile;
    @BindView(R.id.tv_name)
    TextView mUserName;
    @BindView(R.id.tv_email_id)
    TextView mEmailId;
    @BindView(R.id.tv_wallet_balance)
    TextView mWalletBalance;

    @BindView(R.id.tv_scheduled_rides)
    TextView mScheduledRides;

    @BindView(R.id.total_rides_numbers)
    TextView totalRidesNumbers;

    @BindView(R.id.cancelledRidesCount)
    TextView cancelledRidesCount;

    @BindView(R.id.completed_rides_numbers)
    TextView completedRidesNumbers;

    @BindView(R.id.tv_title)
    TextView toolbarTitle;


    private LoginDataModel userInfo;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);
        ButterKnife.bind(this);
        toolbarTitle.setText(getString(R.string.dashboard));
        userInfo = SessionManager.getInstance().loadUser(this);

        getUserDashboard();

//        LoginDataModel userInfo = SessionManager.getInstance().loadUser(this);
//       getCoponCode(userInfo.getUserInfo().getId());
    }


    @OnClick(R.id.iv_back)
    public void onBackPress() {
        onBackPressed();
    }

    @OnClick(R.id.card_saved_locations)
    public void onSavedLocationClick() {
        UIHelper.getInstance().switchActivity(this, SavedLocationActivity.class, null, null, null, false);
    }

    @OnClick(R.id.cardReferral)
    public void onclickCardeferral() {
        startActivity(new Intent(this, ReferralActivity.class));
    }

    @OnClick(R.id.container_wallet)
    public void onWalletClick() {
        UIHelper.getInstance().switchActivity(this, PaymentOptions.class, null, null, null, false);
    }

    @OnClick(R.id.container_scheduled_rides)
    public void onScheduledRidesClick() {
        UIHelper.getInstance().switchActivity(this, ScheduledRidesActivity.class, null, null, null, false);
    }

//    @OnClick(R.id.container_complete_rides)
//    public void onCompletedRidesClick() {
//        UIHelper.getInstance().switchActivity(this, TripHistory.class, null, null, null, false);
//    }

    private void getUserDashboard() {

        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<DashboardInfoModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<DashboardInfoModel>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        Glide.with(DashboardActivity.this).load(AppConstants.IMAGE_URL + response.body().getResponse().getUserDetails().getProfilePicture())
                                .apply(new RequestOptions()
                                        .error(R.drawable.ic_single_user)
                                        .centerCrop()
                                        .placeholder(R.drawable.ic_single_user)).into(mProfile);
                        mUserName.setText(response.body().getResponse().getUserDetails().getName());
                        mWalletBalance.setText(getString(R.string.price_in_naira, response.body().getResponse().getUserDetails().getWalletBalance()));

                        mScheduledRides.setText(String.format(Locale.getDefault(), "%d", response.body().getResponse().getScheduledRides()));
                        completedRidesNumbers.setText(String.format(Locale.getDefault(), "%d", response.body().getResponse().getCompletedRides()));
                        cancelledRidesCount.setText(String.format(Locale.getDefault(), "%d", response.body().getResponse().getCancelledRides()));
                        totalRidesNumbers.setText(String.format(Locale.getDefault(), "%d", response.body().getResponse().getTotalRides()));

                        mEmailId.setText(userInfo.getUserInfo().getEmail());
                    } catch (Exception ignored) {

                    }

                }
            }

            @Override
            public void onError(Response<QualStandardResponse<DashboardInfoModel>> response) {
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
                if (userInfo != null) {
                    mUserName.setText(getString(R.string.full_name, userInfo.getUserInfo().getFirstName(), userInfo.getUserInfo().getLastName()));
                    mEmailId.setText(userInfo.getUserInfo().getEmail());
                    mWalletBalance.setText(getString(R.string.price_in_naira, userInfo.getUserInfo().getWalletBalance()));

                    mScheduledRides.setText(String.format(Locale.getDefault(), "%d", 0));
                    completedRidesNumbers.setText(String.format(Locale.getDefault(), "%d", 0));
                    cancelledRidesCount.setText(String.format(Locale.getDefault(), "%d", 0));
                    totalRidesNumbers.setText(String.format(Locale.getDefault(), "%d", 0));
                }
                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(view -> {
                        hideAlertDialog();
                        getUserDashboard();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getUserDashboard();
    }


    private void getCoponCode(int id) {

        showLoading();
        DruppRequestHandler.clearInstance();

        HashMap<String, String> params = new HashMap<>();

        params.put(AppConstants.Q_ID, String.valueOf(id));

        DruppRequestHandler.getInstanceCount(new INetworkCountList<String>() {
            @Override
            public void onResponse(Response<DriverRiderCountModel> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {


//                    if (referModel.size() != 0) {
//
//                        txtCouponCode.setText(referModel.getData().get(0).getCoupon_code());
//                    }

                }
            }

            @Override
            public void onError(Response<DriverRiderCountModel> response) {
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
        }, SessionManager.getAccessToken()).get_ride_count(params);
    }

}
