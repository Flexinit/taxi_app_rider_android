package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TripHistortDetailModel {
    String ride_date;
    String ride_time;
    String source;
    String destination;
    String total_fare;

    @SerializedName("rate")
    @Expose
    Integer rate;
    @SerializedName("driver_name")
    @Expose
    String driverName;
    @SerializedName("exterior_front")
    @Expose
    String vehicleImage;
    @SerializedName("driver_image")
    @Expose
    String driverImage;
    @SerializedName("payment_option")
    @Expose
    Integer paymentOption;
    @SerializedName("vehicle_model")
    @Expose
    String vehicleModel;
    @SerializedName("vehicle_color")
    @Expose
    String vehicleColor;
    @SerializedName("vehicle_brand")
    @Expose
    String vehicleBrand;

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
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

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public String getRide_date() {
        return ride_date;
    }

    public void setRide_date(String ride_date) {
        this.ride_date = ride_date;
    }

    public String getRide_time() {
        return ride_time;
    }

    public void setRide_time(String ride_time) {
        this.ride_time = ride_time;
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

    public String getTotal_fare() {
        return total_fare;
    }

    public void setTotal_fare(String total_fare) {
        this.total_fare = total_fare;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }
}
