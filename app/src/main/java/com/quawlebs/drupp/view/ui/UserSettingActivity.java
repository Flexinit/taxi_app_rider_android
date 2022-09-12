package com.quawlebs.drupp.view.ui;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.helpers.AppConstants;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserSettingActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    SwitchCompat news, shopping;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private SwitchCompat newsCompat, shopCompat;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        ButterKnife.bind(this);

        shopping = findViewById(R.id.switch_shopping);
        news = findViewById(R.id.switch_news);

        sharedPreferences = getSharedPreferences("popup", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        shopping.setChecked(sharedPreferences.getBoolean(AppConstants.K_SHOPPING, false));
        news.setChecked(sharedPreferences.getBoolean(AppConstants.K_NEWS, false));

        shopping.setOnCheckedChangeListener(this);
        news.setOnCheckedChangeListener(this);
    }

    @OnClick(R.id.container_privacy_policy)
    public void onPressPrivacyPolicy() {
        UIHelper.getInstance().switchActivity(this, PrivacyPolicyActivity.class, null, null, null, false);
    }

    @OnClick(R.id.container_terms_and_condition)
    public void onPressTermsAndCondition() {
        UIHelper.getInstance().switchActivity(this, TermsConditionActivity.class, null, null, null, false);
    }

    @OnClick(R.id.image_back)
    public void onBackPress() {
        onBackPressed();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @OnClick(R.id.emergency)
    public void onEmergencyPress() {
        emergencyAlert();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void emergencyAlert() {
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_emergancy, null);
        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(UserSettingActivity.this);
        final AlertDialog alertDialog = alertDialogbuilder.create();
        alertDialog.setView(alertLayout);
        alertDialog.show();
        alertDialog.findViewById(R.id.btOk).setOnClickListener(v -> alertDialog.dismiss());
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switch_shopping:
                if (buttonView.isPressed()) {
                    editor.putBoolean(AppConstants.K_SHOPPING, isChecked);
                    editor.commit();
                }
                break;
            case R.id.switch_news:
                if (buttonView.isPressed()) {
                    editor.putBoolean(AppConstants.K_NEWS, isChecked);
                    editor.commit();
                }
                break;
        }

    }
}
