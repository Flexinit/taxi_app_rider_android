package com.quawlebs.drupp.view.ui.scheduledRides;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.models.TripHistoryModel;
import com.quawlebs.drupp.view.adapters.MyRvAdapter;
import com.quawlebs.drupp.view.ui.PostRideActivity;

import java.util.ArrayList;

public class ScheduledRidesActivity extends AppCompatActivity {

    private static final String TAG = "TripHistory";
    private ArrayList<TripHistoryModel> tripList;
    private RecyclerView tripRv;
    private MyRvAdapter rvAdapter;
    public FragmentManager fragmentManager;
    private FloatingActionButton fabScheduleRide;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduled_rides);
        findViewById(R.id.image_back).setOnClickListener(v -> onBackPressed());
        ViewPager viewPager = findViewById(R.id.frame);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        fabScheduleRide=findViewById(R.id.fab_schedule_ride);

        FragmentAdapter fragmentAdapter =
                new FragmentAdapter(getSupportFragmentManager());
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.frame, new UserPostedRideLater(),
                        UserPostedRideLater.class.getSimpleName()).commit();

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(0);

        try {
            tabLayout.getTabAt(0).setText(getString(R.string.your_rides));
            tabLayout.getTabAt(1).setText(getString(R.string.driver_posted_rides));
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    fragmentAdapter.userPostedRideLater.getData();
                } else if (position == 1) {
                    fragmentAdapter.driverPostedRides.getData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        fabScheduleRide.setOnClickListener(v-> {
            startActivity(new Intent(ScheduledRidesActivity.this, PostRideActivity.class));
        });
    }


    private class FragmentAdapter extends FragmentPagerAdapter {
        UserPostedRideLater userPostedRideLater;
        DriverPostedRides driverPostedRides;

        FragmentAdapter(FragmentManager fm) {
            super(fm);
            userPostedRideLater = new UserPostedRideLater();
            driverPostedRides = new DriverPostedRides();
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 1)
                return driverPostedRides;
            else
                return userPostedRideLater;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
