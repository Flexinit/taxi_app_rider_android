package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CheckOutModel {
    @SerializedName("data")
    @Expose
    private List<CheckOutProductModel> data;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("cart_total")
    @Expose
    private float cartTotal;


    public void setCartTotal(float cartTotal) {
        this.cartTotal = cartTotal;
    }

    public float getCartTotal() {
        return cartTotal;
    }

    public List<CheckOutProductModel> getData() {
        return data;
    }

    public void setData(List<CheckOutProductModel> data) {
        this.data = data;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
