package com.solution.catterpillars.ui.splash;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.solution.catterpillars.BuildConfig;
import com.solution.catterpillars.R;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.data.remort.APIClient;
import com.solution.catterpillars.data.remort.APIService;
import com.solution.catterpillars.ui.home.Home;
import com.solution.catterpillars.ui.location.LocationActivity;
import com.solution.catterpillars.ui.login.Login;
import com.solution.catterpillars.ui.login.model.ResultLogin;
import com.solution.catterpillars.util.ApplicationConstant;
import com.solution.catterpillars.util.AppsFunction;
import com.solution.catterpillars.util.CustomAlertDialog;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.solution.catterpillars.util.ApplicationConstant.APP_PACKAGE_NAME;

public class SplashActivity extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
    public static final int REQUEST_READ_PHONE_STATE = 10;
    private static final int NUM_PAGES = 3;
    private static final String TAG = SplashActivity.class.getSimpleName();
    private WelcomeViewModel splashViewModel;
    private AppPreferencesService preferencesService;
    private boolean isVersionOk = false;
    LinearLayout loaderView;
    private boolean isActivityOpen;
    com.facebook.ads.AdView fbAdView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        preferencesService = new AppPreferencesService(this);
        loaderView = findViewById(R.id.loaderView);
        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, ApplicationConstant.INSTANCE.ADMOB_APP_ID);
//        adView = new AdView(this);
//        adView.setAdSize(AdSize.BANNER);
//        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        AdView adView = findViewById(R.id.adView);
        adView.loadAd(new AdRequest.Builder().build());


        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
        final int random = new Random().nextInt(50);
        if (random % 2 == 0) {
            AdView adView2 = new AdView(this);
            adView2.setAdSize(com.google.android.gms.ads.AdSize.LARGE_BANNER);
            adView2.setAdUnitId(getString(R.string.adsBannerSplash));
            adContainer.addView(adView2);
            adView2.loadAd(new AdRequest.Builder().build());

        } else {

            //Fb banner Ads
            if (BuildConfig.DEBUG) {
                AdSettings.addTestDevice("0e7cac3c-373a-435c-babc-e3955a6d95a7");
            }
            fbAdView = new com.facebook.ads.AdView(SplashActivity.this, ApplicationConstant.INSTANCE.FB_ADS_PLACEMENT_ID, com.facebook.ads.AdSize.BANNER_HEIGHT_90);

            adContainer.addView(fbAdView);

            fbAdView.loadAd();

        }


        apis();


        /* new Handler().postDelayed(new Runnable() {

         *//*
         * Showing splash screen with a timer. This will be useful when you
         * want to show case your app logo / company
         *//*

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if (isVersionOk) {
                    goToApp();
                } else {
                    goToVersionUpdate();
                }
            }
        }, SPLASH_TIME_OUT);*/

//        shKey();
    }

    /*private void shKey(){
        try {

            PackageInfo info =
                    getPackageManager().getPackageInfo("com.solution.catterpillars",
                            PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("MY KEY HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }*/

    public void openLoginActivity() {
      /*  Intent intent = Login.newIntent(SplashActivity.this);
        startActivity(intent);
        finish();*/
        startActivity(new Intent(SplashActivity.this, StartAppActivity.class));
        finish();
    }

    public void openHomeActivity(String userId, String password, boolean taskStatus) {
        if (!preferencesService.isLockScreenDataAvailable()) {
            AppsFunction.loadLockScreenAds(getApplication(), userId);
        }

        if (!preferencesService.isLocationCapture()) {
            Intent intent = LocationActivity.newIntent(SplashActivity.this);
            startActivity(intent);
            finish();
        } else {
            Intent intent = Home.newIntent(SplashActivity.this);
            intent.putExtra("UserId", userId);
            intent.putExtra("Password", password);
            intent.putExtra("TaskStatus", taskStatus);
            intent.putExtra("isAppOpen", true);
            startActivity(intent);
            finish();
        }
    }

    private void goToApp() {
        if (!preferencesService.getLauncher()) {
            Intent intent = WelcomeActivity.newIntent(SplashActivity.this);
            startActivity(intent);
            finish();
        } else {
            if (preferencesService.isSessionActive()) {
                String userId = preferencesService.getUserId();
                String pass = preferencesService.getPassword();
                boolean taskStatus = preferencesService.getTaskStatus();
                openHomeActivity(userId, pass, taskStatus);
            } else {
                openLoginActivity();
            }
        }
    }

    private void goToVersionUpdate() {

        try {
            startActivityForResult(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + APP_PACKAGE_NAME)), 41);
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivityForResult(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" +
                            APP_PACKAGE_NAME)), 41);
        }

        // finish();
    }


    private void apis() {

        String version = "0";

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        APIService apiService = APIClient.getConnect().create(APIService.class);
        Call<ResultLogin> call = null;// = apiService.checkSheetDetails(module, "", "");

        call = apiService.versionCheck(version);

        if (call != null) {
            call.enqueue(new Callback<ResultLogin>() {
                @Override
                public void onResponse(@NonNull Call<ResultLogin> call, @NonNull
                        Response<ResultLogin> response) {
                    ResultLogin data = response.body();
                    if (data != null) {
                        if (data.getStatus().equals("1")) {
                            isVersionOk = true;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                     goToApp();
                                }
                            }, SPLASH_TIME_OUT);

                        } else {
                            isVersionOk = false;
                            new CustomAlertDialog(SplashActivity.this, isActivityOpen).ErrorUpdateAppWithCallBack(getString(R.string.update_available), new CustomAlertDialog.DialogCallBack() {
                                @Override
                                public void onPositiveClick() {
                                    goToVersionUpdate();
                                }

                                @Override
                                public void onNegativeClick() {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        finishAffinity();
                                    } else {
                                        finish();
                                    }
                                }
                            });
                        }
                    } else {
                        new CustomAlertDialog(SplashActivity.this, isActivityOpen).ErrorWithFinsh(getString(R.string.data_null_error));
                    }
                    loaderView.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(@NonNull Call<ResultLogin> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    call.cancel();
                   /* if (t.getMessage().contains("No address associated with hostname")) {
                        new CustomAlertDialog(SplashActivity.this,isActivityOpen).ErrorWithFinsh(getString(R.string.network_not_found));
                    } else {
                        new CustomAlertDialog(SplashActivity.this,isActivityOpen).ErrorWithFinsh(getString(R.string.try_again));
                    }*/
                    if (t.getMessage() != null && !t.getMessage().isEmpty()) {
                        if (t.getMessage().contains("No address associated with hostname")) {
                            new CustomAlertDialog(SplashActivity.this, isActivityOpen).ErrorWithFinsh(SplashActivity.this.getString(R.string.network_not_found));

                        } else {
                            new CustomAlertDialog(SplashActivity.this, isActivityOpen).ErrorWithFinsh(SplashActivity.this.getString(R.string.try_again));
                        }
                    }/*else{
                        new CustomAlertDialog(context,isActivityOpen).ErrorWithFinsh(context.getString(R.string.try_again));
                    }*/
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 0 && requestCode == 41) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (fbAdView != null) {
            fbAdView.destroy();
        }
        super.onDestroy();
        isActivityOpen = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActivityOpen = true;
    }

}
