package com.example.talkhive.utilities.firebaseutils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.talkhive.utilities.model.UpdateUserModel;
import com.example.talkhive.utilities.model.UserToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class FirebaseUtilities {
    private static final String CLASS_NAME = "FirebaseUtilities";
    private static UserToken model = UserToken.getInstance();

    public static void updateUser(final UpdateUserModel userModel) {
        final String ownerEmail = model.getAuth().getCurrentUser().getEmail()
                .replace(".", "");
        final String recipientEmail = userModel.getEmail().replace(".", "");
        model.getDatabaseReference().
                child("Users/" + ownerEmail + "/contacts/" + recipientEmail)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            return;
                        }
                        model.getDatabaseReference().child("Users/" +
                                recipientEmail + "/contacts/" + ownerEmail).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    String chatId = snapshot.child("chatId").getValue(String.class);
                                    if (chatId != null) userModel.setChatId(chatId);
                                }
                                addUser(userModel, ownerEmail, recipientEmail);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private static void addUser(final UpdateUserModel userModel, final
    String ownerKey, final String recipientKey) {
        model.getDatabaseReference().child("Users/" + ownerKey + "/contacts/" + recipientKey)
                .setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i(CLASS_NAME, "User added");
                    }
                });
    }

    public static void getFlag(FirebaseChatUtils.FirebaseCallback callback,
                               final String recieverKey) {
        final String ownerKey = model.getAuth().getCurrentUser().getEmail()
                .replace(".", "");
        model.getDatabaseReference().
                child("Users/" + ownerKey + "/contacts/" + recieverKey)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String value = null;
                        if (snapshot.hasChild("chatId"))
                            value = snapshot.child("chatId").getValue(String.class);
                        callback.onCallback(value);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}
