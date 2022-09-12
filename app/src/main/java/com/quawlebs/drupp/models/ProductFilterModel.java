package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductFilterModel {
    @SerializedName("brand")
    @Expose
    private List<FilterAttributeModel> brands;
    @SerializedName("color")
    @Expose
    private List<FilterAttributeModel> colors;
    @SerializedName("size")
    @Expose
    private List<FilterAttributeModel> sizes;

    public List<FilterAttributeModel> getBrands() {
        return brands;
    }

    public void setBrands(List<FilterAttributeModel> brands) {
        this.brands = brands;
    }

    public List<FilterAttributeModel> getColors() {
        return colors;
    }

    public void setColors(List<FilterAttributeModel> colors) {
        this.colors = colors;
    }

    public List<FilterAttributeModel> getSizes() {
        return sizes;
    }

    public void setSizes(List<FilterAttributeModel> sizes) {
        this.sizes = sizes;
    }
}
