package com.quawlebs.drupp.view.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.view.ui.base.MainBaseFragment;

import butterknife.ButterKnife;

public class FragmentNewsHome extends MainBaseFragment {


    public static FragmentNewsHome newInstance(Bundle args) {
        FragmentNewsHome fragment = new FragmentNewsHome();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViewsForFragment(View view) {
        if (getArguments() != null) {
            if (getArguments().getString(AppConstants.K_LAUNCH_TYPE).equals(AppConstants.DRAWER)) {

            }
        }

    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.activity_news_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void showAlertDialog(int resId) {

    }
}
