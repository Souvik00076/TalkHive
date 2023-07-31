package com.example.talkhive;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.talkhive.utilities.adapters.UpdateChatScreenAdapter;
import com.example.talkhive.utilities.model.ChatModel;
import com.example.talkhive.utilities.model.MessageModel;
import com.example.talkhive.utilities.model.UpdateUserModel;
import com.example.talkhive.utilities.model.UserToken;
import com.example.talkhive.utilities.services.WriteChatService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatScreen extends AppCompatActivity {
    private AppCompatImageView chatDp, buttonBack;
    private TextView chatName;
    private FrameLayout buttonSend;
    private EditText sendMessageEt;
    private ProgressBar waitPbar;
    private UserToken token;
    private FirebaseAuth auth;
    private UpdateUserModel model;
    private ChatModel model2;
    private RecyclerView chatView;
    private UpdateChatScreenAdapter adapter;
    private ArrayList<MessageModel> dataSet;
    private DatabaseReference reference;
    private String recipientEmail, recipientName;
    private String chatId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = UserToken.getInstance();
        setContentView(R.layout.chat_screen_activity);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) actionBar.hide();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            model = extras.getParcelable("USER_DETAILS");
            model2 = extras.getParcelable("CHAT_DETAILS");

            if (model != null) {
                recipientEmail = model.getEmail();
                recipientName = model.getName() == null ? model.getEmail() : model.getName();
            } else {
                recipientEmail = model2.getSender();
                recipientName = model2.getSender();
                final String path="Users/" + token.getAuth().getCurrentUser()
                        .getEmail().replace(".", "") +"/contacts/"+
                        recipientEmail.replace(".", "");
                DatabaseReference reference = token.getDatabaseReference().
                        child(path);
                Log.i("recipient path", path);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.hasChild("name"))
                            recipientName = snapshot.child("name").getValue(String.class);
                        else recipientName = model2.getSender();
                        chatName.setText(recipientName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
            init();
            chatName.setText(recipientName);
            token.getImageReference().child(recipientEmail.replace(".", "") + "/dp.jpg")
                    .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            try {
                                Glide.with(ChatScreen.this).load(uri).into(chatDp);
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            chatDp.setImageResource(R.drawable.dummy);
                        }
                    });
        }

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String chatMessage = sendMessageEt.getText().toString();
                if (!TextUtils.isEmpty(chatMessage)) {
                    MessageModel message = new MessageModel(auth.getCurrentUser().getEmail(),
                            recipientEmail, chatMessage,
                            System.currentTimeMillis() / 1000
                    );
                    sendMessageEt.getText().clear();
                    Intent intent = new Intent(ChatScreen.this, WriteChatService.class);
                    intent.putExtra("Message", message);
                    startService(intent);
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

        waitPbar = (ProgressBar) findViewById(R.id.wait_chat);
        auth = token.getAuth();
        chatView = findViewById(R.id.chat_view);
        adapter = new UpdateChatScreenAdapter(this);
        dataSet = new ArrayList<>();
        chatView.setAdapter(adapter);
        adapter.setDataSet(dataSet);
        chatView.setLayoutManager(new LinearLayoutManager(this));
        reference = token.getDatabaseReference().child("Users/" +
                auth.getCurrentUser().getEmail().replace(".", "") + "/contacts/"
                + recipientEmail.replace(".", ""));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    chatId = snapshot.child("chatId").getValue(String.class);


                    token.getDatabaseReference().child("Convos/" + chatId)
                            .addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    waitPbar.setVisibility(View.GONE);
                                    Log.i("On Child Added", "not called?");
                                    MessageModel messageModel = snapshot.getValue(MessageModel.class);
                                    dataSet.add(messageModel);
                                    adapter.notifyDataSetChanged();
                                    chatView.scrollToPosition(adapter.getItemCount() - 1);
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}