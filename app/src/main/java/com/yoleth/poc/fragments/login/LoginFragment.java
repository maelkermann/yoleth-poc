package com.yoleth.poc.fragments.login;

import android.database.Observable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;
import com.yoleth.poc.MainApplication;
import com.yoleth.poc.R;
import com.yoleth.poc.controllers.LoginController;
import com.yoleth.poc.core.ContentFragment;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import dagger.Module;
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

    @Inject LoginController loginController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.getComponent().inject(this);
    }

    @Override
    protected void afterCreate() {

        RxView.clicks(loginSubmit).debounce(400, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                loginController.login(loginEmail.getText().toString(), loginPassword.getText().toString());
            }
        });

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_login;
    }

}
