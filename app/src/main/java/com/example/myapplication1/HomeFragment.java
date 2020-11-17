package com.example.myapplication1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


import com.example.myapplication1.Activities.Payments;
import com.example.myapplication1.Model.AccountModel;
import com.example.myapplication1.Model.AddressModel;
import com.example.myapplication1.Model.IndexModel;
import com.example.myapplication1.Model.InvoiceModel;
import com.example.myapplication1.Remote.APIInterfaces;
import com.example.myapplication1.Remote.RetrofitClientLogIn;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    public interface RevealDetailsCallbacks {
        void getDataFromAddress (List<AddressModel> address);
        void getDataFromSold(Float sold);
        void getDataFromInvoices(List<InvoiceModel> list);
        String invoiceStatus(int id);
    }

    private RevealDetailsCallbacks callback;
    private AccountModel account;

    private ArrayList<String> FullAddressName= new ArrayList<String>();
    private Vector<Integer> vaddressId= new Vector<>();
    private List<InvoiceModel> invoiceList ;
    private TextView invoice1, invoice2, invoice3;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = mPrefs.getString("AccountInfo",null);
        account = gson.fromJson(json, AccountModel.class);

        //Set the sold for the last unpaid invoice and set the button
        TextView textView_sold = view.findViewById(R.id.valLastUnpaidInvoices);
        Button payButton = view.findViewById(R.id.mainMenuPayButton);


        invoice1= view.findViewById(R.id.firstInvoice);
        invoice2= view.findViewById(R.id.secondInvoice);
        invoice3= view.findViewById(R.id.thirdInvoice);

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
                getSold(getContext(), callback, vaddressId.get(0));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, (String[]) FullAddressName.toArray(new String[FullAddressName.size()]));
                        Spinner addressSpinner = (Spinner) view.findViewById(R.id.spAddress);
                        addressSpinner.setAdapter(dataAdapter);
                        addressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                int spinnerPosition = (int) addressSpinner.getItemIdAtPosition(position);
                                SharedPreferences  aPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                                SharedPreferences.Editor aEditor = aPrefs.edit();
                                aEditor.putInt("Address",vaddressId.get(spinnerPosition));
                                aEditor.apply();
                                getSold(getContext(), callback, vaddressId.get(spinnerPosition));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                });
            }

            @Override
            public String invoiceStatus(int id) {
                String payed="Platita"; String unpayed="NEPLATITA";
                if (invoiceIsPayd(id))
                    return payed;
                else
                    return unpayed;
                return null;
            }

            @Override
            public void getDataFromInvoices(List<InvoiceModel> list) {
                String payed="Platita"; String unpayed="NEPLATITA";
                for(int i=0; i < 3; i++) {
                    if(list.get(i).getAccountId() == account.getAccountId()) {

                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        String dataDue1 = formatter.format(list.get(0).getDueDate());
                        String dataDue2 = formatter.format(list.get(1).getDueDate());
                        String dataDue3 = formatter.format(list.get(2).getDueDate());
                        invoice1.setText("Numar factura: "+list.get(0).getInvoiceId()+", data scadenta: "+dataDue1 + ", valoare: "+list.get(0).getValueWithVat()+", "+invoiceStatus(list.get(0).getInvoiceId()));
                        invoice2.setText("Numar factura: "+list.get(1).getInvoiceId()+", data scadenta: "+dataDue2 + ", valoare: "+list.get(1).getValueWithVat()+", "+invoiceStatus(list.get(1).getInvoiceId()));
                        invoice3.setText("Numar factura: "+list.get(2).getInvoiceId()+", data scadenta: "+dataDue3 + ", valoare: "+list.get(2).getValueWithVat()+", "+invoiceStatus(list.get(2).getInvoiceId()));
                        //invoiceList.add(list.get(i));
                    }
                }

            }




            @Override
            public void getDataFromSold(Float sold) {
                buttonAndSoldUpdated(textView_sold, payButton, sold);
            }

        };
        getAddressByAccount(getContext(), callback);
        invoiceIsPayd(getContext(), callback);
        get3Invoices(getContext(), callback);
        return view;
    }


    public void buttonAndSoldUpdated(TextView textView, Button button, Float sold)
    {
        textView.setText(String.valueOf(sold));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Payments.class);
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

    public void get3Invoices(Context context, RevealDetailsCallbacks callback)
    {
        APIInterfaces invoiceAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
        Call<List<InvoiceModel>> call = invoiceAPI.getInvoicesByAccountId(account.getAccountId());
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

    public boolean invoiceIsPayd(Context context, RevealDetailsCallbacks callback){
        APIInterfaces invoiceAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
        Call<List<InvoiceModel>> call = invoiceAPI.isInvoicePaid(account.getAccountId());
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
}