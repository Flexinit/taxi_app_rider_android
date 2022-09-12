package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class MatrixElement {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("duration")
    @Expose
    private ETA duration;
    @SerializedName("distance")
    @Expose
    private ETA distance;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ETA getDuration() {
        return duration;
    }

    public void setDuration(ETA duration) {
        this.duration = duration;
    }

    public ETA getDistance() {
        return distance;
    }

    public void setDistance(ETA distance) {
        this.distance = distance;
    }
}
