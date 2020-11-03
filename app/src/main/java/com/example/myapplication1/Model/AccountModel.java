package com.example.myapplication1.Model;

import com.google.gson.annotations.SerializedName;

public class AccountModel {
    @SerializedName("accountId")
    private int accountId;
    @SerializedName("userId")
    private int userId;
    @SerializedName("isIndividual")
    private boolean isIndividual;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("email")
    private String email;
    @SerializedName("telephoneNumber")
    private long telephoneNumber;
    @SerializedName("cnp")
    private long cnp;
    @SerializedName("cui")
    private long cui;
    @SerializedName("companyName")
    private String companyName;

    public AccountModel(int accountId, int userId, boolean isIndividual, String firstName, String lastName,
                        String email, long telephoneNumber, long cnp, long cui, String companyName) {
        this.accountId = accountId;
        this.userId = userId;
        this.isIndividual = isIndividual;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
        this.cnp = cnp;
        this.cui = cui;
        this.companyName = companyName;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setIndividual(boolean individual) {
        isIndividual = individual;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelephoneNumber(long telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public void setCnp(long cnp) {
        this.cnp = cnp;
    }

    public void setCui(long cui) {
        this.cui = cui;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getUserId() {
        return userId;
    }

    public boolean isIndividual() {
        return isIndividual;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public long getTelephoneNumber() {
        return telephoneNumber;
    }

    public long getCnp() {
        return cnp;
    }

    public long getCui() {
        return cui;
    }

    public String getCompanyName() {
        return companyName;
    }
}
