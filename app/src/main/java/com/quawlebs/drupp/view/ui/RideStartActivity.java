package com.quawlebs.drupp.view.ui;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.maps.android.PolyUtil;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.INetwrokUpdateLocation;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.GpsTracker;
import com.quawlebs.drupp.helpers.Helper;
import com.quawlebs.drupp.helpers.IDialogObserver;
import com.quawlebs.drupp.helpers.IFragmentEventObserver;
import com.quawlebs.drupp.helpers.INotifyEvent;
import com.quawlebs.drupp.helpers.IRideOnResponseObserver;
import com.quawlebs.drupp.helpers.MapHelper;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.login.StartUpActivity;
import com.quawlebs.drupp.models.PaymentMethod;
import com.quawlebs.drupp.models.ResponseCount;
import com.quawlebs.drupp.models.RideInfo;
import com.quawlebs.drupp.models.RideInfoModel;
import com.quawlebs.drupp.models.RideModel;
import com.quawlebs.drupp.models.UserInfo;
import com.quawlebs.drupp.models.getlocation.GetLocationModel;
import com.quawlebs.drupp.service.NotificationService;
import com.quawlebs.drupp.util.RxBus;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.view.ui.chat.ChatDialog;
import com.quawlebs.drupp.view.ui.dialog.NewsDialog;
import com.quawlebs.drupp.view.ui.dialog.PaymentMethodDialog;
import com.quawlebs.drupp.view.ui.dialog.RateDriverDialog;
import com.quawlebs.drupp.view.ui.dialog.ShoppingDialog;
import com.quawlebs.drupp.view.ui.ride.BottomSheetRideActivity;
import com.willy.ratingbar.ScaleRatingBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

import static com.google.android.gms.maps.model.JointType.ROUND;

