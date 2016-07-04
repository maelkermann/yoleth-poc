package com.yoleth.poc.core;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yoleth.poc.MainApplication;

import butterknife.ButterKnife;

/**
 * Created by Mael Kermann on 25/09/2015.
 *
 * Copyright Mael Kermann
 *
 */
abstract public class BaseFragment extends Fragment {

    public static BaseFragment fragment = null;

    protected View view;
    protected BaseActivity activity;

    protected FragmentManager fragmentManager;
    protected FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view            = inflater.inflate(getLayout(), container, false);
        activity        = (BaseActivity) getActivity();
        fragmentManager = getChildFragmentManager();

        ButterKnife.bind(this, view);

        this.afterCreate();

        return view;

    }

    abstract protected void afterCreate();

    abstract protected int getLayout();

}