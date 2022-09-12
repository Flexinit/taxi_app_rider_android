package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DriversLocation {

    public ArrayList<DriversLocationData> getData() {
        return data;
    }

    public void setData(ArrayList<DriversLocationData> data) {
        this.data = data;
    }

    @SerializedName("data")
    @Expose
    ArrayList<DriversLocationData> data;
}
