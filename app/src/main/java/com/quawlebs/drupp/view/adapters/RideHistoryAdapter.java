package com.quawlebs.drupp.view.adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quawlebs.drupp.R;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.quawlebs.drupp.helpers.Timing;
import com.quawlebs.drupp.models.TripHistoryModel;
import com.quawlebs.drupp.util.RxBus;
import com.quawlebs.drupp.view.TripHistoryDialogFinal;
import com.quawlebs.drupp.view.ui.history.RideHistory;

import java.util.List;

public class RideHistoryAdapter extends RecyclerView.Adapter<RideHistoryAdapter.RideHistoryHolder> {

    private Context context;
    private List<TripHistoryModel> tripHistory;
    public RideHistoryAdapter(Context context, List<TripHistoryModel> tripHistory){
        this.tripHistory=tripHistory;
        this.context=context;
    }
    private TripHistoryDialogFinal tripHistoryFinalDialog;
    @NonNull
    @Override
    public RideHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RideHistoryHolder(LayoutInflater.from(context).inflate(R.layout.layout_trip_history_new, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RideHistoryHolder holder, int position) {
        TripHistoryModel riderInfo=tripHistory.get(position);
        holder.sourceCity.setText(riderInfo.getSource());
        holder.destination.setText(riderInfo.getDestination());

        String fare=context.getString(R.string.naira) + riderInfo.getTotalFare();


        holder.rideFare.setText(fare);
//        holder.rideStatus.setText(riderInfo.getSucceededOrCancelled());
//        if(riderInfo.getSucceededOrCancelled().equals("Successful")){
//            holder.rideStatus.setTextColor(context.getResources().getColor(R.color.app_theme_color));
//        }
//        else{
//            holder.rideStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
//        }

        setTimeDiff(holder,position);
    }

    @Override
    public int getItemCount() {
        return tripHistory.size();
    }
    private void setTimeDiff(RideHistoryHolder holder, int position) {
        try {

            CharSequence ago = DateUtils.getRelativeTimeSpanString(Long.parseLong(tripHistory.get(position).getRideDate()) * 1000L, Timing.getCurrentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
            holder.rideDate.setText(ago);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void removeAllSections() {
    }

    public void addSection(TripHistorySection tripRideSection) {
    }

    public class RideHistoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView sourceCity;
        public TextView destination;
        public TextView rideDate;
        public TextView rideFare;
        public TextView rideStatus;

        public int position;
        public RideHistoryHolder(@NonNull View itemView) {
            super(itemView);
            sourceCity=itemView.findViewById(R.id.tvSourceCity);
            destination=itemView.findViewById(R.id.tv_rider_destination);
            rideDate=itemView.findViewById(R.id.tv_time_since);
            rideFare=itemView.findViewById(R.id.tv_ride_fare);
            rideStatus=itemView.findViewById(R.id.tv_status);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            Intent intent = new Intent(context, TripHistoryFinal.class);
            tripHistoryFinalDialog = new TripHistoryDialogFinal();
            FragmentTransaction transaction = ((RideHistory)context).getSupportFragmentManager()
                    .beginTransaction()
                    .add(tripHistoryFinalDialog, TripHistoryDialogFinal.class.getSimpleName());
            transaction.commit();
            RxBus.getInstance().setIntentEvent(tripHistory.get(getAdapterPosition()));
//            context.startActivity(intent);
        }
    }
}
