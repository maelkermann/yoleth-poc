package com.yoleth.poc;

import com.yoleth.poc.core.BaseActivity;
import com.yoleth.poc.core.BaseFragment;
import com.yoleth.poc.fragments.login.LoginFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by mael on 04/07/16.
 */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    void inject(BaseActivity activity);
    void inject(LoginFragment fragment);

}
