package com.quawlebs.drupp.view.ui.scheduledRides;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.tabs.TabLayout;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.IAdapterItemClickListener;
import com.quawlebs.drupp.view.ui.base.MainBaseFragment;

public class BusRidesFragment extends MainBaseFragment implements IAdapterItemClickListener {
    @Override
    protected void initViewsForFragment(View view) {

    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_bus_rides, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager viewPager = view.findViewById(R.id.frame);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

        FragmentAdapter fragmentAdapter = new FragmentAdapter(getFragmentManager());

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(0);


        try {
            tabLayout.getTabAt(0).setText(getString(R.string.upcoming_rides));
            tabLayout.getTabAt(1).setText(getString(R.string.completed_rides));
        } catch (Exception e) {

        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    //  fragmentAdapter.scheduledBusRideFragment.getData();
                } else if (position == 1) {
                    //fragmentAdapter.completedBusRideFragment.getData();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onAdapterItemClick(View v, int position) {

    }

    @Override
    public void onAdapterItemClick(Place place) {

    }

    @Override
    public void showAlertDialog(int resId) {

    }

    private class FragmentAdapter extends FragmentPagerAdapter {
        ScheduledBusRideFragment scheduledBusRideFragment;
        CompletedBusRideFragment completedBusRideFragment;


        FragmentAdapter(FragmentManager fm) {
            super(fm);

            scheduledBusRideFragment = new ScheduledBusRideFragment();
            completedBusRideFragment = new CompletedBusRideFragment();
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return scheduledBusRideFragment;
                case 1:
                    return completedBusRideFragment;
                default:
                    return scheduledBusRideFragment;
            }


        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
