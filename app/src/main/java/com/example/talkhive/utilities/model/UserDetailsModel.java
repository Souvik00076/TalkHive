package com.example.talkhive.utilities.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserDetailsModel {

    public final FirebaseAuth auth;
    public final DatabaseReference reference;
    private final StorageReference storageReference;
    private static UserDetailsModel instance;

    private UserDetailsModel() {
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        String emailKey = auth.getCurrentUser().getEmail().replace(".", "");
        storageReference = FirebaseStorage.getInstance().getReference().child("Users/");
    }

    public static UserDetailsModel getInstance() {
        if (instance == null) instance = new UserDetailsModel();
        return instance;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public DatabaseReference getReference() {
        return reference;
    }

    public StorageReference getStorageReference() {
        return storageReference;
    }
}
