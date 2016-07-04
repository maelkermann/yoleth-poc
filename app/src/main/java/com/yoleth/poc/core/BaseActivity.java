package com.yoleth.poc.core;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.yoleth.poc.MainApplication;
import com.yoleth.poc.R;
import com.yoleth.poc.fragments.menu.MenuMainFragment;

import butterknife.ButterKnife;

/**
 * Created by Mael Kermann on 25/09/2015.
 *
 * Copyright Mael Kermann
 *
 */
abstract public class BaseActivity extends AppCompatActivity {

    protected FragmentManager fragmentManager;
    protected FragmentTransaction fragmentTransaction;

    protected DrawerLayout menu;
    protected FrameLayout menulayout;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected ActionBar actionBar;

    /**
     * La référence vers le layout XML a afficher
     *
     * @return      L'id du layout
     *
     */
    abstract protected int getLayout();

    /**
     * Méthode appelée aprés la création de l'activity
     *
     */
    abstract protected void afterCreate();

    abstract protected int getMainWrapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // On met le XML comme contenu
        setContentView(this.getLayout());

        ButterKnife.bind(this);

        // On créé les objets de getion des fragments
        fragmentManager = getSupportFragmentManager();

        // Initialise le menu principal
        this.initMainMenu();

        // Initialise l'action bar
        this.initActionBar();

        // On lance la méthode de post création
        this.afterCreate();

    }

    /**
     * Initiliase le menu principal
     *
     */
    protected void initMainMenu(){

        if ( !hasMenu() )
            return;

        menulayout  = (FrameLayout) findViewById(R.id.menuFragmentWrapper);
        menu        = (DrawerLayout) findViewById(R.id.menu);

        this.backToMainMenu();

    }

    /**
     * Initialise l'action bar
     *
     */
    protected void initActionBar(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if ( toolbar == null )
            return;

        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        if ( actionBar == null )
            return;

        // On ajoute les boutons de de menu
        this.addMenuButtons(toolbar);

    }

    private void addMenuButtons(Toolbar toolbar){

        if ( hasMenu() ) {

            mDrawerToggle = new ActionBarDrawerToggle(this, menu, toolbar, 0, 0) {
                @Override
                public void onDrawerClosed(View drawerView) {
                    invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
                }
            };
            menu.post(new Runnable() {
                @Override
                public void run() {
                    mDrawerToggle.syncState();
                }
            });
            menu.addDrawerListener(mDrawerToggle);

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);

        }else{

            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);

        }

    }

    public void setTitle(int id){
        if ( this.actionBar != null && id != 0 )
            this.actionBar.setTitle(getString(id));
    }

    /**
     * Remplace le fragment en cours par un nouveau
     * On ferme aussi le menu
     *
     * @param fragment      Le fragment a afficher
     *
     */
    public void setContent(Fragment fragment){

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(getMainWrapper(), fragment);
        fragmentTransaction.commit();
        this.closeDrawer();

    }

    /**
     * Gére le click sur le bouton menu de l'action bar
     *
     * On affiche ou cache le menu slidant si on a un menu
     *
     */
    public void closeDrawer(){
        if ( this.menu != null && this.menulayout != null ){
            if ( menu.isDrawerOpen(menulayout)) {
                menu.closeDrawers();
                this.menuSwitch(new MenuMainFragment());
            }
        }
    }


    /**
     * Change le fragment du menu principal
     *
     * @param fragment      Le fragment a afficher
     *
     */
    public void menuSwitch(Fragment fragment, boolean left) {

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.menuFragmentWrapper, fragment);
        fragmentTransaction.commit();

    }

    /**
     * Change le fragment du menu principal
     *
     * @param fragment      Le fragment a afficher
     *
     */
    public void menuSwitch(Fragment fragment) {
        this.menuSwitch(fragment, false);
    }

    /**
     * Retourne au menu home
     *
     */
    public void backToMainMenu() {
        this.menuSwitch(new MenuMainFragment(), true);
    }

    /**
     * Est ce que l'activity a un menu ?
     *
     */
    protected boolean hasMenu(){
        return true;
    }


    /**
     * Lance une activité
     *
     * @param classe => la classe de l'acivité é lancer
     * @param finish => Est ce qu'on arréte l'activité en cours
     * @param bundle => Les infos qu'on envoi é la nouvelle activity
     *
     */
    public void startActivity(Class classe, boolean finish, Bundle bundle){

        Intent intent = new Intent(this, classe);
        if ( bundle != null )
            intent.putExtras(bundle);

        this.startActivity(intent);
        this.manageTransition();

        if ( finish )
            this.finish();

    }

    /**
     * Lance une activité
     *
     * @param classe => la classe de l'acivité é lancer
     * @param finish => Est ce qu'on arréte l'activité en cours
     *
     */
    public void startActivity(Class classe, boolean finish){
        this.startActivity(classe, finish, null);
    }

    /**
     * Lance une activité et termine l'activity en cours
     *
     * @param classe => la classe de l'acivité é lancer
     *
     */
    public void startActivity(Class classe){
        this.startActivity(classe, true);
    }

    /**
     * Lance une activité et termine l'activity en cours
     *
     * Ajoute un delai avant de lancer la nouvelle activity, pour un activity_splash screen par exemple
     *
     * @param classe => la classe de l'acivité é lancer
     *
     */
    public void startActivityWithDelay(final Class classe, int delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(classe);
            }
        }, delay);
    }

    /**
     * Lance une activité et termine l'activity en cours
     *
     * @param classe => la classe de l'acivité é lancer
     * @param bundle => Les infos qu'on envoi é la nouvelle activity
     *
     */
    public void startActivity(Class classe, Bundle bundle){
        this.startActivity(classe, true, bundle);
    }

    /**
     * Affiche la transition entre les activitys
     *
     * On peut utiliser la méthode setTransition pour affiche celle qu'on veut
     *
     */
    public void manageTransition() {
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * Gestion du bouton retour
     *
     */
    @Override
    public void onBackPressed(){

        if ( ContentFragment.fragment == null )
            super.onBackPressed();

        ContentFragment backFragment = ContentFragment.fragment.getBackFragment();
        if ( backFragment != null )
            this.setContent(backFragment);
        else
            super.onBackPressed();

    }

}
