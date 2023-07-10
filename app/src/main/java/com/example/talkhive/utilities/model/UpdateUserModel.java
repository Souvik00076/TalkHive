package com.example.talkhive.utilities.model;

import android.net.Uri;

import java.io.Serializable;

public class UpdateUserModel implements Serializable {
    private String email, name;



    public UpdateUserModel(String email, String name) {
        this.email = email;
        this.name = name;
    }
    public UpdateUserModel(){

    }
    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

}
