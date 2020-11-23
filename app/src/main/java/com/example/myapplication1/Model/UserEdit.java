package com.example.myapplication1.Model;

import com.google.gson.annotations.SerializedName;

public class UserEdit {
    @SerializedName("accountId")
    private int accountId;
    @SerializedName("email")
    private String email;
    @SerializedName("telephoneNumber")
    private long telephoneNumber;

    public UserEdit(int accountId, String email, long telephoneNumber) {
        this.accountId = accountId;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(long telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }
}
