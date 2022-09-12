package com.quawlebs.drupp.view.ui.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.CommonUtils;
import com.quawlebs.drupp.util.NetworkUtils;
import com.quawlebs.drupp.connectivity.BaseModelHandler;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.helpers.AppConstants;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Response;

public abstract class DialogBaseFragment extends DialogFragment implements MvpView {
    private Context mContext;
    private Activity mActivity;
    private ProgressDialog mProgressDialog;
    protected AlertDialog mAlertDialog;
    protected AlertDialog mAlertDialogProgressBar;

    //abstract method
    protected abstract void initViewsForFragment(View view);

    protected abstract View inflateFragmentView(LayoutInflater inflater, ViewGroup container);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflateFragmentView(inflater, container);
//        initViewsForFragment(view);
        setupUI(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewsForFragment(view);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO : Change Implementation
        if (mContext == null)
            mContext = getActivity();

        if (mActivity == null)
            mActivity = getActivity();
    }

    @Override
    public void showAnimatedtDialog(Context context, String title, String content, String confirm, String cancelText, SweetAlertDialog.OnSweetClickListener onDialogClickListener, int type) {

    }

    @Override
    public void hideAnimatedDialog() {

    }

    public Context getmContext() {
        return mContext;
    }

    public Activity getmActivity() {
        return mActivity;
    }

    @Override
    public void showPopUpWindow(int resId, View view) {

    }

    @Override
    public void hidePopUpWindow() {

    }

    @Override
    public void showAlertDialog(int resId, AppConstants.NotificationType notificationType) {
        hideKeyboard();
        mAlertDialog = CommonUtils.showCustomAlertDialog(mActivity, resId, notificationType);
        mAlertDialog.show();
    }

    @Override
    public void hideAlertDialog() {
        if (mAlertDialog != null && mAlertDialog.isShowing())
            mAlertDialog.dismiss();
    }

    public void showDialogProgressBar() {
        hideKeyboard();
        mAlertDialogProgressBar = CommonUtils.showDialogProgressBar(getmContext());
        mAlertDialogProgressBar.show();
    }

    public void hideDialogProgressBar() {
        if (mAlertDialogProgressBar != null && mAlertDialogProgressBar.isShowing()) {
            mAlertDialogProgressBar.dismiss();
        }
    }

    @Override
    public void showLoading() {
        hideKeyboard();
        mProgressDialog = CommonUtils.showLoadingDialog(getmContext());
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void openActivityOnTokenExpire() {

    }

    @Override
    public void onError(int resId) {
        showMessage(resId);
    }

    @Override
    public void onError(String message) {
        showMessage(message);
    }

    @Override
    public void showMessage(String message) {
        if (message != null) {
            showErrorPrompt(message);
        } else {
            showErrorPrompt(getString(R.string.some_error));
        }
    }

    @Override
    public void showMessage(int resId) {
        showMessage(getString(resId));
    }

    public void showErrorPrompt(int error) {
        showErrorPrompt(getString(error));
    }

    public void showErrorPrompt(String error) {
        Toast toast = Toast.makeText(getmActivity(), error, Toast.LENGTH_LONG);
        if (!toast.getView().isShown()) {
            toast.show();
        }
    }


    public boolean showErrorPrompt(Response response) {
        try {
            String error = getErrorFromResponsse(response);
            this.showErrorPrompt(error);
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(getmContext());
    }

    @Override
    public void hideKeyboard() {

        View view = getDialog().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getmActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private String getErrorFromResponsse(Response response) {
        QualStandardResponse stdResponse = getStandardResponse(response);
        String error = stdResponse.getErrorResponse();
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        return error;
    }

    public QualStandardResponse getStandardResponse(Response response) {
        return BaseModelHandler.parseError(response);
    }

    public void setupUI(View view) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                hideKeyboard();
                return false;
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
}
