package com.solution.catterpillars.facebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.solution.catterpillars.R;

import java.util.Collections;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 12/12/2018
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 */
public class FacebookLogin extends AppCompatActivity implements FacebookNavigator{

    private static final String TAG = FacebookLogin.class.getSimpleName();
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";

    private static AccessToken accessToken = AccessToken.getCurrentAccessToken();
    private boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
    private AccessTokenTracker accessTokenTracker;
    private FacebookNavigator facebookNavigator;

    public static AccessToken getAccessToken(){
        return accessToken;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();

        facebookNavigator = this;

        if(!isLoggedIn) {
            loginToFacebook();
        }
        accessToken();


    }

    private void loginToFacebookUsingButton(){
        setContentView(R.layout.facebook_login);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        //        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

        loginButton.setReadPermissions(Collections.singletonList(EMAIL));
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    private void loginToFacebook(){
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Log.e(TAG, "Login success");
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });



//        LoginManager.getInstance().logInWithReadPermissions(this,
//                Arrays.asList("public_profile"));

        LoginManager.getInstance().logInWithReadPermissions(this,
                Collections.singletonList("public_profile"));
    }

    private void accessToken(){
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
                accessToken = currentAccessToken;
            }
        };
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();


        Utils.fd(accessToken);
        Utils.like(accessToken);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }




    @Override
    public void onSuccessAccessToken(AccessToken accessToken) {

    }

}
