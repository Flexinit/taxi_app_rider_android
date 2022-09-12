package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductSearchModel {
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_images")
    @Expose
    private List<Object> productImages;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<Object> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<Object> productImages) {
        this.productImages = productImages;
    }
}
