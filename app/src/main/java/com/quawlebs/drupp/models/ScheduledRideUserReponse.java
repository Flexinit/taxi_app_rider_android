package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ScheduledRideUserReponse {

    @SerializedName("day_timestamp")
    @Expose
    private String dayTimestamp;
    @SerializedName("scheduled_rides")
    @Expose
    private List<TripHistoryModel> scheduledRides;

    public String getDayTimestamp() {
        return dayTimestamp;
    }

    public void setDayTimestamp(String dayTimestamp) {
        this.dayTimestamp = dayTimestamp;
    }

    public List<TripHistoryModel> getScheduledRides() {
        return scheduledRides;
    }

    public void setScheduledRides(List<TripHistoryModel> scheduledRides) {
        this.scheduledRides = scheduledRides;
    }
}
