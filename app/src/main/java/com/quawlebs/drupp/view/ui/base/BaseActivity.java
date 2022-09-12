package com.quawlebs.drupp.view.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.quawlebs.drupp.models.LoginDataModel;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.CommonUtils;
import com.quawlebs.drupp.util.ProgressWheel;
import com.quawlebs.drupp.helpers.SessionManager;

public abstract class BaseActivity extends MainBaseActivity implements MvpView {
    protected LoginDataModel userInfoModel;
    private TextView toolbarTitle;
    private ImageButton toolbarBack;
    private ProgressWheel mProgressWheel;
    private AlertDialog mAlertDialogProgressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    protected Snackbar mSnackbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfoModel = SessionManager.getInstance().loadUser(this);
    }

    @Override
    public void setContentView(int layoutResID) {

        View inflateView = LayoutInflater.from(this).inflate(R.layout.activity_base, null);

        mProgressWheel = inflateView.findViewById(R.id.progress_wheel);
        swipeRefreshLayout = inflateView.findViewById(R.id.swipe_refresh);
        // Configure the refreshing colors
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,

                android.R.color.holo_green_light,

                android.R.color.holo_orange_light,

                android.R.color.holo_red_light);


        toolbarBack = inflateView.findViewById(R.id.iv_back);
        toolbarTitle = inflateView.findViewById(R.id.tv_title);

        toolbarBack.setOnClickListener(v -> onBackPressed());

        FrameLayout mainContainer = inflateView.findViewById(R.id.fl_main_container);
        LayoutInflater.from(this).inflate(layoutResID, mainContainer, true);
        super.setContentView(inflateView);

    }


    @Override
    public void showLoading() {
        if (mProgressWheel.getVisibility() == View.GONE) {
            mProgressWheel.setVisibility(View.VISIBLE);
            mProgressWheel.spin();
        }
    }

    @Override
    public void hideLoading() {
        if (mProgressWheel.getVisibility() == View.VISIBLE) {
            mProgressWheel.setVisibility(View.GONE);
            mProgressWheel.stopSpinning();
        }
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    protected void setToolbarTitle(@StringRes int title) {
        toolbarTitle.setText(getString(title));
    }

    protected void setToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }

    protected ImageButton getToolbarBack() {
        return toolbarBack;
    }


    public void showSnackBarMessage(View view, String message) {
        hideKeyboard();
        mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        mSnackbar.show();
    }

    @Override
    public void showDialogProgressBar() {

        hideKeyboard();
        mAlertDialogProgressBar = CommonUtils.showDialogProgressBar(this);
        mAlertDialogProgressBar.show();
    }

    @Override
    public void hideDialogProgressBar() {
        if (mAlertDialogProgressBar != null && mAlertDialogProgressBar.isShowing()) {
            mAlertDialogProgressBar.dismiss();
        }
    }


    @Override
    public void hideAlertDialog() {

    }

    @Override
    public void openActivityOnTokenExpire() {

    }

    @Override
    public void onError(int resId) {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void showMessage(String message) {
        if (message != null) {
            showErrorPrompt(message);
        } else {
            showErrorPrompt(getString(R.string.some_error));
        }
    }

    public void showErrorPrompt(String error) {
        Toast toast = Toast.makeText(this, error, Toast.LENGTH_LONG);
        if (!toast.getView().isShown()) {
            toast.show();
        }
    }

    @Override
    public void showMessage(int resId) {

    }

    @Override
    public boolean isNetworkConnected() {
        return false;
    }

    @Override
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public void hideAnimatedDialog() {

    }
}
