package com.example.myapplication1.Model;

import com.google.gson.annotations.SerializedName;

public class TokenModel {
    @SerializedName("jwtToken")
    private String jwtToken;
    @SerializedName("refreshToken")
    private String refreshToken;

    public TokenModel(String jwtToken, String refreshToken) {
        this.jwtToken = jwtToken;
        this.refreshToken = refreshToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
