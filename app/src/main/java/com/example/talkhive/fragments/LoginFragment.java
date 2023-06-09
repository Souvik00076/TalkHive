package com.example.talkhive.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.talkhive.R;

public class LoginFragment extends Fragment {
    final static String FILE = "LOGIN_FRAGMENT";
    private EditText emailEt, passwordEt;
    private TextView dontHaveAccount;
    private Button loginButton;

    public LoginFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //create a view and return the view
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        __init__(root);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailEt.getText().toString();
                final String passwrod = passwordEt.getText().toString();
                Log.i(FILE, "Called Button click()");
            }
        });
        return root;
    }

    private void __init__(View root) {
        emailEt = root.findViewById(R.id.email_text);
        passwordEt = root.findViewById(R.id.password_text);
        loginButton = root.findViewById(R.id.login_button);
        dontHaveAccount = root.findViewById(R.id.dont_hv_acc_sign);
    }
}