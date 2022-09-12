package com.quawlebs.drupp.models.rxbus;


import com.google.android.libraries.places.api.model.Place;

public class LocationPicker {
    private int locationType;
    private String locationTitle;
    private String nextField;
    private Place sourcePlace, destinationPlace;

    public LocationPicker() {

    }

    public LocationPicker(Place sourcePlace, Place destinationPlace, int locationType, String locationTitle) {
        this.locationType = locationType;
        this.locationTitle = locationTitle;
        this.sourcePlace = sourcePlace;
        this.destinationPlace = destinationPlace;

    }

    public LocationPicker(int locationType, String nextField, String locationTitle) {
        this.locationType = locationType;
        this.locationTitle = locationTitle;
        this.nextField = nextField;
    }

    public void setSourcePlace(Place sourcePlace) {
        this.sourcePlace = sourcePlace;
    }

    public void setDestinationPlace(Place destinationPlace) {
        this.destinationPlace = destinationPlace;
    }

    public String getNextField() {
        return nextField;
    }

    public Place getSourcePlace() {
        return sourcePlace;
    }

    public Place getDestinationPlace() {
        return destinationPlace;
    }

    public int getLocationType() {
        return locationType;
    }

    public void setLocationType(int locationType) {
        this.locationType = locationType;
    }

    public String getLocationTitle() {
        return locationTitle;
    }

    public void setLocationTitle(String locationTitle) {
        this.locationTitle = locationTitle;
    }
}
