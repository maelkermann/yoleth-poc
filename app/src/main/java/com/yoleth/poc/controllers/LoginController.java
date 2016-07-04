package com.yoleth.poc.controllers;

import android.util.Log;

import com.yoleth.poc.network.Api;

import javax.inject.Inject;

import dagger.Module;

/**
 * Created by mael on 04/07/16.
 */
public class LoginController {

    private static final String TAG = LoginController.class.getSimpleName();

    private Api api;

    public LoginController(Api api) {
        this.api = api;
    }

    public void login(String email, String password){

        Log.d(TAG, "email : " + email +", password : " + password);

    }

}
