package com.example.talkhive.utilities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talkhive.R;
import com.example.talkhive.utilities.model.UpdateUserModel;

import java.util.ArrayList;

public class UpdateUserAdapter extends RecyclerView.Adapter<UpdateUserAdapter.UserHolder> {

    private ArrayList<UpdateUserModel> dataSet;
    private Context context;

    public UpdateUserAdapter(Context context) {
        this.context = context;
        dataSet = new ArrayList<>();
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserHolder(LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.rv_chat_bubble_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        holder.bindView(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setDataSet(final ArrayList<UpdateUserModel> dataSet) {
        this.dataSet = dataSet;
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        private AppCompatImageView displayUserView;
        private TextView displayNameView;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            displayUserView = itemView.findViewById(R.id.chat_bubble_user_dp);
            displayNameView = itemView.findViewById(R.id.name_tv);
        }

        public void bindView(final UpdateUserModel model) {
            displayNameView.setText(model.getName());
        }
    }
}
