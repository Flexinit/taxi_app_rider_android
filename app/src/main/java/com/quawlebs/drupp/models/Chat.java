package com.quawlebs.drupp.models;

import com.google.firebase.database.Exclude;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.quawlebs.drupp.helpers.Timing;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Date;

public class Chat implements IMessage {
    @SerializedName("senderId")
    @Expose
    private String senderId;
    @SerializedName("receiverId")
    @Expose
    private String receiverId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("message_id")
    @Expose
    private String message_id;
    @SerializedName("read_status")
    @Expose
    private Long readStatus;
    @SerializedName("read_at")
    @Expose
    private Object readAt;
    @Exclude
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("sender")
    @Expose
    private String sender;
    @Exclude
    private Author author;

    public Chat() {

    }

    public Chat(String message, Author author) {
        this.message = message;
        this.author = author;
    }

    public Chat(String receiverId, String text, Author author) {
        this.receiverId = receiverId;
        this.message = text;
        this.author = author;
    }

    @Override
    public String getId() {
        return receiverId.toString();
    }

    @Override
    public String getText() {
        return message;
    }

    @Override
    public IUser getUser() {
        return author;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Long readStatus) {
        this.readStatus = readStatus;
    }

    public Object getReadAt() {
        return readAt;
    }

    public void setReadAt(Object readAt) {
        this.readAt = readAt;
    }

    @Override
    public Date getCreatedAt() {
//        long timeInUnixStamp;
//        if (createdAt != null) {
//            timeInUnixStamp = Timing.getTimeInUnixStamp(createdAt, Timing.TimeFormats.YYYY_MM_DD_HH_MM_S);
//            return new Date(timeInUnixStamp);
//
//        } else {
//            timeInUnixStamp = System.currentTimeMillis() + 1000;
//            return new Date(timeInUnixStamp * 1000);
//        }
        long createAtTimeStamp;
        if (createdAt != null) {
            createAtTimeStamp = Timing.getTimeInMillis(createdAt, Timing.TimeFormats.YYYY_MM_DD_HH_MM_S);
        } else {
            createAtTimeStamp = Timing.getTimeInMillis(Timing.getTimeInString(System.currentTimeMillis(), Timing.TimeFormats.YYYY_MM_DD_HH_MM_S), Timing.TimeFormats.YYYY_MM_DD_HH_MM_S);
        }

        return new Date(createAtTimeStamp);

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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setMessageId(String key) {
        this.message_id = key;
    }
}
