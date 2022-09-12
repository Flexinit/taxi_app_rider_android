package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Matrix {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("origin_addresses")
    @Expose
    private List<String> originAddresses;
    @SerializedName("destination_addresses")
    @Expose
    private List<String> destinationAddresses;
    @SerializedName("rows")
    @Expose
    private List<MatrixRow> rows;
}
