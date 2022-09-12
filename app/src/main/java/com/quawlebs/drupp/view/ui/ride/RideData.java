package com.quawlebs.drupp.view.ui.ride;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.quawlebs.drupp.models.CalculatedFareModel;
import com.quawlebs.drupp.models.PaymentMethod;
import com.quawlebs.drupp.models.VehicleTypeModel;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.RxBus;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.Helper;
import com.quawlebs.drupp.helpers.IActivityHelper;
import com.quawlebs.drupp.helpers.IFragmentChangeListener;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.view.ui.base.MainBaseFragment;
import com.quawlebs.drupp.view.ui.dialog.PaymentMethodDialog;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;


public class RideData extends MainBaseFragment {
    // private IGetData iGetData;
    private TextView text;
    private Button done;
    private TextView mCapacity, mBaseFare, mPerMinuteWaitTime;
    private VehicleTypeModel vehicleTypeModel;
    private IActivityHelper iActivityHelper;
    private CountDownTimer countDownTimer;
    private IFragmentChangeListener iFragmentChangeListener;
    private Disposable paymentMethodDisposable;
    private TextView paymentMethod;
    private ImageView paymentLogo;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    public static RideData newInstance(Bundle bundle) {
        RideData rideData = new RideData();
        rideData.setArguments(bundle);
        return rideData;
    }

    @Override
    protected void initViewsForFragment(View view) {
        done = view.findViewById(R.id.bt_confirm_go2);
        mCapacity = view.findViewById(R.id.tv_vehicle_capacity);
        mBaseFare = view.findViewById(R.id.tv_fare);
        paymentLogo = view.findViewById(R.id.iv_payment_method);
        paymentMethod = view.findViewById(R.id.tv_payment_method);
        mPerMinuteWaitTime = view.findViewById(R.id.tv_per_minute_wait_time);
    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_ride_data, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getmActivity() instanceof RideActivity) {
            iActivityHelper = (RideActivity) getmActivity();
            iFragmentChangeListener = (RideActivity) getmActivity();
        } else if (getmActivity() instanceof BottomSheetRideActivity) {
            iFragmentChangeListener = (BottomSheetRideActivity) getmActivity();
            iActivityHelper = (BottomSheetRideActivity) getmActivity();
        }


        iFragmentChangeListener.onFragmentChanged(this);

        //  iGetData = (IGetData) getActivity();
        vehicleTypeModel = (VehicleTypeModel) (getArguments() != null ? getArguments().getSerializable(AppConstants.K_RIDE_TYPE_DATA) : null);

        if (vehicleTypeModel != null) {
            mCapacity.setText(String.valueOf(vehicleTypeModel.getCapacity()));
            mBaseFare.setText(getString(R.string.fare_in_naira, vehicleTypeModel.getBaseFare()));
            mPerMinuteWaitTime.setText(getString(R.string.fare_in_naira, vehicleTypeModel.getPerMinuteWaitCharge()));
        }

