package com.yoleth.poc.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yoleth.poc.R;
import com.yoleth.poc.core.BaseActivity;
import com.yoleth.poc.fragments.login.LoginFragment;

public class LoginActivity extends BaseActivity {


    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void afterCreate() {
        setContent(new LoginFragment());
    }

    @Override
    protected int getMainWrapper() {
        return R.id.activityLoginWrapper;
    }

    @Override
    protected boolean hasMenu(){
        return false;
    }

}
