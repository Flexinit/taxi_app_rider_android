
package com.quawlebs.drupp.models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum implements Serializable
{

    @SerializedName("coupon_number")
    @Expose
    private String couponNumber;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("u2id")
    @Expose
    private Integer u2id;
    @SerializedName("u1id")
    @Expose
    private Integer u1id;
    @SerializedName("u2type")
    @Expose
    private Integer u2type;
    @SerializedName("u1type")
    @Expose
    private Integer u1type;
    @SerializedName("connected_user")
    @Expose
    private String connectedUser;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("cid")
    @Expose
    private Integer cid;
    private final static long serialVersionUID = 7135259691352661396L;

    public String getCouponNumber() {
        return couponNumber;
    }

    public void setCouponNumber(String couponNumber) {
        this.couponNumber = couponNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getU2id() {
        return u2id;
    }

    public void setU2id(Integer u2id) {
        this.u2id = u2id;
    }

    public Integer getU1id() {
        return u1id;
    }

    public void setU1id(Integer u1id) {
        this.u1id = u1id;
    }

    public Integer getU2type() {
        return u2type;
    }

    public void setU2type(Integer u2type) {
        this.u2type = u2type;
    }

    public Integer getU1type() {
        return u1type;
    }

    public void setU1type(Integer u1type) {
        this.u1type = u1type;
    }

    public String getConnectedUser() {
        return connectedUser;
    }

    public void setConnectedUser(String connectedUser) {
        this.connectedUser = connectedUser;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

}
