package com.example.talkhive.utilities.model;

import java.io.Serializable;

public class MessageModel implements Serializable {
    private String senderId, recieverId;
    private String message;
    private long timeStamp;

    public MessageModel(String senderId, String recieverId, String message, long timeStamp) {
        this.senderId = senderId;
        this.recieverId = recieverId;
        this.message = message;
        this.timeStamp = timeStamp;
    }
    public MessageModel(){

    }
       public long getTimeStamp() {
        return timeStamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getRecieverId() {
        return recieverId;
    }

    public String getMessage() {
        return message;
    }
}
