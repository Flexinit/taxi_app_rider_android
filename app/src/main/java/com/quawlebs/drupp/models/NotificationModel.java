package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("sender")
    @Expose
    private Integer sender;
    @SerializedName("receiver")
    @Expose
    private Integer receiver;
    @SerializedName("notification_message")
    @Expose
    private String notificationMessage;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("timestamp")
    @Expose
    private long notificationTime;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("user_ride_id")
    @Expose
    private Object userRideId;
    @SerializedName("driver_ride_id")
    @Expose
    private Object driverRideId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSender() {
        return sender;
    }

    public void setSender(Integer sender) {
        this.sender = sender;
    }

    public Integer getReceiver() {
        return receiver;
    }

    public void setReceiver(Integer receiver) {
        this.receiver = receiver;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setNotificationTime(long notificationTime) {
        this.notificationTime = notificationTime;
    }

    public long getNotificationTime() {
        return notificationTime;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getUserRideId() {
        return userRideId;
    }

    public void setUserRideId(Object userRideId) {
        this.userRideId = userRideId;
    }

    public Object getDriverRideId() {
        return driverRideId;
    }

    public void setDriverRideId(Object driverRideId) {
        this.driverRideId = driverRideId;
    }
}
