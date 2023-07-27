package com.example.talkhive.fragments;

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

import com.example.talkhive.R;
import com.example.talkhive.utilities.adapters.UpdateChatAdapter;
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


public class ChatFragment extends Fragment {
    private RecyclerView chatRv;
    private UpdateChatAdapter adapter;
    private UserToken token;
    private final String ROUTE_PATH = "/ChatIds";
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;
    private ArrayList<MessageModel> dataSet;

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
        String path="Users/" + key + ROUTE_PATH;
        Log.i("Path",path);
        reference = token.getDatabaseReference().child(path);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //dataSet.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Log.i("ChatFragment", " Called add Value Event Listener");
                    ChatModel model = childSnapshot.getValue(ChatModel.class);
                    if(model.getID()!=null)
                    reference.child(model.getID())
                            .addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    Log.i("On Child ","Called");
                                    MessageModel model = snapshot.getValue(MessageModel.class);
                                    dataSet.add(model);
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    Log.i("On Child ","Updated");
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        chatRv = root.findViewById(R.id.chat_rv);
        adapter = new UpdateChatAdapter(this);
        chatRv.setAdapter(adapter);
        chatRv.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}