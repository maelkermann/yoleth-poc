package com.yoleth.poc.network;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.yoleth.poc.MainApplication;
import com.yoleth.poc.R;
import com.yoleth.poc.account.AuthConstants;
import com.yoleth.poc.controllers.AccountController;
import com.yoleth.poc.models.response.Tokens;
import com.yoleth.poc.utils.Utils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mael on 04/07/16.
 */
public class ApiInterceptor implements Interceptor {

    private final static String TAG = ApiInterceptor.class.getSimpleName();

    private AccountController mController;

    public ApiInterceptor(AccountController controller){
        this.mController        = controller;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Log.d(TAG, "intercept request");
        String accessToken = mController.getAccessToken();

        if ( accessToken == null ){
            return null;
        }

        Request newRequest = chain.request().newBuilder()
                .addHeader("Authorization", accessToken)
                .build();

        return chain.proceed(newRequest);

    }

}
