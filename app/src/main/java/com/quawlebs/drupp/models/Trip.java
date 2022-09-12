package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Trip {

    private String SCity, DCity;
    private int id;
    @SerializedName("time")
    @Expose
    private long time;

    public Trip(String SCity, String DCity, int id) {
        this.SCity = SCity;
        this.DCity = DCity;
        this.id = id;
    }

    public String getSCity() {
        return SCity;
    }

    public void setSCity(String SCity) {
        this.SCity = SCity;
    }

    public String getDCity() {
        return DCity;
    }

    public void setDCity(String DCity) {
        this.DCity = DCity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "SCity='" + SCity + '\'' +
                ", DCity='" + DCity + '\'' +
                ", id=" + id +
                '}';
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
