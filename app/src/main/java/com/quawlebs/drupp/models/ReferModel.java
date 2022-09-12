package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReferModel {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String errorResponse;


    @SerializedName("response")
    @Expose
    private response response;


    public ReferModel.response getResponse() {
        return response;
    }

    public void setResponse(ReferModel.response response) {
        this.response = response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public String getErrorResponse() {
        return errorResponse;
    }

    public void setErrorResponse(String errorResponse) {
        this.errorResponse = errorResponse;
    }

    public class response{

        @SerializedName("data")
        @Expose
        private List<data> data;

        public List<ReferModel.response.data> getData() {
            return data;
        }

        public void setData(List<ReferModel.response.data> data) {
            this.data = data;
        }

        public class data{
            @SerializedName("id")
            @Expose
            private String id;

            @SerializedName("user_id")
            @Expose
            private String user_id;

            @SerializedName("coupon_code")
            @Expose
            private String coupon_code;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getCoupon_code() {
                return coupon_code;
            }

            public void setCoupon_code(String coupon_code) {
                this.coupon_code = coupon_code;
            }
        }

    }


}
