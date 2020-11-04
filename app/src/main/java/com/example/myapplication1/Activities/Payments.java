package com.example.myapplication1.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.example.myapplication1.R;

public class Payments extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        CardForm cardForm = findViewById(R.id.card_form);
        Button buy = findViewById(R.id.btnBuy);

        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Este necesara trimiterea unui SMS")
                .setup(Payments.this);

        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardForm.isValid()) {

                }else {
                    Toast.makeText(Payments.this, "Va rugam completati formularul", Toast.LENGTH_LONG).show();
                }

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(Payments.this);
                alertBuilder.setTitle("Confirmare date");
                alertBuilder.setMessage("Numar card: " + cardForm.getCardNumber() + "\n" +
                        "Data expirare: " + cardForm.getExpirationDateEditText().getText().toString() + "\n" +
                        "CVV: " + cardForm.getCvv() + "\n" +
                        "Cod postal: " + cardForm.getPostalCode() + "\n" +
                        "Telefon: " + cardForm.getMobileNumber());
                alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Toast.makeText(Payments.this, "Multumim!", Toast.LENGTH_LONG).show();
                    }
                });
                alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = alertBuilder.create();
                alertDialog.show();
            }
        });


    }
}