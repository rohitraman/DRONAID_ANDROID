package com.dronaid.dronaid;

/**
 * Created by bennyhawk on 12/18/17.
 */

public class SigninResponseModel {
    private boolean success;
    private String token;
    private boolean usernameExistsPassed;
    private boolean credentialsPassed;

    public boolean isSuccess() {
        return success;
    }

    public String getToken() {
        return token;
    }

    public boolean isUsernameExistsPassed() {
        return usernameExistsPassed;
    }

    public boolean isCredentialsPassed() {
        return credentialsPassed;
    }
}
