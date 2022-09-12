package com.quawlebs.drupp.view.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.quawlebs.drupp.models.NewsFeeds;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.helpers.Timing;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class NewsDetailsActivity extends MainBaseActivity {

    @BindView(R.id.tv_title)
    TextView mToolTitle;
    @BindView(R.id.image_news)
    ImageView mNewsImage;
    @BindView(R.id.tv_news)
    TextView mNewsFeed;
    @BindView(R.id.tv_header)
    TextView mHeader;
    @BindView(R.id.tv_time)
    TextView mTime;
    @BindView(R.id.tv_category_name)
    TextView mCategoryName;

    @Override
    protected void setUp() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);

        mToolTitle.setText(R.string.news);

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_emergancy, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NewsDetailsActivity.this);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setView(alertLayout);


        if (getIntent() != null && getIntent().hasExtra(AppConstants.K_ID)) {
            getNewsDetails(getIntent().getStringExtra(AppConstants.K_ID));
        }
    }

    @OnClick(R.id.iv_back)
    public void onBackPress() {
        onBackPressed();
    }

    public void getNewsDetails(String id) {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<NewsFeeds>() {
            @Override
            public void onResponse(Response<QualStandardResponse<NewsFeeds>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    setNewsDetails(response.body().getResponse());

                }
            }

            @Override
            public void onError(Response<QualStandardResponse<NewsFeeds>> response) {
                hideLoading();
                showErrorPrompt(response);
            }

            @Override
            public void onNullResponse() {
                hideLoading();
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoading();
                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        getNewsDetails(id);
                    });
                }
            }
        }, SessionManager.getAccessToken()).getNewsDetails(id);
    }

    private void setNewsDetails(NewsFeeds feeds) {
        try {
            Glide.with(NewsDetailsActivity.this).load(AppConstants.IMAGE_URL + feeds.getNewsImage()).apply(new RequestOptions().placeholder(R.drawable.news_icon)
                    .error(R.drawable.news_icon)).into(mNewsImage);
            mNewsFeed.setText(feeds.getDesc());
            mHeader.setText(feeds.getHeadLine());
            mTime.setText(Timing.getTimeInString(feeds.getTimeStamp(), Timing.TimeFormats.MONTH_DD_YEAR));
            mCategoryName.setText(getString(R.string.news_category, feeds.getCategoryName(), feeds.getNewsSubCategory()));

        } catch (Exception e) {
            Log.e("Exception: ", e.getMessage());
        }
    }
}
