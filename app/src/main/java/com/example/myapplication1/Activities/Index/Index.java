package com.example.myapplication1.Activities.Index;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication1.Activities.CompanyDetails;
import com.example.myapplication1.Activities.Invoice.InvoiceDetails;
import com.example.myapplication1.LogIn;
import com.example.myapplication1.MainActivity;
import com.example.myapplication1.Model.AccountModel;
import com.example.myapplication1.Model.AddIIndex;
import com.example.myapplication1.Model.AddressModel;
import com.example.myapplication1.Model.CityModel;
import com.example.myapplication1.Model.CountryModel;
import com.example.myapplication1.Model.DistrictModel;
import com.example.myapplication1.Model.IndexModel;

import com.example.myapplication1.R;
import com.example.myapplication1.Remote.APIInterfaces;
import com.example.myapplication1.Remote.RetrofitClientLogIn;

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
        void getDataFromIndex(List<IndexModel> list);
        void getDataFromResult(IndexModel index);
    }

    private RecyclerView recyclerView;
    private IndexAdaptor indexesAdaptor;
    private APIInterfaces indexesAPI;
    private CompositeDisposable compositeDisposable= new CompositeDisposable();
    private Index.RevealDetailsCallbacks callback;

    private AddressModel address_obj;
    private CityModel city_obj;
    private DistrictModel district_obj;
    private CountryModel country_obj;
    private List<IndexModel> index_obj;
    private AlertDialog.Builder builder;

    private Button addIndex;
    private EditText addIndexValue;
    private ImageView img;
    private Spinner adressSpinner;

    private List<IndexModel> allIndexes;
    private List<IndexModel> forAdaptorIndexes;
    AccountModel account;
    private int AddressIdFromSpinner;
    IndexModel indexThink;
    private AlertDialog dialogError;

    private LayoutInflater inflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        recyclerView = this.findViewById(R.id.indexRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        allIndexes = new ArrayList<>();
        forAdaptorIndexes = new ArrayList<>();


        indexesAdaptor = new IndexAdaptor(Index.this, forAdaptorIndexes, Index.this);
        recyclerView.setAdapter(indexesAdaptor);

        addIndex = findViewById(R.id.addIndex);
        addIndexValue = findViewById(R.id.addIndexValue);
        img = findViewById(R.id.imageView);


        // addIndex.setVisibility(View.INVISIBLE);
        //addIndexValue.setVisibility(View.INVISIBLE);
        //img.setVisibility(View.VISIBLE);

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = mPrefs.getString("AccountInfo", null);
        account = gson.fromJson(json, AccountModel.class);

        SharedPreferences  aPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        int ad= aPrefs.getInt("Address",0);
        AddressIdFromSpinner = ad;
        Log.e("AddressId", String.valueOf(AddressIdFromSpinner));
        this.callback = new Index.RevealDetailsCallbacks() {
            @Override
            public void getDataFromIndex(List<IndexModel> list) {
                index_obj = list;

                for (int i = 0; i < list.size(); i++) {
                    if (account.getAccountId() == list.get(i).getAccountId()) {

                        allIndexes.add(list.get(i));
                        forAdaptorIndexes.add(list.get(i));
                        indexesAdaptor.notifyDataSetChanged();
                        // getAddressByAccount(Index.this, callback);
                        //list.get(i).setAddress(buildAddress());

                    }
                }
            }

            @Override
            public void getDataFromResult(IndexModel index) {

            }


        };
        getIndexList(this, callback);

        if (Calendar.DAY_OF_MONTH >= 20 && Calendar.DAY_OF_MONTH <= 25) {
            addIndex.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String strIndex = String.valueOf(addIndexValue.getText());
                    float indexValue = Float.parseFloat(strIndex);
                    if (addIndexValue.getText().toString().isEmpty() || addIndexValue.getText() == null) {
                        addIndexValue.setError("Index invalid");
                    } else {
                        //DUPA CE SE REALIZEAZA SPINNERUL PRINCIPAL SE REVINE AICI SA CA SA INLOCUIM ADDRESSID CU ID UL VAL DIN SPINNER
                        AddIIndex index = new AddIIndex(indexValue, account.getAccountId(), AddressIdFromSpinner);
                        Call<IndexModel> call = indexesAPI.insertIndex(index);
                        call.enqueue(new Callback<IndexModel>() {
                            @Override
                            public void onResponse(Call<IndexModel> call, Response<IndexModel> response) {
                                IndexModel model = response.body();
                                if (model == null) {
                                    Toast.makeText(Index.this, "Invalid", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(Index.this, "Succes", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<IndexModel> call, Throwable t) {
                                call.cancel();
                            }
                        });
                    }
                }
            });
        }
        else
        {
            builder = new AlertDialog.Builder(Index.this);

            inflater = LayoutInflater.from(Index.this);
            View view = inflater.inflate(R.layout.index_alert_dialog, null);

            Button noButton = view.findViewById(R.id.button_indcancel);
            Button yesButton = view.findViewById(R.id.button_indreload);

            builder.setView(view);
            dialogError = builder.create();
            dialogError.show();

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogError.dismiss();
                }
            });
            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Index.this, MainActivity.class);
                    startActivity(intent);
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
