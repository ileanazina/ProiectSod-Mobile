package com.example.myapplication1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication1.Activities.FormsFragment;
import com.example.myapplication1.Activities.Index.IndexFragment;
import com.example.myapplication1.Activities.Invoice.InvoicesFragment;
import com.example.myapplication1.Activities.Payments;
import com.example.myapplication1.Activities.ProfileFragment;
import com.example.myapplication1.Model.AccountModel;
import com.example.myapplication1.Model.AddressModel;
import com.example.myapplication1.Model.InvoiceModel;
import com.example.myapplication1.Remote.APIInterfaces;
import com.example.myapplication1.Remote.RetrofitClientLogIn;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public interface RevealDetailsCallbacks {
        void getDataFromAddress (List<AddressModel> address);
        void getDataFromSold(Float sold);
    }

    private APIInterfaces invoiceAPI;
    private RevealDetailsCallbacks callback;
    private InvoiceModel lastUnpaidInvoice = null;
    private AccountModel account;

    private ArrayList<String> FullAddressName= new ArrayList<String>();
    private Vector<Integer> vaddressId= new Vector<>();

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

                Intent intentDetails = new Intent(MainActivity.this, TryTest.class);
                startActivity(intentDetails);
            }
        });

        Button buttonForms = (Button) findViewById(R.id.buttonForms);
        setDimensions(buttonForms);
        buttonForms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentForms = new Intent(MainActivity.this, FormsFragment.class);
                startActivity(intentForms);
            }
        });

        Button buttonIndex = (Button) findViewById(R.id.buttonIndex);
        setDimensions(buttonIndex);
        buttonIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentIndex = new Intent(MainActivity.this, IndexFragment.class);
                startActivity(intentIndex);
            }
        });

        Button buttonInvoices = (Button) findViewById(R.id.buttonInvoice);
        setDimensions(buttonInvoices);
        buttonInvoices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentInvoices = new Intent(MainActivity.this, InvoicesFragment.class);
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
                Intent intentProfile = new Intent(MainActivity.this, ProfileFragment.class);
                startActivity(intentProfile);
            }
        });

        //Set the sold for the last unpaid invoice and set the button
        TextView textView_sold = findViewById(R.id.valLastUnpaidInvoices);
        Button payButton = findViewById(R.id.mainMenuPayButton);

        this.callback = new RevealDetailsCallbacks() {
            @Override
            public void getDataFromAddress(List<AddressModel> address) {
                    int position=0;
                for(AddressModel model: address)
                {
                    FullAddressName.add(model.getFullAddressName());
                    vaddressId.add(position,model.getAddressId()) ;
                    position++;

                }
                getSold(MainActivity.this, callback, vaddressId.get(0));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_spinner_item, (String[]) FullAddressName.toArray(new String[FullAddressName.size()]));
                        Spinner addressSpinner = (Spinner) findViewById(R.id.spAddress);
                        addressSpinner.setAdapter(dataAdapter);
                        addressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                int spinnerPosition = (int) addressSpinner.getItemIdAtPosition(position);
                                SharedPreferences  aPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                SharedPreferences.Editor aEditor = aPrefs.edit();
                                aEditor.putInt("Address",vaddressId.get(spinnerPosition));
                                aEditor.apply();
                                getSold(MainActivity.this, callback, vaddressId.get(spinnerPosition));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                });
            }

            @Override
            public void getDataFromSold(Float sold) {
                buttonAndSoldUpdated(textView_sold, payButton, sold);
            }
        };
        getAddressByAccount(this, callback);
    }

    void setDimensions(Button button)
    {
        button.setWidth((int) (getScreenWidth()*0.3));
        button.setHeight((int) (getScreenHeight()*0.2));
    }

    public void buttonAndSoldUpdated(TextView textView, Button button, Float sold)
    {
        textView.setText(String.valueOf(sold));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Payments.class);
                startActivity(intent);
            }
        });
    }

    public void getSold(Context context, RevealDetailsCallbacks callback, int addressId){
        APIInterfaces invoiceAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
        Call<Float> call = invoiceAPI.getSold(account.getAccountId(), addressId );
        call.enqueue(new Callback<Float>() {
            @Override
            public void onResponse(Call<Float> call, Response<Float> response) {
                Float sold = response.body();
                if(callback != null) {
                    callback.getDataFromSold(sold);
                }
            }

            @Override
            public void onFailure(Call<Float> call, Throwable t) {
                call.cancel();
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
            }
        });
    }
}