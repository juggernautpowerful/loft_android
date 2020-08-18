package com.nechaev.loftmoney.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.nechaev.loftmoney.LoftApp;
import com.nechaev.loftmoney.R;
import com.nechaev.loftmoney.activities.LogonActivity;
import com.nechaev.loftmoney.activities.MainActivity;

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