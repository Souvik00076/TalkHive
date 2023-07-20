package com.example.talkhive;

import static com.example.talkhive.utilities.firebaseutils.FirebaseChatUtils.loadChat;

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
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.talkhive.utilities.adapters.UpdateChatAdapter;
import com.example.talkhive.utilities.firebaseutils.FirebaseChatUtils;
import com.example.talkhive.utilities.model.MessageModel;
import com.example.talkhive.utilities.model.UpdateUserModel;
import com.example.talkhive.utilities.model.UserDetailsModel;
import com.example.talkhive.utilities.services.ChatService;
import com.example.talkhive.utilities.services.UpdateUserService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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
    private UserDetailsModel detailsModel;
    private FirebaseAuth auth;
    private UpdateUserModel model;
    private RecyclerView chatView;
    private UpdateChatAdapter adapter;
    private ArrayList<MessageModel> dataSet;
    private DatabaseReference reference;
    private String recipientEmail;
    public interface ChatCallBack{
        void onLoad(ArrayList<MessageModel> dataSet);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_screen_activity);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) actionBar.hide();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            model = (UpdateUserModel) extras.getParcelable("USER_DETAILS");
            recipientEmail = model.getEmail();
            init();
            chatName.setText(model.getName());
            detailsModel.getImageReference().child(recipientEmail.replace(".", "") + "/dp.jpg")
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
                if (!TextUtils.isEmpty(chatMessage)) {
                    MessageModel message = new MessageModel(auth.getCurrentUser().getEmail(),
                            model.getEmail(), chatMessage,
                            System.currentTimeMillis() / 1000
                    );
                    sendMessageEt.getText().clear();
                    Intent intent = new Intent(ChatScreen.this, ChatService.class);
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
        detailsModel = UserDetailsModel.getInstance();
        waitPbar = (ProgressBar) findViewById(R.id.wait_chat);
        auth = detailsModel.getAuth();
        chatView = findViewById(R.id.chat_view);
        adapter = new UpdateChatAdapter(this);
        dataSet = new ArrayList<>();
        chatView.setAdapter(adapter);
        chatView.setLayoutManager(new LinearLayoutManager(this));
        reference = detailsModel.getDatabaseReference().child("Users/" +
                auth.getCurrentUser().getEmail().replace(".", "") + "/contacts/"
                + recipientEmail.replace(".", ""));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    final String chatId = snapshot.child("chatId").getValue(String.class);
                    ChatCallBack callBack=new ChatCallBack() {
                        @Override
                        public void onLoad(ArrayList<MessageModel> dataSet) {
                            Log.i("New Main",dataSet.size()+"");
                            adapter.setDataSet(dataSet);
                            adapter.notifyDataSetChanged();
                        }
                    };
                    loadChat(chatId,callBack);

                    Log.i("ChatScreen",dataSet.size()+"");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}