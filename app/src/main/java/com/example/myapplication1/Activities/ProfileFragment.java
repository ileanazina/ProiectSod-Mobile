package com.example.myapplication1.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication1.Model.AccountModel;
import java.util.regex.Pattern;
import com.example.myapplication1.Model.UserEdit;
import com.example.myapplication1.R;
import com.example.myapplication1.Remote.APIInterfaces;
import com.example.myapplication1.Remote.RetrofitClientLogIn;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private AccountModel account;
    private static final String regex = "^(.+)@(.+)$";
    private SharedPreferences mPrefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_page, container, false);
        CompleteTheProfile(view);
        return view;
    }

    public void CompleteTheProfile(View view)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = mPrefs.getString("AccountInfo",null);
        account = gson.fromJson(json, AccountModel.class);

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
            textView_telephon.setText("0" + account.getTelephoneNumber());

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

        //button update
        Button updateData = view.findViewById(R.id.editProfile);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopupEditDialog(view); }
        });
    }

    private void createPopupEditDialog(View profileView) {
        builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.edit_profile_popup, null);

        EditText editPhone = view.findViewById(R.id.editPhone);
        editPhone.setText("0" + account.getTelephoneNumber());
        EditText editEmail = view.findViewById(R.id.editMail);
        editEmail.setText(account.getEmail());

        Button update = view.findViewById(R.id.saveProfile);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editEmail.getText().toString().isEmpty()&& !editPhone.getText().toString().isEmpty()) {
                    UserEdit user = new UserEdit(account.getAccountId(), editEmail.getText().toString(), Long.parseLong(editPhone.getText().toString()));

                    System.out.println(user.getEmail() + "   " + user.getTelephoneNumber());
                    Pattern pattern = Pattern.compile(regex);
                    if (String.valueOf(user.getTelephoneNumber()).length() == 9 & pattern.matcher(user.getEmail()).matches()
                            && !String.valueOf(user.getTelephoneNumber()).isEmpty() && !user.getEmail().isEmpty()) {
                        APIInterfaces invoiceAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
                        Call<Void> call = invoiceAPI.editUser("Bearer " + account.getToken(), user);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Toast.makeText(ProfileFragment.this.getContext(), getResources().getString(R.string.profile_saved_modify),
                                        Toast.LENGTH_LONG).show();

                                //set email textview
                                account.setEmail(user.getEmail());
                                TextView textView_email = profileView.findViewById(R.id.email);
                                textView_email.setText(account.getEmail());

                                //set telephone textview
                                account.setTelephoneNumber(user.getTelephoneNumber());
                                TextView textView_telephon = profileView.findViewById(R.id.telephonNumber);
                                textView_telephon.setText("0" + account.getTelephoneNumber());

                                //save new info about this account in sharepreferences
                                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(account);
                                prefsEditor.putString("AccountInfo", json);
                                prefsEditor.commit();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(ProfileFragment.this.getContext(), getResources().getString(R.string.profile_error1),
                                        Toast.LENGTH_LONG).show();
                                call.cancel();
                            }
                        });
                    } else if (!pattern.matcher(user.getEmail()).matches() | user.getEmail().isEmpty())
                        Toast.makeText(ProfileFragment.this.getContext(), getResources().getString(R.string.profile_error2),
                                Toast.LENGTH_LONG).show();
                    if (String.valueOf(user.getTelephoneNumber()).isEmpty() | String.valueOf(user.getTelephoneNumber()).length() < 9)
                        Toast.makeText(ProfileFragment.this.getContext(), getResources().getString(R.string.profile_error3),
                                Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(ProfileFragment.this.getContext(), getResources().getString(R.string.profile_error4),
                            Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
        });

        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }
}