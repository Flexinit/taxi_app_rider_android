package com.quawlebs.drupp.view.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.models.RateDriverModel;
import com.quawlebs.drupp.models.RideInfo;
import com.quawlebs.drupp.util.IDialogButtonClickListener;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.view.ui.base.MainBaseFragment;
import com.quawlebs.drupp.view.ui.dialog.CompletedDialog;
import com.quawlebs.drupp.view.ui.ride.BottomSheetRideActivity;
import com.willy.ratingbar.ScaleRatingBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

public class RateDriverFragment extends MainBaseFragment implements IDialogButtonClickListener {

    @BindView(R.id.rating_bar)
    ScaleRatingBar ratingBar;
    @BindView(R.id.et_comment)
    EditText comment;

    @BindView(R.id.rg_driver_careful)
    RadioGroup rgDriverCareful;
    @BindView(R.id.rg_vehicle_neat)
    RadioGroup rgVehicleNeat;
    @BindView(R.id.rg_driver_punctual)
    RadioGroup rgDriverPunctual;


    private int driverCourteous = 0, driverClean = 0, driverCareful = 0,driverPunctual=0;
    private RideInfo responseData;
    private int currentRideId;
    private int currentDriverId;
    private int currentRideType;

    public static RateDriverFragment newInstance(Integer rideId, Integer driverId, int currentRideType, String currentDriverImage) {

        Bundle args = new Bundle();
        args.putInt(AppConstants.K_RIDE_ID, rideId);
        args.putInt(AppConstants.K_RIDE_TYPE, currentRideType);
        args.putInt(AppConstants.K_DRIVER_ID, driverId);
        args.putString(AppConstants.K_IMAGE, currentDriverImage);
        RateDriverFragment fragment = new RateDriverFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static RateDriverFragment newInstance(int currentRideId, int driverId, int currentRideType, String currentDriverImage, RateDriverModel rateDriverModel) {
        Bundle args = new Bundle();
        args.putInt(AppConstants.K_RIDE_ID, currentRideId);
        args.putInt(AppConstants.K_DRIVER_ID, driverId);
        args.putInt(AppConstants.K_RIDE_TYPE, currentRideType);
        args.putString(AppConstants.K_IMAGE, currentDriverImage);
        args.putParcelable(AppConstants.K_DRIVER_ID, rateDriverModel);
        RateDriverFragment fragment = new RateDriverFragment();
        fragment.setArguments(args);

        return fragment;
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

        }


        rgDriverCareful.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.yes){
                    driverCareful=0;
                }
                else{
                    driverCareful=1;
                }

            }
        });
        rgVehicleNeat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.yes){
                    driverClean=0;
                }
                else{
                    driverClean=1;
                }

            }
        });
        rgDriverPunctual.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.yes){
                    driverPunctual=0;
                }
                else{
                    driverPunctual=1;
                }

            }
        });


        getRateReviewForEdit();
    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_driver_rating, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

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
            rateDriverModel.setIsDriverPunctual(driverPunctual);

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

                    CompletedDialog completedDialog=new CompletedDialog(RateDriverFragment.this,R.layout.custom_dialog_complete);
                    completedDialog.show(getActivity().getSupportFragmentManager(),CompletedDialog.class.getSimpleName());

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


                        if(rateDriverModel.getIsDriverCareful()==1){
                            rgDriverCareful.check(R.id.no);
                        }
                        else{
                            rgDriverCareful.check(R.id.yes);
                        }
                        if(rateDriverModel.getIsVehicleClean()==1){
                            rgVehicleNeat.check(R.id.no);
                        }{
                            rgVehicleNeat.check(R.id.yes);
                        }
                        if(rateDriverModel.getIsDriverPunctual()!=null){
                            if(rateDriverModel.getIsDriverPunctual()==1){
                                rgDriverPunctual.check(R.id.no);
                            }
                            else{
                                rgDriverPunctual.check(R.id.no);
                            }
                        }
                        else{
                            rgDriverPunctual.check(R.id.yes);
                        }



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
    public void onButtonClick() {
        UIHelper.getInstance().switchActivity(getActivity(), BottomSheetRideActivity.class, null, null, null, true);

    }

    @Override
    public void setUpDialogViews(View view) {
        ((TextView)view.findViewById(R.id.textView1)).setText("Submited");
        ((TextView)view.findViewById(R.id.textView2)).setText("Your review has been successfully submitted");
        ((Button)view.findViewById(R.id.btnComplete)).setText("Done");
        ((Button)view.findViewById(R.id.btnComplete)).setTextColor(getActivity().getColor(android.R.color.white));

    }

    @Override
    public void showAlertDialog(int resId) {

    }
}
