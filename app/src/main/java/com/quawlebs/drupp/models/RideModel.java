package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RideModel {
    @SerializedName("ride_id")
    @Expose
    private Integer rideId;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("source_latitude")
    @Expose
    private String sourceLatitude;
    @SerializedName("source_longitude")
    @Expose
    private String sourceLongitude;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("destination_latitude")
    @Expose
    private String destinationLatitude;
    @SerializedName("passengers_preference")
    @Expose
    private Integer passengersPreference;
    @SerializedName("destination_longitude")
    @Expose
    private String destinationLongitude;
    @SerializedName("ride_type")
    @Expose
    private Integer rideType;
    @SerializedName("ride_date")
    @Expose
    private String rideDate;
    @SerializedName("vehicle_type")
    @Expose
    private Integer vehicleType;
    @SerializedName("ride_posted_by_driver")
    @Expose
    private Integer ridePostedByDriver;

    public Integer getRidePostedByDriver() {
        return ridePostedByDriver;
    }

    public void setRidePostedByDriver(Integer ridePostedByDriver) {
        this.ridePostedByDriver = ridePostedByDriver;
    }

    public Integer getRideId() {
        return rideId;
    }

    public void setRideId(Integer rideId) {
        this.rideId = rideId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceLatitude() {
        return sourceLatitude;
    }

    public void setSourceLatitude(String sourceLatitude) {
        this.sourceLatitude = sourceLatitude;
    }

    public String getSourceLongitude() {
        return sourceLongitude;
    }

    public void setSourceLongitude(String sourceLongitude) {
        this.sourceLongitude = sourceLongitude;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(String destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public Integer getPassengersPreference() {
        return passengersPreference;
    }

    public void setPassengersPreference(Integer passengersPreference) {
        this.passengersPreference = passengersPreference;
    }

    public String getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(String destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }

    public Integer getRideType() {
        return rideType;
    }

    public void setRideType(Integer rideType) {
        this.rideType = rideType;
    }

    public String getRideDate() {
        return rideDate;
    }

    public void setRideDate(String rideDate) {
        this.rideDate = rideDate;
    }

    public Integer getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(Integer vehicleType) {
        this.vehicleType = vehicleType;
    }
}
