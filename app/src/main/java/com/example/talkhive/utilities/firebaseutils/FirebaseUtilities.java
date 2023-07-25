package com.example.talkhive.utilities.firebaseutils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.talkhive.utilities.interfaces.FirebaseCallbacks;
import com.example.talkhive.utilities.model.UpdateUserModel;
import com.example.talkhive.utilities.model.UserToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class FirebaseUtilities {
    private static final String CLASS_TAG = "FirebaseUtilities";
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
                        Log.i(CLASS_TAG, "User added");
                    }
                });
    }

    public static void getFlag(FirebaseCallbacks callback,
                               final String recieverKey) {
        final String ownerKey = model.getAuth().getCurrentUser().getEmail()
                .replace(".", "");
        model.getDatabaseReference().child("Users/" + ownerKey + "/contacts/" + recieverKey)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String value = null;
                        Log.i("first event", " called");
                        if (snapshot.hasChild("chatId")) {
                            value = snapshot.child("chatId").getValue(String.class);
                        }

                        if (value != null) {
                            // ChatId found in contacts, no need to continue
                            callback.onCallback(value);
                        } else {
                            // ChatId not found in contacts, check in requests
                            Log.i("first event", " called not found");
                            model.getDatabaseReference().child("Users/" + ownerKey + "/requests/" + recieverKey)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Log.i("Second", " called");
                                            String value = null;
                                            if (snapshot.hasChild("chatId")) {
                                                value = snapshot.child("chatId").getValue(String.class);
                                            }
                                            if(value!=null) Log.i("flag",value);
                                            callback.onCallback(value);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Log.i("second event error", " called");
                                            // Handle the error in the second event here
                                            // Callback with null value
                                            callback.onCallback(null);
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.i("first event error", " called");
                        // Handle the error in the first event here
                        // Callback with null value
                        callback.onCallback(null);
                    }
                });

    }
    public static void updateUserHelper(UpdateUserModel uModel) {
        final String nameToSearch = uModel.getEmail().replace(".", "");
        FirebaseUser user = model.getAuth().getCurrentUser();
        if (user != null) {
            Log.i(CLASS_TAG, "Called Here");
            final String userId = user.getEmail().replace(".", "");
            DatabaseReference databaseReference = model.getDatabaseReference();
            databaseReference.child("Users/" + nameToSearch).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Log.i(CLASS_TAG, "User Exist");
                        databaseReference.child("Users/"+userId+"/contacts/"+ uModel.getEmail().replace(".",""))
                                .setValue(uModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.i(CLASS_TAG,"Success in add user");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.i(CLASS_TAG,"Failure to add user");
                                    }
                                });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

}
