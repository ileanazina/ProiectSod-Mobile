package com.example.myapplication1.Model;

import com.google.gson.annotations.SerializedName;

public class CountryModel {
    @SerializedName("countryId")
    private int countryId;
    @SerializedName("countryCode")
    private String countryCode;
    @SerializedName("countryName")
    private String countryName;

    public CountryModel(int countryId, String countryCode, String countryName) {
        this.countryId = countryId;
        this.countryCode = countryCode;
        this.countryName = countryName;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
