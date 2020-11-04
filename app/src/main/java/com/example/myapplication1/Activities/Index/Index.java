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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.myapplication1.Activities.Invoice.Invoices;
import com.example.myapplication1.Model.AccountModel;
import com.example.myapplication1.Model.IndexModel;
import com.example.myapplication1.R;
import com.example.myapplication1.Remote.APIInterfaces;
import com.example.myapplication1.Remote.RetrofitClientLogIn;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Index extends AppCompatActivity implements IndexAdaptor.OnIndexListener{



    public interface RevealDetailsCallbacks {
        void getDataFromResult(List<IndexModel> list);
    }

    private RecyclerView recyclerView;
    private IndexAdaptor indexesAdaptor;
    private APIInterfaces indexesAPI;
    CompositeDisposable compositeDisposable= new CompositeDisposable();
    Index.RevealDetailsCallbacks callback;

    private Button addIndex;
    private EditText addIndexValue;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        recyclerView = this.findViewById(R.id.indexRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<IndexModel> indexes = new ArrayList<>();
        indexesAdaptor = new IndexAdaptor(Index.this,indexes,Index.this);
        recyclerView.setAdapter(indexesAdaptor);
        addIndex= findViewById(R.id.addIndex);
        addIndexValue = findViewById(R.id.addIndexValue);
        img=findViewById(R.id.imageView);


        img.setVisibility(View.VISIBLE);
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = mPrefs.getString("AccountInfo",null);
        AccountModel account = gson.fromJson(json, AccountModel.class);

        this.callback = new Index.RevealDetailsCallbacks() {
            @Override
            public void getDataFromResult(List<IndexModel> list) {
                for(int i = 0; i< list.size(); i++)
                {
                    //if(account.getAccountId() == list.get(i).getAccountId())
                    {
                        indexes.add(list.get(i));
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
        Call<List<IndexModel>> call = indexesAPI.getAllIndexis();
        call.enqueue(new Callback<List<IndexModel>>() {
            @Override
            public void onResponse(Call<List<IndexModel>> call, Response<List<IndexModel>> response) {
                List<IndexModel> indexes = response.body();
                if(callback != null) {
                    callback.getDataFromResult(indexes);
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