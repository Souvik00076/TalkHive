package com.example.talkhive.fragments;

import static com.example.talkhive.utilities.GeneralUtils.checkEqualityPassword;
import static com.example.talkhive.utilities.GeneralUtils.replaceInMain;
import static com.example.talkhive.utilities.GeneralUtils.verifyEmail;
import static com.example.talkhive.utilities.firebaseutils.FirebaseHttpUtils.registerUser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.talkhive.R;
import com.example.talkhive.utilities.ErrorCodes;
import com.example.talkhive.utilities.interfaces.GeneralCallbacks;
import com.example.talkhive.utilities.model.User;
import com.example.talkhive.utilities.model.UserToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

public class SignupFragment extends Fragment {
    private EditText emailEt, passwordEt, confirmPasswordEt;
    private Button signUpButton;

    private FirebaseAuth auth;
    private FrameLayout progressBar;
    private ImageView userDp;
    private StorageReference imageReference;
    private DatabaseReference dbReference;
    private GeneralCallbacks callbacks;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_signup, container, false);
        __init__(root);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailEt.getText().toString();
                if (!verifyEmail(email)) {
                    Toast.makeText(getContext(), ErrorCodes.getMap().get(18), Toast.LENGTH_SHORT).show();
                    return;
                }
                final String password = passwordEt.getText().toString();
                final String confPassword = confirmPasswordEt.getText().toString();
                if (!checkEqualityPassword(password, confPassword)) {
                    Toast.makeText(getContext(), ErrorCodes.getMap().get(19), Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                Drawable drawable = userDp.getDrawable();
                Bitmap dpMap = ((BitmapDrawable) drawable).getBitmap();
                User user = new User(email, password, dpMap);
                registerUser(user, callbacks);
            }
        });
        ActivityResultLauncher<String> imageSelectionLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            userDp.setImageURI(result);
                        }
                    }
                }
        );
        userDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageSelectionLauncher.launch("image/*");
            }
        });


        return root;
    }


    private void __init__(View root) {
        UserToken uModel = UserToken.getInstance();
        emailEt = root.findViewById(R.id.email_text);
        passwordEt = root.findViewById(R.id.password_text);
        confirmPasswordEt = root.findViewById(R.id.confirm_password_text);
        auth = uModel.getAuth();
        signUpButton = root.findViewById(R.id.signup_button);
        progressBar = root.findViewById(R.id.progress_bar);
        userDp = root.findViewById(R.id.dp_iv);
        imageReference = uModel.getImageReference();
        dbReference = uModel.getDatabaseReference();
        callbacks = new GeneralCallbacks() {
            @Override
            public void getSignupFlag(boolean flag, final int errorCode) {
                if (flag) {
                    replaceInMain(new LoginFragment(), getContext());
                } else {
                    Toast.makeText(getContext(), ErrorCodes.getMap().get(errorCode), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void getFriendName(String name) {

            }

            @Override
            public void getLoginFlag(boolean flag, final int errorCode) {

            }
        };
    }

}

