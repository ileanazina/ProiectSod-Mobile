package com.example.myapplication1.Remote;

import com.example.myapplication1.Activities.Invoice.InvoiceDetails;
import com.example.myapplication1.Model.AccountModel;
import com.example.myapplication1.Model.AddressModel;
import com.example.myapplication1.Model.CityModel;
import com.example.myapplication1.Model.CountryModel;
import com.example.myapplication1.Model.DistrictModel;
import com.example.myapplication1.Model.IndexModel;
import com.example.myapplication1.Model.InvoiceDetailsModel;
import com.example.myapplication1.Model.InvoiceModel;
import com.example.myapplication1.Model.PaymentModel;
import com.example.myapplication1.Model.UnitMeasurementsModel;
import com.example.myapplication1.Model.UserLogIn;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterfaces {

    @GET("Invoices/GetAll")
    Call<List<IndexModel>> getAllIndexis();

    @GET("Invoices/GetAll")
    Call<List<InvoiceModel>> getAllInvoices();

    @POST("Users/Login")
    Call<AccountModel> loginUser(@Body UserLogIn user);

    @GET("Payment/GetPayments")
    Call<List<PaymentModel>> getAllPayments();

    @GET("InvoiceDetail/GetInvoiceDetails/{invoiceId}")
    Call<InvoiceDetailsModel> getInvoiceDetailsByInvoice(@Path("invoiceId") int invoiceId);

    @GET("Address/GetAddressesByAccountId/{accountId}")
    Call<List<AddressModel>> getAddressesByAccountId(@Path("accountId") int accountId);

    @GET("City/GetCityByCityId/{cityId}")
    Call<CityModel> getCityByCityId(@Path("cityId") int cityId);

    @GET("District/GetDistrictByDistrictId/{districtId}")
    Call<DistrictModel> getDistrictByDistrictId(@Path("districtId") int districtId);

    @GET("Country/GetCountryByCountryId/{countryId}")
    Call<CountryModel> getCountryByCountryId(@Path("countryId") int countryId);

    @GET("UnitMeasurements/GetUnitMeasurementsByUMId/{umId}")
    Call<UnitMeasurementsModel> getUnitMeasurementsByUMId(@Path("umId") int umId);
}
