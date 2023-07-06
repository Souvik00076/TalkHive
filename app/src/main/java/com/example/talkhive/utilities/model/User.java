package com.example.talkhive.utilities.model;

public class User {

    private String email, imageUri;

    public User(String email, String imageUri) {
        this.email = email;
        this.imageUri = imageUri;
    }

    public User(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
