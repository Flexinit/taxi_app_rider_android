package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiveLocationModel {
    @SerializedName("data")
    @Expose
    private LocationDataModel data;
    @SerializedName("eta")
    @Expose
    private EtaModel eta;

    public LocationDataModel getData() {
        return data;
    }

    public void setData(LocationDataModel data) {
        this.data = data;
    }

    public EtaModel getEta() {
        return eta;
    }

    public void setEta(EtaModel eta) {
        this.eta = eta;
    }
}
