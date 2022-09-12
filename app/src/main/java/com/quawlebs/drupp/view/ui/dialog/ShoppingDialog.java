package com.quawlebs.drupp.view.ui.dialog;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.IRideOnResponseObserver;
import com.quawlebs.drupp.view.ui.base.DialogBaseFragment;

public class ShoppingDialog extends DialogBaseFragment {
    private Button mBtnNoThanks, mBtnEnterShopp;
    private IRideOnResponseObserver iRideOnResponseObserver;

    public static ShoppingDialog newInstance() {
        return new ShoppingDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStyle(DialogFragment.STYLE_NORMAL,
                    android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        }
    }

    public void setiRideOnResponseObserver(IRideOnResponseObserver iRideOnResponseObserver) {
        this.iRideOnResponseObserver = iRideOnResponseObserver;
    }

    @Override
    protected void initViewsForFragment(View view) {
        mBtnEnterShopp = view.findViewById(R.id.btn_enter_shopp);
        mBtnNoThanks = view.findViewById(R.id.btn_no_thanks);
    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.activity_drupp_shop_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        btnImageBack.setVisibility(View.GONE);
        //      btnNotification.setVisibility(View.GONE);

        view.findViewById(R.id.iv_cancel).setOnClickListener(v -> dismissAndShowNextDialog());
        mBtnEnterShopp.setOnClickListener(v -> {
            dismiss();
            showMessage(R.string.coming_soon);
        });

        mBtnNoThanks.setOnClickListener(v -> {
            showAlertDialog(R.layout.dialog_news_shop_popup, AppConstants.NotificationType.NEWS_SHOP_POPUP);
            if (mAlertDialog != null) {
                mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v1 -> {
                    hideAlertDialog();
                    dismissAndShowNextDialog();
                });
                mAlertDialog.findViewById(R.id.btn_cancel).setOnClickListener(v1 -> {
                    hideAlertDialog();
                    dismissAndShowNextDialog();
                });
            }
        });

    }

    private void dismissAndShowNextDialog() {
        dismiss();
        iRideOnResponseObserver.onDialogDismiss(AppConstants.DIALOG_NONE);
    }

    @Override
    public void showAlertDialog(int resId) {

    }
}
