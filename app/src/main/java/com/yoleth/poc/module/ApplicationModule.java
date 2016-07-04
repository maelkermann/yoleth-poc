package com.yoleth.poc.module;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yoleth.poc.R;
import com.yoleth.poc.controllers.AccountController;
import com.yoleth.poc.network.Api;
import com.yoleth.poc.network.ApiInterceptor;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mael on 04/07/16.
 */
@Module
public class ApplicationModule {

    Context mContext;

    public ApplicationModule(Context context) {
        mContext = context;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(ApiInterceptor apiInterceptor) {
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(apiInterceptor)
                .build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(mContext.getString(R.string.ws_root_url))
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    ApiInterceptor provideApiInterceptor() {
        return new ApiInterceptor(mContext);
    }

    @Provides
    @Singleton
    Api provideApi(Retrofit retrofit) {
        return retrofit.create(Api.class);
    }

    @Provides
    @Singleton
    AccountController provideAccountController(Api api) {
        return new AccountController(mContext, api);
    }

}
