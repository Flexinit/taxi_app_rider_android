package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LogCustomer {
    @SerializedName("start_time")
    @Expose
    private Integer startTime;
    @SerializedName("time_spent")
    @Expose
    private Integer timeSpent;
    @SerializedName("attempts")
    @Expose
    private Integer attempts;
    @SerializedName("errors")
    @Expose
    private Integer errors;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("mobile")
    @Expose
    private Boolean mobile;
    @SerializedName("input")
    @Expose
    private List<Object> input = null;
    @SerializedName("history")
    @Expose
    private List<History> history = null;

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public Integer getErrors() {
        return errors;
    }

    public void setErrors(Integer errors) {
        this.errors = errors;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getMobile() {
        return mobile;
    }

    public void setMobile(Boolean mobile) {
        this.mobile = mobile;
    }

    public List<Object> getInput() {
        return input;
    }

    public void setInput(List<Object> input) {
        this.input = input;
    }

    public List<History> getHistory() {
        return history;
    }

    public void setHistory(List<History> history) {
        this.history = history;
    }
}
