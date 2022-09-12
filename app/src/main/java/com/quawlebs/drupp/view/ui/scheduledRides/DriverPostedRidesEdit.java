package com.quawlebs.drupp.view.ui.scheduledRides;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quawlebs.drupp.login.StartUpActivity;
import com.quawlebs.drupp.models.Sche_driverpostModel;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.RxBus;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public class DriverPostedRidesEdit extends MainBaseActivity {

    String eDtype, ePref, eCoriders, esource, eRideId;
    private Integer rideType;
    RadioGroup coridersNumber, driverType;
    RadioButton coridersN, driverT;
    private RelativeLayout containerPoolRide;
    EditText message, pickupLocation;
    Button done_userPref;
    private Disposable intentDisposable;

    @Override
    protected void setUp() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_posted_rides_edit);
        ButterKnife.bind(this);
        coridersNumber = findViewById(R.id.edit_pf_corider_number);
        driverType = findViewById(R.id.edit_pf_type_of_driver);
        containerPoolRide = findViewById(R.id.rel1);
        message = findViewById(R.id.edit_pf_message);
        pickupLocation = findViewById(R.id.edit_pf_pickupLocation);
        done_userPref = findViewById(R.id.edit_drive_done);
        Intent intent = getIntent();
        intentDisposable = RxBus.getInstance().getIntentEvent().subscribe(o -> {
            if (o instanceof Sche_driverpostModel) {
                Sche_driverpostModel sche_driverpostModel = (Sche_driverpostModel) o;
                eDtype = sche_driverpostModel.getType_of_driver();
                ePref = sche_driverpostModel.getPreference();
                eCoriders = sche_driverpostModel.getCo_riders();
                esource = sche_driverpostModel.getSource();
                eRideId = sche_driverpostModel.getRide_id();
                rideType = sche_driverpostModel.getRideType();
                containerPoolRide.setVisibility(rideType == AppConstants.RIDE_TYPE.USER_RIDE ? View.GONE : View.VISIBLE);
            }
        });


        pickupLocation.setText(esource);
        message.setText(ePref);
        switch (eDtype) {
            case "1": {
                driverType.check(R.id.cbA1);
                break;
            }
            case "2": {
                driverType.check(R.id.cbA2);
                break;
            }
            case "3": {
                driverType.check(R.id.cbA3);
                break;
            }
        }
        switch (eCoriders) {
            case "1": {
                coridersNumber.check(R.id.cb1);
                break;
            }
            case "2": {
                coridersNumber.check(R.id.cb2);
                break;
            }
            case "3": {
                coridersNumber.check(R.id.cb3);
                break;
            }
        }

        done_userPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int coR, drT;
                coR = coridersNumber.getCheckedRadioButtonId();
                coridersN = findViewById(coR);
                drT = driverType.getCheckedRadioButtonId();
                driverT = findViewById(drT);
                if (!message.getText().toString().equals(null)) {
                    ePref = message.getText().toString();
                }

                submit();

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        intentDisposable.dispose();
    }

    @OnClick(R.id.image_back)
    public void onBack() {
        onBackPressed();
    }

    private void submit() {
        DruppRequestHandler.clearInstance();
        HashMap<String, String> parms = new HashMap<>();
        parms.put("id", eRideId);
        parms.put("co_riders_preference", coridersN.getTag().toString());
        parms.put("type_of_driver", driverT.getTag().toString());
        parms.put("pick_up_location", pickupLocation.getText().toString());
        parms.put("pick_up_latitude", "22.68610");
        parms.put("pick_up_longitude", "75.8570");
        parms.put("preference", ePref);


        hideLoading();
        DruppRequestHandler.getInstance(new INetworkList<String>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<String>> response) {
                hideLoading();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<String>> response) {
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
                            SessionManager.getInstance().removeUserData(DriverPostedRidesEdit.this);
                            UIHelper.getInstance().switchActivity(DriverPostedRidesEdit.this, StartUpActivity.class, null, null, null, true);
                        });
                    }
                    return;

                }
                showErrorPrompt(response);
            }

            @Override
            public void onNullListResponse() {
                hideLoading();
                Toast.makeText(DriverPostedRidesEdit.this, "Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailureList(Throwable t) {
                hideLoading();
                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        submit();
                    });
                }
            }
        }, SessionManager.getAccessToken()).edit_driverPostedRide(parms);
    }

}
