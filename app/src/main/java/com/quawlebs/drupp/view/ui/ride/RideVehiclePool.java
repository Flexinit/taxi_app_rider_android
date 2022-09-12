package com.quawlebs.drupp.view.ui.ride;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.quawlebs.drupp.login.StartUpActivity;
import com.quawlebs.drupp.models.PaymentMethod;
import com.quawlebs.drupp.models.VehicleTypeModel;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.util.RxBus;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.Helper;
import com.quawlebs.drupp.helpers.IActivityHelper;
import com.quawlebs.drupp.helpers.IDialogResponseObserver;
import com.quawlebs.drupp.helpers.IFragmentChangeListener;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.view.ui.base.MainBaseFragment;
import com.quawlebs.drupp.view.ui.dialog.PaymentMethodDialog;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;


public class RideVehiclePool extends MainBaseFragment implements IDialogResponseObserver {
    private LinearLayout micro, mini, keke;
    private Button mRideNow, mRideLater;
    private RadioGroup groupPoolOption;
    private Integer vehicleType = 1;
    private LinearLayout rideTypeSelector;
    private ImageView microVehicle, miniVehicle, kekeVehicle;
    //   private TextView withouAC, withAc, bus;
    private IActivityHelper iActivityHelper;
    private boolean isRideLater;
    private CountDownTimer countDownTimer;
    private EditText mSource, mDestination;
    private IFragmentChangeListener iFragmentChangeListener;
    private ArrayList<VehicleTypeModel> vehicleTypeModelArrayLis = new ArrayList<>();
    private Button rideTypeBtn;
    private ListPopupWindow listPopupWindow;
    private Button bookRide;
    //private
    private Animation slideUp;
    private TextView paymentMethod, withoutACFare, withACFare, kekeFare, perKMFare;
    private ImageView paymentLogo;
    private Disposable paymentMethodDisposable;
    private int passengerPref;