public class RideStartActivity extends NavDrawer implements /*GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,*/ OnMapReadyCallback
        , DirectionCallback, IFragmentEventObserver, INotifyEvent, IDialogObserver, IRideOnResponseObserver {

    private Polyline runningPathPolyline=null;
    private float polylineWidth = 15;
    //--------------------------------Views-----------------------------
    private TextView mDriverName, mCabNumber, mCabType, mCabModel, mHeader, mDriverRating;

    private ImageView btnChat, btnCall;
    private LinearLayout containerRate, containerShare;
    private LinearLayout containerOTP;
    private FloatingActionButton fabBottomSheet;
    private ViewSwitcher switcherBottom;
    private ConstraintLayout containerRideOnWay, containerRideStarted;
    private CircleImageView ivImageProfile;
    private EditText mSource, mDestination;
    private CardView sourceCard, destinationCard;
    private ImageView vehicleImage,vehicleImageTop;
    private JSONObject rideObjectParams;
    private ImageButton btnEmergency;
    private ConstraintLayout bottomSheetLayout;
    private ImageView paymentLogo;
    private MapHelper mapHelper;
    private int currentRideStatus;
    private AppBarLayout appBarLayout;
    private ScaleRatingBar driverRating;

    public Marker marker = null;

    private LinearLayout containerPaymentMethod;
    //FireBase
    private FirebaseDatabase mDatabase;
    private DatabaseReference mMessagesReference;

    //---------------------------------List & Globals-------------------
    private Marker driverMarker;
    private int currentRideId;
    private LatLng driverCurrentLatLng;
    private String currentDriverPhoneNumber = "";
    private RideInfo rideInfoModel;
    private DatabaseReference mUserDbReference;
    private FirebaseAuth mFirebaseAuth;
    private NotificationService notificationService;
    private HashMap<String, Object> rideParmas;
    private String currentVehicleType;
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};
    private ArrayList<String> permissions = new ArrayList<>();
    private UserInfo userInfo;
    ArrayList<LatLng> coordinate;
    private LatLng latLng;
    private GoogleMap mMap;
    private Double driverLongitude, driverLatitude, userLatitude, userLongitude;
    private Timer myTimer;
    //------------------------BroadCast Receivers-----------------------L
    TextView trip_Count,distanceAway,arrivalTime,mTitleBottom,mOTP,tvDriverDescription;

    private SharedPreferences spPopup;
    private boolean isShowNewsDialog = false;
    private boolean isShowShopDialog = false;
    private RideStartViewModel model;

    private BroadcastReceiver rideFinished = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            Intent i = new Intent(RideStartActivity.this, BillActivity.class);
            String params = rideObjectParams == null ? null : rideObjectParams.toString();
            i.putExtra(AppConstants.K_RIDE_PARAMS, params);
            i.putExtra(AppConstants.K_BUNDLE, getIntent().getBundleExtra(AppConstants.K_BUNDLE));
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            active = false;
        }
    };

    private BroadcastReceiver driverArrived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            distanceAway.setText("Driver arrived");
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("hh:mm a");
            Date date=new Date();
            String time=simpleDateFormat.format(date);
            arrivalTime.setText(getString(R.string.arrived_by,time));
            tvDriverDescription.setVisibility(View.INVISIBLE);
            containerOTP.setVisibility(View.VISIBLE);

            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(RideStartActivity.this, SweetAlertDialog.NORMAL_TYPE);
            sweetAlertDialog
                    .setTitleText(getString(R.string.the_driver_has_arrived_at_your_location))
                    .setConfirmText(getString(R.string.ok))
                    .setConfirmClickListener(SweetAlertDialog::dismissWithAnimation);

            sweetAlertDialog.show();
            //         sweetAlertDialog.setConfirmText()
        }
    };
    private BroadcastReceiver rideStarted = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                clearChatHistory();
                if (intent.hasExtra(AppConstants.K_RIDE_TYPE)) {
                    HashMap<String, Object> params = new HashMap<>();
                    params.put(AppConstants.K_RIDE_TYPE, String.valueOf(intent.getIntExtra(AppConstants.K_RIDE_TYPE, AppConstants.USER_RIDE)));
                    params.put(AppConstants.K_VEHICLE_TYPE, currentVehicleType);
                    currentRideStatus = AppConstants.RIDE_STATUS.RIDE_STARTED;

                    bottomSheetLayout.post(() -> {
                        //bottomSheetBehavior.setPeekHeight(100);
                        ViewGroup.LayoutParams layoutParams = bottomSheetLayout.getLayoutParams();
                        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;

                        bottomSheetLayout.requestLayout();
                    });

                    if (rideObjectParams != null) {
                        try {
                            params.put(AppConstants.K_SOURCE, rideObjectParams.getString(AppConstants.K_SOURCE));
                            params.put(AppConstants.K_DEST, rideObjectParams.getString(AppConstants.K_DEST));
                            params.putAll(rideParmas);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        params.put(AppConstants.K_SOURCE, rideInfoModel.getSource());
                        params.put(AppConstants.K_DEST, rideInfoModel.getDestination());
                    }

                    //containerRideStarted.setVisibility(View.VISIBLE);
                    //containerRideOnWay.setVisibility(View.GONE);

                    startDialogTimer();
                    mHeader.setText(getString(R.string.your_ride_is_in_progress, userInfo.getFirstName()));
                    mTitleBottom.setText(getString(R.string.your_ride_is_in_progress, userInfo.getFirstName()));

                }
            }
            if (userMarker != null) {
                userMarker.remove();
                userMarker = null;
                updateDriverMarker(fromLatLng, 0);
            }
        }
    };

    private BroadcastReceiver cancelRide = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            clearChatHistory();
            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_ride_canceled_by_driver, null);
            AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(RideStartActivity.this);
            final androidx.appcompat.app.AlertDialog alertDialog = alertDialogbuilder.create();
            alertDialog.setCancelable(false);
            alertDialog.setView(alertLayout);
            alertDialog.show();

            alertLayout.findViewById(R.id.btYesCancel).setOnClickListener(v -> {
                alertDialog.dismiss();
                Helper.removeRideParams(getContext());
                startActivity(new Intent(RideStartActivity.this, BottomSheetRideActivity.class));
                finish();
            });
        }
    };

    private boolean fabBottomSheetClicked;
    private ValueEventListener singleValueEventListener;
    private ValueEventListener childValueEventListener;
    private Location currentBestLocation;
    private Marker userMarker;
    private Disposable paymentMethodDisposable;
    private boolean active = true;
    private boolean flagRate;
    private int currentDriverType;
    private Integer currentDriverId;
    private LatLng fromLatLng;
    private LatLng toLatLng;
    private BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior;
    private int currentRideType;
    private String currentDriverImage;
    private RateDriverDialog rateDriverDialog;
    private AnimatorSet animatorSet;
    private List<LatLng> polyLineList;
    private PolylineOptions polylineOptions;
    private Polyline greyPolyLine;
    private PolylineOptions blackPolylineOptions;
    private Polyline blackPolyline;
    private Handler handler;
    private int index;
    private int next;
    private LatLng startPosition;
    private LatLng endPosition;
    private boolean isInitialLoading = true;
    double latitude=0.0;
    double longitude=0.0;
    private boolean toolBarHidden=false;
    private boolean bottomSheetCollapsed=true;
    private TextView expandedBottomSheetTitle;



    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo = SessionManager.getInstance().loadUser(this).getUserInfo();
        stub.setLayoutResource(R.layout.activity_ride_start);
        stub.inflate();

        btnEmergency = findViewById(R.id.btn_emergency);
        bottomSheetLayout = findViewById(R.id.driver_info);
        //containerShare = findViewById(R.id.container_share);
        //containerRate = findViewById(R.id.container_rate_review);
        fabBottomSheet = findViewById(R.id.fab_bottom_sheet);
        trip_Count=findViewById(R.id.trip_Count);
        distanceAway=findViewById(R.id.tv_distance_away);
        arrivalTime=findViewById(R.id.tv_arrival_time);
        vehicleImage = findViewById(R.id.iv_vehicle_image);
        vehicleImageTop=findViewById(R.id.image_car_main);
        //estimatedFare = findViewById(R.id.tv_estimated_fare);
        mDriverName = findViewById(R.id.tv_driver_name);
        mCabNumber = findViewById(R.id.tv_driver_car_number);
        mCabModel = findViewById(R.id.tv_driver_car_model);
        //mDriverRating = findViewById(R.id.tv_driver_rating);
        btnCall = findViewById(R.id.btn_call);
        //containerRideStarted = findViewById(R.id.container_ride_started);
        //containerRideOnWay = findViewById(R.id.container_ride_on_way);
        mHeader = findViewById(R.id.tv_ride_status);
        mTitleBottom=findViewById(R.id.tv_title_bottom);
        mOTP=findViewById(R.id.tv_otp);
        containerOTP=findViewById(R.id.container_driver_otp);
        tvDriverDescription=findViewById(R.id.tv_driver_description);
        expandedBottomSheetTitle = findViewById(R.id.tv_title);
        // switcherBottom = findViewById(R.id.switcher_bottom);
        ivImageProfile = findViewById(R.id.iv_driver_image);
        //containerPaymentMethod = findViewById(R.id.container_payment_method);
        btnChat = findViewById(R.id.btn_chat);

        //paymentLogo = findViewById(R.id.iv_payment_method);
        //paymentMethod = findViewById(R.id.tv_payment_method);
        mSource = findViewById(R.id.etDeparture);
        mDestination = findViewById(R.id.etDestination);

        //For Firebase SignIn
        currentRideStatus = AppConstants.RIDE_STATUS.RIDE_ACCEPTED;
        mFirebaseAuth = FirebaseAuth.getInstance();

        sourceCard = findViewById(R.id.card_source);
        appBarLayout=findViewById(R.id.app_toolbar);
        driverRating=findViewById(R.id.rating_bar_driver);

        //Get Ride Details
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomsheet, int i) {

            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        RxBus.getInstance().getIntentEvent()
                .subscribe(o -> {
                    if (o instanceof RideInfo) {
                        rideInfoModel = (RideInfo) o;
                        if (rideInfoModel.getActionType() != null && rideInfoModel.getActionType().equalsIgnoreCase("30")) {
                            showChatDialog();
                        }
                    }
                });

        if (getIntent() != null) {
            try {
                if (getIntent().hasExtra(AppConstants.K_OTP)) {

                    String otp = getString(R.string.otp_in_ride, getIntent().getIntExtra(AppConstants.K_OTP, 0));
                    SpannableString boldString = new SpannableString(otp);


                    boldString.setSpan(new StyleSpan(Typeface.BOLD), 10, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mHeader.setText(boldString);
                    mTitleBottom.setText(boldString);
                    String sOTP=String.valueOf(getIntent().getIntExtra(AppConstants.K_OTP, 0));
                    mOTP.setText(sOTP);
                }


                rideParmas = (HashMap<String, Object>) getIntent().getSerializableExtra(AppConstants.K_RIDE);

                if (rideParmas != null) {
                    fromLatLng = new LatLng(Double.parseDouble(rideParmas.get(AppConstants.K_SOURCE_LAT).toString()), Double.parseDouble(rideParmas.get(AppConstants.K_SOURCE_LONG).toString()));
                    toLatLng = new LatLng(Double.parseDouble(rideParmas.get(AppConstants.K_DEST_LAT).toString()), Double.parseDouble(rideParmas.get(AppConstants.K_DEST_LONG).toString()));
                }

                if (getIntent().getStringExtra(AppConstants.K_RIDE_DETAIL) != null) {
                    rideObjectParams = new JSONObject(getIntent().getStringExtra(AppConstants.K_RIDE_DETAIL));

                    rideInfoModel = new Gson().fromJson(rideObjectParams.toString(), RideInfo.class);

                    Log.e("driver info",new Gson().toJson(rideInfoModel));
                    currentDriverImage = rideObjectParams.getString(AppConstants.K_DRIVER_IMAGE);
                    currentDriverId = rideObjectParams.getInt(AppConstants.K_DRIVER_ID);
                    currentDriverType = rideObjectParams.getInt(AppConstants.K_DRIVER_TYPE);
                    mSource.setText(rideObjectParams.getString(AppConstants.K_SOURCE));
                    mDestination.setText(rideObjectParams.getString(AppConstants.K_DEST));
                    //estimatedFare.setText(getString(R.string.estimated_fare_in_naira, rideObjectParams.getString(AppConstants.K_TOTAL_FARE)));


                    mCabNumber.setText(rideObjectParams.getString(AppConstants.K_CAB_NUMBER));
                    mCabModel.setText(rideObjectParams.getString(AppConstants.K_VEHICLE_MODEL));
                    currentDriverPhoneNumber = rideObjectParams.getString(AppConstants.K_PHONE);
                    Glide.with(this).load(AppConstants.IMAGE_URL + rideObjectParams.getString(AppConstants.K_DRIVER_IMAGE)).apply(new RequestOptions()
                            .error(R.drawable.ic_user_silhouette)
                            .centerCrop().placeholder(R.drawable.ic_user_silhouette)).into(ivImageProfile);
                    mDriverName.setText(rideObjectParams.getString(AppConstants.K_DRIVER_NAME));
                    //mDriverRating.setText(rideObjectParams.getString(AppConstants.K_AVERAGE_RATING));
                    float fDriverRating=Float.parseFloat(rideObjectParams.getString(AppConstants.K_AVERAGE_RATING));
                    driverRating.setRating(fDriverRating);
                    currentRideId = Integer.parseInt(rideObjectParams.getString(AppConstants.K_RIDE_ID));
                    currentRideType = rideObjectParams.getInt(AppConstants.K_POSTED_BY_DRIVER) == 1 ? AppConstants.DRIVER_RIDE : AppConstants.USER_RIDE;
                    Glide.with(this).load(AppConstants.IMAGE_URL + rideObjectParams.getString(AppConstants.K_VEHICLE_IMAGES)).apply(new RequestOptions()
                            .error(R.drawable.ic_user_silhouette)
                            .centerCrop().placeholder(R.drawable.ic_user_silhouette)).into(vehicleImage);
                    Glide.with(this).load(AppConstants.IMAGE_URL + rideObjectParams.getString(AppConstants.K_VEHICLE_IMAGES)).apply(new RequestOptions()
                            .error(R.drawable.ic_user_silhouette)
                            .centerCrop().placeholder(R.drawable.ic_user_silhouette)).into(vehicleImageTop);
                } else {

                    if (rideInfoModel.getStatus() == AppConstants.RIDE_STATUS.RIDE_STARTED) {
                        clearChatHistory();
                        if (userMarker != null) {
                            userMarker.remove();
                            userMarker = null;
                        }

                        bottomSheetLayout.post(() -> {
                            bottomSheetBehavior.setPeekHeight(100);
                            ViewGroup.LayoutParams layoutParams = bottomSheetLayout.getLayoutParams();
                            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;

                            bottomSheetLayout.requestLayout();
                        });


//                        bottomSheetLayout.invalidate();

                        currentRideStatus = AppConstants.RIDE_STATUS.RIDE_STARTED;
                        //containerRideOnWay.setVisibility(View.GONE);
                        //containerRideStarted.setVisibility(View.VISIBLE);
                        startDialogTimer();
                        mHeader.setText(getString(R.string.your_ride_is_in_progress, userInfo.getFirstName()));
                        mTitleBottom.setText(getString(R.string.your_ride_is_in_progress, userInfo.getFirstName()));
                    } else {

                        String otp = getString(R.string.otp_in_ride, rideInfoModel.getOtp() == null ? 0 : rideInfoModel.getOtp());
                        SpannableString boldString = new SpannableString(otp);

                        boldString.setSpan(new StyleSpan(Typeface.BOLD), 10, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        mHeader.setText(boldString);
                        mTitleBottom.setText(boldString);
                    }
                    Log.e("driver info",new Gson().toJson(rideInfoModel));

                    currentRideType = rideInfoModel.getPostedByDriver();
                    currentDriverImage = rideInfoModel.getProfilePicture();
                    currentDriverId = rideInfoModel.getDriverId();
                    currentDriverType = rideInfoModel.getDriverType();
                    mSource.setText(rideInfoModel.getSource());
                    mDestination.setText(rideInfoModel.getDestination());
                    mCabNumber.setText(rideInfoModel.getVehicleNumber());
                    mCabModel.setText(rideInfoModel.getVehicleModel());
                    currentDriverPhoneNumber = rideInfoModel.getPhone();
                    mDriverName.setText(rideInfoModel.getDriverName());
                    //mDriverRating.setText(rideInfoModel.getAverageRating().toString());
                    float fDriverRating=Float.parseFloat(rideInfoModel.getAverageRating().toString());
                    driverRating.setRating(fDriverRating);
                    currentRideId = rideInfoModel.getId();
                    //estimatedFare.setText(getString(R.string.estimated_fare_in_naira, rideInfoModel.getTotalFare()));


                    try {
                        fromLatLng = new LatLng(Double.parseDouble(rideInfoModel.getSourceLatitude()),
                                Double.parseDouble(rideInfoModel.getSourceLongitude()));
                        toLatLng = new LatLng(Double.parseDouble(rideInfoModel.getDestinationLatitude()),
                                Double.parseDouble(rideInfoModel.getDestinationLongitude()));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Glide.with(this).load(AppConstants.IMAGE_URL + rideInfoModel.getProfilePicture()).apply(new RequestOptions()
                            .error(R.drawable.ic_user_silhouette)
                            .centerCrop().placeholder(R.drawable.ic_user_silhouette)).into(ivImageProfile);

                    Glide.with(this).load(AppConstants.IMAGE_URL + rideInfoModel.getExteriorFront()).apply(new RequestOptions()
                            .error(R.drawable.ic_car_right)
                            .centerCrop().placeholder(R.drawable.ic_car_right)).into(vehicleImage);
                    Glide.with(this).load(AppConstants.IMAGE_URL + rideInfoModel.getExteriorFront()).apply(new RequestOptions()
                            .error(R.drawable.ic_car_right)
                            .centerCrop().placeholder(R.drawable.ic_car_right)).into(vehicleImageTop);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        sourceCard.setEnabled(false);
        sourceCard.setClickable(false);
        sourceCard.setCardBackgroundColor(getResources().getColor(R.color.colorLightGrey));


        try {


            mUserDbReference = FirebaseDatabase.getInstance().getReference()
                    .child(AppConstants.FIREBASE_DATABASE.USERS)
                    .child(AppConstants.FIREBASE_DATABASE.RIDE_INFO)
                    .child(String.valueOf(currentRideId));
        } catch (Exception e) {
            e.printStackTrace();
        }


        //Sets Profile Image
        mDestination.setOnClickListener(v -> {
            showPlaceSearchDialog();
        });
        mDestination.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showPlaceSearchDialog();

            } else {
                hideKeyboard();
            }
        });

        mSource.setEnabled(false);


        if (getIntent() != null) {
            currentVehicleType = getIntent().getStringExtra(AppConstants.K_VEHICLE_TYPE);
            if (getIntent().hasExtra(AppConstants.K_SOURCE_LAT) || getIntent().hasExtra(AppConstants.K_SOURCE_LONG)) {
                latLng = new LatLng(Double.parseDouble(getIntent().getStringExtra(AppConstants.K_SOURCE_LAT)), Double.parseDouble(getIntent().getStringExtra(AppConstants.K_SOURCE_LONG)));
                userLatitude = Double.parseDouble(getIntent().getStringExtra(AppConstants.K_SOURCE_LAT));
                userLongitude = Double.parseDouble(getIntent().getStringExtra(AppConstants.K_SOURCE_LONG));
            } else {

            }

        }


        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.image_Map);
        mapFragment.getMapAsync(this);

        //Fab Button animation
        AnimationSet animSet = new AnimationSet(true);
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.setFillAfter(true);
        animSet.setFillEnabled(true);

        fabBottomSheet.setRotation(180.0f);

        final RotateAnimation animDown = new RotateAnimation(0.0f, 180.0f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);


        final RotateAnimation animUp = new RotateAnimation(180.0f, 0.0f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        animDown.setDuration(200);
        animDown.setFillAfter(true);

        animUp.setDuration(200);
        animUp.setFillAfter(true);
        animSet.addAnimation(animUp);

        fabBottomSheet.setOnClickListener(v -> {
            fabBottomSheetClicked = true;
            if(toolBarHidden){
                appBarLayout.setVisibility(View.VISIBLE);
                expandedBottomSheetTitle.setVisibility(View.GONE);
                mHeader.setVisibility(View.VISIBLE);
                toolBarHidden=false;
            }
            else{
                appBarLayout.setVisibility(View.GONE);
                expandedBottomSheetTitle.setVisibility(View.VISIBLE);
                mHeader.setVisibility(View.GONE);
                toolBarHidden=true;
            }


            bottomSheetBehavior.setState(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED ? BottomSheetBehavior.STATE_EXPANDED : BottomSheetBehavior.STATE_COLLAPSED);
        });


        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {



            @Override
            public void onStateChanged(@NonNull View view, int state) {
                switch (state) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        bottomSheetCollapsed = true;
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        bottomSheetCollapsed=false;
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                if (v == 1) {
                    fabBottomSheet.startAnimation(animDown);
                    fabBottomSheetClicked = false;


                } else if (v == 0) {
                    fabBottomSheet.startAnimation(animUp);
                    fabBottomSheetClicked = false;
                }
            }
        });


        //----------------Listeners------------------------------
        btnChat.setOnClickListener(v -> {

            showChatDialog();

        });
        btnCall.setOnClickListener(v -> {
            //startActivity(new Intent(RideStartActivity.this,ChatActivity.class));
            Intent i = new Intent(Intent.ACTION_DIAL);
            String p = AppConstants.URI_TEL + currentDriverPhoneNumber;
            i.setData(Uri.parse(p));
            startActivity(i);
        });
        //btnCancelRide.setOnClickListener(v -> showCustomDailog());
        //containerShare.setOnClickListener(v -> shareLiveLocation(currentRideId, currentRideType));

        initListeners();
        signIntoFirebase();
//        setUpRouteAnimator();
        //startRealTimeDriverLocationTracking();
        registerReceiver(rideStarted, new IntentFilter(AppConstants.I_RIDE_START_RIDER));
        registerReceiver(cancelRide, new IntentFilter(AppConstants.I_CANCEL_RIDE));
        registerReceiver(rideFinished, new IntentFilter(AppConstants.I_RIDE_FINISHED));
        registerReceiver(driverArrived, new IntentFilter(AppConstants.I_DRIVER_ARRIVED));
        startRealTimeDriverLocationTracking();
        coordinate=new ArrayList<>();
        callHundler();
        getRideCount(currentDriverId.toString());

        model = new ViewModelProvider(this).get(RideStartViewModel.class);
        initObservers();
    }

    public void startDialogTimer() {
        Handler handler = new Handler();
        spPopup = getSharedPreferences("popup", MODE_PRIVATE);
        if (spPopup.getBoolean(AppConstants.K_NEWS, false)) {
            handler.postDelayed(() -> {
                if (active) {
                    newsDialog();
                }
            }, AppConstants.TIME_NEWS_DIALOG);
        }
        if (spPopup.getBoolean(AppConstants.K_SHOPPING, false)) {
            handler.postDelayed(() -> {
                if (active) {
                    shopingDialog();
                }
            }, AppConstants.TIME_SHOP_DIALOG);
        }
    }
    public void initObservers() {
        model.isShowNewsDialog.observe(this, isShow -> {
            isShowNewsDialog = isShow;
        });
        model.isShowShopDialog.observe(this, isShow -> {
            isShowShopDialog = isShow;
        });
    }

    public void newsDialog() {
        if (!isShowNewsDialog) {

            NewsDialog newDialog = NewsDialog.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .add(newDialog, NewsDialog.class.getSimpleName());
            transaction.commit();
            model.setIsShowNewsDialog();
        }
    }

    public void shopingDialog() {

        if (!isShowShopDialog) {
            ShoppingDialog shoppingDialogFragment = ShoppingDialog.newInstance();
            shoppingDialogFragment.show(getSupportFragmentManager(), ShoppingDialog.class.getSimpleName());
            model.setIsShowShopDialog();
        }
    }

    private void showChatDialog() {
        ChatDialog chatDialog = ChatDialog.newInstance(rideInfoModel);
        chatDialog.show(getSupportFragmentManager(), ChatDialog.class.getSimpleName());
    }


    private void initListeners() {
        //Subscribe to notifications
        FirebaseMessaging.getInstance().subscribeToTopic(AppConstants.K_TOPIC + userInfo.getId() + currentRideId);
        //Notify Admin
        btnEmergency.setOnClickListener(v -> emergencyAction());
        /*containerRate.setOnClickListener(v -> {
            if (flagRate) {
                showMessage(getString(R.string.you_can_rate_again_after_ride_finish));
                return;
            }
            rateDriverDialog = RateDriverDialog.newInstance(currentRideId, currentDriverId, currentRideType, currentDriverImage);
            rateDriverDialog.setiDialogObserver(this);
            rateDriverDialog.show(getSupportFragmentManager(), RateDriverDialog.class.getSimpleName());
        });*/
    }

    private void clearChatHistory() {
        mDatabase = FirebaseDatabase.getInstance();
        if (SessionManager.getInstance().getUserModel() != null) {
            UserInfo userInfo = SessionManager.getInstance().getUserModel().getUserInfo();
            if (userInfo != null) {
                mMessagesReference = mDatabase.getReference().child(AppConstants.MESSAGES).child(userInfo.getId() + "_" + currentDriverId);
                mMessagesReference.removeValue((databaseError, databaseReference) -> {
                });
            }
        }

    }


    private void signIntoFirebase() {

        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null) {
            onSignedInInitialize(user.getDisplayName());
        } else {
            onSignedOutCleanup();
            mFirebaseAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(getClass().getSimpleName(), "signInAnonymously:success");
                        FirebaseUser user = mFirebaseAuth.getCurrentUser();
                        onSignedInInitialize(user.getDisplayName());
                        //updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(getClass().getSimpleName(), "signInAnonymously:failure", task.getException());
                        showMessage(R.string.authentication_failed);
//                            updateUI(null);

                    }
                }
            });
        }
        //containerPaymentMethod.setOnClickListener(v -> onPaymentMethodSelection());


        paymentMethodDisposable = RxBus.getInstance().getIntentEvent().subscribe(o -> {
            if (o instanceof PaymentMethod) {
                switchPaymentSelection((PaymentMethod) o);
            }
        });
        switchPaymentSelection(Helper.getPaymentMethod(this));

    }

    private void switchPaymentSelection(PaymentMethod method) {
        switch (method.getType()) {
            case AppConstants.PAYMENT_METHOD.WALLET:

                //paymentLogo.setImageDrawable(getResources().getDrawable(R.drawable.ic_wallet));
                //paymentMethod.setText(getString(R.string.wallet));
                break;
            case AppConstants.PAYMENT_METHOD.CARD:
                //paymentMethod.setText((method).getMethod());
                //paymentLogo.setImageDrawable(getResources().getDrawable(R.drawable.ic_visa_logo));
                break;
            case AppConstants.PAYMENT_METHOD.CASH:
                chooseCash();
                break;
        }
    }

    private void chooseCash() {
        //paymentMethod.setText(getString(R.string.cash));
        //paymentLogo.setImageDrawable(getResources().getDrawable(R.drawable.ic_cash));
    }


    public void onPaymentMethodSelection() {
        PaymentMethodDialog paymentMethodDialog = PaymentMethodDialog.newInstance(currentRideId, currentRideType, 1);
        paymentMethodDialog.show(getSupportFragmentManager(), PaymentMethodDialog.class.getSimpleName());
    }

    private void onSignedInInitialize(String username) {
        //onRefreshChatHistory();
        //attachDatabaseReadListener();
    }

    private void onSignedOutCleanup() {
        //detachDatabaseReadListener();
    }

    private void startRealTimeDriverLocationTracking() {


        if (singleValueEventListener == null) {
            singleValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(getClass().getSimpleName(), "Log");
                    //Setting Up marker
                    try {
                        RideInfoModel rideInfoModel = dataSnapshot.getValue(RideInfoModel.class);
                        driverCurrentLatLng = new LatLng(rideInfoModel.getcLatitude(), rideInfoModel.getcLongitude());

                        if (isInitialLoading) {
                            //Update Driver Marker Position
                            updateDriverMarker(driverCurrentLatLng, (float) rideInfoModel.getBearing());
                            isInitialLoading = false;
                        }

//                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(driverCurrentLatLng, 14f));

                    } catch (Exception e) {
                        Log.d(getClass().getSimpleName(), e.getMessage());
                    } finally {
                        if (driverCurrentLatLng != null) {
                            driverCurrentLatLng = null;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
        }

        if (childValueEventListener == null) {
            childValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        RideInfoModel rideInfoModel = dataSnapshot.getValue(RideInfoModel.class);
                        driverCurrentLatLng = new LatLng(rideInfoModel.getcLatitude(), rideInfoModel.getcLongitude());

                        updateDriverMarker(driverCurrentLatLng, (float) rideInfoModel.getBearing());


                        // setUpDriverRiderLocation(rideInfoModel);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
        }

        if (mUserDbReference != null) {
            mUserDbReference.addValueEventListener(childValueEventListener);
            mUserDbReference.addListenerForSingleValueEvent(singleValueEventListener);
        }

    }
    //Driver Tracking Methods

    private void addRoute(LatLng newLatLng) {
        LatLng nexLatLng;
        if (currentRideStatus == AppConstants.RIDE_STATUS.RIDE_ACCEPTED) {
            nexLatLng = fromLatLng;
        } else {
            nexLatLng = toLatLng;
        }

        if (polyLineList == null) {
            GoogleDirection.withServerKey(getString(R.string.google_maps_key))
                    .from(newLatLng)
                    .to(nexLatLng)
                    .avoid(AvoidType.FERRIES)
                    .transportMode(TransportMode.DRIVING)
                    .transportMode(TransportMode.WALKING)
                    .alternativeRoute(false)
                    .optimizeWaypoints(true)
                    .execute(this);
        } else if (!PolyUtil.isLocationOnEdge(newLatLng, polyLineList, false)) {
            GoogleDirection.withServerKey(getString(R.string.google_maps_key))
                    .from(newLatLng)
                    .to(nexLatLng)
                    .avoid(AvoidType.FERRIES)
                    .transportMode(TransportMode.DRIVING)
                    .transportMode(TransportMode.WALKING)
                    .alternativeRoute(false)
                    .optimizeWaypoints(true)
                    .execute(this);
        }

    }
    private void addRouteDriver(LatLng newLatLng) {
        LatLng nexLatLng;

        nexLatLng = new LatLng(latitude,longitude);


        if (polyLineList == null) {
            GoogleDirection.withServerKey(getString(R.string.google_maps_key))
                    .from(newLatLng)
                    .to(nexLatLng)
                    .avoid(AvoidType.FERRIES)
                    .transportMode(TransportMode.DRIVING)
                    .transportMode(TransportMode.WALKING)
                    .alternativeRoute(false)
                    .optimizeWaypoints(true)
                    .execute(this);
        } else if (!PolyUtil.isLocationOnEdge(newLatLng, polyLineList, false)) {
            GoogleDirection.withServerKey(getString(R.string.google_maps_key))
                    .from(newLatLng)
                    .to(nexLatLng)
                    .avoid(AvoidType.FERRIES)
                    .transportMode(TransportMode.DRIVING)
                    .transportMode(TransportMode.WALKING)
                    .alternativeRoute(false)
                    .optimizeWaypoints(true)
                    .execute(this);
        }

    }


    private synchronized void updateDriverMarker(LatLng newLatLng, float bearing) {

        if (driverMarker != null) {
            float bearingangle = bearing < 1 ? Calculatebearingagle(newLatLng) : bearing;
            driverMarker.setAnchor(0.5f, 0.5f);
            animatorSet = new AnimatorSet();
            animatorSet.playTogether(rotateMarker(Float.isNaN(bearingangle) ? -1 : bearingangle, driverMarker.getRotation()), moveVechile(newLatLng, driverMarker.getPosition()));
            animatorSet.start();
        } else {
            addDriverMarker(newLatLng);

        }
        if (currentRideStatus == AppConstants.RIDE_STATUS.RIDE_ACCEPTED) {
            addUserMarker(fromLatLng);
        } else {
            addUserMarker(toLatLng);
        }
        addRoute(newLatLng);


        mMap.animateCamera(CameraUpdateFactory.newCameraPosition
                (new CameraPosition.Builder().target(newLatLng)
                        .zoom(AppConstants.DEFAULT_ZOOM).build()));
    }

    private void addDriverMarker(LatLng newLatLng) {
        int markerDrawable;
        if (currentDriverType == AppConstants.DRIVER_TYPE.KEKE) {
            markerDrawable = R.drawable.ic_rickshaw;
        } else {
            markerDrawable = R.drawable.ic_car_driver;
        }
        MarkerOptions markerOptions = new MarkerOptions().position(newLatLng).flat(true).icon(bitmapDescriptorFromVector(this, markerDrawable));

        driverMarker = mMap.addMarker(markerOptions);

    }

    private void addUserMarker(LatLng newLatLng) {
        int markerDrawable;
        if (currentRideStatus == AppConstants.RIDE_STATUS.RIDE_ACCEPTED) {
            markerDrawable = R.drawable.ic_green_maker;
        } else {
            markerDrawable = R.drawable.ic_red_marker;
        }
        if (userMarker == null) {
            MarkerOptions markerOptions = new MarkerOptions().position(newLatLng).flat(true).icon(bitmapDescriptorFromVector(this, markerDrawable));
            userMarker = mMap.addMarker(markerOptions);
        } else {
            userMarker.setPosition(newLatLng);
        }


    }

    private float Calculatebearingagle(LatLng newlatlng) {
        Location destinationLoc = new Location("service Provider");
        Location userLoc = new Location("service Provider");

        userLoc.setLatitude(driverMarker.getPosition().latitude);
        userLoc.setLongitude(driverMarker.getPosition().longitude);

        destinationLoc.setLatitude(newlatlng.latitude);
        destinationLoc.setLongitude(newlatlng.longitude);


        return userLoc.bearingTo(destinationLoc);

    }

    public synchronized ValueAnimator rotateMarker(final float toRotation, final float startRotation) {

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(1555);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                float t = Float.parseFloat(valueAnimator.getAnimatedValue().toString());

                float rot = t * toRotation + (1 - t) * startRotation;

                driverMarker.setRotation(-rot > 180 ? rot / 2 : rot);


            }
        });
        return valueAnimator;
    }


    public synchronized ValueAnimator moveVechile(final LatLng finalPosition, final LatLng startPosition) {

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(3000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float t = Float.parseFloat(valueAnimator.getAnimatedValue().toString());

                LatLng currentPosition = new LatLng(
                        startPosition.latitude * (1 - t) + (finalPosition.latitude) * t,
                        startPosition.longitude * (1 - t) + (finalPosition.longitude) * t);
                driverMarker.setPosition(currentPosition);


            }
        });

        return valueAnimator;
    }


    public void sendNotificationChat(String notificationTitle, String notificationBody) {

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, AppConstants.CHANNEL_ID)
                .setAutoCancel(true)   //Automatically delete the notification
                .setSmallIcon(R.drawable.drupp_logo) //Notification icon
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setSound(defaultSoundUri);

        PendingIntent pendingIntent;
        Intent intentBuyer = new Intent(this, MainActivity.class);
        intentBuyer.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        pendingIntent = PendingIntent.getActivity(this, 0, intentBuyer, 0);
        notificationBuilder.setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel mChannel = new NotificationChannel("1", "ch__name", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(mChannel);
            notificationManager.notify(002, notificationBuilder.build());
        } else {

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(002, notificationBuilder.build());
        }
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
                            SessionManager.getInstance().removeUserData(RideStartActivity.this);
                            UIHelper.getInstance().switchActivity(RideStartActivity.this, StartUpActivity.class, null, null, null, true);
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

    @Override
    protected void onStop() {
        super.onStop();
        if (mUserDbReference != null) {
            mUserDbReference.removeEventListener(singleValueEventListener);
            mUserDbReference.removeEventListener(childValueEventListener);
        }

    }


    private void showCustomDailog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_cancel_ride, null);
        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = alertDialogbuilder.create();
        alertDialog.setView(alertLayout);
        // alertDialog.setContentView(R.layout.custom_alert_dailog);
        alertDialog.show();
        //alertDialog.getWindow().setLayout(1000,900);

        alertLayout.findViewById(R.id.btYesCancel).setOnClickListener(v -> {

            alertDialog.cancel();
            //showCancelledDailog();
            Intent intent = new Intent(RideStartActivity.this, CancelRideReason.class);
            intent.putExtra(AppConstants.K_DRIVER_ID, currentDriverId);
            intent.putExtra(AppConstants.K_RIDE_ID, currentRideId);
            startActivity(intent);

        });

        alertDialog.findViewById(R.id.btDontCancel).setOnClickListener(v -> alertDialog.cancel());
    }

    //--------------------------------Helper Map Calls--------------------------

    private void showPlaceSearchDialog() {
        Bundle b = new Bundle();
        b.putInt(AppConstants.K_LOCATION_TYPE, AppConstants.WORK_LOCATION);
        b.putString(AppConstants.SEARCH_DIALOG_TITLE, getString(R.string.enter_drop_location));
        UIHelper.getInstance().showPlaceSearchDialog(RideStartActivity.this, b);
    }

    /**
     * Refreshes driver position on map and also draws new route
     */
    private void refreshDriverRoute(LatLng driverCurrentLatLng) {

        GoogleDirection.withServerKey(getString(R.string.google_maps_key))
                .from(fromLatLng)
                .to(driverCurrentLatLng)
                .avoid(AvoidType.FERRIES)
                .transportMode(TransportMode.DRIVING)
                .transportMode(TransportMode.WALKING)
                .alternativeRoute(false)
                .optimizeWaypoints(true)
                .execute(this);

    }

    private void setInitialRoute(LatLng from_latlang, LatLng to_latlang) {
        if (from_latlang != null && to_latlang != null) {
            mMap.addMarker(new MarkerOptions().position(from_latlang).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_pin)));
            mMap.addMarker(new MarkerOptions().position(to_latlang).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_pin_des)));

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(from_latlang);
            builder.include(to_latlang);
            final LatLngBounds bounds = builder.build();
            mMap.setOnMapLoadedCallback(() -> mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
                    bounds, 100)));


            if (driverCurrentLatLng != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(driverCurrentLatLng, 14f));
            }
            GoogleDirection.withServerKey(getString(R.string.google_maps_key))
                    .from(from_latlang)
                    .to(to_latlang)
                    .avoid(AvoidType.FERRIES)
                    .transportMode(TransportMode.DRIVING)
                    .transportMode(TransportMode.WALKING)
                    .alternativeRoute(false)
                    .optimizeWaypoints(true)
                    .execute(this);

        }

    }

    //--------------------------------Helper API Calls -------------------------
    private void changeDestination(RideModel params) {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<String>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<String>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    showMessage(response.body().getErrorResponse());
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
                        changeDestination(params);
                    });
                }
            }
        }, SessionManager.getAccessToken()).changeDestination(params);
    }


    private void shareLiveLocation(Integer rideId, Integer rideType) {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<String>() {
            @Override
            public void onResponse(Response<QualStandardResponse<String>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    //Share Location Link
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, response.body().getResponse());
                    sendIntent.setType(AppConstants.TYPE_TEXT);
                    startActivity(sendIntent);
                }
            }

            @Override
            public void onError(Response<QualStandardResponse<String>> response) {
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
                        shareLiveLocation(rideId, rideType);
                    });
                }
            }
        }, SessionManager.getAccessToken()).shareLiveLocation(rideId, rideType);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        paymentMethodDisposable.dispose();
        unregisterReceiver(driverArrived);
        unregisterReceiver(rideStarted);
        unregisterReceiver(cancelRide);
        unregisterReceiver(rideFinished);


    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.REQ_RATE_DRIVER) {
            if (resultCode == RESULT_OK) {
                flagRate = true;
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapHelper = new MapHelper(mMap);
        GpsTracker tracker=new GpsTracker(getApplicationContext());
        latitude=tracker.getLatitude();
        longitude=tracker.getLongitude();
        marker = mMap.addMarker(
                new MarkerOptions().position(new LatLng(latitude,longitude)).icon(
                        BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                )
        );
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude,longitude)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 17f));
        //new Handler().postDelayed(() -> setInitialRoute(fromLatLng, toLatLng), 1000);

    }




    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = AppCompatResources.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable != null ? vectorDrawable.getIntrinsicWidth() : 0, vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onDialogDismiss(int dialogType) {
//        if (dialogType == AppConstants.DIALOG_NEWS) {
//            newsDialog();
////            if (spPopup.getBoolean("news", true)) {
////
////            }
//        } else {
//
//        }
    }


    private interface LatLngInterpolatorNew {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class LinearFixed implements LatLngInterpolatorNew {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;
                // Take the shortest path across the 180th meridian.
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
    }

    @Override
    public void onDirectionSuccess(Direction direction) {

        String status = direction.getStatus();

        if (status.equalsIgnoreCase(RequestResult.OK)) {
            Route singleRoute = direction.getRouteList().get(0);
            Leg leg = singleRoute.getLegList().get(0);

//
//            List<LatLng> pointsList = leg.getDirectionPoint();



            List<Route> routeList = direction.getRouteList();
            for (Route route : routeList) {
                polyLineList = route.getOverviewPolyline().getPointList();
                updateRoute(polyLineList);
            }
            Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
            // setCameraWithCoordinationBounds(singleRoute);
        } else if (status.equalsIgnoreCase(RequestResult.NOT_FOUND)) {
            if (polyLineList != null) {
                polyLineList.clear();
            }
            greyPolyLine.remove();
            blackPolyline.remove();
        }
    }
    private void setCameraWithCoordinationBounds( Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds =new LatLngBounds(southwest, northeast);
        //Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
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
            blackPolylineOptions.color(Color.RED);
            blackPolylineOptions.startCap(new SquareCap());
            blackPolylineOptions.endCap(new SquareCap());
            blackPolylineOptions.jointType(ROUND);
            blackPolyline = mMap.addPolyline(blackPolylineOptions);

        } else {
            greyPolyLine.setPoints(polyLineList);
        }

        if (driverMarker == null) {
            addDriverMarker(greyPolyLine.getPoints().get(0));
        }


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

        addUserMarker(greyPolyLine.getPoints().get(greyPolyLine.getPoints().size() - 1));


    }

    @Override
    public void onDirectionFailure(Throwable t) {
        if (t != null) {
            Toast.makeText(this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRideSelected(int rideType) {

    }

    @Override
    public void onPlaceSelected(Place place, int locationType) {
        mDestination.setText(place.getAddress());
        try {
            RideModel rideModel = new RideModel();
            rideModel.setDestination(mDestination.getText().toString().trim());
            double lat = place.getLatLng().latitude;
            double lng = place.getLatLng().longitude;
            rideModel.setDestinationLatitude(String.valueOf(lat));
            rideModel.setDestinationLongitude(String.valueOf(lng));
            rideModel.setRideId(currentRideId);


            LatLng to_latlang = new LatLng(lat, lng);
            setInitialRoute(fromLatLng, to_latlang);
            // editRide(rideModel);
            changeDestination(rideModel);

        } catch (Exception e) {
            showMessage(R.string.something_went_wrong);
        }

    }

    @Override
    public void onCurrentLocationSelected() {

    }


    @Override
    public void onBackPressed() {
        //showCustomDailog();
        moveTaskToBack(true);

    }


    @Override
    public void onNotificationReceived(String title, String message) {
        sendNotificationChat(title, message);
    }

    @Override
    public void onChatReceived(DataSnapshot message) {

    }


    @Override
    public void onDialogResult(Object type) {
        flagRate = true;
        if (rateDriverDialog != null) {
            rateDriverDialog.dismiss();
        }
    }

    private void getLatlng(int id) {


        DruppRequestHandler.clearInstance();

        HashMap<String, String> params = new HashMap<>();

        params.put("user_id", String.valueOf(id));

        DruppRequestHandler.getInstanceCount(new INetwrokUpdateLocation<String>() {
            @Override
            public void onResponse(Response<GetLocationModel> response) {
                Log.e("reponse",new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    GetLocationModel model=response.body();
                    if(marker!=null){

                        marker.remove();
                    }
                    addRouteDriver(new LatLng(model.getResponse().getLatitude(),model.getResponse().getLongitude()));

                    //  zoomMap(latitude,longitude,model.getResponse().getLatitude(),model.getResponse().getLongitude());

//

                }
            }

            @Override
            public void onError(Response<GetLocationModel> response) {

            }

            @Override
            public void onNullResponse() {
            }

            @Override
            public void onFailure(Throwable t) {



            }
        }, SessionManager.getAccessToken()).get_LatLng(params);
    }

    //------------------Update locations api----------------
    Handler handler_loc = new Handler();
    Runnable runnable;
    int delay = 30*1000; //Delay for 30 seconds.  One second = 1000 milliseconds.
    public void callHundler(){
        handler_loc.postDelayed( runnable = new Runnable() {
            public void run() {
                //do something
                //getMapRoute(tracker.getLatitude(),tracker.getLongitude(),toLatitude,toLongitude);

                getLatlng(currentDriverId);
                handler_loc.postDelayed(runnable, delay);
            }
        }, delay);

    }

    public void zoomMap(double start_latitude,double start_longitude,double end_latitude,double end_longitude){


        LatLng latLngStart = new LatLng(start_latitude, start_longitude);
        LatLng latLngEnd = new LatLng(end_latitude, end_longitude);


        // mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(end_latitude,end_longitude)));

        LatLngBounds bounds = new LatLngBounds.Builder().include(latLngStart).include(latLngEnd).build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        GoogleDirection.withServerKey(getString(R.string.google_maps_key))
                .from(latLngEnd)
                .to(latLngStart)
                .avoid(AvoidType.FERRIES)
                .transportMode(TransportMode.DRIVING)
                .transportMode(TransportMode.WALKING)
                .alternativeRoute(false)
                .optimizeWaypoints(true)
                .execute(this);

    }

    private void drawPolyline(double start_latitude,double start_longitude,double end_latitude,double end_longitude) {
        LatLng from =
                new LatLng(start_latitude,start_longitude);
        LatLng to = new LatLng(end_latitude, end_longitude);
        runningPathPolyline = mMap.addPolyline(new PolylineOptions().add(from, to).width(polylineWidth).color(Color.BLUE).geodesic(true)
        );
    }

    private void getRideCount(String id) {

        HashMap<String, String> parse = new HashMap<>();
        parse.put("id", String.valueOf(id));

        DruppRequestHandler.clearInstance();

        DruppRequestHandler.getInstance(new INetwork<ResponseCount>() {
            @Override
            public void onResponse(Response<QualStandardResponse<ResponseCount>> response) {
                hideLoading();

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        ResponseCount dashboardModel = response.body().getResponse();
                        trip_Count.setText("Trip count: "+dashboardModel.getDriverCount());
                    } catch (Exception e) {
                        Log.d(getClass().getSimpleName(), "error");
                    }


                }
            }

            @Override
            public void onError(Response<QualStandardResponse<ResponseCount>> response) {
                showErrorPrompt(response);
            }

            @Override
            public void onNullResponse() {
                hideLoading();
            }

            @Override
            public void onFailure(Throwable t) {

                showErrorPrompt(t.getMessage());

                //   showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
//                if (mAlertDialog != null) {
//                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
//                        hideAlertDialog();
//
//                    });
//                }

            }
        }, SessionManager.getAccessToken()).getRideCountData(parse);
    }

    //Helper methods
}
