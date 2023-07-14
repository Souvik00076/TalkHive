package com.example.talkhive.utilities.firebaseutils;

import android.util.Log;

import com.example.talkhive.utilities.model.MessageModel;
import com.example.talkhive.utilities.model.UserDetailsModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

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
        DatabaseReference reference= model.getDatabaseReference().child("Users/Convos/" + flag);
        String key=reference.push().getKey();
                reference.child(key).setValue(message).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i(CLASS_NAME, "Message written at chat id " + flag);
                    }
                });
    }
}
