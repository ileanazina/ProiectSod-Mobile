package com.example.myapplication1.Remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClientLogIn {

    private static Retrofit instance;

    public static Retrofit getInstance()
    {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        if(instance==null)
            instance= new Retrofit.Builder().baseUrl("http://10.1.2.88:62297/").addConverterFactory(
                    ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        return instance;
    }
}
