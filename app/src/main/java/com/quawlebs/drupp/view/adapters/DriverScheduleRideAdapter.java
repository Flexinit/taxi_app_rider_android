package com.quawlebs.drupp.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.models.DriverRideModel;
import com.quawlebs.drupp.models.ScheduledRideUser;
import com.quawlebs.drupp.models.TripHistoryModel;
import com.quawlebs.drupp.util.RxBus;
import com.quawlebs.drupp.view.ui.scheduledRides.DriverPostedRideSingelView;
import com.quawlebs.drupp.view.ui.scheduledRides.ScheduleRideDriverResponse;
import com.quawlebs.drupp.view.ui.scheduledRides.ScheduledRideDetailDialogDriver;
import com.quawlebs.drupp.view.ui.scheduledRides.ScheduledRideDetailDialogUser;
import com.quawlebs.drupp.view.ui.scheduledRides.ScheduledRidesActivity;

import java.util.ArrayList;
import java.util.List;

public class DriverScheduleRideAdapter extends RecyclerView.Adapter<DriverScheduleRideAdapter.DriverScheduledRidesHolder>{

    private Context context;
    ArrayList<ScheduleRideDriverResponse> scheduleList;
    private List<TripHistoryModel> driverRideModelList=new ArrayList<>();

    public DriverScheduleRideAdapter(Context context, ArrayList<ScheduleRideDriverResponse> scheduleList){
        this.scheduleList=scheduleList;
        this.context=context;
        getAllDriverModels();

    }
    private void getAllDriverModels() {
        driverRideModelList.clear();
        for(int i=0;i<scheduleList.size();i++){
            driverRideModelList.addAll(scheduleList.get(i).getScheduledRides());
        }
    }
    @NonNull
    @Override
    public DriverScheduleRideAdapter.DriverScheduledRidesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DriverScheduledRidesHolder(LayoutInflater.from(context).inflate(R.layout.layout_scheduled_rides_item_new, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DriverScheduleRideAdapter.DriverScheduledRidesHolder holder, int position) {
        TripHistoryModel rideModel=driverRideModelList.get(position);
        holder.sourceCity.setText(rideModel.getSource());
        holder.rideDate.setText(handleDateString(rideModel.getDayTimestamp()));
        holder.destination.setText(rideModel.getDestination());
        holder.rideTime.setText(handleTimeString(rideModel.getDayTimestamp()));
        String rideTypePreference="";
        /*if(rideModel.getRideType()==1){
            rideTypePreference="Single";
        }
        else{
            rideTypePreference="Pool";
        }
        holder.rideTypeAndPreference.setText(rideTypePreference);*/
    }

    @Override
    public int getItemCount() {
        return driverRideModelList.size();
    }
    private String handleDateString(String timeStamp) {
        java.util.Date given_timestamp = new java.util.Date((long) (Long.valueOf(timeStamp)) * 1000);

        String my_timestamp = String.valueOf(given_timestamp);

        String[] arr = my_timestamp.split(" ");
        String week_day = arr[0];
        String month = arr[1];
        String day = arr[2];
        String time = arr[3];
        String utc = arr[4];
        String year = arr[5];


        switch (month) {
            case "Jan":
                month = "1";
                break;
            case "Feb":
                month = "2";
                break;
            case "Mar":
                month = "3";
                break;
            case "Apr":
                month = "4";
                break;
            case "May":
                month = "5";
                break;
            case "Jun":
                month = "6";
                break;
            case "Jul":
                month = "7";
                break;
            case "Aug":
                month = "8";
                break;
            case "Sep":
                month = "9";
                break;
            case "Oct":
                month = "10";
                break;
            case "Nov":
                month = "11";
                break;
            case "Dec":
                month = "12";
                break;

        }

        String xdate = (day + "/" + month + "/" + year);
        return xdate;
    }
    private String handleTimeString(String timeStamp) {
        java.util.Date given_timestamp = new java.util.Date((long) (Long.valueOf(timeStamp)) * 1000);

        String my_timestamp = String.valueOf(given_timestamp);

        String[] arr = my_timestamp.split(" ");
        String week_day = arr[0];
        String month = arr[1];
        String day = arr[2];
        String time = arr[3];
        String utc = arr[4];
        String year = arr[5];

        String xdate = (day + "/" + month + "/" + year);
        return time;
    }
    public class DriverScheduledRidesHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView sourceCity;
        public TextView destination;
        public TextView rideDate;
        public TextView rideTime;
        public TextView rideTypeAndPreference;
        public int position;



        public DriverScheduledRidesHolder(@NonNull View itemView) {
            super(itemView);
            sourceCity=itemView.findViewById(R.id.tvSourceCity);
            destination=itemView.findViewById(R.id.tv_rider_destination);
            rideDate=itemView.findViewById(R.id.tv_ride_date);
            rideTime=itemView.findViewById(R.id.tv_ride_time);
            rideTypeAndPreference=itemView.findViewById(R.id.tv_ridetype_and_preference);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ScheduledRideDetailDialogDriver scheduledRideDetailDialog = new ScheduledRideDetailDialogDriver();
            FragmentTransaction transaction = ((ScheduledRidesActivity)context).getSupportFragmentManager()
                    .beginTransaction()
                    .add(scheduledRideDetailDialog, ScheduledRideDetailDialogDriver.class.getSimpleName());
            transaction.commit();
            RxBus.getInstance().setIntentEvent(driverRideModelList.get(getAdapterPosition()));
        }
    }
}
