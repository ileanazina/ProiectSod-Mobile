package com.example.myapplication1.Activities.Invoice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.example.myapplication1.Model.AccountModel;
import com.example.myapplication1.Model.AddressModel;
import com.example.myapplication1.Model.CityModel;
import com.example.myapplication1.Model.CountryModel;
import com.example.myapplication1.Model.DistrictModel;
import com.example.myapplication1.Model.InvoiceDetailsModel;
import com.example.myapplication1.Model.InvoiceModel;
import com.example.myapplication1.Model.UnitMeasurementsModel;
import com.example.myapplication1.R;
import com.example.myapplication1.Remote.APIInterfaces;
import com.example.myapplication1.Remote.RetrofitClientLogIn;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceDetails extends AppCompatActivity {

    public interface RevealDetailsCallbacks {
        void getDataFromInvoiceDetails(InvoiceDetailsModel invoiceDetails);
        void getDataFromAddress (List<AddressModel> address);
        void getDataFromCity(CityModel city);
        void getDataFromDistrict(DistrictModel district);
        void getDataFromCountry(CountryModel country);
        void getDataFromUnitMeasurement(UnitMeasurementsModel unitMeasurements);
    }

    private RevealDetailsCallbacks callback;
    private AccountModel account;

    private InvoiceModel invoice_obj;
    private InvoiceDetailsModel invoiceDetails_obj;
    private AddressModel address_obj;
    private CityModel city_obj;
    private DistrictModel district_obj;
    private CountryModel country_obj;
    private UnitMeasurementsModel unitMeasurements_obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_details);

        final ProgressDialog dialog = ProgressDialog.show(this, null, "Va rog sa asteptati");
        Intent i = getIntent();
        invoice_obj = (InvoiceModel) i.getSerializableExtra("extra");

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = mPrefs.getString("AccountInfo",null);
        account = gson.fromJson(json, AccountModel.class);

        this.callback = new RevealDetailsCallbacks() {
            @Override
            public void getDataFromInvoiceDetails(InvoiceDetailsModel invoiceDetails) {
                invoiceDetails_obj = invoiceDetails;
                if(invoiceDetails != null)
                    getUnitMeasurementFromInvoiceDetail(InvoiceDetails.this, callback);
                else getAddressByAccount(InvoiceDetails.this, callback);
            }

            @Override
            public void getDataFromUnitMeasurement(UnitMeasurementsModel unitMeasurements) {
                unitMeasurements_obj = unitMeasurements;
                getAddressByAccount(InvoiceDetails.this, callback);
            }

            @Override
            public void getDataFromAddress(List<AddressModel> address) {
                for (int i = 0; i < address.size(); i++)
                    if (invoice_obj.getAddressId() == address.get(i).getAddressId()) {
                        address_obj = address.get(i);
                        break;
                    }
                getCityFromAddress(InvoiceDetails.this, callback);
            }

            @Override
            public void getDataFromCity(CityModel city) {
                city_obj = city;
                getDistrictFromCity(InvoiceDetails.this, callback);
            }

            @Override
            public void getDataFromDistrict(DistrictModel district) {
                district_obj = district;
                getCountryFromDistrict(InvoiceDetails.this, callback);
            }

            @Override
            public void getDataFromCountry(CountryModel country) {
                country_obj = country;
                completeTheView();
                dialog.dismiss();
            }
        };

        getInvoiceDetail(this, callback);
    }

    public void getInvoiceDetail(Context context, RevealDetailsCallbacks callback) {
        APIInterfaces invoiceAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
        Call<InvoiceDetailsModel> call = invoiceAPI.getInvoiceDetailsByInvoice(invoice_obj.getInvoiceId());
        call.enqueue(new Callback<InvoiceDetailsModel>() {
            @Override
            public void onResponse(Call<InvoiceDetailsModel> call, Response<InvoiceDetailsModel> response) {
                InvoiceDetailsModel invoiceDetails = response.body();
                if(callback != null) {
                    callback.getDataFromInvoiceDetails(invoiceDetails);
                }
            }

            @Override
            public void onFailure(Call<InvoiceDetailsModel> call, Throwable t) {
                call.cancel();
                Log.d("eroare", t.toString());
            }
        });
    }

    public void getAddressByAccount(Context context, RevealDetailsCallbacks callback) {
        APIInterfaces invoiceAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
        Call<List<AddressModel>> call = invoiceAPI.getAddressesByAccountId(account.getAccountId());
        call.enqueue(new Callback<List<AddressModel>>() {
            @Override
            public void onResponse(Call<List<AddressModel>> call, Response<List<AddressModel>> response) {
                List<AddressModel> address = response.body();
                if(callback != null) {
                    callback.getDataFromAddress(address);
                }
            }

            @Override
            public void onFailure(Call<List<AddressModel>> call, Throwable t) {
                call.cancel();
                Log.d("eroare", t.toString());
            }
        });
    }

    public void getCityFromAddress(Context context, RevealDetailsCallbacks callback) {
        APIInterfaces invoiceAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
        Call<CityModel> call = invoiceAPI.getCityByCityId(address_obj.getCityId());
        call.enqueue(new Callback<CityModel>() {
            @Override
            public void onResponse(Call<CityModel> call, Response<CityModel> response) {
                CityModel city = response.body();
                if(callback != null) {
                    callback.getDataFromCity(city);
                }
            }

            @Override
            public void onFailure(Call<CityModel> call, Throwable t) {
                call.cancel();
                Log.d("eroare", t.toString());
            }
        });
    }

    public void getDistrictFromCity(Context context, RevealDetailsCallbacks callback) {
        APIInterfaces invoiceAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
        Call<DistrictModel> call = invoiceAPI.getDistrictByDistrictId(city_obj.getDistrictId());
        call.enqueue(new Callback<DistrictModel>() {
            @Override
            public void onResponse(Call<DistrictModel> call, Response<DistrictModel> response) {
                DistrictModel district = response.body();
                if(callback != null) {
                    callback.getDataFromDistrict(district);
                }
            }

            @Override
            public void onFailure(Call<DistrictModel> call, Throwable t) {
                call.cancel();
                Log.d("eroare", t.toString());
            }
        });
    }

    public void getCountryFromDistrict(Context context, RevealDetailsCallbacks callback) {
        APIInterfaces invoiceAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
        Call<CountryModel> call = invoiceAPI.getCountryByCountryId(district_obj.getCountryId());
        call.enqueue(new Callback<CountryModel>() {
            @Override
            public void onResponse(Call<CountryModel> call, Response<CountryModel> response) {
                CountryModel country = response.body();
                if(callback != null) {
                    callback.getDataFromCountry(country);
                }
            }

            @Override
            public void onFailure(Call<CountryModel> call, Throwable t) {
                call.cancel();
                Log.d("eroare", t.toString());
            }
        });
    }

    public void getUnitMeasurementFromInvoiceDetail(Context context, RevealDetailsCallbacks callback) {
        APIInterfaces invoiceAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
        Call<UnitMeasurementsModel> call = invoiceAPI.getUnitMeasurementsByUMId(invoiceDetails_obj.getUnitMeasurementType());
        call.enqueue(new Callback<UnitMeasurementsModel>() {
            @Override
            public void onResponse(Call<UnitMeasurementsModel> call, Response<UnitMeasurementsModel> response) {
                UnitMeasurementsModel unitMeasurements = response.body();
                if(callback != null) {
                    callback.getDataFromUnitMeasurement(unitMeasurements);
                }
            }

            @Override
            public void onFailure(Call<UnitMeasurementsModel> call, Throwable t) {
                call.cancel();
                Log.d("eroare", t.toString());
            }
        });
    }

    public void completeTheView()
    {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

        //invoice id
        TextView textView_invoiceID = findViewById(R.id.invoiceId);
        textView_invoiceID.setText(Integer.toString(invoice_obj.getInvoiceId()));

        //dateOfIssue
        TextView textView_dateOfIssue = findViewById(R.id.dateOfIssue);
        if(invoice_obj.getDateOfIssue() == null)
            textView_dateOfIssue.setText("-");
        else textView_dateOfIssue.setText(df.format(invoice_obj.getDateOfIssue()));

        //dueData
        TextView textView_dueData = findViewById(R.id.dueDate);
        if(invoice_obj.getDueDate() == null)
            textView_dueData.setText("-");
        else textView_dueData.setText(df.format(invoice_obj.getDueDate()));

        //accountId
        TextView textView_accountId = findViewById(R.id.accountId_invoicesdetail);
        textView_accountId.setText(String.valueOf(invoice_obj.getAccountId()));

        //clientAddress
        TextView textView_addressClient = findViewById(R.id.addressId);
        textView_addressClient.setText(buildAddress());

        //CUI issue company
        TextView textView_cui = findViewById(R.id.IssueCUI);
        if(invoice_obj.getCUIIssuer() == 0)
            textView_cui.setText("-");
        else textView_cui.setText(String.valueOf(invoice_obj.getCUIIssuer()));

        //addressIssue
        TextView textView_addressIssue = findViewById(R.id.IssueAddress);
        if(invoice_obj.getIssuerAddress() == null || invoice_obj.getIssuerAddress() == "")
            textView_addressIssue.setText("-");
        else textView_addressIssue.setText(invoice_obj.getIssuerAddress());

        if(invoiceDetails_obj != null){
            //youHaveToPay
            TextView textView_toPay = findViewById(R.id.youHaveToPay);
            textView_toPay.setText(String.valueOf(invoiceDetails_obj.getValueWithVat()+invoiceDetails_obj.getSold()));

            //sold
            TextView textView_sold = findViewById(R.id.soldFromPast);
            if(invoiceDetails_obj.getSold() == 0)
                textView_sold.setText("-");
            else textView_sold.setText(String.valueOf(invoiceDetails_obj.getSold()));

            //unitMeasurementType
            TextView textView_unitM = findViewById(R.id.unitMeasurementType);
            if(invoiceDetails_obj.getUnitMeasurementType() == 0)
                textView_unitM.setText("-");
            else textView_unitM.setText(unitMeasurements_obj.getUnitMeasurementType());

            //pricePerUnit
            TextView textView_pricePerUnit = findViewById(R.id.pricePerUnit);
            if(invoiceDetails_obj.getPricePerUnit() == 0)
                textView_pricePerUnit.setText("-");
            else textView_pricePerUnit.setText(String.valueOf(invoiceDetails_obj.getPricePerUnit()));

            //consumedQuantity
            TextView textView_consumedQuantity = findViewById(R.id.consumedQuantity);
            if(invoiceDetails_obj.getConsumedQuantity() == 0)
                textView_consumedQuantity.setText("-");
            else textView_consumedQuantity.setText(String.valueOf(invoiceDetails_obj.getConsumedQuantity()));

            //VAT
            TextView textView_VAT = findViewById(R.id.simplyVAT);
            if(invoiceDetails_obj.getVAT() == 0)
                textView_VAT.setText("-");
            else textView_VAT.setText(String.valueOf(invoiceDetails_obj.getVAT()));

            //valueWithoutVAT
            TextView textView_valueWithoutVAT = findViewById(R.id.valueWithoutVAT);
            if(invoiceDetails_obj.getValueWithoutVAT() == 0)
                textView_valueWithoutVAT.setText("-");
            else textView_valueWithoutVAT.setText(String.valueOf(invoiceDetails_obj.getValueWithoutVAT()));

            //valueWithVat
            TextView textView_valueWithVat = findViewById(R.id.valueWithVat);
            if(invoiceDetails_obj.getValueWithVat() == 0)
                textView_valueWithVat.setText("-");
            else textView_valueWithVat.setText(String.valueOf(invoiceDetails_obj.getValueWithVat()));
        }
    }

    public String buildAddress()
    {
        String address ="";

        if(country_obj.getCountryName() != null & country_obj.getCountryName() != "")
            address = address + " Tara: " + country_obj.getCountryName() + " ";
        if(country_obj.getCountryCode() != null & country_obj.getCountryCode() != "")
            address = address + country_obj.getCountryCode() + " ";

        if(district_obj.getDistrictName() != null & district_obj.getDistrictName() != "")
            address = address + " Judet: " + district_obj.getDistrictName() + " ";
        if(district_obj.getDistrictCode() != null & district_obj.getDistrictCode() != "")
            address = address + district_obj.getDistrictCode() + " ";

        if(city_obj.getCityName() != null & city_obj.getCityName() != "")
            address = address + " Oras: " + city_obj.getCityName() + " ";
        if(city_obj.getCityCode() != null & city_obj.getCityCode() != "")
            address = address + city_obj.getCityCode() + " ";

        if(address_obj.getStreet() != "" & address_obj.getStreet() != null)
            address = address + " Strada: " + address_obj.getStreet() + " ";

        if(address_obj.getStreetNumber() != "" & address_obj.getStreetNumber() != null)
            address = address + " Numar: " + address_obj.getStreetNumber() + " ";

        if(address_obj.getImmobileNumber() != null & address_obj.getImmobileNumber() != "")
            address = address + " Cladirea: " + address_obj.getImmobileNumber() + " ";

        if(address_obj.getStairNumber() != null & address_obj.getStairNumber() != "")
            address = address + " Scara: " + address_obj.getStairNumber() + " ";

        if(address_obj.getFloorNumber() != 0)
            address = address + " Etaj: " + address_obj.getFloorNumber() + " ";

        if(address_obj.getFlatNumber() != 0)
            address = address + " Apartament: " + address_obj.getFlatNumber() + " ";

        if(address == "")
            return "-";
        else return  address;
    }
}