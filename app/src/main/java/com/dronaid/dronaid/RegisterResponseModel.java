package com.dronaid.dronaid;

/**
 * Created by bennyhawk on 12/20/17.
 */

public class RegisterResponseModel {
    private boolean success;
    private String token;
    private boolean usernamePassed;
    private boolean passwordPassed;
    private boolean accountTypePassed;

    public boolean isSuccess() {
        return success;
    }

    public String getToken() {
        return token;
    }

    public boolean isUsernamePassed() {
        return usernamePassed;
    }

    public boolean isPasswordPassed() {
        return passwordPassed;
    }

    public boolean isAccountTypePassed() {
        return accountTypePassed;
    }
}
