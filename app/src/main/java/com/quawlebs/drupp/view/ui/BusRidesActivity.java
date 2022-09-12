package com.quawlebs.drupp.view.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;
import com.quawlebs.drupp.view.ui.scheduledRides.BusRidesFragment;

public class BusRidesActivity extends MainBaseActivity {

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_ride);

        //Set UP Toolbar
        TextView toolbarTile = findViewById(R.id.tv_title_toolbar);
        toolbarTile.setText(getString(R.string.bus_rides));

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_bus_rides, new BusRidesFragment(), BusRidesFragment.class.getSimpleName())
                .commit();
        findViewById(R.id.image_back).setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
//        Fragment fragment = getSupportFragmentManager().findFragmentByTag()
        super.onBackPressed();
    }
}
