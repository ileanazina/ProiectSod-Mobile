package com.example.myapplication1.Model;

import com.google.gson.annotations.SerializedName;

public class AddressModel {
    @SerializedName("addressId")
    private int addressId;
    @SerializedName("addressType")
    private int addressType;
    @SerializedName("cityId")
    private int cityId;
    @SerializedName("accountId")
    private int accountId;
    @SerializedName("street")
    private String street;
    @SerializedName("flatNumber")
    private int flatNumber;
    @SerializedName("floorNumber\n")
    private int floorNumber;
    @SerializedName("stairNumber")
    private int stairNumber;

    public AddressModel(int addressId, int addressType, int cityId, int accountId, String street, int flatNumber,
                        int floorNumber, int stairNumber) {
        this.addressId = addressId;
        this.addressType = addressType;
        this.cityId = cityId;
        this.accountId = accountId;
        this.street = street;
        this.flatNumber = flatNumber;
        this.floorNumber = floorNumber;
        this.stairNumber = stairNumber;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getAddressType() {
        return addressType;
    }

    public void setAddressType(int addressType) {
        this.addressType = addressType;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(int flatNumber) {
        this.flatNumber = flatNumber;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public int getStairNumber() {
        return stairNumber;
    }

    public void setStairNumber(int stairNumber) {
        this.stairNumber = stairNumber;
    }
}
