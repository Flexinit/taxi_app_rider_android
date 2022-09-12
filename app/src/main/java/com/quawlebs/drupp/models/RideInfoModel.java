package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RideInfoModel {
    @SerializedName("cLatitude")
    @Expose
    private double cLatitude;
    @SerializedName("cLongitude")
    @Expose
    private double cLongitude;
    @SerializedName("sLatitude")
    @Expose
    private double sLatitude;
    @SerializedName("sLongitude")
    @Expose
    private double sLongitude;
    @SerializedName("dLatitude")
    @Expose
    private double dLatitude;
    @SerializedName("dLongitude")
    @Expose
    private double dLongitude;
    @SerializedName("driverName")
    @Expose
    private String driverName;
    @SerializedName("rideID")
    @Expose
    private int rideID;
    @SerializedName("riderName")
    @Expose
    private String riderName;
    @SerializedName("bearing")
    @Expose
    private double bearing;

    public double getBearing() {
        return bearing;
    }

    public double getcLatitude() {
        return cLatitude;
    }

    public void setcLatitude(double cLatitude) {
        this.cLatitude = cLatitude;
    }

    public double getcLongitude() {
        return cLongitude;
    }

    public void setcLongitude(double cLongitude) {
        this.cLongitude = cLongitude;
    }

    public double getsLatitude() {
        return sLatitude;
    }

    public void setsLatitude(double sLatitude) {
        this.sLatitude = sLatitude;
    }

    public double getsLongitude() {
        return sLongitude;
    }

    public void setsLongitude(double sLongitude) {
        this.sLongitude = sLongitude;
    }

    public double getdLatitude() {
        return dLatitude;
    }

    public void setdLatitude(double dLatitude) {
        this.dLatitude = dLatitude;
    }

    public double getdLongitude() {
        return dLongitude;
    }

    public void setdLongitude(double dLongitude) {
        this.dLongitude = dLongitude;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public int getRideID() {
        return rideID;
    }

    public void setRideID(int rideID) {
        this.rideID = rideID;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public void setBearing(double bearing) {
        this.bearing = bearing;
    }
}
