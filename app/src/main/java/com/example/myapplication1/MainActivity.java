package com.example.myapplication1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.myapplication1.Activities.Index.IndexFragment;
import com.example.myapplication1.Activities.Invoice.InvoicesFragment;
import com.example.myapplication1.Activities.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    public int intentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}