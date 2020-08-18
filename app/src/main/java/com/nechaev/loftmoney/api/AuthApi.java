package com.nechaev.loftmoney.api;

import com.nechaev.loftmoney.data.AuthResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AuthApi {
    @GET("./auth")
    Single<AuthResponse> performLogin(@Query("social_user_id") String userId);
}