package com.example.talkhive.utilities;

import java.util.HashMap;

public class ErrorCodes {
    private HashMap<Integer, String> map;
    private static ErrorCodes errorCodes;

    private ErrorCodes() {
        map = new HashMap<>();
        map.put(101, "Email Not Send!!");
        map.put(102, "Something wrong happened while signing up");
        map.put(103, "Network Interrupted");
        map.put(18, "Invalid Email");
        map.put(19, "Passwords do not match");
        map.put(104, "Mail Not Verified. Check Your Gmail ");
        map.put(105, "Login Error");
        map.put(106, "Verification Mail Could not be sent");
    }

    public static HashMap<Integer, String> getMap() {
        if (errorCodes == null) errorCodes = new ErrorCodes();
        return errorCodes.map;
    }


}
