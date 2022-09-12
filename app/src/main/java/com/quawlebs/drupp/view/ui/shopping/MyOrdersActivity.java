package com.quawlebs.drupp.view.ui.shopping;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.view.ui.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyOrdersActivity extends BaseActivity implements FragmentManager.OnBackStackChangedListener {


    @Override
    protected void setUp() {

    }


    @OnClick(R.id.iv_back)
    public void onBack() {
        onBackPressed();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_orders_main_layout);
        setToolbarTitle(R.string.my_orders);
        getToolbarBack().setOnClickListener(v -> onBackPressed());
        getSwipeRefreshLayout().setEnabled(false);
        ButterKnife.bind(this);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_my_orders, new MyOrdersFragment(), MyOrdersFragment.class.getSimpleName())
                .commit();
        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    public void onBackStackChanged() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_my_orders);
        if (fragment instanceof MyOrdersFragment) {
            setToolbarTitle(getString(R.string.my_orders));
        } else if (fragment instanceof OrderDetailsFragment) {
            setToolbarTitle(getString(R.string.order_details));
        }
    }
}
