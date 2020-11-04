package com.example.myapplication1.Model;

import com.google.gson.annotations.SerializedName;

public class DistrictModel {
    @SerializedName("districtId")
    private int districtId;
    @SerializedName("districtCode")
    private String districtCode;
    @SerializedName("districtName")
    private String districtName;
    @SerializedName("countryId")
    private int countryId;

    public DistrictModel(int districtId, String districtCode, String districtName, int countryId) {
        this.districtId = districtId;
        this.districtCode = districtCode;
        this.districtName = districtName;
        this.countryId = countryId;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}
