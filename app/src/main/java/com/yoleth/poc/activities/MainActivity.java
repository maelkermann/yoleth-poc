package com.yoleth.poc.activities;

import com.yoleth.poc.R;
import com.yoleth.poc.core.BaseActivity;
import com.yoleth.poc.fragments.login.LoginFragment;
import com.yoleth.poc.fragments.main.MainFragment;

public class MainActivity extends BaseActivity {


    @Override
    protected int getLayout() {
        return R.layout.activity_main_withmenu;
    }

    @Override
    protected void afterCreate() {
        setContent(new MainFragment());
    }

    @Override
    protected int getMainWrapper() {
        return R.id.activityMainWrapper;
    }

}
