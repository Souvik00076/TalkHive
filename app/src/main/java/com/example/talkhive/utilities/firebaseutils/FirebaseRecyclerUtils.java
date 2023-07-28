package com.example.talkhive.utilities.firebaseutils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.talkhive.utilities.interfaces.FirebaseRecyclerViewCallbacks;
import com.example.talkhive.utilities.interfaces.GeneralCallbacks;
import com.example.talkhive.utilities.model.UpdateUserModel;
import com.example.talkhive.utilities.model.UserToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class FirebaseRecyclerUtils {
    private static UserToken token = UserToken.getInstance();

    public static void getNameOfFriend(final String key, GeneralCallbacks callbacks) {
        final String userGmail = token.getAuth().getCurrentUser().getEmail().replace(".", "");
        DatabaseReference reference = token.getDatabaseReference()
                .child("Users/" +
                        userGmail + "/contacts/" + key.replace(".", ""));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UpdateUserModel model = snapshot.getValue(UpdateUserModel.class);
                    Log.i("Model name :", model.getName());
                    callbacks.getFriendName(model.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
