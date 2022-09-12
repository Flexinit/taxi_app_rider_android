package com.quawlebs.drupp.view.ui.scheduledRides;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.places.api.model.Place;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.IAdapterItemClickListener;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.login.StartUpActivity;
import com.quawlebs.drupp.models.BusInfoModel;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.view.adapters.BusRidesAdapter;
import com.quawlebs.drupp.view.ui.base.MainBaseFragment;

import java.util.ArrayList;

import retrofit2.Response;

public class ScheduledBusRideFragment extends MainBaseFragment implements IAdapterItemClickListener {
    private BusRidesAdapter busRidesAdapter;
    private RecyclerView busRidesRecyclerView;
    private ArrayList<BusInfoModel> busInfoModels = new ArrayList<>();

    @Override
    protected void initViewsForFragment(View view) {

    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_scheduled_bus_ride, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        busRidesRecyclerView = view.findViewById(R.id.rv_scheduled_rides);
        busRidesRecyclerView.setLayoutManager(new LinearLayoutManager(getmContext()));

        busRidesAdapter = new BusRidesAdapter(getmContext(), R.layout.layout_trip_history_item, busInfoModels);
        busRidesAdapter.setiAdapterItemClickListener(this);
        busRidesRecyclerView.setAdapter(busRidesAdapter);

        getBookedBusRides();
    }

    private void getBookedBusRides() {
        showDialogProgressBar();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<BusInfoModel>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<BusInfoModel>> response) {
                hideDialogProgressBar();
                busInfoModels.clear();
                if (response.isSuccessful() && response.body() != null) {
                    busInfoModels.addAll(response.body().getResponse());
                }
                busRidesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<BusInfoModel>> response) {
                hideDialogProgressBar();
                if (response.code() == 401) {
                    showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
                    if (mAlertDialog != null) {
                        mAlertDialog.setCancelable(false);
                        TextView title = mAlertDialog.findViewById(R.id.tv_title);
                        title.setText(getString(R.string.your_session_has_expired));
                        Button btnOk = mAlertDialog.findViewById(R.id.btn_cancel);
                        btnOk.setText(getString(R.string.login));
                        btnOk.setOnClickListener(v -> {
                            SessionManager.getInstance().removeUserData(getmActivity());
                            UIHelper.getInstance().switchActivity(getmActivity(), StartUpActivity.class, null, null, null, true);
                        });
                    }
                    return;

                }
                showErrorPrompt(response);
            }

            @Override
            public void onNullListResponse() {
                hideDialogProgressBar();
            }

            @Override
            public void onFailureList(Throwable t) {
                hideDialogProgressBar();
                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.APP_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        getBookedBusRides();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getBookedBusRides();
    }

    @Override
    public void onAdapterItemClick(View v, int position) {
        try {
            Bundle bundle = new Bundle();
            bundle.putLong(AppConstants.K_BUS_DEPARTURE_TIME, Long.valueOf(busInfoModels.get(position).getStartTime()));
            bundle.putString(AppConstants.K_SOURCE, busInfoModels.get(position).getSource());
            bundle.putString(AppConstants.K_DEST, busInfoModels.get(position).getDestination());
            bundle.putInt(AppConstants.K_NO_OF_SEATS, busInfoModels.get(position).getNoOfSeats());
            bundle.putInt(AppConstants.K_BUS_RIDE_ID, busInfoModels.get(position).getId());
            bundle.putInt(AppConstants.K_RIDE_TYPE, AppConstants.BUS_RIDE_SCHEDULED);

            UIHelper.getInstance().switchFragment((AppCompatActivity) getmActivity(), R.id.fl_bus_rides, new SingleBusRideInfoFragment(), SingleBusRideInfoFragment.class.getSimpleName(), bundle, false);

        } catch (Exception e) {
            Log.d(getClass().getSimpleName(), "Error");
        }
    }

    @Override
    public void onAdapterItemClick(Place place) {

    }

    @Override
    public void showAlertDialog(int resId) {

    }
}
