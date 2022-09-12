package com.quawlebs.drupp.view.ui.scheduledRides;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import com.quawlebs.drupp.login.StartUpActivity;
import com.quawlebs.drupp.models.Sche_driverpostModel;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.RxBus;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.helpers.Timing;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class DriverPostedRideSingelView extends MainBaseActivity {

    TextView source, des, time, driType, pref, fare, coriders;
    String eDtype, ePref, eCoriders, esource;
    String crideId, rideId;
    private TableRow coRidersContainer;
    private View separatorCoRider;
    private Sche_driverpostModel sche_driverpostModel;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_posted_ride_singel_view);
        ButterKnife.bind(this);

        source = findViewById(R.id.source);
        coRidersContainer = findViewById(R.id.container_co_riders);
        separatorCoRider = findViewById(R.id.separator_co_rider);
        coriders = findViewById(R.id.coriders);
        des = findViewById(R.id.destination);
        driType = findViewById(R.id.drivertype);
        pref = findViewById(R.id.pref);
        fare = findViewById(R.id.fare);
        time = findViewById(R.id.time);
        final Intent intent = getIntent();
        crideId = intent.getStringExtra("rideId");
        getData();

        findViewById(R.id.image_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getInstance().setIntentEvent(sche_driverpostModel);
                Intent intent = new Intent(DriverPostedRideSingelView.this, DriverPostedRidesEdit.class);
                startActivityForResult(intent, AppConstants.REQUEST_CODES.RIDE_EDIT);
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void onBack() {
        onBackPressed();
    }


    private void getData() {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<Sche_driverpostModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<Sche_driverpostModel>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    try {

                        sche_driverpostModel = response.body().getResponse();

                        String date = Timing.getTimeInString(Long.parseLong(response.body().getResponse().getRide_date()), Timing.TimeFormats.CUSTOM_DATE_TIME);
                        time.setText(date);
                        esource = response.body().getResponse().getSource();
                        ePref = response.body().getResponse().getPreference();
                        eDtype = response.body().getResponse().getType_of_driver();
                        eCoriders = response.body().getResponse().getCo_riders();
                        rideId = response.body().getResponse().getRide_id();
                        source.setText(esource);
                        des.setText(response.body().getResponse().getDestination());
                        pref.setText(ePref);
                        fare.setText(getString(R.string.price_in_naira, response.body().getResponse().getDriverFare()));
                        driType.setText(eDtype);
                        coRidersContainer.setVisibility(response.body().getResponse().getRideType() == AppConstants.RIDE_TYPE.USER_RIDE ?
                                View.GONE : View.VISIBLE);
                        separatorCoRider.setVisibility(response.body().getResponse().getRideType() == AppConstants.RIDE_TYPE.USER_RIDE ?
                                View.GONE : View.VISIBLE);
                        coriders.setText(eCoriders);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onError(Response<QualStandardResponse<Sche_driverpostModel>> response) {
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
                            SessionManager.getInstance().removeUserData(DriverPostedRideSingelView.this);
                            UIHelper.getInstance().switchActivity(DriverPostedRideSingelView.this, StartUpActivity.class, null, null, null, true);
                        });
                    }
                    return;

                }
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
                        getData();
                    });
                }
            }
        }, SessionManager.getAccessToken()).get_Single_Driver_Posted_Ride(crideId);
    }
}
