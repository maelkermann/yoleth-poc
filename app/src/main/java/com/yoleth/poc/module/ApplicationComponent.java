package com.yoleth.poc.module;

import com.yoleth.poc.fragments.login.LoginFragment;
import com.yoleth.poc.account.AccountAuthenticator;
import com.yoleth.poc.network.ApiInterceptor;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by mael on 04/07/16.
 */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    void inject(LoginFragment fragment);
    void inject(AccountAuthenticator accountAuthenticator);

}
