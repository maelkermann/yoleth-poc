package com.yoleth.poc.activities;

import com.yoleth.poc.MainApplication;
import com.yoleth.poc.R;
import com.yoleth.poc.controllers.AccountController;
import com.yoleth.poc.core.BaseActivity;
import com.yoleth.poc.fragments.login.LoginFragment;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity {

    @Inject
    AccountController loginController;

    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void afterCreate() {
        MainApplication.getComponent().inject(this);

        String token = loginController.getAccessToken(false);
        if ( token == null ){
            startActivity(LoginActivity.class);
        }else{
            startActivity(MainActivity.class);
        }

    }

    @Override
    protected int getMainWrapper() {
        return 0;
    }

    @Override
    protected boolean hasMenu(){
        return false;
    }

}
