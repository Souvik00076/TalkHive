package com.example.talkhive.utilities;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.talkhive.MainActivity;
import com.example.talkhive.utilities.model.UpdateUserModel;
import com.example.talkhive.utilities.model.UserToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class GeneralUtils {
    private static final UserToken utilities = UserToken.getInstance();
    private static final String CLASS_TAG = GeneralUtils.class.getName();
    final static String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    public static boolean verifyEmail(final String email) {
        return email.matches(emailPattern);
    }

    public static boolean checkEqualityPassword(final String pass1, final String pass2) {
        return !(pass1.isEmpty() || pass2.isEmpty() || !pass1.equals(pass2));
    }

    public static void replaceInMain(Fragment fragment, Context context) {
        MainActivity activity = (MainActivity) context;
        activity.addOrReplace(fragment);
    }

    public static void updateUserHelper(UpdateUserModel model) {
        final String nameToSearch = model.getEmail().replace(".", "");
        FirebaseUser user = utilities.getAuth().getCurrentUser();
        if (user != null) {
            Log.i(CLASS_TAG, "Called Here");
            final String userId = user.getEmail().replace(".", "");
            DatabaseReference databaseReference = utilities.getDatabaseReference();
            databaseReference.child("Users/" + nameToSearch).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Log.i(CLASS_TAG, "User Exist");
                        databaseReference.child("Users/"+userId+"/contacts/"+ model.getEmail().replace(".",""))
                                .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public static String convertUnixTimeStamp(long unixTimeStamp) {
        Instant instant = Instant.ofEpochSecond(unixTimeStamp);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDateTime now = LocalDateTime.now();

        long diffInSeconds = java.time.Duration.between(dateTime, now).getSeconds();

        if (diffInSeconds < 60) {
            return "Just now";
        } else if (diffInSeconds < 3600) {
            long minutesAgo = diffInSeconds / 60;
            return minutesAgo + " minute" + (minutesAgo > 1 ? "s" : "") + " ago";
        } else if (diffInSeconds < 86400) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
            return "" + dateTime.format(formatter);
        } else if (diffInSeconds < 604800) {
            long daysAgo = diffInSeconds / 86400;
            if (daysAgo == 1) {
                return "Yesterday";
            } else {
                return daysAgo + " days ago";
            }
        } else if (diffInSeconds < 2419200) {
            long weeksAgo = diffInSeconds / 604800;
            return weeksAgo + " week" + (weeksAgo > 1 ? "s" : "") + " ago";
        } else if (diffInSeconds < 29030400) {
            long monthsAgo = diffInSeconds / 2419200;
            return monthsAgo + " month" + (monthsAgo > 1 ? "s" : "") + " ago";
        } else {
            long yearsAgo = diffInSeconds / 29030400;
            return yearsAgo + " year" + (yearsAgo > 1 ? "s" : "") + " ago";
        }
    }

}
