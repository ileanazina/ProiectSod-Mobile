package com.example.myapplication1.Activities.Index;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;



import com.example.myapplication1.Model.AccountModel;
import com.example.myapplication1.Model.AddressModel;
import com.example.myapplication1.Model.IndexModel;
import com.example.myapplication1.Model.InvoiceModel;
import com.example.myapplication1.R;
import com.example.myapplication1.Remote.APIInterfaces;
import com.example.myapplication1.Remote.RetrofitClientLogIn;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Index extends AppCompatActivity implements IndexAdaptor.OnIndexListener{

    public interface RevealDetailsCallbacks {
        void getDataFromIndex(List<IndexModel> list);
    }

    private RecyclerView recyclerView;
    private IndexAdaptor indexesAdaptor;
    private APIInterfaces indexesAPI;
    private CompositeDisposable compositeDisposable= new CompositeDisposable();
    private Index.RevealDetailsCallbacks callback;

    private Button addIndex;
    private EditText addIndexValue;
    private ImageView img;
    private Spinner adressSpinner;

    private List<IndexModel> allIndexes;
    private List<IndexModel> forAdaptorIndexes;
    AccountModel account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        recyclerView = this.findViewById(R.id.indexRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        allIndexes = new ArrayList<>();
        forAdaptorIndexes = new ArrayList<>();


        indexesAdaptor = new IndexAdaptor(Index.this,forAdaptorIndexes,Index.this);
        recyclerView.setAdapter(indexesAdaptor);

        addIndex= findViewById(R.id.addIndex);
        addIndexValue = findViewById(R.id.addIndexValue);
        img=findViewById(R.id.imageView);
        adressSpinner= findViewById(R.id.adressSpinner);
        ArrayAdapter<String> adapterSpineer = new ArrayAdapter<String>
                (this,android.R.layout.simple_spinner_item, Collections.singletonList("Adresa DE Domiciul"));

        adapterSpineer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adressSpinner.setAdapter(adapterSpineer);


        img.setVisibility(View.VISIBLE);

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = mPrefs.getString("AccountInfo",null);
        account = gson.fromJson(json, AccountModel.class);

        this.callback = new Index.RevealDetailsCallbacks() {
            @Override
            public void getDataFromIndex(List<IndexModel> list) {
                for(int i = 0; i< list.size(); i++)
                {
                    if(account.getAccountId() == list.get(i).getAccountId())
                    {
                        allIndexes.add(list.get(i));
                        forAdaptorIndexes.add(list.get(i));
                        indexesAdaptor.notifyDataSetChanged();
                    }
                }
            }
        };
        getIndexList(this, callback);

        if(Calendar.DAY_OF_MONTH>=20 && Calendar.DAY_OF_MONTH<=25) {
            img.setVisibility(View.INVISIBLE);
            addIndex.setVisibility(View.VISIBLE);
            addIndexValue.setVisibility(View.VISIBLE);

            addIndex.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (addIndexValue.getText().toString().isEmpty()) {
                        Toast.makeText(Index.this, "Introduceti indexul",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //add into data base
                    }
                }
            });
        }

    }

    public void getIndexList(Context context, RevealDetailsCallbacks callback) {
        indexesAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
        Call<List<IndexModel>> call = indexesAPI.getAllIndexis(account.getAccountId());
        call.enqueue(new Callback<List<IndexModel>>() {
            @Override
            public void onResponse(Call<List<IndexModel>> call, Response<List<IndexModel>> response) {
                List<IndexModel> indexes = response.body();
                if(callback != null) {
                    callback.getDataFromIndex(indexes);
                }
            }

            @Override
            public void onFailure(Call<List<IndexModel>> call, Throwable t) {
                call.cancel();
                Log.d("eroare", t.toString());
            }
        });
    }
    @Override
    public void onIndexListener(int position) {

    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}