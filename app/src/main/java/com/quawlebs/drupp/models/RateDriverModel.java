package com.quawlebs.drupp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RateDriverModel implements Parcelable {

    @SerializedName("ride_type")
    @Expose
    private int rideType;
    @SerializedName("rate")
    @Expose
    private Float rate;
    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("ride_id")
    @Expose
    private Integer rideId;
    @SerializedName("receiver")
    @Expose
    private Integer receiver;
    @SerializedName("is_driver_courteous")
    @Expose
    private Integer isDriverCourteous;
    @SerializedName("is_vehicle_clean")
    @Expose
    private Integer isVehicleClean;
    @SerializedName("is_driver_careful")
    @Expose
    private Integer isDriverCareful;

    @SerializedName("did you arrive on time")
    @Expose
    private Integer isDriverPunctual;



    public RateDriverModel() {

    }


    protected RateDriverModel(Parcel in) {
        rideType = in.readInt();
        if (in.readByte() == 0) {
            rate = null;
        } else {
            rate = in.readFloat();
        }
        review = in.readString();
        if (in.readByte() == 0) {
            rideId = null;
        } else {
            rideId = in.readInt();
        }
        if (in.readByte() == 0) {
            receiver = null;
        } else {
            receiver = in.readInt();
        }
        if (in.readByte() == 0) {
            isDriverCourteous = null;
        } else {
            isDriverCourteous = in.readInt();
        }
        if (in.readByte() == 0) {
            isVehicleClean = null;
        } else {
            isVehicleClean = in.readInt();
        }
        if (in.readByte() == 0) {
            isDriverCareful = null;
        } else {
            isDriverCareful = in.readInt();
        }
    }

    public static final Creator<RateDriverModel> CREATOR = new Creator<RateDriverModel>() {
        @Override
        public RateDriverModel createFromParcel(Parcel in) {
            return new RateDriverModel(in);
        }

        @Override
        public RateDriverModel[] newArray(int size) {
            return new RateDriverModel[size];
        }
    };

    public int getRideType() {
        return rideType;
    }

    public void setRideType(int rideType) {
        this.rideType = rideType;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Integer getRideId() {
        return rideId;
    }

    public void setRideId(Integer rideId) {
        this.rideId = rideId;
    }

    public Integer getReceiver() {
        return receiver;
    }

    public void setReceiver(Integer receiver) {
        this.receiver = receiver;
    }

    public Integer getIsDriverCourteous() {
        return isDriverCourteous;
    }

    public void setIsDriverCourteous(Integer isDriverCourteous) {
        this.isDriverCourteous = isDriverCourteous;
    }

    public Integer getIsVehicleClean() {
        return isVehicleClean;
    }

    public void setIsVehicleClean(Integer isVehicleClean) {
        this.isVehicleClean = isVehicleClean;
    }

    public Integer getIsDriverCareful() {
        return isDriverCareful;
    }

    public void setIsDriverCareful(Integer isDriverCareful) {
        this.isDriverCareful = isDriverCareful;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(rideType);
        if (rate == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(rate);
        }
        dest.writeString(review);
        if (rideId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(rideId);
        }
        if (receiver == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(receiver);
        }
        if (isDriverCourteous == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(isDriverCourteous);
        }
        if (isVehicleClean == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(isVehicleClean);
        }
        if (isDriverCareful == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(isDriverCareful);
        }
    }

    public Integer getIsDriverPunctual() {
        return isDriverPunctual;
    }

    public void setIsDriverPunctual(Integer isDriverPunctual) {
        this.isDriverPunctual = isDriverPunctual;
    }
}
