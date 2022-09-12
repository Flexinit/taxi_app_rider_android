package com.quawlebs.drupp.view.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.quawlebs.drupp.login.StartUpActivity;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;
import com.quawlebs.drupp.view.ui.ride.BottomSheetRideActivity;

import java.util.HashMap;

import retrofit2.Response;

public class SupportMessageActivity extends MainBaseActivity {

    TextView tvReason;
    EditText etQuery;
    Button done;
    String query = null, reason, rideId, source, destination;

    @Override
    protected void setUp() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_message);

        tvReason = findViewById(R.id.etReason);
        etQuery = findViewById(R.id.etQuery);
        done = findViewById(R.id.done);

        Intent intent = getIntent();
        reason = intent.getStringExtra("reason");
        rideId = intent.getStringExtra("rideId");
        source = intent.getStringExtra("source");
        destination = intent.getStringExtra("destination");
        tvReason.setText(reason);
        findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        query = etQuery.getText().toString().trim();
                                        if (!query.isEmpty()) {
                                            reason = reason + " (" + query + ")";
                                        }
                                        showLoading();
                                        submitQuery(reason);
                                    }
                                }
        );
    }

    private void submitQuery(String reason) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(done.getWindowToken(), 0);
        DruppRequestHandler.clearInstance();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ride_id", rideId);
        hashMap.put("ride_source", source);
        hashMap.put("ride_destination", destination);
        hashMap.put("issue", reason);
        hashMap.put("date", "123456789013");
        DruppRequestHandler.getInstance(new INetworkList() {
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
                        submitQuery(reason);
                    });
                }
            }

            @Override
            public void onErrorList(Response response) {
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
                            SessionManager.getInstance().removeUserData(SupportMessageActivity.this);
                            UIHelper.getInstance().switchActivity(SupportMessageActivity.this, StartUpActivity.class, null, null, null, true);
                        });
                    }
                    return;

                }
                showErrorPrompt(response);
            }

            @Override
            public void onResponseList(Response response) {
                hideLoading();
                popup();
            }
        }, SessionManager.getAccessToken()).supportQuery(hashMap);
    }

    public void popup() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_query_sent, null);
        androidx.appcompat.app.AlertDialog.Builder alertDialogbuilder = new androidx.appcompat.app.AlertDialog.Builder(SupportMessageActivity.this);
        androidx.appcompat.app.AlertDialog alertDialog = alertDialogbuilder.create();
        alertDialog.setView(alertLayout);
        alertDialog.show();
        alertLayout.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SupportMessageActivity.this, BottomSheetRideActivity.class));
            }
        });
    }
}
