package com.quawlebs.drupp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quawlebs.drupp.models.BusInfoModel;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.IAdapterItemClickListener;

import java.util.ArrayList;

public class BusRidesAdapter extends RecyclerView.Adapter<BusRidesAdapter.NewHolder> {
    private Context mContext;
    private int mResId;
    private ArrayList<BusInfoModel> mData;
    private IAdapterItemClickListener iAdapterItemClickListener;

    public BusRidesAdapter(Context context, int resId, ArrayList<BusInfoModel> data) {
        mContext = context;
        mResId = resId;
        mData = data;
    }

    @NonNull
    @Override
    public NewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewHolder(LayoutInflater.from(mContext).inflate(mResId, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewHolder holder, int position) {
        holder.mSource.setText(mData.get(position).getSource());
        holder.mDestination.setText(mData.get(position).getDestination());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setiAdapterItemClickListener(IAdapterItemClickListener iAdapterItemClickListener) {
        this.iAdapterItemClickListener = iAdapterItemClickListener;
    }

    public class NewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mSource, mDestination;

        public NewHolder(@NonNull View itemView) {
            super(itemView);
            mSource = itemView.findViewById(R.id.tvSourceCity);
            mDestination = itemView.findViewById(R.id.tvDestinationCity);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (iAdapterItemClickListener == null) return;
            iAdapterItemClickListener.onAdapterItemClick(v, getAdapterPosition());
        }
    }
}
