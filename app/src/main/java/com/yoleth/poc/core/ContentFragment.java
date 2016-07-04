package com.yoleth.poc.core;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Mael Kermann on 25/09/2015.
 *
 * Copyright Mael Kermann
 *
 */
abstract public class ContentFragment extends BaseFragment {

    /**
     * Le fragment en cours
     *
     */
    public static ContentFragment fragment = null;

    /**
     * Création du fragment
     * On enregistre une référence vers le fragment en cours
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragment        = this;
        view            = super.onCreateView(inflater, container, savedInstanceState);

        return view;

    }

    /**
     * Retourne le fragemnt de retour
     *
     * @return ContentFragment
     *
     */
    public ContentFragment getBackFragment() {
        return null;
    }

}