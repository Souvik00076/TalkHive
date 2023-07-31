package com.example.talkhive.utilities.firebaseutils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.talkhive.utilities.interfaces.FirebaseCallbacks;
import com.example.talkhive.utilities.model.UpdateUserModel;
import com.example.talkhive.utilities.model.UserToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class FirebaseUtilities {
    private static final String CLASS_TAG = "FirebaseUtilities";
    private static UserToken token = UserToken.getInstance();
    public static void updateUser(final UpdateUserModel userModel) {
        final String ownerEmail = token.getAuth().getCurrentUser().getEmail()
                .replace(".", "");
        final String recipientEmail = userModel.getEmail().replace(".", "");
        token.getDatabaseReference().
                child("Users/" + ownerEmail + "/contacts/" + recipientEmail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            return;
                        }
                        token.getDatabaseReference().child("Users/" +
                                recipientEmail + "/contacts/" + ownerEmail).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    String chatId = snapshot.child("chatId").getValue(String.class);
                                    if (chatId != null) {
                                        Log.i("Again called","Yes");
                                        userModel.setChatId(chatId);
                                    }
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
        token.getDatabaseReference().child("Users/" + ownerKey + "/contacts/" + recipientKey)
                .setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i(CLASS_TAG, "User added");
                    }
                });
    }

    public static void getFlag(FirebaseCallbacks callback,
                               final String recieverKey) {
        final String ownerKey = token.getAuth().getCurrentUser().getEmail()
                .replace(".", "");
        token.getDatabaseReference().child("Users/" + ownerKey + "/contacts/" + recieverKey)
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
                            token.getDatabaseReference().child("Users/" + ownerKey + "/requests/" + recieverKey)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Log.i("Second", " called");
                                            String value = null;
                                            if (snapshot.hasChild("chatId")) {
                                                value = snapshot.child("chatId").getValue(String.class);
                                            }
                                            if (value != null) Log.i("flag", value);
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

    public static void deleteUserFromContact(final UpdateUserModel model) {
        DatabaseReference reference = token.getDatabaseReference()
                .child("Users/" + token.getAuth().getCurrentUser()
                        .getEmail().replace(".", "")
                        + "/contacts/" + model.getEmail().replace(".", ""));

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapShot : snapshot.getChildren()) {
                    System.out.println("is it " + snapshot.getKey());
                    System.out.println("key : " + model.getEmail());

                    if (snapshot.getKey().equals( model.getEmail().replace(".", ""))) {
                        Log.i("deleteUserFromContact", " deleted");
                        childSnapShot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                    Log.i("User item with ", model.getEmail() + " deleted");
                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
