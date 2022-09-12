package com.quawlebs.drupp.view.ui.ride;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.Helper;
import com.quawlebs.drupp.helpers.IActivityHelper;
import com.quawlebs.drupp.helpers.ICancelCallBack;
import com.quawlebs.drupp.helpers.IFragmentChangeListener;
import com.quawlebs.drupp.helpers.IFragmentEventObserver;
import com.quawlebs.drupp.helpers.MapHelper;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.helpers.Timing;
import com.quawlebs.drupp.login.StartUpActivity;
import com.quawlebs.drupp.models.DriverDetails;
import com.quawlebs.drupp.models.DriverPostRide;
import com.quawlebs.drupp.models.MessageEvent;
import com.quawlebs.drupp.models.RideInfo;
import com.quawlebs.drupp.models.RiderDetails;
import com.quawlebs.drupp.models.UserInfo;
import com.quawlebs.drupp.models.UserPostedRideResponse;
import com.quawlebs.drupp.models.rxbus.LocationPicker;
import com.quawlebs.drupp.service.FetchAddressIntentService;
import com.quawlebs.drupp.util.CommonUtils;
import com.quawlebs.drupp.util.LocationUtil;
import com.quawlebs.drupp.util.RxBus;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.util.WorkaroundMapFragment;
import com.quawlebs.drupp.view.ui.NavDrawer;
import com.quawlebs.drupp.view.ui.RideFor;
import com.quawlebs.drupp.view.ui.RideStartActivity;
import com.quawlebs.drupp.view.ui.busbooking.BusBookActivity;
import com.quawlebs.drupp.view.ui.scheduledRides.ScheduledRidesActivity;
import com.quawlebs.drupp.view.ui.search.PlaceSearchActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.google.android.gms.maps.model.JointType.ROUND;

