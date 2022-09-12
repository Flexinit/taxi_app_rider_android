package com.quawlebs.drupp.view.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.quawlebs.drupp.models.LiveLocationModel;
import com.quawlebs.drupp.models.LocationDataModel;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.Helper;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Response;

public class LiveLocationActivity extends MainBaseActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Timer timer;
    private Integer id;
    private String key;
    private Marker sourceMarker;
    private TextView mEta;
    private AlertDialog.Builder alertDialogbuilder;
    private AlertDialog alertDialog;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Helper.getActivityStack(this) == AppConstants.RIDE_ON) {
            UIHelper.getInstance().switchActivity(this, RideOnActivity.class, null, null, null, false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Helper.getActivityStack(this) == AppConstants.RIDE_ON) {
            UIHelper.getInstance().switchActivity(this, RideOnActivity.class, null, null, null, false);
        }

    }


    private void showPopUp() {
        timer.cancel();
        if (alertDialog != null && !alertDialog.isShowing()) {
            alertDialog.show();

        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_location);
        mEta = findViewById(R.id.tv_eta);
        timer = new Timer();

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_ride_status, null);
        TextView heading = alertLayout.findViewById(R.id.tvRide_for);
        TextView message = alertLayout.findViewById(R.id.tv_ride_message);
        alertDialogbuilder = new AlertDialog.Builder(LiveLocationActivity.this);
        alertDialog = alertDialogbuilder.create();
        message.setText(getString(R.string.ride_finished));
        heading.setText(getString(R.string.ride_status));
        alertDialog.setView(alertLayout);
        alertLayout.findViewById(R.id.rideAlert_view).setOnClickListener(v -> {
            alertDialog.dismiss();
            UIHelper.getInstance().switchActivity(LiveLocationActivity.this, MainActivity.class, null, null, null, true);

        });
        //Initialize View
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_live_location);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        //Get data from intent
        Intent mapData = getIntent();
        if (mapData != null) {
            if (Objects.requireNonNull(mapData.getAction()).equals(Intent.ACTION_VIEW)) {
                try {
                    String path;
                    if (Objects.requireNonNull(Objects.requireNonNull(mapData.getData()).getPath()).equals(AppConstants.DEEP_LINK)) {
                        path = Objects.requireNonNull(mapData.getExtras().get("android.intent.extra.REFERRER")).toString();
                    } else {
                        path = mapData.getData().getPath();
                    }
                    if (path != null) {
                        key = path.substring(nthLastIndexOf(1, "/", path) + 1);
                        id = Integer.valueOf(path.substring(nthLastIndexOf(2, "/", path) + 1, nthLastIndexOf(1, "/", path)));
                    }

                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            getLiveLocation(id, key);
                        }
                    }, 1000, AppConstants.INTERVAL_GET_RIDE_TIME_DISTANCE);

                } catch (Exception e) {
                    showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
                    timer.cancel();
                }

            }
        }
        findViewById(R.id.card_go_back_main_screen).setOnClickListener(v -> UIHelper.getInstance().switchActivity(LiveLocationActivity.this, MainActivity.class, null, null, null, true));

    }

    static int nthLastIndexOf(int nth, String ch, String string) {
        if (nth <= 0) return string.length();
        return nthLastIndexOf(--nth, ch, string.substring(0, string.lastIndexOf(ch)));
    }

    @Override
    protected void setUp() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

    }

    private void getLiveLocation(Integer id, String key) {
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<LiveLocationModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<LiveLocationModel>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    showRiderPosition(response.body().getResponse().getData());
                    mEta.setText(getString(R.string.estimated_arrival, response.body().getResponse().getEta().getTime()));
                }
            }

            @Override
            public void onError(Response<QualStandardResponse<LiveLocationModel>> response) {
                hideLoading();
                //  showErrorPrompt(response);
                showPopUp();
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
                        getLiveLocation(id, key);
                    });
                }
            }
        }).getLiveLocation(id, key);
    }

    private void showRiderPosition(LocationDataModel data) {
        Log.d(getClass().getSimpleName(), data.getLatitude() + "," + data.getLongitude());
        LatLng source = new LatLng(Double.valueOf(data.getLatitude()), Double.valueOf(data.getLongitude()));
        if (sourceMarker == null) {
            sourceMarker = mMap.addMarker(new MarkerOptions().position(source).icon(bitmapDescriptorFromVector(getApplicationContext())));
        } else {
            sourceMarker.setPosition(source);
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(source, 15f));
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Drawable vectorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_pin);
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
    protected void onDestroy() {
        super.onDestroy();
    }
}
