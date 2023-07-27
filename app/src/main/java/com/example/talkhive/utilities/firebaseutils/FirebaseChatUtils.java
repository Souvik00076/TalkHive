package com.example.talkhive.utilities.firebaseutils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.talkhive.ChatScreen;
import com.example.talkhive.utilities.interfaces.ChatCallback;
import com.example.talkhive.utilities.interfaces.FirebaseCallbacks;
import com.example.talkhive.utilities.model.ChatModel;
import com.example.talkhive.utilities.model.MessageModel;
import com.example.talkhive.utilities.model.UserToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseChatUtils {
    private static UserToken model = UserToken.getInstance();
    private static final String CLASS_NAME = "Firebase Chat Utils";

    public static void writeMessage(final MessageModel message) {
        final String receiverKey = message.getRecieverId().replace(".", "");
        final String ownerKey = message.getSenderId().replace(".", "");
        DatabaseReference referenceChatIds = model.getDatabaseReference().child("Users/"
                + ownerKey + "/ChatIds");
        FirebaseCallbacks callback = new FirebaseCallbacks() {
            @Override
            public void onCallback(String flag) {
                if (flag == null) {
                    Log.i("Write message", "called here");

                    ChatCallback chatCallback = new ChatCallback() {
                        @Override
                        public void onChildAdded(MessageModel model) {

                        }

                        @Override
                        public void onChatIdCreated(String flag) {
                            Log.i("On Chat Created", "called here");
                            if (flag != null) {
                                writeMessageToConvo(message, flag);
                                String flag2 = receiverKey.substring(0, receiverKey.indexOf('@') + 1) + "gmail.com";
                                addToChatIds(new ChatModel(flag2, flag), referenceChatIds);
                            }
                        }
                    };
                    Log.i("create ChatId", "Called");
                    createChatId(receiverKey, ownerKey, chatCallback);
                } else writeMessageToConvo(message, flag);


            }


        };
        FirebaseUtilities.getFlag(callback, receiverKey);
    }


    private static void createChatId(final String recieverKey,
                                     final String ownerKey, ChatCallback callback) {
        String chatId = model.getDatabaseReference()
                .child("Convos").push().getKey();
        DatabaseReference referenceRequest = model.getDatabaseReference().child("Users/" + recieverKey
                + "/requests/" + ownerKey + "/chatId");
        DatabaseReference referenceContacts = model.getDatabaseReference().child("Users/" + ownerKey
                + "/contacts/" + recieverKey + "/chatId");

        referenceRequest.setValue(chatId).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    referenceContacts.setValue(chatId).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                callback.onChatIdCreated(chatId);
                            }
                        }
                    });
                }
            }
        });
    }

    private static void addToChatIds(final ChatModel model, DatabaseReference reference) {
        reference.push().setValue(model);
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


    public static void loadChat(final String chatId, ChatCallback callback) {
        DatabaseReference reference = model.getDatabaseReference().child("Convos/" + chatId);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MessageModel messageModel = snapshot.getValue(MessageModel.class);
                callback.onChildAdded(messageModel);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
