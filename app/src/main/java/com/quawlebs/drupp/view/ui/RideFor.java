package com.quawlebs.drupp.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.quawlebs.drupp.models.DriverPostRide;
import com.quawlebs.drupp.models.SingleNotificationModel;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.Helper;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.helpers.Timing;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;

import retrofit2.Response;

public class RideFor extends MainBaseActivity {

    private TextView mDate, mTime, mDriverName, mVehicleModel, mVehicleNumber, mSource, mDest, mFare;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_for);
        findViewById(R.id.back_rideFor).setOnClickListener(v -> onBackPressed());
        findViewById(R.id.accept_RideFor).setOnClickListener(v -> startActivity(new Intent(RideFor.this, UserPreferencesActivity.class)));

        TextView des1 = findViewById(R.id.tvGetRide);
        mDate = findViewById(R.id.tv_date);
        mTime = findViewById(R.id.tv_time);
        mDriverName = findViewById(R.id.tv_driver_name);
        mVehicleModel = findViewById(R.id.tv_vehicle_model);
        mVehicleNumber = findViewById(R.id.tv_vehicle_number);
        mSource = findViewById(R.id.tv_from);
        mDest = findViewById(R.id.tv_to);
        mFare = findViewById(R.id.tv_bill_amount);
        DriverPostRide driverPostRide = Helper.getInstance(RideFor.this).readFromJson(AppConstants.DRIVER_POST_RIDE, DriverPostRide.class);
        try {
            mDriverName.setText(driverPostRide.getDriverPost_driverName());
            mVehicleModel.setText(driverPostRide.getDriverPost_vehicle_name());
            mVehicleNumber.setText(driverPostRide.getDriverPost_vehicle_number());
            mSource.setText(driverPostRide.getDriverPost_source());
            mDest.setText(driverPostRide.getDriverPost_destination());
            mFare.setText(getString(R.string.fare_pop_up, driverPostRide.getDriverPost_total_fare()));
            String Ddate = Timing.getTimeInString(Long.parseLong(driverPostRide.getDriverPost_ride_date()), Timing.TimeFormats.CUSTOM_DATE_TIME);

            String[] splited = Ddate.split(" ");
            mDate.setText(splited[0]);
            mTime.setText(splited[1]);


            des1.setText(getString(R.string.get_a_ride, driverPostRide.getDriverPost_destination()));
        } catch (Exception e) {
            return;
        }


        if (getIntent() != null) {
            try {
                //TODO
                getSingleNotification(Integer.valueOf(getIntent().getStringExtra(AppConstants.K_RIDE_ID)));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void getSingleNotification(Integer id) {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<SingleNotificationModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<SingleNotificationModel>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        mDriverName.setText(response.body().getResponse().getDriverName());
                        mVehicleModel.setText(response.body().getResponse().getVehicleName());
                        mVehicleNumber.setText(response.body().getResponse().getVehicleNumber());
                        mSource.setText(response.body().getResponse().getSource());
                        mDest.setText(response.body().getResponse().getDestination());
                        mFare.setText(getString(R.string.fare_, response.body().getResponse().getDriverFare()));

                        String Ddate = Timing.getTimeInString(Long.parseLong(response.body().getResponse().getRideDate()), Timing.TimeFormats.CUSTOM_DATE_TIME);

                        String[] splited = Ddate.split(" ");
                        mDate.setText(getString(R.string.date, splited[0]));
                        mTime.setText(getString(R.string.time, splited[1]));

//                        Glide.with(RideFor.this).load(AppConstants.IMAGE_URL + response.body().getResponse().getProfilePicture()).apply(new RequestOptions()
//                                .error(R.drawable.user_profile_icon);

                    } catch (Exception exception) {

                    }

                }
            }

            @Override
            public void onError(Response<QualStandardResponse<SingleNotificationModel>> response) {
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
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        getSingleNotification(id);
                    });
                }
            }
        }, SessionManager.getAccessToken()).getSingleNotification(id);
    }
}
