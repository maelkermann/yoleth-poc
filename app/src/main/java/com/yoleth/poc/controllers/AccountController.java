package com.yoleth.poc.controllers;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.yoleth.poc.R;
import com.yoleth.poc.models.response.Tokens;
import com.yoleth.poc.models.response.User;
import com.yoleth.poc.network.Api;
import com.yoleth.poc.account.AuthConstants;
import com.yoleth.poc.network.ApiNoAuth;
import com.yoleth.poc.utils.Utils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by mael on 04/07/16.
 */
public class AccountController {

    private static final String TAG = AccountController.class.getSimpleName();

    private AccountManager accountManager;
    private ApiNoAuth api;
    private Context mContext;

    public AccountController(Context context, ApiNoAuth api) {
        this.mContext = context;
        this.api = api;
        this.accountManager = AccountManager.get(mContext);
    }

    public void login(String email, String password){
        Log.d(TAG, "login with email : " + email +" and password : " + password);

        api.tokens(mContext.getString(R.string.ws_header), "password", email, password)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap(new Func1<Tokens, Observable<User>>() {
                @Override
                public Observable<User> call(Tokens t) {
                    Log.d(TAG, t.getAccess_token());
                    return api.getUser("Bearer " + t.getAccess_token())
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread());
                }
            }, new Func2<Tokens, User, Pair<Tokens, User>>() {
                @Override
                public Pair<Tokens, User> call(Tokens tokens, User user) {
                    return new Pair<>(tokens, user);
                }
            })
            .subscribe(new Action1<Pair<Tokens, User>>() {
                @Override
                public void call(Pair<Tokens, User> tokensUserPair) {
                    User user = tokensUserPair.second;
                    saveTokens(tokensUserPair.first, user.getUsername());
                    saveUser(user);
                    Log.d(TAG, user.getEmail());
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Log.e(TAG, "error : "+throwable.getMessage());
                }
            });

    }

    public void saveUser(User user){
        Log.d(TAG, "save user");

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(user);
        realm.commitTransaction();

    }

    public void saveTokens(Tokens tokens, String username){
        Log.d(TAG, "save tokens");

        Date limit      = new Date();
        Long dateLimit  = limit.getTime()+tokens.getExpires_in() * 1000;

        Account account = addOrFindAccount(username, tokens.getRefresh_token());
        accountManager.setAuthToken(account, AuthConstants.AUTHTOKEN_TYPE, tokens.getAccess_token());
        accountManager.setUserData(account, AuthConstants.KEY_TOKEN_LIMIT, "" + dateLimit);
        accountManager.setUserData(account, AuthConstants.KEY_TOKEN_TYPE, tokens.getToken_type());

    }

    private Account addOrFindAccount(String email, String password) {
        Log.d(TAG, "add or find account");

        Account[] accounts  = accountManager.getAccountsByType(AuthConstants.ACCOUNT_TYPE);
        Account account     = accounts.length != 0 ? accounts[0] : new Account(email, AuthConstants.ACCOUNT_TYPE);

        if (accounts.length == 0) {
            accountManager.addAccountExplicitly(account, password, null);
        } else {
            accountManager.setPassword(accounts[0], password);
        }

        return account;

    }

    public String getAccessToken(){
        Log.d(TAG, "get access token");

        Account[] accounts  = accountManager.getAccountsByType(AuthConstants.ACCOUNT_TYPE);

        if (accounts.length != 0) {

            String token    = accountManager.peekAuthToken(accounts[0], AuthConstants.AUTHTOKEN_TYPE);
            String password = accountManager.getPassword(accounts[0]);
            String limit    = accountManager.getUserData(accounts[0], AuthConstants.KEY_TOKEN_LIMIT);
            String type     = accountManager.getUserData(accounts[0], AuthConstants.KEY_TOKEN_TYPE);

            Log.d(TAG, "token : "+token);
            Log.d(TAG, "limit : "+limit);
            Log.d(TAG, "type : "+type);

            Date dateLimit      = new Date();
            dateLimit.setTime(Long.parseLong(limit));

            if ( (TextUtils.isEmpty(token) || dateLimit.before(Utils.addPeriod(new Date(), 60, Calendar.SECOND))) && !TextUtils.isEmpty(password) ) {
                try {
                    Log.d(TAG, "get new token from refresh token");
                    Tokens tokens = api.tokens(mContext.getString(R.string.ws_header), "refresh_token", password).execute().body();
                    saveTokens(tokens, accounts[0].name);
                    return Utils.ucfirst(tokens.getToken_type())+" "+tokens.getAccess_token();
                } catch (IOException e) {
                    removeAccounts();
                    Log.e(TAG, "error getting new token, remove account : "+e.getMessage());
                }
            }else if ( TextUtils.isEmpty(password) ){
                removeAccounts();
                Log.d(TAG, "empty refresh token, remove account");
            }else {
                Log.d(TAG, "valid access token");
                return Utils.ucfirst(type)+" "+token;
            }

        }else{
            Log.d(TAG, "no accounts");
        }

        return null;

    }

    public void removeAccounts(){
        Log.d(TAG, "remove accounts");

        Account[] accounts  = accountManager.getAccountsByType(AuthConstants.ACCOUNT_TYPE);
        for ( Account account : accounts ) {
            removeAccount(account);
        }

    }

    private void removeAccount(Account account){
        Log.d(TAG, "remove account "+account.name);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            accountManager.removeAccount(account, null, null, null);
        }else{
            accountManager.removeAccount(account, null, null);
        }
    }

}
