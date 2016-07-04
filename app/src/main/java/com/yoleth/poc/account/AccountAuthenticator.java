package com.yoleth.poc.account;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.yoleth.poc.MainApplication;
import com.yoleth.poc.R;
import com.yoleth.poc.activities.LoginActivity;
import com.yoleth.poc.models.response.Tokens;
import com.yoleth.poc.network.Api;

import javax.inject.Inject;

/**
 * Created by mael on 04/07/16.
 */
public class AccountAuthenticator extends AbstractAccountAuthenticator {
    
    private final static String TAG = AccountAuthenticator.class.getSimpleName();

    private final Context mContext;
    @Inject Api api;

    public AccountAuthenticator(Context context) {
        super(context);
        this.mContext = context;
        MainApplication.getComponent().inject(this);
    }

    /*
     * The user has requested to add a new account to the system. We return an intent that will launch our login screen
     * if the user has not logged in yet, otherwise our activity will just pass the user's credentials on to the account
     * manager.
     */
    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) {
        Log.v(TAG, "addAccount()");
        final Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }


    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) {
        return null;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    // See /Applications/android-sdk-macosx/samples/android-18/legacy/SampleSyncAdapter/src/com/example/android/samplesync/authenticator/Authenticator.java
    // Also take a look here https://github.com/github/android/blob/d6ba3f9fe2d88967f56e9939d8df7547127416df/app/src/main/java/com/github/mobile/accounts/AccountAuthenticator.java
    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        Log.d(TAG, "getAuthToken() account="+account.name+ " type="+account.type);

        final Bundle bundle = new Bundle();

        // If the caller requested an authToken type we don't support, then
        // return an error
        if (!authTokenType.equals(AuthConstants.AUTHTOKEN_TYPE)) {
            Log.d(TAG, "invalid authTokenType" + authTokenType);
            bundle.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authTokenType");
            return bundle;
        }

        // Extract the username and password from the Account Manager, and ask
        // the server for an appropriate AuthToken
        final AccountManager accountManager = AccountManager.get(mContext);
        // Password is storing the refresh token
        final String password = accountManager.getPassword(account);
        if (password != null) {
            Log.i(TAG, "Trying to refresh access token");
            try {
                Tokens accessToken = api.tokens(mContext.getString(R.string.ws_header), "refresh_token", password).execute().body();
                if (accessToken!=null && !TextUtils.isEmpty(accessToken.getAccess_token())) {
                    bundle.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
                    bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, AuthConstants.ACCOUNT_TYPE);
                    bundle.putString(AccountManager.KEY_AUTHTOKEN, accessToken.getAccess_token());
                    accountManager.setPassword(account, accessToken.getRefresh_token());
                    return bundle;
                }
            } catch (Exception e) {
                Log.e(TAG, "Failed refreshing token.");
            }
        }

        // Otherwise... start the login intent
        Log.i(TAG, "Starting login activity");
        final Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return authTokenType.equals(AuthConstants.AUTHTOKEN_TYPE) ? authTokenType : null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        final Bundle result = new Bundle();
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
        return result;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) {
        return null;
    }
}
