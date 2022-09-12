package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverPostRide {

    @SerializedName("driver_ride_id")
    @Expose
    private String driverPost_rideId;
    @SerializedName("driver_name")
    @Expose
    private String driverPost_driverName;
    @SerializedName("source")
    @Expose
    private String driverPost_source;
    @SerializedName("destination")
    @Expose
    private String driverPost_destination;
    @SerializedName("vehicle_name")
    @Expose
    private String driverPost_vehicle_name;
    @SerializedName("vehicle_number")
    @Expose
    private String driverPost_vehicle_number;
    @SerializedName("ride_date")
    @Expose
    private String driverPost_ride_date;
    @SerializedName("total_fare")
    @Expose
    private String driverPost_total_fare;
    @SerializedName("ride_type")
    @Expose
    private String rideType;

    public String getRideType() {
        return rideType;
    }

    public void setRideType(String rideType) {
        this.rideType = rideType;
    }

    public String getDriverPost_rideId() {
        return driverPost_rideId;
    }

    public void setDriverPost_rideId(String driverPost_rideId) {
        this.driverPost_rideId = driverPost_rideId;
    }

    public String getDriverPost_driverName() {
        return driverPost_driverName;
    }

    public void setDriverPost_driverName(String driverPost_driverName) {
        this.driverPost_driverName = driverPost_driverName;
    }

    public String getDriverPost_source() {
        return driverPost_source;
    }

    public void setDriverPost_source(String driverPost_source) {
        this.driverPost_source = driverPost_source;
    }

    public String getDriverPost_destination() {
        return driverPost_destination;
    }

    public void setDriverPost_destination(String driverPost_destination) {
        this.driverPost_destination = driverPost_destination;
    }

    public String getDriverPost_vehicle_name() {
        return driverPost_vehicle_name;
    }

    public void setDriverPost_vehicle_name(String driverPost_vehicle_name) {
        this.driverPost_vehicle_name = driverPost_vehicle_name;
    }

    public String getDriverPost_vehicle_number() {
        return driverPost_vehicle_number;
    }

    public void setDriverPost_vehicle_number(String driverPost_vehicle_number) {
        this.driverPost_vehicle_number = driverPost_vehicle_number;
    }

    public String getDriverPost_ride_date() {
        return driverPost_ride_date;
    }

    public void setDriverPost_ride_date(String driverPost_ride_date) {
        this.driverPost_ride_date = driverPost_ride_date;
    }

    public String getDriverPost_total_fare() {
        return driverPost_total_fare;
    }

    public void setDriverPost_total_fare(String driverPost_total_fare) {
        this.driverPost_total_fare = driverPost_total_fare;
    }
}