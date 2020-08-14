package com.nechaev.loftmoney;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {
    @GET("./items")
    Single<List<MoneyItem>> getItems(@Query("auth-token") String token, @Query("type") String type);

    @POST("./items/add")
    @FormUrlEncoded
    Completable addMoney(@Query("auth-token") String token, @Field("price") String price,@Field("name") String name, @Field("type") String type);

    @POST("./items/remove")
    Call<Status> removeItem(@Query("id") String id, @Query("auth-token") String token);
//    @GET("auth")
//    Call<Status> auth(@Query("social_user_id") String userId);
//
//    @GET("items")
//    Call<List<Item>> getItems(@Query("type") String type, @Query("auth-token") String token);
//
//    @POST("items/add")
//    Call<Status> addItem(@Body AddItemRequest request, @Query("auth-token") String token);
//
//    @POST("items/remove")
//    Call<Status> removeItem(@Query("id") String id, @Query("auth-token") String token);
//
//    @GET("balance")
//    Call<BalanceResponce> getBalance(@Query("auth-token") String token);
}
