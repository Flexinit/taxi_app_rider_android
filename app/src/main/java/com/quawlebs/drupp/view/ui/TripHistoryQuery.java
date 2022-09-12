package com.quawlebs.drupp.view.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quawlebs.drupp.models.TripHistoryModel;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.RxBus;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.view.ui.base.BaseActivity;
import com.quawlebs.drupp.view.ui.ride.BottomSheetRideActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class TripHistoryQuery extends BaseActivity {

    LinearLayout l1, l2, l3, l4;
    String re = "null", ride_id, ride_source, ride_destination;
    Button done;
    String query;
    EditText etQuery;
    @BindView(R.id.tv_title)
    public TextView title;
    private TripHistoryModel tripHistoryModel;
    private Disposable tripDisposable;


    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history_query);
        ButterKnife.bind(this);
        setToolbarTitle(R.string.query);
        getToolbarBack().setOnClickListener(v -> onBackPressed());
        getSwipeRefreshLayout().setEnabled(false);

        l1 = findViewById(R.id.r1);
        l2 = findViewById(R.id.r2);
        l3 = findViewById(R.id.r3);
        l4 = findViewById(R.id.r4);

        done = findViewById(R.id.done);
        etQuery = findViewById(R.id.etQuery);
        Intent intent = getIntent();

        tripDisposable = RxBus.getInstance().getIntentEvent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof TripHistoryModel) {
                        tripHistoryModel = (TripHistoryModel) o;
                        ride_id = tripHistoryModel.getId().toString();
                        ride_source = tripHistoryModel.getSource();
                        ride_destination = tripHistoryModel.getDestination();
                    }
                });

        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l1.setBackgroundResource(R.drawable.textview_background_afterclick);
                l2.setBackgroundResource(R.drawable.textview_background);
                l3.setBackgroundResource(R.drawable.textview_background);
                l4.setBackgroundResource(R.drawable.textview_background);
                re = getString(R.string.resend_rec);
//                intent.putExtra("reason",getString(R.string.resend_rec));
//                startActivity(intent);
            }
        });
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l2.setBackgroundResource(R.drawable.textview_background_afterclick);
                l1.setBackgroundResource(R.drawable.textview_background);
                l3.setBackgroundResource(R.drawable.textview_background);
                l4.setBackgroundResource(R.drawable.textview_background);
                re = getString(R.string.misconduct);
//                intent.putExtra("reason",getString(R.string.misconduct));
//                startActivity(intent);
            }
        });
        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l3.setBackgroundResource(R.drawable.textview_background_afterclick);
                l2.setBackgroundResource(R.drawable.textview_background);
                l1.setBackgroundResource(R.drawable.textview_background);
                l4.setBackgroundResource(R.drawable.textview_background);
                re = getString(R.string.lost_item);
//                intent.putExtra("reason",getString(R.string.lost_item));
//                startActivity(intent);
            }
        });
        l4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l4.setBackgroundResource(R.drawable.textview_background_afterclick);
                l2.setBackgroundResource(R.drawable.textview_background);
                l3.setBackgroundResource(R.drawable.textview_background);
                l1.setBackgroundResource(R.drawable.textview_background);
                re = getString(R.string.longer_route);
//                intent.putExtra("reason",getString(R.string.longer_route));
//                startActivity(intent);
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (re.equals("null")) {
                    Toast.makeText(TripHistoryQuery.this, "Please select one reason", Toast.LENGTH_SHORT).show();
                } else {
                    query = etQuery.getText().toString();
                    if (!query.isEmpty()) {
                        re = re + " (" + query + ")";
                    }
                    showLoading();
                    submitquery(ride_id, ride_source, ride_destination, re);
                }
            }
        });
        title.setText(R.string.query);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tripDisposable.dispose();
    }

    @OnClick(R.id.iv_back)
    public void onBackPress() {
        onBackPressed();
    }

    private void submitquery(String ride_id, String ride_source, String ride_destination, String re) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(done.getWindowToken(), 0);
        DruppRequestHandler.clearInstance();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ride_id", ride_id);
        hashMap.put("ride_source", ride_source);
        hashMap.put("ride_destination", ride_destination);
        hashMap.put("issue", re);
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
                        submitquery(ride_id, ride_source, ride_destination, re);
                    });
                }
            }

            @Override
            public void onErrorList(Response response) {
                hideLoading();
                Toast.makeText(TripHistoryQuery.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponseList(Response response) {
                hideLoading();
                popup();
            }
        }).supportQuery(hashMap);

    }

    public void popup() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_query_sent, null);
        androidx.appcompat.app.AlertDialog.Builder alertDialogbuilder = new androidx.appcompat.app.AlertDialog.Builder(TripHistoryQuery.this);
        androidx.appcompat.app.AlertDialog alertDialog = alertDialogbuilder.create();
        alertDialog.setView(alertLayout);
        alertDialog.show();
        alertLayout.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TripHistoryQuery.this, BottomSheetRideActivity.class));
            }
        });
    }


}
