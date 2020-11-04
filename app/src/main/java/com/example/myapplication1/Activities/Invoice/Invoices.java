package com.example.myapplication1.Activities.Invoice;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

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
        void getDataFromPayments(List<PaymentModel> list);
    }

    private RecyclerView recyclerView;
    private InvoiceAdaptor invoiceAdaptor;
    private APIInterfaces invoiceAPI;
    private CompositeDisposable compositeDisposable= new CompositeDisposable();
    private RevealDetailsCallbacks callback;
    private AccountModel account;

    private List<InvoiceModel> allInvoices;
    private List<InvoiceModel> forAdaptorInvoices;
    private List<PaymentModel> allPayments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoices);

        recyclerView = this.findViewById(R.id.invoiceRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        allInvoices = new ArrayList<>();
        forAdaptorInvoices = new ArrayList<>();
        allPayments = new ArrayList<>();

        invoiceAdaptor = new InvoiceAdaptor(Invoices.this,forAdaptorInvoices,Invoices.this);
        recyclerView.setAdapter(invoiceAdaptor);

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = mPrefs.getString("AccountInfo",null);
        account = gson.fromJson(json, AccountModel.class);

        this.callback = new RevealDetailsCallbacks() {
            @Override
            public void getDataFromInvoices(List<InvoiceModel> list) {
                for(int i = 0; i< list.size(); i++)
                {
                    if(account.getAccountId() == list.get(i).getAccountId())
                    {
                        allInvoices.add(list.get(i));
                        forAdaptorInvoices.add(list.get(i));
                        invoiceAdaptor.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void getDataFromPayments(List<PaymentModel> list) {
                for(int i = 0; i< list.size(); i++)
                {
                    allPayments.add(list.get(i));
                }
            }
        };
        getInvoiceList(this, callback);
        getPaymentList(this, callback);
        deleteAllPaymentsFromAnotherInvoices();

        findViewById(R.id.allInvoices).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putAllInvoices();
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
    }

    public void getInvoiceList(Context context, RevealDetailsCallbacks callback) {
        invoiceAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
        Call<List<InvoiceModel>> call = invoiceAPI.getAllInvoices();
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

    public void getPaymentList(Context context, RevealDetailsCallbacks callback_payment) {
        invoiceAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
        Call<List<PaymentModel>> call = invoiceAPI.getAllPayments();
        call.enqueue(new Callback<List<PaymentModel>>() {
            @Override
            public void onResponse(Call<List<PaymentModel>> call, Response<List<PaymentModel>> response) {
                List<PaymentModel> payments = response.body();
                if(callback != null) {
                    callback.getDataFromPayments(payments);
                }
            }

            @Override
            public void onFailure(Call<List<PaymentModel>> call, Throwable t) {
                call.cancel();
                Log.d("eroare", t.toString());
            }
        });
    }

    public void deleteAllPaymentsFromAnotherInvoices()
    {
        for(int i =0; i< allPayments.size(); i++)
        {
            boolean verify = false;
            for (int j = 0; j < allInvoices.size() && verify == false; i++)
                if (allInvoices.get(j).getInvoiceId() == allPayments.get(i).getInvoiceId())
                    verify = true;
            if (verify == false)
            {
                allPayments.remove(i);
                i--;
            }
        }
    }

    public void putAllInvoices()
    {
        forAdaptorInvoices.clear();
        invoiceAdaptor.notifyDataSetChanged();
        for(int i = 0; i< allInvoices.size(); i++)
        {
            if(account.getAccountId() == allInvoices.get(i).getAccountId())
            {
                forAdaptorInvoices.add(allInvoices.get(i));
                invoiceAdaptor.notifyDataSetChanged();
            }
        }
    }

    public void putJustPayedInvoices()
    {
        forAdaptorInvoices.clear();
        invoiceAdaptor.notifyDataSetChanged();
        for(int i = 0; i < allInvoices.size(); i++)
        {
            boolean verify = false;
            for(int j = 0; j < allPayments.size(); i++)
                if(allInvoices.get(i).getInvoiceId() == allPayments.get(j).getInvoiceId())
                    verify = true;
            if(verify == true)
            {
                forAdaptorInvoices.add(allInvoices.get(i));
                invoiceAdaptor.notifyDataSetChanged();
            }
        }

    }

    public void putUnpayedInvoices()
    {
        forAdaptorInvoices.clear();
        invoiceAdaptor.notifyDataSetChanged();
        for(int i = 0; i < allInvoices.size(); i++)
        {
            boolean verify = true;
            for(int j = 0; j < allPayments.size(); i++)
                if(allInvoices.get(i).getInvoiceId() == allPayments.get(j).getInvoiceId())
                    verify = false;
            if(verify == true)
            {
                forAdaptorInvoices.add(allInvoices.get(i));
                invoiceAdaptor.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onInvoiceListener(int position) {
        InvoiceDetails secondFragment = new InvoiceDetails();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}