package com.quawlebs.drupp.view.ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.models.TripHistoryModel;
import com.quawlebs.drupp.util.AppUtil;
import com.quawlebs.drupp.util.BaseRecyclerView;
import com.quawlebs.drupp.util.RxBus;
import com.quawlebs.drupp.view.adapters.IAdapterClicked;
import com.quawlebs.drupp.view.adapters.RideHistoryAdapter;
import com.quawlebs.drupp.view.adapters.TripHistorySection;
import com.quawlebs.drupp.view.ui.TripHistoryFinal;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;
import com.quawlebs.drupp.view.ui.dialog.DateDialog;
import com.quawlebs.drupp.helpers.IDateDialogResponseObserver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import retrofit2.Response;

public class RideHistory extends MainBaseActivity implements IAdapterClicked , IDateDialogResponseObserver {

    @BindView(R.id.trips_history_empty_view)
    LinearLayout emptyLayout;
    @BindView(R.id.trips_history_recycler)
     BaseRecyclerView completedRideRecyclerView;
    List<TripHistoryModel> filteredList= new ArrayList<>();
    public static final long CONVERT_TO_DAYS_MILLI = 24 * 3600 * 1000L;
    public static final long CONVERT_TO_MONTHS_MILLI = 30*24 * 3600 * 1000L;
    public static final long CONVERT_TO_HOURS_MILLI = 3600*1000L;
    public static final long CONVERT_TO_SECONDS_MILLI = 1000L;
    private HashMap<String, List<TripHistoryModel>> tripMap = new HashMap<>();
    private ArrayList<TripHistoryModel> tripList = new ArrayList<>();
    private SectionedRecyclerViewAdapter sectionedRecyclerViewAdapterold;
    private RideHistoryAdapter sectionedRecyclerViewAdapter;
    private boolean settingDateFrom=false;
    private ImageView calendarFrom,calendarTo;
    private TextView textDateFrom,textDateTo;
    private DateDialog dateDialog;
    private String dateToSet;
    private long timeFromInMilli;
    private long timeToInMilli;
    private ConstraintLayout containerTop;
    @Override
    protected void setUp() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history);

        ButterKnife.bind(this);

        textDateFrom=findViewById(R.id.tv_date_from);
        textDateTo=findViewById(R.id.tv_date_to);
        calendarFrom=findViewById(R.id.iv_calendar_from);
        calendarTo=findViewById(R.id.iv_calendar_to);
        containerTop=findViewById(R.id.container_top);
        findViewById(R.id.image_back).setOnClickListener(v -> onBackPressed());
        Toast.makeText(getApplicationContext(),"Rider History",Toast.LENGTH_LONG).show();

       textDateFrom.setOnClickListener(v -> showDateDialog(true));
        calendarFrom.setOnClickListener(v -> showDateDialog(true));
       textDateTo.setOnClickListener(v -> showDateDialog(false));
        calendarTo.setOnClickListener(v -> showDateDialog(false));
        sectionedRecyclerViewAdapter = new RideHistoryAdapter(this,tripList);
        completedRideRecyclerView.setAdapter(sectionedRecyclerViewAdapter);
        completedRideRecyclerView.setEmptyView(emptyLayout);
        getData();
    }
    private void showDateDialog(boolean settingDateFrom) {
        this.settingDateFrom=settingDateFrom;
        Bundle bundle = new Bundle();
        bundle.putLong(AppConstants.K_MIN_DATE, System.currentTimeMillis()-72* CONVERT_TO_MONTHS_MILLI);
        bundle.putLong(AppConstants.K_MAX_DATE, System.currentTimeMillis() );
        dateDialog = DateDialog.newInstance(bundle);
        dateDialog.setiDateDialogResponseObserver(this);
        dateDialog.show(getSupportFragmentManager(), DateDialog.class.getSimpleName());
    }

    public void getData() {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<TripHistoryModel>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<TripHistoryModel>> response) {
                hideLoading();
                tripList.clear();
                tripMap.clear();
                sectionedRecyclerViewAdapter.removeAllSections();
                if (response.isSuccessful() && response.body() != null) {
                    containerTop.setVisibility(View.VISIBLE);
                    tripList.addAll(response.body().getResponse());
                    handleSortData();
                }else{
                    containerTop.setVisibility(View.GONE);
                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<TripHistoryModel>> response) {
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
                        getData();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getCompletedRides();
    }

    private void handleSortData() {
        try {
            if (tripList.size() != 0) {
                for (TripHistoryModel t : tripList) {
                    String converted_date = AppUtil.handleDateString(t.getDayTimestamp());
                    if (tripMap.containsKey(converted_date)) {
                        List<TripHistoryModel> new_trip_list = tripMap.get(converted_date);
                        new_trip_list.add(t);
                        tripMap.put(converted_date, new_trip_list);
                    } else {
                        List<TripHistoryModel> new_trip_list = new ArrayList<>();
                        new_trip_list.add(t);

                        tripMap.put(converted_date, new_trip_list);

                    }
                }

                for (Map.Entry<String, List<TripHistoryModel>> entry : tripMap.entrySet()) {
                    TripHistorySection tripRideSection = new TripHistorySection(entry.getKey(), entry.getValue(), this);
                    sectionedRecyclerViewAdapter.addSection(tripRideSection);
                }

            }

        } catch (Exception e) {
            Log.d(getClass().getSimpleName(), e.getMessage());
        }
        sectionedRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPostionTap(int position, String id) {
        Intent intent = new Intent(this, TripHistoryFinal.class);
        RxBus.getInstance().setIntentEvent(tripList.get(position));
        startActivity(intent);
    }

    @Override
    public void onDateSelected(int year, int month, int dayOfMonth) {
        showLoading();
        String sday = String.valueOf(dayOfMonth);
        String smonth = String.valueOf(month);
        if (dayOfMonth < 10) {
            sday = "0" + sday;
        }
        if (month < 10) {
            smonth = "0" + smonth;
        }
        dateToSet = sday + "/" + smonth + "/" + year;
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM, yyyy");
        if(settingDateFrom){
            textDateFrom.setText(dateToSet);
            try {
                Date date = sdf.parse(dateToSet);
                timeFromInMilli=date.getTime()/1000L;
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
        else{
            textDateTo.setText(dateToSet);
            try {
                Date date = sdf.parse(dateToSet);
                timeToInMilli=(date.getTime()+CONVERT_TO_DAYS_MILLI)/1000L;


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        filterList();
    }

    private void filterList() {
        filteredList.clear();
        for(int i=0;i<tripList.size();i++){
            Log.d("Filter",tripList.get(i).getSource() + "date is " +tripList.get(i).getRideDate()+"and "+timeToInMilli);
        }
        for(int i=0;i<tripList.size();i++){
            if(Long.parseLong(tripList.get(i).getRideDate())>=timeFromInMilli){
                if(Long.parseLong(tripList.get(i).getRideDate())<=timeToInMilli){

                    filteredList.add(tripList.get(i));
                }
            }
        }
        hideLoading();
        sectionedRecyclerViewAdapter = new RideHistoryAdapter(this,filteredList);
        completedRideRecyclerView.setAdapter(sectionedRecyclerViewAdapter);
    }
}
