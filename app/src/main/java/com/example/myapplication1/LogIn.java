package com.example.myapplication1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication1.Model.UserLogIn;
import com.example.myapplication1.Remote.LogInAPI;
import com.example.myapplication1.Remote.RetrofitClientLogIn;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LogIn extends AppCompatActivity {

    LogInAPI logInAPI;
    CompositeDisposable compositeDisposable= new CompositeDisposable();

    EditText userName, userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //initializare api
        logInAPI = RetrofitClientLogIn.getInstance().create(LogInAPI.class);

        userName = findViewById(R.id.userName);
        userPassword = findViewById(R.id.userPassword);

        Button buttonAutentificare = (Button) findViewById(R.id.buttonAutentificare);
        buttonAutentificare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserLogIn user = new UserLogIn(userName.getText().toString(), userPassword.getText().toString());
                compositeDisposable.add(logInAPI.loginUser(user).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                Toast.makeText(LogIn.this, s, Toast.LENGTH_LONG).show();
                                Intent intentAutentificare = new Intent(LogIn.this,MainActivity.class);
                                startActivity(intentAutentificare);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(LogIn.this,throwable.getMessage(),Toast.LENGTH_LONG).show();
                                Log.d("Eroare", throwable.getMessage().toString());

                            }
                        }));
            }
        });
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}