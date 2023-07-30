package com.example.talkhive.fragments;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.talkhive.ChatActivity;
import com.example.talkhive.R;
import com.example.talkhive.utilities.adapters.UpdateChatAdapter;
import com.example.talkhive.utilities.interfaces.FirebaseRecyclerViewCallbacks;
import com.example.talkhive.utilities.model.ChatModel;

import com.example.talkhive.utilities.model.UpdateUserModel;
import com.example.talkhive.utilities.model.UserToken;
import com.example.talkhive.utilities.services.DeleteChatItemService;
import com.example.talkhive.utilities.services.WriteChatService;
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
    private DatabaseReference chatReference, requestReference;
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
        chatReference = token.getDatabaseReference().child("Users/" + firebaseUser.getEmail()
                .replace(".", "") + "/ChatIds");
        chatReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String email = snapshot.child("sender").getValue(String.class);
                String id = snapshot.child("id").getValue(String.class);
                ChatModel model = new ChatModel(email, id);
                Log.i("error : ", model.getSender());

                token.getDatabaseReference().child("Convos/" + model.getID())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (ChatModel cm : dataSet) {
                                    if (cm.getID() == model.getID()) {
                                        dataSet.remove(cm);
                                        break;
                                    }
                                }

                                dataSet.add(0, model);
                                adapter.notifyDataSetChanged();
                                Log.i("Dataset added", dataSet.size() + "");
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String
                    previousChildName) {

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

        chatRv = root.findViewById(R.id.chat_rv);
        adapter = new

                UpdateChatAdapter(this);
        adapter.setDataSet(dataSet);
        chatRv.setAdapter(adapter);
        chatRv.setLayoutManager(new

                LinearLayoutManager(getContext()));
    }

    @Override
    public void onClickListener(ChatModel model) {
        Log.i("Chat Fragment", "Request for new fragment");
        if (model.getID() == null) Log.i("Chat Id", "null");
        chatActivity.addChatScreen(model);
    }

    @Override
    public void onDeleteListener(ChatModel model, String name) {
        Intent intent = new Intent(chatActivity, DeleteChatItemService.class);
        intent.putExtra("Message", model);
        chatActivity.startService(intent);
        Log.i("In onlong", model.getID());
        if (name != null)
            Toast.makeText(chatActivity, name + " Removed Succefully!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickListener(UpdateUserModel model) {

    }

    @Override
    public void onDeleteListener(UpdateUserModel model) {

    }
}