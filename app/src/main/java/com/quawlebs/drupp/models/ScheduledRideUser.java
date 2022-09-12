package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ScheduledRideUser {
    @SerializedName("day_timestamp")
    @Expose
    private String dayTimestamp;
    @SerializedName("scheduled_rides")
    @Expose
    private List<DriverRideModel> scheduledRides = null;

    public String getDayTimestamp() {
        return dayTimestamp;
    }

    public void setDayTimestamp(String dayTimestamp) {
        this.dayTimestamp = dayTimestamp;
    }

    public List<DriverRideModel> getScheduledRides() {
        return scheduledRides;
    }

    public void setScheduledRides(List<DriverRideModel> scheduledRides) {
        this.scheduledRides = scheduledRides;
    }

}
