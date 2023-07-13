package com.example.talkhive.utilities.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserDetailsModel {

    private final FirebaseAuth auth;
    private final FirebaseDatabase database;
    private final DatabaseReference databaseReference;

    private final StorageReference imageReference;
    private static UserDetailsModel instance;
    private final FirebaseStorage firebaseStorage;

    private UserDetailsModel() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        imageReference = firebaseStorage.getReference().child("Users/");
    }

    public static UserDetailsModel getInstance() {
        if (instance == null) instance = new UserDetailsModel();
        return instance;
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public StorageReference getImageReference() {
        return imageReference;
    }

    public FirebaseStorage getFirebaseStorage() {
        return firebaseStorage;
    }
}
