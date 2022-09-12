package com.quawlebs.drupp.view.ui.scheduledRides;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.quawlebs.drupp.login.StartUpActivity;
import com.quawlebs.drupp.models.Sche_userpostModel;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.helpers.Timing;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;

import retrofit2.Response;

public class UserPostedRideSingelView extends MainBaseActivity {
    TextView time;
    TextView source;
    TextView des;
    TextView vehType;
    private TextView mDriverName, mVehicleName, mVehicleModel;
    private TextView mRating;
    String rideId;
    String eTime, eSource, eDestination, eCoriders;
    LatLng sourceLL, desLL;
    private static final String TAG = "UserPostedRideSingelVie";

    @Override
    protected void setUp() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_posted_ride_single_view);
        showLoading();
        TextView toolbarTitle = findViewById(R.id.tv_title);
        toolbarTitle.setText(getString(R.string.ride_details));

        mVehicleName = findViewById(R.id.tv_vehicle_model);
        mVehicleModel = findViewById(R.id.tv_vehicle_model);
        mRating = findViewById(R.id.tv_driver_rating);
        mDriverName = findViewById(R.id.tv_driver_name);
        time = findViewById(R.id.time);
        source = findViewById(R.id.source);
        des = findViewById(R.id.destination);
        vehType = findViewById(R.id.vehType);
        findViewById(R.id.iv_back).setOnClickListener(v -> onBackPressed());
        Intent intent = getIntent();

        rideId = intent.getStringExtra(AppConstants.K_RIDE_ID);
        getData();
        source.setSelected(true);
        des.setSelected(true);
        source.setMovementMethod(new ScrollingMovementMethod());
        des.setMovementMethod(new ScrollingMovementMethod());
        findViewById(R.id.image_edit).setOnClickListener(v -> {
            Intent intent1 = new Intent(UserPostedRideSingelView.this, UserPostedRidesEdit.class);
            intent1.putExtra("from", eSource);
            intent1.putExtra("to", eDestination);
            intent1.putExtra("time", eTime);
            intent1.putExtra("ride_id", rideId);
            intent1.putExtra("sourceLL", sourceLL);
            intent1.putExtra("desLL", desLL);
            startActivityForResult(intent1, AppConstants.REQUEST_CODES.RIDE_EDIT);


        });
    }

    private void getData() {
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<Sche_userpostModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<Sche_userpostModel>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    try {

                        //eCoriders = response.body().getResponse().getCoriders().equals();
                        eSource = response.body().getResponse().getSource();
                        eDestination = response.body().getResponse().getDestination();
                        sourceLL = new LatLng(Double.parseDouble(response.body().getResponse().getSource_latitude()), Double.parseDouble(response.body().getResponse().getSource_longitude()));
                        desLL = new LatLng(Double.parseDouble(response.body().getResponse().getDestination_latitude()), Double.parseDouble(response.body().getResponse().getDestination_longitude()));
//                    if (!eCoriders.equals("")){
//                        ll.setVisibility(View.VISIBLE);
//                        corider.setText(eCoriders);}
                        source.setText(eSource);
                        des.setText(eDestination);
                        switch (response.body().getResponse().getVehicle_type()) {
                            case 1:
                                vehType.setText(getString(R.string.without_ac));
                                break;
                            case 2:
                                vehType.setText(getString(R.string.with_ac));
                                break;
                            case 3:
                                vehType.setText(getString(R.string.bus));
                                break;
                        }
                        mDriverName.setText(response.body().getResponse().getDriverName());
                        mRating.setText(Float.toString(response.body().getResponse().getAverageRating()));
                        mVehicleName.setText(response.body().getResponse().getVehicleName());
                        mVehicleModel.setText(response.body().getResponse().getVehicleModel());
                        String date = Timing.getTimeInString(Long.parseLong(response.body().getResponse().getRide_date()), Timing.TimeFormats.CUSTOM_DATE_TIME);
                        time.setText(date);
                        eTime = date;

                    } catch (Exception e) {
                        Log.d(getClass().getSimpleName(), e.getMessage());
                    }

                }
            }

            @Override
            public void onError(Response<QualStandardResponse<Sche_userpostModel>> response) {
                hideLoading();
                if (response.code() == 401) {
                    showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
                    if (mAlertDialog != null) {
                        mAlertDialog.setCancelable(false);
                        TextView title = mAlertDialog.findViewById(R.id.tv_title);
                        title.setText(getString(R.string.your_session_has_expired));
                        Button btnOk = mAlertDialog.findViewById(R.id.btn_cancel);
                        btnOk.setText(getString(R.string.login));
                        btnOk.setOnClickListener(v -> {
                            SessionManager.getInstance().removeUserData(UserPostedRideSingelView.this);
                            UIHelper.getInstance().switchActivity(UserPostedRideSingelView.this, StartUpActivity.class, null, null, null, true);
                        });
                    }
                    return;

                }
                showErrorPrompt(response);
            }

            @Override
            public void onNullResponse() {
                Log.d(TAG, "onNullResponse: ");
                hideLoading();
            }

            @Override
            public void onFailure(Throwable t) {

                hideLoading();
                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        getData();
                    });
                }
            }
        }, SessionManager.getAccessToken()).get_Single_User_Posted_Ride(rideId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
