package com.example.myapplication1.Activities.Index;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.myapplication1.Activities.Invoice.InvoiceDetails;
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
//        void getDataFromAddress (List<AddressModel> address);
//        void getDataFromCity(CityModel city);
//        void getDataFromDistrict(DistrictModel district);
//        void getDataFromCountry(CountryModel country);
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


    private Button addIndex;
    private EditText addIndexValue;
    private ImageView img;
    private Spinner adressSpinner;

    private List<IndexModel> allIndexes;
    private List<IndexModel> forAdaptorIndexes;
    AccountModel account;
    IndexModel indexThink;

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


        indexesAdaptor = new IndexAdaptor(Index.this,forAdaptorIndexes,Index.this);
        recyclerView.setAdapter(indexesAdaptor);

        addIndex= findViewById(R.id.addIndex);
        addIndexValue = findViewById(R.id.addIndexValue);
        img=findViewById(R.id.imageView);


       // addIndex.setVisibility(View.INVISIBLE);
        //addIndexValue.setVisibility(View.INVISIBLE);
        //img.setVisibility(View.VISIBLE);

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = mPrefs.getString("AccountInfo",null);
        account = gson.fromJson(json, AccountModel.class);

        this.callback = new Index.RevealDetailsCallbacks() {
            @Override
            public void getDataFromIndex(List<IndexModel> list) {
                index_obj=list;

                for(int i = 0; i< list.size(); i++)
                {
                    if(account.getAccountId() == list.get(i).getAccountId())
                    {

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

//            @Override
//            public void getDataFromAddress(List<AddressModel> address) {
//
//                for( IndexModel q : index_obj){
//                    for(AddressModel currentAddress : address){
//                            if(currentAddress.getAddressId() == q.getAddressId()){
//                                address_obj=currentAddress;
//                                getCityFromAddress(Index.this, callback);
//                                q.setAddress(buildAddress());
//                            }
//                        }
////                    allIndexes.add(q);
////                    forAdaptorIndexes.add(q);
////                    indexesAdaptor.notifyDataSetChanged();
//                }
//
////                for (int i = 0; i < address.size(); i++) {
////
////                    for(int j=0;j<index_obj.size();j++ )
////                        if (index_obj.get(j).getAddressList() == address.get(i).getAddressId()) {
////                            index_obj.get(j).setAddress(buildAddress());
////                        }
////                }
//                //getCityFromAddress(Index.this, callback);
//            }
//                @Override
//                public void getDataFromAddress(List<AddressModel> address) {
//                    for (int i = 0; i < address.size(); i++) {
//                        for(int j=0;j<index_obj.size();j++ )
//                        if (index_obj.get(j).getAddressId() == address.get(i).getAddressId()) {
//                            address_obj = address.get(i);
//                            getCityFromAddress(Index.this, callback);
//                            index_obj.get(j).setAddress(buildAddress());
//                            break;
//                        }
//                    }
//
//                }


//            @Override
//            public void getDataFromCity(CityModel city) {
//                city_obj = city;
//                getDistrictFromCity(Index.this, callback);
//            }
//
//            @Override
//            public void getDataFromDistrict(DistrictModel district) {
//                district_obj = district;
//               getCountryFromDistrict(Index.this, callback);
//            }
//
//            @Override
//            public void getDataFromCountry(CountryModel country) {
//                country_obj = country;
//
//            }

        };
        getIndexList(this, callback);

        if(Calendar.DAY_OF_MONTH>=9 && Calendar.DAY_OF_MONTH<=25) {
            //img.setVisibility(View.INVISIBLE);
            addIndex.setVisibility(View.VISIBLE);
            addIndexValue.setVisibility(View.VISIBLE);

            addIndex.setOnClickListener(new View.OnClickListener() {
                float indexValue= (Float.parseFloat( addIndexValue.getText().toString()));
                @Override
                public void onClick(View v) {
                    if (addIndexValue.getText().toString().isEmpty()) {
                        Toast.makeText(Index.this, "Introduceti indexul", Toast.LENGTH_SHORT).show();
                    } else {

                        AddIIndex index = new AddIIndex(indexValue, indexThink.getAccountId(), indexThink.getAddressId());
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

        //adressSpinner= findViewById(R.id.adressSpinner);

       // SpinnerThink();
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

//    public void getAddressByAccount(Context context, RevealDetailsCallbacks callback) {
//        indexesAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
//        Call<List<AddressModel>> call = indexesAPI.getAddressesByAccountId(account.getAccountId());
//        call.enqueue(new Callback<List<AddressModel>>() {
//            @Override
//            public void onResponse(Call<List<AddressModel>> call, Response<List<AddressModel>> response) {
//                List<AddressModel> address = response.body();
//                if(callback != null) {
//                    callback.getDataFromAddress(address);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<AddressModel>> call, Throwable t) {
//                call.cancel();
//                Log.d("eroare", t.toString());
//            }
//        });
//    }
//
//    public void getCityFromAddress(Context context, RevealDetailsCallbacks callback) {
//        indexesAPI  = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
//        Call<CityModel> call = indexesAPI.getCityByCityId(address_obj.getCityId());
//        call.enqueue(new Callback<CityModel>() {
//            @Override
//            public void onResponse(Call<CityModel> call, Response<CityModel> response) {
//                CityModel city = response.body();
//                if(callback != null) {
//                    callback.getDataFromCity(city);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CityModel> call, Throwable t) {
//                call.cancel();
//                Log.d("eroare", t.toString());
//            }
//        });
//    }
//
//    public void getDistrictFromCity(Context context, RevealDetailsCallbacks callback) {
//        indexesAPI= RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
//        Call<DistrictModel> call = indexesAPI.getDistrictByDistrictId(city_obj.getDistrictId());
//        call.enqueue(new Callback<DistrictModel>() {
//            @Override
//            public void onResponse(Call<DistrictModel> call, Response<DistrictModel> response) {
//                DistrictModel district = response.body();
//                if(callback != null) {
//                    callback.getDataFromDistrict(district);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<DistrictModel> call, Throwable t) {
//                call.cancel();
//                Log.d("eroare", t.toString());
//            }
//        });
//    }
//
//    public void getCountryFromDistrict(Context context, RevealDetailsCallbacks callback) {
//
//        indexesAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
//        Call<CountryModel> call = indexesAPI.getCountryByCountryId(district_obj.getCountryId());
//        call.enqueue(new Callback<CountryModel>() {
//            @Override
//            public void onResponse(Call<CountryModel> call, Response<CountryModel> response) {
//                CountryModel country = response.body();
//                if(callback != null) {
//                    callback.getDataFromCountry(country);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CountryModel> call, Throwable t) {
//                call.cancel();
//                Log.d("eroare", t.toString());
//            }
//        });
//    }

//    public void populateAdress()
//    {
//        addressClient.setText(buildAddress());
//    }

//    public String buildAddress() {
//        String address = "";
//                 if( country_obj != null) {
//                    if (country_obj.getCountryName() != null & country_obj.getCountryName() != "")
//                        address = address + country_obj.getCountryName() + " ";
//
//                    if (district_obj.getDistrictName() != null & district_obj.getDistrictName() != "")
//                        address = address + district_obj.getDistrictName() + " ";
//
//                    if (city_obj.getCityName() != null & city_obj.getCityName() != "")
//                        address = address + city_obj.getCityName() + " ";
//
//                    if (address_obj.getStreet() != "" & address_obj.getStreet() != null)
//                        address = address + address_obj.getStreet() + " ";
//
//                    if (address_obj.getStreetNumber() != "" & address_obj.getStreetNumber() != null)
//                        address = address + address_obj.getStreetNumber() + " ";
//
//                    if (address_obj.getImmobileNumber() != null & address_obj.getImmobileNumber() != "")
//                        address = address + address_obj.getImmobileNumber() + " ";
//
//                    if (address_obj.getStairNumber() != null & address_obj.getStairNumber() != "")
//                        address = address + address_obj.getStairNumber() + " ";
//
//                    if (address_obj.getFloorNumber() != 0)
//                        address = address + address_obj.getFloorNumber() + " ";
//
//                    if (address_obj.getFlatNumber() != 0)
//                        address = address + address_obj.getFlatNumber() + " ";
//                }
//
//
//        if (address == "")
//            address += "nu merge";
//         return address;
//    }
//
////    public void SpinnerThink()
//    {
//        ArrayAdapter<String> adapterSpineer = new ArrayAdapter<String>
//                (this,android.R.layout.simple_spinner_item, Collections.singletonList(buildAddress()));
//        adapterSpineer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        adressSpinner.setAdapter(adapterSpineer);
//    }

    @Override
    public void onIndexListener(int position) {

    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
