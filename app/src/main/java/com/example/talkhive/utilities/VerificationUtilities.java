package com.example.talkhive.utilities;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;

import com.example.talkhive.MainActivity;

public class VerificationUtilities {
    final static String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    public static boolean verifyEmail(final String email) {
        return email.matches(emailPattern);
    }

    public static boolean checkEqualityPassword(final String pass1, final String pass2) {
        return !(pass1.isEmpty() || pass2.isEmpty() || !pass1.equals(pass2));
    }

    public static void replaceInMain(Fragment fragment, Context context) {
        MainActivity activity = (MainActivity) context;
        activity.addOrReplace(fragment);

    }
}
