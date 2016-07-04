package com.yoleth.poc;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by mael on 04/07/16.
 */
public class MainApplication extends Application {

    private static ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);

        mComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

    }

    public static ApplicationComponent getComponent() {
        return mComponent;
    }

}
