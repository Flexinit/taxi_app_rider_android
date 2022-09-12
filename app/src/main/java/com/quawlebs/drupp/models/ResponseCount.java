
package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseCount implements Serializable
{

    @SerializedName("rider_count")
    @Expose
    private int rideCount;
    @SerializedName("driver_count")
    @Expose
    private int DriverCount;


    public int getRideCount() {
        return rideCount;
    }

    public void setRideCount(int cashTrips) {
        this.rideCount = rideCount;
    }

    public int getDriverCount() {
        return DriverCount;
    }

    public void setDriverCount(int totalKm) {
        this.DriverCount = totalKm;
    }


}
