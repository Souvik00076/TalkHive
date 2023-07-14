package com.example.talkhive.utilities.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class UpdateUserModel implements Parcelable {
    private String email, name;
    private String chatId;


    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public UpdateUserModel(String email, String name,
                           String chatId) {
        this.email = email;
        this.name = name;
        this.chatId=chatId;
    }
    public UpdateUserModel(){

    }

    protected UpdateUserModel(Parcel in) {
        email = in.readString();
        name = in.readString();
        chatId=in.readString();
    }

    public static final Creator<UpdateUserModel> CREATOR = new Creator<UpdateUserModel>() {
        @Override
        public UpdateUserModel createFromParcel(Parcel in) {
            return new UpdateUserModel(in);
        }

        @Override
        public UpdateUserModel[] newArray(int size) {
            return new UpdateUserModel[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(email);
        parcel.writeString(name);
        parcel.writeString(chatId);
    }
}
