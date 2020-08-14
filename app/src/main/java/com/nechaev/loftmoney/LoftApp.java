package com.nechaev.loftmoney;

import android.app.Application;
import android.content.SharedPreferences;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoftApp extends Application {
    private Api mApi;
    private AuthApi authApi;

    public static String TOKEN_KEY = "token";

    @Override
    public void onCreate() {
        super.onCreate();
        ConfigureNetwork();

    }

    private void ConfigureNetwork(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://loftschool.com/android-api/basic/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();

        mApi = retrofit.create(Api.class);
        authApi = retrofit.create(AuthApi.class);
    }

    public SharedPreferences getSharedPreferences() {
        return getSharedPreferences(getString(R.string.app_name), 0);
    }

    public Api getApi() {
        return mApi;
    }

    public AuthApi getAuthApi() {
        return authApi;
    }
}
