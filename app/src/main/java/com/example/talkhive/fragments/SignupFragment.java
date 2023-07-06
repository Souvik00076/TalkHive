package com.example.talkhive.fragments;

import static com.example.talkhive.utilities.VerificationUtilities.checkEqualityPassword;
import static com.example.talkhive.utilities.VerificationUtilities.replaceInMain;
import static com.example.talkhive.utilities.VerificationUtilities.verifyEmail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableKt;
import androidx.fragment.app.Fragment;

import com.example.talkhive.MainActivity;
import com.example.talkhive.R;
import com.example.talkhive.utilities.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class SignupFragment extends Fragment {
    private EditText emailEt, passwordEt, confirmPasswordEt;
    private Button signUpButton;
    private static final String NETWORK_ERROR_MSG = "Unknown network problem";
    private final String INVALID_STRING_MSG = "INVALID EMAIL";
    private final String PASSWORD_NOT_MATCHED_MSG = "PASSWORDS DO NOT MATCH";
    private final String MAIL_NOT_SEND = "MAIL COULD NOT BE SENT, TRY AFTER SOME TIME";
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private ImageView userDp;
    private FirebaseStorage storage;

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
                    Toast.makeText(getContext(), INVALID_STRING_MSG, Toast.LENGTH_SHORT).show();
                    return;
                }
                final String password = passwordEt.getText().toString();
                final String confPassword = confirmPasswordEt.getText().toString();
                if (!checkEqualityPassword(password, confPassword)) {
                    Toast.makeText(getContext(), PASSWORD_NOT_MATCHED_MSG, Toast.LENGTH_SHORT).show();
                    return;
                }
                registerUser(email, password);
            }
        });
        ActivityResultLauncher<String> imageSelectionLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            // Handle the selected image URI
                            // Set the image in the ImageView
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

    private void registerUser(final String email, final String password) {
        enableDisableProgressBar(true);
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null)
                                sendVerificationEmail(user);
                            else {
                                enableDisableProgressBar(false);
                                Toast.makeText(getContext(), MAIL_NOT_SEND, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            enableDisableProgressBar(false);
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null)
                                deleteUser(user);
                        }

                    }
                });
    }

    private void deleteUser(final FirebaseUser user) {
        user.delete();
    }

    private void sendVerificationEmail(final FirebaseUser user) {
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    uploadUser(user.getEmail());
                } else {
                    enableDisableProgressBar(false);
                    deleteUser(user);
                    Toast.makeText(getContext(), "Problem with verification email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void enableDisableProgressBar(boolean flag) {
        if (flag) progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(View.GONE);
    }

    private void uploadUser(final String email) {
        //convert to bitmap first
        Drawable drawable = userDp.getDrawable();
        Bitmap dpMap = ((BitmapDrawable) drawable).getBitmap();

        //convert to bytearraystream and set the compression type
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        dpMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();
        final String uniqueKey = email.replace(".", "");
        StorageReference storageReference = storage.getReference().child("Users/" + uniqueKey + "/dp.jpg");
        UploadTask uploadTask = storageReference.putBytes(imageData);

        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            uploadUserRealTimeDb(uri.toString(), email);
                        }
                    });
                } else uploadUserRealTimeDb(null, email);
            }

            public void uploadUserRealTimeDb(final String uri, final String email) {
                final String uniqueKey = email.replace(".", "");
                DatabaseReference dbReference = FirebaseDatabase.getInstance()
                        .getReference().child("Users/" + uniqueKey);
                User user;
                if (uri == null) user = new User(email);
                else user = new User(email, uri);

                dbReference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            enableDisableProgressBar(false);
                            replaceInMain(new LoginFragment(), getContext());
                        } else {
                            Toast.makeText(getContext(), NETWORK_ERROR_MSG, Toast.LENGTH_SHORT).show();
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) deleteUser(user);
                        }
                    }
                });
            }


        });


    }


    private void __init__(View root) {
        emailEt = root.findViewById(R.id.email_text);
        passwordEt = root.findViewById(R.id.password_text);
        confirmPasswordEt = root.findViewById(R.id.confirm_password_text);
        auth = FirebaseAuth.getInstance();
        signUpButton = root.findViewById(R.id.signup_button);
        progressBar = root.findViewById(R.id.progress_bar);
        userDp = root.findViewById(R.id.dp_iv);
        storage = FirebaseStorage.getInstance();
    }

}