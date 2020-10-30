package com.example.myapplication1.Remote;

import com.example.myapplication1.Model.UserLogIn;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LogInAPI {

    @POST("Users/Login")
    Observable<String> loginUser(@Body UserLogIn user);


}
