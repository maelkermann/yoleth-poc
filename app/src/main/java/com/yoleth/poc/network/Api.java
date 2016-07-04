package com.yoleth.poc.network;

import android.database.Observable;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by mael on 04/07/16.
 */
public interface Api {

    @FormUrlEncoded
    @POST("oauth/token")
    Observable<String> tokens(
            @Header("Authorization")    String authorization,
            @Field("grant_type")        String grant_type,
            @Field("username")          String username,
            @Field("password")          String password
    );

}
