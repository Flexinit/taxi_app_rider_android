package com.quawlebs.drupp.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.quawlebs.drupp.models.RecentSupportModel;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;

import retrofit2.Response;

public class SupportActivity extends MainBaseActivity {

    TextView tvModel, tvDriverName, tvSource, tvDestination;
    LinearLayout l1, l2, l3, l4;
    String re, rideId, source, destination, time, driver_name, vehicel_details;
    Intent intent;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        showLoading();
        tvModel = findViewById(R.id.tvModel);
        tvDriverName = findViewById(R.id.tvDriverName);
        tvSource = findViewById(R.id.tvSource);
        tvDestination = findViewById(R.id.tvDestination);
        l1 = findViewById(R.id.r1);
        l2 = findViewById(R.id.r2);
        l3 = findViewById(R.id.r3);
        l4 = findViewById(R.id.r4);
        getData();
        intent = new Intent(SupportActivity.this, SupportMessageActivity.class);


        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l1.setBackgroundResource(R.drawable.textview_background_afterclick);
                l2.setBackgroundResource(R.drawable.textview_background);
                l3.setBackgroundResource(R.drawable.textview_background);
                l4.setBackgroundResource(R.drawable.textview_background);
                re = "1";
                intent.putExtra("reason", getString(R.string.resend_rec));
                startActivity(intent);
            }
        });
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l2.setBackgroundResource(R.drawable.textview_background_afterclick);
                l1.setBackgroundResource(R.drawable.textview_background);
                l3.setBackgroundResource(R.drawable.textview_background);
                l4.setBackgroundResource(R.drawable.textview_background);
                re = "2";
                intent.putExtra("reason", getString(R.string.misconduct));
                startActivity(intent);
            }
        });
        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l3.setBackgroundResource(R.drawable.textview_background_afterclick);
                l2.setBackgroundResource(R.drawable.textview_background);
                l1.setBackgroundResource(R.drawable.textview_background);
                l4.setBackgroundResource(R.drawable.textview_background);
                re = "3";
                intent.putExtra("reason", getString(R.string.lost_item));
                startActivity(intent);
            }
        });
        l4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l4.setBackgroundResource(R.drawable.textview_background_afterclick);
                l2.setBackgroundResource(R.drawable.textview_background);
                l3.setBackgroundResource(R.drawable.textview_background);
                l1.setBackgroundResource(R.drawable.textview_background);
                re = "4";
                intent.putExtra("reason", getString(R.string.longer_route));
                startActivity(intent);
            }
        });
//        findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//
//                Thread background = new Thread() {
//            public void run() {
//                try {
//                    // Thread will sleep for 5 seconds
//                    sleep(4000);
//
//
//                    Intent intent=new Intent(SupportActivity.this,SupportMessageActivity.class);
//                    startActivity(intent);
//
//                } catch (Exception e) {
//                }
//            }
//        };
//
//        // start thread
//        background.start();
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_emergancy, null);
        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(SupportActivity.this);
        final AlertDialog alertDialog = alertDialogbuilder.create();
        alertDialog.setView(alertLayout);

        findViewById(R.id.emergency).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
                alertDialog.findViewById(R.id.btOk).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
    }

    private void getData() {
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<RecentSupportModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<RecentSupportModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    source = response.body().getResponse().getSource();
                    rideId = response.body().getResponse().getRide_id();
                    time = response.body().getResponse().getRide_date();
                    destination = response.body().getResponse().getDestination();
                    driver_name = response.body().getResponse().getDriver_name();
                    vehicel_details = response.body().getResponse().getVehicle_name();

                    tvSource.setText(source);
                    tvDestination.setText(destination);
                    tvDriverName.setText(driver_name);
                    tvModel.setText(vehicel_details);

                    intent.putExtra("rideId", rideId);
                    intent.putExtra("source", source);
                    intent.putExtra("destination", destination);
                    hideLoading();
                }
            }

            @Override
            public void onError(Response<QualStandardResponse<RecentSupportModel>> response) {
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
                        getData();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getRecentRide();
    }

}
