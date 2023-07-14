package com.example.talkhive.utilities.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.talkhive.R;
import com.example.talkhive.utilities.model.UpdateUserModel;
import com.example.talkhive.utilities.services.UpdateUserService;

public class AddUserDialog extends DialogFragment {
    private static final String CLASS_NAME = AddUserDialog.class.getName();
    private static final String TITLE_TAG = "Add User";
    private static final String QUERY_TAG = "QUERY";
    private EditText emailEt, nameEt;

    //private Context context;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_user_dialog, null);
        emailEt = dialogView.findViewById(R.id.email_et);
        nameEt = dialogView.findViewById(R.id.pet_name_et);
        builder.setView(dialogView);
        builder.setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Log.i(CLASS_NAME, "User Added");
                final String emailText = emailEt.getText().toString();
                final String nameText = nameEt.getText().toString();
                Intent intent = new Intent(getContext(), UpdateUserService.class);
                intent.putExtra(QUERY_TAG, new UpdateUserModel(emailText, nameText, null));
                requireActivity().startService(intent);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AddUserDialog.this.getDialog().cancel();
            }
        });
        return builder.create();
    }
}
