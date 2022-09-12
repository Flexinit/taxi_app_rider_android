package com.quawlebs.drupp.view.ui.ride;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.IActivityHelper;
import com.quawlebs.drupp.helpers.IFragmentChangeListener;
import com.quawlebs.drupp.helpers.IFragmentEventObserver;
import com.quawlebs.drupp.view.ui.base.MainBaseFragment;

public class RideType extends MainBaseFragment {
    //---------------Globals------------
    private int rideType;
    //---------------Views--------------
    private ImageView ivSingle, ivPool;
    private TextView tvSingle, tvPool;
    private RelativeLayout mPool, mSingle;
    private IFragmentEventObserver iFragmentEventObserver;
    private IFragmentChangeListener iFragmentChangeListener;
    private IActivityHelper iActivityHelper;

    public static RideType newInstance() {

        Bundle args = new Bundle();

        RideType fragment = new RideType();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initViewsForFragment(View view) {
        ivSingle = view.findViewById(R.id.iv_single);
        ivPool = view.findViewById(R.id.iv_pool);
        tvPool = view.findViewById(R.id.tvPool);
        tvSingle = view.findViewById(R.id.tvSingle);
        mPool = view.findViewById(R.id.poolRide);
        mSingle = view.findViewById(R.id.singleRide);


    }

    @Override
    public void onResume() {
        super.onResume();


    }

    public void setiActivityHelper(IActivityHelper iActivityHelper) {
        this.iActivityHelper = iActivityHelper;
    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_ride_type, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rideType = 1;
        if (getmActivity() instanceof RideActivity) {
            iFragmentChangeListener = (RideActivity) getmActivity();
        } else if (getmActivity() instanceof BottomSheetRideActivity) {
            iFragmentChangeListener = (BottomSheetRideActivity) getmActivity();
        }

        iFragmentChangeListener.onFragmentChanged(this);


        mPool.setOnClickListener(v -> {

            /*rideType = 2;
            switchToNext(rideType);*/
            View alertDialogView=getLayoutInflater().inflate(R.layout.alert_pool_rides_suspended,null);
            AlertDialog alertDialog=new AlertDialog.Builder(getActivity()).create();
            alertDialog.setView(alertDialogView);
            alertDialogView.findViewById(R.id.btnOk)
                    .setOnClickListener(v1 -> {
                        alertDialog.dismiss();
                    });
            alertDialog.show();


        });
        mSingle.setOnClickListener(v -> {


            rideType = 1;
            switchToNext(rideType);
        });

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getmActivity() instanceof RideActivity) {
            iFragmentEventObserver = (RideActivity) getmActivity();
        } else if (getmActivity() instanceof BottomSheetRideActivity) {
            iFragmentEventObserver = (BottomSheetRideActivity) getmActivity();
        }

    }

    private void switchToNext(int rideType) {
        switch (iActivityHelper != null ? iActivityHelper.setPickerStatus() : 0) {
            case 1:
                showMessage(R.string.source_cant_be_empty);
                break;
            case 2:
                showMessage(R.string.destination_cant_be_empty);
                break;
            case 3:
                RideVehicleSingle rideVehicleSingle = new RideVehicleSingle();
                RideVehiclePool rideVehiclePool=new RideVehiclePool();
                Bundle bundle = new Bundle();
                bundle.putInt(AppConstants.K_RIDE_TYPE, rideType);
                rideVehicleSingle.setArguments(bundle);
                rideVehiclePool.setArguments(bundle);
                if(rideType==1){
                    getFragmentManager().beginTransaction()
                            .replace(R.id.fl_bottom_sheet_home, rideVehicleSingle)
                            .addToBackStack(AppConstants.RIDE_VEHICLE_BACK_STACK)
                            .commit();
                }
                else{
                    getFragmentManager().beginTransaction()
                            .replace(R.id.fl_bottom_sheet_home, rideVehiclePool)
                            .addToBackStack(AppConstants.RIDE_VEHICLE_BACK_STACK)
                            .commit();
                }

                iFragmentEventObserver.onRideSelected(rideType);
                break;


        }
    }

    @Override
    public void showDialogProgressBar() {

    }

    @Override
    public void hideDialogProgressBar() {

    }

    @Override
    public void showAlertDialog(int resId) {

    }

    public void disableRide() {
        showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
        if (mAlertDialog != null) {
            TextView title = mAlertDialog.findViewById(R.id.tv_title);
            title.setText(getString(R.string.pickup_and_drop_location));

        }
        mSingle.setEnabled(false);
        mPool.setEnabled(false);
    }

    public void enableRide() {
        mSingle.setEnabled(true);
        mPool.setEnabled(true);
    }
}
