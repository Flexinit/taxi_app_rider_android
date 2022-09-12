package com.quawlebs.drupp.view.ui.ride;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.Helper;
import com.quawlebs.drupp.helpers.IDateDialogResponseObserver;
import com.quawlebs.drupp.helpers.ITimeDialogResponseObserver;
import com.quawlebs.drupp.helpers.Timing;
import com.quawlebs.drupp.view.ui.base.MainBaseFragment;
import com.quawlebs.drupp.view.ui.dialog.DateDialog;
import com.quawlebs.drupp.view.ui.dialog.TimeDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class RideLater extends MainBaseFragment implements IDateDialogResponseObserver, ITimeDialogResponseObserver {
    public SimpleDateFormat simpleDateFormat;
    private EditText dateTime, basefare;
    public Date finalDT;
    private long value;
    private int day, month, year, hour, minute;
    private int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;
    private DateDialog dateDialog;
    private TimeDialog timeDialog;


    @Override
    protected void initViewsForFragment(View view) {
        basefare = view.findViewById(R.id.baseFare);
        dateTime = view.findViewById(R.id.etPickupTime);
    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_ride_later, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dateTime.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showDateDialog();
                //showDateTimeDialog();
            }
        });
        dateTime.setOnClickListener(v -> {
            showDateDialog();
            //            showDateTimeDialog();
        });

        view.findViewById(R.id.bt_Confirm_booking).setOnClickListener(v -> {
            //Save date and Fare
            if (dateTime.getText().toString().trim().isEmpty()) {
                hideKeyboard();
                showMessage(R.string.please_select_date_and_time);
                return;
            }
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(AppConstants.K_BASE_FARE_RL, basefare.getText().toString().trim());
            hashMap.put(AppConstants.K_TIME_RL, String.valueOf(value));
            Helper.saveRideLaterDetails(hashMap, getmActivity());
            if (getmActivity() instanceof BottomSheetRideActivity) {
                BottomSheetRideActivity rideActivity = (BottomSheetRideActivity) getmActivity();
                rideActivity.timestamp(String.valueOf(value), basefare.getText().toString());
                try {
                    rideActivity.post();
                    RideType rideType = new RideType();
                    rideType.setiActivityHelper((BottomSheetRideActivity) getmActivity());
                    ((BottomSheetRideActivity) getmActivity()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fl_bottom_sheet_home, rideType)
                            .addToBackStack(null)
                            .commit();
                } catch (NullPointerException e) {
                    showAlertDialog(R.layout.dialog_app_error, AppConstants.NotificationType.APP_ERROR);
                }
            }

            //TODO CHANGE IMPLEMENTATION


        });
    }


    private void showDateDialog() {
        Bundle bundle = new Bundle();
        bundle.putLong(AppConstants.K_MIN_DATE, System.currentTimeMillis() + 1000);
        bundle.putLong(AppConstants.K_MAX_DATE, System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
        dateDialog = DateDialog.newInstance(bundle);
        dateDialog.setiTimeDialogResponseObserver(this);
        dateDialog.setiDateDialogResponseObserver(this);
        dateDialog.show(getFragmentManager(), DateDialog.class.getSimpleName());
    }


    private void showRideBookedDailog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_ride_sheduled, null);
        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(getContext());
        final AlertDialog alertDialog = alertDialogbuilder.create();
        alertDialog.setView(alertLayout);
        // alertDialog.setContentView(R.layout.custom_alert_dailog);
        alertDialog.show();
        //alertDialog.getWindow().setLayout(1000,900);
    }


    @Override
    public void onDateSelected(int year, int month, int dayOfMonth) {
        yearFinal = year;
        monthFinal = month;
        dayFinal = dayOfMonth;
    }


    @Override
    public void onTimeSelected(int hourOfDay, int minute) {
        hourFinal = hourOfDay;
        minuteFinal = minute;
        String dateToShow = "Date: " + dayFinal + "/" + monthFinal + "/" + yearFinal + " Time: " + hourFinal + ":" + checkDigit(minuteFinal);
        String dateToSend = dayFinal + "-" + monthFinal + "-" + yearFinal + " " + hourFinal + ":" + minuteFinal;

        value = Timing.getTimeInUnixStamp(dateToSend, Timing.TimeFormats.CUSTOM_DATE_TIME);
        dateTime.setText(dateToShow);
    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    @Override
    public void showAlertDialog(int resId) {

    }
}
