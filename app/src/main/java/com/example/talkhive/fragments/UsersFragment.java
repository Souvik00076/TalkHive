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
import com.example.talkhive.utilities.adapters.UpdateUserAdapter;
import com.example.talkhive.utilities.dialogs.AddUserDialog;
import com.example.talkhive.utilities.interfaces.FirebaseRecyclerViewCallbacks;
import com.example.talkhive.utilities.model.ChatModel;
import com.example.talkhive.utilities.model.UpdateUserModel;
import com.example.talkhive.utilities.model.UserToken;
import com.example.talkhive.utilities.services.DeleteChatItemService;
import com.example.talkhive.utilities.services.DeleteUserItemService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class UsersFragment extends Fragment implements FirebaseRecyclerViewCallbacks {
    private FloatingActionButton addPersonButton;
    private RecyclerView usersRv;
    private static final String SHOW_USER_TAG = "Show Users";
    private UpdateUserAdapter adapter;
    private DatabaseReference databaseReference;
    private ChildEventListener listener;
    private ArrayList<UpdateUserModel> dataSet;
    private ChatActivity chatActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        chatActivity = (ChatActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_users, container, false);
        init(root);
        addPersonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddUserDialog dialog = new AddUserDialog();
                dialog.show(getChildFragmentManager(), SHOW_USER_TAG);
            }
        });
        return root;
    }

    private void init(final View root) {
        UserToken userToken = UserToken.getInstance();
        dataSet = new ArrayList<>();
        addPersonButton = root.findViewById(R.id.addPerson);
        usersRv = root.findViewById(R.id.users_rv);
        FirebaseUser user = userToken.getAuth().getCurrentUser();
        String userEmail = null;
        if (user != null) {
            userEmail = user.getEmail().replace(".", "");
        }
        adapter = new UpdateUserAdapter(this);
        usersRv.setAdapter(adapter);
        usersRv.setLayoutManager(new LinearLayoutManager(getContext()));
        databaseReference = userToken.getDatabaseReference().child("Users/" + userEmail + "/contacts");
        listener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.i("Users Fragment", snapshot.getValue(UpdateUserModel.class).getEmail() + " added");
                dataSet.add(snapshot.getValue(UpdateUserModel.class));
                adapter.setDataSet(dataSet);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //called when data at specified reference changed.
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Log.i("ON Delete ", "Called");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.addChildEventListener(listener);
    }

    @Override
    public void onClickListener(ChatModel model) {

    }

    @Override
    public void onDeleteListener(ChatModel model, String name) {

    }

    @Override
    public void onClickListener(UpdateUserModel model) {
        chatActivity.addChatScreen(model);
    }

    @Override
    public void onDeleteListener(UpdateUserModel model) {
        Intent intent = new Intent(chatActivity, DeleteUserItemService.class);
        intent.putExtra("Message", model);
        chatActivity.startService(intent);
        Log.i("In onlong", model.getName());
        if (model.getName() != null)
            Toast.makeText(chatActivity, model.getName() + " Removed Succefully!!", Toast.LENGTH_SHORT).show();
    }
}