public class BottomSheetRideActivity extends NavDrawer implements OnMapReadyCallback, IActivityHelper, IFragmentChangeListener, IFragmentEventObserver
        , LocationListener, DirectionCallback {


    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private Location currentBestLocation;
    private ImageButton btnEmergency;
    //---------------Globals------------------
    private LatLng fromLatLong;
    private UserInfo userInfo;

    private FirebaseFunctions mFirebaseFunctionInstance;
    private LatLng toLatLong;
    private Location currentLocation;
    private String ride_type;
    private String passengers_preference;
    private String vehicle_type;
    private int rideoption;
    private String basefare;
    private String timeStamp;
    private DatabaseReference mDatabase;
    private HashMap<Integer, Marker> mHashMap = new HashMap<>();
    private String currentRideId;
    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};
    private CountDownTimer ridePostedTimer;
    private FloatingActionButton fabBus;
    private int topLayoutHeight;
    private View mLayoutTopRideType;
    private SharedPreferences sharedPreferences;
    //---------------Views--------------------
    private TextView homeHeader;
    private AppCompatImageView currentLocationIcon;
    private EditText etSource, etDestination;
    private LinearLayout containerSource;
    private CardView cvSearch;
    private CardView containerDesination;
    private CircleImageView profileImageView;
    private MapHelper mapHelper;
    private Disposable placePickerDisposable;
    private List<LatLng> polyLineList;
    private PolylineOptions blackPolylineOptions;
    private PolylineOptions polylineOptions;
    private Polyline greyPolyLine;
    private Polyline blackPolyline;
    private HashMap<String, Marker> markerHashMap = new HashMap<>();
    private boolean mapReady;
    private List<String> nearbyDrivers;


    public static Intent createIntent(Context context, RideInfo rideInfo) {
        Intent intent = new Intent(context, BottomSheetRideActivity.class);
        intent.putExtra(AppConstants.K_RIDE_INFO, rideInfo);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, BottomSheetRideActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    //-------------------------Receivers---------------------

    private AddressResultReceiver resultReceiver;


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            hideLoading();
            try {
                Intent i = new Intent(BottomSheetRideActivity.this, RideStartActivity.class);
                i.putExtra(AppConstants.K_RIDE, parms);
                i.putExtra(AppConstants.K_OTP, intent.getIntExtra(AppConstants.K_OTP, 0));
                i.putExtra(AppConstants.K_RIDE_DETAIL, intent.getStringExtra(AppConstants.K_RIDE_DETAIL));
                startActivity(i);
            } catch (Exception e) {
                Log.d(getClass().getSimpleName(), "Error");
            }

        }
    };


    private BroadcastReceiver rideLater = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.custom_dialog_ridelater_accepted, null);
            TextView driverName = alertLayout.findViewById(R.id.tv_ride_message);
            driverName.setText(getString(R.string.ridelater_accepted, intent.getStringExtra(AppConstants.K_DRIVER_NAME)));

            AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(BottomSheetRideActivity.this);
            AlertDialog alertDialog = alertDialogbuilder.create();
            alertDialog.setView(alertLayout);
            alertDialog.show();
            alertLayout.findViewById(R.id.rideAlert_view).setOnClickListener(v ->
                    {
                        alertDialog.dismiss();
                        startActivity(new Intent(BottomSheetRideActivity.this, ScheduledRidesActivity.class));
                    }
            );
        }
    };

    private BroadcastReceiver rideFromDriver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showRideFromDriverDialog();
        }
    };
    private Dialog searchProgres;

    public BottomSheetRideActivity() {
    }

    private void showRideFromDriverDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_ride, null);
        DriverPostRide driverPostRide = Helper.getInstance(BottomSheetRideActivity.this).readFromJson(AppConstants.DRIVER_POST_RIDE, DriverPostRide.class);
        TextView heading = alertLayout.findViewById(R.id.tvRide_for);
        TextView tvdate = alertLayout.findViewById(R.id.tvdate);
        TextView source = alertLayout.findViewById(R.id.tvSourcePlace);
        TextView destination = alertLayout.findViewById(R.id.tvDestinationPlace);
        TextView fare = alertLayout.findViewById(R.id.tvBillAmount);

        fare.setText(getString(R.string.fare_pop_up, driverPostRide.getDriverPost_total_fare()));
        String date = Timing.getTimeInString(Long.parseLong(driverPostRide.getDriverPost_ride_date()), Timing.TimeFormats.CUSTOM_DATE_TIME);
        destination.setText(driverPostRide.getDriverPost_destination());
        source.setText(driverPostRide.getDriverPost_source());
        //heading.setText(getString(R.string.ride_for_abuja, driverPostRide.getDriverPost_destination()));
        tvdate.setText(getString(R.string._06_june_2019, date));

        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(BottomSheetRideActivity.this);
        AlertDialog alertDialog = alertDialogbuilder.create();
        alertDialog.setView(alertLayout);
        alertDialog.show();
        alertLayout.findViewById(R.id.rideAlert_view).setOnClickListener(v -> {
                    alertDialog.dismiss();
                    startActivity(new Intent(BottomSheetRideActivity.this, RideFor.class));
                }
        );
    }

    //TODO : CHANGE LAYOUT BASED ON CONDITION
    private BroadcastReceiver prefChange = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.custom_dialog_ridelater_accepted, null);
            TextView rideMessage = alertLayout.findViewById(R.id.tv_ride_message);

            //TODO CHANGE IMPLEMENTATION BASED ON CONDITION
            HashMap<String, String> map = Helper.getRideLaterDetails(BottomSheetRideActivity.this);
            String finalDate = Timing.getTimeInString(Long.parseLong(Objects.requireNonNull(map.get(AppConstants.K_TIME_RL))), Timing.TimeFormats.CUSTOM_DATE_TIME);
            HashMap<String, String> mapSourceDest = Helper.getSourceDestination(BottomSheetRideActivity.this);
            if (intent != null) {
                rideMessage.setText(getString(R.string.ridelater_accepted, intent.getStringExtra(AppConstants.K_DRIVER_NAME)));
            }

            AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(BottomSheetRideActivity.this);
            AlertDialog alertDialog = alertDialogbuilder.create();
            alertDialog.setView(alertLayout);
            alertDialog.show();
            alertLayout.findViewById(R.id.rideAlert_view).setOnClickListener(v -> {
                startActivity(new Intent(BottomSheetRideActivity.this, ScheduledRidesActivity.class));
                alertDialog.dismiss();
            });
        }
    };


    private BroadcastReceiver cancelRide = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showCancelledRideDialog();
        }
    };
    private HashMap<String, Object> parms = new HashMap<>();
    private RideInfo currentRideInfo;


    private void showCancelledRideDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_ride_canceled_by_driver, null);
        TextView tvCancelRide = alertLayout.findViewById(R.id.tvCancelRide);
        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(BottomSheetRideActivity.this);
        final AlertDialog alertDialog = alertDialogbuilder.create();
        tvCancelRide.setText(getString(R.string.ride_canceled_text));
        alertDialog.setView(alertLayout);
        alertDialog.show();

        alertLayout.findViewById(R.id.btYesCancel).setOnClickListener(v -> {
                    alertDialog.dismiss();
                    currentLocation();

                }
        );
    }


    private BroadcastReceiver third = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.dialog_ride_status, null);
            TextView heading = alertLayout.findViewById(R.id.tvRide_for);
            TextView rideMessage = alertLayout.findViewById(R.id.tv_ride_message);
            AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(BottomSheetRideActivity.this);
            final AlertDialog alertDialog = alertDialogbuilder.create();
            heading.setText(R.string.ride_confirmed_by_driver);
            int status = intent.getIntExtra(AppConstants.K_STATUS, 3);
            if (status == 3) {
                //Ride Rejected
                rideMessage.setText(R.string.ride_rejected_by_driver);
            } else {
                rideMessage.setText(R.string.ride_confirmed_by_driver);
            }
            alertDialog.setView(alertLayout);
            alertDialog.show();

            alertLayout.findViewById(R.id.rideAlert_view).setOnClickListener(v -> alertDialog.dismiss());
        }
    };
    private BroadcastReceiver rating = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            sharedPreferences = getSharedPreferences("Ratings", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            //if (!sharedPreferences.getBoolean("status", false)) {
            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_ratings, null);
            DriverPostRide driverPostRide = Helper.getInstance(BottomSheetRideActivity.this).readFromJson(AppConstants.DRIVER_POST_RIDE, DriverPostRide.class);
            TextView by = alertLayout.findViewById(R.id.by_ratings);
            TextView msg = alertLayout.findViewById(R.id.msg_ratings);
            RatingBar ratingBar = alertLayout.findViewById(R.id.rateBar_ratings);
            AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(BottomSheetRideActivity.this);
            final AlertDialog alertDialog = alertDialogbuilder.create();
            by.setText(driverPostRide.getDriverPost_driverName());
            msg.setText(sharedPreferences.getString("msg", ""));
            ratingBar.setRating(Integer.parseInt(Objects.requireNonNull(sharedPreferences.getString("rate", ""))));
            alertDialog.setView(alertLayout);
            //editor.putBoolean("status",true);
            editor.apply();
            alertDialog.show();
            alertLayout.findViewById(R.id.btOk).setOnClickListener(v -> alertDialog.dismiss());
        }
        //}
    };

    private BroadcastReceiver rideNotFoundReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            hideProgressBarWithCancel();
            onNoDriverFound();
        }
    };


    private boolean doubleBackToExitPressedOnce;


    public enum DATABASE_REF {
        RIDERS,
        DRIVERS,
        RIDE_INFO
    }


    @Override
    protected void setUp() {


        mFirebaseFunctionInstance = FirebaseFunctions.getInstance();
        userInfo = SessionManager.getInstance().loadUser(this).getUserInfo();
        if (userInfo != null) {
            Glide.with(BottomSheetRideActivity.this).load(AppConstants.IMAGE_URL + userInfo.getProfilePicture()).apply(new RequestOptions()
                    .error(R.drawable.user_profile_icon)
                    .centerCrop().placeholder(R.drawable.rotate_spinner)).into(profileImageView);
            homeHeader.setText(getString(R.string.welcome_text_home, getString(R.string.full_name, userInfo.getFirstName(), userInfo.getLastName())));
        }
        //Setting Up Map
        mapFragment = (WorkaroundMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.image_Map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            mapFragment.getMapAsync(googleMap -> ((WorkaroundMapFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.image_Map)))
                    .setListener(() -> {
                    }));
        }
        switchBottom();
        //
        RelativeLayout mBottomSheet = findViewById(R.id.ride_bottom_sheet);


        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_bottom_sheet_home);
            if (fragment instanceof RideVehicleSingle||fragment instanceof RideVehiclePool) {
                //   mBottomSheetBehavior.setPeekHeight(535);
                Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
                containerSource.startAnimation(slideUp);
                slideUp.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        containerSource.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                currentLocationIcon.setVisibility(View.GONE);

                containerDesination.setVisibility(View.GONE);
            } else if (fragment instanceof RideData) {
                currentLocationIcon.setVisibility(View.GONE);
            } else if (fragment instanceof RideType) {
                currentLocationIcon.setVisibility(View.VISIBLE);
                containerSource.setVisibility(View.VISIBLE);
                containerDesination.setVisibility(View.VISIBLE);
            }
        });

        currentLocationIcon.setOnClickListener(v -> {
            currentLocation();

        });
        resultReceiver = new AddressResultReceiver(new Handler());
        setupPlaces();
        initPlaces();
