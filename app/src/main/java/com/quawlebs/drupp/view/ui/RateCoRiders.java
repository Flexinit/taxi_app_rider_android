package com.quawlebs.drupp.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.quawlebs.drupp.login.StartUpActivity;
import com.quawlebs.drupp.models.Corider;
import com.quawlebs.drupp.models.ResponseData;
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

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Response;

public class RateCoRiders extends MainBaseActivity implements View.OnClickListener {

    CardView card1, card2;
    TextView crName1, crName2, skip;
    Button rate1, rate2;
    RatingBar crRating1, crRating2;
    EditText crMsg1, crMsg2;
    ResponseData responseData;
    ArrayList<Corider> mList;
    int flag = 0;
    String id, msg;
    int stars;

    @Override
    protected void setUp() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_co_riders);
        card1 = findViewById(R.id.card_1);
        card1 = findViewById(R.id.card_2);
        crName1 = findViewById(R.id.rate_c1_name);
        crName2 = findViewById(R.id.rate_c2_name);
        crRating1 = findViewById(R.id.rate_c1_rating);
        crRating2 = findViewById(R.id.rate_c2_rating);
        crMsg1 = findViewById(R.id.rate_c1_msg);
        crMsg2 = findViewById(R.id.rate_c2_msg);
        rate1 = findViewById(R.id.rate_c1_done);
        rate2 = findViewById(R.id.rate_c2_done);
        skip = findViewById(R.id.header_skip);

        mList = new ArrayList<>();
        responseData = Helper.getInstance(RateCoRiders.this).readFromJson(AppConstants.DRIVER_DETAILS, ResponseData.class);

        mList.clear();
        mList.addAll(responseData.getCorider_list());
        switch (mList.size()) {
            case 2: {
                card2.setVisibility(View.VISIBLE);
                crName2.setText(mList.get(1).getCo_rider_name());
            }
            case 1: {
                crName1.setText(mList.get(0).getCo_rider_name());
                break;
            }
        }
        rate1.setOnClickListener(this);
        rate2.setOnClickListener(this);
        skip.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rate_c1_done: {
                flag++;
                id = mList.get(0).getCo_rider_id();
                stars = crRating1.getNumStars();
                msg = crMsg1.getText().toString();
                sendRating(id, stars, msg);
                rate1.setVisibility(View.GONE);
                break;
            }
            case R.id.rate_c2_done: {
                flag++;
                id = mList.get(1).getCo_rider_id();
                stars = crRating2.getNumStars();
                msg = crMsg2.getText().toString();
                sendRating(id, stars, msg);
                rate2.setVisibility(View.GONE);
                break;
            }
            case R.id.header_skip: {
                startActivity(new Intent(RateCoRiders.this, BottomSheetRideActivity.class));
                break;
            }
        }
    }

    private void sendRating(String id, int stars, String msg) {
        DruppRequestHandler.clearInstance();
        HashMap<String, String> parms = new HashMap<>();
        parms.put("ride_id", responseData.getRideID());
        parms.put("receiver", id);
        parms.put("rate", String.valueOf(stars));
        parms.put("review", msg);
        showLoading();
        DruppRequestHandler.getInstance(new INetworkList<String>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<String>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    if (mList.size() == 2) {
                        if (flag == 2) {
                            startActivity(new Intent(RateCoRiders.this, BottomSheetRideActivity.class));
                        }
                    } else {
                        startActivity(new Intent(RateCoRiders.this, BottomSheetRideActivity.class));
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
                            SessionManager.getInstance().removeUserData(RateCoRiders.this);
                            UIHelper.getInstance().switchActivity(RateCoRiders.this, StartUpActivity.class, null, null, null, true);
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
                        sendRating(id, stars, msg);
                    });
                }
            }
        }, SessionManager.getAccessToken()).rate_Driver(parms);
    }
}
