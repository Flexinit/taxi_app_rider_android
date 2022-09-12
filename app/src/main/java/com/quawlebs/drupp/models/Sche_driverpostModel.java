package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sche_driverpostModel {
    String source;
    String destination;
    String ride_date;
    String total_fare;
    String type_of_driver;
    String ride_id;
    String preference;
    String co_riders;
    @SerializedName("ride_type")
    @Expose
    private Integer rideType;
    @SerializedName("driver_fare")
    @Expose
    private String driverFare;
    @SerializedName("driver_name")
    @Expose
    String driverName;
    @SerializedName("vehicle_name")
    @Expose
    String vehicleName;
    @SerializedName("vehicle_model")
    @Expose
    String vehicleModel;
    @SerializedName("average_rating")
    @Expose
    float averageRating;
    @SerializedName("exterior_front")
    @Expose
    String vehicleImage;
    @SerializedName("driver_image")
    @Expose
    String driverImage;
    @SerializedName("payment_option")
    @Expose
    Integer paymentOption;

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public String getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public String getDriverImage() {
        return driverImage;
    }

    public void setDriverImage(String driverImage) {
        this.driverImage = driverImage;
    }

    public Integer getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(Integer paymentOption) {
        this.paymentOption = paymentOption;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    @SerializedName("vehicle_color")
    @Expose
    String vehicleColor;

    public String getRide_id() {
        return ride_id;
    }

    public void setRide_id(String ride_id) {
        this.ride_id = ride_id;
    }

    public String getCo_riders() {
        return co_riders;
    }

    public void setCo_riders(String co_riders) {
        this.co_riders = co_riders;
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

    public String getRide_date() {
        return ride_date;
    }

    public void setRide_date(String ride_date) {
        this.ride_date = ride_date;
    }

    public String getTotal_fare() {
        return total_fare;
    }

    public void setTotal_fare(String total_fare) {
        this.total_fare = total_fare;
    }

    public String getType_of_driver() {
        return type_of_driver;
    }

    public void setType_of_driver(String type_of_driver) {
        this.type_of_driver = type_of_driver;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }



    public String getDriverFare() {
        return driverFare;
    }

    public void setDriverFare(String driverFare) {
        this.driverFare = driverFare;
    }

    public Integer getRideType() {
        return rideType;
    }

    public void setRideType(Integer rideType) {
        this.rideType = rideType;
    }
}
