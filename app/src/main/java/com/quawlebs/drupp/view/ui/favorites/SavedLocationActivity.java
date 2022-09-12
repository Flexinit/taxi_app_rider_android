package com.quawlebs.drupp.view.ui.favorites;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SavedLocationActivity extends MainBaseActivity {

    @BindView(R.id.tv_title)
    TextView toolbarTitle;

    @Override
    protected void setUp() {
        toolbarTitle.setText(getString(R.string.saved_locations));

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_locations_layout);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.iv_back)
    public void onBack() {
        onBackPressed();
    }

}
