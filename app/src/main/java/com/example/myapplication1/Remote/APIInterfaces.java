package com.example.myapplication1.Remote;

import com.example.myapplication1.Model.AccountModel;
import com.example.myapplication1.Model.AddIIndex;
import com.example.myapplication1.Model.AddressModel;
import com.example.myapplication1.Model.DocumentDownloadModel;
import com.example.myapplication1.Model.DownloadFilter;
import com.example.myapplication1.Model.IndexModel;
import com.example.myapplication1.Model.InvoiceDetailsModel;
import com.example.myapplication1.Model.InvoiceModel;
import com.example.myapplication1.Model.InvoiceFilter;
import com.example.myapplication1.Model.UnitMeasurementsModel;
import com.example.myapplication1.Model.UserEdit;
import com.example.myapplication1.Model.UserLogIn;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIInterfaces {

    @GET("Index/GetIndexesByAccountIdAndAddressId/{accountId}/{addressId}")
    Call<List<IndexModel>> getAllIndexis(@Path("accountId") int accountId, @Path("addressId") int addressId);

    @POST("/Invoices/GetInvoicesByAccountId")
    Call<List<InvoiceModel>> getInvoicesByAccountId(@Body InvoiceFilter invoiceFilter);

    @GET("Invoices/GetSold/{accountId}/{addressId}")
    Call<Float> getSold(@Path("accountId") int accountId, @Path("addressId") int addressId);

    @POST("Users/Login")
    Call<AccountModel> loginUser(@Body UserLogIn user);

    @POST("Users/EditUser")
    Call<Void> editUser(@Body UserEdit user);

    @POST("Index/AddIndexToAccountId")
    Call<IndexModel> insertIndex(@Body AddIIndex index);

    @POST("Dropbox/Upload")
    Call<DocumentDownloadModel> uploadDoc(@Body DocumentDownloadModel file);

    @GET("InvoiceDetail/GetInvoiceDetails/{invoiceId}")
    Call<InvoiceDetailsModel> getInvoiceDetailsByInvoice(@Path("invoiceId") int invoiceId);

    @GET("Address/GetAddressesByAccountId/{accountId}")
    Call<List<AddressModel>> getAddressesByAccountId(@Path("accountId") int accountId);

    @GET("UnitMeasurements/GetUnitMeasurementsByUMId/{umId}")
    Call<UnitMeasurementsModel> getUnitMeasurementsByUMId(@Path("umId") int umId);

    @GET("Dropbox/DownloadInvoice/{accountId}/{invoiceId}")
    Call<Void> sentInvoiceByEmail(@Path("accountId") int accountId, @Path("invoiceId") int invoiceId);

    @POST("Document")
    Call<List<DocumentDownloadModel>> downloadAllDocuments(@Body DownloadFilter downloadFilter);
}
