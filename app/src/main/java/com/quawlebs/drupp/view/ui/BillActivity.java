package com.quawlebs.drupp.view.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.Helper;
import com.quawlebs.drupp.helpers.IDialogObserver;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.helpers.Timing;
import com.quawlebs.drupp.models.PaymentMethod;
import com.quawlebs.drupp.models.RateDriverModel;
import com.quawlebs.drupp.models.RideInfo;
import com.quawlebs.drupp.models.TransactionRefStoreModel;
import com.quawlebs.drupp.util.AppUtil;
import com.quawlebs.drupp.util.IDialogButtonClickListener;
import com.quawlebs.drupp.util.NotificationUtil;
import com.quawlebs.drupp.util.RxBus;
import com.quawlebs.drupp.util.UIHelper;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;
import com.quawlebs.drupp.view.ui.dialog.CompletedDialog;
import com.quawlebs.drupp.view.ui.dialog.RateDriverDialog;
import com.quawlebs.drupp.view.ui.payments.MainPaymentActivity;
import com.quawlebs.drupp.view.ui.ride.BottomSheetRideActivity;
import com.willy.ratingbar.ScaleRatingBar;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import retrofit2.Response;

public class BillActivity extends MainBaseActivity implements IDialogObserver, IDialogButtonClickListener {
    private RideInfo rideInfo;
    private String fareAmount;
    private TextView driverName, vehicleModel, vehicleNumber, toolBarTitle; /*source, destination*/
    ;
    Toolbar toolbar;
    ImageView driverImage, vehicleImage;
    ScaleRatingBar ratingBar;
    //private MagicText mBillAmount, mPaymentMethod;
    //private TextView time;
    private int driverCourteous = -1, driverClean = -1, driverCareful = -1;
    private PaymentMethod currentPaymentMethodType;

    //---------------------Globals----------------------------


    private BroadcastReceiver ratings = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_ratings, null);
            TextView by = alertLayout.findViewById(R.id.by_ratings);
            TextView msg = alertLayout.findViewById(R.id.msg_ratings);
            RatingBar ratingBar = alertLayout.findViewById(R.id.rateBar_ratings);

            if (intent != null) {
                msg.setText(intent.getStringExtra(AppConstants.K_REVIEW));
                //TODO: CHANGE TO FLOAT LATER
                ratingBar.setRating((float) Integer.parseInt(intent.getStringExtra(AppConstants.K_RATE)));
            }


            AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(BillActivity.this);
            final AlertDialog alertDialog = alertDialogbuilder.create();
            alertDialog.setView(alertLayout);
            alertDialog.show();
            alertLayout.findViewById(R.id.btOk).setOnClickListener(v -> alertDialog.dismiss());
        }
    };
    private JSONObject jsonObject;
    private int currentRideId;
    private int driverId;
    private int currentRideType;
    private String currentDriverImage;
    private RateDriverDialog rateDriverDialog;
    private Button buttonDone;
    private String source;
    private String destination;


    @Override
    protected void setUp() {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_ride);
        ButterKnife.bind(this);
        /*source = findViewById(R.id.tv_source);
        destination = findViewById(R.id.tv_destination);*/
        //mBillAmount = findViewById(R.id.tv_fare);
        //time = findViewById(R.id.tv_time);
        //mPaymentMethod = findViewById(R.id.tv_payment_method);
        driverName = findViewById(R.id.tv_driver_name);
        vehicleNumber = findViewById(R.id.tv_driver_car_number);
        vehicleModel = findViewById(R.id.tv_driver_car_model);
        driverImage = findViewById(R.id.iv_driver_image);
        vehicleImage = findViewById(R.id.iv_vehicle_image);
        toolbar = findViewById(R.id.profileToolbar);
        toolBarTitle = findViewById(R.id.tv_title);
        ratingBar = findViewById(R.id.rating_bar_driver);
        setSupportActionBar(toolbar);
        toolBarTitle.setText("Trip Completed");
        buttonDone = findViewById(R.id.btn_done);


