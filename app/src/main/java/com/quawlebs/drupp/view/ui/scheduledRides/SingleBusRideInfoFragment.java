package com.quawlebs.drupp.view.ui.scheduledRides;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.helpers.Timing;
import com.quawlebs.drupp.view.ui.base.MainBaseFragment;

import java.util.HashMap;

import retrofit2.Response;

public class SingleBusRideInfoFragment extends MainBaseFragment {
    private TextView mDepartureTime, mSource, mDestination, mNoOfSeats;
    private Button cancelBusBooking;
    int busRideId = 0;

    @Override
    protected void initViewsForFragment(View view) {

    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_single_bus_ride_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isAdded()) {
            TextView toolbarTitle = getmActivity().findViewById(R.id.tv_title_toolbar);
            toolbarTitle.setText(getString(R.string.ride_details));
        }
        mDepartureTime = view.findViewById(R.id.tv_departure_time);
        mSource = view.findViewById(R.id.tv_source);
        mDestination = view.findViewById(R.id.tv_destination);
        mNoOfSeats = view.findViewById(R.id.tv_no_of_seats);
        cancelBusBooking = view.findViewById(R.id.btn_cancel_booking);


        if (getArguments() != null) {
            try {
                if (getArguments().getInt(AppConstants.K_RIDE_TYPE) == AppConstants.BUS_RIDE_COMPLETED) {
                    cancelBusBooking.setVisibility(View.GONE);
                } else {
                    cancelBusBooking.setVisibility(View.VISIBLE);
                }
                long departureTime = getArguments().getLong(AppConstants.K_BUS_DEPARTURE_TIME);
                String source = getArguments().getString(AppConstants.K_SOURCE);
                String dest = getArguments().getString(AppConstants.K_DEST);
                int noOfSeat = getArguments().getInt(AppConstants.K_NO_OF_SEATS);
                busRideId = getArguments().getInt(AppConstants.K_BUS_RIDE_ID);

                mDepartureTime.setText(Timing.getTimeInString(departureTime, Timing.TimeFormats.HH_12));
                mSource.setText(source);
                mDestination.setText(dest);
                mNoOfSeats.setText(String.valueOf(noOfSeat));

            } catch (Exception ignored) {

            }

        }
        cancelBusBooking.setOnClickListener(v -> {
            showAlertDialog(R.layout.dialog_cancel_booking, AppConstants.NotificationType.PAYMENT_SUCCESS);
            if (mAlertDialog != null) {
                mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v1 -> cancelBusBooking(busRideId));
            }

        });
    }

    private void cancelBusBooking(int busRideId) {
        hideAlertDialog();
        showDialogProgressBar();
        HashMap<String, String> param = new HashMap<>();
        param.put(AppConstants.K_BUS_RIDE_ID, String.valueOf(busRideId));
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<String>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<String>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    showMessage(R.string.booking_cancelled);
                    getmActivity().onBackPressed();
                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<String>> response) {
                hideDialogProgressBar();
                showErrorPrompt(response);
            }

            @Override
            public void onNullListResponse() {
                hideDialogProgressBar();

            }

            @Override
            public void onFailureList(Throwable t) {
                hideDialogProgressBar();
                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        cancelBusBooking(busRideId);
                    });
                }
            }
        }, SessionManager.getAccessToken()).cancelBusBooking(param);
    }

    @Override
    public void showAlertDialog(int resId) {

    }
}
