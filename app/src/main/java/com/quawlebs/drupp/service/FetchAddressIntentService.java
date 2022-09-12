package com.quawlebs.drupp.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.AppConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FetchAddressIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    protected ResultReceiver receiver;

    public FetchAddressIntentService() {
        super(FetchAddressIntentService.class.getSimpleName());
    }

    public FetchAddressIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }
        String errorMessage = "";
        receiver = intent.getParcelableExtra(AppConstants.RECEIVER);

        Location location = intent.getParcelableExtra(
                AppConstants.LOCATION_DATA_EXTRA);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);


            // Handle case where no address was found.
            if (addresses == null || addresses.size() == 0) {
                if (errorMessage.isEmpty()) {
                    errorMessage = getString(R.string.no_address_found);
                }
                deliverResultToReceiver(AppConstants.FAILURE_RESULT, errorMessage);
            } else {
                Address address = addresses.get(0);
                ArrayList<String> addressFragments = new ArrayList<String>();

                // Fetch the address lines using getAddressLine,
                // join them, and send them to the thread.
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressFragments.add(address.getAddressLine(i));
                }
                deliverResultToReceiver(AppConstants.SUCCESS_RESULT,
                        TextUtils.join(System.getProperty("line.separator"),
                                addressFragments));
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.service_not_available);

            deliverResultToReceiver(AppConstants.FAILURE_RESULT, errorMessage);
        } catch (IllegalArgumentException e) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long_used);
            deliverResultToReceiver(AppConstants.FAILURE_RESULT, errorMessage);

        }


    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.RESULT_DATA_KEY, message);
        receiver.send(resultCode, bundle);
    }

}
