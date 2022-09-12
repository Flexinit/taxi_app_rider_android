package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShopTransactionModel {

    @SerializedName("products")
    @Expose
    private List<CheckOutProductModel> products;
    @SerializedName("transaction_date")
    @Expose
    private String transactionDate;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("payment_option")
    @Expose
    private Integer paymentOption;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("shipping_address_id")
    @Expose
    private Integer shippingAddressId;
    @SerializedName("status")
    @Expose
    private Integer status;
    private float amount;
    private String reference;

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<CheckOutProductModel> getProducts() {
        return products;
    }

    public void setProducts(List<CheckOutProductModel> products) {
        this.products = products;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(Integer paymentOption) {
        this.paymentOption = paymentOption;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getShippingAddressId() {
        return shippingAddressId;
    }

    public void setShippingAddressId(Integer shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
