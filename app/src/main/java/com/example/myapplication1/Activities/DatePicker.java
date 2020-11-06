package com.example.myapplication1.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication1.R;

public class DatePicker extends AppCompatActivity {
    android.widget.DatePicker picker;
    Button btnGet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datapicker);
        picker=findViewById(R.id.datePicker);
        btnGet=findViewById(R.id.button_save_data);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("day",String.valueOf(picker.getDayOfMonth())).putExtra("year",String.valueOf(picker.getYear()))
                        .putExtra("month",String.valueOf(picker.getMonth()));
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
