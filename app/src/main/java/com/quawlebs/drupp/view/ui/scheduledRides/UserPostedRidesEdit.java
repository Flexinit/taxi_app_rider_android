package com.quawlebs.drupp.view.ui.scheduledRides;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.quawlebs.drupp.login.StartUpActivity;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.LocationUtil;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.IFragmentEventObserver;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.helpers.Timing;
import com.quawlebs.drupp.service.FetchAddressIntentService;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;
import com.quawlebs.drupp.view.ui.ride.RideType;


import java.util.Calendar;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class UserPostedRidesEdit extends MainBaseActivity implements
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener, IFragmentEventObserver, LocationListener {

    EditText mTime, mSource, mDestination;
    Button mSubmit;
    String rideId, from, to, time;
    LatLng lFrom, lTo;
    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;
    long finalTime;
    private static final String TAG = "UserPostedRidesEdit";
    private LatLng from_latlang;
    private LatLng to_latlang;
    private Location currentBestLocation;
    private Location location;
    private AddressResultReceiver resultReceiver;


    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_posted_rides_edit);
        ButterKnife.bind(this);
        mTime = findViewById(R.id.edit_time);
        mSubmit = findViewById(R.id.edit_user_done);
        mSource = findViewById(R.id.etDeparture);
        mDestination = findViewById(R.id.etDestination);
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        to = intent.getStringExtra("to");
        time = intent.getStringExtra("time");
        mTime.setText(time);
        rideId = intent.getStringExtra("ride_id");
        lFrom = intent.getParcelableExtra("sourceLL");
        lTo = intent.getParcelableExtra("desLL");
        mSource.setText(from);
        mDestination.setText(to);
        placeSelect();


        resultReceiver = new AddressResultReceiver(new Handler());
        mTime.setOnClickListener(v -> dateTime());
        findViewById(R.id.edit_user_done).setOnClickListener(v -> {
                    if (isValidate()) {
                        update();
                    }
                }
        );

        //Listeners
        final Drawable finalImgClearButton = AppCompatResources.getDrawable(getContext(), R.drawable.ic_clear_black_24dp);
        mSource.setOnTouchListener((v, event) -> {
            if (mSource.getCompoundDrawables()[2] == null) {
                return false;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                return false;
            }
            if (event.getX() > mSource.getWidth() - mSource.getPaddingRight() - finalImgClearButton.getIntrinsicWidth()) {
                mSource.setText("");

                return true;
            }
            return false;
        });
        mDestination.setOnTouchListener((v, event) -> {
            if (mDestination.getCompoundDrawables()[2] == null) {
                return false;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                return false;
            }
            if (event.getX() > mDestination.getWidth() - mDestination.getPaddingRight() - finalImgClearButton.getIntrinsicWidth()) {
                mDestination.setText("");

                return true;
            }
            return false;
        });

        setupPlaces();
    }

    @OnClick(R.id.image_back)
    public void onBack() {
        onBackPressed();
    }

    private boolean isValidate() {
        if (mSource.getText().toString().trim().isEmpty()) {
            showMessage(R.string.please_enter_source);
            return false;
        } else if (mDestination.getText().toString().trim().isEmpty()) {
            showMessage(R.string.please_enter_destination);
            return false;
        }
        return true;
    }

    private void setupPlaces() {
        mSource.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(AppConstants.K_LOCATION_TYPE, AppConstants.HOME_LOCATION);
            bundle.putString(AppConstants.SEARCH_DIALOG_TITLE, getString(R.string.enter_pickup_location));
            UIHelper.getInstance().showPlaceSearchDialog(UserPostedRidesEdit.this, bundle);

        });
        mSource.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                Bundle bundle = new Bundle();
                bundle.putInt(AppConstants.K_LOCATION_TYPE, AppConstants.HOME_LOCATION);
                bundle.putString(AppConstants.SEARCH_DIALOG_TITLE, getString(R.string.enter_pickup_location));
                UIHelper.getInstance().showPlaceSearchDialog(UserPostedRidesEdit.this, bundle);


            } else {
                hideKeyboard();
            }
        });
        mDestination.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                Bundle b = new Bundle();
                b.putInt(AppConstants.K_LOCATION_TYPE, AppConstants.WORK_LOCATION);
                b.putString(AppConstants.SEARCH_DIALOG_TITLE, getString(R.string.enter_drop_location));
                UIHelper.getInstance().showPlaceSearchDialog(UserPostedRidesEdit.this, b);

            } else {
                hideKeyboard();
            }
        });
        mDestination.setOnClickListener(v -> {

            Bundle b = new Bundle();
            b.putInt(AppConstants.K_LOCATION_TYPE, AppConstants.WORK_LOCATION);
            b.putString(AppConstants.SEARCH_DIALOG_TITLE, getString(R.string.enter_drop_location));
            UIHelper.getInstance().showPlaceSearchDialog(UserPostedRidesEdit.this, b);
        });

    }

    private void dateTime() {
        Calendar c = Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, UserPostedRidesEdit.this, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        yearFinal = year;
        monthFinal = month + 1;
        dayFinal = dayOfMonth;

        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, hour, minute, true);
        timePickerDialog.show();

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hourFinal = hourOfDay;
        minuteFinal = minute;
        String dt = "Date: " + dayFinal + "/" + monthFinal + "/" + yearFinal + " Time: " + hourFinal + ":" + minuteFinal;
        String fdt = dayFinal + "-" + monthFinal + "-" + yearFinal + " " + hourFinal + ":" + minuteFinal;

        finalTime = Timing.getTimeInUnixStamp(fdt, Timing.TimeFormats.CUSTOM_DATE_TIME);
        mTime.setText(dt);
    }

    private void update() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("source", mSource.getText().toString().trim());
        hashMap.put("source_latitude", String.valueOf(from_latlang.latitude));
        hashMap.put("source_longitude", String.valueOf(from_latlang.longitude));
        hashMap.put("destination", mDestination.getText().toString().trim());
        hashMap.put("destination_latitude", String.valueOf(to_latlang.latitude));
        hashMap.put("destination_longitude", String.valueOf(to_latlang.longitude));
        hashMap.put("ride_date", String.valueOf(finalTime));
        hashMap.put("ride_id", rideId);

        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<String>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showMessage(response.body().getErrorResponse());
                    setResult(RESULT_OK);
                    finish();
                }

            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<String>> response) {

                if (response.code() == 401) {
                    showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
                    if (mAlertDialog != null) {
                        mAlertDialog.setCancelable(false);
                        TextView title = mAlertDialog.findViewById(R.id.tv_title);
                        title.setText(getString(R.string.your_session_has_expired));
                        Button btnOk = mAlertDialog.findViewById(R.id.btn_cancel);
                        btnOk.setText(getString(R.string.login));
                        btnOk.setOnClickListener(v -> {
                            SessionManager.getInstance().removeUserData(UserPostedRidesEdit.this);
                            UIHelper.getInstance().switchActivity(UserPostedRidesEdit.this, StartUpActivity.class, null, null, null, true);
                        });
                    }
                    return;

                }
                setResult(RESULT_CANCELED);
                finish();
                showErrorPrompt(response);
            }

            @Override
            public void onNullListResponse() {
                Log.d(TAG, "onNullListResponse: ");
            }

            @Override
            public void onFailureList(Throwable t) {
                Log.d(TAG, "onFailureList: ");
                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        update();
                    });
                }
            }
        }, SessionManager.getAccessToken()).edit_userPostedRide(hashMap);
    }

    private void placeSelect() {
//        mFrom.showClearButton(true);
//        mTo.showClearButton(true);
//        mFrom.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
//            @Override
//            public void onPlaceSelected(@NonNull Place place) {
//                mFrom.setText(place.description);
//                mFrom.getDetailsFor(place, new DetailsCallback() {
//                    @Override
//                    public void onSuccess(PlaceDetails placeDetails) {
//                        lFrom = new LatLng(placeDetails.geometry.location.lat, placeDetails.geometry.location.lng);
//                    }
//
//                    @Override
//                    public void onFailure(Throwable throwable) {
//                    }
//                });
//            }
//        });
//        mTo.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
//            @Override
//            public void onPlaceSelected(@NonNull Place place) {
//                mTo.setText(place.description);
//                mTo.getDetailsFor(place, new DetailsCallback() {
//                    @Override
//                    public void onSuccess(PlaceDetails placeDetails) {
//                        lTo = new LatLng(placeDetails.geometry.location.lat, placeDetails.geometry.location.lng);
//                    }
//
//                    @Override
//                    public void onFailure(Throwable throwable) {
//                    }
//                });
//            }
//        });
    }

    @Override
    public void onRideSelected(int rideType) {

    }

    @Override
    public void onPlaceSelected(Place place, int locationType) {

        if (locationType == AppConstants.HOME_LOCATION) {

            mSource.setText(place.getAddress());
            //          hashMap.put(AppConstants.K_SOURCE, place.getAddress());
            from_latlang = place.getLatLng();

        } else {
//            hashMap.put(AppConstants.K_DEST, place.getAddress());
            mDestination.setText(place.getAddress());
            to_latlang = place.getLatLng();
        }
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(RideType.class.getSimpleName());
        if (!mSource.getText().toString().trim().isEmpty() && !mDestination.getText().toString().trim().isEmpty()) {
            if (mSource.getText().toString().trim().equalsIgnoreCase(mDestination.getText().toString().trim())) {

                if (fragment instanceof RideType) {
                    RideType rideTypeFragment = (RideType) fragment;
                    rideTypeFragment.disableRide();
                }
            } else {
                if (fragment instanceof RideType) {
                    RideType rideTypeFragment = (RideType) fragment;
                    rideTypeFragment.enableRide();
                }
            }
        }

    }

    @Override
    public void onCurrentLocationSelected() {
        currentLocation();
    }

    private void currentLocation() {
        //Better approach to get locaiton

        location = fetchCurrentLocation();
        if (location != null) {

            getAddress(location);
            from_latlang = new LatLng(location.getLatitude(), location.getLongitude());
        }


    }

    public Location fetchCurrentLocation() {
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
                                            UserPostedRidesEdit.this,
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
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, AppConstants.LOCATION_UPDATE_TIME_INTERVAL, AppConstants.LOCATION_UPDATE_MIN_DISTANCE, this);

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
                currentBestLocation = locationManager.getLastKnownLocation(provider);
                return currentBestLocation;
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


    public void getAddress(Location lastLocation) {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(AppConstants.RECEIVER, resultReceiver);
        intent.putExtra(AppConstants.LOCATION_DATA_EXTRA, lastLocation);
        startService(intent);

    }

    class AddressResultReceiver extends ResultReceiver {
        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.s
         *
         * @param handler
         */
        private String addressOutput;

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultData == null) {
                return;
            }

            // Display the address string
            // or an error message sent from the intent service.
            addressOutput = resultData.getString(AppConstants.RESULT_DATA_KEY);
            if (addressOutput == null) {
                addressOutput = "";
            }
            if (addressOutput.equals(getString(R.string.service_not_available))) {
                hideAlertDialog();
                showMessage(getString(R.string.service_not_available));
            } else {
///                hashMap.put(AppConstants.K_SOURCE, addressOutput);
                mSource.setText(addressOutput);
            }


        }

    }

}
