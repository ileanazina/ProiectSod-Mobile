package com.example.myapplication1.Model;

public class UserLogIn {
    private String userName;
    private String password;

    public UserLogIn(){

    }

    public UserLogIn(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
