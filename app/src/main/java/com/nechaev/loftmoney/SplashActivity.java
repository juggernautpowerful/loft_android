package com.nechaev.loftmoney;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        String token = ((LoftApp)getApplication()).getSharedPreferences(getString(R.string.app_name), 0).getString(LoftApp.TOKEN_KEY, "");
        if(token == null || token.isEmpty()){
            Intent logonIntent = new Intent(getApplicationContext(), LogonActivity.class);
            startActivity(logonIntent);
        }else{
            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainIntent);
        }
    }
}