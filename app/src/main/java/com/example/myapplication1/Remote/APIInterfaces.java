package com.example.myapplication1.Remote;

import com.example.myapplication1.Model.InvoiceModel;
import com.example.myapplication1.Model.UserLogIn;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIInterfaces {
    @GET("Invoices/GetAll")
    Call<List<InvoiceModel>> getAllInvoices(@Body InvoiceModel body);

    @POST("Users/Login")
    Observable<String> loginUser(@Body UserLogIn user);
}
