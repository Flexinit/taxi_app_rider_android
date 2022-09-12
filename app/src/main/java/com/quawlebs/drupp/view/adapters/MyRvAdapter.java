package com.quawlebs.drupp.view.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quawlebs.drupp.models.Trip;
import com.quawlebs.drupp.R;

import java.util.List;

public class MyRvAdapter extends RecyclerView.Adapter<MyRvAdapter.ViewHolder> {

    private List<Object> tlist;
    private Context mContext;

    private int pos;

    private boolean isheader = false;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView SCity, DCity;

        private TextView header;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            SCity = itemView.findViewById(R.id.tvSourceCity);
            DCity = itemView.findViewById(R.id.tvDestinationCity);
            header = itemView.findViewById(R.id.tvHeaderTripHis);
//            if (tlist.get(pos) instanceof Trip)
//            {
//
//                SCity=itemView.findViewById(R.id.tvSourceCity);
//                DCity=itemView.findViewById(R.id.tvDestinationCity);
//
//            }
//            else
//            {
//                header=itemView.findViewById(R.id.tvHeaderTripHis);
//            }

        }
    }

    public MyRvAdapter(List<Object> tList, Context context) {
        this.tlist = tList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyRvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        pos = i;
        if (tlist.get(i) instanceof Trip) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_trip_history_item, viewGroup, false));
        } else
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_trip_history_header, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyRvAdapter.ViewHolder viewHolder, int i) {

        if (tlist.get(i) instanceof Trip) {
            isheader = false;
            final Trip trip = (Trip) tlist.get(i);
            viewHolder.SCity.setText(trip.getSCity());
            viewHolder.DCity.setText(trip.getDCity());

        } else {
            isheader = true;
            final Object obj = tlist.get(i);
            viewHolder.header.setText(obj.toString());
        }


    }

    @Override
    public int getItemCount() {
        return tlist.size();
    }
}
