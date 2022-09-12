package com.quawlebs.drupp.view.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.quawlebs.drupp.helpers.IDialogObserver;
import com.quawlebs.drupp.models.RateDriverModel;
import com.quawlebs.drupp.models.RideInfo;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.view.ui.base.DialogBaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

public class RateDriverDialog extends DialogBaseFragment {
    @BindView(R.id.rating_bar)
    AppCompatRatingBar ratingBar;
    @BindView(R.id.et_comment)
    EditText comment;
    @BindView(R.id.switch_courteous)
    LabeledSwitch switchCourteous;
    @BindView(R.id.switch_careful)
    LabeledSwitch switchCareful;
    @BindView(R.id.switch_clean)
    LabeledSwitch switchClean;
    @BindView(R.id.iv_driver_image)
    CircleImageView driverProfile;
    private IDialogObserver iDialogObserver;

    public void setiDialogObserver(IDialogObserver iDialogObserver) {
        this.iDialogObserver = iDialogObserver;
    }

    private int driverCourteous = -1, driverClean = -1, driverCareful = -1;
    private RideInfo responseData;
    private int currentRideId;
    private int currentDriverId;
    private int currentRideType;

    public static RateDriverDialog newInstance(Integer rideId, Integer driverId, int currentRideType, String currentDriverImage) {

        Bundle args = new Bundle();
        args.putInt(AppConstants.K_RIDE_ID, rideId);
        args.putInt(AppConstants.K_RIDE_TYPE, currentRideType);
        args.putInt(AppConstants.K_DRIVER_ID, driverId);
        args.putString(AppConstants.K_IMAGE, currentDriverImage);
        RateDriverDialog fragment = new RateDriverDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static RateDriverDialog newInstance(int currentRideId, int driverId, int currentRideType, String currentDriverImage, RateDriverModel rateDriverModel) {
        Bundle args = new Bundle();
        args.putInt(AppConstants.K_RIDE_ID, currentRideId);
        args.putInt(AppConstants.K_DRIVER_ID, driverId);
        args.putInt(AppConstants.K_RIDE_TYPE, currentRideType);
        args.putString(AppConstants.K_IMAGE, currentDriverImage);
        args.putParcelable(AppConstants.K_DRIVER_ID, rateDriverModel);
        RateDriverDialog fragment = new RateDriverDialog();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.AppTheme);
    }

