package com.example.talkhive.utilities.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

//this class responsible for storing id's in ChatId node which will be used to populate recycler view
//whenever a new message comes
public class ChatModel implements Parcelable {
    private  String sender;
    private  String ID;

    public ChatModel(final String receiver, final String ID) {
        this.sender = receiver;
        this.ID = ID;
    }
    public ChatModel(){

    }

    protected ChatModel(Parcel in) {
        sender = in.readString();
        ID = in.readString();
    }

    public static final Creator<ChatModel> CREATOR = new Creator<ChatModel>() {
        @Override
        public ChatModel createFromParcel(Parcel in) {
            return new ChatModel(in);
        }

        @Override
        public ChatModel[] newArray(int size) {
            return new ChatModel[size];
        }
    };

    public String getSender() {
        return sender;
    }

    public String getID() {
        return ID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(sender);
        parcel.writeString(ID);
    }
}
