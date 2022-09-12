package com.quawlebs.drupp.login;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.quawlebs.drupp.models.FireBaseToken;
import com.quawlebs.drupp.models.LoginDataModel;
import com.quawlebs.drupp.models.RideInfo;
import com.quawlebs.drupp.models.UserInfo;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.LocationUtil;
import com.quawlebs.drupp.util.NetworkUtils;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.Helper;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.util.RxBus;
import com.quawlebs.drupp.view.ui.BillActivity;
import com.quawlebs.drupp.view.ui.MainActivity;
import com.quawlebs.drupp.view.ui.RideStartActivity;
import com.quawlebs.drupp.view.ui.VerificationActivity;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;
import com.quawlebs.drupp.view.ui.ride.BottomSheetRideActivity;
import com.rilixtech.CountryCodePicker;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Objects;

import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public class StartUpActivity extends MainBaseActivity implements LocationListener {

    private CountryCodePicker ccp;
    private EditText edtPhoneNumber;
    private LoginDataModel userModel;
    private static final String TAG = "MainActivity";
    public static String fb_token;
    private ImageButton signInButton;
    private Location mCurrentLocation;
    private Disposable phoneNumberDisposable;
    private int isDriverPosted;
    int rideId;

    private Class clazz;

    String type = "";
    private RideInfo currentRideInfo;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ccp = findViewById(R.id.ccp);
        signInButton = findViewById(R.id.btn_sign_in);
        edtPhoneNumber = findViewById(R.id.phone_number_edt);
        //ccp.registerPhoneNumberTextView(edtPhoneNumber);

        //        ccp.setDefaultCountryUsingNameCode(ccp.getDefaultCountryName());
        edtPhoneNumber.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                if (isValidate()) {
                    verification();
                }

            }
            return false;
        });
        if (getIntent() != null) {
            if ((RideInfo) getIntent().getSerializableExtra("currentRideInfo") != null) {
                currentRideInfo = ((RideInfo) getIntent().getSerializableExtra("currentRideInfo"));
            }
            if ( getIntent().getSerializableExtra("isDriverPosted") != null) {
                isDriverPosted =getIntent().getExtras().getInt("isDriverPosted");
            }
            //isDriverPosted = getIntent().getExtras().getInt("isDriverPosted");

        }


        mCurrentLocation = fetchCurrentLocation();
        checkNetworkAndFetchToken();
        signInButton.setOnClickListener(v -> {
            if (isValidate()) {//valid Mobile No
                verification();
            }

        });

