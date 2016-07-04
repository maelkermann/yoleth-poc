package com.yoleth.poc.controllers;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.yoleth.poc.R;
import com.yoleth.poc.models.response.Tokens;
import com.yoleth.poc.models.response.User;
import com.yoleth.poc.network.Api;

import javax.inject.Inject;

import dagger.Module;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by mael on 04/07/16.
 */
public class LoginController {

    private static final String TAG = LoginController.class.getSimpleName();

    private Api api;
    private Context context;

    public LoginController(Context context, Api api) {
        this.context = context;
        this.api = api;
    }

    public void login(String email, String password){

        Log.d(TAG, "email : " + email +", password : " + password);

        api.tokens(context.getString(R.string.ws_header), "password", email, password)
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
                    Log.d(TAG, tokensUserPair.second.getEmail());
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Log.e(TAG, "error : "+throwable.getMessage());
                }
            });

    }

}
