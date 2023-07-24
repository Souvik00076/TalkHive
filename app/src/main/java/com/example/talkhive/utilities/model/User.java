package com.example.talkhive.utilities.model;

import android.graphics.Bitmap;

public class User {

    private String email;
    private String password;
    private Bitmap bitmap;

    public User(String email, String password, Bitmap bitmap) {

        this.email = email;
        this.password = password;
        this.bitmap = bitmap;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
