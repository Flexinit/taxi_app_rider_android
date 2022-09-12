package com.quawlebs.drupp.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.quawlebs.drupp.login.StartUpActivity;
import com.quawlebs.drupp.models.TripHistortDetailModel;
import com.quawlebs.drupp.models.TripHistoryModel;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.view.ui.base.BaseActivity;
import com.quawlebs.drupp.util.MagicText;
import com.quawlebs.drupp.util.RxBus;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetwork;
import com.quawlebs.drupp.connectivity.http.QualStandardResponse;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.helpers.Timing;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class TripHistoryFinal extends BaseActivity {
    private String date, source, destination, fare;
    private TextView mDate, mSource, mDestination, mStatus;
    private TextView mDriverName;
    private MagicText mFare;
    private TextView mVehicleDetails;
    private CircleImageView driverProfile, carImage;
    private Button query;
    private RatingBar ratingBar;
    private TableRow containerAmount;
    private TripHistoryModel tripHistoryModel;
    private Disposable tripDisposable;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_ride_details);
        getToolbarBack().setOnClickListener(v -> onBackPressed());
        setToolbarTitle(R.string.ride_details);
        mVehicleDetails = findViewById(R.id.tv_car_details);
        driverProfile = findViewById(R.id.iv_driver_image);
        carImage = findViewById(R.id.iv_vehicle_image);
        mDriverName = findViewById(R.id.tv_driver_name);
        mDate = findViewById(R.id.tv_Trip_His_Time);
        mSource = findViewById(R.id.tvSourceCity);
        mDestination = findViewById(R.id.tvDestinationCity);
        mFare = findViewById(R.id.tv_fare);
        query = findViewById(R.id.query);
        containerAmount = findViewById(R.id.container_amount);
        ratingBar = findViewById(R.id.rate_bar);
        mStatus = findViewById(R.id.tv_status);


        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                RxBus.getInstance().setIntentEvent(tripHistoryModel);
                Intent in = new Intent(TripHistoryFinal.this, TripHistoryQuery.class);
                startActivity(in);
            }
        });
        tripDisposable = RxBus.getInstance().getIntentEvent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof TripHistoryModel) {
                        tripHistoryModel = (TripHistoryModel) o;
                        getData(((TripHistoryModel) o).getId());
                    }
                });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tripDisposable.dispose();
    }

    private void getData(Integer rideId) {
        showDialogProgressBar();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<TripHistortDetailModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<TripHistortDetailModel>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    TripHistortDetailModel tripHistortDetailModel = response.body().getResponse();
                    try {
                        source = response.body().getResponse().getSource();
                        date = response.body().getResponse().getRide_date();
                        destination = response.body().getResponse().getDestination();
                        fare=getApplicationContext().getString(R.string.naira) + response.body().getResponse().getTotal_fare();
                        //fare = response.body().getResponse().getTotal_fare();
                        String datefinal = Timing.getTimeInString(Long.parseLong(date), Timing.TimeFormats.CUSTOM_DATE_TIME);

                        mDate.setText(datefinal);
                        mSource.setText(source);
                        mDestination.setText(destination);
                        mFare.setText(fare);
                        switch (tripHistoryModel.getStatus()) {
                            case AppConstants.RIDE_STATUS.RIDE_CANCELLED:
                                mStatus.setText(getString(R.string.ride_canceled));
                                break;
                            case AppConstants.RIDE_STATUS.RIDE_COMPLETED:
                            case AppConstants.RIDE_STATUS.RIDE_PAID:
                                mStatus.setText(getString(R.string.ride_completed));
                                break;
                        }

                        if (tripHistoryModel.getStatus() == AppConstants.RIDE_STATUS.RIDE_CANCELLED) {
                            containerAmount.setVisibility(View.GONE);
                            ratingBar.setVisibility(View.GONE);
                        } else {
                            containerAmount.setVisibility(View.VISIBLE);
                            ratingBar.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(getApplicationContext(),"Rating is "+response.body().getResponse().getRate(),Toast.LENGTH_LONG).show();
                        ratingBar.setRating(response.body().getResponse().getRate());
                        String method = "";
                        switch (response.body().getResponse().getPaymentOption()) {
                            case AppConstants.PAYMENT_METHOD.CARD:
                                method = getString(R.string.debit_card);
                                break;
                            case AppConstants.PAYMENT_METHOD.WALLET:
                                method = getString(R.string.wallet);
                                break;
                            case AppConstants.PAYMENT_METHOD.NET_BANKING:
                                method = getString(R.string.net_banking);
                                break;
                            case AppConstants.PAYMENT_METHOD.CASH:
                                method = getString(R.string.cash);
                                break;
                        }

                        String fullMethod = getString(R.string.price_in_naira_with_method, fare, method);
                        mFare.change(fullMethod, getResources().getColor(R.color.colorBlack), 14, "bold", method);

                        Glide.with(TripHistoryFinal.this).load(AppConstants.IMAGE_URL + response.body().getResponse().getDriverImage()).apply(new RequestOptions()
                                .error(R.drawable.user_profile_icon)
                                .centerCrop().placeholder(R.drawable.user_profile_icon)).into(driverProfile);

                        Glide.with(TripHistoryFinal.this).load(AppConstants.IMAGE_URL + response.body().getResponse().getVehicleImage()).apply(new RequestOptions()
                                .error(R.drawable.user_profile_icon)
                                .centerCrop().placeholder(R.drawable.user_profile_icon)).into(carImage);

                        mDriverName.setText(response.body().getResponse().getDriverName());
                        String fullVehicleDetails = getString(R.string.vehicle_details, response.body().getResponse().getVehicleColor()
                                , response.body().getResponse().getVehicleBrand(), response.body().getResponse().getVehicleModel());
                        mVehicleDetails.setText(fullVehicleDetails);

                    } catch (Exception e) {

                    }

                }
            }

            @Override
            public void onError(Response<QualStandardResponse<TripHistortDetailModel>> response) {
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
                            SessionManager.getInstance().removeUserData(TripHistoryFinal.this);
                            UIHelper.getInstance().switchActivity(TripHistoryFinal.this, StartUpActivity.class, null, null, null, true);
                        });
                    }
                    return;

                }
                showErrorPrompt(response);
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
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        getData(rideId);
                    });
                }

            }
        }, SessionManager.getAccessToken()).get_Single_Trip_Detail(rideId.toString());
    }
}
