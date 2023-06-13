package com.example.talkhive.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.talkhive.MainActivity;
import com.example.talkhive.R;

public class VerificationFragment extends Fragment {

    private MainActivity activity;
    private EditText verificationCodeText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_verification, container, false);
        init(root);
        verificationCodeText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                final String text=editable.toString();
                if(text.length()==6) checkForVerification(text);
            }
        });
        return root;
    }
    private void checkForVerification(final String text){

    }
    private void init(View root) {
        verificationCodeText = root.findViewById(R.id.verfication_text);
    }
}