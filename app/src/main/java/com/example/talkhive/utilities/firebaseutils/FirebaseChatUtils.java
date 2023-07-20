package com.example.talkhive.utilities.firebaseutils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.talkhive.ChatScreen;
import com.example.talkhive.utilities.model.MessageModel;
import com.example.talkhive.utilities.model.UserDetailsModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FirebaseChatUtils {
    private static UserDetailsModel model = UserDetailsModel.getInstance();
    private static final String CLASS_NAME = "Firebase Chat Utils";

    public interface FirebaseCallback {
        void onCallback(String flag);

    }

    public static void writeMessage(final MessageModel message) {
        final String recieverKey = message.getRecieverId().replace(".", "");
        final String ownerKey = message.getSenderId().replace(".", "");
        FirebaseCallback callback = new FirebaseCallback() {
            @Override
            public void onCallback(String flag) {
                if (flag == null) {
                    flag = createChatId(recieverKey, ownerKey);
                }
                //send message to the chatId
                if (flag != null) writeMessageToConvo(message, flag);
            }
        };
        FirebaseUtilities.getFlag(callback, recieverKey);

    }

    private static String createChatId(final String recieverKey,
                                       final String ownerKey) {
        String chatId = model.getDatabaseReference()
                .child("Convos").push().getKey();
        boolean flag1 = model.getDatabaseReference().child("Users/" + recieverKey
                + "/contacts/" + ownerKey + "/chatId").setValue(chatId).isSuccessful();
        boolean flag2 = model.getDatabaseReference().child("Users/" + ownerKey
                + "/contacts/" + recieverKey + "/chatId").setValue(chatId).isSuccessful();
        if (flag1 && flag2) return chatId;
        return null;
    }

    private static void writeMessageToConvo(final MessageModel message,
                                            final String flag) {
        DatabaseReference reference = model.getDatabaseReference().child("Convos/" + flag);
        String key = reference.push().getKey();
        reference.child(key).setValue(message).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i(CLASS_NAME, "Message written at chat id " + flag);
                    }
                });
    }

    public static void loadChat(final String chatId, ChatScreen.ChatCallBack callBack) {
        ArrayList<MessageModel> dataSet = new ArrayList<>();
        DatabaseReference reference = model.getDatabaseReference().child("Convos/" + chatId);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        MessageModel value = childSnapshot.getValue(MessageModel.class);

                        dataSet.add(value);
                    }
                    callBack.onLoad(dataSet);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