        ObjectAnimator colorAnim = ObjectAnimator.ofInt(done, AppConstants.P_TEXT_COLOR, getResources().getColor(R.color.colorWhite), Color.TRANSPARENT);
        colorAnim.setDuration(1000);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setRepeatCount(ValueAnimator.INFINITE);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                int type = Helper.getNotificationReceived(getmActivity());
                if (type != 2) {
                    postRideDelayAction();
                    //Show a Pop Up
                    Helper.removeNotification(getmActivity());
                }
            }
        };


        done.setOnClickListener(v -> {
            done.setText(getString(R.string.searching_for_your_ride));
            done.setClickable(false);

            if (iActivityHelper.setPickerStatus() == 3) {
                colorAnim.start();
                try {
                    iActivityHelper.post();
                    countDownTimer.start();
                } catch (NullPointerException e) {
                    showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
                }


            }

        });

        text = view.findViewById(R.id.tvMini);

        try {
            if (isAdded()) {
                HashMap<String, Object> parms = iActivityHelper.getData();
                switch (parms.get(AppConstants.K_VEHICLE).toString()) {
                    case "1":
                        text.setText(getString(R.string.without_ac));
                        break;
                    case "2":
                        text.setText(getString(R.string.with_ac));
                        break;
                    case "3":
                        text.setText(getString(R.string.bus));
                        break;
                }
                getCalculatedFare();
            }

        } catch (Exception e) {
            showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
        }

        PaymentMethod paymentMethod = Helper.getPaymentMethod(getContext());
        paymentMethodSelection(paymentMethod);

        paymentMethodDisposable = RxBus.getInstance().getIntentEvent().subscribe(o -> {
            if (o instanceof PaymentMethod) {
                paymentMethodSelection((PaymentMethod) o);
            }
        });

    }

    public void paymentMethodSelection(PaymentMethod paymentMethod) {
        switch (paymentMethod.getType()) {
            case AppConstants.PAYMENT_METHOD.WALLET:

                paymentLogo.setImageDrawable(getResources().getDrawable(R.drawable.ic_wallet_payment));
                this.paymentMethod.setText(getString(R.string.wallet));
                break;
            case AppConstants.PAYMENT_METHOD.CARD:
                this.paymentMethod.setText(paymentMethod.getMethod());
                paymentLogo.setImageDrawable(paymentMethod.getImage());
                break;
            case AppConstants.PAYMENT_METHOD.CASH:
                chooseCash();
                break;
        }
    }

    private void chooseCash() {
        paymentMethod.setText(getString(R.string.cash));
        paymentLogo.setImageDrawable(getResources().getDrawable(R.drawable.ic_cash));
        //     RxBus.getInstance().setIntentEvent(new PaymentMethod(2, getString(R.string.cash), AppConstants.PAYMENT_METHOD.CASH));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        paymentMethodDisposable.dispose();
    }

    @OnClick(R.id.container_payment_method)
    public void onPaymentMethodSelection() {
        PaymentMethodDialog paymentMethodDialog = new PaymentMethodDialog();
        paymentMethodDialog.show(getChildFragmentManager(), PaymentMethodDialog.class.getSimpleName());
    }

    private void postRideDelayAction() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(AppConstants.K_RIDE_ID, Helper.getRideId(getmActivity()));
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<String>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    getmActivity().onBackPressed();
                    iActivityHelper.onNoDriverFound();
                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<String>> response) {
                hideLoading();
                showErrorPrompt(response);
            }

            @Override
            public void onNullListResponse() {
                hideLoading();
            }

            @Override
            public void onFailureList(Throwable t) {
                hideLoading();
                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        postRideDelayAction();
                    });
                }
            }
        }, SessionManager.getAccessToken()).postRideDelayAction(hashMap);
    }

    private void getCalculatedFare() throws NullPointerException {
        HashMap<String, String> hashMap = new HashMap<>();
        LatLng source = iActivityHelper.getSource();
        LatLng dest = iActivityHelper.getDestination();
        hashMap.put(AppConstants.Q_SOURCE_LAT, String.valueOf(source.latitude));
        hashMap.put(AppConstants.Q_SOURCE_LONG, String.valueOf(source.longitude));
        hashMap.put(AppConstants.Q_DEST_LAT, String.valueOf(dest.latitude));
        hashMap.put(AppConstants.Q_DEST_LONG, String.valueOf(dest.longitude));
        hashMap.put(AppConstants.Q_VEHICLE_TYPE, iActivityHelper.getData().get(AppConstants.K_VEHICLE).toString());

        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<CalculatedFareModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<CalculatedFareModel>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    mBaseFare.setText(getString(R.string.fare_in_naira, String.valueOf(response.body().getResponse().getTotalFare())));
                    Helper.saveTotalFare(String.valueOf(response.body().getResponse().getTotalFare()), getmActivity());
                }
            }

            @Override
            public void onError(Response<QualStandardResponse<CalculatedFareModel>> response) {
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
                        getCalculatedFare();
                    });
                }

            }
        }, SessionManager.getAccessToken()).getCalculatedFare(hashMap);
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
}
