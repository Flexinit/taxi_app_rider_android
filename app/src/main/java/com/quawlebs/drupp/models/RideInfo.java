package com.quawlebs.drupp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RideInfo implements Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("ride_id")
    @Expose
    private Integer rideId;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("source_latitude")
    @Expose
    private String sourceLatitude;
    @SerializedName("source_longitude")
    @Expose
    private String sourceLongitude;
    @SerializedName("destination_latitude")
    @Expose
    private String destinationLatitude;
    @SerializedName("destination_longitude")
    @Expose
    private String destinationLongitude;
    @SerializedName("ride_type")
    @Expose
    private Integer rideType;
    @SerializedName("passengers_preference")
    @Expose
    private Integer passengersPreference;
    @SerializedName("ride_option")
    @Expose
    private Integer rideOption;
    @SerializedName("ride_date")
    @Expose
    private String rideDate;
    @SerializedName("ride_time")
    @Expose
    private Object rideTime;
    @SerializedName("requested_at")
    @Expose
    private Object requestedAt;
    @SerializedName("start_time")
    @Expose
    private Object startTime;
    @SerializedName("complete_time")
    @Expose
    private Object completeTime;
    @SerializedName("duration")
    @Expose
    private Object duration;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("driver_id")
    @Expose
    private Integer driverId;
    @SerializedName("vehicle_type")
    @Expose
    private Integer vehicleType;
    @SerializedName("base_fare")
    @Expose
    private String baseFare;
    @SerializedName("per_km")
    @Expose
    private String perKm;
    @SerializedName("per_minute_wait_charge")
    @Expose
    private String perMinuteWaitCharge;
    @SerializedName("vehicle_name")
    @Expose
    private String vehicleName;
    @SerializedName("vehicle_number")
    @Expose
    private String vehicleNumber;
    @SerializedName("cancel_reason")
    @Expose
    private String cancelReason;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("total_fare")
    @Expose
    private String totalFare;
    @SerializedName("posted_by_driver")
    @Expose
    private Integer postedByDriver;
    @SerializedName("driver_ride_id")
    @Expose
    private Integer driverRideId;
    @SerializedName("user_fare")
    @Expose
    private String userFare;
    @SerializedName("driver_fare")
    @Expose
    private Object driverFare;
    @SerializedName("unique_key")
    @Expose
    private Object uniqueKey;
    @SerializedName("drupp_earnings")
    @Expose
    private Object druppEarnings;
    @SerializedName("driver_name")
    @Expose
    private String driverName;
    @SerializedName("country_code")
    @Expose
    private Integer countryCode;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("average_rating")
    @Expose
    private Double averageRating;
    @SerializedName("vehicle_type_id")
    @Expose
    private Integer vehicleTypeId;
    @SerializedName("license_number")
    @Expose
    private String licenseNumber;
    @SerializedName("vehicle_model")
    @Expose
    private String vehicleModel;
    @SerializedName("vehicle_brand")
    @Expose
    private String vehicleBrand;
    @SerializedName("chassis_number")
    @Expose
    private String chassisNumber;
    @SerializedName("vehicle_color")
    @Expose
    private String vehicleColor;
    @SerializedName("authenticated_license")
    @Expose
    private Integer authenticatedLicense;
    @SerializedName("experienced")
    @Expose
    private Integer experienced;
    @SerializedName("experience_years")
    @Expose
    private Object experienceYears;
    @SerializedName("functional_ac")
    @Expose
    private Integer functionalAc;
    @SerializedName("reason_for_no_ac")
    @Expose
    private Object reasonForNoAc;
    @SerializedName("vehicle_condition")
    @Expose
    private Integer vehicleCondition;
    @SerializedName("remittances")
    @Expose
    private Object remittances;
    @SerializedName("reason_for_applying")
    @Expose
    private Object reasonForApplying;
    @SerializedName("comment")
    @Expose
    private Object comment;
    @SerializedName("co_riders")
    @Expose
    private List<Corider> coriderList;
    @SerializedName("profile_picture")
    @Expose
    private String profilePicture;
    @SerializedName("driver_type")
    @Expose
    private int driverType;
    @SerializedName("otp")
    @Expose
    private Integer otp;
    @SerializedName("exterior_front")
    @Expose
    private String exteriorFront;
    @SerializedName("payment_option")
    @Expose
    private Integer paymentOption;

    @SerializedName("action_type")
    @Expose
    private String actionType;





    public RideInfo() {

    }


    protected RideInfo(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readInt();
        }
        if (in.readByte() == 0) {
            rideId = null;
        } else {
            rideId = in.readInt();
        }
        source = in.readString();
        destination = in.readString();
        sourceLatitude = in.readString();
        sourceLongitude = in.readString();
        destinationLatitude = in.readString();
        destinationLongitude = in.readString();
        if (in.readDouble() == 0) {
            averageRating = null;
        } else {
            averageRating = in.readDouble();
        }
        if (in.readByte() == 0) {
            rideType = null;
        } else {
            rideType = in.readInt();
        }
        if (in.readByte() == 0) {
            rideOption = null;
        } else {
            rideOption = in.readInt();
        }
        rideDate = in.readString();
        if (in.readByte() == 0) {
            driverId = null;
        } else {
            driverId = in.readInt();
        }
        if (in.readByte() == 0) {
            vehicleType = null;
        } else {
            vehicleType = in.readInt();
        }
        baseFare = in.readString();
        perKm = in.readString();
        perMinuteWaitCharge = in.readString();
        vehicleName = in.readString();
        vehicleNumber = in.readString();
        if (in.readByte() == 0) {
            status = null;
        } else {
            status = in.readInt();
        }
        createdAt = in.readString();
        updatedAt = in.readString();
        totalFare = in.readString();
        if (in.readByte() == 0) {
            postedByDriver = null;
        } else {
            postedByDriver = in.readInt();
        }
        if (in.readByte() == 0) {
            driverRideId = null;
        } else {
            driverRideId = in.readInt();
        }
        userFare = in.readString();
        driverName = in.readString();
        if (in.readByte() == 0) {
            countryCode = null;
        } else {
            countryCode = in.readInt();
        }
        phone = in.readString();
        if (in.readByte() == 0) {
            vehicleTypeId = null;
        } else {
            vehicleTypeId = in.readInt();
        }
        licenseNumber = in.readString();
        vehicleModel = in.readString();
        vehicleBrand = in.readString();
        chassisNumber = in.readString();
        vehicleColor = in.readString();
        if (in.readByte() == 0) {
            authenticatedLicense = null;
        } else {
            authenticatedLicense = in.readInt();
        }
        if (in.readByte() == 0) {
            experienced = null;
        } else {
            experienced = in.readInt();
        }
        if (in.readByte() == 0) {
            functionalAc = null;
        } else {
            functionalAc = in.readInt();
        }
        if (in.readByte() == 0) {
            vehicleCondition = null;
        } else {
            vehicleCondition = in.readInt();
        }
        profilePicture = in.readString();
        driverType = in.readInt();
        if (in.readByte() == 0) {
            otp = null;
        } else {
            otp = in.readInt();
        }
        actionType = in.readString();
        exteriorFront = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        if (userId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(userId);
        }
        if (rideId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(rideId);
        }
        dest.writeString(source);
        dest.writeString(destination);
        dest.writeString(sourceLatitude);
        dest.writeString(sourceLongitude);
        dest.writeString(destinationLatitude);
        dest.writeString(destinationLongitude);
        if (averageRating == null) {
            dest.writeDouble(0);
        } else {
            dest.writeDouble(averageRating);
        }
        if (rideType == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(rideType);
        }
        if (rideOption == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(rideOption);
        }
        dest.writeString(rideDate);
        if (driverId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(driverId);
        }
        if (vehicleType == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(vehicleType);
        }
        dest.writeString(baseFare);
        dest.writeString(perKm);
        dest.writeString(perMinuteWaitCharge);
        dest.writeString(vehicleName);
        dest.writeString(vehicleNumber);
        if (status == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(status);
        }
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(totalFare);
        if (postedByDriver == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(postedByDriver);
        }
        if (driverRideId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(driverRideId);
        }
        dest.writeString(userFare);
        dest.writeString(driverName);
        if (countryCode == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(countryCode);
        }
        dest.writeString(phone);
        if (vehicleTypeId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(vehicleTypeId);
        }
        dest.writeString(actionType);
        dest.writeString(licenseNumber);
        dest.writeString(vehicleModel);
        dest.writeString(vehicleBrand);
        dest.writeString(chassisNumber);
        dest.writeString(vehicleColor);
        if (authenticatedLicense == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(authenticatedLicense);
        }
        if (experienced == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(experienced);
        }
        if (functionalAc == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(functionalAc);
        }
        if (vehicleCondition == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(vehicleCondition);
        }
        dest.writeString(profilePicture);
        dest.writeInt(driverType);
        if (otp == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(otp);
        }
        dest.writeString(exteriorFront);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RideInfo> CREATOR = new Creator<RideInfo>() {
        @Override
        public RideInfo createFromParcel(Parcel in) {
            return new RideInfo(in);
        }

        @Override
        public RideInfo[] newArray(int size) {
            return new RideInfo[size];
        }
    };

    public String getExteriorFront() {
        return exteriorFront;
    }

    public Integer getOtp() {
        return otp;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public List<Corider> getCoriderList() {
        return coriderList;
    }

    public void setCoriderList(List<Corider> coriderList) {
        this.coriderList = coriderList;
    }

    public Integer getPaymentOption() {
        return paymentOption;
    }

    public int getDriverType() {
        return driverType;
    }

    public void setDriverType(int driverType) {
        this.driverType = driverType;
    }

    public Integer getId() {
        return id == null ? rideId : id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getSourceLatitude() {
        return sourceLatitude;
    }

    public void setSourceLatitude(String sourceLatitude) {
        this.sourceLatitude = sourceLatitude;
    }

    public String getSourceLongitude() {
        return sourceLongitude;
    }

    public void setSourceLongitude(String sourceLongitude) {
        this.sourceLongitude = sourceLongitude;
    }

    public String getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(String destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public String getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(String destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }

    public Integer getRideType() {
        return rideType;
    }

    public void setRideType(Integer rideType) {
        this.rideType = rideType;
    }

    public Object getPassengersPreference() {
        return passengersPreference;
    }

    public void setPassengersPreference(Integer passengersPreference) {
        this.passengersPreference = passengersPreference;
    }

    public Integer getRideOption() {
        return rideOption;
    }

    public void setRideOption(Integer rideOption) {
        this.rideOption = rideOption;
    }

    public String getRideDate() {
        return rideDate;
    }

    public void setRideDate(String rideDate) {
        this.rideDate = rideDate;
    }

    public Object getRideTime() {
        return rideTime;
    }

    public void setRideTime(Object rideTime) {
        this.rideTime = rideTime;
    }

    public Object getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(Object requestedAt) {
        this.requestedAt = requestedAt;
    }

    public Object getStartTime() {
        return startTime;
    }

    public void setStartTime(Object startTime) {
        this.startTime = startTime;
    }

    public Object getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Object completeTime) {
        this.completeTime = completeTime;
    }

    public Object getDuration() {
        return duration;
    }

    public void setDuration(Object duration) {
        this.duration = duration;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public Integer getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(Integer vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(String baseFare) {
        this.baseFare = baseFare;
    }

    public String getPerKm() {
        return perKm;
    }

    public void setPerKm(String perKm) {
        this.perKm = perKm;
    }

    public String getPerMinuteWaitCharge() {
        return perMinuteWaitCharge;
    }

    public void setPerMinuteWaitCharge(String perMinuteWaitCharge) {
        this.perMinuteWaitCharge = perMinuteWaitCharge;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public Object getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(String totalFare) {
        this.totalFare = totalFare;
    }

    public Integer getPostedByDriver() {
        return postedByDriver;
    }

    public void setPostedByDriver(Integer postedByDriver) {
        this.postedByDriver = postedByDriver;
    }

    public Integer getDriverRideId() {
        return driverRideId;
    }

    public void setDriverRideId(Integer driverRideId) {
        this.driverRideId = driverRideId;
    }

    public String getUserFare() {
        return userFare;
    }

    public void setUserFare(String userFare) {
        this.userFare = userFare;
    }

    public Object getDriverFare() {
        return driverFare;
    }

    public void setDriverFare(Object driverFare) {
        this.driverFare = driverFare;
    }

    public Object getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(Object uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public Object getDruppEarnings() {
        return druppEarnings;
    }

    public void setDruppEarnings(Object druppEarnings) {
        this.druppEarnings = druppEarnings;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public Integer getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(Integer countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(Integer vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public Integer getAuthenticatedLicense() {
        return authenticatedLicense;
    }

    public void setAuthenticatedLicense(Integer authenticatedLicense) {
        this.authenticatedLicense = authenticatedLicense;
    }

    public Integer getRideId() {
        return rideId == null ? id : rideId;
    }

    public Integer getExperienced() {
        return experienced;
    }

    public void setExperienced(Integer experienced) {
        this.experienced = experienced;
    }

    public Object getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Object experienceYears) {
        this.experienceYears = experienceYears;
    }

    public Integer getFunctionalAc() {
        return functionalAc;
    }

    public void setFunctionalAc(Integer functionalAc) {
        this.functionalAc = functionalAc;
    }

    public Object getReasonForNoAc() {
        return reasonForNoAc;
    }

    public void setReasonForNoAc(Object reasonForNoAc) {
        this.reasonForNoAc = reasonForNoAc;
    }

    public Integer getVehicleCondition() {
        return vehicleCondition;
    }

    public void setVehicleCondition(Integer vehicleCondition) {
        this.vehicleCondition = vehicleCondition;
    }

    public Object getRemittances() {
        return remittances;
    }

    public void setRemittances(Object remittances) {
        this.remittances = remittances;
    }

    public Object getReasonForApplying() {
        return reasonForApplying;
    }

    public void setReasonForApplying(Object reasonForApplying) {
        this.reasonForApplying = reasonForApplying;
    }

    public Object getComment() {
        return comment;
    }

    public void setComment(Object comment) {
        this.comment = comment;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionType() {
        return actionType;
    }
}
