package com.example.talkhive.utilities.interfaces;

import com.example.talkhive.utilities.model.ChatModel;
import com.example.talkhive.utilities.model.UpdateUserModel;

public interface FirebaseRecyclerViewCallbacks {


    void onClickListener(final ChatModel model);

    void onDeleteListener(ChatModel model, String name);

    void onClickListener(final UpdateUserModel model);

    void onDeleteListener(final UpdateUserModel model);
}
