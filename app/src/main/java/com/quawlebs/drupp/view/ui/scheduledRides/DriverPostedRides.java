package com.quawlebs.drupp.view.ui.scheduledRides;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.login.StartUpActivity;
import com.quawlebs.drupp.models.ScheduledRideUser;
import com.quawlebs.drupp.models.TripHistoryModel;
import com.quawlebs.drupp.util.AppUtil;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.view.adapters.DriverScheduleRideAdapter;
import com.quawlebs.drupp.view.adapters.IAdapterClicked;
import com.quawlebs.drupp.view.adapters.RideSection;
import com.quawlebs.drupp.view.adapters.UserScheduledRidesAdapter;
import com.quawlebs.drupp.view.ui.base.MainBaseFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import retrofit2.Response;

public class DriverPostedRides extends MainBaseFragment implements IAdapterClicked {

    private RecyclerView tripRecyclerView;
    //private SectionedRecyclerViewAdapter adapter;
    private DriverScheduleRideAdapter adapter;
    private HashMap<String, List<TripHistoryModel>> tripMap = new HashMap<>();
    private ArrayList<TripHistoryModel> tripList = new ArrayList<>();
    ArrayList<ScheduleRideDriverResponse> scheduleList=new ArrayList<>();
    private static final String TAG = "DriverPostedRides";

    @Override
    protected void initViewsForFragment(View view) {
        tripRecyclerView = view.findViewById(R.id.ScheduleRidesRecycler);
        tripRecyclerView.setLayoutManager(new LinearLayoutManager(getmContext()));
    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_posted_rides, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new DriverScheduleRideAdapter(getActivity(),scheduleList);
        tripRecyclerView.setAdapter(adapter);
    }


    public void getData() {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<ScheduleRideDriverResponse>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<ScheduleRideDriverResponse>> response) {
                hideLoading();
                Log.d("DriverScheduledRides","request made");
                //allTrips.clear();
                scheduleList.clear();
                //sectionAdapter.removeAllSections();

                if (response.isSuccessful() && response.body() != null) {
                    for (ScheduleRideDriverResponse scheduleRideDriverResponse : response.body().getResponse()) {
                        Log.d("ScheduledRideUser","request made");
                        Log.d("ScheduledRideUser","Your rides request response size:"+ response.body().getResponse());
                        //String currentDateKey = handleDateString(scheduleRideDriverResponse.getDayTimestamp());
                        String timeStamp = handleDateString(scheduleRideDriverResponse.getDayTimestamp());
                        //whole_list.put(timeStamp,scheduleRideDriverResponse.getScheduledRides());
                        scheduleList.add(scheduleRideDriverResponse);

                        //getAllTimeStamps(scheduleRideDriverResponse.getScheduledRides());


                    }
                    if(scheduleList.size()>0){
                        adapter = new DriverScheduleRideAdapter(getActivity(),scheduleList);
                        tripRecyclerView.setAdapter(adapter);
                        getView().findViewById(R.id.schedule_ride_btn).setVisibility(View.GONE);
                        getView().findViewById(R.id.notification_icon).setVisibility(View.GONE);
                        getView().findViewById(R.id.no_rides_label).setVisibility(View.GONE);
                    }
                    else {
                        getView().findViewById(R.id.schedule_ride_btn).setVisibility(View.VISIBLE);
                        getView().findViewById(R.id.notification_icon).setVisibility(View.VISIBLE);
                        getView().findViewById(R.id.no_rides_label).setVisibility(View.VISIBLE);
                    }
                }




                //                tripList.clear();
//                tripMap.clear();
//                scheduleList.clear();
//                hideLoading();
//                if (response.isSuccessful() && response.body() != null) {
//                    List<ScheduleRideDriverResponse> scheduledRideDriver = response.body().getResponse();
//
//                    if (scheduledRideDriver.size() == 0 && getView() != null) {
//                        getView().findViewById(R.id.schedule_ride_btn).setVisibility(View.VISIBLE);
//                        getView().findViewById(R.id.notification_icon).setVisibility(View.VISIBLE);
//                        getView().findViewById(R.id.no_rides_label).setVisibility(View.VISIBLE);
//                    }
//
//                    for (ScheduleRideDriverResponse scheduledRideUser : response.body().getResponse()) {
////                        String currentKey = AppUtil.handleDateString(scheduledRideUser.getDayTimestamp());
////                        tripMap.put(currentKey, scheduledRideUser.getScheduledRides());
//                        String timeStamp=handleDateString(scheduledRideUser.getDayTimestamp());
//                        //whole_list.put(timeStamp,scheduleRideDriverResponse.getScheduledRides());
//                        scheduleList.add(scheduledRideUser);
//                    }
                    //handleSortData();
                }



            @Override
            public void onErrorList(Response<QualStandardResponseList<ScheduleRideDriverResponse>> response) {
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
                            SessionManager.getInstance().removeUserData(getmContext());
                            UIHelper.getInstance().switchActivity(getmActivity(), StartUpActivity.class,
                                    null, null, null, true);
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
                        getData();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getScheduledRidesDriver();

    }

//    private void handleSortData() {
//        adapter.removeAllSections();
//        for (Map.Entry<String, List<TripHistoryModel>> entry : tripMap.entrySet()) {
//            adapter.addSection(new RideSection(entry.getKey(), entry.getValue(), this));
//        }
//        adapter.notifyDataSetChanged();
 //  }

    @Override
    public void onPostionTap(int position, String id) {
        Intent intent = new Intent(getContext(), DriverPostedRideSingelView.class);
        intent.putExtra("rideId", id);
        startActivity(intent);
    }

    private String handleDateString(String timeStamp) {
        if (timeStamp.isEmpty()) {
            return "";
        }
        if (timeStamp != null) {
            Date given_timestamp = new java.util.Date((long) (Long.valueOf(timeStamp)) * 1000);

            String my_timestamp = String.valueOf(given_timestamp);

            String[] arr = my_timestamp.split(" ");
            String week_day = arr[0];
            String month = arr[1];
            String day = arr[2];
            String time = arr[3];
            String utc = arr[4];
            String year = arr[5];

            return (day + "/" + month + "/" + year);
        } else {
            return "";
        }
    }

    @Override
    public void showAlertDialog(int resId) {

    }
}





