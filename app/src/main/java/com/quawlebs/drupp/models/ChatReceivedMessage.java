package com.quawlebs.drupp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatReceivedMessage extends BaseMessage {
    @SerializedName("chatId")
    @Expose
    private String chatId;
    @SerializedName("senderId")
    @Expose
    private String senderId;
    @SerializedName("senderName")
    @Expose
    private String senderName;
    @SerializedName(("messageId"))
    @Expose
    private String messageId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("imageMessage")
    @Expose
    private String imageMessage;
    @SerializedName("isType")
    @Expose
    private String isType;
    @SerializedName("receiverId")
    @Expose
    private String receiverId;
    @SerializedName("receiver")
    @Expose
    private String receiver;
    @SerializedName("createdAt")
    @Expose
    private long createdAt;

    public ChatReceivedMessage(String message, String mUsername) {
        this.message = message;
        receiver = mUsername;
    }

    public ChatReceivedMessage() {

    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getIsType() {
        return isType;
    }

    public void setIsType(String isType) {
        this.isType = isType;
    }

    public String getImageMessage() {
        return imageMessage;
    }

    public void setImageMessage(String imageMessage) {
        this.imageMessage = imageMessage;
    }


    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiver() {
        return receiver;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
