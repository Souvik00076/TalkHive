package com.example.talkhive.utilities.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.DialogFragment;

import com.example.talkhive.R;
import com.example.talkhive.utilities.model.UpdateUserModel;
import com.example.talkhive.utilities.services.UpdateUserService;

public class ProfileUserDialog extends DialogFragment {
    private AppCompatImageView dpIv;
    private TextView userView;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.profile_user_dialog, null);
        dpIv = dialogView.findViewById(R.id.dp_iv);
        userView = dialogView.findViewById(R.id.user_id);
        builder.setView(dialogView);
        builder.setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //final String emailText = emailEt.getText().toString();
                //final String nameText = nameEt.getText().toString();
                //Intent intent = new Intent(getContext(), UpdateUserService.class);
                //intent.putExtra(QUERY_TAG, new UpdateUserModel(emailText, nameText, null));
                //requireActivity().startService(intent);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ProfileUserDialog.this.getDialog().cancel();
            }
        });
        return builder.create();
    }
}
