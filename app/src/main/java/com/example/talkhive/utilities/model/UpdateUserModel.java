package com.example.talkhive.utilities.model;

import java.io.Serializable;

public class UpdateUserModel implements Serializable {
    private String email, name;


    public UpdateUserModel(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }


}
