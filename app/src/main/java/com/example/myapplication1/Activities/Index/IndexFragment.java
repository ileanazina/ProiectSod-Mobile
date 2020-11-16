package com.example.myapplication1.Activities.Index;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication1.MainActivity;
import com.example.myapplication1.Model.AccountModel;
import com.example.myapplication1.Model.AddIIndex;
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

public class IndexFragment extends Fragment implements IndexAdaptor.OnIndexListener {

    public interface RevealDetailsCallbacks {
        void getDataFromIndex(List<IndexModel> list);
    }

    private RecyclerView recyclerView;
    private IndexAdaptor indexesAdaptor;
    private APIInterfaces indexesAPI;
    private CompositeDisposable compositeDisposable= new CompositeDisposable();
    private IndexFragment.RevealDetailsCallbacks callback;
    private AlertDialog.Builder builder;

    private Button addIndex;
    private EditText addIndexValue;
    private ImageView img;

    private List<IndexModel> allIndexes;
    private List<IndexModel> forAdaptorIndexes;
    AccountModel account;
    private int AddressIdFromSpinner;
    private AlertDialog dialogError;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index_page, container, false);

        recyclerView = view.findViewById(R.id.indexRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        allIndexes = new ArrayList<>();
        forAdaptorIndexes = new ArrayList<>();

        indexesAdaptor = new IndexAdaptor(getContext(), forAdaptorIndexes, IndexFragment.this);
        recyclerView.setAdapter(indexesAdaptor);

        addIndex = view.findViewById(R.id.addIndex);
        addIndexValue = view.findViewById(R.id.addIndexValue);
        img = view.findViewById(R.id.imageView);

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = mPrefs.getString("AccountInfo", null);
        account = gson.fromJson(json, AccountModel.class);

        SharedPreferences  aPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        int ad= aPrefs.getInt("Address",0);
        AddressIdFromSpinner = ad;
        Log.e("AddressId", String.valueOf(AddressIdFromSpinner));
        this.callback = new IndexFragment.RevealDetailsCallbacks() {
            @Override
            public void getDataFromIndex(List<IndexModel> list) {
                for (int i = 0; i < list.size(); i++) {
                    if (account.getAccountId() == list.get(i).getAccountId()&&list.get(i).getAddressId()==AddressIdFromSpinner)
                    {
                        allIndexes.add(list.get(i));
                        forAdaptorIndexes.add(list.get(i));
                        indexesAdaptor.notifyDataSetChanged();
                    }
                }
            }
        };

        getIndexList(getContext(), callback);
            addIndex.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String strIndex = String.valueOf(addIndexValue.getText());
                    float indexValue = Float.parseFloat(strIndex);
                    if (addIndexValue.getText().toString().isEmpty() || addIndexValue.getText() == null) {
                        addIndexValue.setError("Index invalid");
                    } else {

                        AddIIndex index = new AddIIndex(indexValue, account.getAccountId(), AddressIdFromSpinner);
                        Call<IndexModel> call = indexesAPI.insertIndex(index);
                        call.enqueue(new Callback<IndexModel>() {
                            @Override
                            public void onResponse(Call<IndexModel> call, Response<IndexModel> response) {
                                IndexModel model = response.body();
                                if (model == null) {
                                    Toast.makeText(getContext(), "Invalid", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getContext(), "Succes", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<IndexModel> call, Throwable t) {
                                call.cancel();
                            }
                        });
                    }

                    Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container_view_tag);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.detach(currentFragment);
                    fragmentTransaction.attach(currentFragment);
                    fragmentTransaction.commit();
                }
            });

        if (Calendar.DAY_OF_MONTH >= 10 && Calendar.DAY_OF_MONTH <= 25) {

            builder = new AlertDialog.Builder(getContext());

            inflater = LayoutInflater.from(getContext());
            View view_alert = inflater.inflate(R.layout.index_alert_dialog, null);

            Button noButton = view_alert.findViewById(R.id.button_indcancel);
            Button yesButton = view_alert.findViewById(R.id.button_indreload);

            builder.setView(view_alert);
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
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
            });
        }
        return view;
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
            }
        });
    }

    @Override
    public void onIndexListener(int position) {

    }
}