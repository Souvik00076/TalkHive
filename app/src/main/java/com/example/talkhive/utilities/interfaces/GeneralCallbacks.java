package com.example.talkhive.utilities.interfaces;

public interface GeneralCallbacks {
    void getSignupFlag(boolean flag, final int errorCode);
    void getFriendName(final String name);
    void getLoginFlag(boolean flag, final int errorCode);
}
