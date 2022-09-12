package com.quawlebs.drupp.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.quawlebs.drupp.models.NewsFeeds;
import com.quawlebs.drupp.view.ui.NewsDetailsActivity;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.Timing;

import java.util.List;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.Holder> {

    private Context context;
    private List<NewsFeeds> newsFeeds;

    public NewsFeedAdapter(Context context, List<NewsFeeds> list) {
        this.newsFeeds = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.item_news_feed, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        try {
            NewsFeeds feeds = newsFeeds.get(i);
            Glide.with(context).load(AppConstants.IMAGE_URL + feeds.getNewsImage()).apply(new RequestOptions().placeholder(R.drawable.news_icon)
                    .error(R.drawable.news_icon)).into(holder.newsImage);
            holder.newsTitle.setText(feeds.getHeadLine());
            holder.newsTime.setText(Timing.getTimeInString(feeds.getTimeStamp(), Timing.TimeFormats.CUSTOM_DAY));
            holder.newsDesc.setText(feeds.getDesc());
        } catch (Exception e) {
            Log.e("Exception: ", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return newsFeeds.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        private ImageView newsImage;
        private TextView newsTitle, newsDesc, newsTime;

        Holder(@NonNull final View itemView) {
            super(itemView);

            newsImage = itemView.findViewById(R.id.iv_news_thumb);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsDesc = itemView.findViewById(R.id.news_desc);
            newsTime = itemView.findViewById(R.id.news_time);

            itemView.setOnClickListener(v -> context.startActivity(new Intent(itemView.getContext(), NewsDetailsActivity.class).putExtra(AppConstants.K_ID,
                    String.valueOf(newsFeeds.get(getAdapterPosition()).getId()))));
        }
    }
}