//        mLayoutTopRideType = findViewById(R.id.top_container);
//        ViewTreeObserver vto = mLayoutTopRideType.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                mLayoutTopRideType.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                topLayoutHeight = mLayoutTopRideType.getMeasuredHeight();
//            }
//        });

        fabBus.setOnClickListener(v -> {
            UIHelper.getInstance().switchActivity(this, BusBookActivity.class, null, null, null, false);
        });
        mDatabase = FirebaseDatabase.getInstance().getReference().child(AppConstants.FIREBASE_DATABASE.USERS);
        //Syncs From Firebase Drivers Location
        driversFromRealTimeDB();

        RxBus.getInstance().getIntentEvent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof MessageEvent) {
                        Glide.with(this).load(AppConstants.IMAGE_URL + ((MessageEvent) o).getMessage()).apply(new RequestOptions()
                                .error(R.drawable.ic_user_search)
                                .centerCrop().placeholder(R.drawable.rotate_spinner)).into(profileImageView);
                    }
                });

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stub.setLayoutResource(R.layout.bottom_sheet_ride_layout);
        stub.inflate();

        btnEmergency = findViewById(R.id.btn_emergency);
        etSource = findViewById(R.id.etDeparture);
        containerSource = findViewById(R.id.container_source);
        currentLocationIcon = findViewById(R.id.get_current_location);
        etDestination = findViewById(R.id.etDestination);
        profileImageView = findViewById(R.id.profile_in_search);
        homeHeader = findViewById(R.id.tv_home_header);
        cvSearch = findViewById(R.id.cv_search);
        containerDesination = findViewById(R.id.cv_destination);
        fabBus = findViewById(R.id.fab_bus);
        //Progress bar With Cancel
        searchProgres = CommonUtils.showDialogCancelProgressBar(this, new ICancelCallBack() {
            @Override
            public void onCancel() {
                BottomSheetRideActivity.this.cancelAPI();
            }
        });
