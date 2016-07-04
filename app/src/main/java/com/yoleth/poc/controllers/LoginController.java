package com.yoleth.poc.controllers;

import android.content.Context;
import android.util.Log;

import com.yoleth.poc.R;
import com.yoleth.poc.network.Api;

import javax.inject.Inject;

import dagger.Module;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
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

        Observable<String> observable = api.tokens(context.getString(R.string.ws_header), "password", email, password);

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d(TAG, "toto");
                    }
                })
                .flatMap(new Func1<String, Observable<?>>() {
                    @Override
                    public Observable<?> call(String s) {
                        return null;
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                })
                .subscribe();

    }

}
