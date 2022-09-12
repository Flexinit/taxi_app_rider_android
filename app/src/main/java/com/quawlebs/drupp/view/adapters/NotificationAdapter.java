package com.quawlebs.drupp.view.adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quawlebs.drupp.models.NotificationModel;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.IAdapterItemClickListener;
import com.quawlebs.drupp.helpers.Timing;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NewHolder> {
    private Context mContext;
    private int mResId;
    private ArrayList<NotificationModel> mData;
    private IAdapterItemClickListener iAdapterItemClickListener;

    public void setiAdapterItemClickListener(IAdapterItemClickListener iAdapterItemClickListener) {
        this.iAdapterItemClickListener = iAdapterItemClickListener;
    }

    public NotificationAdapter(Context context, int resId, ArrayList<NotificationModel> data) {
        mContext = context;
        mResId = resId;
        mData = data;
    }

    @NonNull
    @Override
    public NotificationAdapter.NewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewHolder(LayoutInflater.from(mContext).inflate(mResId, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.NewHolder holder, int position) {
        holder.mMessage.setText(mData.get(position).getNotificationMessage());
        String currentDate = Timing.getTimeInString(mData.get(position).getNotificationTime(), Timing.TimeFormats.DD_MM_YYYY_HH_MM_A);
        holder.mRecent.setText(mContext.getString(R.string._1_hour_ago, currentDate));

//        setTimeDiff(holder, position);
    }

    private void setTimeDiff(NewHolder holder, int position) {
        try {

            CharSequence ago = DateUtils.getRelativeTimeSpanString(
                    mData.get(position).getNotificationTime() * 1000L,
                    Timing.getCurrentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);

            holder.mRecent.setText(ago);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class NewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mMessage;
        TextView mRecent;

        NewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mRecent = itemView.findViewById(R.id.tv_notification_time);
            mMessage = itemView.findViewById(R.id.tv_notification_content);
        }

        @Override
        public void onClick(View v) {
            if (iAdapterItemClickListener == null) return;
            iAdapterItemClickListener.onAdapterItemClick(v, getAdapterPosition());
        }
    }
}
