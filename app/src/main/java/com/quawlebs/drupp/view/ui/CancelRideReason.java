package com.quawlebs.drupp.view.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.quawlebs.drupp.login.StartUpActivity;
import com.quawlebs.drupp.models.ResponseData;
import com.quawlebs.drupp.models.UserInfo;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.Helper;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;
import com.quawlebs.drupp.view.ui.ride.BottomSheetRideActivity;

import java.util.HashMap;

import retrofit2.Response;

public class CancelRideReason extends MainBaseActivity {
    String reason;
    TextView rs1, rs2, rs3, rs4;
    LinearLayout l1, l2, l3, l4;
    String re = "1";
    private FirebaseDatabase mDatabase;
    private DatabaseReference mMessagesReference;
    private int currentDriverId;
    private int currentRideid;

    @Override
    protected void setUp() {
        mDatabase = FirebaseDatabase.getInstance();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cancel_ride);
        rs1 = findViewById(R.id.r1);
        rs2 = findViewById(R.id.r2);
        rs3 = findViewById(R.id.r3);
        rs4 = findViewById(R.id.r4);
        l1 = findViewById(R.id.l1);
        l2 = findViewById(R.id.l2);
        l3 = findViewById(R.id.l3);
        l4 = findViewById(R.id.l4);
        reason = rs1.getText().toString();
        reason = rs2.getText().toString();
        reason = rs3.getText().toString();
        reason = rs4.getText().toString();

        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l1.setBackgroundResource(R.drawable.textview_background_afterclick);
                l2.setBackgroundResource(R.drawable.textview_background);
                l3.setBackgroundResource(R.drawable.textview_background);
                l4.setBackgroundResource(R.drawable.textview_background);
                re = "1";
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
            }
        });
        findViewById(R.id.btReasonCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAPI();
            }
        });
        if (getIntent() != null) {
            currentDriverId = getIntent().getIntExtra(AppConstants.K_DRIVER_ID, 0);
            currentRideid = getIntent().getIntExtra(AppConstants.K_RIDE_ID, 0);
        }

    }

    private void cancelAPI() {
        DruppRequestHandler.clearInstance();
        ResponseData responseData = Helper.getInstance(this).readFromJson(AppConstants.DRIVER_DETAILS, ResponseData.class);

        HashMap<String, Object> params = new HashMap<>();
        params.put(AppConstants.K_CANCEL_REASON, re);
        params.put(AppConstants.K_RIDE_ID, currentRideid);
        showLoading();
        DruppRequestHandler.getInstance(new INetworkList<String>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<String>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        UIHelper.getInstance().switchActivity(CancelRideReason.this, BottomSheetRideActivity.class, null, null, null, true);
                        Helper.removeRideParams(getContext());

                        Helper.removeRideId(CancelRideReason.this);
                        // showCancelledDialog();

                        clearChatHistory();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
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
                            SessionManager.getInstance().removeUserData(CancelRideReason.this);
                            UIHelper.getInstance().switchActivity(CancelRideReason.this, StartUpActivity.class, null, null, null, true);
                        });
                    }
                    return;

                }

                Log.e("errrrrrr",""+ response);
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
                        cancelAPI();
                    });
                }

            }
        }, SessionManager.getAccessToken()).cancelRide(params);
    }




    private void clearChatHistory() {
        if (SessionManager.getInstance().getUserModel() != null) {
            UserInfo userInfo = SessionManager.getInstance().getUserModel().getUserInfo();
            if (userInfo != null) {
                mMessagesReference = mDatabase.getReference().child(AppConstants.MESSAGES).child(userInfo.getId() + "_" + currentDriverId);
                mMessagesReference.removeValue((databaseError, databaseReference) -> {
                });
            }
        }

    }

    private void showCancelledDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_ride_canceled, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setView(alertLayout);
        alertDialog.show();

    }

    @Override
    protected void onStop() {
        super.onStop();


    }
}
