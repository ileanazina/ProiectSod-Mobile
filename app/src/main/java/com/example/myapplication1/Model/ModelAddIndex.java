package com.example.myapplication1.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ModelAddIndex {

    @SerializedName("indexValue")
    private float indexValue;
    @SerializedName("addressId")
    private int addressId;
    @SerializedName("accountId")
    private int accountId;

    public ModelAddIndex(float indexValue, int addressId, int accountId) {
        this.indexValue = indexValue;
        this.addressId = addressId;
        this.accountId = accountId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public float getIndexValue() {
        return indexValue;
    }

    public void setIndexValue(float indexValue) {
        this.indexValue = indexValue;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
}
