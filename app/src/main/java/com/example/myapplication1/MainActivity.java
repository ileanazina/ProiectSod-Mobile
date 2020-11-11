package com.example.myapplication1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication1.Activities.CompanyDetails;
import com.example.myapplication1.Activities.Invoice.InvoiceDetails;
import com.example.myapplication1.Activities.Forms;
import com.example.myapplication1.Activities.Index.Index;
import com.example.myapplication1.Activities.Invoice.Invoices;
import com.example.myapplication1.Activities.Payments;
import com.example.myapplication1.Activities.Profile;
import com.example.myapplication1.Model.AccountModel;
import com.example.myapplication1.Model.AddressModel;
import com.example.myapplication1.Model.CityModel;
import com.example.myapplication1.Model.CountryModel;
import com.example.myapplication1.Model.DistrictModel;
import com.example.myapplication1.Model.InvoiceDetailsModel;
import com.example.myapplication1.Model.InvoiceModel;
import com.example.myapplication1.Model.UnitMeasurementsModel;
import com.example.myapplication1.Remote.APIInterfaces;
import com.example.myapplication1.Remote.RetrofitClientLogIn;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public interface RevealDetailsCallbacks {
        void getDataFromAddress (List<AddressModel> address);
        void getDataFromInvoices(List<InvoiceModel> invoiceModels);
    }

    private APIInterfaces invoiceAPI;
    private RevealDetailsCallbacks callback;
    private InvoiceModel lastUnpaidInvoice = null;
    private AccountModel account;

    private ArrayList<String> FullAddressName= new ArrayList<String>();

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = mPrefs.getString("AccountInfo",null);
        account = gson.fromJson(json, AccountModel.class);


        Button buttonDetails = (Button) findViewById(R.id.buttonDetails);
        setDimensions(buttonDetails);
        buttonDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentDetails = new Intent(MainActivity.this, CompanyDetails.class);
                startActivity(intentDetails);
            }
        });

        Button buttonForms = (Button) findViewById(R.id.buttonForms);
        setDimensions(buttonForms);
        buttonForms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentForms = new Intent(MainActivity.this, Forms.class);
                startActivity(intentForms);
            }
        });

        Button buttonIndex = (Button) findViewById(R.id.buttonIndex);
        setDimensions(buttonIndex);
        buttonIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentIndex = new Intent(MainActivity.this, Index.class);
                startActivity(intentIndex);
            }
        });

        Button buttonInvoices = (Button) findViewById(R.id.buttonInvoice);
        setDimensions(buttonInvoices);
        buttonInvoices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentInvoices = new Intent(MainActivity.this, Invoices.class);
                startActivity(intentInvoices);
            }
        });

        Button buttonPayments = (Button) findViewById(R.id.buttonPayments);
        setDimensions(buttonPayments);
        buttonPayments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPayments = new Intent(MainActivity.this, Payments.class);
                startActivity(intentPayments);
            }
        });

        Button buttonProfile = (Button) findViewById(R.id.buttonProfile);
        setDimensions(buttonProfile);
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentProfile = new Intent(MainActivity.this, Profile.class);
                startActivity(intentProfile);
            }
        });

        //Set the sold for the last unpaid invoice and set the button
        TextView textView_sold = findViewById(R.id.valLastUnpaidInvoices);
        Button payButton = findViewById(R.id.mainMenuPayButton);

        this.callback = new MainActivity.RevealDetailsCallbacks() {
            @Override
            public void getDataFromAddress(List<AddressModel> address) {
                for(AddressModel model: address)
                {
                    FullAddressName.add(model.getFullAddressName());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_spinner_item, (String[]) FullAddressName.toArray(new String[FullAddressName.size()]));
                        Spinner addressSpinner = (Spinner) findViewById(R.id.spAddress);
                        addressSpinner.setAdapter(dataAdapter);
                    }
                });

            }

            @Override
            public void getDataFromInvoices(List<InvoiceModel> list) {
                if(list != null){
                    lastUnpaidInvoice = list.get(0);
                    for(int i = 1; i < list.size(); i++)
                        if(lastUnpaidInvoice.getDateOfIssue().compareTo(list.get(i).getDateOfIssue()) < 0)
                            lastUnpaidInvoice = list.get(i);
                    buttonAndSoldUpdated(textView_sold, payButton);
                }
            }
        };
        getAddressByAccount(this, callback);
        getUnpaidInvoices();



    }

    void setDimensions(Button button)
    {
        button.setWidth((int) (getScreenWidth()*0.3));
        button.setHeight((int) (getScreenHeight()*0.2));
    }

    public void getUnpaidInvoices()
    {
        invoiceAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
        Call<List<InvoiceModel>> call = invoiceAPI.getUnpaidInvoicesByAccountId(account.getAccountId());
        call.enqueue(new Callback<List<InvoiceModel>>() {
            @Override
            public void onResponse(Call<List<InvoiceModel>> call, Response<List<InvoiceModel>> response) {
                List<InvoiceModel> invoices = response.body();
                if(callback != null) {
                    callback.getDataFromInvoices(invoices);
                }
            }

            @Override
            public void onFailure(Call<List<InvoiceModel>> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public void buttonAndSoldUpdated(TextView textView, Button button)
    {
        textView.setText(String.valueOf(lastUnpaidInvoice.getValueWithVat()));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Payments.class);
                startActivity(intent);
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
}