package com.example.myapplication1.Activities;

import android.app.Activity;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.myapplication1.Activities.Invoice.InvoicesFragment;
import com.example.myapplication1.Model.AccountModel;
import com.example.myapplication1.Model.SearchByDate;
import com.example.myapplication1.R;
import com.google.gson.Gson;

import android.text.format.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchDateFragment extends Fragment implements View.OnClickListener{
    TextView startDate, endDate;
    String save_startDate, save_endDate;
    AccountModel account;
    int addressId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmet_data, container, false);

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Gson gson = new Gson();
        String json = mPrefs.getString("AccountInfo",null);
        account = gson.fromJson(json, AccountModel.class);
        addressId = mPrefs.getInt("Address", 0);

        startDate = view.findViewById(R.id.startDateSearch);
        startDate.setOnClickListener(this);
        endDate = view.findViewById(R.id.endDateSearch);
        endDate.setOnClickListener(this);
        view.findViewById(R.id.searchButtonByData).setOnClickListener(this::onClick);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.startDateSearch:
                Intent intent = new Intent(getActivity(), DatePicker.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.endDateSearch:
                Intent intent1 = new Intent(getActivity(), DatePicker.class);
                startActivityForResult(intent1, 2);
                break;
            case R.id.searchButtonByData:
                if(save_startDate != null & save_endDate != null) {
                    SearchByDate searchByDate = new SearchByDate(account.getAccountId(), addressId, save_startDate,save_endDate);

                    InvoicesFragment fragm = (InvoicesFragment) getFragmentManager().findFragmentById(R.id.fragment_container_view_tag);
                    fragm.getInvoiceListFromSearchFragment(searchByDate);
                }
                else Toast.makeText(getActivity(), getResources().getString(R.string.search_date_fragment_invalid_dates), Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SimpleDateFormat  df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        getActivity();
        if(requestCode == 1 && resultCode == Activity.RESULT_OK) {
            save_startDate = data.getStringExtra("date");
            Date first_date;
            try {
                first_date = df.parse(save_startDate);
                startDate.setText(getResources().getString(R.string.search_date_fragment_from) + DateFormat.format("dd", first_date) + "/"
                        + DateFormat.format("MM", first_date) + "/"
                        + DateFormat.format("yyyy", first_date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else
            if(requestCode == 2 && resultCode == Activity.RESULT_OK) {
                save_endDate = data.getStringExtra("date");
                Date end_date;
                try {
                    end_date = df.parse(save_endDate);
                    endDate.setText(getResources().getString(R.string.search_date_fragment_until) + DateFormat.format("dd", end_date) + "/"
                            + DateFormat.format("MM", end_date) + "/"
                            + DateFormat.format("yyyy", end_date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
    }
}