//        searchProgres.findViewById(R.id.btn_cancel).setOnClickListener(v -> searchProgres.dismiss());

        //Resgistering Receiver
        registerReceiver(cancelRide, new IntentFilter(AppConstants.I_CANCEL_RIDE));
        registerReceiver(broadcastReceiver, new IntentFilter(AppConstants.I_RIDE_ACCEPTED));
        registerReceiver(rideFromDriver, new IntentFilter(AppConstants.I_RIDE_FROM_DRIVER));
        registerReceiver(third, new IntentFilter(AppConstants.I_DRIVER_ACCEPTED_RIDE));
        registerReceiver(prefChange, new IntentFilter(AppConstants.I_PREF_CHANGE));
        registerReceiver(rideLater, new IntentFilter(AppConstants.I_RIDE_LATER));
        registerReceiver(rideNotFoundReceiver, new IntentFilter(AppConstants.I_RIDE_NOT_FOUND));

        //Listen for location picker
        placePickerDisposable = RxBus.getInstance().getIntentEvent().subscribe(o -> {
            if (o instanceof LocationPicker) {
                LocationPicker locationPicker = (LocationPicker) o;

                if (((LocationPicker) o).getSourcePlace() != null) {
                    etSource.setText(locationPicker.getSourcePlace().getAddress());
                    parms.put(AppConstants.K_SOURCE, locationPicker.getSourcePlace().getAddress());
                    fromLatLong = locationPicker.getSourcePlace().getLatLng();
                }
                if (((LocationPicker) o).getDestinationPlace() != null) {

                    etDestination.setText(locationPicker.getDestinationPlace().getAddress());
                    parms.put(AppConstants.K_DEST, locationPicker.getDestinationPlace().getAddress());
                    toLatLong = locationPicker.getDestinationPlace().getLatLng();
                }
                if (((LocationPicker) o).getLocationType() == AppConstants.CURRENT_LOCATION) {
                    currentLocation();
                }

                Fragment fragment = getSupportFragmentManager().findFragmentByTag(RideType.class.getSimpleName());
                if (!etSource.getText().toString().trim().isEmpty() && !etDestination.getText().toString().trim().isEmpty()) {
                    if (etSource.getText().toString().trim().equalsIgnoreCase(etDestination.getText().toString().trim())) {

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
                routing(fromLatLong, toLatLong);
            }


        });
        //Check if notification
        if (getIntent().getExtras() != null) {
            currentRideInfo = getIntent().getParcelableExtra(AppConstants.K_RIDE_INFO);
            if (currentRideInfo != null) {
                switch (currentRideInfo.getStatus()) {
                    case AppConstants.RIDE_STATUS.RIDE_POSTED:
                        showRideFromDriverDialog();
                        break;
                }
            }
        }
        //Notify Admin
        btnEmergency.setOnClickListener(v -> emergencyAction());
        //  new Handler().postDelayed(() -> clearMap(), 2000);

    }

    private void clearMap() {

        for (Map.Entry<String, Marker> entry : markerHashMap.entrySet()) {
            entry.getValue().remove();
        }
        markerHashMap.clear();
        if (greyPolyLine != null && blackPolyline != null) {
            greyPolyLine.remove();
            blackPolyline.remove();
        }
        if (currentBestLocation != null) {
            LatLng cLoc = new LatLng(currentBestLocation.getLatitude(), currentBestLocation.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cLoc, 14f));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapReady = false;
        placePickerDisposable.dispose();
        try {
            unregisterReceiver(broadcastReceiver);
            unregisterReceiver(rideFromDriver);
            unregisterReceiver(third);
            unregisterReceiver(rideNotFoundReceiver);
            //unregisterReceiver(rating);
            unregisterReceiver(prefChange);
            unregisterReceiver(cancelRide);
            unregisterReceiver(rideLater);
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onBackPressed() {
        //Fragment Back Stack Management
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_bottom_sheet_home);

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            if (fragment instanceof RideType) {
                showExit();
                return;

            }
            if (fragment instanceof RideData) {
                getSupportFragmentManager().popBackStack(AppConstants.RIDE_VEHICLE_BACK_STACK, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else {
                super.onBackPressed();
            }
        } else {

            if (doubleBackToExitPressedOnce || getSupportFragmentManager().getBackStackEntryCount() != 0) {
                //  super.onBackPressed();
                finish();
                return;
            }

            showExit();

        }

    }


    private void showExit() {
        this.doubleBackToExitPressedOnce = true;
        showMessage(getString(R.string.please_click_back_again));
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    private void switchBottom() {
        RideType rideType = RideType.newInstance();
        rideType.setiActivityHelper(this);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_bottom_sheet_home, rideType, RideType.class.getSimpleName())
                .commit();
    }


    private void setupPlaces() {
        etSource.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(AppConstants.K_LOCATION_TYPE, AppConstants.HOME_LOCATION);
            bundle.putString(AppConstants.SEARCH_DIALOG_TITLE, getString(R.string.enter_pickup_location));
            //        UIHelper.getInstance().showPlaceSearchDialog(this, bundle);
            RxBus.getInstance().setIntentEvent(new LocationPicker(AppConstants.HOME_LOCATION, etDestination.getText().toString().trim(), etSource.getText().toString().trim()));

            UIHelper.getInstance().switchActivity(this, PlaceSearchActivity.class, null, null, null, false);

        });
        etSource.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                Bundle bundle = new Bundle();
                bundle.putInt(AppConstants.K_LOCATION_TYPE, AppConstants.HOME_LOCATION);
                bundle.putString(AppConstants.SEARCH_DIALOG_TITLE, getString(R.string.enter_pickup_location));

                RxBus.getInstance().setIntentEvent(new LocationPicker(AppConstants.HOME_LOCATION, etDestination.getText().toString().trim(), etSource.getText().toString().trim()));
//                UIHelper.getInstance().showPlaceSearchDialog(this, bundle);
                UIHelper.getInstance().switchActivity(this, PlaceSearchActivity.class, null, null, null, false);

            } else {
                hideKeyboard();
            }
        });
        etDestination.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                Bundle b = new Bundle();
                b.putInt(AppConstants.K_LOCATION_TYPE, AppConstants.WORK_LOCATION);
                b.putString(AppConstants.SEARCH_DIALOG_TITLE, getString(R.string.enter_drop_location));
                RxBus.getInstance().setIntentEvent(new LocationPicker(AppConstants.WORK_LOCATION, etSource.getText().toString().trim(), etDestination.getText().toString().trim()));
//                UIHelper.getInstance().showPlaceSearchDialog(this, b);
                UIHelper.getInstance().switchActivity(this, PlaceSearchActivity.class, null, null, null, false);

            } else {
                hideKeyboard();
            }
        });
        etDestination.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putInt(AppConstants.K_LOCATION_TYPE, AppConstants.WORK_LOCATION);
            b.putString(AppConstants.SEARCH_DIALOG_TITLE, getString(R.string.enter_drop_location));
            RxBus.getInstance().setIntentEvent(new LocationPicker(AppConstants.WORK_LOCATION, etSource.getText().toString().trim(), etDestination.getText().toString().trim()));
