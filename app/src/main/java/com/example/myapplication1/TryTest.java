package com.example.myapplication1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.myapplication1.Activities.FormsFragment;
import com.example.myapplication1.Activities.Index.IndexFragment;
import com.example.myapplication1.Activities.Invoice.InvoicesFragment;
import com.example.myapplication1.Activities.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TryTest extends AppCompatActivity {
    public int intentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try_test);

        BottomNavigationView bottomNav = findViewById(R.id.nav_view);

        bottomNav.setOnNavigationItemSelectedListener(navListener);
      intentFragment = getIntent().getExtras().getInt("startFromHome");
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
//                    if(intentFragment!= 0)
//                    {
//                        selectedFragment = new MainActivity();
//
//                    }
                switch (item.getItemId()) {
                    case R.id.index:
                        selectedFragment = new IndexFragment();
                        break;
                    case R.id.formulare:
                        selectedFragment = new FormsFragment();
                        break;
                    case R.id.home:
                        selectedFragment = new MainActivity();
                        break;
                    case R.id.facturi:
                        selectedFragment = new InvoicesFragment();
                        break;
                    case R.id.profil:
                        selectedFragment = new ProfileFragment();
                        break;
                    default:
                        selectedFragment = new MainActivity();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag, selectedFragment).commit();
                return true;
            }

    };
}