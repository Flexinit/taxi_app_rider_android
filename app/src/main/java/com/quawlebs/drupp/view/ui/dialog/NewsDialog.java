package com.quawlebs.drupp.view.ui.dialog;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.IRideOnResponseObserver;
import com.quawlebs.drupp.view.ui.news.NewsFeedActivity;
import com.quawlebs.drupp.view.ui.base.DialogBaseFragment;
import com.quawlebs.drupp.view.ui.news.NewsFeedMainActivity;

public class NewsDialog extends DialogBaseFragment {

    private IRideOnResponseObserver iResponseObserver;

    public static NewsDialog newInstance() {
        return new NewsDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStyle(DialogFragment.STYLE_NORMAL,
                    android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        getDialog().getWindow().setAttributes(params);
    }

    public void setiResponseObserver(IRideOnResponseObserver iResponseObserver) {
        this.iResponseObserver = iResponseObserver;
    }

    @Override
    protected void initViewsForFragment(View view) {

    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.dialog_show_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        view.findViewById(R.id.iv_cancel).setOnClickListener(v -> dismissAndShowNextDialog());
        view.findViewById(R.id.btn_no_thanks).setOnClickListener(v -> {
            showAlertDialog(R.layout.dialog_news_shop_popup, AppConstants.NotificationType.NEWS_SHOP_POPUP);
            if (mAlertDialog != null) {
                mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v1 ->
                {
                    hideAlertDialog();
                    dismissAndShowNextDialog();
                });
                mAlertDialog.findViewById(R.id.btn_cancel).setOnClickListener(v1 ->
                {
                    hideAlertDialog();
                    dismissAndShowNextDialog();
                });
            }
        });
        view.findViewById(R.id.btn_access_news).setOnClickListener(v -> {
            dismiss();
            UIHelper.getInstance().switchActivity(getmActivity(), NewsFeedMainActivity.class, null, AppConstants.DIALOG, AppConstants.K_LAUNCH_TYPE, false);
        });
    }

    private void dismissAndShowNextDialog() {
        dismiss();
        if (iResponseObserver == null) return;
        iResponseObserver.onDialogDismiss(AppConstants.DIALOG_NONE);
    }

    @Override
    public void showAlertDialog(int resId) {

    }
}