//            UIHelper.getInstance().showPlaceSearchDialog(this, b);
            UIHelper.getInstance().switchActivity(this, PlaceSearchActivity.class, null, null, null, false);
        });

        //      Helper.saveSourceDestination(hashMap, this);
    }

    private void initPlaces() {
        Places.initialize(this, getString(R.string.google_maps_key));
        PlacesClient placesClient = Places.createClient(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapReady = true;
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.clear();

        MapStyleOptions mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.uber_style);
        mMap.setMapStyle(mapStyleOptions);

        mapHelper = new MapHelper(googleMap);
        mapHelper.setMyLocationButtonEnabled(true);
        currentLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public LatLng getSource() {
        return fromLatLong;
    }

    @Override
    public LatLng getDestination() {
        return toLatLong;
    }

    @Override
    public void onNoDriverFound() {
        hideLoading();
        showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
        if (mAlertDialog != null) {
            mAlertDialog.setCancelable(false);
            TextView dialogTitle = mAlertDialog.findViewById(R.id.tv_title);
            dialogTitle.setText(getString(R.string.no_drivers_nearby_to_accept_your_ride));
            mAlertDialog.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
                mAlertDialog.dismiss();
                onBackPressed();
                //getSupportFragmentManager().popBackStack(RideType.class.getSimpleName(), 0);
            });
        }

    }

    @Override
    public int setPickerStatus() {
        if (etSource.getText().toString().trim().isEmpty())
            return 1;
        if (etDestination.getText().toString().trim().isEmpty())
            return 2;

        return 3;
    }

    @Override
    public void changeType(int rideType, int poolOption, int rideOption) {
        ride_type = String.valueOf(rideType);
        passengers_preference = String.valueOf(poolOption);
        this.rideoption = rideOption;
    }

    @Override
    public void changeVehicleType(int vehicleType) {
        vehicle_type = String.valueOf(vehicleType);
    }

    @Override
    public HashMap<String, Object> getData() {
        return parms;
    }

    @Override
    public void post() {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put(AppConstants.K_LATITUDE, fromLatLong.latitude);
        data.put(AppConstants.K_LONGITUDE, fromLatLong.longitude);

        showProgressBarWithCancel();
        Task<ArrayList<String>> result = mFirebaseFunctionInstance
                .getHttpsCallable("getDrivers")
                .call(data)
                .continueWith(task -> {
                    // This continuation runs on either success or failure, but if the task
                    // has failed then getResult() will throw an Exception which will be
                    // propagated down.

                Object results= task.getResult().getData();

                    return (ArrayList<String>) task.getResult().getData();
                });

        result.addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Exception e = task.getException();
                if (e instanceof FirebaseFunctionsException) {
                    hideProgressBarWithCancel();
                    FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                    FirebaseFunctionsException.Code code = ffe.getCode();
                //    Object details = ffe.getDetails();
                //    showMessage(e.getMessage());

                    ArrayList<String> allDrivers =new ArrayList<>();
                    //allDrivers.add("30");
                    //allDrivers.add("31");
                    //allDrivers.add("247");

                    bookRide(allDrivers);
                }

            } else {
                ArrayList<String> allDrivers = task.getResult();
                bookRide(allDrivers);
            }
        });
    }

    private void bookRide(ArrayList<String> drivers) {

        sharedPreferences = getSharedPreferences("userPickupLocation", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userSource_lat", String.valueOf(fromLatLong.latitude));
        editor.putString("userSource_long", String.valueOf(fromLatLong.longitude));
        editor.putString("userDes_lat", String.valueOf(toLatLong.latitude));
        editor.putString("userDes_long", String.valueOf(toLatLong.longitude));
        editor.apply();
        DruppRequestHandler.clearInstance();
        parms.put(AppConstants.K_SOURCE, etSource.getText().toString());
        parms.put(AppConstants.K_SOURCE_LAT, String.valueOf(fromLatLong.latitude));
        parms.put(AppConstants.K_SOURCE_LONG, String.valueOf(fromLatLong.longitude));
        parms.put(AppConstants.K_DEST, etDestination.getText().toString());
        parms.put(AppConstants.K_DEST_LAT, String.valueOf(toLatLong.latitude));
        parms.put(AppConstants.K_DEST_LONG, String.valueOf(toLatLong.longitude));
        parms.put(AppConstants.K_RIDE_TYPE, ride_type);
        parms.put(AppConstants.K_RIDE_OPTION, String.valueOf(rideoption));
        parms.put(AppConstants.K_VEHICLE_TYPE, vehicle_type);
        parms.put(AppConstants.K_RIDE_DATE, timeStamp);
        parms.put(AppConstants.K_USER_FARE, basefare);
        parms.put(AppConstants.K_PASSENGER_PREFRENCE, passengers_preference);
        parms.put(AppConstants.K_DRIVERS, drivers);
        parms.put(AppConstants.K_PAYMENT_OPTION, Helper.getPaymentMethod(this).getType());
        saveAndUpdateRealtimeLocation(fromLatLong.latitude, fromLatLong.longitude);


        DruppRequestHandler.getInstance(new INetwork<UserPostedRideResponse>() {
            @Override
            public void onResponse(Response<QualStandardResponse<UserPostedRideResponse>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        currentRideId = response.body().getResponse().getRideId();
                        FirebaseMessaging.getInstance().subscribeToTopic("messages" + currentRideId);

                        Helper.saveRideId(Integer.parseInt(response.body().getResponse().getRideId()), BottomSheetRideActivity.this);
                        if (rideoption == 2) {
                            hideLoading();
                            showRideLaterPostedDialog();
                        }
                    } catch (Exception e) {

                    }


                }
            }

            @Override
            public void onError(Response<QualStandardResponse<UserPostedRideResponse>> response) {
                hideProgressBarWithCancel();

                if (response.code() == 401) {
                    showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
                    if (mAlertDialog != null) {
                        mAlertDialog.setCancelable(false);
                        TextView title = mAlertDialog.findViewById(R.id.tv_title);
                        title.setText(getString(R.string.your_session_has_expired));
                        Button btnOk = mAlertDialog.findViewById(R.id.btn_cancel);
                        btnOk.setText(getString(R.string.login));
                        btnOk.setOnClickListener(v -> {
                            hideAlertDialog();
                            SessionManager.getInstance().removeUserData(BottomSheetRideActivity.this);
                            UIHelper.getInstance().switchActivity(BottomSheetRideActivity.this, StartUpActivity.class, null, null, null, true);
                        });
                    }
                    return;

                }
                //               showErrorPrompt(response);

            }

            @Override
            public void onNullResponse() {
                hideProgressBarWithCancel();
            }

            @Override
            public void onFailure(Throwable t) {

                Log.e("ropppp", String.valueOf(t));
                hideProgressBarWithCancel();
               // showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        post();
                    });
                }
            }
        }, SessionManager.getAccessToken()).userPostRide(parms);
    }
    public void scheduleRide(){

    }

    //Update location to firebase
    private void saveAndUpdateRealtimeLocation(double lat, double lng) {
        RiderDetails riderDetails = new RiderDetails(userInfo.getId(), lat, lng);
        updateFirebaseDatabaseReferences(DATABASE_REF.RIDERS).child(String.valueOf(riderDetails.getId())).setValue(riderDetails);


    }

    //Emergency Action
    private void emergencyAction() {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<HashMap<String, String>>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<HashMap<String, String>>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    showAdminNotifiedDialog();
                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<HashMap<String, String>>> response) {
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
                            hideAlertDialog();
                            SessionManager.getInstance().removeUserData(BottomSheetRideActivity.this);
                            UIHelper.getInstance().switchActivity(BottomSheetRideActivity.this, StartUpActivity.class, null, null, null, true);
                        });
                    }
                    return;

                }
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
                        emergencyAction();
                    });
                }
            }
        }, SessionManager.getAccessToken()).emergencyAction();

    }

    private void showAdminNotifiedDialog() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
        sweetAlertDialog.setTitleText(getString(R.string.admin_notified))
                .setConfirmText(getString(R.string.ok))
                .show();
    }

    //Cancel Ride
    public void cancelAPI() {
        DruppRequestHandler.clearInstance();

        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstants.K_CANCEL_REASON, "4");
        params.put(AppConstants.K_RIDE_ID, currentRideId);
        DruppRequestHandler.getInstance(new INetworkList<String>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<String>> response) {
                hideProgressBarWithCancel();
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        getSupportFragmentManager().popBackStackImmediate();
                        Helper.removeRideId(BottomSheetRideActivity.this);

                    } catch (Exception e) {

                    }

                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<String>> response) {
                hideProgressBarWithCancel();
                if (response.code() == 401) {
                    showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
                    if (mAlertDialog != null) {
                        mAlertDialog.setCancelable(false);
                        TextView title = mAlertDialog.findViewById(R.id.tv_title);
                        title.setText(getString(R.string.your_session_has_expired));
                        Button btnOk = mAlertDialog.findViewById(R.id.btn_cancel);
                        btnOk.setText(getString(R.string.login));
                        btnOk.setOnClickListener(v -> {
                            SessionManager.getInstance().removeUserData(BottomSheetRideActivity.this);
                            UIHelper.getInstance().switchActivity(BottomSheetRideActivity.this, StartUpActivity.class, null, null, null, true);
                        });
                    }
                    return;

                }
                showErrorPrompt(response);
            }

            @Override
            public void onNullListResponse() {
                hideProgressBarWithCancel();
            }

            @Override
            public void onFailureList(Throwable t) {
                hideProgressBarWithCancel();
                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        cancelAPI();
                    });
                }

            }
        }, SessionManager.getAccessToken()).cancelRide(params);
    }

    private void showProgressBarWithCancel() {
        if (!searchProgres.isShowing()) {
            searchProgres.show();
        }
    }

    private void hideProgressBarWithCancel() {
        searchProgres.dismiss();
    }

    private void showRideLaterPostedDialog() {
        hideProgressBarWithCancel();
        showAlertDialog(R.layout.custom_dialog_ride_later_posted, AppConstants.NotificationType.PAYMENT_SUCCESS);

    }

    @Override
    public void timestamp(String stamp, String fare) {
        basefare = fare;
        timeStamp = stamp;
    }


    @Override
    public <T> void onFragmentChanged(T clazz) {
        if (!(clazz instanceof RideType)) {
            //          fabBus.hide();
        } else {
//            fabBus.show();
        }
    }

    @Override
    public void onRideSelected(int rideType) {

    }

    @Override
    public void onPlaceSelected(Place place, int locationType) {
        if (locationType == AppConstants.HOME_LOCATION) {
//            sc = true;
            etSource.setText(place.getAddress());
            fromLatLong = place.getLatLng();
            parms.put(AppConstants.K_SOURCE, place.getAddress());
            //         current_location_flag = true;
        } else {
            parms.put(AppConstants.K_DEST, place.getAddress());
//            des = true;
            //       current_location_flag = true;
            etDestination.setText(place.getAddress());
            toLatLong = place.getLatLng();
        }
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(RideType.class.getSimpleName());
        if (!etSource.getText().toString().trim().isEmpty() && !etDestination.getText().toString().trim().isEmpty()) {
            if (etSource.getText().toString().trim().equalsIgnoreCase(etDestination.getText().toString().trim())) {

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

        routing(fromLatLong, toLatLong);
    }

    @Override
    public void onCurrentLocationSelected() {
        currentLocation();

    }

    public void currentLocation() {
        if (mapReady) {
            //Better approach to get locaiton
            clearMap();

            fromLatLong = null;
            etSource.setText("");

            toLatLong = null;
            etDestination.setText("");

            currentLocation = fetchCurrentLocation();
            if (currentLocation != null) {

                getAddress(currentLocation);
                fromLatLong = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                routing(fromLatLong, toLatLong);
            }
            removeRouteBetweenMarkers();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.REQUEST_ACCESS_LOCATION) {
            if (resultCode == Activity.RESULT_OK) {
                Location loc = fetchCurrentLocation();

                etSource.setText("");
                if (loc != null) {
                    getAddress(loc);
                }

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    public void getAddress(Location lastLocation) {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(AppConstants.RECEIVER, resultReceiver);
        intent.putExtra(AppConstants.LOCATION_DATA_EXTRA, lastLocation);
        startService(intent);

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
                                            BottomSheetRideActivity.this,
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
                if (currentBestLocation == null) {
                    currentBestLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                }
                return currentBestLocation;
            }

        }
    }

    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            currentBestLocation = location;
            saveAndUpdateRealtimeLocation(location.getLatitude(), location.getLongitude());
//            if (isBetterLocation(location, currentBestLocation)) {
//
//                fromLatLong = new LatLng(location.getLatitude(), location.getLongitude());
//            } else {
//                toLatLong = new LatLng(currentBestLocation.getLatitude(), currentBestLocation.getLongitude());
//            }
//
//            routing(fromLatLong, toLatLong);
        }
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

    private void removeRouteBetweenMarkers() {
        try {
            if (greyPolyLine != null && blackPolyline != null) {
                greyPolyLine.remove();
                blackPolyline.remove();
            }
            etDestination.setText("");

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentBestLocation.getLatitude(), currentBestLocation.getLongitude()), 16f));
            mMap.setLatLngBoundsForCameraTarget(null);
        } catch (Exception e) {

        }
    }

    public void routing(LatLng source, LatLng destination) {
        try {
            //TODO: Change Implementation of adding source marker
            if (source != null && destination == null) {
                updateSourceMarker(source);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(source, 16f));
                // mMap.setLatLngBoundsForCameraTarget(null);
            } else if (destination != null && source == null) {
                updateDestinationMarker(destination);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destination, 16f));
            } else {
                updateSourceMarker(source);
                updateDestinationMarker(destination);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                //builder.include(source);
                // builder.include(destination);
                for (Map.Entry<String, Marker> entry : markerHashMap.entrySet()) {
                    builder.include(entry.getValue().getPosition());
                }
                LatLngBounds bounds = builder.build();

                //           Find height of bottom layout
                int width = getResources().getDisplayMetrics().widthPixels - topLayoutHeight;
                int height = getResources().getDisplayMetrics().heightPixels - topLayoutHeight;
                final int minMetric = Math.min(width, height);
                int padding = (int) (minMetric * 0.2);
                //    int padding = (int) (height * 0.2);
                double distanceBetweenLatLongs_kilometers = mapHelper.getDistanceBetweenLatLongs_Kilometers(source, destination);
                if (distanceBetweenLatLongs_kilometers > 10) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(source, 14f));

                } else {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
                    mMap.setLatLngBoundsForCameraTarget(bounds);
                }

                GoogleDirection.withServerKey(getString(R.string.google_maps_key))
                        .from(source)
                        .to(destination)
                        .avoid(AvoidType.FERRIES)
                        .transportMode(TransportMode.DRIVING)
                        .transportMode(TransportMode.WALKING)
                        .alternativeRoute(false)
                        .optimizeWaypoints(true)
                        .execute(this);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private BitmapDescriptor bitmapDescriptorFromVector(int vectorResId) {

        Drawable vectorDrawable = AppCompatResources.getDrawable(this, vectorResId);
        if (vectorDrawable != null) {
            vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        }
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable != null ? vectorDrawable.getIntrinsicWidth() : 0, vectorDrawable != null ? vectorDrawable.getIntrinsicHeight() : 0, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        if (vectorDrawable != null) {
            vectorDrawable.draw(canvas);
        }
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onDirectionSuccess(Direction direction) {
        String status = direction.getStatus();
        if (status.equalsIgnoreCase(RequestResult.OK)) {


            List<Route> routeList = direction.getRouteList();
            for (Route route : routeList) {
                polyLineList = route.getOverviewPolyline().getPointList();
                updateRoute(polyLineList);
            }
            setCameraWithCoordinationBounds( direction.getRouteList().get(0));

        } else if (status.equalsIgnoreCase(RequestResult.NOT_FOUND)) {

        }

    }
    private void setCameraWithCoordinationBounds( Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds =new LatLngBounds(southwest, northeast);
        //Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
    }
    private void updateRoute(List<LatLng> polyLineList) {
        if (greyPolyLine == null || blackPolyline == null) {

            polylineOptions = new PolylineOptions();
            polylineOptions.color(Color.GRAY);
            polylineOptions.width(5);
            polylineOptions.startCap(new SquareCap());
            polylineOptions.endCap(new SquareCap());
            polylineOptions.jointType(ROUND);
            polylineOptions.addAll(polyLineList);
            greyPolyLine = mMap.addPolyline(polylineOptions);

            blackPolylineOptions = new PolylineOptions();
            blackPolylineOptions.width(5);
            blackPolylineOptions.color(Color.BLACK);
            blackPolylineOptions.startCap(new SquareCap());
            blackPolylineOptions.endCap(new SquareCap());
            blackPolylineOptions.jointType(ROUND);
            blackPolyline = mMap.addPolyline(blackPolylineOptions);

        } else {
            greyPolyLine.setPoints(polyLineList);
        }
        updateSourceMarker(greyPolyLine.getPoints().get(0));


        ValueAnimator polylineAnimator = ValueAnimator.ofInt(0, 100);
        polylineAnimator.setDuration(2000);
        polylineAnimator.setInterpolator(new LinearInterpolator());
        polylineAnimator.addUpdateListener(valueAnimator -> {
            List<LatLng> points = greyPolyLine.getPoints();
            int percentValue = (int) valueAnimator.getAnimatedValue();
            int size = points.size();
            int newPoints = (int) (size * (percentValue / 100.0f));
            List<LatLng> p = points.subList(0, newPoints);
            blackPolyline.setPoints(p);

        });
        polylineAnimator.start();
        updateDestinationMarker(greyPolyLine.getPoints().get(greyPolyLine.getPoints().size() - 1));

    }

    private void updateDestinationMarker(LatLng newLatLng) {


        Marker marker = markerHashMap.get(AppConstants.MARKERS.DESTINATION_MARKER);
        if (marker == null) {
            MarkerOptions markerOptions = new MarkerOptions().position(newLatLng).flat(true).icon(bitmapDescriptorFromVector(R.drawable.ic_pin_des));
            marker = mMap.addMarker(markerOptions);
            markerHashMap.put(AppConstants.MARKERS.DESTINATION_MARKER, marker);
        } else {
            marker.setPosition(newLatLng);
        }

    }


    private void updateSourceMarker(LatLng newLatLng) {

        Marker marker = markerHashMap.get(AppConstants.MARKERS.SOURCE_MARKER);
        if (marker == null) {
            MarkerOptions markerOptions = new MarkerOptions().position(newLatLng).flat(true).icon(bitmapDescriptorFromVector(R.drawable.ic_green_maker));
            marker = mMap.addMarker(markerOptions);
            markerHashMap.put(AppConstants.MARKERS.SOURCE_MARKER, marker);
        } else {
            marker.setPosition(newLatLng);
        }


    }


    @Override
    public void onDirectionFailure(Throwable t) {


        currentLocation();
        showMessage(R.string.service_not_available);
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
                etSource.setText(addressOutput);
                parms.put(AppConstants.K_SOURCE, addressOutput);
            }


        }

    }

    //--------------------Map Helper Methods------------------------------

    //--------------------FireBase Database Helper Methods-----------------
    private DatabaseReference updateFirebaseDatabaseReferences(DATABASE_REF value) {
        return mDatabase.child(value.toString().toLowerCase());
    }

    private DatabaseReference baseDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference().child(AppConstants.FIREBASE_DATABASE.USERS);
    }

    private void showRideDetails() {
        updateFirebaseDatabaseReferences(DATABASE_REF.RIDE_INFO);
    }

    private List<String> getNearbyDrivers() {
        mDatabase.child(AppConstants.FIREBASE_DATABASE.DRIVERS).child(AppConstants.FIREBASE_DATABASE.DRIVER_DETAILS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {

                    nearbyDrivers = new ArrayList<>();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        DriverDetails driverDetail = child.getValue(DriverDetails.class);
                        LatLng latLng1 = new LatLng(driverDetail.getLatitude(), driverDetail.getLongitude());
                        LatLng latLng2 = new LatLng(currentBestLocation.getLatitude(), currentBestLocation.getLongitude());
                        if (mapHelper.getDistanceBetweenLatLongs_Kilometers(latLng1, latLng2) < AppConstants.DEFAULT_RANGE) {
                            nearbyDrivers.add(child.getKey());
                        }
                    }


                } catch (Exception e) {
                    Log.e("Exception: ", e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return nearbyDrivers;

    }


    //TODO : implement 3 km radius
    private void driversFromRealTimeDB() {
        mDatabase.child(AppConstants.FIREBASE_DATABASE.DRIVERS).child(AppConstants.FIREBASE_DATABASE.DRIVER_DETAILS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    List <DriverDetails> nearbyDrivers = new ArrayList<>();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        DriverDetails driverDetail = child.getValue(DriverDetails.class);
                        LatLng latLng1 = new LatLng(driverDetail.getLatitude(), driverDetail.getLongitude());
                        LatLng latLng2 = new LatLng(currentBestLocation.getLatitude(), currentBestLocation.getLongitude());
                        if (mapHelper.getDistanceBetweenLatLongs_Kilometers(latLng1, latLng2) < AppConstants.DEFAULT_RANGE) {
                            nearbyDrivers.add(driverDetail);
                        }
                    }

                    updateMarkersToMap(nearbyDrivers);
                } catch (Exception e) {
                    Log.e("Exception: ", e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void updateMarkersToMap(List<DriverDetails> drivers) {
        try {
            if (drivers.get(0) == null) drivers.remove(0);
            for (DriverDetails details : drivers) {
                if (details.getAvailability() == 1) {
                    if (mHashMap.containsKey(details.getId())) {
                        mapHelper.animateMarker(mHashMap.get(details.getId()), new LatLng(details.getLatitude(), details.getLongitude()), (float) details.getBearing(), false);
                        continue;
                    }
                    LatLng driver = new LatLng(details.getLatitude(), details.getLongitude());
                    Marker marker;
                    if (details.getType() != null && details.getType() == AppConstants.DRIVER_TYPE.KEKE) {
                        marker = mMap.addMarker(new MarkerOptions().position(driver).icon(bitmapDescriptorFromVector(R.drawable.ic_rickshaw)));
                    } else {
                        marker = mMap.addMarker(new MarkerOptions().position(driver).icon(bitmapDescriptorFromVector(R.drawable.ic_car_driver)));

                    }
                    mHashMap.put(details.getId(), marker);
                } else {
                    Marker marker = mHashMap.get(details.getId());
                    if (marker == null) return;
                    marker.remove();
                    mHashMap.remove(details.getId());
                }
            }
        } catch (Exception e) {
            Log.e("Exception: ", e.getMessage());
        }
    }

    /**
     * Determines whether one Location reading is better than the current Location fix
     *
     * @param location            The new Location that you want to evaluate
     * @param currentBestLocation The current Location fix, to which you want to compare the new one
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > AppConstants.TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -AppConstants.TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else return isNewer && !isSignificantlyLessAccurate && isFromSameProvider;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }


}
