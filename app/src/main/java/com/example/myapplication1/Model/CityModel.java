package com.example.myapplication1.Model;

import com.google.gson.annotations.SerializedName;

public class CityModel {
    @SerializedName("cityId")
    private int cityId;
    @SerializedName("cityCode")
    private String cityCode;
    @SerializedName("cityName")
    private String cityName;
    @SerializedName("districtId")
    private int districtId;

    public CityModel(int cityId, String cityCode, String cityName, int districtId) {
        this.cityId = cityId;
        this.cityCode = cityCode;
        this.cityName = cityName;
        this.districtId = districtId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }
}