    @Override
    protected void initViewsForFragment(View view) {

        rideTypeSelector = view.findViewById(R.id.container_bottom);
        micro = view.findViewById(R.id.micro_vehicle);
        mini = view.findViewById(R.id.mini_vehicle);
        keke = view.findViewById(R.id.keke_vehicle);

        microVehicle = view.findViewById(R.id.image_car1);
        miniVehicle = view.findViewById(R.id.image_car2);
        kekeVehicle = view.findViewById(R.id.image_car3);

        mRideNow = view.findViewById(R.id.btRideNow);
        mSource = view.findViewById(R.id.tv_source);
        mDestination = view.findViewById(R.id.et_destination);
        mRideLater = view.findViewById(R.id.btRideLater);
        //    withAc = view.findViewById(R.id.tv_with_ac_rpk);
        bookRide = view.findViewById(R.id.btn_book_ride);
        rideTypeBtn = view.findViewById(R.id.btn_ride_type);
        //  withouAC = view.findViewById(R.id.tv_withou_ac_rpk);
        withoutACFare = view.findViewById(R.id.tv_without_ac_fare);
        withACFare = view.findViewById(R.id.tv_with_ac_fare);
        kekeFare = view.findViewById(R.id.tv_bus_fare);
        perKMFare = view.findViewById(R.id.tv_per_km_fare);
        //  bus = view.findViewById(R.id.tv_bus_rpk);
        paymentLogo = view.findViewById(R.id.iv_payment_method);
        paymentMethod = view.findViewById(R.id.tv_payment_method);

        groupPoolOption = view.findViewById(R.id.groupPoolOption);


    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_ride_vehicle, container, false);
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
            iFragmentChangeListener = (RideActivity) getmActivity();
        } else if (getmActivity() instanceof BottomSheetRideActivity) {
            iActivityHelper = (IActivityHelper) getmActivity();

            iFragmentChangeListener = (BottomSheetRideActivity) getmActivity();
        }
        iFragmentChangeListener.onFragmentChanged(this);


        int rideType = getArguments() != null ? getArguments().getInt(AppConstants.K_RIDE_TYPE) : 1;
        if (rideType != 1) {
            keke.setVisibility(View.GONE);

        }
        slideUp = AnimationUtils.loadAnimation(getmActivity(), R.anim.slide_up);
        slideUp.setAnimationListener(perKmAnimationListener);

        try {
            mSource.setText(iActivityHelper.getData().get(AppConstants.K_SOURCE).toString());
            mDestination.setText(iActivityHelper.getData().get(AppConstants.K_DEST).toString());
        } catch (Exception e) {

        }

        if (rideType == 1) {
            iActivityHelper.changeType(1, 0, 1);
            iActivityHelper.changeVehicleType(1);
        }

        microVehicle.setSelected(true);
        //        perKMFare.startAnimation(slideUp);

        //Check Pool Option
        groupPoolOption.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_pool_with_one:
                    passengerPref = 1;
                    break;
                case R.id.rb_pool_with_two:
                    passengerPref = 2;
                    break;
            }
        });
        micro.setOnClickListener(v -> {
            vehicleType = 1;
            //  rideTypeSelector.setVisibility(View.VISIBLE);
            selectVehicle(micro);

            if (rideType == 1) {
                iActivityHelper.changeType(1, 0, 1);
            } else {
                iActivityHelper.changeType(2, passengerPref, 1);
            }
            iActivityHelper.changeVehicleType(vehicleType);

        });

        mini.setOnClickListener(v -> {
            vehicleType = 2;
            selectVehicle(mini);


            if (rideType == 1) {
                iActivityHelper.changeType(1, 0, 1);
            } else {
                iActivityHelper.changeType(2, passengerPref, 1);
            }
            iActivityHelper.changeVehicleType(vehicleType);

        });

        keke.setOnClickListener(v -> {
            vehicleType = 3;

            selectVehicle(keke);
            rideTypeSelector.setVisibility(View.GONE);
            if (rideType == 1) {
                iActivityHelper.changeType(1, 0, 1);
            }
            iActivityHelper.changeVehicleType(vehicleType);


        });

        groupPoolOption.setVisibility(rideType == 2 ? View.VISIBLE : View.GONE);

        mRideNow.setOnClickListener(v -> {

            if (rideType == 1) {
                iActivityHelper.changeType(1, 0, 1);
            } else {
                int selected = groupPoolOption.getCheckedRadioButtonId();
                if (groupPoolOption.getCheckedRadioButtonId() == -1) {
                    showMessage(R.string.please_select_pool_option);
                    return;
                }
                RadioButton poolRadio = view.findViewById(selected);
                iActivityHelper.changeType(2, Integer.parseInt(poolRadio.getTag().toString()), 1);
            }
            iActivityHelper.changeVehicleType(vehicleType);
            Bundle bundle = new Bundle();

            try {
                bundle.putSerializable(AppConstants.K_RIDE_TYPE_DATA, vehicleTypeModelArrayLis.get(vehicleType - 1));

                RideData rideData = RideData.newInstance(bundle);

                getFragmentManager().beginTransaction()
                        .replace(R.id.fl_bottom_sheet_home, rideData)
                        .addToBackStack(null)
                        .commit();
            } catch (Exception e) {
                showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
            }


        });

        mRideLater.setOnClickListener(v -> {
            showRideLater(view, rideType);

        });
        rideTypeBtn.setOnClickListener(v -> {
            isRideLater = !isRideLater;
            if (isRideLater) {
                showRideLater(view, rideType);
            }

        });

        ObjectAnimator colorAnim = ObjectAnimator.ofInt(bookRide, AppConstants.P_TEXT_COLOR, getResources().getColor(R.color.colorWhite), Color.TRANSPARENT);
        colorAnim.setDuration(1000);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setRepeatCount(ValueAnimator.INFINITE);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        countDownTimer = new CountDownTimer(AppConstants.TIMER_RIDE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(getClass().getSimpleName(), "");
            }

            @Override
            public void onFinish() {
                int type = Helper.getNotificationReceived(getmActivity());
                if (type != 2) {
                    //    postRideDelayAction();
                    //Show a Pop Up
                }
                Helper.removeNotification(getmActivity());
            }
        };

        bookRide.setOnClickListener(v -> {


            if (bookRide.getText().toString().equalsIgnoreCase(getString(R.string.searching_for_your_ride))){

                try {
                    colorAnim.end();
                    iActivityHelper.cancelAPI();
                    bookRide.setText(getString(R.string.book));
                    bookRide.setTextColor(getResources().getColor(R.color.white));

                } catch (NullPointerException e) {
                    showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
                }

            }else {
                bookRide.setText(getString(R.string.searching_for_your_ride));
//            bookRide.setClickable(false);

                if (iActivityHelper.setPickerStatus() == 3) {
                    colorAnim.start();
                    try {
                        iActivityHelper.post();
                        countDownTimer.start();
                    } catch (NullPointerException e) {
                        showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
                    }
                }
            }
        });

        PaymentMethod paymentMethod = Helper.getPaymentMethod(getContext());
        paymentMethodSelection(paymentMethod);

        paymentMethodDisposable = RxBus.getInstance().getIntentEvent().subscribe(o -> {
            if (o instanceof PaymentMethod) {
                try {
                    paymentMethodSelection((PaymentMethod) o);
                } catch (Exception e) {

                }
            }
        });
        getVehicleTypes(rideType);

