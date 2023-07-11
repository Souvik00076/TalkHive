package com.example.talkhive;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.talkhive.utilities.model.Message;
import com.example.talkhive.utilities.model.UpdateUserModel;
import com.example.talkhive.utilities.model.UserDetailsModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ChatScreen extends AppCompatActivity {
    private AppCompatImageView chatDp, buttonBack;
    private TextView chatName;
    private FrameLayout buttonSend;
    private EditText sendMessageEt;
    private ProgressBar waitPbar;
    private UserDetailsModel detailsModel;
    private FirebaseAuth auth;
    private UpdateUserModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_screen_activity);
        ActionBar actionBar=getSupportActionBar();
        if(null!=actionBar) actionBar.hide();
        init();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            model = (UpdateUserModel) extras.getSerializable("USER_DETAILS");
            chatName.setText(model.getName());
            detailsModel.getImageReference().child(model.getEmail().replace(".", "") + "/dp.jpg")
                    .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(ChatScreen.this).load(uri).into(chatDp);
                        }
                    });
        }
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String chatMessage = sendMessageEt.getText().toString();
                if(!TextUtils.isEmpty(chatMessage)) {
                    Message message = new Message(auth.getCurrentUser().getEmail(),
                            model.getEmail(),chatMessage,
                            System.currentTimeMillis()/1000
                            );
                }
            }
        });
    }

    private void init() {
        chatDp = (AppCompatImageView) findViewById(R.id.chat_dp);
        buttonBack = (AppCompatImageView) findViewById(R.id.button_back);
        chatName = (TextView) findViewById(R.id.chat_name);
        buttonSend = (FrameLayout) findViewById(R.id.button_send);
        sendMessageEt = (EditText) findViewById(R.id.send_msg_it);
        detailsModel = UserDetailsModel.getInstance();
        waitPbar = (ProgressBar) findViewById(R.id.wait_chat);
        auth = detailsModel.getAuth();
    }
}