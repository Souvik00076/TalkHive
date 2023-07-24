package com.example.talkhive.utilities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UpdateChatAdapter extends RecyclerView.Adapter<UpdateChatAdapter.ChatHolder> {
    private Context context;
    //private ArrayList<>
    public UpdateChatAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ChatHolder extends RecyclerView.ViewHolder {
        public ChatHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
