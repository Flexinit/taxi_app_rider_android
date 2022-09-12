
package com.quawlebs.drupp.models.getlocation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Response implements Serializable
{

    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("latitude")
    @Expose
    private Double latitude;

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double riderCount) {
        this.longitude = riderCount;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double driverCount) {
        this.latitude = latitude;
    }

}
