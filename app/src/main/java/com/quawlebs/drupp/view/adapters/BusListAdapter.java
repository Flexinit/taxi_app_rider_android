package com.quawlebs.drupp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import com.quawlebs.drupp.models.BusModel;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.AutoScrollableTextView;
import com.quawlebs.drupp.helpers.IAdapterItemClickListener;
import com.quawlebs.drupp.helpers.Timing;

import java.util.ArrayList;
import java.util.List;

public class BusListAdapter extends RecyclerView.Adapter<BusListAdapter.NewHolder> implements Filterable {
    private Context mContext;
    private int mResId;
    private List<BusModel> mData, mDataFiltered;
    private IAdapterItemClickListener iAdapterItemClickListener;

    public BusListAdapter(Context context, int resId, List<BusModel> busModelList) {
        mContext = context;
        mResId = resId;
        mData = busModelList;
        mDataFiltered = busModelList;
    }


    public void setiAdapterItemClickListener(IAdapterItemClickListener iAdapterItemClickListener) {
        this.iAdapterItemClickListener = iAdapterItemClickListener;
    }

    @NonNull
    @Override
    public BusListAdapter.NewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewHolder(LayoutInflater.from(mContext).inflate(mResId, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BusListAdapter.NewHolder holder, int position) {
        try {
//            if (mData.get(position).getSource().length() > 7) {
//                mData.get(position).setSource(mData.get(position).getSource().substring(0, 7) + "...");
//            }
//            if (mData.get(position).getDestination().length() > 7) {
//                mData.get(position).setDestination(mData.get(position).getDestination().substring(0, 7) + "...");
//            }
            if (mDataFiltered.get(position).getRemainingSeats() == 0) {
                holder.btnJoin.setText(mContext.getString(R.string.full));
                holder.btnJoin.setEnabled(false);
            } else {
                holder.btnJoin.setEnabled(true);
                holder.btnJoin.setText(mContext.getString(R.string.join));
            }
            holder.routeName.setText(mContext.getString(R.string.route_name, mDataFiltered.get(position).getSource(), mDataFiltered.get(position).getDestination()));
            holder.departureTime.setText(Timing.getTimeInString(Long.valueOf(mDataFiltered.get(position).getStartTime()), Timing.TimeFormats.HH_12));
            holder.arrivalTime.setText(Timing.getTimeInString(Long.valueOf(mDataFiltered.get(position).getFinishTime()), Timing.TimeFormats.HH_12));
            holder.driverName.setText(mDataFiltered.get(position).getDriverName());
            holder.busNo.setText(mContext.getString(R.string.bus_number, mDataFiltered.get(position).getBusNumber()));
            holder.ratingBar.setRating(mDataFiltered.get(position).getAverageRating().floatValue());
        } catch (Exception ignored) {

        }


    }

    @Override
    public int getItemCount() {
        return mDataFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    mDataFiltered = mData;
                } else {
                    List<BusModel> filteredList = new ArrayList<>();
                    for (BusModel row : mData) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getBusNumber().toLowerCase().contains(charString.toLowerCase())
                                || row.getSource().toLowerCase().contains(charString.toLowerCase())
                                || row.getDestination().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    mDataFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mDataFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mDataFiltered = (ArrayList<BusModel>) results.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    public class NewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView busNo, driverName, departureTime, arrivalTime;
        private AutoScrollableTextView routeName;
        private Button btnJoin;
        private AppCompatRatingBar ratingBar;

        public NewHolder(@NonNull View itemView) {
            super(itemView);
            arrivalTime = itemView.findViewById(R.id.tv_arrival_time);
            ratingBar = itemView.findViewById(R.id.rating_driver);
            busNo = itemView.findViewById(R.id.tv_passenger);
            driverName = itemView.findViewById(R.id.tv_driver_name);
            departureTime = itemView.findViewById(R.id.tv_departure_time);
            routeName = itemView.findViewById(R.id.tv_route_name);
            btnJoin = itemView.findViewById(R.id.btn_join);
            btnJoin.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (iAdapterItemClickListener == null) return;
            iAdapterItemClickListener.onAdapterItemClick(v, getAdapterPosition());
        }
    }
}
