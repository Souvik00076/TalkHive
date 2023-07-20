package com.example.talkhive.utilities.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talkhive.R;
import com.example.talkhive.utilities.model.MessageModel;
import com.example.talkhive.utilities.model.UserDetailsModel;

import java.util.ArrayList;

public class UpdateChatAdapter extends RecyclerView.Adapter<UpdateChatAdapter.MessageHolder> {
    private static final String CLASS_NAME = UpdateChatAdapter.class.getName();
    private ArrayList<MessageModel> dataSet;
    private Context context;
    private UserDetailsModel detailsModel;

    public UpdateChatAdapter(Context context) {
        this.context = context;
        dataSet = new ArrayList<>();
        detailsModel = UserDetailsModel.getInstance();
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.chat_box, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        holder.bindView(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setDataSet(final ArrayList<MessageModel> dataSet) {
        this.dataSet = dataSet;
        System.out.println("bind called");
    }

    class MessageHolder extends RecyclerView.ViewHolder {
        private TextView senderMessage, recieverMessage, getSenderMessageTime, recieverMessageTime;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            senderMessage = itemView.findViewById(R.id.sender_message);
            getSenderMessageTime = itemView.findViewById(R.id.sender_message_time);
            recieverMessage = itemView.findViewById(R.id.reciever_message);
            recieverMessageTime = itemView.findViewById(R.id.reciever_message_time);
        }

        public void bindView(MessageModel model) {

            final String sender = model.getSenderId();
            final String owner = detailsModel.getAuth().getCurrentUser().getEmail();
            if (sender.equals(owner)) {
                recieverMessage.setVisibility(View.GONE);
                recieverMessageTime.setVisibility(View.GONE);
                senderMessage.setText(model.getMessage());
                getSenderMessageTime.setText(model.getTimeStamp() + "");
            } else {
                senderMessage.setVisibility(View.GONE);
                getSenderMessageTime.setVisibility(View.GONE);
                recieverMessage.setText(model.getMessage());
                recieverMessageTime.setText(model.getTimeStamp() + "");
            }
        }
    }
}
