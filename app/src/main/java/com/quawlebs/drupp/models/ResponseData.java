package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseData {


    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("ride_id")
    @Expose
    private String rideID;
    @SerializedName("driver_id")
    @Expose
    private String driverId;

    @SerializedName("driver_name")
    @Expose
    private String driverName;
    @SerializedName("cab_number")
    @Expose
    private String cab_number;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("cab_type")
    @Expose
    private String cab_type;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("phone")
    @Expose
    private String driver_phone;

    @SerializedName("co-riders_name")
    @Expose
    private String co_riders_name;
    @SerializedName("co-rider_source")
    @Expose
    private String co_riders_source;
    @SerializedName("co-rider_destination")
    @Expose
    private String co_riders_destination;
    @SerializedName("otp")
    @Expose
    private Integer otp;
    @SerializedName("average_rating")
    @Expose
    private double averageRating;

    public String getCo_riders_id() {
        return co_riders_id;
    }

    public void setCo_riders_id(String co_riders_id) {
        this.co_riders_id = co_riders_id;
    }

    @SerializedName("co-rider_id")
    @Expose
    private String co_riders_id;

    public String getRideID() {
        return rideID;
    }

    public void setRideID(String rideID) {
        this.rideID = rideID;
    }

    public String getDriver_phone() {
        return driver_phone;
    }

    public void setDriver_phone(String driver_phone) {
        this.driver_phone = driver_phone;
    }

    public String getCab_number() {
        return cab_number;
    }

    public void setCab_number(String cab_number) {
        this.cab_number = cab_number;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCab_type() {
        return cab_type;
    }

    public void setCab_type(String cab_type) {
        this.cab_type = cab_type;
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

    public String getCo_riders_source() {
        return co_riders_source;
    }

    public void setCo_riders_source(String co_riders_source) {
        this.co_riders_source = co_riders_source;
    }

    public String getCo_riders_destination() {
        return co_riders_destination;
    }

    public void setCo_riders_destination(String co_riders_destination) {
        this.co_riders_destination = co_riders_destination;
    }


    public String getCo_riders_name() {
        return co_riders_name;
    }

    public ArrayList<Corider> corider_list = new ArrayList<>();

    public ArrayList<Corider> getCorider_list() {
        return corider_list;
    }

    public void setCorider_list(ArrayList<Corider> corider_list) {
        this.corider_list = corider_list;
    }

    public void setCo_riders_name(String co_riders_name) {
        this.co_riders_name = co_riders_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName != null ? driverName : "";
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public double getAverageRating() {
        return averageRating;
    }
    /*Notification type 3*/


}


