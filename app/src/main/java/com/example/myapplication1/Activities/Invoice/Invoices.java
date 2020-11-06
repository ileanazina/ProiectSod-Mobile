package com.example.myapplication1.Activities.Invoice;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.example.myapplication1.Activities.SearchDateFragment;
import com.example.myapplication1.Model.AccountModel;
import com.example.myapplication1.Model.InvoiceModel;
import com.example.myapplication1.Model.PaymentModel;
import com.example.myapplication1.R;
import com.example.myapplication1.Remote.APIInterfaces;
import com.example.myapplication1.Remote.RetrofitClientLogIn;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Invoices extends AppCompatActivity implements InvoiceAdaptor.OnInvoiceListener {

    public interface RevealDetailsCallbacks {
        void getDataFromInvoices(List<InvoiceModel> list);
    }

    private RecyclerView recyclerView;
    private InvoiceAdaptor invoiceAdaptor;
    private APIInterfaces invoiceAPI;
    private CompositeDisposable compositeDisposable= new CompositeDisposable();
    private RevealDetailsCallbacks callback;
    private AccountModel account;
    boolean onClickDate = false;

    private List<InvoiceModel> forAdaptorInvoices;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoices);

        recyclerView = this.findViewById(R.id.invoiceRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        forAdaptorInvoices = new ArrayList<>();

        invoiceAdaptor = new InvoiceAdaptor(Invoices.this,forAdaptorInvoices,Invoices.this);
        recyclerView.setAdapter(invoiceAdaptor);

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = mPrefs.getString("AccountInfo",null);
        account = gson.fromJson(json, AccountModel.class);

        this.callback = new RevealDetailsCallbacks() {
            @Override
            public void getDataFromInvoices(List<InvoiceModel> list) {
                forAdaptorInvoices.clear();
                invoiceAdaptor.notifyDataSetChanged();
                for(int i=0; i < list.size(); i++) {
                    forAdaptorInvoices.add(list.get(i));
                    invoiceAdaptor.notifyDataSetChanged();
                }
            }
        };
        getInvoiceList();

        findViewById(R.id.allInvoices).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInvoiceList();
            }
        });
        findViewById(R.id.payedInvoices).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putJustPayedInvoices();
            }
        });
        findViewById(R.id.unpayedInvoices).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putUnpayedInvoices();
            }
        });
        findViewById(R.id.afterdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchDateFragment searchDateFragment = new SearchDateFragment();
                if(onClickDate == false) {
                    getFragmentManager().beginTransaction().add(R.id.dataFragment, searchDateFragment,"date_fragment").commit();
                    onClickDate = true;
                }
                else{
                   Fragment fragmentDate = getFragmentManager().findFragmentById(R.id.dataFragment);
                   if(fragmentDate != null)
                       getFragmentManager().beginTransaction().remove(fragmentDate).commit();
                   onClickDate = false;
                }
            }
        });
    }

    public void getInvoiceList() {
        invoiceAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
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
                Log.d("eroare", t.toString());
            }
        });
    }

    public void putJustPayedInvoices()
    {
        invoiceAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
        Call<List<InvoiceModel>> call = invoiceAPI.getPaidInvoicesByAccountId(account.getAccountId());
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
                Log.d("eroare", t.toString());
            }
        });
    }

    public void putUnpayedInvoices()
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
                Log.d("eroare", t.toString());
            }
        });
    }

    @Override
    public void onInvoiceListener(int position) {}

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}