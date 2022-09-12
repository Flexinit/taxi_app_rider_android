package com.quawlebs.drupp.models;

import android.graphics.drawable.Drawable;

public class PaymentMethod {
    private int id;
    private String method;
    private int type;
    private String amount;
    private Drawable image;


    public PaymentMethod(int id, String method, int type, String amount) {
        this.method = method;
        this.id = id;
        this.type = type;
        this.amount = amount;
    }

    public PaymentMethod(int id, String method, int type) {
        this.method = method;
        this.id = id;
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
