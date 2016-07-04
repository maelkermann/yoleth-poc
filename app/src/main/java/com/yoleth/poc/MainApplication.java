package com.yoleth.poc;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.yoleth.poc.module.ApplicationComponent;
import com.yoleth.poc.module.ApplicationModule;
import com.yoleth.poc.module.DaggerApplicationComponent;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by mael on 04/07/16.
 */
public class MainApplication extends Application {

    private static ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);

        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);

        mComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

    }

    public static ApplicationComponent getComponent() {
        return mComponent;
    }

}
