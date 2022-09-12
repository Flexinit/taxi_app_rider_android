package com.quawlebs.drupp.view.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.Helper;
import com.quawlebs.drupp.helpers.IFragmentEventObserver;
import com.quawlebs.drupp.helpers.IRideOnResponseObserver;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.models.Corider;
import com.quawlebs.drupp.models.PaymentMethod;
import com.quawlebs.drupp.models.ResponseData;
import com.quawlebs.drupp.models.RideModel;
import com.quawlebs.drupp.models.RideInfo;
import com.quawlebs.drupp.models.UserInfo;
import com.quawlebs.drupp.view.ui.dialog.NewsDialog;
import com.quawlebs.drupp.view.ui.dialog.PaymentMethodDialog;
import com.quawlebs.drupp.view.ui.dialog.ShoppingDialog;
import com.quawlebs.drupp.util.RxBus;
import com.quawlebs.drupp.util.UIHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public class RideOnActivity extends NavDrawer implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
        , IFragmentEventObserver, IRideOnResponseObserver {
    //----------------------Views----------------
    private TextView driverRating, mTimeToArrive;
    private TextView alert_coorider, mRideStarted;
    private LinearLayout mShareETA;
    private ImageView paymentLogo;
    private TextView paymentMethod;
    private EditText destination;
    private LatLng destinationLatLng;
    private LinearLayout containerPaymentMethod;
    //----------------------Lists&Globals--------
    private String currentVehicleType;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mMessagesReference;
    private HashMap<String, Object> mapSourceDestination;
    //  ResponseData responseData;
    private RideInfo responseData;
    LinearLayout co1, co2, corider_full;
    TextView c1n, c1f, c1t, c2n, c2f, c2t;
    TextView veh_name, veh_number, driver_name;
    private Timer timerEta;
    ArrayList<Corider> mList, mList2;
    Button btRate, btCancel;
    public static boolean active = true;
    private UserInfo userInfo;

    private GoogleApiClient googleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final long UPDATE_INTERVAL = 1000, FASTEST_INTERVAL = 1000; // = 5 seconds
    // lists for permissions
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    //private ArrayList<String> permissions = new ArrayList<>();
    // integer for permissions results request
    private static final int ALL_PERMISSIONS_RESULT = 1011;
    //private static final String TAG = "MapsActivity";
    GoogleMap mMap;
    private List<Polyline> polylines;
    @SuppressLint("PrivateResource")
    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};
    private SharedPreferences sharedPreferences, spPopup;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.pool_popup, null);
            alert_coorider = alertLayout.findViewById(R.id.text_pool);
            ResponseData RresponseData = Helper.getInstance(RideOnActivity.this).readFromJson(AppConstants.DRIVER_DETAILS, ResponseData.class);
            mList2 = new ArrayList<>();
            mList2.clear();
            mList2.addAll(RresponseData.getCorider_list());
            alert_coorider.setText(getString(R.string.co_rider, mList2.get(0).getCo_rider_name(), mList2.get(0).getCo_rider_source(), mList2.get(0).getCo_rider_desetination()));
            AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(RideOnActivity.this);
            final AlertDialog alertDialog = alertDialogbuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    startActivity(getIntent());
                }
            }).create();
            alertDialog.setView(alertLayout);
            alertDialog.show();
        }
    };

    private BroadcastReceiver rating = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_ratings, null);
            TextView by = alertLayout.findViewById(R.id.by_ratings);
            TextView msg = alertLayout.findViewById(R.id.msg_ratings);
            RatingBar ratingBar = alertLayout.findViewById(R.id.rateBar_ratings);


            if (intent != null) {
                //TODO:-IMPLEMENT BY
                //         by.setText(driverPostRide.getDriverPost_driverName());
                msg.setText(intent.hasExtra(AppConstants.K_REVIEW) ? intent.getStringExtra(AppConstants.K_REVIEW) : "");
                ratingBar.setRating(intent.hasExtra(AppConstants.K_RATE) ? (float) Integer.parseInt(intent.getStringExtra(AppConstants.K_RATE)) : 0.0f);
            }
            AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(RideOnActivity.this);
            final AlertDialog alertDialog = alertDialogbuilder.create();
            alertDialog.setView(alertLayout);
            alertDialog.show();
            alertLayout.findViewById(R.id.btOk).setOnClickListener(v -> alertDialog.dismiss());
        }

    };
    private BroadcastReceiver rideFinished = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Intent i = new Intent(RideOnActivity.this, BillActivity.class);
            i.putExtra(AppConstants.K_BUNDLE, getIntent().getBundleExtra(AppConstants.K_BUNDLE));
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            active = false;
        }
    };
    private boolean isShowDialog = false;
    private boolean flagRate;
    private Disposable paymentMethodDisposable;
    private String rideType;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            StringBuilder sb = new StringBuilder();
            ResponseData driverData = Helper.getInstance(this).readFromJson(AppConstants.DRIVER_DETAILS, ResponseData.class);
            if (driverData != null) {
                sb.append(getString(R.string.driver_name)).append(driverData.getDriverName()).append('\n');
                // sb.append(getString(R.string.vehicle_number)).append(driverData.getCab_number()).append('\n');
                sb.append(getString(R.string.vehicle_model)).append(driverData.getModel()).append('\n');
                sb.append(getString(R.string.from_)).append(driverData.getSource()).append('\n');
                sb.append(getString(R.string.to_)).append(driverData.getDestination());

            }
            sendIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
            sendIntent.setType(AppConstants.TYPE_TEXT);
            startActivity(sendIntent);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stub.setLayoutResource(R.layout.layout_new_ride_start);
        stub.inflate();
        co1 = findViewById(R.id.co_rider1);
        co2 = findViewById(R.id.co_rider2);
        c1n = findViewById(R.id.co_rider1_name);
        c1f = findViewById(R.id.co_rider1_from);
        c1t = findViewById(R.id.co_rider1_to);
        c2n = findViewById(R.id.co_rider2_name);
        mTimeToArrive = findViewById(R.id.tv_eta);
        c2f = findViewById(R.id.co_rider2_from);
        c2t = findViewById(R.id.co_rider2_to);
        containerPaymentMethod = findViewById(R.id.container_payment_method);
        mRideStarted = findViewById(R.id.tv_ride_started);
        btRate = findViewById(R.id.bt_rate);
        btCancel = findViewById(R.id.bt_cancel_ride);
        corider_full = findViewById(R.id.ll3);
        veh_name = findViewById(R.id.veh_name);
        paymentLogo = findViewById(R.id.iv_payment_method);
        paymentMethod = findViewById(R.id.tv_payment_method);
        veh_number = findViewById(R.id.veh_number);
        driver_name = findViewById(R.id.driver_name);

        driverRating = findViewById(R.id.driver_rating);
        mShareETA = findViewById(R.id.share_eta);
        spPopup = getSharedPreferences("popup", MODE_PRIVATE);


        Helper.saveActivityStack(AppConstants.RIDE_ON, this);
        Intent intent = getIntent();
        boolean flag = intent.getBooleanExtra("flag", false);
        if (flag) {
            btCancel.setVisibility(View.VISIBLE);
            btRate.setVisibility(View.GONE);
        }
        //Get UserInfo from Local Storage
        userInfo = SessionManager.getInstance().loadUser(this).getUserInfo();
        if (userInfo != null) {
            mRideStarted.setText(getString(R.string.your_ride_is_in_progress, userInfo.getFirstName()));
        }
        mList = new ArrayList<>();
        mList.clear();
        mDatabase = FirebaseDatabase.getInstance();
        //TODO cache data
        //       responseData = Helper.getInstance(RideOnActivity.this).readFromJson(AppConstants.DRIVER_DETAILS, ResponseData.class);

        responseData = Helper.getInstance(this).readFromJson(AppConstants.RIDE_DETAILS, RideInfo.class);
        if (responseData != null) {
            //   mList.addAll(responseData.getr());
            driver_name.setText(responseData.getDriverName());
            driver_name.setMovementMethod(new ScrollingMovementMethod());
            veh_number.setText(responseData.getVehicleNumber());
            veh_name.setText(responseData.getVehicleModel());
            driverRating.setText(String.valueOf(responseData.getAverageRating()));
        }
        //Clearing Chat History
