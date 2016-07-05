package com.yoleth.poc.fragments.login;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;
import com.yoleth.poc.MainApplication;
import com.yoleth.poc.R;
import com.yoleth.poc.controllers.AccountController;
import com.yoleth.poc.core.ContentFragment;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by Mael Kermann on 25/09/2015.
 *
 * Copyright Mael Kermann
 *
 */
public class LoginFragment extends ContentFragment {

    @BindView(R.id.fragmentLoginEmail) EditText loginEmail;
    @BindView(R.id.fragmentLoginPassword) EditText loginPassword;
    @BindView(R.id.fragmentLoginSubmit) Button loginSubmit;

    @Inject AccountController accountController;

    @Override
    protected void afterCreate() {

        MainApplication.getComponent().inject(this);

        RxView.clicks(loginSubmit).debounce(400, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                accountController.login(loginEmail.getText().toString(), loginPassword.getText().toString());
            }
        });

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_login;
    }

}
