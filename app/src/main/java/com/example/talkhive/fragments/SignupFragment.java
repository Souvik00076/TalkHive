package com.example.talkhive.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.talkhive.R;

public class SignupFragment extends Fragment {
    private EditText emailEt, passwordEt, confirmPasswordEt;
    private Button signUpButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_signup, container, false);
        __init__(root);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        return root;
    }
    private void registerUser(){

    }
    private void __init__(View root) {
        emailEt = root.findViewById(R.id.email_text);
        passwordEt = root.findViewById(R.id.password_text);
        confirmPasswordEt = root.findViewById(R.id.confirm_password_text);
    }
}