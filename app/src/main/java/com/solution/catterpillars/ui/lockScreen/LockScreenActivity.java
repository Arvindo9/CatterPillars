package com.solution.catterpillars.ui.lockScreen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.solution.catterpillars.BR;
import com.solution.catterpillars.R;
import com.solution.catterpillars.base.BaseActivity;
import com.solution.catterpillars.base.BaseApplication;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.data.remort.APIClient;
import com.solution.catterpillars.data.remort.APIService;
import com.solution.catterpillars.databinding.ActivityLockScreenBinding;
import com.solution.catterpillars.ui.home.setting.SettingViewModel;
import com.solution.catterpillars.ui.lockScreen.model.LockScreenViewModel;
import com.solution.catterpillars.ui.lockScreen.model.NewsData;
import com.solution.catterpillars.ui.lockScreen.unlock.UnlockBar;
import com.solution.catterpillars.util.ApplicationConstant;
import com.solution.catterpillars.util.AppsFunction;
import com.solution.catterpillars.util.CustomAlertDialog;
import com.solution.catterpillars.util.CustomLoader;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 01-Nov-18
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 * About : I am a mathematician
 * Quote : Only brain can make anything possible
 * Strength : Never give up
 */
public class LockScreenActivity extends BaseActivity<ActivityLockScreenBinding, LockScreenViewModel>{

    private static final String TAG = LockScreenActivity.class.getSimpleName();
    private LockScreenViewModel viewModel;
    private AppPreferencesService preferences;
    private LockScreenNewsAdapter adapter;
    private AdView adView1;
    private AdView adView2;
    CustomLoader loader;
    boolean isActivityOpen=false;
    /**
     * @param state Initialise any thing here before binding
     */
    @Override
    protected void initialization(@Nullable Bundle state) {
    }

    /**
     * Do anything on onCreate after binding
     */
    @Override
    protected void init() {
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        MobileAds.initialize(this, ApplicationConstant.INSTANCE.ADMOB_APP_ID);
//        binding.adView1.loadAd(new AdRequest.Builder().build());
//        binding.adView2.loadAd(new AdRequest.Builder().build());

        UnlockBar unlock = (UnlockBar) findViewById(R.id.unlock);

        viewModel = new LockScreenViewModel();

        preferences = new AppPreferencesService(this);
        binding.lockAdsCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.lockAdsCheck.isChecked()){
                   showWarnigAlert();

                }
            }
        });

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float densityDpi = metrics.density;
        int width = metrics.widthPixels;

        int bothSideMargin = (int) (getResources().getDimension(R.dimen._6sdp)*2);
        adView1 = new AdView(context);
        adView1.setAdSize(new AdSize((int) ((width- bothSideMargin) / densityDpi), 210));
        adView1.setAdUnitId(getString(R.string.adsBannerSplash));
        adView1.loadAd(new AdRequest.Builder().build());
        binding.adViewCard1.addView(adView1);

        adView2 = new AdView(context);
        adView2.setAdSize(new AdSize((int) ((width- bothSideMargin) / densityDpi), 140));
        adView2.setAdUnitId(getString(R.string.adsBannerSplash));
        adView2.loadAd(new AdRequest.Builder().build());
        binding.adViewCard2.addView(adView2);
        //ads listener
        adView1.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                if(binding!=null && binding.adViewCard1!=null) {
                    binding.adViewCard1.setVisibility(View.VISIBLE);
                }
                Log.e(TAG, "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.e(TAG, "onAdFailedToLoad " + errorCode);

                if (errorCode == 3){
                    adView1 = new AdView(context);
                    adView1.setAdSize(AdSize.MEDIUM_RECTANGLE);
                    adView1.setAdUnitId(getString(R.string.adsBannerSplash));
                    adView1.loadAd(new AdRequest.Builder().build());
                    if(binding!=null && binding.adViewCard1!=null) {
                        binding.adViewCard1.addView(adView1);
                    }
                }
            }
        });

        adView2.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                if(binding!=null && binding.adViewCard2!=null) {
                    binding.adViewCard2.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.e(TAG, "onAdFailedToLoad " + errorCode);

                if (errorCode == 3){
                    adView2 = new AdView(context);
                    adView2.setAdSize(AdSize.MEDIUM_RECTANGLE);
                    adView2.setAdUnitId(getString(R.string.adsBannerSplash));
                    adView2.loadAd(new AdRequest.Builder().build());
                    if(binding!=null && binding.adViewCard2!=null) {
                        binding.adViewCard2.addView(adView2);
                    }
                }
            }
        });

        // Attach listener
        unlock.setOnUnlockListenerRight(this::finish);

        unlock.setOnUnlockListenerLeft(this::finish);

        setData();
    }

    private void setData() {
        if(preferences.getLockScreenData() != null){
            Gson gson = new Gson();
            viewModel = gson.fromJson(preferences.getLockScreenData(),
                    LockScreenViewModel.class);
            if (viewModel != null) {
                ArrayList<NewsData> list = new ArrayList<>(viewModel.getNewsList());

                binding.listViewNews.setHasFixedSize(true);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
                binding.listViewNews.setLayoutManager(mLayoutManager);

                if (list.size() > 1) {
                    viewModel.setMessage(list.get(0).getMessage());
                    viewModel.setPostDate(list.get(0).getPostDate());
                    viewModel.setTitle(list.get(0).getTitle());
                    viewModel.setImage(list.get(0).getImage());
                    viewModel.setUrl(list.get(0).getUrl());
                    viewModel.setKey(list.get(0).getKey());

                    binding.setData(viewModel);

                    list.remove(0);
                }
                adapter = new LockScreenNewsAdapter(context, LockScreenActivity.this, list);
                binding.listViewNews.setAdapter(adapter);

                adapter.notifyDataSetChanged();

                Log.e(TAG, "LockScreen active");
            }
            else{
                finish();
            }
        }
        else{
            finish();
            Log.e(TAG, "LockScreen not active");
        }
    }

    /**
     * @return R.layout.layout_file
     */
    @Override
    protected int getLayout() {
        return R.layout.activity_lock_screen;
    }

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    @Override
    public int getBindingVariable() {
        return BR.data;
    }

    /**
     * Set the title here or leave it blank
     */
    @Override
    protected void setTitle() {

    }

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    @Override
    public LockScreenViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.newsTop:
