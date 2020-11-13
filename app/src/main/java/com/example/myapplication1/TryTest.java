package com.example.myapplication1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;


import com.example.myapplication1.Activities.Forms;
import com.example.myapplication1.Activities.Index.Index;
import com.example.myapplication1.Activities.Index.IndexF;
import com.example.myapplication1.Activities.Invoice.Invoices;
import com.example.myapplication1.Activities.Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TryTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try_test);

        BottomNavigationView bottomNav = findViewById(R.id.nav_view);

        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.index:
                        selectedFragment = new IndexF();
                        break;
//                    case R.id.formulare:
//                        selectedFragment = new Forms();
//                        break;
//                    case R.id.home:
//                        selectedFragment = new MainActivity();
//                        break;
//                    case R.id.facturi:
//                        selectedFragment = new Invoices();
//                        break;
//                    case R.id.profil:
//                        selectedFragment = new Profile();
//                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag,
                        selectedFragment).commit();
                return true;
            }

    };
}