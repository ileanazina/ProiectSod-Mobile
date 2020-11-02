package com.example.myapplication1.Activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.example.myapplication1.Model.InvoiceModel;
import com.example.myapplication1.R;
import com.example.myapplication1.Remote.APIInterfaces;
import com.example.myapplication1.Remote.RetrofitClientLogIn;
import java.util.List;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Invoices extends AppCompatActivity implements InvoiceAdaptor.OnInvoiceListener{
    public interface RevealDetailsCallbacks {
        void getDataFromResult(List<InvoiceModel> list);
    }

    private RecyclerView recyclerView;
    private InvoiceAdaptor invoiceAdaptor;
    private APIInterfaces invoiceAPI;
    CompositeDisposable compositeDisposable= new CompositeDisposable();
    RevealDetailsCallbacks callback;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoices);
        recyclerView = this.findViewById(R.id.invoiceRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.callback = new RevealDetailsCallbacks() {
            @Override
            public void getDataFromResult(List<InvoiceModel> list) {
                invoiceAdaptor = new InvoiceAdaptor(Invoices.this,list,Invoices.this);
                recyclerView.setAdapter(invoiceAdaptor);
            }
        };
        getInvoiceList(this, callback);
    }

    public void getInvoiceList(Context context, RevealDetailsCallbacks callback) {
        invoiceAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
        Call<List<InvoiceModel>> call = invoiceAPI.getAllInvoices();
        call.enqueue(new Callback<List<InvoiceModel>>() {
            @Override
            public void onResponse(Call<List<InvoiceModel>> call, Response<List<InvoiceModel>> response) {
                List<InvoiceModel> invoices = response.body();
                if(callback != null) {
                    callback.getDataFromResult(invoices);
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
    public void onInvoiceListener(int position) {
        InvoiceDetails secondFragment = new InvoiceDetails();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}