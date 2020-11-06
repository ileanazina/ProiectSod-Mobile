package com.example.myapplication1.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication1.Activities.Invoice.Invoices;
import com.example.myapplication1.R;

public class SearchDateFragment extends Fragment implements View.OnClickListener{
    TextView startDate, endDate;
    String save_startDate, save_endDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmet_data, container, false);
        startDate = view.findViewById(R.id.startDateSearch);
        startDate.setOnClickListener(this);
        endDate = view.findViewById(R.id.endDateSearch);
        endDate.setOnClickListener(this);
        view.findViewById(R.id.searchButtonByData).setOnClickListener(this::onClick);

        return view;
    }

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
                if(save_startDate != null & save_endDate != null)
                    ((Invoices)getActivity()).getInvoiceListFromSearchFragment(save_startDate, save_endDate);
                else Toast.makeText(getActivity(), "Date invalide...", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity();
        if(requestCode == 1 && resultCode == Activity.RESULT_OK) {
            save_startDate = data.getStringExtra("date");
            startDate.setText("Incepand cu: " + save_startDate);
        }
        else
            if(requestCode == 2 && resultCode == Activity.RESULT_OK) {
                save_endDate = data.getStringExtra("date");
                endDate.setText("Pana la: " + save_endDate);
            }
    }
}
