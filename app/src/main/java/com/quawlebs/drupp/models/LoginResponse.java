package com.quawlebs.drupp.models;

import com.google.gson.annotations.SerializedName;
import com.quawlebs.drupp.util.Token;

public class LoginResponse {
    @SerializedName("status")
    String status;

    @SerializedName("response")
    Token response;

    public Token getResponse() {
        return response;
    }

    public LoginResponse(Token response) {
        this.response = response;
    }

    @SerializedName("message")
    String message;

}
