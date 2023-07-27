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
import com.example.talkhive.utilities.model.ChatModel;
import com.example.talkhive.utilities.model.MessageModel;
import com.example.talkhive.utilities.model.UserToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class UpdateChatAdapter extends RecyclerView.Adapter<UpdateChatAdapter.ChatHolder> {
    private ChatFragment context;
    private ArrayList<MessageModel> dataSet;
    private final UserToken token;

    public UpdateChatAdapter(Fragment context) {

        this.context = (ChatFragment) context;
        dataSet = new ArrayList<>();
        token = UserToken.getInstance();
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item_view, parent, false);
        return new ChatHolder(root);

    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
        holder.bindView(dataSet.get(position));
    }

    public void setDataSet(ArrayList<MessageModel> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ChatHolder extends RecyclerView.ViewHolder {
        private AppCompatImageView dpView;
        private TextView recipientName, timeStamp;

        public ChatHolder(@NonNull View itemView) {

            super(itemView);
            dpView = itemView.findViewById(R.id.chat_bubble_user_dp);
            recipientName = itemView.findViewById(R.id.name_tv);
            timeStamp = itemView.findViewById(R.id.time_stamp);
        }

        public void bindView(final MessageModel model) {

            String flag = token.getAuth().getCurrentUser().getEmail();
            if (model.getSenderId().equals(flag)) recipientName.setText(model.getRecieverId());
            else recipientName.setText(model.getSenderId());
            adjustDp(recipientName.getText().toString());
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
