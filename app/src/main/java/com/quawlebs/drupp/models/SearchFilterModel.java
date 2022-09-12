package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchFilterModel {
    @SerializedName("data")
    @Expose
    private String data;
    @SerializedName("sub_category")
    @Expose
    private Integer subCategory;
    @SerializedName("sort")
    @Expose
    private Integer sort;
    @SerializedName("color")
    @Expose
    private List<FilterAttributeModel> color;
    @SerializedName("brand")
    @Expose
    private List<FilterAttributeModel> brand;


    public Integer getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(Integer subCategory) {
        this.subCategory = subCategory;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public List<FilterAttributeModel> getColor() {
        return color;
    }

    public void setColor(List<FilterAttributeModel> color) {
        this.color = color;
    }

    public List<FilterAttributeModel> getBrand() {
        return brand;
    }

    public void setBrand(List<FilterAttributeModel> brand) {
        this.brand = brand;
    }
}
