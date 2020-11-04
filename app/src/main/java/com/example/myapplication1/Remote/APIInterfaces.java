package com.example.myapplication1.Remote;

import com.example.myapplication1.Model.AccountModel;
import com.example.myapplication1.Model.InvoiceModel;
import com.example.myapplication1.Model.PaymentModel;
import com.example.myapplication1.Model.UserLogIn;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIInterfaces {
    @GET("Invoices/GetAll")
    Call<List<InvoiceModel>> getAllInvoices();

    @POST("Users/Login")
    Call<AccountModel> loginUser(@Body UserLogIn user);

    @GET("Payment/GetPayments")
    Call<List<PaymentModel>> getAllPayments();
}
