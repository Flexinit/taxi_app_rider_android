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
import com.quawlebs.drupp.models.ScheduledRideUserReponse;
import com.quawlebs.drupp.models.TripHistoryModel;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.view.adapters.IAdapterClicked;
import com.quawlebs.drupp.view.adapters.RideSection;
import com.quawlebs.drupp.view.adapters.UserScheduledRidesAdapter;
import com.quawlebs.drupp.view.ui.PostRideActivity;
import com.quawlebs.drupp.view.ui.base.MainBaseFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import retrofit2.Response;

public class UserPostedRideLater extends MainBaseFragment implements IAdapterClicked {
    private RecyclerView tripLv;
    ArrayList<ScheduledRideUserReponse> scheduleList=new ArrayList<>();
    private ArrayList<TripHistoryModel> tripList = new ArrayList<>();
    private HashMap<String, List<TripHistoryModel>> tripMap = new HashMap<>();
    //private SectionedRecyclerViewAdapter ridesAdapter;
    private UserScheduledRidesAdapter ridesAdapter;
    private Button btnScheduleRides;
    @Override
    protected void initViewsForFragment(View view) {
        tripLv = view.findViewById(R.id.ScheduleRidesRecycler);
        tripLv.setLayoutManager(new LinearLayoutManager(getmContext()));
        //ridesAdapter = new SectionedRecyclerViewAdapter();
        btnScheduleRides = view.findViewById(R.id.schedule_ride_btn);
        ridesAdapter=new UserScheduledRidesAdapter(getActivity(),scheduleList);
        tripLv.setAdapter(ridesAdapter);
        btnScheduleRides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PostRideActivity.class));
            }
        });
        getData();
    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_posted_rides, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tripLv.setAdapter(ridesAdapter);
    }

    public void getData() {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<ScheduledRideUserReponse>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<ScheduledRideUserReponse>> response) {
//                tripList.clear();
//                tripMap.clear();
                scheduleList.clear();
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    List<ScheduledRideUserReponse> scheduledRideUsers = response.body().getResponse();
                    Log.d("ScheduledRideUser","request made");
                    Log.d("ScheduledRideUser","Your rides request response size:"+ response.body().getResponse());
                    for (ScheduledRideUserReponse scheduledRideUserReponse : scheduledRideUsers) {
                        //String currentKey = handleDateString(scheduledRideUser.getDayTimestamp());
                        //tripMap.put(currentKey, scheduledRideUser.getScheduledRides());
                        String timeStamp=handleDateString(scheduledRideUserReponse.getDayTimestamp());
                        //whole_list.put(timeStamp,scheduleRideDriverResponse.getScheduledRides());
                        scheduleList.add(scheduledRideUserReponse);

                    }
                    if(scheduleList.size()>0){

                        getView().findViewById(R.id.schedule_ride_btn).setVisibility(View.GONE);
                        getView().findViewById(R.id.notification_icon).setVisibility(View.GONE);
                        getView().findViewById(R.id.no_rides_label).setVisibility(View.GONE);
                        ridesAdapter=new UserScheduledRidesAdapter(getActivity(),scheduleList);
                        tripLv.setAdapter(ridesAdapter);

                    }
                    else {
                        getView().findViewById(R.id.schedule_ride_btn).setVisibility(View.VISIBLE);
                        getView().findViewById(R.id.notification_icon).setVisibility(View.VISIBLE);
                        getView().findViewById(R.id.no_rides_label).setVisibility(View.VISIBLE);
                    }
                    //handleSortData();
                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<ScheduledRideUserReponse>> response) {

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
                Log.d("UserPostedRideLater","failure");
                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        getData();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getScheduledRidesUser();

    }

   /* private void handleSortData() {
        ridesAdapter.removeAllSections();
        for (Map.Entry<String, List<TripHistoryModel>> entry : tripMap.entrySet()) {
            ridesAdapter.addSection(new RideSection(entry.getKey(), entry.getValue(), this));
        }
        ridesAdapter.notifyDataSetChanged();
    }*/

    @Override
    public void onPostionTap(int position, String id) {
        Intent intent = new Intent(getContext(), UserPostedRideSingelView.class);
        intent.putExtra(AppConstants.K_RIDE_ID, id);
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
