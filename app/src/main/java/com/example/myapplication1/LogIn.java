package com.example.myapplication1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        Button buttonAutentificare = (Button) findViewById(R.id.buttonAutentificare);
        buttonAutentificare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentAutentificare = new Intent(LogIn.this,MainActivity.class);
                startActivity(intentAutentificare);


            }
        });
    }
}