package com.example.myapplication1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication1.Model.AccountModel;
import com.example.myapplication1.Model.UserLogIn;
import com.example.myapplication1.Remote.APIInterfaces;
import com.example.myapplication1.Remote.RetrofitClientLogIn;
import com.google.gson.Gson;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.SharedPreferences;
import java.util.Locale;

public class LogIn extends AppCompatActivity {

    public interface RevealDetailsCallbacks {
        void getDataFromResult(AccountModel list);
    }

    APIInterfaces logInAPI;
    CompositeDisposable compositeDisposable= new CompositeDisposable();
    EditText userName, userPassword;
    TextView saveData, restoreData;
    RevealDetailsCallbacks callback;
    AccountModel account;

    private AlertDialog.Builder builder;
    private AlertDialog dialogError, dialogNetwork;
    private LayoutInflater inflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getSharedPreferences("Language", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String language = sharedPreferences.getString("saveTheLanguage","");
        if(language == "")
            setLanguage("ro-rRO");
        else setLanguage(language);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        saveData = findViewById(R.id.tvSave) ;
        restoreData = findViewById(R.id.tvRemember);

        //change the language
        ImageButton imageButtonFlags = findViewById(R.id.languageButtonLogIn);
        if(language == "" | language == "ro-rRO")
            imageButtonFlags.setBackgroundResource(R.drawable.romanianflag);
        else imageButtonFlags.setBackgroundResource(R.drawable.englishflag);

        imageButtonFlags.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(LogIn.this);
                inflater = LayoutInflater.from(LogIn.this);
                View view = inflater.inflate(R.layout.custom_language_dialog, null);

                ImageButton ro_imageButton = view.findViewById(R.id.choseRomanianFlag);
                ImageButton en_imageButton = view.findViewById(R.id.choseEnglishFlag);

                builder.setView(view);
                dialogError = builder.create();
                dialogError.show();

                ro_imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editor.putString("saveTheLanguage","ro-rRO");
                        editor.apply();
                        setLanguage("ro-rRO");
                        finish();
                        startActivity(getIntent());
                        dialogError.dismiss();
                    }
                });

                en_imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editor.putString("saveTheLanguage","en");
                        editor.apply();
                        setLanguage("en");
                        finish();
                        startActivity(getIntent());
                        dialogError.dismiss();
                    }
                });
            }
        });
        //here i finish with the language

        this.callback = new RevealDetailsCallbacks() {
            @Override
            public void getDataFromResult(AccountModel accountModel) {
                SharedPreferences  mPrefs = PreferenceManager.getDefaultSharedPreferences(LogIn.this);
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(accountModel);
                prefsEditor.putString("AccountInfo", json);
                prefsEditor.commit();
                account = accountModel;
            }
        };

        saveData.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("User info", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user", userName.getText().toString());
                editor.putString("password", userPassword.getText().toString());
                editor.apply();

                Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_save_toast), Toast.LENGTH_SHORT).show();
            }
        });

        restoreData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreference= getSharedPreferences("User info", Context.MODE_PRIVATE);
                String user=sharedPreference.getString("user","");
                String password=sharedPreference.getString("password","");

                userName.setText(user);
                userPassword.setText(password);
            }
        });

        //initializare api
        logInAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
        userName = findViewById(R.id.userName);
        userPassword = findViewById(R.id.userPassword);

        Button buttonAutentificare = (Button) findViewById(R.id.buttonAutentificare);
        buttonAutentificare.setOnClickListener(new View.OnClickListener() {

            SharedPreferences sharedPreferences= getSharedPreferences("User info", Context.MODE_PRIVATE);

            @Override
            public void onClick(View view) {
                UserLogIn user = new UserLogIn(userName.getText().toString(), userPassword.getText().toString());
                Call<AccountModel> call = logInAPI.loginUser("Bearer " + account.getToken(), user);
                call.enqueue(new Callback<AccountModel>() {
                    @Override
                    public void onResponse(Call<AccountModel> call, Response<AccountModel> response) {
                        AccountModel account = response.body();
                        if(account == null) {
                            builder = new AlertDialog.Builder(LogIn.this);

                            inflater = LayoutInflater.from(LogIn.this);
                            View view = inflater.inflate(R.layout.custom_alert_dialog, null);

                            Button noButton = view.findViewById(R.id.button_cancel);
                            Button yesButton = view.findViewById(R.id.button_reload);

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
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                }
                            });
                        }
                        else {
                            if(callback != null) {
                                callback.getDataFromResult(account);
                            }

                            Toast.makeText(LogIn.this, LogIn.this.getResources().getString(R.string.login_connect_toast), Toast.LENGTH_LONG).show();

                            Intent intentAutentificare = new Intent(LogIn.this, MainActivity.class);
                            intentAutentificare.putExtra("startFromHome", R.id.home);
                            startActivity(intentAutentificare);
                        }
                    }

                    @Override
                    public void onFailure(Call<AccountModel> call, Throwable t) {
                        call.cancel();
                        builder = new AlertDialog.Builder(LogIn.this);

                        inflater = LayoutInflater.from(LogIn.this);
                        View view = inflater.inflate(R.layout.custom_network_dialog, null);

                        Button ntnoButton = view.findViewById(R.id.button_ntcancel);
                        Button ntyesButton = view.findViewById(R.id.button_ntreload);

                        builder.setView(view);
                        dialogNetwork = builder.create();
                        dialogNetwork.show();

                        ntyesButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogNetwork.dismiss();
                            }
                        });
                        ntnoButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                android.os.Process.killProcess(android.os.Process.myPid());
                            }
                        });
                    }
                });
            }
        });
    }

    public void setLanguage (String lang){
        String languageToLoad  = lang; // your language
        Locale.setDefault(new Locale(languageToLoad));
        Configuration config = new Configuration();
        config.locale = new Locale(languageToLoad);
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}