//                //TODO on news card click
                AppsFunction.launchNewApp(context, ApplicationConstant.UP50_PACKAGE_NAME,viewModel.getUrl(),
                        viewModel.getKey(), ApplicationConstant.INSTANCE.BASE_URL_UP_50 + "/" +
                                viewModel.getUrl());
                break;

            case R.id.viewAll:
//                //TODO on news viewAll click
                AppsFunction.launchNewApp(context,
                        ApplicationConstant.UP50_PACKAGE_NAME,viewModel.getCategoryName());
                break;
        }
    }

    /**
     * Injecting dependencies
     */
    @Override
    protected void injectDependencies() {

    }

    /*@Override
    public void onAttachedToWindow() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        super.onAttachedToWindow();
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        ((BaseApplication) getApplication()).lockScreenShow = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((BaseApplication) getApplication()).lockScreenShow = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActivityOpen=false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActivityOpen=true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

   /* @Override
    public void onBackPressed() {
        //do nothing
    }*/


    void showWarnigAlert() {
        new CustomAlertDialog(context,isActivityOpen).EnableLockScreenWithCallBack(new CustomAlertDialog.DialogCallBack() {
            @Override
            public void onPositiveClick() {
                binding.lockAdsCheck.setChecked(true);
            }

            @Override
            public void onNegativeClick() {
                apiStatus("0", "3", new callBackResponse() {
                    @Override
                    public void onSucess() {
                        LockScreen.getInstance().deactivate();
                        finish();
                        binding.lockAdsCheck.setChecked(false);
                    }

                    @Override
                    public void onError() {
                        if(binding.lockAdsCheck.isChecked()) {
                            binding.lockAdsCheck.setChecked(false);
                        }else{
                            binding.lockAdsCheck.setChecked(true);
                        }
                    }
                });

            }
        });
    }


    void apiStatus(String status, String type,callBackResponse mCallBackResponse){
        loader.show();
        loader.setCancelable(false);
        loader.setCanceledOnTouchOutside(false);

        APIService apiService = APIClient.getConnect().create(APIService.class);
        Call<SettingViewModel> call = null;// = apiService.checkSheetDetails(module, "", "");


        call = apiService.updateStatus(preferences.getUMobile(), type, status);


        if (call != null) {
            call.enqueue(new retrofit2.Callback<SettingViewModel>() {
                @Override
                public void onResponse(@NonNull Call<SettingViewModel> call, @NonNull
                        Response<SettingViewModel> response) {
                    SettingViewModel viewModel = response.body();
                    if (viewModel != null && viewModel.getStatus().equalsIgnoreCase("1")) {
                        //list.clear();
                        mCallBackResponse.onSucess();
                    } else {
                        mCallBackResponse.onError();

                    }

                    loader.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<SettingViewModel> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    call.cancel();
                    mCallBackResponse.onError();
                    loader.dismiss();
                   /* if (t.getMessage().contains("No address associated with hostname")) {
                        new CustomAlertDialog(context,isActivityOpen).Error(getString(R.string.network_not_found));

                    } else {
                        new CustomAlertDialog(context,isActivityOpen).Error(getString(R.string.try_again));
                    }*/

                    if(t.getMessage()!=null && !t.getMessage().isEmpty()) {
                        if (t.getMessage().contains("No address associated with hostname")) {
                            new CustomAlertDialog(context,isActivityOpen).Error(getString(R.string.network_not_found));

                        } else {
                            new CustomAlertDialog(context,isActivityOpen).Error(getString(R.string.try_again));
                        }
                    }/*else{
                        new CustomAlertDialog(context,isActivityOpen).Error(getString(R.string.try_again));
                    }*/
                }
            });
        }
    }


    private interface callBackResponse{
        void onSucess();
        void onError();
    }

}