    @Override
    protected void initViewsForFragment(View view) {
        if (getArguments() != null) {
            currentRideId = getArguments().getInt(AppConstants.K_RIDE_ID);
            currentDriverId = getArguments().getInt(AppConstants.K_DRIVER_ID);
            currentRideType = getArguments().getInt(AppConstants.K_RIDE_TYPE);
            if (getArguments().getParcelable(AppConstants.K_DRIVER_ID) != null) {
                RateDriverModel rateDriverModel = getArguments().getParcelable(AppConstants.K_DRIVER_ID);

                ratingBar.setRating(rateDriverModel.getRate());
                driverCourteous = rateDriverModel.getIsDriverCourteous();
                driverCareful = rateDriverModel.getIsDriverCareful();
                driverClean = rateDriverModel.getIsVehicleClean();

            }
            String image = getArguments().getString(AppConstants.K_IMAGE);
            Glide.with(getmActivity()).load(AppConstants.IMAGE_URL + image).apply(new RequestOptions()
                    .error(R.drawable.user_profile_icon)
                    .centerCrop().placeholder(R.drawable.user_profile_icon)).into(driverProfile);


        }

        switchCareful.setOnToggledListener((toggleableView, isOn) -> driverCareful = isOn ? 1 : 0);
        switchClean.setOnToggledListener((toggleableView, isOn) -> driverClean = isOn ? 1 : 0);
        switchCourteous.setOnToggledListener((toggleableView, isOn) -> driverCourteous = isOn ? 1 : 0);

        getRateReviewForEdit();
//
//        if (bundle != null) {
//            boolean isSet = bundle.getBoolean(AppConstants.K_FLAG, false);
//            //TODO : CHANGE RIDE TYPE
//            if (isSet) {
//                RateDriverModel rateDriverModel = new RateDriverModel();
//                rateDriverModel.setRideType(AppConstants.RIDE_TYPE.USER_RIDE);
//                if (responseData != null) {
//                    rateDriverModel.setRideId(responseData.getId());
//                    rateDriverModel.setReceiver(responseData.getDriverId());
//                } else {
//                    try {
//                        rateDriverModel.setRideId(jsonObject.getInt(AppConstants.K_RIDE_ID));
//                        rateDriverModel.setReceiver(jsonObject.getInt(AppConstants.K_DRIVER_ID));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                rateDriverModel.setRate(ratingBar.getRating());
////                            rateDriverModel.setReview(message.getText().toString().trim());
//                rateDriverModel.setIsDriverCourteous(driverCourteous);
//                rateDriverModel.setIsVehicleClean(driverClean);
//                rateDriverModel.setIsDriverCareful(driverCareful);
//
//                editRate(rateDriverModel);
//            }
//        } else {
//            if (isValidate()) {
//                RateDriverModel rateDriverModel = new RateDriverModel();
//                rateDriverModel.setRideType(AppConstants.RIDE_TYPE.USER_RIDE);
//                if (responseData != null) {
//                    rateDriverModel.setRideId(responseData.getId());
//                    rateDriverModel.setReceiver(responseData.getDriverId());
//                } else {
//                    try {
//                        rateDriverModel.setRideId(jsonObject.getInt(AppConstants.K_RIDE_ID));
//                        rateDriverModel.setReceiver(jsonObject.getInt(AppConstants.K_DRIVER_ID));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//                rateDriverModel.setRate(ratingBar.getRating());
////                            rateDriverModel.setReview(message.getText().toString().trim());
//                rateDriverModel.setIsDriverCourteous(driverCourteous);
//                rateDriverModel.setIsVehicleClean(driverClean);
//                rateDriverModel.setIsDriverCareful(driverCareful);
//
//                rate(rateDriverModel);
//            }
//        }
    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.dialog_driver_rating, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_done)
    public void onRate() {
        if (isValidate()) {
            RateDriverModel rateDriverModel = new RateDriverModel();
            rateDriverModel.setRideType(AppConstants.RIDE_TYPE.USER_RIDE);

            rateDriverModel.setRideId(currentRideId);
            rateDriverModel.setReceiver(currentDriverId);
            rateDriverModel.setRate(ratingBar.getRating());

            rateDriverModel.setIsDriverCourteous(driverCourteous);
            rateDriverModel.setIsVehicleClean(driverClean);
            rateDriverModel.setIsDriverCareful(driverCareful);

            rate(rateDriverModel);
        }
    }


    private boolean isValidate() {
        if (ratingBar.getRating() == 0.0) {
            showMessage(R.string.please_give_rating);
            return false;
        } else if (driverCareful == -1 || driverClean == -1 || driverCourteous == -1) {
            showMessage(R.string.please_select_either_yes_or_no);
            return false;
        }
        return true;
    }

    private void rate(RateDriverModel rateDriverModel) {
        DruppRequestHandler.clearInstance();


        showLoading();
        DruppRequestHandler.getInstance(new INetworkList<String>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<String>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    iDialogObserver.onDialogResult(null);

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
                        rate(rateDriverModel);
                    });
                }
            }
        }, SessionManager.getAccessToken()).rateDriver(rateDriverModel);
    }

    private void getRateReviewForEdit() {
        //TODO : CHANGE RIDETYPE DYNAMIC
        showDialogProgressBar();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<RateDriverModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<RateDriverModel>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        RateDriverModel rateDriverModel = response.body().getResponse();
                        ratingBar.setRating(rateDriverModel.getRate());
//                        message.setText(rateDriverModel.getReview());


                        switchCareful.setOn(rateDriverModel.getIsDriverCareful() == 1);
                        switchCourteous.setOn(rateDriverModel.getIsDriverCourteous() == 1);
                        switchClean.setOn(rateDriverModel.getIsVehicleClean() == 1);

                        driverCourteous = rateDriverModel.getIsDriverCourteous();
                        driverCareful = rateDriverModel.getIsDriverCareful();
                        driverClean = rateDriverModel.getIsVehicleClean();

                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onError(Response<QualStandardResponse<RateDriverModel>> response) {
                hideDialogProgressBar();
                //   showErrorPrompt(response);
            }

            @Override
            public void onNullResponse() {
                hideDialogProgressBar();
            }

            @Override
            public void onFailure(Throwable t) {
                hideDialogProgressBar();
                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(view -> {
                        hideAlertDialog();
                        getRateReviewForEdit();

                    });
                }
            }
        }, SessionManager.getAccessToken()).getRateReviewForEdit(currentRideId, currentRideType);
    }

    @Override
    public void showAlertDialog(int resId) {

    }
}