//        new Handler().postDelayed(() -> {
//            if (getIntent().getAction() != null) {
//                if (getIntent().getAction().equalsIgnoreCase(AppConstants.ACTION_SESSION_EXPIRED)) {
//                    runOnUiThread(() -> {
//                        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(DruppApp.getContext(), SweetAlertDialog.WARNING_TYPE);
//                        sweetAlertDialog.setTitleText(DruppApp.getContext().getString(R.string.session_expired))
//                                .setContentText(DruppApp.getContext().getString(R.string.please_sign_in_again))
//                                .setConfirmText(DruppApp.getContext().getString(R.string.ok))
//                                .setConfirmClickListener(sweetAlertDialog12 -> {
//                                    sweetAlertDialog12.dismissWithAnimation();
//                                    SessionManager.getInstance().removeUserData(DruppApp.getContext());
//
//                                })
//                                .show();
//                    });
//
//
//                }
//            }
//        }, 2000);


    }


    private boolean isValidate() {
        if (edtPhoneNumber.getText().toString().trim().isEmpty() || edtPhoneNumber.getText().toString().trim().length() < 10) {
            showMessage(R.string.please_add_a_valid_number);
            return false;
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        //   phoneNumberDisposable.dispose();
    }

    private void checkNetworkAndFetchToken() {
        if (!NetworkUtils.isNetworkConnected(this)) {
            showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
            if (mAlertDialog != null) {
                mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                    mAlertDialog.hide();
                    checkNetworkAndFetchToken();
                });
            }
        } else {
            getToken();
        }
    }

    private String getToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                    }
                    // Get new Instance ID token
                    String token = Objects.requireNonNull(task.getResult()).getToken();
                    SessionManager.getInstance().saveFireBaseToken(StartUpActivity.this, new FireBaseToken(token, "1"));
                    fb_token = token;
                    Log.d(TAG, "onComplete: TOKEN \n" + token);
                });
        return fb_token;
    }

    public AlertDialog.Builder buildDialog(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this.");

        builder.setPositiveButton("Ok", (dialog, which) -> {
        });

        return builder;
    }


    public void verification() {
        if (!isNetworkConnected()) buildDialog(StartUpActivity.this).show();
        else {//If internet connection is present
            if (getToken() != null) {
                fb_token = getToken();
            }
            showLoading();
            HashMap<String, String> parms = new HashMap<>();

            parms.put(AppConstants.K_PHONE, edtPhoneNumber.getText().toString().trim());
            parms.put(AppConstants.K_COUNTRY_CODE, ccp.getSelectedCountryCode());
            parms.put(AppConstants.K_FIREBASE_TOKEN, SessionManager.getInstance().loadFireBaseToken(this).getFirebaseToken());
            parms.put(AppConstants.K_PLATFORM, "1");

            DruppRequestHandler.getInstance(new INetwork<LoginDataModel>() {
                @Override
                public void onResponse(Response<QualStandardResponse<LoginDataModel>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        LoginDataModel loginDataModel = response.body().getResponse();
                        loginDataModel.setFb_token(fb_token);


                        if (response.code() == 200) {
                            //If User Exists Sign In and Save User
                            Helper.saveProfileUrl(response.body().getResponse().getUserInfo().getProfilePicture(), StartUpActivity.this);
                            UserInfo userInfo = loginDataModel.getUserInfo();
                            userInfo.setCountryCodeName(ccp.getSelectedCountryNameCode());
                            userInfo.setPhone(edtPhoneNumber.getText().toString());
                            userInfo.setCountryCodeName(ccp.getSelectedCountryNameCode());

                            loginDataModel.setUserInfo(userInfo);

                            SessionManager.getInstance().saveUser(StartUpActivity.this, loginDataModel);
                            Toast.makeText(getApplicationContext(),"Log In Successful",Toast.LENGTH_LONG).show();
                            //getRideInfo(userInfo.getId());

                            Intent intent = new Intent(StartUpActivity.this, MainActivity.class);
                            intent.putExtra(AppConstants.K_USER_INFO, loginDataModel);
                            startActivity(intent);

                        } else if (response.code() == 201) {
                            try {
                                JSONObject jsonObject = new JSONObject(Helper.getRideParams(getApplicationContext()));
                                String PhoneNO = jsonObject.getString(AppConstants.K_PHONE);
                                rideId = jsonObject.getInt(AppConstants.K_RIDE_ID);
                                isDriverPosted = jsonObject.getInt(AppConstants.K_POSTED_BY_DRIVER);
                                if(((rideId>0)&& (PhoneNO.equals ( edtPhoneNumber.getText().toString().trim())))){
                                    Toast.makeText(getApplicationContext(),"Session Expired",Toast.LENGTH_LONG).show();
                                    hideLoading();
                                    Intent intent = new Intent(StartUpActivity.this, MainActivity.class);
                                    //intent.putExtra(AppConstants.K_USER_INFO, loginDataModel);
                                    startActivity(intent);
                                    return;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            hideLoading();

                            //Create a New User
                            UserInfo userInfo = new UserInfo();
                            userInfo.setPhone(edtPhoneNumber.getText().toString());
                            userInfo.setCountryCode(Integer.valueOf(ccp.getSelectedCountryCode()));
                            userInfo.setCountryCodeName(ccp.getSelectedCountryNameCode());
                            loginDataModel.setFb_token(fb_token);
                            loginDataModel.setUserInfo(userInfo);

                            SessionManager.getInstance().saveUser(StartUpActivity.this, loginDataModel);
                            Intent verificationIntent = new Intent(StartUpActivity.this, VerificationActivity.class);
                            verificationIntent.putExtra(AppConstants.K_USER_INFO, loginDataModel);
                            startActivity(verificationIntent);
                        }

                    }
                }

                @Override
                public void onError(Response<QualStandardResponse<LoginDataModel>> response) {
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
                            verification();
                        });
                    }
                }
            }).userLogin(parms);


        }
    }

    public Location fetchCurrentLocation() {

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                        , AppConstants.REQUEST_ACCESS_LOCATION);
                return null;
            } else {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                LocationSettingsRequest.Builder builder = LocationUtil.showLocationRequestPopUp(this);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    //Ask user to enable GPS
                    Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());
                    result.addOnCompleteListener(task -> {
                        try {
                            LocationSettingsResponse response = task.getResult(ApiException.class);
                            // All location settings are satisfied. The client can initialize location
                            // requests here.
                        } catch (ApiException e) {
                            switch (e.getStatusCode()) {
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied. But could be fixed by showing the
                                    // user a dialog.
                                    try {
                                        // Cast to a resolvable exception.
                                        ResolvableApiException resolvable = (ResolvableApiException) e;
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        resolvable.startResolutionForResult(
                                                StartUpActivity.this,
                                                AppConstants.REQUEST_ACCESS_LOCATION);
                                    } catch (IntentSender.SendIntentException e1) {
                                        // Ignore the error.
                                    } catch (ClassCastException e2) {
                                        // Ignore, should be an impossible error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied. However, we have no way to fix the
                                    // settings so we won't show the dialog.

                                    break;
                            }

                        }
                    });

                    return null;

                } else {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, AppConstants.LOCATION_UPDATE_TIME_INTERVAL, AppConstants.LOCATION_UPDATE_MIN_DISTANCE, this);
                    if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, AppConstants.LOCATION_UPDATE_TIME_INTERVAL, AppConstants.LOCATION_UPDATE_MIN_DISTANCE, this);
                    }

                    Criteria criteria = new Criteria();
                    int currentapiVersion = android.os.Build.VERSION.SDK_INT;

                    if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {

                        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
                        criteria.setSpeedAccuracy(Criteria.ACCURACY_HIGH);
                        criteria.setAccuracy(Criteria.ACCURACY_FINE);
                        criteria.setAltitudeRequired(true);
                        criteria.setBearingRequired(true);
                        criteria.setSpeedRequired(true);

                    }
                    String provider = locationManager.getBestProvider(criteria, true);
                    //Sets Places api to return only for radius
                    return locationManager.getLastKnownLocation(provider);
                }

            }
        }catch (Exception exception){

            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.REQUEST_ACCESS_LOCATION) {
            if (resultCode == Activity.RESULT_OK) {
                fetchCurrentLocation();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void getRideInfo(int id) {

        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<RideInfo>() {
            @Override
            public void onResponse(Response<QualStandardResponse<RideInfo>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    RideInfo rideInfo = response.body().getResponse();
                    rideInfo.setActionType(type);
                    Log.e("rider info", new Gson().toJson(rideInfo));
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
                    hideLoading();

                    Intent intent = new Intent(StartUpActivity.this, clazz);

                    intent.putExtra(AppConstants.K_RIDE_INFO, rideInfo);
                    RxBus.getInstance().setIntentEvent(rideInfo);
                    startActivity(intent);
                    finish();


                }

            }

            @Override
            public void onError(Response<QualStandardResponse<RideInfo>> response) {
                hideLoading();

                Intent intent = new Intent(StartUpActivity.this, BottomSheetRideActivity.class);
                intent.putExtra(AppConstants.K_RIDE_INFO, currentRideInfo);
                startActivity(intent);
                finish();

                Log.d(getClass().getSimpleName(), "ERROR");

            }

            @Override
            public void onNullResponse() {
                hideLoading();

                Log.d(getClass().getSimpleName(), "ERROR");
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoading();

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
        }, SessionManager.getAccessToken()).getRideInfo(isDriverPosted, id);
    }

}
