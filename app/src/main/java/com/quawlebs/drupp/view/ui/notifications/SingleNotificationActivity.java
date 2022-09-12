package com.quawlebs.drupp.view.ui.notifications;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;

public class SingleNotificationActivity extends MainBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_notification);
        if (getIntent().getExtras() != null) {
            int type = getIntent().getIntExtra(AppConstants.K_TYPE, 0);
            int id = getIntent().getIntExtra(AppConstants.K_ID, 0);
            Fragment currentFragment;
            switch (type) {
                case AppConstants.NOTIFICATION_TYPE.RIDE_OTP:
                    currentFragment = RideOTPNotificationFragment.newInstance(id);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + type);
            }
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_single_notification, currentFragment, currentFragment.getClass().getSimpleName())
                    .commit();
        }
    }

    @Override
    protected void setUp() {

    }
}
