package com.example.myapplication1.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.example.myapplication1.Model.AccountModel;
import com.example.myapplication1.R;
import com.google.gson.Gson;

import io.reactivex.disposables.CompositeDisposable;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        CompleteTheProfile();
    }

    public void CompleteTheProfile()
    {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = mPrefs.getString("AccountInfo",null);
        AccountModel account = gson.fromJson(json, AccountModel.class);

        //set accountId textview
        TextView textView_accountid = findViewById(R.id.accoundId);
        textView_accountid.setText(Integer.toString(account.getAccountId()));

        //set email textview
        TextView textView_email = findViewById(R.id.email);
        if(account.getEmail() == "" || account.getEmail() == null)
            textView_email.setText("-");
        else
            textView_email.setText(account.getEmail());

        //set telephone textview
        TextView textView_telephon = findViewById(R.id.telephonNumber);
        if(account.getTelephoneNumber() == 0)
            textView_telephon.setText("-");
        else
            textView_telephon.setText(String.valueOf(account.getTelephoneNumber()));

        //set nametextview
        TextView textView_name = findViewById(R.id.name);
        textView_name.setText(account.getFirstName()+" "+account.getLastName());

        //set company textview and cnp/cui
        TextView textView_companyname = findViewById(R.id.companyname);
        TextView textView_companyintro = findViewById(R.id.companyname_intro);
        TextView textView_cnp_cui = findViewById(R.id.personalCOD);
        TextView textView_cnp_cui_intro = findViewById(R.id.personalCOD_intro);
        if (account.isIndividual() == true)
        {
            textView_companyname.setText("Persoana fizica");
            textView_cnp_cui_intro.setText("CNP");
            if(account.getCnp() == 0)
                textView_cnp_cui.setText("-");
            else
                textView_cnp_cui.setText(String.valueOf(account.getCnp()));
        }
        else
        {
            textView_cnp_cui_intro.setText("CUI");
            if(account.getCui() == 0)
                textView_cnp_cui.setText("-");
            else
                textView_cnp_cui.setText(String.valueOf(account.getCui()));

            textView_companyintro.setText("Company Name");
            if(account.getCompanyName() == "" || account.getCompanyName() == null) {
                textView_companyname.setText("-");
            }
            else {
                textView_companyname.setText(account.getCompanyName());
            }
        }
    }
}