package com.example.myapplication1.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication1.Model.AccountModel;
import com.example.myapplication1.R;
import com.google.gson.Gson;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_page, container, false);
        CompleteTheProfile(view);
        return view;
    }

    public void CompleteTheProfile(View view)
    {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = mPrefs.getString("AccountInfo",null);
        AccountModel account = gson.fromJson(json, AccountModel.class);

        //set accountId textview
        TextView textView_accountid = view.findViewById(R.id.accoundId);
        textView_accountid.setText(Integer.toString(account.getAccountId()));

        //set email textview
        TextView textView_email = view.findViewById(R.id.email);
        if(account.getEmail() == "" || account.getEmail() == null)
            textView_email.setText("-");
        else
            textView_email.setText(account.getEmail());

        //set telephone textview
        TextView textView_telephon = view.findViewById(R.id.telephonNumber);
        if(account.getTelephoneNumber() == 0)
            textView_telephon.setText("-");
        else
            textView_telephon.setText(String.valueOf(account.getTelephoneNumber()));

        //set nametextview
        TextView textView_name = view.findViewById(R.id.name);
        textView_name.setText(account.getFirstName()+" "+account.getLastName());

        //set company textview and cnp/cui
        TextView textView_companyname = view.findViewById(R.id.companyname);
        TextView textView_companyintro = view.findViewById(R.id.companyname_intro);
        TextView textView_cnp_cui = view.findViewById(R.id.personalCOD);
        TextView textView_cnp_cui_intro = view.findViewById(R.id.personalCOD_intro);
        if (account.isIndividual() == true)
        {
            textView_companyname.setText(getResources().getString(R.string.profile_individual_person));
            textView_cnp_cui_intro.setText(getResources().getString(R.string.profile_cnp));
            if(account.getCnp() == 0)
                textView_cnp_cui.setText("-");
            else
                textView_cnp_cui.setText(String.valueOf(account.getCnp()));
        }
        else
        {
            textView_cnp_cui_intro.setText(getResources().getString(R.string.profile_cui));
            if(account.getCui() == 0)
                textView_cnp_cui.setText("-");
            else
                textView_cnp_cui.setText(String.valueOf(account.getCui()));

            textView_companyintro.setText(getResources().getString(R.string.profile_company_name));
            if(account.getCompanyName() == "" || account.getCompanyName() == null) {
                textView_companyname.setText("-");
            }
            else {
                textView_companyname.setText(account.getCompanyName());
            }
        }
    }
}