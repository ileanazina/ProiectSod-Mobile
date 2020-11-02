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
import java.util.ArrayList;
import java.util.List;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Invoices extends AppCompatActivity implements InvoiceAdaptor.OnInvoiceListener{
    private RecyclerView recyclerView;
    private InvoiceAdaptor invoiceAdaptor;
    private APIInterfaces invoiceAPI;
    private List<InvoiceModel> list;
    CompositeDisposable compositeDisposable= new CompositeDisposable();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoices);

        recyclerView = findViewById(R.id.invoiceRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        getInvoiceList(this);
        invoiceAdaptor = new InvoiceAdaptor(this,list,this);
        recyclerView.setAdapter(invoiceAdaptor);
    }

    public void getInvoiceList(Context context) {
        invoiceAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
        InvoiceModel invoiceModel = new InvoiceModel();
        Call<List<InvoiceModel>> call = invoiceAPI.getAllInvoices(invoiceModel);
        call.enqueue(new Callback<List<InvoiceModel>>() {
            @Override
            public void onResponse(Call<List<InvoiceModel>> call, Response<List<InvoiceModel>> response) {
                List<InvoiceModel> invoices = response.body();
                Log.d("mere","intr-un final");
            }

            @Override
            public void onFailure(Call<List<InvoiceModel>> call, Throwable t) {
                call.cancel();
                Log.d("eroare", "de la mama omida stie");
            }
        });
    }

    @Override
    public void onInvoiceListener(int position) {

    }
    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}