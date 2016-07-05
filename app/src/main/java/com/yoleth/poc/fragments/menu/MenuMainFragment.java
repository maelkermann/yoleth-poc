package com.yoleth.poc.fragments.menu;

import android.view.View;
import android.widget.Button;

import com.jakewharton.rxbinding.view.RxView;
import com.yoleth.poc.MainApplication;
import com.yoleth.poc.R;
import com.yoleth.poc.controllers.AccountController;
import com.yoleth.poc.core.BaseFragment;

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
public class MenuMainFragment extends BaseFragment {

    @BindView(R.id.buttonDisconnect) Button buttonDisconnect;
    @Inject AccountController accountController;

    @Override
    protected void afterCreate() {

        MainApplication.getComponent().inject(this);

        RxView.clicks(buttonDisconnect).debounce(400, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                accountController.logout();
            }
        });

    }

    @Override
    protected int getLayout() {
        return R.layout.menu_main;
    }


}
