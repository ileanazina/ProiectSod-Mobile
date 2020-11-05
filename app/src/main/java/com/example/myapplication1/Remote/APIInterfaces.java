package com.example.myapplication1.Remote;

import com.example.myapplication1.Model.AccountModel;
import com.example.myapplication1.Model.IndexModel;
import com.example.myapplication1.Model.InvoiceModel;
import com.example.myapplication1.Model.ModelAddIndex;
import com.example.myapplication1.Model.PaymentModel;
import com.example.myapplication1.Model.UserLogIn;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterfaces {

    //@Multipart
    @GET("Index/GetIndexesByAccountId/{accountId}")
    Call<List<IndexModel>> getAllIndexis(@Path("accountId") int accountId);

    @GET("Invoices/GetAll")
    Call<List<InvoiceModel>> getAllInvoices();

    @POST("Users/Login")
    Call<AccountModel> loginUser(@Body UserLogIn user);

    @GET("Payment/GetPayments")
    Call<List<PaymentModel>> getAllPayments();

    @POST("Index/AddIndexToAccountId")
    Call<ModelAddIndex> insertIndex(@Field("accountId") int accountId,@Field("addresstId") int addresstId,@Field("indexValue") float indexValue);

}
