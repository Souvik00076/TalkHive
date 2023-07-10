package com.example.talkhive.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.talkhive.R;
import com.example.talkhive.utilities.adapters.UpdateUserAdapter;
import com.example.talkhive.utilities.dialogs.AddUserDialog;
import com.example.talkhive.utilities.model.UpdateUserModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UsersFragment extends Fragment {
    private FloatingActionButton addPersonButton;
    private RecyclerView usersRv;
    private static final String SHOW_USER_TAG = "Show Users";
    private UpdateUserAdapter adapter;
    private DatabaseReference reference;
    private ChildEventListener listener;
    private ArrayList<UpdateUserModel> dataSet;

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
        dataSet = new ArrayList<>();
        addPersonButton = root.findViewById(R.id.addPerson);
        usersRv = root.findViewById(R.id.users_rv);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = null;
        if (user != null) {
            userEmail = user.getEmail().replace(".", "");
        }
        adapter = new UpdateUserAdapter(getContext());
        usersRv.setAdapter(adapter);
        usersRv.setLayoutManager(new LinearLayoutManager(getContext()));
        reference = FirebaseDatabase.getInstance().getReference().child("Users/" + userEmail + "/contacts");
        listener = new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
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
                //called when we delete a contact
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        reference.addChildEventListener(listener);
    }
}