//        ResponseData driverData = Helper.getInstance(this).readFromJson(AppConstants.DRIVER_DETAILS, ResponseData.class);
        if (SessionManager.getInstance().getUserModel() != null) {
            UserInfo userInfo = SessionManager.getInstance().getUserModel().getUserInfo();
            if (responseData != null && userInfo != null) {
                mMessagesReference = mDatabase.getReference().child(AppConstants.MESSAGES).child(userInfo.getId() + "_" + responseData.getDriverId());
                mMessagesReference.removeValue((databaseError, databaseReference) -> {
                    Log.d(getClass().getSimpleName(), "REMOVED");
                });
            }
        }


        CardView sourceCard = findViewById(R.id.card_source);
        sourceCard.setEnabled(false);
        sourceCard.setClickable(false);
        sourceCard.setCardBackgroundColor(getResources().getColor(R.color.colorLightGrey));
        EditText departure = findViewById(R.id.etDeparture);
        destination = findViewById(R.id.etDestination);
        departure.setEnabled(false);


        try {
            mapSourceDestination = (HashMap<String, Object>) getIntent().getSerializableExtra(AppConstants.K_RIDE);


            if (mapSourceDestination != null) {
                departure.setText(mapSourceDestination.get(AppConstants.K_SOURCE).toString());
                destination.setText(mapSourceDestination.get(AppConstants.K_DEST).toString());

                destinationLatLng = new LatLng(Double.parseDouble(mapSourceDestination.get(AppConstants.K_DEST_LAT).toString()), Double.parseDouble(mapSourceDestination.get(AppConstants.K_DEST_LONG).toString()));

                rideType = mapSourceDestination.get(AppConstants.K_RIDE_TYPE).toString();
                currentVehicleType = mapSourceDestination.get(AppConstants.K_VEHICLE_TYPE).toString();
            } else {
                departure.setText(responseData.getSource());

                destination.setText(responseData.getDestination());
                destinationLatLng = new LatLng(Double.valueOf(responseData.getDestinationLatitude()), Double.valueOf(responseData.getDestinationLongitude()));

                rideType = responseData.getRideType().toString();
                currentVehicleType = responseData.getVehicleType().toString();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        departure.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putInt(AppConstants.K_LOCATION_TYPE, AppConstants.WORK_LOCATION);
            b.putString(AppConstants.SEARCH_DIALOG_TITLE, getString(R.string.enter_drop_location));
            UIHelper.getInstance().showPlaceSearchDialog(RideOnActivity.this, b);
        });

        destination.setOnClickListener(v -> showPlaceSearchDialog());
        destination.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showPlaceSearchDialog();

            } else {
                hideKeyboard();
            }
        });

        switch (mList.size()) {
            case 2: {
                corider_full.setVisibility(View.VISIBLE);
                co2.setVisibility(View.VISIBLE);
                c2n.setText(mList.get(1).getCo_rider_name());
                c2f.setText(mList.get(1).getCo_rider_source());
                c2t.setText(mList.get(1).getCo_rider_desetination());
            }
            case 1: {
                corider_full.setVisibility(View.VISIBLE);
                co1.setVisibility(View.VISIBLE);
                c1n.setText(mList.get(0).getCo_rider_name());
                c1f.setText(mList.get(0).getCo_rider_source());
                c1t.setText(mList.get(0).getCo_rider_desetination());
                break;
            }
        }
        btRate.setOnClickListener(v -> {
            if (flagRate) {
                showMessage(getString(R.string.you_can_rate_again_after_ride_finish));
                return;
            }
            Intent rateDriver = new Intent(RideOnActivity.this, RateDriverActivity.class);
            startActivityForResult(rateDriver, AppConstants.REQ_RATE_DRIVER);
        });
        btCancel.setOnClickListener(v -> startActivity(new Intent(RideOnActivity.this, CancelRideReason.class)));
        findViewById(R.id.contact_rideOn).setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_DIAL);
            String p = "tel:" + "+" + responseData.getPhone();
            i.setData(Uri.parse(p));
            startActivity(i);
        });
        registerReceiver(rating, new IntentFilter(AppConstants.I_RATINGS));
        registerReceiver(broadcastReceiver, new IntentFilter("new_co_rider"));
        registerReceiver(rideFinished, new IntentFilter("ride_finished"));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.image_Map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        polylines = new ArrayList<>();
        //TODO: CHANGE THIS DEPRECEATED API
        googleApiClient = new GoogleApiClient.Builder(this).
                addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).build();
        Handler handler = new Handler();
        if (spPopup.getBoolean(AppConstants.K_SHOPPING, false)) {
            handler.postDelayed(() -> {
                if (active) {
                    shopingDialog();
                }
            }, AppConstants.TIME_SHOP_DIALOG);
        } else if (!spPopup.getBoolean(AppConstants.K_SHOPPING, false) && spPopup.getBoolean(AppConstants.K_NEWS, false)) {
            handler.postDelayed(() -> {
                if (active) {
                    newsDialog();
                }
            }, AppConstants.TIME_SHOP_DIALOG);
        }

        mShareETA.setOnClickListener(v -> {
            //Share Location Intent
            shareLiveLocation(responseData.getId(), rideType);

        });
        timerEta = new Timer();

        timerEta.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (rideType != null) {
                    try {
                        getRideTimeDistance(Integer.valueOf(Helper.getRideId(RideOnActivity.this)), rideType);
                    } catch (Exception e) {
                        hideAlertDialog();
                        showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
                        timerEta.cancel();
                    }


                }

            }
        }, 1000, AppConstants.INTERVAL_GET_RIDE_TIME_DISTANCE);

        paymentMethodDisposable = RxBus.getInstance().getIntentEvent().subscribe(o -> {
            if (o instanceof PaymentMethod) {
                switchPaymentSelection((PaymentMethod) o);
            }
        });
        containerPaymentMethod.setOnClickListener(v -> onPaymentMethodSelection());
        switchPaymentSelection(Helper.getPaymentMethod(getContext()));

    }

    private void showPlaceSearchDialog() {
        Bundle b = new Bundle();
        b.putInt(AppConstants.K_LOCATION_TYPE, AppConstants.WORK_LOCATION);
        b.putString(AppConstants.SEARCH_DIALOG_TITLE, getString(R.string.enter_drop_location));
        UIHelper.getInstance().showPlaceSearchDialog(RideOnActivity.this, b);
    }

    private void switchPaymentSelection(PaymentMethod method) {
        switch (method.getType()) {
            case AppConstants.PAYMENT_METHOD.WALLET:

                paymentLogo.setImageDrawable(getResources().getDrawable(R.drawable.ic_wallet_payment));
                paymentMethod.setText(getString(R.string.wallet));
                break;
            case AppConstants.PAYMENT_METHOD.CARD:
                paymentMethod.setText((method).getMethod());
                paymentLogo.setImageDrawable(getResources().getDrawable(R.drawable.ic_visa_logo));
                break;
            case AppConstants.PAYMENT_METHOD.CASH:
                chooseCash();
                break;
        }
    }


    public void onPaymentMethodSelection() {
        PaymentMethodDialog paymentMethodDialog = new PaymentMethodDialog();
        paymentMethodDialog.show(getSupportFragmentManager(), PaymentMethodDialog.class.getSimpleName());
    }

    private void chooseCash() {
        paymentMethod.setText(getString(R.string.cash));
        paymentLogo.setImageDrawable(getResources().getDrawable(R.drawable.ic_cash));
    }


    @Override
    protected void onStop() {
        super.onStop();
        timerEta.cancel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isShowDialog = false;
    }


    //TODO:
    public void shopingDialog() {

        if (isShowDialog) {
            ShoppingDialog shoppingDialogFragment = ShoppingDialog.newInstance();
            shoppingDialogFragment.setiRideOnResponseObserver(this);
            shoppingDialogFragment.show(getSupportFragmentManager(), ShoppingDialog.class.getSimpleName());
        }


        //        LayoutInflater inflater = getLayoutInflater();
//        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog, null);
//        androidx.appcompat.app.AlertDialog.Builder alertDialogbuilder = new androidx.appcompat.app.AlertDialog.Builder(RideOnActivity.this);
//        final AlertDialog alertDialog = alertDialogbuilder.create();
//        try {
//            alertDialog.setView(alertLayout);
//            alertDialog.show();
//        } catch (Exception e) {
//            showMessage(R.string.something_went_wrong);
//        }
//
//
//        alertLayout.findViewById(R.id.btYes).setOnClickListener(v -> startActivity(new Intent(RideOnActivity.this, BrowseProducts.class)));
//        alertLayout.findViewById(R.id.btNo).setOnClickListener(v -> {
//            if (spPopup.getBoolean("news", true)) {
//                alertDialog.dismiss();
//                newsDialog();
//            } else {
//                alertDialog.dismiss();
//            }
//        });

    }

    public void newsDialog() {
//        DialogFragment newDialog = NewsDialog.newInstance();
//        ((NewsDialog) newDialog).setiResponseObserver(this);
//        newDialog.show(getSupportFragmentManager(), NewsDialog.class.getSimpleName());

//        LayoutInflater inflater = getLayoutInflater();
//        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_news, null);
//        androidx.appcompat.app.AlertDialog.Builder alertDialogbuilder = new androidx.appcompat.app.AlertDialog.Builder(RideOnActivity.this);
//        final androidx.appcompat.app.AlertDialog alertDialog = alertDialogbuilder.create();
//        alertDialog.setView(alertLayout);
//        alertDialog.show();
//
//        alertLayout.findViewById(R.id.btYes).setOnClickListener(v -> startActivity(new Intent(RideOnActivity.this, NewsFeedActivity.class)));
//        alertLayout.findViewById(R.id.btNo).setOnClickListener(v -> alertDialog.dismiss());
    }


    private void shareLiveLocation(Integer rideId, String rideType) {
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
        }, SessionManager.getAccessToken()).shareLiveLocation(rideId, Integer.valueOf(rideType));

    }

    private void getRideTimeDistance(int id, String rideType) throws NumberFormatException {
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<HashMap<String, String>>() {
            @Override
            public void onResponse(Response<QualStandardResponse<HashMap<String, String>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mTimeToArrive.setText(getString(R.string.eta, response.body().getResponse().get(AppConstants.K_TIME)));
                }
            }

            @Override
            public void onError(Response<QualStandardResponse<HashMap<String, String>>> response) {
                hideLoading();
                timerEta.cancel();
                showErrorPrompt(response);
            }

            @Override
            public void onNullResponse() {
                hideLoading();
                timerEta.cancel();
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoading();
                timerEta.cancel();
            }
        }, SessionManager.getAccessToken()).getRideTimeDistance(id, Integer.valueOf(rideType));
    }

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

    private void editRide(RideModel rideModel) {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<HashMap<String, String>>() {
            @Override
            public void onResponse(Response<QualStandardResponse<HashMap<String, String>>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    Helper.saveTotalFare(response.body().getResponse().get(AppConstants.K_TOTAL_FARE), RideOnActivity.this);
                    showMessage(R.string.destination_changed);
                }
            }

            @Override
            public void onError(Response<QualStandardResponse<HashMap<String, String>>> response) {
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
                        editRide(rideModel);
                    });
                }
            }
        }, SessionManager.getAccessToken()).editUserPostedRide(rideModel);
    }

    private void routing(LatLng from_latlang, LatLng to_latlang) {
        mMap.addMarker(new MarkerOptions().position(from_latlang).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_pin)));
        mMap.addMarker(new MarkerOptions().position(to_latlang).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_pin_des)));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(from_latlang);
        builder.include(to_latlang);
        final LatLngBounds bounds = builder.build();
        mMap.setOnMapLoadedCallback(() -> mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
                bounds, 100)));

    }

    private void routing() {
        SharedPreferences sharedPreferences = getSharedPreferences("userPickupLocation", MODE_PRIVATE);
        LatLng source = new LatLng(Double.parseDouble(sharedPreferences.getString("userSource_lat", "")), Double.parseDouble(sharedPreferences.getString("userSource_long", "")));

        //LatLng des = new LatLng(Double.parseDouble(sharedPreferences.getString("userDes_lat", "")), Double.parseDouble(sharedPreferences.getString("userDes_long", "")));

        LatLng des = new LatLng(destinationLatLng.latitude, destinationLatLng.longitude);
        mMap.addMarker(new MarkerOptions().position(source).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_pin)));
        mMap.addMarker(new MarkerOptions().position(des).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_pin_des)));

        //mMap.addMarker(new MarkerOptions().position(source));
        //mMap.addMarker(new MarkerOptions().position(des));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(source);
        builder.include(des);
        final LatLngBounds bounds = builder.build();
        mMap.setOnMapLoadedCallback(() -> mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
                bounds, 100)));
        mMap.setLatLngBoundsForCameraTarget(bounds);


    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Permissions ok, we get last location
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (location != null) {

            mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_car_driver)));
        }

        startLocationUpdates();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_car_driver)));
        }
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You need to enable permissions to display location !", Toast.LENGTH_SHORT).show();
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perm : permissionsToRequest) {
                    if (!hasPermission(perm)) {
                        permissionsRejected.add(perm);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            new android.app.AlertDialog.Builder(RideOnActivity.this).
                                    setMessage("These permissions are mandatory to get your location. You need to allow them.").
                                    setPositiveButton("OK", (dialogInterface, i) -> {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(permissionsRejected.
                                                    toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                        }
                                    }).setNegativeButton("Cancel", null).create().show();

                            return;
                        }
                    }
                } else {
                    if (googleApiClient != null) {
                        googleApiClient.connect();
                    }
                }

                break;
        }
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isShowDialog = true;
        if (!checkPlayServices()) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Helper.saveActivityStack(AppConstants.RIDE_ACTIVITY, this);
        paymentMethodDisposable.dispose();
        unregisterReceiver(broadcastReceiver);
        unregisterReceiver(rideFinished);
        unregisterReceiver(rating);
        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            //LocationServices.
            googleApiClient.disconnect();
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                finish();
            }

            return false;
        }

        return true;
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = AppCompatResources.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        routing();
    }

    @Override
    public void onRideSelected(int rideType) {

    }

    @Override
    public void onPlaceSelected(Place place, int locationType) {
        destination.setText(place.getAddress());
        try {
            RideModel rideModel = new RideModel();
            rideModel.setDestination(destination.getText().toString().trim());
            double lat = place.getLatLng().latitude;
            double lng = place.getLatLng().longitude;
            rideModel.setDestinationLatitude(String.valueOf(lat));
            rideModel.setDestinationLongitude(String.valueOf(lng));
            rideModel.setRideId(Integer.valueOf(Helper.getRideId(RideOnActivity.this)));

            LatLng from_latlang = new LatLng(Double.valueOf(mapSourceDestination.get(AppConstants.K_SOURCE_LAT).toString()), Double.valueOf(mapSourceDestination.get(AppConstants.K_SOURCE_LONG).toString()));
            LatLng to_latlang = new LatLng(lat, lng);
            routing(from_latlang, to_latlang);
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
        moveTaskToBack(true);
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
    public void onDialogDismiss(int dialogType) {
        if (dialogType == AppConstants.DIALOG_SHOP) {
            shopingDialog();
//            if (spPopup.getBoolean("news", true)) {
//
//            }
        } else {

        }
    }
}