//        getCalculatedFare();

    }

    private void selectVehicle(LinearLayout vehicle) {
        if (vehicle.getId() == R.id.micro_vehicle) {
//            micro.setBackgroundColor(getResources().getColor(R.color.light_grey));
//            mini.setBackgroundColor(0x00000000);
//            keke.setBackgroundColor(0x00000000);

//            microVehicle.setBackgroundColor(getResources().getColor(R.color.light_grey));
//            miniVehicle.setBackgroundColor(0x00000000);
//            kekeVehicle.setBackgroundColor(0x00000000);
            microVehicle.setSelected(true);
            microVehicle.animate().scaleX(AppConstants.SCALE_VALUE).scaleY(AppConstants.SCALE_VALUE);

            miniVehicle.setSelected(false);
            miniVehicle.animate().scaleX(1f).scaleY(1f);

            kekeVehicle.setSelected(false);
            kekeVehicle.animate().scaleX(1f).scaleY(1f);
        } else if (vehicle.getId() == R.id.mini_vehicle) {
//            micro.setBackgroundColor(0x00000000);
//            mini.setBackgroundColor(getResources().getColor(R.color.light_grey));
//            keke.setBackgroundColor(0x00000000);

//            microVehicle.setBackgroundColor(0x00000000);
//            miniVehicle.setBackgroundColor(getResources().getColor(R.color.light_grey));
//            kekeVehicle.setBackgroundColor(0x00000000);
            microVehicle.setSelected(false);
            microVehicle.animate().scaleX(1f).scaleY(1f);

            miniVehicle.setSelected(true);
            miniVehicle.animate().scaleX(AppConstants.SCALE_VALUE).scaleY(AppConstants.SCALE_VALUE);

            kekeVehicle.setSelected(false);
            kekeVehicle.animate().scaleX(1f).scaleY(1f);

        } else if (vehicle.getId() == R.id.keke_vehicle) {
//            micro.setBackgroundColor(0x00000000);
//            mini.setBackgroundColor(0x00000000);
//            keke.setBackgroundColor(getResources().getColor(R.color.light_grey));

//            microVehicle.setBackgroundColor(0x00000000);
//            miniVehicle.setBackgroundColor(0x00000000);
//            kekeVehicle.setBackgroundColor(getResources().getColor(R.color.light_grey));
            microVehicle.setSelected(false);
            microVehicle.animate().scaleX(1f).scaleY(1f);

            miniVehicle.setSelected(false);
            miniVehicle.animate().scaleX(1f).scaleY(1f);

            kekeVehicle.setSelected(true);
            kekeVehicle.animate().scaleX(AppConstants.SCALE_VALUE).scaleY(AppConstants.SCALE_VALUE);
        }
    }

    public void paymentMethodSelection(PaymentMethod paymentMethod) {
        switch (paymentMethod.getType()) {
            case AppConstants.PAYMENT_METHOD.WALLET:

                paymentLogo.setImageDrawable(getResources().getDrawable(R.drawable.ic_wallet));
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

    @OnClick(R.id.container_payment_method)
    public void onPaymentMethodSelection() {
        PaymentMethodDialog paymentMethodDialog = PaymentMethodDialog.newInstance(0, AppConstants.USER_RIDE, 0);
        paymentMethodDialog.show(getChildFragmentManager(), PaymentMethodDialog.class.getSimpleName());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        paymentMethodDisposable.dispose();
    }

    private void showRideLater(View view, int rideType) {
        if (rideType == 1) {
            iActivityHelper.changeType(1, 0, 2);
        } else {
            int selected = groupPoolOption.getCheckedRadioButtonId();
            if (groupPoolOption.getCheckedRadioButtonId() == -1) {
                showMessage(R.string.please_select_pool_option);
                return;
            }
            RadioButton poolRadio = view.findViewById(selected);
            iActivityHelper.changeType(2, Integer.parseInt(poolRadio.getTag().toString()), 2);
        }
        iActivityHelper.changeVehicleType(vehicleType);
        getFragmentManager().beginTransaction()
                .replace(R.id.fl_bottom_sheet_home, new RideLater())
                .addToBackStack(null)
                .commit();
    }


    private void switchToRide(View view, int rideType) {
        if (rideType == 1) {
            iActivityHelper.changeType(1, 0, 1);
        } else {
            int selected = groupPoolOption.getCheckedRadioButtonId();
            if (groupPoolOption.getCheckedRadioButtonId() == -1) {
                showMessage(R.string.please_select_pool_option);
                return;
            }
            RadioButton poolRadio = view.findViewById(selected);
            iActivityHelper.changeType(2, Integer.parseInt(poolRadio.getTag().toString()), 1);
        }
        iActivityHelper.changeVehicleType(vehicleType);
        Bundle bundle = new Bundle();

        try {
            bundle.putSerializable(AppConstants.K_RIDE_TYPE_DATA, vehicleTypeModelArrayLis.get(vehicleType - 1));

            RideData rideData = RideData.newInstance(bundle);

            getFragmentManager().beginTransaction()
                    .replace(R.id.fl_bottom_sheet_home, rideData)
                    .addToBackStack(null)
                    .commit();
        } catch (Exception e) {
            showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
        }
    }


    private Animation.AnimationListener perKmAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            perKMFare.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    /*
     * Get calculated fare for this ride
     * */

    private void postRideDelayAction() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(AppConstants.K_RIDE_ID, Helper.getRideId(getmActivity()));
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<String>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // getmActivity().onBackPressed();
                    //  iActivityHelper.onNoDriverFound();
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

    private void getVehicleTypes(int ridetype) {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<VehicleTypeModel>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<VehicleTypeModel>> response) {
                hideLoading();
                hideAlertDialog();
                vehicleTypeModelArrayLis.clear();
                if (response.isSuccessful() && response.body() != null) {
                    vehicleTypeModelArrayLis.addAll(response.body().getResponse());
                    try {
                        getCalculatedFare(ridetype);
                        perKMFare.setText(getString(R.string.rate_per_km, vehicleTypeModelArrayLis.get(0).getRatePerKm()));
                        //   withouAC.setText(getString(R.string.rate_per_km, vehicleTypeModelArrayLis.get(0).getRatePerKm()));
                        //  withAc.setText(getString(R.string.rate_per_km, vehicleTypeModelArrayLis.get(1).getRatePerKm()));
                        // bus.setText(getString(R.string.rate_per_seat, vehicleTypeModelArrayLis.get(2).getRatePerKm()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<VehicleTypeModel>> response) {
                hideLoading();
                hideAlertDialog();
                if (response.code() == 401) {
                    showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
                    if (mAlertDialog != null) {
                        mAlertDialog.setCancelable(false);
                        TextView title = mAlertDialog.findViewById(R.id.tv_title);
                        title.setText(getString(R.string.your_session_has_expired));
                        Button btnOk = mAlertDialog.findViewById(R.id.btn_cancel);
                        btnOk.setText(getString(R.string.login));
                        btnOk.setOnClickListener(v -> {
                            SessionManager.getInstance().removeUserData(getmContext());
                            UIHelper.getInstance().switchActivity(getmActivity(), StartUpActivity.class, null, null, null, true);
                        });
                    }
                    return;
                }
                showErrorPrompt(response);

            }

            @Override
            public void onNullListResponse() {
                hideLoading();
                hideAlertDialog();
            }

            @Override
            public void onFailureList(Throwable t) {
                hideLoading();
                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        getVehicleTypes(ridetype);
                    });
                }
            }
        }, SessionManager.getAccessToken()).getVehicleTypes();
    }


    @Override
    public void onSuccess() {

    }

    @Override
    public void onDimiss() {


    }

    private void getCalculatedFare(int ridetype) {
        showLoading();
        HashMap<String, Object> hashMap = new HashMap<>();
        LatLng source = iActivityHelper.getSource();
        LatLng dest = iActivityHelper.getDestination();
        hashMap.put(AppConstants.Q_SOURCE_LAT, source.latitude);
        hashMap.put(AppConstants.Q_SOURCE_LONG, source.longitude);
        hashMap.put(AppConstants.Q_DEST_LAT, dest.latitude);
        hashMap.put(AppConstants.Q_DEST_LONG, dest.longitude);
        hashMap.put(AppConstants.Q_VEHICLE_TYPE, iActivityHelper.getData().get(AppConstants.K_VEHICLE));
        hashMap.put(AppConstants.K_TYPE, ridetype);

        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<HashMap<String, Object>>() {
            @Override
            public void onResponse(Response<QualStandardResponse<HashMap<String, Object>>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        withoutACFare.setText(getString(R.string.fare_in_float_naira, Float.parseFloat(response.body().getResponse().get(AppConstants.K_WITHOUT_AC_FARE).toString())));
                        withACFare.setText(getString(R.string.fare_in_float_naira, Float.parseFloat(response.body().getResponse().get(AppConstants.K_WITH_AC_FARE).toString())));
                        kekeFare.setText(getString(R.string.fare_in_float_naira, Float.parseFloat(response.body().getResponse().get(AppConstants.K_KEKE_FARE).toString())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onError(Response<QualStandardResponse<HashMap<String, Object>>> response) {
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
                        getCalculatedFare(ridetype);
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

