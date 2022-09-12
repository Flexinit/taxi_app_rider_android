package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginDataModel implements Serializable {
    @SerializedName("fb_token")
    @Expose
    private String fb_token;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("data")
    @Expose
    private UserInfo userInfo;
    @SerializedName("opt_verified")
    @Expose
    private boolean otpVerified;

    public boolean isOtpVerified() {
        return otpVerified;
    }


    public void setOtpVerified(boolean otpVerified) {
        this.otpVerified = otpVerified;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getFb_token() {
        return fb_token;
    }

    public void setFb_token(String fb_token) {
        this.fb_token = fb_token;
    }
}
