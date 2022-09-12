package com.quawlebs.drupp.view.ui.scheduledRides;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.quawlebs.drupp.models.DriverRideModel;
import com.quawlebs.drupp.models.TripHistoryModel;

import java.util.List;

public class ScheduleRideDriverResponse {

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

