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
import com.example.talkhive.utilities.dialogs.AddUserDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UsersFragment extends Fragment {
    private FloatingActionButton addPersonButton;
    private RecyclerView usersRv;
    private static final String SHOW_USER_TAG = "Show Users";

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
        addPersonButton = root.findViewById(R.id.addPerson);
        usersRv = root.findViewById(R.id.users_rv);
    }
}