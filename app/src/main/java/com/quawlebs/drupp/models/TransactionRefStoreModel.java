package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionRefStoreModel {
    @SerializedName("ride_id")
    @Expose
    private Integer rideId;
    @SerializedName("transaction_date")
    @Expose
    private String transactionDate;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("amount")
    @Expose
    private float amount;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("ride_posted_by_driver")
    @Expose
    private Integer ridePostedByDriver;
    @SerializedName("payment_option")
    @Expose
    private Integer paymentOption;
    @SerializedName("status")
    @Expose
    private Integer status;

    public Integer getRideId() {
        return rideId;
    }

    public void setRideId(Integer rideId) {
        this.rideId = rideId;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getRidePostedByDriver() {
        return ridePostedByDriver;
    }

    public void setRidePostedByDriver(Integer ridePostedByDriver) {
        this.ridePostedByDriver = ridePostedByDriver;
    }

    public Integer getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(Integer paymentOption) {
        this.paymentOption = paymentOption;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
