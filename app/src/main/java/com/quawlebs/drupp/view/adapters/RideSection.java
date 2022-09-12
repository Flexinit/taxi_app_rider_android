package com.quawlebs.drupp.view.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quawlebs.drupp.models.TripHistoryModel;
import com.quawlebs.drupp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

public class RideSection extends Section {
    private List<TripHistoryModel> mUserRides;
    private String title;
    private IAdapterClicked iAdapterClicked;

    public RideSection(String title, List<TripHistoryModel> data, IAdapterClicked iAdapterClicked) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.scheduled_ride_item)
                .headerResourceId(R.layout.scheduled_ride_header).build());
        mUserRides = data;
        this.title = title;
        this.iAdapterClicked = iAdapterClicked;

    }


    @Override
    public int getContentItemsTotal() {
        return mUserRides.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.mSource.setText(mUserRides.get(position).getSource());
        itemViewHolder.mDestination.setText(mUserRides.get(position).getDestination());
        itemViewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iAdapterClicked.onPostionTap(position, mUserRides.get(position).getRideId().toString());
            }
        });

    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);

    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        super.onBindHeaderViewHolder(holder);
        HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
        headerViewHolder.header.setText(title);
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_scheduled_ride_header)
        TextView header;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_source)
        TextView mSource;
        @BindView(R.id.tv_destination)
        TextView mDestination;
        private View rootView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            rootView = itemView;

        }

    }
}
