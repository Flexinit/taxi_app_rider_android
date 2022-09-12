package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecentSupportModel {

    @SerializedName("ride_id")
    @Expose
    private String ride_id;

    @SerializedName("source")
    @Expose
    private String source;

    @SerializedName("destination")
    @Expose
    private String destination;

    @SerializedName("ride_date")
    @Expose
    private String ride_date;

    @SerializedName("name")
    @Expose
    private String driver_name;

    @SerializedName("vehicle_name")
    @Expose
    private String vehicle_name;

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getVehicle_name() {
        return vehicle_name;
    }

    public void setVehicle_name(String vehicle_name) {
        this.vehicle_name = vehicle_name;
    }

    public String getRide_id() {
        return ride_id;
    }

    public void setRide_id(String ride_id) {
        this.ride_id = ride_id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getRide_date() {
        return ride_date;
    }

    public void setRide_date(String ride_date) {
        this.ride_date = ride_date;
    }
}
