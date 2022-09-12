package com.quawlebs.drupp.view.ui.shopping;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShoppingHomeActivity extends MainBaseActivity {
    @BindView(R.id.btn_no_thanks)
    Button btnNoThanks;
    String launchType = "";

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drupp_shop_main);
        ButterKnife.bind(this);

        findViewById(R.id.btn_no_thanks).setOnClickListener(v -> {
            showAlertDialog(R.layout.dialog_news_shop_popup, AppConstants.NotificationType.NEWS_SHOP_POPUP);
            if (mAlertDialog != null) {
                mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v1 ->
                {
                    hideAlertDialog();
                    onBackPressed();
                });
            }
        });
        if (getIntent() != null) {
            if (getIntent().hasExtra(AppConstants.K_LAUNCH_TYPE)) {
                launchType = getIntent().getStringExtra(AppConstants.K_LAUNCH_TYPE);
                if (getIntent().getStringExtra(AppConstants.K_LAUNCH_TYPE).equals(AppConstants.DRAWER)) {

                    btnNoThanks.setVisibility(View.GONE);

                } else {
                    btnNoThanks.setVisibility(View.VISIBLE);
                }
            }

        }

        findViewById(R.id.iv_cancel).setOnClickListener(v -> onBackPressed());
        findViewById(R.id.btn_enter_shopp).setOnClickListener(v -> {
            UIHelper.getInstance().switchActivity(ShoppingHomeActivity.this, ShopMainActivity.class, null, launchType, AppConstants.K_LAUNCH_TYPE, false);
        });
    }


}
