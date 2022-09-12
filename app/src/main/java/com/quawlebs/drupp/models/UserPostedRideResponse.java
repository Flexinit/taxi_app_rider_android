package com.quawlebs.drupp.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserPostedRideResponse {

    @SerializedName("ride_id")
    private String rideId;

    @SerializedName("drivers")
    private ArrayList<Integer> drivers;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private int status;

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public ArrayList<Integer> getDrivers() {
        return drivers;
    }

    public void setDrivers(ArrayList<Integer> drivers) {
        this.drivers = drivers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
