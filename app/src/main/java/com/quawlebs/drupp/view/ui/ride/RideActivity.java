package com.quawlebs.drupp.view.ui.ride;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import com.quawlebs.drupp.helpers.IDialogResponseObserver;
import com.quawlebs.drupp.helpers.IFragmentChangeListener;
import com.quawlebs.drupp.helpers.IFragmentEventObserver;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.helpers.Timing;
import com.quawlebs.drupp.login.StartUpActivity;
import com.quawlebs.drupp.models.DriverDetails;
import com.quawlebs.drupp.models.DriverPostRide;
import com.quawlebs.drupp.models.RideInfo;
import com.quawlebs.drupp.service.FetchAddressIntentService;
import com.quawlebs.drupp.util.LocationUtil;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.util.WorkaroundMapFragment;
import com.quawlebs.drupp.view.ui.NavDrawer;
import com.quawlebs.drupp.view.ui.RideFor;
import com.quawlebs.drupp.view.ui.RideStartActivity;
import com.quawlebs.drupp.view.ui.busbooking.BusBookActivity;
import com.quawlebs.drupp.view.ui.scheduledRides.ScheduledRidesActivity;
import com.quawlebs.drupp.view.ui.search.PlaceSearchDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Timer;

import retrofit2.Response;

public class RideActivity extends NavDrawer implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
        , IFragmentEventObserver, IActivityHelper, IDialogResponseObserver, IFragmentChangeListener {

    public static FragmentManager fragmentManager;
    public String ride_type, passengers_preference, vehicle_type;
    private int rideoption;
    private Timer myTimer;
    private SharedPreferences sharedPreferences;
    private String token;
    private String timeStamp = "", basefare = "";
    private String from, to;
    private PlacesClient placesClient;
    private LatLng to_latlang = null, from_latlang = null;
    private GoogleMap mMap;
    private MarkerOptions place1, place2;
    private SupportMapFragment mapFragment;
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};
    private AutocompleteSupportFragment autocompleteSupportFragment1, autocompleteSupportFragment2;
    private Location location;
    private GoogleApiClient googleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 1000, FASTEST_INTERVAL = 1000; // = 5 seconds
    //----------------------------Views------------------------------------
    private FloatingActionButton fabBus;
    private EditText etSource, etDestination;
    private LinearLayout mLayoutBottomRideType, mLayoutTopRideType;
    private CardView mCardSource, mCardDestination;
    //----------------------------Globals----------------------------------
    private String currentRideId;
    private boolean isRiderStart = false;
    private DatabaseReference mDatabase;
    private HashMap<Integer, Marker> mHashMap = new HashMap<>();
    private CountDownTimer ridePostedTimer;
    private boolean savedInstanceStateDone;
    private HashMap<String, String> hashMap = new HashMap<>();
    private int topLayoutHeight;
    private Location currentBestLocation, locationSource;
    // lists for permissions
    private boolean current_location_flag = false;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    // integer for permissions results request
    private static final int ALL_PERMISSIONS_RESULT = 1011;

    private Boolean sc = false, des = false;
    //When current location button clicked
    private boolean isCurrentMarkerAdded = true;
    //---------------------Globals----------------
    private Marker sourceMarker, destinationMarker;
    //-------------------------Receivers---------------------
    private AddressResultReceiver resultReceiver;


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(AppConstants.K_SOURCE_LAT, String.valueOf(from_latlang.latitude));
                hashMap.put(AppConstants.K_SOURCE_LONG, String.valueOf(from_latlang.longitude));
                hashMap.put(AppConstants.K_DEST_LAT, String.valueOf(to_latlang.latitude));
                hashMap.put(AppConstants.K_DEST_LONG, String.valueOf(to_latlang.longitude));
                hashMap.put(AppConstants.K_VEHICLE_TYPE, vehicle_type);

                UIHelper.getInstance().switchActivity(RideActivity.this, RideStartActivity.class, null, hashMap, true);
            } catch (Exception e) {
                showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
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

            AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(RideActivity.this);
            AlertDialog alertDialog = alertDialogbuilder.create();
            alertDialog.setView(alertLayout);
            alertDialog.show();
            alertLayout.findViewById(R.id.rideAlert_view).setOnClickListener(v ->
                    {
                        alertDialog.dismiss();
                        startActivity(new Intent(RideActivity.this, ScheduledRidesActivity.class));
                    }
            );
        }
    };

    private BroadcastReceiver rideFromDriver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_ride, null);
            DriverPostRide driverPostRide = Helper.getInstance(RideActivity.this).readFromJson(AppConstants.DRIVER_POST_RIDE, DriverPostRide.class);
            TextView heading = alertLayout.findViewById(R.id.tvRide_for);
            TextView tvdate = alertLayout.findViewById(R.id.tvdate);
            TextView source = alertLayout.findViewById(R.id.tvSourcePlace);
            TextView destination = alertLayout.findViewById(R.id.tvDestinationPlace);
            TextView fare = alertLayout.findViewById(R.id.tvBillAmount);

            fare.setText(getString(R.string.fare_pop_up, driverPostRide.getDriverPost_total_fare()));
            String date = Timing.getTimeInString(Long.parseLong(driverPostRide.getDriverPost_ride_date()), Timing.TimeFormats.CUSTOM_DATE_TIME);
            destination.setText(driverPostRide.getDriverPost_destination());
            source.setText(driverPostRide.getDriverPost_source());
            //  heading.setText(getString(R.string.ride_for_abuja, driverPostRide.getDriverPost_destination()));
            tvdate.setText(getString(R.string._06_june_2019, date));

            AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(RideActivity.this);
            AlertDialog alertDialog = alertDialogbuilder.create();
            alertDialog.setView(alertLayout);
            alertDialog.show();
            alertLayout.findViewById(R.id.rideAlert_view).setOnClickListener(v -> {
                        alertDialog.dismiss();
                        startActivity(new Intent(RideActivity.this, RideFor.class));
                    }
            );
        }
    };
    //TODO : CHANGE LAYOUT BASED ON CONDITION
    private BroadcastReceiver prefChange = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.custom_dialog_ridelater_accepted, null);
            TextView rideMessage = alertLayout.findViewById(R.id.tv_ride_message);

            //TODO CHANGE IMPLEMENTATION BASED ON CONDITION
            HashMap<String, String> map = Helper.getRideLaterDetails(RideActivity.this);
            String finalDate = Timing.getTimeInString(Long.parseLong(Objects.requireNonNull(map.get(AppConstants.K_TIME_RL))), Timing.TimeFormats.CUSTOM_DATE_TIME);
            HashMap<String, String> mapSourceDest = Helper.getSourceDestination(RideActivity.this);
            if (intent != null) {
                rideMessage.setText(getString(R.string.ridelater_accepted, intent.getStringExtra(AppConstants.K_DRIVER_NAME)));
            }

            AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(RideActivity.this);
            AlertDialog alertDialog = alertDialogbuilder.create();
            alertDialog.setView(alertLayout);
            alertDialog.show();
            alertLayout.findViewById(R.id.rideAlert_view).setOnClickListener(v -> {
                startActivity(new Intent(RideActivity.this, ScheduledRidesActivity.class));
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


    private BroadcastReceiver third = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isRiderStart = true;
            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.dialog_ride_status, null);
            TextView heading = alertLayout.findViewById(R.id.tvRide_for);
            AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(RideActivity.this);
            final AlertDialog alertDialog = alertDialogbuilder.create();
            heading.setText("Ride Status");
            String status = intent.getStringExtra("status");
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
            DriverPostRide driverPostRide = Helper.getInstance(RideActivity.this).readFromJson(AppConstants.DRIVER_POST_RIDE, DriverPostRide.class);
            TextView by = alertLayout.findViewById(R.id.by_ratings);
            TextView msg = alertLayout.findViewById(R.id.msg_ratings);
            RatingBar ratingBar = alertLayout.findViewById(R.id.rateBar_ratings);
            androidx.appcompat.app.AlertDialog.Builder alertDialogbuilder = new androidx.appcompat.app.AlertDialog.Builder(RideActivity.this);
            final androidx.appcompat.app.AlertDialog alertDialog = alertDialogbuilder.create();
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
    private boolean doubleBackToExitPressedOnce;


    public enum DATABASE_REF {
        DRIVERS,
        RIDE_INFO
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stub.setLayoutResource(R.layout.app_bar_ride_);
        stub.inflate();

        mDatabase = FirebaseDatabase.getInstance().getReference().child(AppConstants.FIREBASE_DATABASE.USERS);
        //Initialize views
        fabBus = findViewById(R.id.fab_bus);
        mCardSource = findViewById(R.id.cv_source);
        mCardDestination = findViewById(R.id.cv_destination);
        etSource = findViewById(R.id.etDeparture);
        etDestination = findViewById(R.id.etDestination);

        ImageView get_current_location = findViewById(R.id.get_current_location);
        //Fragment Here
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.frame, new RideType(), RideType.class.getSimpleName()).commit();
        polylines = new ArrayList<>();
        initPlaces();
        setupPlaces();


        mapFragment = (WorkaroundMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.image_Map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            mapFragment.getMapAsync(googleMap -> ((WorkaroundMapFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.image_Map)))
                    .setListener(() -> {
                    }));
        }

        //Subscribe for push Notifications using cloud functions
        FirebaseMessaging.getInstance().subscribeToTopic(AppConstants.PUSH_NOTIFICATIONS);

        //Save Activity Stack
        Helper.saveActivityStack(AppConstants.RIDE_ACTIVITY, this);
        //Register receivers
        resultReceiver = new AddressResultReceiver(new Handler());


        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionsToRequest = permissionsToRequest(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.
                        toArray(new String[0]), ALL_PERMISSIONS_RESULT);
            }
        }
        googleApiClient = new GoogleApiClient.Builder(this).
                addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).build();

        get_current_location.setOnClickListener(v -> currentLocation());

        //Syncs From Firebase Drivers Location
        driversFromRealTimeDB();

        //Layouts
        mLayoutTopRideType = findViewById(R.id.ll_layout_top_ride_type);
        ViewTreeObserver vto = mLayoutTopRideType.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLayoutTopRideType.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                topLayoutHeight = mLayoutTopRideType.getMeasuredHeight();
            }
        });


        final Drawable finalImgClearButton = AppCompatResources.getDrawable(getContext(), R.drawable.ic_clear_black_24dp);
        etSource.setOnTouchListener((v, event) -> {
            if (etSource.getCompoundDrawables()[2] == null) {
                return false;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                return false;
            }
            if (event.getX() > etSource.getWidth() - etSource.getPaddingRight() - finalImgClearButton.getIntrinsicWidth()) {
                etSource.setText("");
                if (sourceMarker != null) {
                    sourceMarker.remove();
                }
                sourceMarker = null;
                from_latlang = null;
                removeRouteBetweenMarkers();

                return true;
            }
            return false;
        });
        etDestination.setOnTouchListener((v, event) -> {
            if (etDestination.getCompoundDrawables()[2] == null) {
                return false;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                return false;
            }
            if (event.getX() > etDestination.getWidth() - etDestination.getPaddingRight() - finalImgClearButton.getIntrinsicWidth()) {
                etDestination.setText("");
                if (destinationMarker != null) {
                    destinationMarker.remove();
                }
                destinationMarker = null;
                to_latlang = null;
                removeRouteBetweenMarkers();
                return true;
            }
            return false;
        });
        fabBus.setOnClickListener(v -> {
//            BusChooserDialog busChooserDialog = BusChooserDialog.newInstance();
//            busChooserDialog.setiDialogResponseObserver(RideActivity.this);
//            busChooserDialog.show(getSupportFragmentManager(), BusChooserDialog.class.getSimpleName());
            UIHelper.getInstance().switchActivity(this, BusBookActivity.class, null, null, null, false);
        });

        if (getIntent().getSerializableExtra(AppConstants.K_RIDE_INFO) != null) {
            RideInfo rideInfo = (RideInfo) getIntent().getSerializableExtra(AppConstants.K_RIDE_INFO);
            if (rideInfo.getStatus() == AppConstants.RIDE_STATUS.RIDE_CANCELLED) {
                showCancelledRideDialog();
                Helper.saveRideId(0, this);


            }
        }
        //Ride ID Gone
        if (Helper.getRideId(this).equals(AppConstants.DEFAULT_RIDE_ID)) {
            Helper.saveRideId(AppConstants.DEFAULT_RIDE_ID, this);
        }

        //Resgistering Receiver
        registerReceiver(cancelRide, new IntentFilter(AppConstants.I_CANCEL_RIDE));
        registerReceiver(broadcastReceiver, new IntentFilter(AppConstants.I_RIDE_ACCEPTED));
        registerReceiver(rideFromDriver, new IntentFilter(AppConstants.I_RIDE_FROM_DRIVER));
        registerReceiver(third, new IntentFilter(AppConstants.I_DRIVER_ACCEPTED_RIDE));
        registerReceiver(prefChange, new IntentFilter(AppConstants.I_PREF_CHANGE));
        registerReceiver(rideLater, new IntentFilter(AppConstants.I_RIDE_LATER));
    }

    private void showCancelledRideDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_ride_canceled_by_driver, null);
        TextView tvCancelRide = alertLayout.findViewById(R.id.tvCancelRide);
        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(RideActivity.this);
        final AlertDialog alertDialog = alertDialogbuilder.create();
        tvCancelRide.setText(getString(R.string.ride_canceled_text));
        alertDialog.setView(alertLayout);
        alertDialog.show();

        alertLayout.findViewById(R.id.btYesCancel).setOnClickListener(v -> {
            mMap.clear();
            alertDialog.dismiss();
        });
    }

    private void markNearbyCars(String driverLatitude, String driverLongitude) {
        LatLng driver = new LatLng(Double.parseDouble(driverLatitude), Double.parseDouble(driverLongitude));
        mMap.addMarker(new MarkerOptions().position(driver).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_car_driver)));
    }

    private void setupPlaces() {
        etSource.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(AppConstants.K_LOCATION_TYPE, AppConstants.HOME_LOCATION);
            bundle.putString(AppConstants.SEARCH_DIALOG_TITLE, getString(R.string.enter_pickup_location));
            UIHelper.getInstance().showPlaceSearchDialog(RideActivity.this, bundle);

        });
        etSource.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mCardSource.setCardElevation(8);
                mCardDestination.setCardElevation(4);
                Bundle bundle = new Bundle();
                bundle.putInt(AppConstants.K_LOCATION_TYPE, AppConstants.HOME_LOCATION);
                bundle.putString(AppConstants.SEARCH_DIALOG_TITLE, getString(R.string.enter_pickup_location));
                UIHelper.getInstance().showPlaceSearchDialog(RideActivity.this, bundle);


            } else {
                hideKeyboard();
            }
        });
        etDestination.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mCardSource.setCardElevation(4);
                mCardDestination.setCardElevation(8);
                Bundle b = new Bundle();
                b.putInt(AppConstants.K_LOCATION_TYPE, AppConstants.WORK_LOCATION);
                b.putString(AppConstants.SEARCH_DIALOG_TITLE, getString(R.string.enter_drop_location));
                UIHelper.getInstance().showPlaceSearchDialog(RideActivity.this, b);

            } else {
                hideKeyboard();
            }
        });
        etDestination.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putInt(AppConstants.K_LOCATION_TYPE, AppConstants.WORK_LOCATION);
            b.putString(AppConstants.SEARCH_DIALOG_TITLE, getString(R.string.enter_drop_location));
            UIHelper.getInstance().showPlaceSearchDialog(RideActivity.this, b);
        });

        Helper.saveSourceDestination(hashMap, RideActivity.this);
    }


    private void initPlaces() {
        Places.initialize(this, getString(R.string.google_maps_key));
        placesClient = Places.createClient(this);
    }

    @Override
    public LatLng getSource() {
        return from_latlang;
    }

    @Override
    public LatLng getDestination() {
        return to_latlang;
    }

    private void routing(LatLng source, LatLng destination) {
        ArrayList<Marker> markers = new ArrayList<>();
        //TODO: Change Implementation of adding source marker
        if (source != null) {

            if (sourceMarker != null) {
                sourceMarker.remove();
            }
            sourceMarker = mMap.addMarker(new MarkerOptions().position(source).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_pin)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(source, 14f));
            mMap.setLatLngBoundsForCameraTarget(null);
        }
        if (destination != null) {
            if (destinationMarker != null) {
                destinationMarker.remove();
            }
            destinationMarker = mMap.addMarker(new MarkerOptions().position(destination).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_pin_des)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destination, 14f));
        }
        if (source != null && destination != null) {
            if (sourceMarker != null) {
                sourceMarker.remove();
            }
            if (destinationMarker != null) {
                destinationMarker.remove();
            }

            markers.clear();
            mMap.clear();
            sourceMarker = mMap.addMarker(new MarkerOptions().position(source).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_pin)));
            destinationMarker = mMap.addMarker(new MarkerOptions().position(destination).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_pin_des)));
            markers.add(sourceMarker);
            markers.add(destinationMarker);

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            //builder.include(source);
            // builder.include(destination);
            for (Marker marker : markers) {
                builder.include(marker.getPosition());
            }
            LatLngBounds bounds = builder.build();

            //Find height of bottom layout
            int width = getResources().getDisplayMetrics().widthPixels - topLayoutHeight;
            // int height = getResources().getDisplayMetrics().heightPixels - topLayoutHeight;
            int padding = (int) (width * 0.1);


            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
            mMap.setLatLngBoundsForCameraTarget(bounds);


        }
    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {

        Drawable vectorDrawable = AppCompatResources.getDrawable(context, vectorResId);
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
    public void changeType(int rideType, int poolOption, int rideOption) {
        //Toast.makeText(RideActivity.this,String.valueOf(rideType)+" "+String.valueOf(poolOption),Toast.LENGTH_SHORT).show();
        ride_type = String.valueOf(rideType);
        passengers_preference = String.valueOf(poolOption);
        this.rideoption = rideOption;
    }

    @Override
    public void changeVehicleType(int vehicleType) {
        vehicle_type = String.valueOf(vehicleType);
        //Toast.makeText(RideActivity.this,ride_type+" "+passengers_preference+" "+vehicle_type,Toast.LENGTH_SHORT).show();
    }


    public int status() {
        if (!sc || etSource.getText().toString().trim().isEmpty())
            return 1;
        if (!des || etDestination.getText().toString().trim().isEmpty())
            return 2;

        return 3;
    }

    public void post() throws NullPointerException {

        sharedPreferences = getSharedPreferences("userPickupLocation", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userSource_lat", String.valueOf(from_latlang.latitude));
        editor.putString("userSource_long", String.valueOf(from_latlang.longitude));
        editor.putString("userDes_lat", String.valueOf(to_latlang.latitude));
        editor.putString("userDes_long", String.valueOf(to_latlang.longitude));
        editor.apply();

        DruppRequestHandler.clearInstance();
        HashMap<String, Object> parms = new HashMap<>();
        parms.put(AppConstants.K_SOURCE, etSource.getText().toString());
        parms.put(AppConstants.K_SOURCE_LAT, String.valueOf(from_latlang.latitude));
        parms.put(AppConstants.K_SOURCE_LONG, String.valueOf(from_latlang.longitude));
        parms.put(AppConstants.K_DEST, etDestination.getText().toString());
        parms.put(AppConstants.K_DEST_LAT, String.valueOf(to_latlang.latitude));
        parms.put(AppConstants.K_DEST_LONG, String.valueOf(to_latlang.longitude));
        parms.put(AppConstants.K_RIDE_TYPE, ride_type);
        parms.put(AppConstants.K_RIDE_OPTION, String.valueOf(rideoption));
        parms.put(AppConstants.K_VEHICLE_TYPE, vehicle_type);
        parms.put(AppConstants.K_RIDE_DATE, timeStamp);
        parms.put(AppConstants.K_USER_FARE, basefare);
        parms.put(AppConstants.K_PASSENGER_PREFRENCE, passengers_preference);

        Helper.saveSourceDest(parms, this);

        showLoading();
        DruppRequestHandler.getInstance(new INetwork<HashMap<String, String>>() {
            @Override
            public void onResponse(Response<QualStandardResponse<HashMap<String, String>>> response) {

                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        currentRideId = response.body().getResponse().get(AppConstants.K_RIDE_ID);
                        Helper.saveRideId(Integer.parseInt(response.body().getResponse().get(AppConstants.K_RIDE_ID)), RideActivity.this);
                        if (rideoption == 2) {
                            showRideLaterPostedDialog();
                        }
                    } catch (Exception e) {

                    }


                }
            }

            @Override
            public void onError(Response<QualStandardResponse<HashMap<String, String>>> response) {
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
                            SessionManager.getInstance().removeUserData(RideActivity.this);
                            UIHelper.getInstance().switchActivity(RideActivity.this, StartUpActivity.class, null, null, null, true);
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
                        post();
                    });
                }
            }
        }, SessionManager.getAccessToken()).userPostRide(parms);
    }

    @Override
    public void cancelAPI() {

        DruppRequestHandler.clearInstance();

        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstants.K_CANCEL_REASON, "4");
        params.put(AppConstants.K_RIDE_ID, currentRideId);
        DruppRequestHandler.getInstance(new INetworkList<String>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<String>> response) {
                //   hideProgressBarWithCancel();
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        getSupportFragmentManager().popBackStackImmediate();
                        Helper.removeRideId(RideActivity.this);

                    } catch (Exception e) {

                    }

                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<String>> response) {
                // hideProgressBarWithCancel();
                if (response.code() == 401) {
                    showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
                    if (mAlertDialog != null) {
                        mAlertDialog.setCancelable(false);
                        TextView title = mAlertDialog.findViewById(R.id.tv_title);
                        title.setText(getString(R.string.your_session_has_expired));
                        Button btnOk = mAlertDialog.findViewById(R.id.btn_cancel);
                        btnOk.setText(getString(R.string.login));
                        btnOk.setOnClickListener(v -> {
                            SessionManager.getInstance().removeUserData(RideActivity.this);
                          //  UIHelper.getInstance().switchActivity(RideActivity.this, StartActivity.class, null, null, null, true);
                        });
                    }
                    return;

                }
                showErrorPrompt(response);
            }

            @Override
            public void onNullListResponse() {
                // hideProgressBarWithCancel();
            }

            @Override
            public void onFailureList(Throwable t) {
                //  hideProgressBarWithCancel();
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

    public void timestamp(String stamp, String fare) {
        basefare = fare;
        timeStamp = stamp;
    }

    private void showRideLaterPostedDialog() {
        showAlertDialog(R.layout.custom_dialog_ride_later_posted, AppConstants.NotificationType.PAYMENT_SUCCESS);

    }

    //-----------------Helpers Methods-----------------
    private void removeRouteBetweenMarkers() {
        if (polylines.size() > 0) {
            for (Polyline line : polylines) {
                line.remove();
            }
        }
    }


    @Override
    public HashMap<String, Object> getData() {
        HashMap<String, Object> parms = new HashMap<>();
        parms.put("vehicle", vehicle_type);
        return parms;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(broadcastReceiver);
            unregisterReceiver(rideFromDriver);
            unregisterReceiver(third);
            //unregisterReceiver(rating);
            unregisterReceiver(prefChange);
            unregisterReceiver(cancelRide);
            unregisterReceiver(rideLater);
        } catch (Exception ignored) {

        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.clear();

    }


    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED;
        }

        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        savedInstanceStateDone = false;
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();


        savedInstanceStateDone = false;
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, AppConstants.LOCATION_UPDATE_TIME_INTERVAL, AppConstants.LOCATION_UPDATE_MIN_DISTANCE, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, AppConstants.LOCATION_UPDATE_TIME_INTERVAL, AppConstants.LOCATION_UPDATE_MIN_DISTANCE, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        savedInstanceStateDone = true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        // stop location updates
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.removeUpdates(this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager.removeUpdates(this);
            //LocationServices.
            googleApiClient.disconnect();
        }

    }

    /**
     * Returns true if SavedInstanceState was done, and activity was not restarted or resumed yet.
     */
    public boolean isSavedInstanceStateDone() {
        return savedInstanceStateDone;
    }

    @Override
    public void onLocationChanged(Location location) {

        if (location != null && !current_location_flag) {
            if (isBetterLocation(location, currentBestLocation)) {
                from_latlang = new LatLng(location.getLatitude(), location.getLongitude());
            } else {
                from_latlang = new LatLng(currentBestLocation.getLatitude(), currentBestLocation.getLongitude());
            }

            routing(from_latlang, to_latlang);
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


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (sourceMarker != null) {
            from_latlang = null;
            sourceMarker.remove();
        }
        location = fetchCurrentLocation();
        if (location != null) {
            current_location_flag = true;
            sc = true;
            getAddress(location);
            from_latlang = new LatLng(location.getLatitude(), location.getLongitude());
            routing(from_latlang, to_latlang);
            removeRouteBetweenMarkers();
        }

    }


    private void currentLocation() {
        //Better approach to get locaiton
        if (sourceMarker != null) {
            from_latlang = null;
            etSource.setText("");
            sourceMarker.remove();
        }
        if (destinationMarker != null) {
            to_latlang = null;
            etDestination.setText("");
            destinationMarker.remove();
        }
        location = fetchCurrentLocation();
        if (location != null) {

            current_location_flag = true;
            sc = true;
            getAddress(location);
            from_latlang = new LatLng(location.getLatitude(), location.getLongitude());
            routing(from_latlang, to_latlang);
            removeRouteBetweenMarkers();
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
                                            RideActivity.this,
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


    public void getAddress(Location lastLocation) {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(AppConstants.RECEIVER, resultReceiver);
        intent.putExtra(AppConstants.LOCATION_DATA_EXTRA, lastLocation);
        startService(intent);

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
        if (requestCode == ALL_PERMISSIONS_RESULT) {
            for (String perm : permissionsToRequest) {
                if (hasPermission(perm)) {
                    permissionsRejected.add(perm);
                }
            }
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                currentLocation();
            }

            if (permissionsRejected.size() > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                        new AlertDialog.Builder(RideActivity.this).
                                setMessage("These permissions are mandatory to get your location. You need to allow them.").
                                setPositiveButton("OK", (dialogInterface, i) -> requestPermissions(permissionsRejected.
                                        toArray(new String[0]), ALL_PERMISSIONS_RESULT)).setNegativeButton("Cancel", null).create().show();

                    }
                }
            } else {
                if (googleApiClient != null) {
                    googleApiClient.connect();
                }
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //Notifies Next Fragment On Ride Type Changed
    @Override
    public void onRideSelected(int rideType) {

    }

    /**
     * Gets Selected Place from {@link PlaceSearchDialog}
     */
    @Override
    public void onPlaceSelected(Place place, int locationType) {

        if (locationType == AppConstants.HOME_LOCATION) {
            sc = true;
            etSource.setText(place.getAddress());
            hashMap.put(AppConstants.K_SOURCE, place.getAddress());
            from_latlang = place.getLatLng();

            current_location_flag = true;
        } else {
            des = true;
            current_location_flag = true;
            hashMap.put(AppConstants.K_DEST, place.getAddress());
            etDestination.setText(place.getAddress());
            to_latlang = place.getLatLng();
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

        routing(from_latlang, to_latlang);
    }

    @Override
    public void onCurrentLocationSelected() {
        currentLocation();
    }

    @Override
    public void onNoDriverFound() {
        showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
        if (mAlertDialog != null) {
            mAlertDialog.setCancelable(false);
            TextView dialogTitle = mAlertDialog.findViewById(R.id.tv_title);
            dialogTitle.setText(getString(R.string.no_drivers_nearby_to_accept_your_ride));
            mAlertDialog.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
                mAlertDialog.dismiss();
            });
        }
    }

    @Override
    public int setPickerStatus() {
        if (!sc || etSource.getText().toString().trim().isEmpty())
            return 1;
        if (!des || etDestination.getText().toString().trim().isEmpty())
            return 2;

        return 3;
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onDimiss() {

    }

    @Override
    public <T> void onFragmentChanged(T clazz) {
        if (!(clazz instanceof RideType)) {
            fabBus.hide();
        } else {
            fabBus.show();
        }
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
                hashMap.put(AppConstants.K_SOURCE, addressOutput);
                etSource.setText(addressOutput);
            }


        }

    }

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


    //TODO : implement 3 km radius
    private void driversFromRealTimeDB() {
        mDatabase.child(AppConstants.FIREBASE_DATABASE.DRIVERS).child(AppConstants.FIREBASE_DATABASE.DRIVER_DETAILS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    List<DriverDetails> drivers = new ArrayList<>();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        drivers.add(child.getValue(DriverDetails.class));
                    }

                    updateMarkersToMap(drivers);
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
                        animateMarker(mHashMap.get(details.getId()), new LatLng(details.getLatitude(), details.getLongitude()), false);
                        continue;
                    }
                    LatLng driver = new LatLng(details.getLatitude(), details.getLongitude());
                    Marker marker = mMap.addMarker(new MarkerOptions().position(driver).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_car_driver)));
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

    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;
        final Interpolator interpolator = new LinearInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    //--------------------------Activity Helper Methods----------------
    @Override
    public void onBackPressed() {

        //Fragment Back Stack Management
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame);

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            if (fragment instanceof RideData) {
                getSupportFragmentManager().popBackStack(AppConstants.RIDE_VEHICLE_BACK_STACK, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else {
                super.onBackPressed();
            }
        } else {

            if (doubleBackToExitPressedOnce || getSupportFragmentManager().getBackStackEntryCount() != 0) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            showMessage(getString(R.string.please_click_back_again));
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        }


    }
}
