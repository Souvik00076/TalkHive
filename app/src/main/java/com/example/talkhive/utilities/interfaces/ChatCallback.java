package com.example.talkhive.utilities.interfaces;

import android.os.Message;

import com.example.talkhive.utilities.model.MessageModel;

public interface ChatCallback {
    void onChildAdded(MessageModel model);
    void onChatIdCreated(String flag);
}
