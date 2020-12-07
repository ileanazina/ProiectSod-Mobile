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
import com.example.myapplication1.Model.TokenModel;
import com.example.myapplication1.Model.UnitMeasurementsModel;
import com.example.myapplication1.Model.UserEdit;
import com.example.myapplication1.Model.UserLogIn;
import com.example.myapplication1.Model.UserUploadModel;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIInterfaces {

    @GET("Index/GetIndexesByAccountIdAndAddressId/{addressId}")
    Call<List<IndexModel>> getAllIndexis(@Header("Authorization") String token, @Path("addressId") int addressId);

    @POST("/Invoices/GetInvoicesByAccountId")
    Call<List<InvoiceModel>> getInvoicesByAccountId(@Header("Authorization") String token, @Body InvoiceFilter invoiceFilter);

    @GET("Invoices/GetSold/{addressId}")
    Call<Float> getSold(@Header("Authorization") String token, @Path("addressId") int addressId);

    @POST("Users/Login")
    Call<AccountModel> loginUser(@Body UserLogIn user);

    @POST("Users/EditUser")
    Call<Void> editUser(@Header("Authorization") String token, @Body UserEdit user);

    @POST("Index/AddIndexToAccountId")
    Call<IndexModel> insertIndex(@Header("Authorization") String token, @Body AddIIndex index);

    @GET("InvoiceDetail/GetInvoiceDetails/{invoiceId}")
    Call<InvoiceDetailsModel> getInvoiceDetailsByInvoice(@Header("Authorization") String token, @Path("invoiceId") int invoiceId);

    @GET("Address/GetAddressesByAccountId")
    Call<List<AddressModel>> getAddressesByAccountId(@Header("Authorization") String token);

    @GET("UnitMeasurements/GetUnitMeasurementsByUMId/{umId}")
    Call<UnitMeasurementsModel> getUnitMeasurementsByUMId(@Header("Authorization") String token, @Path("umId") int umId);

    @GET("Dropbox/DownloadInvoice/{invoiceId}")
    Call<Void> sentInvoiceByEmail(@Header("Authorization") String token, @Path("invoiceId") int invoiceId);

    @POST("Document")
    Call<List<DocumentDownloadModel>> downloadAllDocuments(@Header("Authorization") String token, @Body DownloadFilter downloadFilter);

    @POST("Dropbox/MobileUserUpload")
    Call<Void> uploadDoc(@Header("Authorization") String token, @Body UserUploadModel file);

    @POST("RefreshToken")
    Call<AccountModel> refreshToken(@Body TokenModel token);
}
