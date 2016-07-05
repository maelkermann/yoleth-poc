package com.yoleth.poc.network;


import com.yoleth.poc.models.response.Tokens;
import com.yoleth.poc.models.response.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by mael on 04/07/16.
 */
public interface ApiNoAuth {

    @FormUrlEncoded
    @POST("oauth/token")
    Observable<Tokens> tokens(
            @Header("Authorization") String authorization,
            @Field("grant_type") String grant_type,
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("oauth/token")
    Call<Tokens> tokens(
            @Header("Authorization") String authorization,
            @Field("grant_type") String grant_type,
            @Field("refresh_token") String refresh
    );

    @GET("api/user")
    Observable<User> getUser(
            @Header("Authorization") String authorization
    );

}
