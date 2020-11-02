package com.example.myapplication1.Activities;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import com.example.myapplication1.R;
import com.example.myapplication1.RecycleView.InvoiceAdaptor;

public class Invoices extends AppCompatActivity implements InvoiceAdaptor.OnInvoiceListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoices);
    }

    @Override
    public void onInvoiceListener(int position) {
        Intent intent = new Intent(this, InvoiceDetails.class);
        startActivity(intent);
    }
}