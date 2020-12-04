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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication1.Activities.Payments;
import com.example.myapplication1.Model.AccountModel;
import com.example.myapplication1.Model.AddressModel;
import com.example.myapplication1.Model.InvoiceFilter;
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
    }

    private RevealDetailsCallbacks callback;
    private AccountModel account;
    private ArrayList<String> FullAddressName= new ArrayList<String>();
    private Vector<Integer> vaddressId= new Vector<>();
    private int addressesID;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Gson gson = new Gson();
        String json = mPrefs.getString("AccountInfo",null);
        account = gson.fromJson(json, AccountModel.class);

        //Set the sold for the last unpaid invoice and set the button
        TextView textView_sold = view.findViewById(R.id.valLastUnpaidInvoices);
        ImageButton payButton = view.findViewById(R.id.mainMenuPayButton);

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

                if(!vaddressId.isEmpty())
                {
                    addressesID = mPrefs.getInt("Address",vaddressId.get(0));
                    get3Invoices(getContext(), callback);
                }

                getSold(getContext(), callback, vaddressId.get(0));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item,
                                (String[]) FullAddressName.toArray(new String[FullAddressName.size()]));
                        Spinner addressSpinner = (Spinner) view.findViewById(R.id.spAddress);
                        addressSpinner.setAdapter(dataAdapter);

                        int spinnerSelected = mPrefs.getInt("SpinnerPosition",-1);
                        if(spinnerSelected != -1)
                            addressSpinner.setSelection(spinnerSelected);

                        addressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                int spinnerPosition = (int) addressSpinner.getItemIdAtPosition(position);

                                SharedPreferences.Editor aEditor = mPrefs.edit();
                                aEditor.putInt("Address",vaddressId.get(spinnerPosition));
                                aEditor.putInt("SpinnerPosition", spinnerPosition);
                                aEditor.apply();

                                addressesID = vaddressId.get(spinnerPosition);
                                get3Invoices(getContext(), callback);

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
            public void getDataFromInvoices(List<InvoiceModel> list) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                for(int index = 0; list != null & index < 3 ; index ++)
                {
                    TextView textView = getView().findViewWithTag("homeInvoice" + index);
                    if(list.size() > index) {
                        String dataDue = formatter.format(list.get(index).getDueDate());
                        String invoiceIsPayed;
                        if (list.get(index).isPaid())
                            invoiceIsPayed = getResources().getString(R.string.home_fragment_isPaid_true);
                        else
                            invoiceIsPayed = getResources().getString(R.string.home_fragment_isPaid_false);
                        textView.setText(getResources().getString(R.string.home_fragment_invoice_number) + list.get(index).getInvoiceId()
                                + ", " + getResources().getString(R.string.home_fragment_invoice_date) + dataDue + ", "
                                + getResources().getString(R.string.home_fragment_invoice_to_pay) + list.get(index).getValueWithVat()
                                + ", " + invoiceIsPayed);
                    }
                    else textView.setText("");

                    if(list!=null)
                        buttonUpdate(payButton, list.get(0));
                }
            }

            @Override
            public void getDataFromSold(Float sold) {
                textView_sold.setText(String.valueOf(sold));
            }
        };

        getAddressByAccount(getContext(), callback);
        return view;
    }

    public void buttonUpdate(ImageButton button, InvoiceModel invoice)
    {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPay = new Intent(getContext() , Payments.class);
                intentPay.putExtra("extra", invoice);
                intentPay.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intentPay);
            }
        });
    }

    public void getSold(Context context, RevealDetailsCallbacks callback, int addressId){
        APIInterfaces invoiceAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
        Call<Float> call = invoiceAPI.getSold("Bearer " + account.getToken(), account.getAccountId(), addressId );
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
        Call<List<AddressModel>> call = invoiceAPI.getAddressesByAccountId("Bearer " + account.getToken(), account.getAccountId());
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
        InvoiceFilter invModel = new InvoiceFilter(account.getAccountId(),addressesID);
        APIInterfaces invoiceAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
        Call<List<InvoiceModel>> call = invoiceAPI.getInvoicesByAccountId("Bearer " + account.getToken(), invModel);

        call.enqueue(new Callback<List<InvoiceModel>>() {
            @Override
            public void onResponse(Call<List<InvoiceModel>> call, Response<List<InvoiceModel>> response) {
                List<InvoiceModel> list = response.body();
                if (callback != null) {
                    callback.getDataFromInvoices(list);
                }
            }

            @Override
            public void onFailure(Call<List<InvoiceModel>> call, Throwable t) {
                call.cancel();
            }
        });
    }
}