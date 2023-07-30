package com.example.talkhive.utilities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talkhive.R;
import com.example.talkhive.utilities.GeneralUtils;
import com.example.talkhive.utilities.model.MessageModel;
import com.example.talkhive.utilities.model.UserToken;

import java.util.ArrayList;

public class UpdateChatScreenAdapter extends RecyclerView.Adapter<UpdateChatScreenAdapter.MessageHolder> {
    private static final String CLASS_NAME = UpdateChatScreenAdapter.class.getName();
    private ArrayList<MessageModel> dataSet;
    private Context context;
    private UserToken detailsModel;

    public UpdateChatScreenAdapter(Context context) {
        this.context = context;
        dataSet = new ArrayList<>();
        detailsModel = UserToken.getInstance();
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
            recieverMessage.setVisibility(View.GONE);
            recieverMessageTime.setVisibility(View.GONE);
            senderMessage.setVisibility(View.GONE);
            getSenderMessageTime.setVisibility(View.GONE);
            final String sender = model.getSenderId();
            final String owner = detailsModel.getAuth().getCurrentUser().getEmail();
            final String timeStamp= GeneralUtils.convertUnixTimeStamp(model.getTimeStamp());
            if (sender.equals(owner)) {
                senderMessage.setVisibility(View.VISIBLE);
                getSenderMessageTime.setVisibility(View.VISIBLE);
                senderMessage.setText(model.getMessage());
                getSenderMessageTime.setText(timeStamp);
            } else {
                recieverMessage.setVisibility(View.VISIBLE);
                recieverMessageTime.setVisibility(View.VISIBLE);
                recieverMessage.setText(model.getMessage());
                recieverMessageTime.setText(timeStamp);

            }
        }
    }
}
