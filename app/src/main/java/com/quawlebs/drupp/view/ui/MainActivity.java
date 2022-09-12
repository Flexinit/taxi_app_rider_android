package com.quawlebs.drupp.view.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.quawlebs.drupp.login.LoginActivity;
import com.quawlebs.drupp.login.StartUpActivity;
import com.quawlebs.drupp.models.LoginDataModel;
import com.quawlebs.drupp.models.RideInfo;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.Helper;
import com.quawlebs.drupp.helpers.IResponseObserver;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.util.RxBus;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;
import com.quawlebs.drupp.view.ui.dialog.LocationAccessDialog;
import com.quawlebs.drupp.view.ui.dialog.LocationRequestDialog;
import com.quawlebs.drupp.view.ui.ride.BottomSheetRideActivity;

import org.json.JSONObject;

import java.net.SocketTimeoutException;

import retrofit2.Response;

public class MainActivity extends MainBaseActivity implements IResponseObserver {
    private LocationAccessDialog locationAccessDialog;

    private Class clazz;
    private LoginDataModel userModel;
    private RideInfo currentRideInfo;
    private int rideId;
    private int isDriverPosted;
    private String type = "";

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_splash);
        if (getIntent().getExtras() != null) {

            try {
                String jobj = getIntent().getStringExtra(AppConstants.K_RIDE_INFO);
                type = getIntent().getStringExtra(AppConstants.K_TYPE);
                currentRideInfo = new Gson().fromJson(jobj, RideInfo.class);
                isDriverPosted = currentRideInfo.getPostedByDriver();
                if (currentRideInfo.getPostedByDriver() == 1) {
                    rideId = currentRideInfo.getId() == null ? currentRideInfo.getDriverRideId() : currentRideInfo.getId();
                } else {
                    rideId = currentRideInfo.getId() == null ? currentRideInfo.getRideId() : currentRideInfo.getId();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(rideId>0) {
            checkPermission();
            return;
        }


        try {
            JSONObject jsonObject = new JSONObject(Helper.getRideParams(this));
            rideId = jsonObject.getInt(AppConstants.K_RIDE_ID);
            isDriverPosted = jsonObject.getInt(AppConstants.K_POSTED_BY_DRIVER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        checkPermission();

    }

    private void checkLocationAccess() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                    , AppConstants.REQUEST_ACCESS_LOCATION);
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationAccessDialog = LocationAccessDialog.newInstance();
                locationAccessDialog.show(getSupportFragmentManager(), LocationAccessDialog.class.getSimpleName());
            } else {
                final int splashTimeOut = 1000;
                Thread splashThread = new Thread() {
                    int wait = 0;

                    @Override
                    public void run() {
                        try {
                            super.run();
                            while (wait < splashTimeOut) {
                                sleep(100);
                                wait += 100;
                            }
                        } catch (Exception e) {
                        } finally {
                            checkDesireActivity();
                            //   finish();
                        }
                    }
                };
                splashThread.start();
            }
        }

    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            LocationRequestDialog locationRequestDialog = LocationRequestDialog.newInstance();
            locationRequestDialog.setCancelable(false);
            locationRequestDialog.show(getSupportFragmentManager(), LocationRequestDialog.class.getSimpleName());
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            askPermission();
        } else {
            checkLocationAccess();
        }

    }

    private void askPermission() {
        // Permission is not granted
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE) && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    AppConstants.REQUEST_WRITE_STORAGE);
        } else {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    AppConstants.REQUEST_WRITE_STORAGE);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case AppConstants.REQUEST_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkLocationAccess();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    checkPermission();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void checkDesireActivity() {
        userModel = SessionManager.getInstance().loadUser(MainActivity.this);

        if (userModel == null) {
            startSignInActivity();
        } else {
            if (userModel.getToken().isEmpty()) {
                startSignInActivity();
            } else if (userModel.isOtpVerified()) {
                //Fetch Ride Status From Server
                getRideInfo();
            } else {
                // switchToSplash();
                getRideInfo();
            }

        }

    }

    private void startSignInActivity() {
        Intent intent = new Intent(MainActivity.this, StartUpActivity.class);
        intent.putExtra("isDriverPosted",isDriverPosted);
        intent.putExtra("currentRideInfo",currentRideInfo);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.REQUEST_ACCESS_LOCATION) {
            if (resultCode == Activity.RESULT_OK) {
                if (locationAccessDialog != null) {
                    checkDesireActivity();
                    locationAccessDialog.dismiss();
                }
            }
        }

    }

    @Override
    public void onFailure(String message) {
    }

    @Override
    public void onSuccess(String message) {
        checkLocationAccess();
    }


    private void getRideInfo() {
        //TODO : CHANGE DRIVER POSTED

        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<RideInfo>() {
            @Override
            public void onResponse(Response<QualStandardResponse<RideInfo>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    RideInfo rideInfo = response.body().getResponse();
                    rideInfo.setActionType(type);
                    Log.e("rider info",new Gson().toJson(rideInfo));
                    //Ride Now
                    switch (rideInfo.getStatus()) {
                        case AppConstants.RIDE_STATUS.RIDE_ACCEPTED:
                            if (rideInfo.getRideOption() == 1) {
                                clazz = RideStartActivity.class;
                            } else {
                                clazz = BottomSheetRideActivity.class;
                            }

                            break;
                        case AppConstants.RIDE_STATUS.RIDE_STARTED:
                            clazz = RideStartActivity.class;
                            break;
                        case AppConstants.RIDE_STATUS.RIDE_COMPLETED:
                            clazz = BillActivity.class;
                            break;
                        case AppConstants.RIDE_STATUS.RIDE_CANCELLED:
                        case AppConstants.RIDE_STATUS.RIDE_PAID:
                        default:
//                            if (userModel.isOtpVerified()) {
//                                clazz = BottomSheetRideActivity.class;
//                            } else {
//                                clazz = VerificationActivity.class;
//                            }
                            clazz = BottomSheetRideActivity.class;

                            break;

                    }
                    Intent intent = new Intent(MainActivity.this, clazz);

                    intent.putExtra(AppConstants.K_RIDE_INFO, rideInfo);
                    RxBus.getInstance().setIntentEvent(rideInfo);
                    startActivity(intent);
                    finish();


                }

            }

            @Override
            public void onError(Response<QualStandardResponse<RideInfo>> response) {
                Intent intent = new Intent(MainActivity.this, BottomSheetRideActivity.class);
                intent.putExtra(AppConstants.K_RIDE_INFO, currentRideInfo);
                startActivity(intent);
                finish();

                Log.d(getClass().getSimpleName(), "ERROR");

            }

            @Override
            public void onNullResponse() {
                Log.d(getClass().getSimpleName(), "ERROR");
            }

            @Override
            public void onFailure(Throwable t) {
                if (t instanceof SocketTimeoutException) {

                }
                Log.d(getClass().getSimpleName(), "ERROR");
//                hideLoading();
//                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
//                if (mAlertDialog != null) {
//                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(view -> {
//                        hideAlertDialog();
//                        getRideInfo();
//                    });
//                }
            }
        }, SessionManager.getAccessToken()).getRideInfo(isDriverPosted, rideId);
    }
}
