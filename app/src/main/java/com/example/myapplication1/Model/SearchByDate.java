package com.example.myapplication1.Model;

import com.google.gson.annotations.SerializedName;

public class SearchByDate {
    @SerializedName("accountId")
    public int accountId;
    @SerializedName("addressId")
    private int addressId;
    @SerializedName("date1")
    private String startDate;
    @SerializedName("date2")
    private String endDate;

    public SearchByDate(int accountId, int addressId, String startDate, String endDate) {
        this.accountId = accountId;
        this.addressId = addressId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
}
