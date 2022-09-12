package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Corider {
    @SerializedName("co-rider_name")
    @Expose
    private String co_rider_name;
    @SerializedName("co-rider_id")
    @Expose
    private String co_rider_id;
    @SerializedName("source")
    @Expose
    private String co_rider_source;
    @SerializedName("destination")
    @Expose
    private String co_rider_desetination;

    public String getCo_rider_id() {return co_rider_id;}

    public void setCo_rider_id(String co_rider_id) {this.co_rider_id = co_rider_id;}

    public String getCo_rider_name() {
        return co_rider_name;
    }

    public void setCo_rider_name(String co_rider_name) {
        this.co_rider_name = co_rider_name;
    }

    public String getCo_rider_source() {
        return co_rider_source;
    }

    public void setCo_rider_source(String co_rider_source) {this.co_rider_source = co_rider_source;}

    public String getCo_rider_desetination() {
        return co_rider_desetination;
    }

    public void setCo_rider_desetination(String co_rider_desetination) {this.co_rider_desetination = co_rider_desetination;}

    public Corider(String co_rider_name, String co_rider_source, String co_rider_desetination,String co_rider_id) {
        this.co_rider_name = co_rider_name;
        this.co_rider_source = co_rider_source;
        this.co_rider_desetination = co_rider_desetination;
        this.co_rider_id = co_rider_id;
    }
}
