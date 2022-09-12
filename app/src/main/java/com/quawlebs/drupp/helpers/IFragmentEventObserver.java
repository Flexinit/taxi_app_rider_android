package com.quawlebs.drupp.helpers;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;

public interface IFragmentEventObserver {
    void onRideSelected(int rideType);

    void onPlaceSelected(Place place, int locationType);

    void onCurrentLocationSelected();

}
