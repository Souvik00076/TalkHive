package com.example.talkhive;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.talkhive.utilities.model.UpdateUserModel;
import com.example.talkhive.utilities.model.UserDetailsModel;
import com.google.android.gms.tasks.OnSuccessListener;

public class ChatScreen extends AppCompatActivity {
    private AppCompatImageView chatDp, buttonBack;
    private TextView chatName;
    private FrameLayout buttonSend;
    private EditText sendMessageEt;
    private UserDetailsModel detailsModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_screen_activity);
        init();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            UpdateUserModel model = (UpdateUserModel) extras.getSerializable("USER_DETAILS");
            chatName.setText(model.getName());
            detailsModel.getStorageReference().child(model.getEmail().replace(".","")+"/dp.jpg")
                    .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Glide.with(ChatScreen.this).load(uri).into(chatDp);
                        }
                    });
        }
    }

    private void init() {
        chatDp = (AppCompatImageView) findViewById(R.id.chat_dp);
        buttonBack = (AppCompatImageView) findViewById(R.id.button_back);
        chatName = (TextView) findViewById(R.id.chat_name);
        buttonSend = (FrameLayout) findViewById(R.id.button_send);
        sendMessageEt = (EditText) findViewById(R.id.send_msg_it);
        detailsModel=UserDetailsModel.getInstance();
    }
}