package com.dronaid.dronaid;

/**
 * Created by bennyhawk on 12/20/17.
 */

public class UpdateUserResponseModel {
    private boolean success;
    private boolean usernameExistsPassed;
    private boolean credentialsPassed;
    private boolean updateSuccessful;

    public boolean isSuccess() {
        return success;
    }

    public boolean isUsernameExistsPassed() {
        return usernameExistsPassed;
    }

    public boolean isCredentialsPassed() {
        return credentialsPassed;
    }

    public boolean isUpdateSuccessful() {
        return updateSuccessful;
    }
}
