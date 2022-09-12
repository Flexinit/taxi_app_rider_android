
package com.quawlebs.drupp.models.ridecount;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.quawlebs.drupp.models.Response;

public class DriverRiderCountModel implements Serializable
{

    @SerializedName("response")
    @Expose
    private com.quawlebs.drupp.models.Response response;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Integer status;
    private final static long serialVersionUID = -4325263953002344551L;

    public com.quawlebs.drupp.models.Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
