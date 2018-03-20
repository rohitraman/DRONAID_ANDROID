package com.dronaid.dronaid;

/**
 * Created by bennyhawk on 12/19/17.
 */

public class SyncResponseModel {
    private boolean success;
    private String username;
    private String password;
    private String accounttype;

    public boolean isSuccess() {
        return success;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAccounttype() {
        return accounttype;
    }
}
