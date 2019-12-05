package com.solution.catterpillars.ui.home.content;


import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.solution.catterpillars.BR;
import com.solution.catterpillars.R;
import com.solution.catterpillars.base.BaseFragment;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.data.remort.APIClient;
import com.solution.catterpillars.data.remort.APIService;
import com.solution.catterpillars.databinding.HomeFragmentBinding;
import com.solution.catterpillars.ui.home.Home;
import com.solution.catterpillars.ui.home.dashboard.task.task_detail.TaskDetailFragment;
import com.solution.catterpillars.ui.home.dashboard.task.task_detail.TaskScreenNavigator;
import com.solution.catterpillars.ui.home.model.List;
import com.solution.catterpillars.util.CustomLoader;
import com.solution.catterpillars.youtube_subscription.YouTubeAuthActivityCallBack;
import com.solution.catterpillars.youtube_subscription.YouTubeSubscribe;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.solution.catterpillars.ui.home.dashboard.task.task_detail.TaskScreen.ADS_VIEW;
import static com.solution.catterpillars.ui.home.dashboard.task.task_detail.TaskScreen.FACEBOOK_LIKE;
import static com.solution.catterpillars.ui.home.dashboard.task.task_detail.TaskScreen.TASK_CANCEL;
import static com.solution.catterpillars.ui.home.dashboard.task.task_detail.TaskScreen.TASK_COMPLETED;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 10-Nov-18
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 * About : I am a mathematician
 * Quote : Only brain can make anything possible
 * Strength : Never give up
 */
