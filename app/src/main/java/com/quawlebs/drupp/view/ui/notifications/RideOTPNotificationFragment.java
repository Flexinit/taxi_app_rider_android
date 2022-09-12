package com.quawlebs.drupp.view.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quawlebs.drupp.models.SingleNotificationModel;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.view.ui.base.MainBaseFragment;

import butterknife.ButterKnife;
import retrofit2.Response;

public class RideOTPNotificationFragment extends MainBaseFragment {
    public static RideOTPNotificationFragment newInstance(int id) {

        Bundle args = new Bundle();
        args.putInt(AppConstants.K_ID, id);
        RideOTPNotificationFragment fragment = new RideOTPNotificationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViewsForFragment(View view) {
        if (getArguments() != null) {
        }

    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_ride_otp, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    private void getSingleNotification(Integer id) {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<SingleNotificationModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<SingleNotificationModel>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    try {


                    } catch (Exception exception) {

                    }

                }
            }

            @Override
            public void onError(Response<QualStandardResponse<SingleNotificationModel>> response) {
                hideLoading();
                showErrorPrompt(response);
            }

            @Override
            public void onNullResponse() {
                hideLoading();
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoading();
                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        getSingleNotification(id);
                    });
                }
            }
        }, SessionManager.getAccessToken()).getSingleNotification(id);
    }

    @Override
    public void showAlertDialog(int resId) {

    }
}
