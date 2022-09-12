package com.quawlebs.drupp.view.ui.busbooking;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.IDialogResponseObserver;
import com.quawlebs.drupp.view.ui.BusChooserActivity;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;

import butterknife.ButterKnife;

public class BusBookActivity extends MainBaseActivity implements IDialogResponseObserver {

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_bus_splash_layout);
        ButterKnife.bind(this);
        ImageView cancelBtn = findViewById(R.id.iv_cancel);
        cancelBtn.setOnClickListener(v -> onBackPressed());
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

        findViewById(R.id.btn_enter_shopp).setOnClickListener(v -> {
            UIHelper.getInstance().switchActivity(this, BusChooserActivity.class, null, null, null, true);

        });
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onDimiss() {
        onBackPressed();
    }
}
