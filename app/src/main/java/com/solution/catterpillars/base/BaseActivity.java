package com.solution.catterpillars.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.AbstractAdListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.solution.catterpillars.BuildConfig;
import com.solution.catterpillars.R;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.data.local.prefs.PreferencesService;
import com.solution.catterpillars.util.ApplicationConstant;

/**
 * Created by Arvindo Mondal on 29-Oct-18.
 * Roundpay Techno Media OPC Pvt Ltd
 * I am a mathematician
 * arvindomondal@gmail.com
 * Only intelligence can make anything possible
 * Never give up
 */
public abstract class BaseActivity<B extends ViewDataBinding, V extends BaseViewModel>
        extends AppCompatActivity
        implements View.OnClickListener,
        BaseFragment.Callback {

    public static final int REQUEST_PERMISSIONS = 20;
    protected boolean REQUEST_PERMISSION_FOR_ACTIVITY = true;
    public B binding;
    private V viewModel;
    protected Context context;
    protected Activity activity;
    private SparseIntArray mErrorString;
    InterstitialAd interstitial;
    private com.facebook.ads.InterstitialAd interstitialAdFb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialization(savedInstanceState);
        context = this;
        activity = this;
        performDataBinding();
        setTitle();
        init();
        initInterstitial(false);
        initFbInterstitial(false);
        mErrorString = new SparseIntArray();
//        if(REQUEST_PERMISSION_FOR_ACTIVITY) {
//            checkAllPermission();
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (REQUEST_PERMISSION_FOR_ACTIVITY) {
            checkAllPermission();
        }
    }

    private void performDataBinding() {
        binding = DataBindingUtil.setContentView(this, getLayout());

        this.viewModel = viewModel == null ? getViewModel() : viewModel;
        binding.setVariable(getBindingVariable(), viewModel);
        binding.executePendingBindings();
    }

    /**
     * @param state Initialise any thing here before binding
     */
    protected abstract void initialization(@Nullable Bundle state);

    /**
     * Do anything on onCreate after binding
     */
    protected abstract void init();

    /**
     * @return R.layout.layout_file
     */
    @LayoutRes
    protected abstract int getLayout();

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    public abstract int getBindingVariable();

    /**
     * Set the title here or leave it blank
     */
    protected abstract void setTitle();

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    public abstract V getViewModel();

    @Override
    protected void onDestroy() {
        super.onDestroy();

        binding = null;
        viewModel = null;

        System.gc();
    }

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }

    @Override
    public abstract void onClick(View v);

    /**
     * Injecting dependencies
     */
    protected abstract void injectDependencies();

    public void showToast(int index) {
        Toast.makeText(context, getString(index), Toast.LENGTH_SHORT).show();
    }

    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void showToastLong(int index) {
        Toast.makeText(context, getString(index), Toast.LENGTH_LONG).show();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //-------------------Permission----------------------------

    public void checkAllPermission() {
        try {
            requestAppPermissions(new String[]{
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.GET_ACCOUNTS,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    R.string.str_ShowOnPermisstion,
                    REQUEST_PERMISSIONS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //permission already granted
//            onPermissionsGranted(requestCode);
//            createFolder();

            onPermissionGranted();
        } else {
            Snackbar mSnackBar = Snackbar.make(findViewById(android.R.id.content),
                    getString(R.string.str_ShowOnPermisstion),
                    Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            startActivity(intent);
                        }
                    });

            mSnackBar.setActionTextColor(getResources().getColor(R.color.snackBarActionColor));
            TextView mainTextView = (TextView) (mSnackBar.getView()).
                    findViewById(android.support.design.R.id.snackbar_text);
            mainTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.snackbar_textsize));
            mSnackBar.show();
        }
    }

    public void onPermissionGranted() {

    }

    public void requestAppPermissions(final String[] requestedPermissions,
                                      final int stringId, final int requestCode) {
        mErrorString.put(requestCode, stringId);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean shouldShowRequestPermissionRationale = false;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale) {
                Snackbar mSnackBar = Snackbar.make(findViewById(android.R.id.content), stringId,
                        Snackbar.LENGTH_INDEFINITE).setAction("GRANT",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(BaseActivity.this,
                                        requestedPermissions, requestCode);
                            }
                        });

                mSnackBar.setActionTextColor(getResources().getColor(R.color.snackBarActionColor));
                TextView mainTextView = (TextView) (mSnackBar.getView()).
                        findViewById(android.support.design.R.id.snackbar_text);
                mainTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.snackbar_textsize));
                mSnackBar.show();
            } else {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
            }
        } else {
//            createFolder();
            //onSuccess permission grant
//            onPermissionsGranted(requestCode);
        }
    }

    //-----------------------TaskActivity----------------------------
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public AdView setAds(int height, int margin) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float densityDpi = metrics.density;
        int width = metrics.widthPixels;

        AdView adView = new AdView(context);
        adView.setAdSize(new AdSize((int) ((width - margin) / densityDpi), height));
        adView.setAdUnitId(getString(R.string.adsBannerSplash));
        adView.loadAd(new AdRequest.Builder().build());
        return adView;
    }

    public void initInterstitial(boolean isAppClosed) {
        // Initialize the Mobile Ads SDK
        MobileAds.initialize(this, (ApplicationConstant.INSTANCE.ADMOB_APP_ID));
        AdRequest adIRequest = new AdRequest.Builder().build();
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(getString(R.string.adsInterstitial));
        interstitial.loadAd(adIRequest);

    }

    public void showInterstitial(boolean isAppClosed) {
        //  initInterstitial();
        if (interstitial != null) {
            if (interstitial.isLoaded()) {
                interstitial.show();
                if (isAppClosed) {
                    backPressed();
                }
            } else {
                initInterstitial(isAppClosed);
                interstitial.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        // Code to be executed when an ad finishes loading.
                        if (interstitial.isLoaded()) {
                            interstitial.show();
                            if (isAppClosed) {
                                backPressed();
                            }
                        }
                    }

                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // Code to be executed when an ad request fails.
                    }

                    @Override
                    public void onAdOpened() {
                        // Code to be executed when the ad is displayed.
                    }

                    @Override
                    public void onAdLeftApplication() {
                        // Code to be executed when the user has left the app.
                    }

                    @Override
                    public void onAdClosed() {
                        // Code to be executed when when the interstitial ad is closed.
                        initInterstitial(isAppClosed);
                        if (isAppClosed) {
                            backPressed();
                        }
                    }
                });
            }
        } else {
            initInterstitial(true);
        }

    }


    public void initFbInterstitial(boolean isAppClosed) {
        // Initialize the Mobile Ads SDK
        interstitialAdFb = new com.facebook.ads.InterstitialAd(this, ApplicationConstant.INSTANCE.FB_ADS_PLACEMENT_ID);
       /* interstitialAdFb.setAdListener(new AbstractAdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                super.onError(ad, adError);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                super.onAdLoaded(ad);
            }

            @Override
            public void onInterstitialDisplayed(Ad ad) {
                super.onInterstitialDisplayed(ad);
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                super.onInterstitialDismissed(ad);
            }
        });*/
        if (BuildConfig.DEBUG) {
            AdSettings.addTestDevice("0e7cac3c-373a-435c-babc-e3955a6d95a7");
        }
        interstitialAdFb.loadAd();

    }

    public void showFbInterstitial(boolean isAppClosed) {
        if (interstitialAdFb != null) {
            if (interstitialAdFb.isAdLoaded()) {
                interstitialAdFb.show();
                if (isAppClosed) {
                    backPressed();
                }
            } else {
                initFbInterstitial(isAppClosed);
                interstitialAdFb.setAdListener(new InterstitialAdListener() {
                    @Override
                    public void onInterstitialDisplayed(Ad ad) {

                    }

                    @Override
                    public void onInterstitialDismissed(Ad ad) {
                        initFbInterstitial(isAppClosed);
                        if (isAppClosed) {
                            backPressed();
                        }
                    }

                    @Override
                    public void onError(Ad ad, AdError adError) {

                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        if (interstitialAdFb.isAdLoaded()) {
                            interstitialAdFb.show();
                            if (isAppClosed) {
                                backPressed();
                            }
                        }
                    }

                    @Override
                    public void onAdClicked(Ad ad) {

                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {

                    }
                });
                if (BuildConfig.DEBUG) {
                    AdSettings.addTestDevice("0e7cac3c-373a-435c-babc-e3955a6d95a7");
                }
                interstitialAdFb.loadAd();
            }
        } else {
            initFbInterstitial(true);
        }

    }


    public void destroyFbInterstitialAd(){
        if(interstitialAdFb!=null){
            interstitialAdFb.destroy();
        }
    }


    private void backPressed() {
        finish();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        }
    }

}
