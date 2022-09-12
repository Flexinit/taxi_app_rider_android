package com.quawlebs.drupp.view.ui.scheduledRides;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.util.CommonUtils;
import com.quawlebs.drupp.util.NetworkUtils;
import com.quawlebs.drupp.view.ui.base.MvpView;

import retrofit2.Response;

public abstract class DialogBaseFragment extends DialogFragment implements MvpView {
    private Context mContext;
    private Activity mActivity;
    protected AlertDialog mAlertDialog;
    private ProgressDialog mProgressDialog;
    protected AlertDialog mAlertDialogProgressBar;

    //abstract method
    protected abstract void initViewsForFragment(View view);

    protected abstract View inflateFragmentView(LayoutInflater inflater, ViewGroup container);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflateFragmentView(inflater, container);
        initViewsForFragment(view);
        setupUI(view);
        return view;
    }

    @Override
    public void showDialogProgressBar() {
        if (mAlertDialogProgressBar == null) {
            mAlertDialogProgressBar = CommonUtils.showDialogProgressBar(mContext);
        }
        mAlertDialogProgressBar.show();
    }

    @Override
    public void hideDialogProgressBar() {
        if (mAlertDialogProgressBar != null && mAlertDialogProgressBar.isShowing()) {
            mAlertDialogProgressBar.dismiss();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mContext == null)
            mContext = getActivity();

        if (mActivity == null)
            mActivity = getActivity();
    }

    public Context getmContext() {
        return mContext;
    }

    public Activity getmActivity() {
        return mActivity;
    }

    @Override
    public void showLoading() {
        hideKeyboard();
        mProgressDialog = CommonUtils.showLoadingDialog(getmContext());
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    @Override
    public void showAlertDialog(int resId) {
        hideKeyboard();
        mAlertDialog = CommonUtils.showCustomAlertDialog(getmContext(), resId);
        if (!mAlertDialog.isShowing()) {
            mAlertDialog.show();
        }
    }

    @Override
    public void hideAlertDialog() {
        if (mAlertDialog != null && mAlertDialog.isShowing())
            mAlertDialog.dismiss();
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
        Toast toast = Toast.makeText(getmActivity(), error, Toast.LENGTH_SHORT);
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
        getmActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

//        View view = getmActivity().getCurrentFocus();
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager) getmActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
    }

    public String getErrorFromResponsse(Response response) {
        QualStandardResponse stdResponse = getQualStandardResponse(response);
        String error = stdResponse.getErrorResponse();
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        return error;
    }

    public QualStandardResponse getQualStandardResponse(Response response) {
//        return BaseModelHandler.parseError(response);
        return null;
    }

    public void setupUI(View view) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard();
                    return false;
                }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("message:", "baseFrag");
    }

}
