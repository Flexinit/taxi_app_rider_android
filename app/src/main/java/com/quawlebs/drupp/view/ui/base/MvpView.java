package com.quawlebs.drupp.view.ui.base;


import android.content.Context;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;

import com.quawlebs.drupp.helpers.AppConstants.NotificationType;

import cn.pedant.SweetAlert.SweetAlertDialog;

public interface MvpView {
    void showLoading();

    void hideLoading();

    void showDialogProgressBar();

    void hideDialogProgressBar();

    void showPopUpWindow(@LayoutRes int resId, View view);

    void hidePopUpWindow();

    void showAlertDialog(@LayoutRes int resId, NotificationType notificationType);
    void showAlertDialog(@LayoutRes int resId);
    void hideAlertDialog();

    void openActivityOnTokenExpire();

    void onError(@StringRes int resId);

    void onError(String message);

    void showMessage(String message);

    void showMessage(@StringRes int resId);

    boolean isNetworkConnected();

    void hideKeyboard();

    void showAnimatedtDialog(Context context, String title, String content, String confirm, String cancelText, SweetAlertDialog.OnSweetClickListener onDialogClickListener, int type);

    void hideAnimatedDialog();
}
