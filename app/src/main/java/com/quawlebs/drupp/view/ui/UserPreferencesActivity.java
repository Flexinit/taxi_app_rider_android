package com.quawlebs.drupp.view.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.quawlebs.drupp.login.StartUpActivity;
import com.quawlebs.drupp.models.DriverPostRide;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.Helper;
import com.quawlebs.drupp.helpers.IFragmentEventObserver;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;
import com.quawlebs.drupp.view.ui.ride.BottomSheetRideActivity;

import java.util.HashMap;

import retrofit2.Response;

public class UserPreferencesActivity extends MainBaseActivity implements IFragmentEventObserver {

    //-----------------Views---------------------------
    private LinearLayout poolLayout;
    private RadioGroup coridersNumber, driverType;
    private RadioButton coridersN, driverT;
    private EditText message;
    private EditText mPickUpLocation;
    private LatLng mPickup;
    private String cn;
    private String dt;
    private String msg = "";

    @Override
    protected void setUp() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpref);
        coridersNumber = findViewById(R.id.pf_corider_number);
        driverType = findViewById(R.id.pf_type_of_driver);
        coridersN = findViewById(R.id.cb1);
        driverT = findViewById(R.id.cbA1);
        poolLayout = findViewById(R.id.rel1);
        message = findViewById(R.id.pf_message);
        mPickUpLocation = findViewById(R.id.et_pickup_location);
        Button done_userPref = findViewById(R.id.done_userPref);

        //TODO://GET FORM PREVIOUS ACTIVITY
        DriverPostRide driverPostRide = Helper.getInstance(UserPreferencesActivity.this).readFromJson(AppConstants.DRIVER_POST_RIDE, DriverPostRide.class);


        findViewById(R.id.backnav_pref).setOnClickListener(v -> onBackPressed());
        findViewById(R.id.done_userPref).setOnClickListener(v -> startActivity(new Intent(UserPreferencesActivity.this, RideStartActivity.class)));

        poolLayout.setVisibility(Integer.valueOf(driverPostRide.getRideType()) == 1 ? View.GONE : View.VISIBLE);
        final Drawable finalImgClearButton = AppCompatResources.getDrawable(getContext(), R.drawable.ic_clear_black_24dp);
        mPickUpLocation.setOnTouchListener((v, event) -> {
            if (mPickUpLocation.getCompoundDrawables()[2] == null) {
                return false;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                return false;
            }
            if (event.getX() > mPickUpLocation.getWidth() - mPickUpLocation.getPaddingRight() - finalImgClearButton.getIntrinsicWidth()) {
                mPickUpLocation.setText("");
                return true;
            }
            return false;
        });
        mPickUpLocation.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(AppConstants.K_LOCATION_TYPE, AppConstants.HOME_LOCATION);
            bundle.putString(AppConstants.SEARCH_DIALOG_TITLE, getString(R.string.enter_pickup_location));
            UIHelper.getInstance().showPlaceSearchDialog(UserPreferencesActivity.this, bundle);
        });

        mPickUpLocation.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                Bundle bundle = new Bundle();
                bundle.putInt(AppConstants.K_LOCATION_TYPE, AppConstants.HOME_LOCATION);
                bundle.putString(AppConstants.SEARCH_DIALOG_TITLE, getString(R.string.enter_pickup_location));
                UIHelper.getInstance().showPlaceSearchDialog(UserPreferencesActivity.this, bundle);


            } else {
                hideKeyboard();
            }
        });
        done_userPref.setOnClickListener(v -> {
            int coR, drT;
            coR = coridersNumber.getCheckedRadioButtonId();
            coridersN = findViewById(coR);
            drT = driverType.getCheckedRadioButtonId();
            driverT = findViewById(drT);

            if (isValidate()) {
                submit();
            }

        });
    }

    private boolean isValidate() {
        if (mPickUpLocation.getText().toString().trim().isEmpty()) {
            showMessage(R.string.please_enter_pickup_location);
            return false;
        } else if (message.getText().toString().trim().isEmpty()) {
            showMessage(R.string.please_enter_preference_message);
            return false;
        }
        return true;
    }

    private void submit() {
        DriverPostRide driverPostRide = Helper.getInstance(UserPreferencesActivity.this).readFromJson(AppConstants.DRIVER_POST_RIDE, DriverPostRide.class);
        DruppRequestHandler.clearInstance();
        HashMap<String, String> parms = new HashMap<>();
        parms.put(AppConstants.K_DRIVER_RIDE_ID, driverPostRide.getDriverPost_rideId());
        parms.put(AppConstants.K_CO_RIDER_PREF, coridersN.getTag().toString().trim());
        parms.put(AppConstants.K_TYPE_OF_DRIVER, driverT.getTag().toString().trim());
        parms.put(AppConstants.K_PICKUP_LOCATION, mPickUpLocation.getText().toString().trim());
        parms.put(AppConstants.K_PICKUP_LAT, String.valueOf(mPickup.latitude));
        parms.put(AppConstants.K_PICKUP_LONG, String.valueOf(mPickup.longitude));
        parms.put(AppConstants.K_PREFERENCE, message.getText().toString().trim());

        showLoading();
        DruppRequestHandler.getInstance(new INetworkList<String>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<String>> response) {
                hideLoading();
                UIHelper.getInstance().switchActivity(UserPreferencesActivity.this,BottomSheetRideActivity.class,null,null,null,true);
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
                            SessionManager.getInstance().removeUserData(UserPreferencesActivity.this);
                            UIHelper.getInstance().switchActivity(UserPreferencesActivity.this, StartUpActivity.class, null, null, null, true);
                        });
                    }
                    return;

                }
                showErrorPrompt(response);
            }

            @Override
            public void onNullListResponse() {
                hideLoading();
                Toast.makeText(UserPreferencesActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
        }, SessionManager.getAccessToken()).request_driver_posted_ride(parms);
    }

    @Override
    public void onRideSelected(int rideType) {

    }

    @Override
    public void onPlaceSelected(Place place, int locationType) {
        mPickup = place.getLatLng();
        mPickUpLocation.setText(place.getAddress());
    }

    @Override
    public void onCurrentLocationSelected() {

    }
}
