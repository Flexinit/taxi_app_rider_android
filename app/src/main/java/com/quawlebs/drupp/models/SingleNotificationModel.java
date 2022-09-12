package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SingleNotificationModel {
    @SerializedName("driver_name")
    @Expose
    private String driverName;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("vehicle_name")
    @Expose
    private String vehicleName;
    @SerializedName("vehicle_number")
    @Expose
    private String vehicleNumber;
    @SerializedName("ride_date")
    @Expose
    private String rideDate;
    @SerializedName("ride_id")
    @Expose
    private Integer rideId;
    @SerializedName("driver_fare")
    @Expose
    private String driverFare;

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
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

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getRideDate() {
        return rideDate;
    }

    public void setRideDate(String rideDate) {
        this.rideDate = rideDate;
    }

    public Integer getRideId() {
        return rideId;
    }

    public void setRideId(Integer rideId) {
        this.rideId = rideId;
    }

    public String getDriverFare() {
        return driverFare;
    }

    public void setDriverFare(String driverFare) {
        this.driverFare = driverFare;
    }
}
