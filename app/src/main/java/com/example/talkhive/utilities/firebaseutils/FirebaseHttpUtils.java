package com.example.talkhive.utilities.firebaseutils;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.talkhive.MainActivity;
import com.example.talkhive.utilities.interfaces.GeneralCallbacks;
import com.example.talkhive.utilities.model.User;
import com.example.talkhive.utilities.model.UserToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class FirebaseHttpUtils {
    private static final UserToken token = UserToken.getInstance();

    public static void registerUser(final User user, final GeneralCallbacks callbacks) {
        token.getAuth().createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = token.getAuth().getCurrentUser();
                            if (user != null)
                                sendVerificationEmail(firebaseUser, user, callbacks);
                            else {
                                callbacks.getLoginFlag(false, 101);
                            }
                        } else
                            callbacks.getSignupFlag(false, 102);
                    }
                }
        );
    }
    //sends the verification email to the new user's email address.
    private static void sendVerificationEmail(final FirebaseUser firebaseUser, final User user, final GeneralCallbacks callbacks) {
        firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    uploadUser(user, callbacks);
                } else callbacks.getSignupFlag(false, 102);
            }
        });
    }

    private static void uploadUser(User user, final GeneralCallbacks callbacks) {
        String email = user.getEmail();
        Bitmap dpMap = user.getBitmap();
        StorageReference imageReference = token.getImageReference();
        //convert to byte arraystream and set the compression type
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        dpMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();
        final String uniqueKey = email.replace(".", "");
        UploadTask uploadTask = imageReference.child(uniqueKey + "/dp.jpg").putBytes(imageData);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Log.i("Upload User", "Called");
                if (task.isSuccessful()) uploadUserRealTimeDb(email);
            }

            public void uploadUserRealTimeDb(final String email) {
                final String uniqueKey = email.replace(".", "");
                token.getDatabaseReference().child("Users/" + uniqueKey + "/info/").setValue(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callbacks.getSignupFlag(true, -1);
                        } else callbacks.getSignupFlag(false, 103);
                    }
                });
            }


        });

    }

    public static void loginUser(User user, final GeneralCallbacks callbacks) {

        token.getAuth().signInWithEmailAndPassword(user.getEmail(), user.getPassword()).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = token.getAuth().getCurrentUser();
                            if (user != null && user.isEmailVerified()) {
                                Log.i("Login User","Called Here");
                                callbacks.getLoginFlag(true, -1);
                            } else if (user != null) {

                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isComplete())
                                            callbacks.getLoginFlag(false, 104);
                                        else
                                            callbacks.getLoginFlag(false,106);
                                    }
                                });
                            }

                        } else callbacks.getLoginFlag(false, 105);
                    }
                });
    }

}
