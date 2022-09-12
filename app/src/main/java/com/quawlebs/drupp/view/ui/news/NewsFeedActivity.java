package com.quawlebs.drupp.view.ui.news;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsFeedActivity extends MainBaseActivity {
    @BindView(R.id.btn_no_thanks)
    Button mBtnNoThanks;
    @BindView(R.id.iv_cancel)
    ImageView mBtnCancel;



    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_home);
        ButterKnife.bind(this);

        if (getIntent() != null) {

            Bundle bundle = new Bundle();
            if (getIntent().hasExtra(AppConstants.K_LAUNCH_TYPE)) {
                if (getIntent().getStringExtra(AppConstants.K_LAUNCH_TYPE).equals(AppConstants.DRAWER)) {

                    bundle.putString(AppConstants.K_LAUNCH_TYPE, AppConstants.DRAWER);
                    mBtnNoThanks.setVisibility(View.GONE);
                } else {
                    bundle.putString(AppConstants.K_LAUNCH_TYPE, AppConstants.DIALOG);
                }
            }

        }

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @OnClick(R.id.btn_access_news)
    public void accessNews() {
        UIHelper.getInstance().switchActivity(this, NewsFeedMainActivity.class, null, null, null, false);
    }

    @OnClick(R.id.btn_no_thanks)
    public void onThanksClick() {
        showAlertDialog(R.layout.dialog_news_shop_popup, AppConstants.NotificationType.NEWS_SHOP_POPUP);
        if (mAlertDialog != null) {
            mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v1 ->
            {
                hideAlertDialog();
                onBackPressed();
            });
        }
    }


}
