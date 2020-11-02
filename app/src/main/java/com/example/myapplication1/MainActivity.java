package com.example.myapplication1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication1.Activities.InvoiceDetails;
import com.example.myapplication1.Activities.Forms;
import com.example.myapplication1.Activities.Index;
import com.example.myapplication1.Activities.Invoices;
import com.example.myapplication1.Activities.Payments;
import com.example.myapplication1.Activities.Profile;

public class MainActivity extends AppCompatActivity {

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonDetails = (Button) findViewById(R.id.buttonDetails);
        buttonDetails.setWidth((int) (getScreenWidth()*0.3));
        buttonDetails.setHeight((int) (getScreenHeight()*0.2));
        buttonDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentDetails = new Intent(MainActivity.this, InvoiceDetails.class);
                startActivity(intentDetails);


            }
        });

        Button buttonForms = (Button) findViewById(R.id.buttonForms);
        buttonForms.setWidth((int) (getScreenWidth()*0.3));
        buttonForms.setHeight((int) (getScreenHeight()*0.2));
        buttonForms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentForms = new Intent(MainActivity.this, Forms.class);
                startActivity(intentForms);
            }
        });

        Button buttonIndex = (Button) findViewById(R.id.buttonIndex);
        buttonIndex.setWidth((int) (getScreenWidth()*0.3));
        buttonIndex.setHeight((int) (getScreenHeight()*0.2));
        buttonIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentIndex = new Intent(MainActivity.this, Index.class);
                startActivity(intentIndex);
            }
        });


        Button buttonInvoices = (Button) findViewById(R.id.buttonInvoice);
        buttonInvoices.setWidth((int) (getScreenWidth()*0.3));
        buttonInvoices.setHeight((int) (getScreenHeight()*0.2));
        buttonInvoices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentInvoices = new Intent(MainActivity.this, Invoices.class);
                startActivity(intentInvoices);
            }
        });


        Button buttonPayments = (Button) findViewById(R.id.buttonPayments);
        buttonPayments.setWidth((int) (getScreenWidth()*0.3));
        buttonPayments.setHeight((int) (getScreenHeight()*0.2));
        buttonPayments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPayments = new Intent(MainActivity.this, Payments.class);
                startActivity(intentPayments);
            }
        });


        Button buttonProfile = (Button) findViewById(R.id.buttonProfile);
        buttonProfile.setWidth((int) (getScreenWidth()*0.3));
        buttonProfile.setHeight((int) (getScreenHeight()*0.2));
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentProfile = new Intent(MainActivity.this, Profile.class);
                startActivity(intentProfile);

            }
        });



    }


}