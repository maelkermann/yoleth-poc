package com.yoleth.poc.fragments.menu;

import android.view.View;

import com.yoleth.poc.R;
import com.yoleth.poc.core.BaseFragment;

/**
 * Created by Mael Kermann on 25/09/2015.
 *
 * Copyright Mael Kermann
 *
 */
public class MenuMainFragment extends BaseFragment {

    View buttonDisconnect, menuMainMessages, menuMainSendedBets;

    @Override
    protected void afterCreate() {


    }

    @Override
    protected int getLayout() {
        return R.layout.menu_main;
    }


}
