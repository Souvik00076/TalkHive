package com.example.talkhive.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.talkhive.R;
import com.example.talkhive.utilities.adapters.UpdateChatAdapter;
import com.example.talkhive.utilities.model.ChatModel;
import com.example.talkhive.utilities.model.User;
import com.example.talkhive.utilities.model.UserToken;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;


public class RequestFragment extends Fragment {
    private RecyclerView requestRv;
    private UpdateChatAdapter adapter;
    private ArrayList<ChatModel> dataSet;
    private UserToken token;
    private DatabaseReference requestReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_request, container, false);
        init(root);
        requestReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ChatModel model = snapshot.getValue(ChatModel.class);
                dataSet.add(model);
                adapter.notifyDataSetChanged();
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
        return root;
    }

    private void init(final View root) {
        token = UserToken.getInstance();
        requestReference = token.getDatabaseReference().child("Users/"
                        + token.getAuth().getCurrentUser().getEmail().replace(".", "")
                +"/requests");
        requestRv = root.findViewById(R.id.chat_rv);
        dataSet = new ArrayList<>();
        adapter = new UpdateChatAdapter(this);
        adapter.setDataSet(dataSet);
        requestRv.setAdapter(adapter);
    }
}