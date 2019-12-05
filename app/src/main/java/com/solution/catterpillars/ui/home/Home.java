package com.solution.catterpillars.ui.home;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.solution.catterpillars.BR;
import com.solution.catterpillars.R;
import com.solution.catterpillars.base.BaseActivity;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.data.remort.APIClient;
import com.solution.catterpillars.data.remort.APIService;
import com.solution.catterpillars.databinding.HomeBinding;
import com.solution.catterpillars.ui.home.content.HomeFragment;
import com.solution.catterpillars.ui.home.content.NavigationMenuCallBack;
import com.solution.catterpillars.ui.home.dashboard.DashboardFragment;
import com.solution.catterpillars.ui.home.dashboard.ShareRefralDialog;
import com.solution.catterpillars.ui.home.dashboard.model.DataList;
import com.solution.catterpillars.ui.home.model.List;
import com.solution.catterpillars.ui.home.setting.SettingFragment;
import com.solution.catterpillars.ui.home.setting.SettingViewModel;
import com.solution.catterpillars.ui.home.task.TaskFragment;
import com.solution.catterpillars.ui.home.task.model.AppList;
import com.solution.catterpillars.ui.home.task.model.ResultTask;
import com.solution.catterpillars.ui.lockScreen.LockScreen;
import com.solution.catterpillars.util.ApplicationConstant;
import com.solution.catterpillars.util.AppsFunction;
import com.solution.catterpillars.util.CustomAlertDialog;
import com.solution.catterpillars.util.CustomLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 06-Nov-18
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 * About : I am a mathematician
 * Quote : Only brain can make anything possible
 * Strength : Never give up
 */
public class Home extends BaseActivity<HomeBinding, HomeViewModel>
        implements NavigationView.OnNavigationItemSelectedListener, HomeNavigator,
        RewardedVideoAdListener {

    int exit_check = 0;
    public FragmentManager fragmentManager;
    public Fragment fragment;
    private boolean shuldVideoAdsShow;
    private RewardedVideoAd mRewardedVideoAd;
    public AppPreferencesService preferencesService;
    private ArrayList<List> list;
    private ArrayList<AppList> appList = new ArrayList<>();
    private HomeViewModel viewModel;
    private final static String TAG = Home.class.getSimpleName();
    private CustomLoader loader;
    private DataList mAffiliateLink;
    private InterstitialAd interstitial;
    boolean isPositiveBtnClick = false;
    boolean isOnResumeTrigger = false;
    boolean isOnStartTrigger = false;
    private String currentDate;
    public boolean isAppOpen = false;
    public boolean isTaskViewFragmentOpen = false;
    public boolean isOnTaskCompleted = false;
    private boolean isActivityOpen = false;
    public NavigationMenuCallBack mNavigationMenuCallBack;
    public ArrayList<Boolean> backPressedEventClickList;



    public static Intent newIntent(Context context) {
        return new Intent(context, Home.class);
    }

    /**
     * @param state Initialise any thing here before binding
     */
    /**
     * @param state Initialise any thing here before binding
     */
    @Override
    protected void initialization(@Nullable Bundle state) {
//        taskStatus = getIntent().getBooleanExtra("TaskStatus", false);
        viewModel = new HomeViewModel();
        backPressedEventClickList = new ArrayList<>();
    }

    /**
     * Do anything on onCreate after binding
     */
    @Override
    protected void init() {

        isAppOpen = getIntent().getBooleanExtra("isAppOpen", false);
        isTaskViewFragmentOpen = isAppOpen;
        viewModel = new HomeViewModel();
        shuldVideoAdsShow = false;
//        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713"); //test ads
        MobileAds.initialize(this, ApplicationConstant.INSTANCE.ADMOB_APP_ID);

        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(Home.this);

        //video ads is not loading here
//        loadRewardedVideoAd();

//        params = (AppBarLayout.LayoutParams) binding.toolbar.toolbar.getLayoutParams();
        loader = new CustomLoader(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        preferencesService = new AppPreferencesService(context);
        list = new ArrayList<>();

        initToolbar();

        this.fragmentManager = getSupportFragmentManager();
        binding.navView.setNavigationItemSelectedListener(this);
        binding.networkError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.networkError.setVisibility(View.GONE);
                // setDataToUI();
//                apisCheckTask();
            }
        });

        Date date = new Date();
        currentDate = new SimpleDateFormat("yyyyMMdd").format(date);


        //lock screen code

        LockScreen.getInstance().init(Home.this);
        LockScreen.getInstance().startServiceIfLockEnable();
        LockScreen.getInstance().callNightAlarmReceiver();

        setDataToUI();

        openDashboardInit();
