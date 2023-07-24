package com.example.talkhive.fragments;


import static com.example.talkhive.utilities.VerificationUtilities.replaceInMain;
import static com.example.talkhive.utilities.firebaseutils.FirebaseHttpUtils.loginUser;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.talkhive.MainActivity;
import com.example.talkhive.R;
import com.example.talkhive.utilities.ErrorCodes;
import com.example.talkhive.utilities.interfaces.GeneralCallbacks;
import com.example.talkhive.utilities.model.User;
import com.example.talkhive.utilities.model.UserToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;

public class LoginFragment extends Fragment {

    private EditText emailEt, passwordEt;
    private TextView dontHaveAccount;
    private Button loginButton;
    private FirebaseAuth auth;

    private FrameLayout progressBar;
    private GeneralCallbacks callbacks;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
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
                final String password = passwordEt.getText().toString();
                User user = new User(email, password, null);
                progressBar.setVisibility(View.VISIBLE);
                loginUser(user, callbacks);

            }
        });
        dontHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceInMain(new SignupFragment(), getContext());
            }
        });
        return root;
    }


    private void __init__(View root) {
        UserToken uModel = UserToken.getInstance();
        emailEt = root.findViewById(R.id.email_text);
        passwordEt = root.findViewById(R.id.password_text);
        loginButton = root.findViewById(R.id.login_button);
        dontHaveAccount = root.findViewById(R.id.dont_hv_acc_sign);
        auth = uModel.getAuth();
        progressBar = root.findViewById(R.id.progress_bar);
        callbacks = new GeneralCallbacks() {
            @Override
            public void getSignupFlag(boolean flag, int errorCode) {

            }

            @Override
            public void getLoginFlag(boolean flag, int errorCode) {
                progressBar.setVisibility(View.GONE);
                Log.i("getLogin"," Called here");
                if (flag) {
                    MainActivity activity = (MainActivity) getContext();
                    activity.nextActivity();
                } else
                    Toast.makeText(getContext(), ErrorCodes.getMap().get(errorCode), Toast.LENGTH_SHORT).show();
            }
        };
    }
}