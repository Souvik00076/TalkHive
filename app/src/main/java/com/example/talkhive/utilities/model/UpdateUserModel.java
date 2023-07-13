package com.example.talkhive.utilities.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class UpdateUserModel implements Parcelable {
    private String email, name;
    private boolean flag;

    public UpdateUserModel(String email, String name,boolean flag) {
        this.email = email;
        this.name = name;
        this.flag=flag;
    }
    public UpdateUserModel(){

    }

    protected UpdateUserModel(Parcel in) {
        email = in.readString();
        name = in.readString();
        flag = in.readByte() != 0;
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

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(email);
        parcel.writeString(name);
        parcel.writeByte((byte) (flag ? 1 : 0));
    }
}
