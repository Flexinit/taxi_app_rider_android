package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SingleProductModel {
    @SerializedName("data")
    @Expose
    private ProductInfoModel data;
    @SerializedName("in_cart")
    @Expose
    private Integer inCart;

    public Integer getInCart() {
        return inCart;
    }

    public void setData(ProductInfoModel data) {
        this.data = data;
    }

    public ProductInfoModel getData() {
        return data;
    }

    public void setInCart(Integer inCart) {
        this.inCart = inCart;
    }
}