//        apisCheckTask(); // replaced by openDashboardInit
//        apisCheckTask();
    }

    private void openDashboardInit() {
        AppPreferencesService pref = new AppPreferencesService(context);
        pref.setTaskStatus(true);

        fragment = DashboardFragment.newInstance();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, fragment, DashboardFragment.TAG)
                .commitAllowingStateLoss();
        final int random = new Random().nextInt(50);
        if (random%2 == 0) {
            showFbInterstitial(false);
        }else{
            showInterstitial(false);
        }
       // showInterstitial(false);
//        fragment = DashboardFragment.newInstance();
//        fragmentManager.beginTransaction()
//                .replace(R.id.frame, fragment, DashboardFragment.TAG)
//                .commitAllowingStateLoss();
//        showInterstitial(false);
    }


   /* private void openDashboardInit(){
        AppPreferencesService pref = new AppPreferencesService(context);
        pref.setTaskStatus(true);
        fragment = DashboardFragment.newInstance();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, fragment, DashboardFragment.TAG)
                .commitAllowingStateLoss();
//        showInterstitial(false);
    }*/


    //    private void createDrawerList(){
//        list.add(new List("Home", url));
//
//        //list.addAll(data.getList());
//        list.add(new List("About Us", "https://www.catterpillars.in/Website/AboutUs"));
//        // list.add(new List("Term & Conditions","No url"));
//        list.add(new List("Logout", "No url"));
//        addNewMenuItemDynamically(list);
//    }

    private void apisCheckTask() {
        loader.show();
        loader.setCancelable(false);
        loader.setCanceledOnTouchOutside(false);

        APIService apiService = APIClient.getConnect().create(APIService.class);
        Call<ResultTask> call = null;// = apiService.checkSheetDetails(module, "", "");

        String userID = preferencesService.getUserId();
        // userID = "9795130580";

        call = apiService.loadInstallationTask(userID);

        if (call != null) {
            call.enqueue(new retrofit2.Callback<ResultTask>() {
                @Override
                public void onResponse(@NonNull Call<ResultTask> call,
                                       @NonNull Response<ResultTask> response) {
                    ResultTask data = response.body();
                    if (data != null) {
                        if (data.getStatus().equals("1")) {
                            setTitle("Home");
                            ArrayList<AppList> acualDataList = new ArrayList<>(data.getAppList());

                            int taskSie = acualDataList.size();

                            int c = 0;
                            for (int i = 0; i < acualDataList.size(); i++) {
                                if (i > 0 && i % 3 == 0) {
                                    appList.add(null);
                                }
                                appList.add(acualDataList.get(i));
                            }

                            appList.add(null);

                            fragment = TaskFragment.newInstance(Home.this, null);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("AppList", appList);
                            bundle.putSerializable("Data", data);
                            bundle.putInt("TaskSize", taskSie);
                            fragment.setArguments(bundle);
//                            openFragment(TaskFragment.TAG);


                            fragmentManager.beginTransaction()
                                    .replace(R.id.frame, fragment, TaskFragment.TAG)
                                    .commitAllowingStateLoss();
                        } else if (data.getStatus().equals("0")) {
                            AppPreferencesService pref = new AppPreferencesService(context);
                            pref.setTaskStatus(true);

                            fragment = DashboardFragment.newInstance();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.frame, fragment, DashboardFragment.TAG)
                                    .commitAllowingStateLoss();
                            final int random = new Random().nextInt(50);
                            if (random%2 == 0) {
                                showFbInterstitial(false);
                            }else{
                                showInterstitial(false);
                            }
//                            openDashboard();
                        }

                        loader.dismiss();
                    } else {
                        loader.dismiss();
                        binding.networkError.setVisibility(View.VISIBLE);
                        new CustomAlertDialog(context, isActivityOpen).Error(getString(R.string.data_null_error));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResultTask> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    call.cancel();
                   /* if (t.getMessage().contains("No address associated with hostname")) {
                        new CustomAlertDialog(context).Error(getString(R.string.network_not_found));

                    } else {
                        new CustomAlertDialog(context).Error(getString(R.string.try_again));
                    }*/

                    if (t.getMessage() != null && !t.getMessage().isEmpty()) {
                        if (t.getMessage().contains("No address associated with hostname")) {
                            new CustomAlertDialog(context, isActivityOpen).Error(getString(R.string.network_not_found));

                        } else {
                            new CustomAlertDialog(context, isActivityOpen).Error(getString(R.string.try_again));
                        }
                    }/*else{
                        new CustomAlertDialog(context).Error(context.getString(R.string.try_again));
                    }*/
                    binding.networkError.setVisibility(View.VISIBLE);
                    loader.dismiss();
                }
            });
        }
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbarView.toolbar);
        setTitle("Home");
        binding.toolbarView.toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);

        binding.toolbarView.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        binding.toolbarView.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.toolbarView.nextBtn.setVisibility(View.GONE);
                HomeNavigator navigator = (HomeNavigator) activity;
                navigator.onActivityEvent("1");

                Log.e(TAG, "Task Done");
            }
        });

        binding.toolbarView.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareRefralDialog shareDialog = new ShareRefralDialog(context,5);
                shareDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.full_screen_dialog);
                shareDialog.show(fragmentManager, ShareRefralDialog.TAG);
            }
        });
    }

    public void nextBtnVisibility(int value) {
        binding.toolbarView.nextBtn.setVisibility(value);

    }


    public void shareButtonVisible(int value, DataList mAffiliateLink) {
        binding.toolbarView.shareBtn.setVisibility(value);
        if (mAffiliateLink != null) {
            this.mAffiliateLink = mAffiliateLink;
        }

    }

    public View networkErrorViewVisible(int value) {
        binding.networkErrorView.setVisibility(value);

        return binding.retryBtn;
    }

    private void setDataToUI() {
        View headerLayout = binding.navView.getHeaderView(0);
        TextView textView1 = (TextView) headerLayout.findViewById(R.id.text1);
        textView1.setText(preferencesService.getUName());
        TextView textView2 = (TextView) headerLayout.findViewById(R.id.text2);
        textView2.setText(preferencesService.getUMobile());
    }


    /**
     * @return R.layout.layout_file
     */
    @Override
    protected int getLayout() {
        return R.layout.home;
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
        setTitle(R.string.home);
    }

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    @Override
    public HomeViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * Injecting dependencies
     */
    @Override
    protected void injectDependencies() {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(true);
        if (preferencesService.getTaskStatus()) {
            int listSize = list.size() - 1;
            switch (menuItem.getItemId()) {
                case R.id.home:
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            binding.toolbarView.toolbar.setElevation(0);
                        }
                        if(mNavigationMenuCallBack!=null){
                            mNavigationMenuCallBack.navigationMenuClick();
                        }
                        openDashboard();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case R.id.membersDetails:
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            binding.toolbarView.toolbar.setElevation(getResources().getDimension(R.dimen.dp_4));
                        }
                        if(mNavigationMenuCallBack!=null){
                            mNavigationMenuCallBack.navigationMenuClick();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case R.id.aboutUs:
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            binding.toolbarView.toolbar.setElevation(getResources().getDimension(R.dimen.dp_4));
                        }
                        if(mNavigationMenuCallBack!=null){
                            mNavigationMenuCallBack.navigationMenuClick();
                        }
                        fragment = HomeFragment.newInstance("https://www.catterpillars.in/Website/AboutUs");
                        setTitle(menuItem.getTitle());
                        openFragment(fragment, HomeFragment.TAG);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case R.id.setting:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        binding.toolbarView.toolbar.setElevation(getResources().getDimension(R.dimen.dp_4));
                    }
                    if(mNavigationMenuCallBack!=null){
                        mNavigationMenuCallBack.navigationMenuClick();
                    }
                    fragment = SettingFragment.newInstance();
                    openFragment(fragment, SettingFragment.TAG);
                    break;

                case R.id.logout:
                    new CustomAlertDialog(context, isActivityOpen).Logout(getString(R.string.logout_message));
                    break;

            }
        } else {
            switch (menuItem.getItemId()) {
                case R.id.logout:
                    new CustomAlertDialog(context, isActivityOpen).Logout(getString(R.string.logout_message));
                    break;

                default:
                    showToast(R.string.complete_task_first);
            }
        }

        // Close the navigation drawer
        binding.drawerLayout.closeDrawers();
        return true;
    }

    public void onNewFragmentLoad() {
        if (!(fragment instanceof DashboardFragment)) {
            binding.toolbarView.shareBtn.setVisibility(View.GONE);
        }

        if (!(fragment instanceof HomeFragment)) {
            binding.toolbarView.timeLayout.setVisibility(View.GONE);
        }
    }

   /* @Override
    public void onBackPressed() {

        try {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                int count = fragmentManager.getBackStackEntryCount();
                if (count > 1) {
                    fragmentManager.popBackStack();
                } else if (count == 1) {
                    // showToast(R.string.pressBackToExit);
                    fragmentManager.popBackStack();
                } else {
                    if (exit_check == 0) {
                        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
                        exit_check = 1;
                        final int random = new Random().nextInt(50);
                        if (random%2 == 0) {
                            showFbInterstitial(true);
                        }else{
                            showInterstitial(true);
                        }
                    } else {
                        super.onBackPressed();
                    }

                }
                onNewFragmentLoad();
            }
        } catch (IllegalStateException ile) {

        }
    }
    public void onBackPressedForced(){
        try {
            int count = fragmentManager.getBackStackEntryCount();
            if (count > 1) {
                fragmentManager.popBackStack();
            } else if (count == 1) {
                // showToast(R.string.pressBackToExit);
                fragmentManager.popBackStack();
            } else {
                if (exit_check == 0) {
                    Toast.makeText(this, "Please click BACK again to exit",
                            Toast.LENGTH_SHORT).show();
                    exit_check = 1;
                    showInterstitial(true);
                } else {
                    super.onBackPressed();
                }
            }
        } catch (IllegalStateException ile) {

        }
    }*/


    @Override
    public void onBackPressed() {
        try {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                int count = fragmentManager.getBackStackEntryCount();
                if (count > 1) {
                    fragmentManager.popBackStack();
                } else if (count == 1) {
                    // showToast(R.string.pressBackToExit);
                    fragmentManager.popBackStack();
                } else {
                    if (exit_check == 0) {
                        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
                        exit_check = 1;
                        showInterstitial(true);
                    } else {
                        super.onBackPressed();
                    }

                }
                onNewFragmentLoad();
            }
        }
        catch (IllegalStateException ile) {
            backPressedEventClickList.add(true);
        }
    }

    public void onBackPressedForced(){
        try {
            int count = fragmentManager.getBackStackEntryCount();
            if (count > 1) {
                fragmentManager.popBackStack();
            } else if (count == 1) {
                // showToast(R.string.pressBackToExit);
                fragmentManager.popBackStack();
            } else {
                if (exit_check == 0) {
                    Toast.makeText(this, "Please click BACK again to exit",
                            Toast.LENGTH_SHORT).show();
                    exit_check = 1;
                    showInterstitial(true);
                } else {
                    super.onBackPressed();
                }
            }
        } catch (IllegalStateException ile) {
            backPressedEventClickList.add(true);
        }
    }

    public void onBackPressedOffline(){

        int j = backPressedEventClickList.size();
        for(int i = 0; i < j; i++){
            try {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                }

                int count = fragmentManager.getBackStackEntryCount();
                if (count > 1) {
                    fragmentManager.popBackStack();
                } else if (count == 1) {
                    // showToast(R.string.pressBackToExit);
                    fragmentManager.popBackStack();
                } else {
                    if (exit_check == 0) {
                        Toast.makeText(this, "Please click BACK again to exit",
                                Toast.LENGTH_SHORT).show();
                        exit_check = 1;
                        showInterstitial(true);
                    } else {
                        super.onBackPressed();
                    }

                }
                onNewFragmentLoad();

                backPressedEventClickList.remove(0);
            } catch (IllegalStateException ile) {
                backPressedEventClickList.add(true);
            }
        }
    }

    @Override
    protected void onDestroy() {
        mRewardedVideoAd.destroy(this);
        destroyFbInterstitialAd();
        isActivityOpen = false;
        super.onDestroy();
    }


    //--------------For Configuration changed-------------

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
//        toggle.onConfigurationChanged(newConfig);
    }

    void openDashboard() {
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        } else {
            shuldVideoAdsShow = true;
        }

        fragment = DashboardFragment.newInstance();
        openFragment(fragment, DashboardFragment.TAG);
    }

    @Override
    public void onActivityEvent(String... params) {
        if (params.length > 0 && params[0].equals("1")) {
            setTitle("Home");
            openDashboard();
        }
    }

    public void openFragment(Fragment fragment, String tag) {
        fragmentManager.beginTransaction()
                .replace(R.id.frame, fragment)
                .addToBackStack(tag)
                .commitAllowingStateLoss();

        onNewFragmentLoad();

        if (fragment instanceof DashboardFragment) {
            final int random = new Random().nextInt(50);
            if (random%2 == 0) {
                showFbInterstitial(false);
            }else{
                showInterstitial(false);
            }
            //showInterstitial(false);
        }
    }

    //-----------------video ads--------------------

    /**
     * for video ads
     */
    private void loadRewardedVideoAd() {
//        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
//                new AdRequest.Builder().build());     //test
        mRewardedVideoAd.loadAd(getString(R.string.adsVideoHome),
                new AdRequest.Builder().build());
    }

    @Override
    public void onRewarded(RewardItem reward) {
//        Toast.makeText(this, "onRewarded! currency: " + reward.getType() + "  amount: " +
//                reward.getAmount(), Toast.LENGTH_SHORT).show();
        // Reward the user.
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
//        Toast.makeText(this, "onRewardedVideoAdLeftApplication",
//                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
//        Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        Log.e(TAG, "video ads failed:" + errorCode);
//        Toast.makeText(this, "onRewardedVideoAdFailedToLoad:" + errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdLoaded() {
//        Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
        if (shuldVideoAdsShow && mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    @Override
    public void onRewardedVideoAdOpened() {
//        Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
//        Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoCompleted() {
//        Toast.makeText(this, "onRewardedVideoCompleted", Toast.LENGTH_SHORT).show();
    }

    //-------------fragment event calls-------------------------
    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (fragmentManager.findFragmentByTag(HomeFragment.TAG) instanceof HomeFragment) {
                HomeFragment hf = (HomeFragment) fragmentManager.findFragmentById(R.id.frame);
                if (hf != null) {
                    if (hf.onKeyDownHome(keyCode, event)) {
                        return true;
                    }
                }
            }

            if (fragment instanceof HomeFragment) {
                if (((HomeFragment) fragment).onKeyDownHome(keyCode, event)) {
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();

        if(!preferencesService.isLockScreenOn()) {
            if (isPositiveBtnClick && !isOnResumeTrigger) {
                isOnResumeTrigger = true;
                enableLockScreen();
            }
        }
    }


    @Override
    protected void onPause() {
        mRewardedVideoAd.pause(this);
        isOnResumeTrigger = false;
        super.onPause();
    }


    @Override
    protected void onStart() {
        super.onStart();
        isActivityOpen = true;
        if (!preferencesService.isLockScreenOn()) {
            if (!isPositiveBtnClick && !isOnStartTrigger) {
                isOnStartTrigger = true;
                enableLockScreen();
            }
        }
    }


    void enableLockScreen() {
        String lastDate = preferencesService.getDateLockEnable();

        if (Integer.parseInt(currentDate) > Integer.parseInt(lastDate)) {


            LockScreen.getInstance().init(context);
            if (!LockScreen.getInstance().isActive()) {

                new CustomAlertDialog(context, isActivityOpen).ErrorUpdateAppWithCallBack(0, "Cancel", "Enable", getString(R.string.enable_lock_msg), new CustomAlertDialog.DialogCallBack() {
                    @Override
                    public void onPositiveClick() {
                        apiStatus("1", "3", new callBackResponse() {
                            @Override
                            public void onSucess() {
                                LockScreen.getInstance().active();
                                isPositiveBtnClick = true;
                                //AppsFunction.loadLockScreenAds(context, new AppPreferencesService(Home.this).getUMobile());
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }

                    @Override
                    public void onNegativeClick() {
                        showWarnigAlert();
                        isPositiveBtnClick = false;
                    }
                });
            }
        }

    }

    void showWarnigAlert() {
        new CustomAlertDialog(context, isActivityOpen).EnableLockScreenWithCallBack(new CustomAlertDialog.DialogCallBack() {
            @Override
            public void onPositiveClick() {

                apiStatus("1", "3", new callBackResponse() {
                    @Override
                    public void onSucess() {
                        LockScreen.getInstance().active();
                        isPositiveBtnClick = true;
                       // AppsFunction.loadLockScreenAds(context, new AppPreferencesService(Home.this).getUMobile());
                    }

                    @Override
                    public void onError() {

                    }
                });

            }

            @Override
            public void onNegativeClick() {

                preferencesService.setDateLockEnable(currentDate);
                isPositiveBtnClick = false;
            }
        });
    }





    void apiStatus(String status, String type, callBackResponse mCallBackResponse) {
        loader.show();
        loader.setCancelable(false);
        loader.setCanceledOnTouchOutside(false);

        APIService apiService = APIClient.getConnect().create(APIService.class);
        Call<SettingViewModel> call = null;// = apiService.checkSheetDetails(module, "", "");


        call = apiService.updateStatus(preferencesService.getUMobile(), type, status);


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
                    /*if (t.getMessage().contains("No address associated with hostname")) {
                        new CustomAlertDialog(context).Error(getString(R.string.network_not_found));

                    } else {
                        new CustomAlertDialog(context).Error(getString(R.string.try_again));
                    }*/
                    if (t.getMessage() != null && !t.getMessage().isEmpty()) {
                        if (t.getMessage().contains("No address associated with hostname")) {
                            new CustomAlertDialog(context, isActivityOpen).Error(getString(R.string.network_not_found));

                        } else {
                            new CustomAlertDialog(context, isActivityOpen).Error(getString(R.string.try_again));
                        }
                    }/*else{
                        new CustomAlertDialog(context).Error(getString(R.string.try_again));
                    }*/
                }
            });
        }
    }


    private interface callBackResponse {
        void onSucess();

        void onError();
    }



}
