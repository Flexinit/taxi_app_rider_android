
package com.quawlebs.drupp.models.ridecount;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response implements Serializable
{

    @SerializedName("rider_count")
    @Expose
    private Integer riderCount;
    @SerializedName("driver_count")
    @Expose
    private Integer driverCount;
    private final static long serialVersionUID = -4703447647721936596L;

    public Integer getRiderCount() {
        return riderCount;
    }

    public void setRiderCount(Integer riderCount) {
        this.riderCount = riderCount;
    }

    public Integer getDriverCount() {
        return driverCount;
    }

    public void setDriverCount(Integer driverCount) {
        this.driverCount = driverCount;
    }

}
