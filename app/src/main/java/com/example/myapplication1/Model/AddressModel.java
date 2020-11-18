package com.example.myapplication1.Model;

import com.google.gson.annotations.SerializedName;

public class AddressModel {
    @SerializedName("addressId")
    private int addressId;
    @SerializedName("addressType")
    private int addressType;
    @SerializedName("cityId")
    private int cityId;
    @SerializedName("contractId")
    private int contractId;
    @SerializedName("street")
    private String street;
    @SerializedName("streetNumber")
    private String streetNumber;
    @SerializedName("immobileNumber")
    private String immobileNumber;
    @SerializedName("flatNumber")
    private int flatNumber;
    @SerializedName("floorNumber")
    private int floorNumber;
    @SerializedName("stairNumber")
    private String stairNumber;
    @SerializedName("fullAddressName")
    private String fullAddressName;

    public AddressModel(int addressId, int addressType, int cityId, int contractId, String street,
                        String streetNumber, String immobileNumber, int flatNumber,
                        int floorNumber, String stairNumber, String fullAddressName) {
        this.addressId = addressId;
        this.addressType = addressType;
        this.cityId = cityId;
        this.contractId = contractId;
        this.street = street;
        this.streetNumber = streetNumber;
        this.immobileNumber = immobileNumber;
        this.flatNumber = flatNumber;
        this.floorNumber = floorNumber;
        this.stairNumber = stairNumber;
        this.fullAddressName = fullAddressName;
    }

    public String getFullAddressName() {
        return fullAddressName;
    }

    public void setFullAddressName(String fullAddressName) {
        this.fullAddressName = fullAddressName;
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

    public int geContactId() {
        return contractId;
    }

    public void setContactId(int contractId) {
        this.contractId = contractId;
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

    public String getStairNumber() {
        return stairNumber;
    }

    public void setStairNumber(String stairNumber) {
        this.stairNumber = stairNumber;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getImmobileNumber() {
        return immobileNumber;
    }

    public void setImmobileNumber(String immobileNumber) {
        this.immobileNumber = immobileNumber;
    }
}
