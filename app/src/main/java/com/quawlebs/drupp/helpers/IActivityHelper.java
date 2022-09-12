package com.quawlebs.drupp.helpers;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

public interface IActivityHelper {


    LatLng getSource();

    LatLng getDestination();

    void onNoDriverFound();

    int setPickerStatus();

    void changeType(int rideType, int poolOption, int rideOption);

    void changeVehicleType(int vehicleType);

    HashMap<String, Object> getData();

    void post();

    void cancelAPI();

    void timestamp(String stamp, String fare);
}




