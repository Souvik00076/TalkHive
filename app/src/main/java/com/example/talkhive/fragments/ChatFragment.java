package com.example.talkhive.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.talkhive.ChatActivity;
import com.example.talkhive.R;
import com.example.talkhive.utilities.adapters.UpdateChatAdapter;
import com.example.talkhive.utilities.interfaces.FirebaseRecyclerViewCallbacks;
import com.example.talkhive.utilities.model.ChatModel;
import com.example.talkhive.utilities.model.MessageModel;
import com.example.talkhive.utilities.model.User;
import com.example.talkhive.utilities.model.UserToken;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ChatFragment extends Fragment implements FirebaseRecyclerViewCallbacks {
    private RecyclerView chatRv;
    private UpdateChatAdapter adapter;
    private UserToken token;
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;
    private ArrayList<ChatModel> dataSet;
    private ChatActivity chatActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        chatActivity = (ChatActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chat, container, false);
        init(root);
        return root;
    }

    private void init(final View root) {
        token = UserToken.getInstance();
        dataSet = new ArrayList<>();
        firebaseUser = token.getAuth().getCurrentUser();
        String key = firebaseUser.getEmail().replace(".", "");
        reference = token.getDatabaseReference().child("Users/" + firebaseUser.getEmail()
                .replace(".", "") + "/ChatIds");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataSet.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Log.i("Chat Fragment", "called here");
                    ChatModel chatModel = childSnapshot.getValue(ChatModel.class);
                    dataSet.add(chatModel);
                }
                adapter.setDataSet(dataSet);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        chatRv = root.findViewById(R.id.chat_rv);
        adapter = new UpdateChatAdapter(this);
        chatRv.setAdapter(adapter);
        chatRv.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onClickListener(ChatModel model) {
        Log.i("Chat Fragment", "Request for new fragment");
        chatActivity.addChatScreen(model);
    }
}