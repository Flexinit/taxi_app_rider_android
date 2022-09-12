package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardInfoModel {
    @SerializedName("user_details")
    @Expose
    private UserInfo userDetails;

    @Expose
    @SerializedName("completed_rides")
    private Integer completedRides;

    @Expose
    @SerializedName("scheduled_rides")
    private Integer scheduledRides;

    @Expose
    @SerializedName("total_rides")
    private Integer totalRides;

    @Expose
    @SerializedName("cancelled_rides")
    private Integer cancelledRides;

    public Integer getTotalRides() {
        return totalRides;
    }

    public void setTotalRides(Integer totalRides) {
        this.totalRides = totalRides;
    }

    public Integer getCancelledRides() {
        return cancelledRides;
    }

    public void setCancelledRides(Integer cancelledRides) {
        this.cancelledRides = cancelledRides;
    }

    public UserInfo getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserInfo userDetails) {
        this.userDetails = userDetails;
    }

    public Integer getCompletedRides() {
        return completedRides;
    }

    public void setCompletedRides(Integer completedRides) {
        this.completedRides = completedRides;
    }

    public Integer getScheduledRides() {
        return scheduledRides;
    }

    public void setScheduledRides(Integer scheduledRides) {
        this.scheduledRides = scheduledRides;
    }
}
