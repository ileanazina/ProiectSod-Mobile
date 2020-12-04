package com.example.myapplication1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import com.example.myapplication1.Activities.Form.FormsFragment;
import com.example.myapplication1.Activities.Index.IndexFragment;
import com.example.myapplication1.Activities.Invoice.InvoicesFragment;
import com.example.myapplication1.Activities.ProfileFragment;
import com.example.myapplication1.Model.AccountModel;
import com.example.myapplication1.Model.InvoiceModel;
import com.example.myapplication1.Model.TokenModel;
import com.example.myapplication1.Remote.APIInterfaces;
import com.example.myapplication1.Remote.RetrofitClientLogIn;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public int intentFragment;
    public AccountModel account;
    public SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = mPrefs.getString("AccountInfo",null);
        account = gson.fromJson(json, AccountModel.class);

        timer();

        BottomNavigationView bottomNav = findViewById(R.id.nav_view);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.homeNavbar);
        intentFragment = getIntent().getExtras().getInt("startFromHome");
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.indexNavbar:
                        selectedFragment = new IndexFragment();
                        break;
                    case R.id.formsNavbav:
                        selectedFragment = new FormsFragment();
                        break;
                    case R.id.homeNavbar:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.invoicesNavbar:
                        selectedFragment = new InvoicesFragment();
                        break;
                    case R.id.profilNavbar:
                        selectedFragment = new ProfileFragment();
                        break;
                    default:
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag, selectedFragment).commit();
                return true;
            }

    };

    private void timer() {
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                APIInterfaces invoiceAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
                Call<AccountModel> call = invoiceAPI.refreshToken(new TokenModel(account.getToken(), account.getRefreshToken()));
                call.enqueue(new Callback<AccountModel>() {
                    @Override
                        public void onResponse(Call<AccountModel> call, Response<AccountModel> response) {
                        account = response.body();
                        SharedPreferences.Editor prefsEditor = mPrefs.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(account);
                        prefsEditor.putString("AccountInfo", json);
                        prefsEditor.commit();
                    }

                    @Override
                    public void onFailure(Call<AccountModel> call, Throwable t) {
                        call.cancel();
                    }
                });
            }
        }, 0, 8 * 60 * 1000);
    }
}