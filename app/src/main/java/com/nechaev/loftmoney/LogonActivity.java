package com.nechaev.loftmoney;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LogonActivity extends AppCompatActivity {
    Button loginButtonView;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon);

        loginButtonView = findViewById(R.id.loginButton);

        loginButtonView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String socialUserId = String.valueOf(new Random().nextInt());
                compositeDisposable.add(((LoftApp)getApplication()).getAuthApi().performLogin(socialUserId)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<AuthResponse>() {
                                       @Override
                                       public void accept(AuthResponse authResponse) throws Exception {
                                           ((LoftApp) getApplication()).getSharedPreferences().edit().putString(LoftApp.TOKEN_KEY, authResponse.getAuthToken()).apply();
                                           Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                                           startActivity(mainIntent);
                                       }
                                   }, new Consumer<Throwable>() {
                                       @Override
                                       public void accept(Throwable throwable) throws Exception {
                                           Log.e("TAG", "Error: " + throwable);
                                       }
                                   }
                        ));
            }
        });
    }
}