//        driverImage = findViewById(R.id.ll_img);
//        driverRateHeader = findViewById(R.id.tvRateDriver);


        NotificationUtil.removeAllNotification(getApplicationContext());
        //Firebase Set Up
        //Unsubscribe from chat notifications
        FirebaseMessaging.getInstance().unsubscribeFromTopic("pushNotifications");


        Helper.saveActivityStack(AppConstants.BILL_ACTIVITY, this);
        //responseData = Helper.getInstance(BillActivity.this).readFromJson(AppConstants.DRIVER_DETAILS, ResponseData.class);

        currentPaymentMethodType = Helper.getPaymentMethod(this);
        Intent intent = getIntent();

        if (intent == null) {
            String json = SessionManager.getInstance().loadCurrentRideDetails(getApplicationContext());
            try {
                jsonObject = new JSONObject(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (intent.hasExtra(AppConstants.K_RIDE_PARAMS)) {
            try {
                jsonObject = new JSONObject(getIntent().getStringExtra(AppConstants.K_RIDE_PARAMS));


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (jsonObject != null) {

            try {

                source = jsonObject.getString(AppConstants.K_SOURCE);
                destination = jsonObject.getString(AppConstants.K_DEST);
                fareAmount = jsonObject.getString(AppConstants.K_TOTAL_FARE);


                currentRideType = jsonObject.getInt(AppConstants.K_POSTED_BY_DRIVER);


                driverName.setText(jsonObject.getString(AppConstants.K_DRIVER_NAME));

                vehicleModel.setText(jsonObject.getString(AppConstants.K_VEHICLE_MODEL));


                vehicleNumber.setText(jsonObject.getString(AppConstants.K_CAB_NUMBER));

                Glide.with(this).load(AppConstants.IMAGE_URL + jsonObject.getString(AppConstants.K_DRIVER_IMAGE)).apply(new RequestOptions()
                        .error(R.drawable.ic_user_silhouette)
                        .centerCrop().placeholder(R.drawable.ic_user_silhouette)).into(driverImage);
                Glide.with(this).load(AppConstants.IMAGE_URL + jsonObject.getString(AppConstants.K_VEHICLE_IMAGES)).apply(new RequestOptions()
                        .error(R.drawable.ic_user_silhouette)
                        .centerCrop().placeholder(R.drawable.ic_user_silhouette)).into(vehicleImage);
                currentRideId = jsonObject.getInt(AppConstants.K_RIDE_ID);
                driverId = jsonObject.getInt(AppConstants.K_DRIVER_ID);

                currentRideType = jsonObject.getInt(AppConstants.K_POSTED_BY_DRIVER);

                currentDriverImage = jsonObject.getString(AppConstants.K_DRIVER_IMAGE);
                ratingBar.setRating(Float.parseFloat(jsonObject.getString(AppConstants.K_AVERAGE_RATING)));

                if (currentPaymentMethodType == null || currentPaymentMethodType.getType() == AppConstants.PAYMENT_METHOD.CASH) {
                    buttonDone.setText("Rate Driver");
                }

                startTripSummaryFragment();

            } catch (Exception e) {
                e.printStackTrace();

            }

        }

        // if jsonObject not yet set

        else {

            RxBus.getInstance().getIntentEvent().subscribe(o -> {
                if (o instanceof RideInfo) {
                    rideInfo = (RideInfo) o;
                    if (rideInfo.getId() != null) {
                        currentRideId = rideInfo.getId();
                        driverId = rideInfo.getDriverId();

                        source = rideInfo.getSource();
                        destination = rideInfo.getDestination();

                        fareAmount = rideInfo.getTotalFare();


                        currentRideType = rideInfo.getPostedByDriver();


                        driverName.setText(rideInfo.getDriverName());


                        vehicleModel.setText(rideInfo.getVehicleModel());


                        vehicleNumber.setText(rideInfo.getVehicleNumber());

                        Glide.with(this).load(AppConstants.IMAGE_URL + rideInfo.getProfilePicture()).apply(new RequestOptions()
                                .error(R.drawable.ic_user_silhouette)
                                .centerCrop().placeholder(R.drawable.ic_user_silhouette)).into(driverImage);
                        Glide.with(this).load(AppConstants.IMAGE_URL + rideInfo.getExteriorFront()).apply(new RequestOptions()
                                .error(R.drawable.ic_user_silhouette)
                                .centerCrop().placeholder(R.drawable.ic_user_silhouette)).into(vehicleImage);

                        currentRideId = rideInfo.getId();
                        driverId = rideInfo.getDriverId();
                        fareAmount = rideInfo.getTotalFare();
                        currentRideType = rideInfo.getRideType();

                        currentDriverImage = rideInfo.getProfilePicture();

                        double averageRating = rideInfo.getAverageRating();

                        ratingBar.setRating((float) averageRating);

                        if (currentPaymentMethodType == null || currentPaymentMethodType.getType() == AppConstants.PAYMENT_METHOD.CASH) {
                            buttonDone.setText("Rate Driver");
                        }

                        startTripSummaryFragment();


                    }

                }

            });

        }


        buttonDone.setOnClickListener(v -> {
            if (currentPaymentMethodType == null || currentPaymentMethodType.getType() == AppConstants.PAYMENT_METHOD.CASH) {
                saveTransaction();
            } else {
                startPaymentActivity();
            }


        });


        registerReceiver(ratings, new IntentFilter(AppConstants.I_RATINGS));
    }

    private void startTripSummaryFragment() {
        FragmentTripSummary fragmentTripSummary = new FragmentTripSummary(source, destination, fareAmount, currentPaymentMethodType.getType());


        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragmentTripSummary, RateDriverFragment.class.getSimpleName())
                .commit();
    }

    private void startDriverRatingFragment() {
        RateDriverFragment rateDriverFragment = RateDriverFragment.newInstance(currentRideId, driverId, currentRideType, currentDriverImage);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, rateDriverFragment, RateDriverFragment.class.getSimpleName())
                .commit();
        Button buttonDone = findViewById(R.id.btn_done);
        buttonDone.setText("Submit Review");
        buttonDone.setOnClickListener(v -> {
            rateDriverFragment.onRate();
        });
    }


    private void startPaymentActivity() {
        //Clear Chat History
        Intent paymentIntent = new Intent(this, MainPaymentActivity.class);
        try {
            paymentIntent.setAction(AppConstants.U_PAY_VIA_WALLET);
            paymentIntent.putExtra(AppConstants.K_REQUEST_CODE, AppConstants.REQ_PAYMENT);
            paymentIntent.putExtra(AppConstants.K_RIDE_INFO, jsonObject != null ? jsonObject.toString() : null);
            startActivityForResult(paymentIntent, AppConstants.REQ_PAYMENT);
        } catch (Exception e) {
            showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
        }

    }

   /* @OnClick(R.id.btn_info)
    public void onInfo() {
        FareDialog fareDialog = FareDialog.newInstance(responseData.getBaseFare(), responseData.getPerKm(), responseData.getDistance());
        fareDialog.show(getSupportFragmentManager(), FareDialog.class.getSimpleName());


    }*/


    private void editRate(RateDriverModel rateDriverModel) {
        DruppRequestHandler.clearInstance();

        //        HashMap<String, String> parms = new HashMap<>();
//        parms.put("ride_id", responseData.getRideID());
//        parms.put("receiver", responseData.getDriverId());
//        parms.put("rate", String.valueOf(rating));
//        parms.put("review", message);


        showLoading();
        DruppRequestHandler.getInstance(new INetworkList<String>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<String>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {


//                        UIHelper.getInstance().switchActivity(BillActivity.this, RideActivity.class, null, Helper.getTotalFare(BillActivity.this), AppConstants.K_TOTAL_FARE, false);
                    if (currentPaymentMethodType == null || currentPaymentMethodType.getType() == AppConstants.PAYMENT_METHOD.WALLET) {
                        saveTransaction();
                    } else {
                        startPaymentActivity();
                    }


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
                        editRate(rateDriverModel);
                    });
                }
            }
        }, SessionManager.getAccessToken()).editRateDriver(rateDriverModel);
    }

    private void saveTransaction() {
        try {
            showLoading();
            TransactionRefStoreModel transactionRefStoreModel = new TransactionRefStoreModel();
            transactionRefStoreModel.setAmount(Float.parseFloat(fareAmount));
            transactionRefStoreModel.setCurrency(AppConstants.CURRENCY_NIGERIA);
            transactionRefStoreModel.setPaymentOption(5);
            transactionRefStoreModel.setRideId(currentRideId);
            transactionRefStoreModel.setStatus(1);
            transactionRefStoreModel.setTransactionDate(Timing.getTimeInString(System.currentTimeMillis(), Timing.TimeFormats.DD_MM_YYYY));
            transactionRefStoreModel.setTransactionId(AppUtil.generateReference());
            transactionRefStoreModel.setRidePostedByDriver(currentRideType);

            DruppRequestHandler.clearInstance();
            DruppRequestHandler.getInstance(new INetworkList<String>() {
                @Override
                public void onResponseList(Response<QualStandardResponseList<String>> response) {
                    hideLoading();
//                    UIHelper.getInstance().switchActivity(BillActivity.this, BottomSheetRideActivity.class, null, null, null, true);
                    startDriverRatingFragment();

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

                }
            }, SessionManager.getAccessToken()).saveTransaction(transactionRefStoreModel);

        } catch (Exception e) {
            e.printStackTrace();
            ;
        }

    }

    private void showPaymentCompleteDialog() {
        CompletedDialog completedDialog = new CompletedDialog(this, R.layout.custom_dialog_payment_complete);
        completedDialog.show(getSupportFragmentManager(), CompletedDialog.class.getSimpleName());

    }

    private void showRatingDialog() {

        try {
            if (getIntent().getExtras() != null) {
                RateDriverModel rateDriverModel = Helper.getRating(BillActivity.this);
                Bundle bundle = getIntent().getBundleExtra(AppConstants.K_BUNDLE);


                if (bundle != null) {
                    if (rateDriverModel != null && bundle.getBoolean(AppConstants.K_FLAG, false)) {

                        /*rateDriverDialog = RateDriverDialog.newInstance(currentRideId, driverId, currentRideType, currentDriverImage, rateDriverModel);
                        rateDriverDialog.setiDialogObserver(this);
                        rateDriverDialog.show(getSupportFragmentManager(), RateDriverDialog.class.getSimpleName());*/

                        Intent intent = new Intent(this, EndRideActivity.class);
                        intent.putExtra(AppConstants.K_RIDE_ID, currentRideId);
                        intent.putExtra(AppConstants.K_DRIVER_ID, driverId);
                        intent.putExtra(AppConstants.K_DRIVER_IMAGE, currentDriverImage);
                        intent.putExtra(AppConstants.K_RIDE_TYPE, currentRideType);
                        intent.putExtra(AppConstants.K_DRIVER_MODEL, rateDriverModel);
                        startActivity(intent);

                    }
                } else {
                    /*rateDriverDialog = RateDriverDialog.newInstance(currentRideId, driverId, currentRideType, currentDriverImage);
                    rateDriverDialog.setiDialogObserver(this);
                    rateDriverDialog.show(getSupportFragmentManager(), RateDriverDialog.class.getSimpleName());*/
                    Intent intent = new Intent(this, EndRideActivity.class);
                    intent.putExtra(AppConstants.K_RIDE_ID, currentRideId);
                    intent.putExtra(AppConstants.K_DRIVER_ID, driverId);
                    intent.putExtra(AppConstants.K_RIDE_TYPE, currentRideType);
                    intent.putExtra(AppConstants.K_DRIVER_IMAGE, currentDriverImage);
                    startActivity(intent);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.REQ_PAYMENT && resultCode == RESULT_OK) {
            Helper.removeRideId(BillActivity.this);
//            UIHelper.getInstance().switchActivity(this, BottomSheetRideActivity.class, null, null, null, true);
            showPaymentCompleteDialog();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(ratings);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onDialogResult(Object type) {
        if (rateDriverDialog != null) {
            rateDriverDialog.dismiss();
        }
        UIHelper.getInstance().switchActivity(this, BottomSheetRideActivity.class, null, null, null, true);
    }

    @Override
    public void onButtonClick() {
        startDriverRatingFragment();


    }


    @Override
    public void setUpDialogViews(View view) {
        try {
            ((TextView) view.findViewById(R.id.tv_source)).setText(jsonObject.getString(AppConstants.K_SOURCE));
            ((TextView) view.findViewById(R.id.tv_destination)).setText(jsonObject.getString(AppConstants.K_DEST));
            ((TextView) view.findViewById(R.id.tvFareAmount)).setText(rideInfo.getTotalFare());

            String method = "";
            switch (rideInfo.getPaymentOption()) {
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
            ((TextView) view.findViewById(R.id.tvPaymentMethod)).setText(method);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
