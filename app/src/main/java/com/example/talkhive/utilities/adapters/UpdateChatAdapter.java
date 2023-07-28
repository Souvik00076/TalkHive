package com.example.talkhive.utilities.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.talkhive.R;
import com.example.talkhive.fragments.ChatFragment;
import com.example.talkhive.utilities.firebaseutils.FirebaseRecyclerUtils;
import com.example.talkhive.utilities.interfaces.FirebaseRecyclerViewCallbacks;
import com.example.talkhive.utilities.interfaces.GeneralCallbacks;
import com.example.talkhive.utilities.model.ChatModel;
import com.example.talkhive.utilities.model.UserToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class UpdateChatAdapter extends RecyclerView.Adapter<UpdateChatAdapter.ChatHolder> {
    private ChatFragment context;
    private ArrayList<ChatModel> dataSet;
    private final UserToken token;
    private FirebaseRecyclerViewCallbacks listener;

    public UpdateChatAdapter(Fragment context) {
        this.context = (ChatFragment) context;
        listener = (FirebaseRecyclerViewCallbacks) context;
        dataSet = new ArrayList<>();
        token = UserToken.getInstance();
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item_view, parent, false);
        return new ChatHolder(root, listener);

    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
        holder.bindView(dataSet.get(position));
    }

    public void setDataSet(ArrayList<ChatModel> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class ChatHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private AppCompatImageView dpView;
        private TextView recipientName, timeStamp;
        private FirebaseRecyclerViewCallbacks listener;

        public ChatHolder(@NonNull View itemView, final FirebaseRecyclerViewCallbacks
                listener) {

            super(itemView);
            this.listener = listener;
            itemView.setOnClickListener(this);
            dpView = itemView.findViewById(R.id.chat_bubble_user_dp);
            recipientName = itemView.findViewById(R.id.name_tv);
            timeStamp = itemView.findViewById(R.id.time_stamp);
        }

        @Override
        public void onClick(View view) {
            listener.onClickListener(dataSet.get(getAdapterPosition()));
        }

        public void bindView(final ChatModel model) {

            String flag = model.getSender();
            recipientName.setText(flag);
            adjustDp(model.getSender());
            GeneralCallbacks callbacks = new GeneralCallbacks() {
                @Override
                public void getSignupFlag(boolean flag, int errorCode) {

                }

                @Override
                public void getFriendName(String name) {
                    recipientName.setText(name);
                }

                @Override
                public void getLoginFlag(boolean flag, int errorCode) {

                }
            };
            FirebaseRecyclerUtils.getNameOfFriend(flag, callbacks);
        }

        private void adjustDp(final String recipient) {
            token.getImageReference().child
                            (recipient.replace(".", "") + "/dp.jpg")
                    .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(context).load(uri).into(dpView);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dpView.setImageResource(R.drawable.dummy);
                        }
                    });
        }
    }
}
