package com.quawlebs.drupp.view.ui.dialog;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.jraska.falcon.Falcon;
import com.quawlebs.drupp.models.BusBookingModel;
import com.quawlebs.drupp.models.NameModel;
import com.quawlebs.drupp.models.UserInfo;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.IDialogResponseObserver;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.helpers.Timing;
import com.quawlebs.drupp.view.ui.base.DialogBaseFragment;

import java.util.ArrayList;

import retrofit2.Response;

public class BusBookingDialog extends DialogBaseFragment implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerSeats;
    private String[] noOfSeats;
    private TextView mBookingPassenger;
    private EditText passenger1, passenger2;
    private String busNumber;
    private long departureTime;
    private Button bookRide;
    private ArrayList<EditText> editTextArrayList = new ArrayList<>();
    private int noOfPassenger = 1;
    private String bookingUserName = "";
    private String source = "", destination = "";
    private IDialogResponseObserver iDialogResponseObserver;


    public void setiDialogResponseObserver(IDialogResponseObserver iDialogResponseObserver) {
        this.iDialogResponseObserver = iDialogResponseObserver;
    }

    public static BusBookingDialog newInstance(Bundle bundle) {
        BusBookingDialog busBookingDialog = new BusBookingDialog();
        busBookingDialog.setArguments(bundle);
        return busBookingDialog;
    }

    @Override
    protected void initViewsForFragment(View view) {

    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.dialog_seat_booking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bookRide = view.findViewById(R.id.btn_book_ride);
        passenger1 = view.findViewById(R.id.et_name_1);
        passenger2 = view.findViewById(R.id.et_name_2);
        mBookingPassenger = view.findViewById(R.id.tv_default_passenger);

        if (SessionManager.getInstance().getUserModel() != null) {
            UserInfo userInfo = SessionManager.getInstance().loadUser(getmContext()).getUserInfo();
            if (userInfo != null) {
                bookingUserName = getString(R.string.full_name, userInfo.getFirstName(), userInfo.getLastName());
                mBookingPassenger.setText(bookingUserName);
            }
        }
        editTextArrayList.add(passenger1);
        editTextArrayList.add(passenger2);


        spinnerSeats = view.findViewById(R.id.spinner_seats);
        spinnerSeats.setOnItemSelectedListener(this);

        if (getArguments() != null) {
            departureTime = getArguments().getLong(AppConstants.K_BUS_DEPARTURE);
            busNumber = getArguments().getString(AppConstants.K_BUS_NUMBER);
            source = getArguments().getString(AppConstants.K_SOURCE);
            destination = getArguments().getString(AppConstants.K_DEST);
        }
        noOfSeats = getResources().getStringArray(R.array.seats);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getmActivity(), android.R.layout.simple_spinner_dropdown_item, noOfSeats);
        spinnerSeats.setAdapter(spinnerAdapter);

        bookRide.setOnClickListener(v -> {

            if (isValidate()) {
                BusBookingModel busBookingModel = new BusBookingModel();
                ArrayList<NameModel> names = new ArrayList<>();
                if (getArguments() != null) {
                    busBookingModel.setBusRideId(String.valueOf(getArguments().getInt(AppConstants.K_BUS_RIDE_ID)));
                }
                busBookingModel.setNoOfSeats(String.valueOf(noOfPassenger));

                if (noOfPassenger == 1) {
                    addDefaultPassenger(names);
                } else {
                    addDefaultPassenger(names);
                    for (int i = 0; i < noOfPassenger - 1; i++) {
                        NameModel nameModel = new NameModel();
                        nameModel.setName(editTextArrayList.get(i).getText().toString().trim());
                        names.add(nameModel);
                    }
                }

                hideKeyboard();
                busBookingModel.setName(names);
                bookBusRide(busBookingModel);

            }
        });

    }

    private void addDefaultPassenger(ArrayList<NameModel> names) {
        NameModel nameModel = new NameModel();
        nameModel.setName(mBookingPassenger.getText().toString().trim());
        names.add(nameModel);
    }

    private boolean isValidate() {
        switch (noOfPassenger) {
            case 2:
                if (passenger1.getText().toString().trim().isEmpty()) {
                    showMessage(R.string.please_enter_name_of_passengers);
                    return false;
                }
                break;
            case 3:
                if (passenger1.getText().toString().trim().isEmpty()) {
                    showMessage(R.string.please_enter_name_of_passengers);
                    return false;
                }
                if (passenger2.getText().toString().trim().isEmpty()) {
                    showMessage(R.string.please_enter_name_of_passengers);
                    return false;
                }
                break;
            case 4:
                if (passenger1.getText().toString().trim().isEmpty()) {
                    showMessage(R.string.please_enter_name_of_passengers);
                    return false;
                }
                if (passenger2.getText().toString().trim().isEmpty()) {
                    showMessage(R.string.please_enter_name_of_passengers);
                    return false;
                }
                break;
        }
        return true;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
            switch (Integer.valueOf(noOfSeats[position])) {
                case 1:
                    noOfPassenger = 2;
                    passenger1.setVisibility(View.VISIBLE);
                    passenger2.setVisibility(View.GONE);
                    break;
                case 2:
                    noOfPassenger = 3;
                    passenger1.setVisibility(View.VISIBLE);
                    passenger2.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    noOfPassenger = 3;
                    passenger2.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    noOfPassenger = 4;
                    passenger2.setVisibility(View.VISIBLE);
                    break;
            }
        } else {
            passenger1.setVisibility(View.GONE);
            passenger2.setVisibility(View.GONE);
            noOfPassenger = 1;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void bookBusRide(BusBookingModel busBookingModel) {
        //Hide Keyboard
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        showDialogProgressBar();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<String>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<String>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    showSuccessDialog();
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
                showErrorPrompt(R.string.something_went_wrong);
            }
        }, SessionManager.getAccessToken()).bookBusRide(busBookingModel);
    }


    private void showSuccessDialog() {

        showAlertDialog(R.layout.dialog_booking_done, AppConstants.NotificationType.APP_ERROR);
        if (mAlertDialog != null) {
            TextView mSource = mAlertDialog.findViewById(R.id.tv_source);
            TextView mDestination = mAlertDialog.findViewById(R.id.tv_destination);
            TextView mBookingUser = mAlertDialog.findViewById(R.id.tv_booking_user);
            TextView mBusNumber = mAlertDialog.findViewById(R.id.tv_bus_number);
            TextView mPassengers = mAlertDialog.findViewById(R.id.tv_passenger);
            TextView mDepartureTime = mAlertDialog.findViewById(R.id.tv_departure_time);

            mBusNumber.setText(String.valueOf(busNumber));
            mBookingUser.setText(bookingUserName);
            StringBuilder passengeStringBuilder = new StringBuilder();
            passengeStringBuilder.append(mBookingPassenger.getText().toString().trim());
            if (noOfPassenger == 2) {
                passengeStringBuilder.append(",\n").append(passenger1.getText().toString().trim());

            } else if (noOfPassenger == 3) {
                passengeStringBuilder.append(",\n").append(passenger1.getText().toString().trim());
                passengeStringBuilder.append(",\n").append(passenger2.getText().toString().trim());
            }
            mPassengers.setText(getString(R.string.passenger_in_ride, passengeStringBuilder.toString()));
            mDepartureTime.setText(Timing.getTimeInString(departureTime, Timing.TimeFormats.HH_12));
            mSource.setText(source);
            mDestination.setText(destination);

            //  ImageView checkMarkAnimation = mAlertDialog.findViewById(R.id.check_animation);
            //  checkMarkAnimation.setVisibility(View.VISIBLE);
            //((Animatable) checkMarkAnimation.getDrawable()).start();
            mAlertDialog.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
                // iDialogResponseObserver.onSuccess();
                hideAlertDialog();
                dismiss();
            });
            mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                try {
                    if (ContextCompat.checkSelfPermission(getmActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        askPermission();

                    } else {
                        Bitmap bitmap = Falcon.takeScreenshotBitmap(getmActivity());
                        String path = MediaStore.Images.Media.insertImage(getmActivity().getContentResolver(), bitmap, "I want to share ticket", null);
                        Uri uri = Uri.parse(path);
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        shareIntent.setType("image/*");
                        startActivity(Intent.createChooser(shareIntent, "Sharing Ticket"));

                        //iDialogResponseObserver.onSuccess();
                        hideAlertDialog();
                        dismiss();
                    }


                } catch (Exception e) {
                    showMessage(R.string.something_went_wrong_with_sharing);
                }


            });
        }
    }

    private void askPermission() {
        // Permission is not granted
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                getmActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) && ContextCompat.checkSelfPermission(getmActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            ActivityCompat.requestPermissions(
                    getmActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    AppConstants.REQUEST_WRITE_STORAGE);
        } else {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(
                    getmActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    AppConstants.REQUEST_WRITE_STORAGE);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }


    @Override
    public void showAlertDialog(int resId) {

    }
}
