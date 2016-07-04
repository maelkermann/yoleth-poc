package com.yoleth.poc.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mael on 04/07/16.
 */
public class ApiInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request newRequest = chain.request().newBuilder()
//                .header("Authorization", "Bearer "+ YlMainApplication.getAccountToken())
                .build();

        return chain.proceed(newRequest);

    }
}
