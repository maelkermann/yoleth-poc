package com.yoleth.poc.fragments.main;

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
public class MainFragment extends ContentFragment {

    @Override
    protected void afterCreate() {

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_main;
    }

}
