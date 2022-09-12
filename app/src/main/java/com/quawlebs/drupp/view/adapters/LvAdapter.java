package com.quawlebs.drupp.view.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quawlebs.drupp.models.TripHistoryModel;
import com.quawlebs.drupp.R;

import java.util.ArrayList;

public class LvAdapter extends BaseAdapter {
    private static final String TAG = "LvAdapter";
    private static int HEADER_COUNT = 0;
    private static int ITEM_COUNT = 0;
    private static final int HEADER = 0;
    private static final int ITEM = 1;
    private final Context context;
    private ArrayList<TripHistoryModel> objectList;
    private LayoutInflater inflater;
    private IAdapterClicked iAdapterClicked;

    public void setiAdapterClicked(IAdapterClicked iAdapterClicked) {
        this.iAdapterClicked = iAdapterClicked;
    }


    public LvAdapter(ArrayList<TripHistoryModel> objectList, Context context) {
        this.context = context;
        this.objectList = objectList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d(TAG, "LvAdapter: Constructor");
    }

    @Override
    public int getViewTypeCount() {
        //return getCount();
        //return super.getViewTypeCount();
        //return objectList.size();
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (objectList.get(position) != null) {
            Log.d(TAG, "getItemViewType: trip");
            return ITEM;
        } else {
            Log.d(TAG, "getItemViewType: header");
            return HEADER;
        }
    }

    @Override
    public int getCount() {
        return objectList.size();
    }

    @Override
    public Object getItem(int position) {
        return objectList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            if (getItemViewType(position) == ITEM) {
                convertView = inflater.inflate(R.layout.layout_trip_history_item, null);
                ViewHolder viewHolder = new ViewHolder(convertView, position);
                //convertView.setTag(viewHolder);
                TextView SCity = convertView.findViewById(R.id.tvSourceCity);
                TextView DCity = convertView.findViewById(R.id.tvDestinationCity);
                SCity.setText((objectList.get(position)).getSource());
                DCity.setText((objectList.get(position)).getDestination());
            } else {
                convertView = inflater.inflate(R.layout.layout_trip_history_header, null);
                TextView header = convertView.findViewById(R.id.tvHeaderTripHis);
                header.setText(objectList.get(position).toString());
            }
            return convertView;
        }
        return convertView;
    }

    public class ViewHolder {
        public View view;

        public ViewHolder(View view, final int position) {
            this.view = view;
            view.setOnClickListener(v -> iAdapterClicked.onPostionTap(position, objectList.get(position).getRideId().toString()));
        }
    }
}
