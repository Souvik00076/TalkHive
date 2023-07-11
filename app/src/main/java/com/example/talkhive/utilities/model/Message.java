package com.example.talkhive.utilities.model;

public class Message {
    private String senderId, recieverId;
    private String message;
    private long timeStamp;

    public Message(String senderId, String recieverId, String message, long timeStamp) {
        this.senderId = senderId;
        this.recieverId = recieverId;
        this.message = message;
        this.timeStamp = timeStamp;
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
