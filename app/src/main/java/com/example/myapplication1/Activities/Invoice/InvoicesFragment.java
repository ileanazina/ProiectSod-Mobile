package com.example.myapplication1.Activities.Invoice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication1.Activities.SearchDateFragment;
import com.example.myapplication1.Model.AccountModel;
import com.example.myapplication1.Model.InvoiceModel;
import com.example.myapplication1.Model.InvoiceFilter;
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

public class InvoicesFragment extends Fragment implements InvoiceAdaptor.OnInvoiceListener {

    public interface RevealDetailsCallbacks {
        void getDataFromInvoices(List<InvoiceModel> list);
    }

    private RecyclerView recyclerView;
    private InvoiceAdaptor invoiceAdaptor;
    private APIInterfaces invoiceAPI;
    private CompositeDisposable compositeDisposable= new CompositeDisposable();
    private RevealDetailsCallbacks callback;
    private AccountModel account;
    private int addressId;
    boolean onClickDate = false;

    private List<InvoiceModel> forAdaptorInvoices;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invoice_page, container, false);

        recyclerView = view.findViewById(R.id.invoiceRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        forAdaptorInvoices = new ArrayList<>();

        invoiceAdaptor = new InvoiceAdaptor(getContext(),forAdaptorInvoices, InvoicesFragment.this);
        recyclerView.setAdapter(invoiceAdaptor);

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = mPrefs.getString("AccountInfo",null);
        account = gson.fromJson(json, AccountModel.class);
        addressId = mPrefs.getInt("Address", 0);

        this.callback = new RevealDetailsCallbacks() {
            @Override
            public void getDataFromInvoices(List<InvoiceModel> list) {
                forAdaptorInvoices.clear();
                invoiceAdaptor.notifyDataSetChanged();
                if(list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getAddressId() == addressId) {
                            forAdaptorInvoices.add(list.get(i));
                            invoiceAdaptor.notifyDataSetChanged();
                        }
                    }
                }
            }
        };
        getInvoiceList(new InvoiceFilter(account.getAccountId(),addressId));

        view.findViewById(R.id.allInvoices).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InvoiceFilter invFilter = new InvoiceFilter(account.getAccountId(),addressId);
                getInvoiceList(invFilter);
            }
        });
        view.findViewById(R.id.payedInvoices).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InvoiceFilter invFilter = new InvoiceFilter(account.getAccountId(),addressId, "p");
                getInvoiceList(invFilter);
            }
        });
        view.findViewById(R.id.unpayedInvoices).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InvoiceFilter invFilter = new InvoiceFilter(account.getAccountId(),addressId, "up");
                getInvoiceList(invFilter);
            }
        });
        view.findViewById(R.id.afterdate).setOnClickListener(new View.OnClickListener() {
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
        return view;
    }

    public void getInvoiceList(InvoiceFilter invModel) {
        invoiceAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
        Call<List<InvoiceModel>> call = invoiceAPI.getInvoicesByAccountId("Bearer " + account.getToken(), invModel);
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

    @Override
    public void onInvoiceListener(int position) {}

    public void getInvoiceListFromSearchFragment(InvoiceFilter invoiceFilter) {
        invoiceAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
        Call<List<InvoiceModel>> call = invoiceAPI.getInvoicesByAccountId("Bearer " + account.getToken(), invoiceFilter);
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