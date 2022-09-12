package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BusBookingModel {

    @SerializedName("bus_ride_id")
    @Expose
    private String busRideId;
    @SerializedName("no_of_seats")
    @Expose
    private String noOfSeats;
    @SerializedName("names")
    @Expose
    private List<NameModel> name;

    public String getBusRideId() {
        return busRideId;
    }

    public void setBusRideId(String busRideId) {
        this.busRideId = busRideId;
    }

    public String getNoOfSeats() {
        return noOfSeats;
    }

    public void setNoOfSeats(String noOfSeats) {
        this.noOfSeats = noOfSeats;
    }

    public List<NameModel> getName() {
        return name;
    }

    public void setName(List<NameModel> name) {
        this.name = name;
    }
}
