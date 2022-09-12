package com.quawlebs.drupp.view.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RatingBar;

import androidx.appcompat.app.AlertDialog;

import com.quawlebs.drupp.models.RateDriverModel;
import com.quawlebs.drupp.models.ResponseData;
import com.quawlebs.drupp.models.RideInfo;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.Helper;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;

import org.json.JSONObject;

import retrofit2.Response;

public class RateDriverActivity extends MainBaseActivity {

    private RatingBar rating;
    private EditText message;
    private ResponseData responseData;
    private int driverCourteous = -1, driverClean = -1, driverCareful = -1;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_driver);

        RadioGroup rgCareful = findViewById(R.id.rg_careful);
        RadioGroup rgClean = findViewById(R.id.rg_vehicle_clean);
        RadioGroup rgCourteous = findViewById(R.id.rg_driver_courteous);

        rating = findViewById(R.id.rating_bar);
        message = findViewById(R.id.et_comment);

        responseData = Helper.getInstance(RateDriverActivity.this).readFromJson(AppConstants.DRIVER_DETAILS, ResponseData.class);
        SharedPreferences sharedPreferences = getSharedPreferences("rating", MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_emergancy, null);
        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(RateDriverActivity.this);
        final AlertDialog alertDialog = alertDialogbuilder.create();
        alertDialog.setView(alertLayout);

        if (getIntent() != null) {
            try {
                String profileURL = "";
                if (getIntent().getStringExtra(AppConstants.K_RIDE_INFO) != null) {

                    JSONObject jsonObject = new JSONObject(getIntent().getStringExtra(AppConstants.K_RIDE_INFO));
                    profileURL = jsonObject.getString(AppConstants.K_DRIVER_IMAGE);
//                    rateDriverHeader.setText(jsonObject.getString(AppConstants.K_DRIVER_NAME));


                } else {
                    RideInfo rideInfo = (RideInfo) getIntent().getSerializableExtra(AppConstants.K_RIDE);
                    profileURL = rideInfo.getProfilePicture();
//                    rateDriverHeader.setText(rideSaveModel.getDriverName());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        findViewById(R.id.btn_done).setOnClickListener(v -> {
            if (isValidate()) {
                RateDriverModel rateDriverModel = new RateDriverModel();

                rateDriverModel.setRate(rating.getRating());
                rateDriverModel.setReview(message.getText().toString().trim());
                rateDriverModel.setRideId(Integer.valueOf(responseData.getRideID()));
                rateDriverModel.setReceiver(Integer.valueOf(responseData.getDriverId()));
                rateDriverModel.setIsDriverCareful(driverCareful);
                rateDriverModel.setIsDriverCourteous(driverCourteous);
                rateDriverModel.setIsVehicleClean(driverClean);

                rate(rateDriverModel);
            }

        });
        findViewById(R.id.btn_skip).setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        rgCareful.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_yes_careful:
                    driverCareful = 1;
                    break;
                case R.id.rb_no_careful:
                    driverCareful = 0;
                    break;
            }
        });
        rgClean.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_yes_clean:
                    driverClean = 1;
                    break;
                case R.id.rb_no_clean:
                    driverClean = 0;
                    break;
            }
        });
        rgCourteous.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_yes:
                    driverCourteous = 1;
                    break;
                case R.id.rb_no:
                    driverCourteous = 0;
                    break;
            }
        });
    }

    private boolean isValidate() {
        if (message.getText().toString().trim().isEmpty()) {
            showMessage(R.string.please_give_a_review);
            return false;
        } else if (driverCourteous == -1 || driverClean == -1 || driverCareful == -1) {
            showMessage(R.string.please_fill_all_details);
            return false;
        }
        return true;

    }

    private void rate(RateDriverModel rateDriverModel) {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<String>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<String>> response) {
                hideLoading();
                if (response.isSuccessful()) {
                    //Save Rating to local storage
                    Helper.saveRating(rateDriverModel, RateDriverActivity.this);

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(AppConstants.K_FLAG, true);
                    intent.putExtra(AppConstants.K_BUNDLE, bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }


            @Override
            public void onErrorList(Response<QualStandardResponseList<String>> response) {
                hideLoading();
                showErrorPrompt(response);
            }


            @Override
            public void onNullListResponse() {
                hideLoading();
            }

            @Override
            public void onFailureList(Throwable t) {
                hideLoading();
                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        rate(rateDriverModel);
                    });
                }
            }
        }, SessionManager.getAccessToken()).rateDriver(rateDriverModel);


    }
}