public class HomeFragment extends BaseFragment<HomeFragmentBinding, HomeFragmentViewModel>
        implements EasyPermissions.PermissionCallbacks, YouTubeAuthActivityCallBack,
        HomeFragmentNavigator ,NavigationMenuCallBack{

    public static final String TAG = HomeFragment.class.getSimpleName();
    private AppPreferencesService preferencesService;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private RewardedVideoAd mRewardedVideoAd;
    private static final String DATA_KEY = "keyData";
    private static final String WEB_URL = "web_url";
    private HomeFragmentViewModel viewModel;
    private List data;
    private CustomLoader loader;
    private YouTubeSubscribe mYouTubeSubscribe;
    private WebView newOpenView;
    //private int count;
    boolean isErrorTaskCompleted =false;
    boolean isFragmentOpen = false;
    private String facebookShareParam = "";
    private Home activityHome;
    private long time = 0;
    boolean isTimerStart = false;
    boolean isTimerRunnig = false;
    private java.util.List<String> urlList;
    private boolean isTaskRunning = false;

    private String channelId = "UCMqVnnpvc1S1oeC_teoa67Q";
    private Fragment fragmentCalls;
    private TaskScreenNavigator taskScreenNavigator;
    private CountDownTimer countDownTimer;
    private String title = "";
    private int TASK_TYPE = 0;

    private boolean isPageLikeClicked = false;
    private boolean isPageLikeSucess = false;
    private boolean isPostLikeSucess = false;
    private boolean isPostLikeClicked = false;
    private boolean isAdsViewClicked = false;
    private boolean isSuccessfulAdsFinish = false;

    public static HomeFragment newInstance(String url) {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        args.putString(WEB_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    public void setTaskViewFragment(Context context, Fragment fragment, String title, long time,
                                    java.util.List<String> urlList) {
        this.fragmentCalls = fragment;
        this.taskScreenNavigator = (TaskScreenNavigator) fragment;
        this.activityHome = (Home) context;
        this.time = time;
        this.urlList = urlList;
        this.activityHome.binding.toolbarView.timeLayout.setVisibility(View.VISIBLE);
        this.activityHome.binding.toolbarView.time.setText(R.string.timer);
        isTaskRunning = true;
        this.title = title;
        TASK_TYPE = ADS_VIEW;
        isAdsViewClicked = false;
        isSuccessfulAdsFinish = false;
    }

    public void setFragmentTitle(String title) {

        this.title = title;

    }

    public void setTaskViewFragmentFbLike(Context context, Fragment fragment, String title) {
        this.fragmentCalls = fragment;
        this.taskScreenNavigator = (TaskScreenNavigator) fragment;
        this.activityHome = (Home)
                context;
        this.title = title;
        TASK_TYPE = FACEBOOK_LIKE;
        activityHome.binding.toolbarView.taskCompleateddBtn.setVisibility(View.GONE);
        this.time = 50000;//50 sec
    }

   /* public void setTaskViewFragmentFbLike(Context context, Fragment fragment, String title) {
        this.fragmentCalls = fragment;
        this.taskScreenNavigator = (TaskScreenNavigator) fragment;
        this.activityHome = (Home) context;
        this.title = title;
        TASK_TYPE = FACEBOOK_LIKE;

    }*/

    private void showTimer() {
        long firstUrlTime = 10000;  //10 sec
        final long updateTime = (time - firstUrlTime) / urlList.size() - 1;
        final long[] changeTime = {time - updateTime};
        final int[] listSize = {1};
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(time, 1000) {

                public void onTick(long millisUntilFinished) {
                    isTimerRunnig=true;
                    String text = String.format(Locale.getDefault(), "%02d : %02d",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);

                    if(activityHome!=null &&activityHome.binding!=null &&activityHome.binding.toolbarView!=null ) {
                        activityHome.binding.toolbarView.time.setText(text);
                    }
                    //here you can have your logic to set text to edittext

                    if (millisUntilFinished < changeTime[0] && listSize[0] < urlList.size() &&
                            !isAdsViewClicked) {
                        binding.webView.loadUrl(urlList.get(listSize[0]++));
                        changeTime[0] = changeTime[0] - updateTime;
                    }
                }

                public void onFinish() {
                    isSuccessfulAdsFinish = true;
                    isTimerRunnig=false;
                    if (!isAdsViewClicked) {
                        onTaskComplete();
                    } else {
                        if(activityHome!=null && activityHome.binding!=null && activityHome.binding.toolbarView!=null) {
                            activityHome.binding.toolbarView.taskCompleateddBtn.setVisibility(View.VISIBLE);
                            activityHome.binding.toolbarView.timeLayout.setVisibility(View.GONE);

                            activityHome.binding.toolbarView.taskCompleateddBtn
                                    .setOnClickListener(v -> {
                                        onTaskComplete();
                                    });
                            activityHome.showToast(R.string.now_you_can_go_back);
                        }
                    }
                }
            };

            countDownTimer.start();
        }
    }


    private void showTimerFbLike() {
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(time, 1000) {

                public void onTick(long millisUntilFinished) {
                    isTimerRunnig=true;
                    String text = String.format(Locale.getDefault(), "%02d : %02d",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                }

                public void onFinish() {
                    isTimerRunnig=false;
                    activityHome.binding.toolbarView.taskCompleateddBtn.setVisibility(View.VISIBLE);
                    activityHome.binding.toolbarView.taskCompleateddBtn
                            .setOnClickListener(v -> {
                                onFacebookLikeSuccess();

                            });
                }
            };

            countDownTimer.start();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!activityHome.backPressedEventClickList.isEmpty()){
            activityHome.onBackPressedOffline();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        isFragmentOpen = true;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        isFragmentOpen = false;
        try {
            activityHome.binding.toolbarView.taskCompleateddBtn.setVisibility(View.GONE);
        } catch (NullPointerException npe) {

        }
    }

    private void onTaskComplete() {

       try{
           isTaskRunning = false;
            activityHome.binding.toolbarView.timeLayout.setVisibility(View.GONE);
            activityHome.binding.toolbarView.taskCompleateddBtn.setVisibility(View.GONE);
            activityHome.binding.toolbarView.time.setText(getString(R.string.timer));
            activityHome.onBackPressed();

            if (taskScreenNavigator != null) {
                /* isSuccessfulAdsFinish = false;*/
                taskScreenNavigator.onFragmentResume(TASK_COMPLETED);
            }
        }catch (IllegalStateException ile){

       }
    }

    private void onTaskCancel() {
        isTaskRunning = false;
       /* activityHome.onBackPressed();*/
        if(activityHome.binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            activityHome.onBackPressed();
            activityHome.onBackPressedForced();
        }
        else{
            activityHome.onBackPressed();
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (taskScreenNavigator != null) {
            taskScreenNavigator.onFragmentResume(TASK_CANCEL);
        }
        activityHome.binding.toolbarView.timeLayout.setVisibility(View.GONE);
        activityHome.binding.toolbarView.time.setText(getResources().getText(R.string.timer));
    }

    private void loadAdsViewUrl() {
        binding.webView.loadUrl(urlList.get(0));
    }

    /**
     * @return R.layout.layout_file
     */
    @Override
    public int getLayout() {
        return R.layout.home_fragment;
    }

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    @Override
    public HomeFragmentViewModel getViewModel() {
        return viewModel;
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

    @Override
    public int setTitle() {
        return R.string.about_us;
    }

    /**
     * @return String value null or "" then this title will not show otherwise it will show
     */
    @Override
    protected String setTitleString() {
        return title;
    }

    private void onFacebookLikeSuccess() {
        /*activityHome.onBackPressed();*/
        if(activityHome.binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            activityHome.onBackPressed();
            activityHome.onBackPressedForced();
        }
        else{
            activityHome.onBackPressed();
        }
        if (taskScreenNavigator != null) {
            taskScreenNavigator.onFragmentResume(TASK_COMPLETED);
        }
        if(countDownTimer!=null){
            countDownTimer.cancel();
        }
        activityHome.binding.toolbarView.taskCompleateddBtn.setVisibility(View.GONE);
    }

    private void onFacebookLikeFailed() {
       /* activityHome.onBackPressed();*/
        if(activityHome.binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            activityHome.onBackPressed();
            activityHome.onBackPressedForced();
        }
        else{
            activityHome.onBackPressed();
        }
        if (taskScreenNavigator != null) {
            taskScreenNavigator.onFragmentResume(TASK_CANCEL);

        }

        if(countDownTimer!=null){
            countDownTimer.cancel();
        }
        activityHome.binding.toolbarView.taskCompleateddBtn.setVisibility(View.GONE);
    }

    private void facebookLike(String url) {
        binding.likeButtonView.setVisibility(View.GONE);
        //page like unlike check
        if (url.contains("https://m.facebook.com/a/profile.php?fan&id=")
                && url.contains("&origin=page_profile&pageSuggestionsOnLiking=1")) {
            //page
            isPageLikeSucess = false;
            isPostLikeSucess = false;
            activity.showToast("User Like Click");
        } else if (url.contains("https://m.facebook.com/a/profile.php?fan&id=")
                && url.contains("&origin=search&gfid=")) {
            //page
            isPageLikeSucess = false;
            isPostLikeSucess = false;
            activity.showToast("User Like Click");
        } else if (url.contains("?fc=f&showPageSuggestions&_rdr")) {
            //page
            isPageLikeSucess = true;
            isPostLikeSucess = false;
            activity.showToast("Like Sucess");
        } else if (url.contains("https://m.facebook.com/a/profile.php?unfan&id=")) {
            //page
            isPageLikeSucess = false;
            isPostLikeSucess = false;
            activity.showToast("User Unliked Click");
        } else if (url.contains("?fc=u&_rdr")) {
            //page
            isPageLikeSucess = false;
            isPostLikeSucess = false;
            activity.showToast("Unliked Success");
        }

        // post like unlike check
        else if (url.contains("https://m.facebook.com/a/like.php?")) {
            //post
            isPageLikeSucess = false;
            isPostLikeSucess = false;
            isPostLikeClicked = true;
            activity.showToast("User Post liked Click");
        } else if (isPostLikeClicked && url.contains("https://m.facebook.com/story.php?story_fbid=")) {
            //post
            isPostLikeClicked = false;
            isPageLikeSucess = false;
            isPostLikeSucess = true;
            activity.showToast("Post Liked  Sucess");
        } else if (!isPostLikeClicked && url.contains("https://m.facebook.com/story.php?story_fbid=")) {
            //post
            isPostLikeClicked = false;
            isPageLikeSucess = false;
            isPostLikeSucess = false;
            activity.showToast("Post Unliked  Sucess");
        } else if (url.contains("https://m.facebook.com/reactions/picker/?ft_id=")) {
            //post
            isPageLikeSucess = false;
            isPostLikeSucess = false;
            activity.showToast("Post Like Emoji pannel open");
        } else if (url.contains("?lul&_ft_=mf_story_key")) {
            //post
            isPageLikeSucess = false;
            isPostLikeSucess = false;
            activity.showToast("Unliked Post Click");
        } else if (isPostLikeClicked && url.contains("https://m.facebook.com/groups/")) {
            //post
            isPostLikeClicked = false;
            isPageLikeSucess = false;
            isPostLikeSucess = true;
            activity.showToast("Liked Post Sucess");
        } else if (!isPostLikeClicked && url.contains("https://m.facebook.com/groups/")) {
            //post
            isPostLikeClicked = false;
            isPageLikeSucess = false;
            isPostLikeSucess = false;
            activity.showToast("Unliked Post Sucess");
        } else if (url.contains("https://m.facebook.com/ufi/reaction/?ft_ent_identifier=")) {
            //post
            isPageLikeSucess = false;
            isPostLikeSucess = false;
            activity.showToast("Unliked Post Click");
        } else if (url.contains("https://m.facebook.com/stories.php?aftercursorr=")) {
            //post
            isPageLikeSucess = false;
            isPostLikeSucess = true;
            activity.showToast("Post Liked Sucess");
        } else {
//            binding.likeButtonView.setVisibility(View.VISIBLE);
        }

        if (isPageLikeSucess && !isPostLikeSucess) {
            isPageLikeSucess = false;
            isPostLikeSucess = false;

            onFacebookLikeSuccess();
        } else if (isPostLikeSucess && !isPageLikeSucess) {
            isPageLikeSucess = false;
            isPostLikeSucess = false;

            onFacebookLikeSuccess();
        }
    }

    private void facebookLike1(String url) {
        //page like unlike check
        if (url.contains("https://m.facebook.com/a/profile.php?fan&id=")
                && url.contains("&origin=page_profile&pageSuggestionsOnLiking=1")) {
            //page
            isPageLikeClicked = true;
            activity.showToast("User Like Click");
        } else if (url.contains("https://m.facebook.com/a/profile.php?fan&id=")
                && url.contains("&origin=search&gfid=")) {
            //page
            isPageLikeClicked = true;
            activity.showToast("User Like Click");
        } else if (isPageLikeClicked && url.contains("?fc=f&showPageSuggestions&_rdr")) {
            //page
            isPageLikeClicked = false;
            activity.showToast("Like Sucess");
        } else if (url.contains("https://m.facebook.com/a/profile.php?unfan&id=")) {
            //page
            activity.showToast("User Unliked Click");
        } else if (url.contains("?fc=u&_rdr")) {
            //page
            activity.showToast("Unliked Success");
        }

        // post like unlike check
        else if (url.contains("https://m.facebook.com/a/like.php?")) {
            //post
            activity.showToast("User Post liked Click");
        } else if (url.contains("https://m.facebook.com/story.php?story_fbid=")) {
            //post
            activity.showToast("Post Liked & Unliked Sucess");
        } else if (url.contains("https://m.facebook.com/reactions/picker/?ft_id=")) {
            //post
            activity.showToast("Post Like Emoji pannel open");
        } else if (url.contains("?lul&_ft_=mf_story_key")) {
            //post
            activity.showToast("Unliked Post");
        } else if (url.contains("https://m.facebook.com/groups/")) {
            //post
            activity.showToast("Liked Post");
        } else if (url.contains("https://m.facebook.com/ufi/reaction/?ft_ent_identifier=")) {
            //post
            activity.showToast("Unliked Post");
        } else if (url.contains("https://m.facebook.com/stories.php?aftercursorr=")) {
            //post
            activity.showToast("Post Liked Sucess");
        }










        /*boolean isLikeClicked = false;
        if (url.contains("https://m.facebook.com/a/profile.php?fan&id=")
                && url.contains("&origin=page_profile&pageSuggestionsOnLiking=1")) {
            activity.showToast("User Like Click");

        } else if (url.contains("https://m.facebook.com/a/profile.php?fan&id=")
                && url.contains("&origin=search&gfid=")) {
            activity.showToast("User Like Click");
        } else if (url.contains("?fc=f&showPageSuggestions&_rdr")) {
            activity.showToast("Like Sucess");
        } else if (url.contains("https://m.facebook.com/a/profile.php?unfan&id=")) {
            activity.showToast("User Unliked Click");
        } else if (url.contains("?fc=u&_rdr")) {
            activity.showToast("Unliked Success");
        } else if (url.contains("https://m.facebook.com/a/like.php?")) {
            isLikeClicked = true;
            activity.showToast("User liked Click");
        } else if (url.contains("https://m.facebook.com/story.php?story_fbid=")) {
            activity.showToast("Liked & Unliked Sucess");
        } else if (url.contains("https://m.facebook.com/reactions/picker/?ft_id=")) {
            activity.showToast("Like Emoji pannel open");
        } else if (url.contains("?lul&_ft_=mf_story_key")) {
            activity.showToast("Unliked Post");
        } else if (isLikeClicked && url.contains("https://m.facebook.com/groups/")) {
            activity.showToast("Liked Post");
        } else if (url.contains("https://m.facebook.com/ufi/reaction/?ft_ent_identifier=")) {
            activity.showToast("Unliked Post");
        }
        else if ( url.contains("https://m.facebook.com/stories.php?aftercursorr=")) {
            activity.showToast("Liked Sucess");
        }
        else {
            //TODO call default
        }
        */
    }

    /**
     * Write rest of the code of fragment in onCreateView after view created
     */
    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface", "AddJavascriptInterface"})
    @Override
    protected void init() {
        activityHome = (Home) context;
        activityHome.backPressedEventClickList.clear();

        isFragmentOpen = true;
        ((Home)context).mNavigationMenuCallBack=this;
        preferencesService = new AppPreferencesService(context);
        loader = new CustomLoader(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        loader.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mYouTubeSubscribe = new YouTubeSubscribe(getActivity(), this);

        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        binding.webView.getSettings().setDomStorageEnabled(true);
        //setDesktopMode(binding.webView, false);
        binding.webView.getSettings().setSaveFormData(true);
        // binding.webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 4.4.4; One Build/KTU84L.H4) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/33.0.0.0 Mobile Safari/537.36 [FB_IAB/FB4A;FBAV/28.0.0.20.16;]");

        binding.webView.getSettings().setAllowContentAccess(true);
        binding.webView.getSettings().setAllowFileAccess(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            binding.webView.getSettings().setAllowFileAccessFromFileURLs(true);
            binding.webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }
        binding.webView.getSettings().setSupportZoom(true);
        binding.webView.setClickable(true);

      //  binding.webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        binding.webView.setWebViewClient(new MyWebViewClient());

        binding.webView.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {
                // Log.d(TAG + " 185", "Load Status: " + progress);

//                if (progress >= 40) {
//
//                    binding.webView.loadUrl("javascript:window.HTMLOUT.processHTML(" +
//                            "'<head>'+document.getElementsByClassName(" +
//                            "'lispantxt')[1].innerHTML+'</head>');");
//
//                    binding.webView.loadUrl("javascript:window.HTMLOUT.processHTML(" +
//                            "'<head>'+document.getElementsByClassName(" +
//                            "'lispantxt')[0].innerHTML+'</head>');");
//
//                    count++;
//
//                    if (TASK_TYPE != ADS_VIEW) {
//                        loader.dismiss();
//
//                    }
//
//
//                }

                if (progress >= 60) {
                    //-------------task ads-----------------
                    if (TASK_TYPE == ADS_VIEW && !isTimerStart) {
                        if (fragmentCalls instanceof TaskDetailFragment && isTaskRunning) {
                            isTimerStart = true;
                            showTimer();
                        }
                    } else if (TASK_TYPE == FACEBOOK_LIKE && !isTimerStart) {
                        if (fragmentCalls instanceof TaskDetailFragment) {
                            isTimerStart = true;
                            showTimerFbLike();
                        }
                    }
                    try{
                        loader.dismiss();
                    }catch (IllegalArgumentException e){

                    }

                }

            }
        });

        binding.likeButtonView.setVisibility(View.GONE);
        binding.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFacebookLikeSuccess();
            }
        });

        String url = "";
        if (TASK_TYPE == ADS_VIEW) {
            loadAdsViewUrl();
        } else {
            if (getArguments() != null) {
                url = getArguments().getString(WEB_URL);
                loader.show();
                loader.setCancelable(true);
                if(url!=null && !url.isEmpty()) {
                    if (url.contains("www.facebook.com")) {
                        url = url.replace("www.facebook.com", "mbasic.facebook.com");
                    }
                    if (url.contains("m.facebook.com")) {
                        url = url.replace("m.facebook.com", "mbasic.facebook.com");
                    }
                    Log.e(TAG + " URL", url);
                    binding.webView.loadUrl(url);
                }

            }
        }

        //----facebook----------------
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

            @Override
            public void onSuccess(Sharer.Result result) {
                Log.e(TAG + " onSuccess", String.valueOf(result));
                Log.e(TAG + " facebookShareParam", facebookShareParam);
                apis(1, 0);
            }

            @Override
            public void onCancel() {
                Log.e(TAG + " onCancel", "onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG + " error", String.valueOf(error));
            }
        });

        /*//-------------task ads-----------------
        if(fragmentCalls instanceof TaskDetailFragment && isTaskRunning){
            showTimer();
        }*/


    }

    @Override
    public void startActivityForResult(Intent intent, boolean isChooseAccount) {
        if (isChooseAccount) {
            startActivityForResult(intent, YouTubeSubscribe.REQUEST_ACCOUNT_PICKER);
        } else {
            startActivityForResult(intent, YouTubeSubscribe.RC_SIGN_IN);
        }
    }

    /**
     * @param status 0 for subscription success
     *               1 for already subscribe
     *               2 for nothing
     */
    @Override
    public void onFragmentResume(int status) {
        apis(2, status);
    }

    @Override
    public void navigationMenuClick() {

        /*if(context!=null){
            ((Home)context).binding.toolbarView.timeLayout.setVisibility(View.GONE);
        }*/
        if(TASK_TYPE==ADS_VIEW || TASK_TYPE == FACEBOOK_LIKE) {
            if (isTimerRunnig && countDownTimer != null) {
                onTaskCancel();
            } else if (!isTimerRunnig && countDownTimer != null) {
                onTaskComplete();
            }
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //for facebook sharing
            if (TASK_TYPE == FACEBOOK_LIKE) {
                view.loadUrl(url);
                facebookLike(url);
            } else if (TASK_TYPE == ADS_VIEW) {
                isAdsViewClicked = true;
                view.loadUrl(url);
            } else {
                view.loadUrl(url);
                /*if(url!=null && !url.isEmpty()) {
                    if (url.contains("facebook.com") && url.contains("dialog_refer_earn")) {
                        String url1 = url.substring(url.indexOf("href="));
                        url1 = url1.substring(5, url1.indexOf('&'));
                        url1 = Uri.decode(url1);

                        Log.e(TAG, "dialog_refer_earn url" + url1);
                        Log.e(TAG, "real" + url);

                        ShareLinkContent content = new ShareLinkContent.Builder()
                                .setContentUrl(Uri.parse(url1))
                                .build();
                        shareOnFacebook(content);
                    }

                    //for youtube subscribtion
                    else if (url.contains("youtube") && url.contains("channel")) {
                        channelId = url.replace("https://www.youtube.com/channel/", "");
                        mYouTubeSubscribe.getResultsFromApi(channelId, HomeFragment.this);
                    } else {
                        view.loadUrl(url);
                        Log.e(TAG, "I: U:when click on any interlink:-" + url);
                    }
                }*/

            }

            /*else {
                view.loadUrl(url);
                Log.e(TAG, "I: U:when click on any interlink:-" + url);
            }
*/
           // count = 0;

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            loader.dismiss();


            Log.e("WebView", "your current url when webpage loading.. finish" + url);
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
           // count = 0;

           // if (TASK_TYPE != ADS_VIEW) {
                loader.show();
                loader.setCancelable(true);
            //}

            Log.e("WebView", "your current url when webpage loading.." + url);
        }
    }

    @Override
    public void onClick(View v) {

    }

    public boolean onKeyDownHome(final int keyCode, final KeyEvent event) {
        if (isFragmentOpen) {
            if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                if (TASK_TYPE == FACEBOOK_LIKE) {
                    onFacebookLikeFailed();
                } else if (TASK_TYPE == ADS_VIEW) {
                    if (isSuccessfulAdsFinish && isTaskRunning) {
                        onTaskComplete();
                    } else if (binding.webView.canGoBack()) {
                        binding.webView.goBack();
                        return true;
                    } else if (isTaskRunning) {
                        onTaskCancel();
                    }
                } else {
                    if (binding.webView.canGoBack()) {
                        if (newOpenView != null && newOpenView.getChildCount() > 0) {
                            newOpenView.removeAllViews();
                        } else {
                            binding.webView.goBack();
                        }

                        //If there is history, then the canGoBack method will return ‘true’//
                        return true;
                    } else {
//                    if (isTaskRunning) {
//                        onTaskCancel();
//                    }
                    }
                }
            }

        }

        //If the button that’s been pressed wasn’t the ‘Back’ button, or there’s currently no
        //WebView history, then the system should resort to its default behavior and return
        //the user to the previous Activity//
        return false;
    }

    private void shareOnFacebook(ShareLinkContent content) {
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            shareDialog.show(content);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case YouTubeSubscribe.REQUEST_GOOGLE_PLAY_SERVICES: // if user don't have google play service
                if (resultCode != RESULT_OK) {
                    Toast.makeText(getActivity(), "This app requires Google Play Services. Please " +
                                    "install Google Play Services on your device and relaunch this app.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    mYouTubeSubscribe.getResultsFromApi(channelId, this);
                }
                break;


            case YouTubeSubscribe.REQUEST_ACCOUNT_PICKER: // when user select google account
                if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        new AppPreferencesService(context).setEmailDevice(accountName);

                        mYouTubeSubscribe.mCredential.setSelectedAccountName(accountName);
                        mYouTubeSubscribe.getResultsFromApi(channelId, this);
                    }
                }
                break;


            case YouTubeSubscribe.REQUEST_AUTHORIZATION: // when your grant account access permission
                if (resultCode == RESULT_OK) {
                    mYouTubeSubscribe.getResultsFromApi(channelId, this);
                }
                break;


            case YouTubeSubscribe.RC_SIGN_IN: // if user do sign in
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    mYouTubeSubscribe.getResultsFromApi(channelId, this);
                } else {
                    if(!isErrorTaskCompleted) {
                        isErrorTaskCompleted=true;
                         onTaskComplete();
                    }

                    Toast.makeText(context, "Permission Required if granted then check internet connection", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                callbackManager.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, java.util.List<String> perms) {
        mYouTubeSubscribe.getResultsFromApi(channelId, this); // user have granted permission so continue
    }

    @Override
    public void onPermissionsDenied(int requestCode, java.util.List<String> perms) {
        Toast.makeText(context, "This app needs to access your Google account for YouTube channel subscription.", Toast.LENGTH_SHORT).show();

    }


//    private class MyJavaScriptInterface {
//        @JavascriptInterface
//        @SuppressWarnings("unused")
//        public void processHTML(String html) {
//            // process the html as needed by the app
//            Log.e(TAG + " Html", html);
//
//            if (html.contains("callshare")) {
//                String str1 = html.substring(html.indexOf("callshare"));
//                facebookShareParam = str1.substring(0, str1.indexOf(";"));
//            }
//            /*else if (html.contains("TaskCompletion()")) {
//                Log.e(TAG, "LIked get --------------");
//            }*/
//        }
//    }

    private void apis(int index, int subscription) {
        APIService apiService = APIClient.getConnect().create(APIService.class);
        Call<HomeFragmentViewModel> call = null;// = apiService.checkSheetDetails(module, "", "");

        if (index == 1) {
            call = apiService.facebookShareUpdate(facebookShareParam, preferencesService.getUMobile());
        } else {
            call = apiService.youtubeUpdate(facebookShareParam, preferencesService.getUMobile(),
                    subscription);
        }

        if (call != null) {
            call.enqueue(new retrofit2.Callback<HomeFragmentViewModel>() {
                @Override
                public void onResponse(@NonNull Call<HomeFragmentViewModel> call,
                                       @NonNull Response<HomeFragmentViewModel> response) {
                    HomeFragmentViewModel data = response.body();
                    if (data != null && data.getStatus().equals("1")) {
                        if (binding.webView.canGoBack()) {
                            binding.webView.goBack();
                        }
                        if (binding.webView.canGoBack()) {
                            binding.webView.goBack();
                        }
                        binding.webView.loadUrl(data.getUrl());
                    } else {
                        activity.showToast(R.string.try_again);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<HomeFragmentViewModel> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    call.cancel();
                    activity.showToast(R.string.network_error);
                }
            });
        }

    }

    public void setDesktopMode(WebView webView, boolean enabled) {
        String newUserAgent = webView.getSettings().getUserAgentString();
        if (enabled) {
            try {
                String ua = webView.getSettings().getUserAgentString();
                String androidOSString = webView.getSettings().getUserAgentString().substring(ua.indexOf("("), ua.indexOf(")") + 1);
                newUserAgent = webView.getSettings().getUserAgentString().replace(androidOSString, "(X11; Linux x86_64)");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            newUserAgent = null;
        }

        webView.getSettings().setUserAgentString(newUserAgent);
        webView.getSettings().setUseWideViewPort(enabled);
        webView.getSettings().setLoadWithOverviewMode(enabled);
        webView.reload();
    }
}
