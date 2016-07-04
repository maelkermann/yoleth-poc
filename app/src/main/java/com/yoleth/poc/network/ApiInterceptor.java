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

    private Context mContext;
    private AccountManager mAccountManager;

    public ApiInterceptor (Context context){
        this.mContext           = context;
        this.mAccountManager    = AccountManager.get(this.mContext);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Log.d(TAG, "intercept request");

        String token        = getAccessToken();
        Request newRequest;

        Log.d(TAG, "access token : "+token);

        if ( token != null ){
             newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", token)
                    .build();
        }else{
            newRequest = chain.request().newBuilder().build();
        }

        return chain.proceed(newRequest);

    }

    private String getAccessToken(){

        Account[] accounts  = mAccountManager.getAccountsByType(AuthConstants.ACCOUNT_TYPE);

        if (accounts.length != 0) {

            String token    = mAccountManager.peekAuthToken(accounts[0], AuthConstants.AUTHTOKEN_TYPE);
            String password = mAccountManager.getPassword(accounts[0]);
            String limit    = mAccountManager.getUserData(accounts[0], AuthConstants.KEY_TOKEN_LIMIT);
            String type     = mAccountManager.getUserData(accounts[0], AuthConstants.KEY_TOKEN_TYPE);

            Log.d(TAG, "token : "+token);
            Log.d(TAG, "limit : "+limit);
            Log.d(TAG, "type : "+type);

            Date dateLimit      = new Date();
            dateLimit.setTime(Long.parseLong(limit));

            if (TextUtils.isEmpty(token) || dateLimit.before(Utils.addPeriod(new Date(), 60, Calendar.SECOND))) {
                // TODO : get new access token
                return null;
            }else{
                return Utils.ucfirst(type)+" "+token;
            }

        }else{
            Log.d(TAG, "no tokens");
        }

        return null;

    }

}
