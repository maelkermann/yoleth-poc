package com.yoleth.poc.account;

import android.accounts.AccountManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by mael on 04/07/16.
 */
public class AccountAuthenticatorService extends Service {

    private static AccountAuthenticator AUTHENTICATOR = null;

    @Override
    public IBinder onBind(Intent intent) {
        return intent.getAction().equals(AccountManager.ACTION_AUTHENTICATOR_INTENT) ? getAuthenticator().getIBinder() : null;
    }

    private AccountAuthenticator getAuthenticator() {
        if (AUTHENTICATOR == null)
            AUTHENTICATOR = new AccountAuthenticator(this);
        return AUTHENTICATOR;
    }
}
