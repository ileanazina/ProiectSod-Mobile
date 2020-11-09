package com.example.myapplication1.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication1.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

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
                Calendar calendar = Calendar.getInstance();
                calendar.set(picker.getYear(), picker.getMonth(), picker.getDayOfMonth());
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
                df.setTimeZone(TimeZone.getTimeZone("Europe/London"));

                String date = df.format(calendar.getTime());

                intent.removeExtra("date");
                intent.putExtra("date", date);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
