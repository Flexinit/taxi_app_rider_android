package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriversLocationData {
    @SerializedName("driver_id")
    @Expose
    private String driverId;
    @SerializedName("driver_latitude")
    @Expose
    private String driverLatitude;
    @SerializedName("driver_longitude")
    @Expose
    private String driverLongitude;
    @SerializedName("distance")
    @Expose
    private String distance;

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverLatitude() {
        return driverLatitude;
    }

    public void setDriverLatitude(String driverLatitude) {
        this.driverLatitude = driverLatitude;
    }

    public String getDriverLongitude() {
        return driverLongitude;
    }

    public void setDriverLongitude(String driverLongitude) {
        this.driverLongitude = driverLongitude;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
