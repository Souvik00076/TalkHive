package com.example.talkhive.utilities.model;

import java.io.Serializable;

//this class responsible for storing id's in ChatId node which will be used to populate recycler view
//whenever a new message comes
public class ChatModel implements Serializable {
    private  String sender;
    private  String ID;

    public ChatModel(final String reciever, final String ID) {
        this.sender = reciever;
        this.ID = ID;
    }
    public ChatModel(){

    }
    public String getSender() {
        return sender;
    }

    public String getID() {
        return ID;
    }
}
