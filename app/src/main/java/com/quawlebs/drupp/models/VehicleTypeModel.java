package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VehicleTypeModel implements Serializable {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("base_fare")
    @Expose
    private String baseFare;
    @SerializedName("rate_per_km")
    @Expose
    private String ratePerKm;
    @SerializedName("per_minute_wait_charge")
    @Expose
    private String perMinuteWaitCharge;
    @SerializedName("capacity")
    @Expose
    private Integer capacity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(String baseFare) {
        this.baseFare = baseFare;
    }

    public String getRatePerKm() {
        return ratePerKm;
    }

    public void setRatePerKm(String ratePerKm) {
        this.ratePerKm = ratePerKm;
    }

    public String getPerMinuteWaitCharge() {
        return perMinuteWaitCharge;
    }

    public void setPerMinuteWaitCharge(String perMinuteWaitCharge) {
        this.perMinuteWaitCharge = perMinuteWaitCharge;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
