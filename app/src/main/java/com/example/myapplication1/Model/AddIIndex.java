package com.example.myapplication1.Model;

import android.text.Editable;

public class AddIIndex {
    private float indexValue;
    private int accountId;
    private int addressId;

    public AddIIndex(float indexValue, int accountId, int addressId) {
        this.indexValue = indexValue;
        this.accountId = accountId;
        this.addressId = addressId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public float getIndexValue() {
        return indexValue;
    }

    public void setIndexValue(float indexValue) {
        this.indexValue = indexValue;
    }
}
