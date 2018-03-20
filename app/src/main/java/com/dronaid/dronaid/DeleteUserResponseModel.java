package com.dronaid.dronaid;

/**
 * Created by bennyhawk on 12/20/17.
 */

public class DeleteUserResponseModel {
    private boolean success;
    private boolean usernameExistsPassed;
    private boolean credentialsPassed;
    private boolean deleteSuccessful;

    public boolean isSuccess() {
        return success;
    }

    public boolean isUsernameExistsPassed() {
        return usernameExistsPassed;
    }

    public boolean isCredentialsPassed() {
        return credentialsPassed;
    }

    public boolean isDeleteSuccessful() {
        return deleteSuccessful;
    }
}
