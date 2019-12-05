package com.solution.catterpillars.ui.home.dashboard.task.task_detail;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.plus.PlusShare;
import com.solution.catterpillars.R;
import com.solution.catterpillars.base.BaseActivity;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.databinding.TaskAdapterBinding;
import com.solution.catterpillars.ui.home.Home;
import com.solution.catterpillars.ui.home.content.HomeFragment;
import com.solution.catterpillars.ui.home.dashboard.task.model.TaskList;
import com.solution.catterpillars.ui.home.task.TaskAdapter;
import com.solution.catterpillars.ui.home.task.model.AppList;
import com.solution.catterpillars.util.CustomLoader;
import com.solution.catterpillars.util.TaskWatcher;
import com.solution.catterpillars.youtube_subscription.YouTubeSubscribe;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;
import static com.solution.catterpillars.util.TaskWatcher.TASK_QUEUE;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 12/10/2018
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 */
public class TaskScreen extends TaskDetailFragment
        implements EasyPermissions.PermissionCallbacks {

    private static final String TAG = TaskScreen.class.getSimpleName();
    private Context context;
    private TaskCallBack taskCallBack;
    private TaskList data;
    private int currentPosition;
    private TaskDetailFragment fragment;

    private int TASK_TYPE;
    public static final int FACEBOOK_LIKE = 1;
    public static final int FACEBOOK_SHARE = 2;
    public static final int YOUTUBE_SUBSCRIPTION = 3;
    public static final int ADS_VIEW = 4;
    public static final int GOOGLE_PLUS = 5;
    public static final int APP_DOWNLOAD = 6;
    private ShareDialog shareDialog;
    private BaseActivity activity;
    private final int GOOGLE_PLUS_REQUEST = 573;

    boolean isErrorTaskCompleted=false;

    //facebook
    private static AccessToken accessToken = AccessToken.getCurrentAccessToken();
    private boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
    private AccessTokenTracker accessTokenTracker;

    //facebook dialog_refer_earn
    private CallbackManager callbackManager;
    private CustomLoader loader;

    //youtube subscription
    private YouTubeSubscribe mYouTubeSubscribe;
    private String channelId = "UCMqVnnpvc1S1oeC_teoa67Q";

    public static final int TASK_CANCEL = 157;
    public static final int TASK_COMPLETED = 161;

    public static TaskScreen newTaskScreen() {
        return new TaskScreen();
    }

    public void setValuesToTaskScreen(Context context, BaseActivity activity,
                                      TaskCallBack taskCallBack, TaskList taskList,
                                      int currentPosition, TaskDetailFragment fragment) {
        this.context = context;
        this.activity = activity;
        this.taskCallBack = taskCallBack;
        this.data = taskList;
        this.currentPosition = currentPosition;
        this.fragment = fragment;
        setTaskType();
    }

    private void setTaskType() {
        switch (data.getTitle()) {
            case "Facebook Share":
                TASK_TYPE = FACEBOOK_SHARE;
                taskFaceBookShare();
                break;
            case "Ad View":
                TASK_TYPE = ADS_VIEW;
                taskAdsView();
                break;
            case "Youtube Subscription":
                TASK_TYPE = YOUTUBE_SUBSCRIPTION;
                taskYouTubeSubscription();
                break;
            case "Facebook Like":
                TASK_TYPE = FACEBOOK_LIKE;
                taskFacebookLike();
                break;
            case "Google+":
                TASK_TYPE = GOOGLE_PLUS;
                tashShareGooglePlus(data.getUrl());
                break;

            case "App download":
                TASK_TYPE = APP_DOWNLOAD;
                taskAppDownload();
                break;
        }

        loader = new CustomLoader(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
    }

    private void showLoader() {
        loader.show();
        loader.setCancelable(false);
        loader.setCanceledOnTouchOutside(false);
    }

    //----------------Facebook dialog_refer_earn----------------------
    private void taskFaceBookShare() {
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(fragment);

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(data.getUrl()))
                .build();
        shareOnFacebook(content);

        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

            @Override
            public void onSuccess(Sharer.Result result) {
                Log.e(TAG + " onSuccess", String.valueOf(result));
                apis(TASK_COMPLETED);
            }

            @Override
            public void onCancel() {
                Log.e(TAG + " onCancel", "onCancel");
                apis(TASK_CANCEL);
            }

            @Override
            public void onError(FacebookException error) {
                if(error.getMessage().contains("Error publishing message")){
                    apis(TASK_COMPLETED);
                }
                else{
                    apis(TASK_CANCEL);
                }
                Log.e(TAG + " error", String.valueOf(error));
            }
        });
    }

    private void shareOnFacebook(ShareLinkContent content) {
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            shareDialog.show(content);
        }
    }

    //----------------Google plus-----------------------
    private void tashShareGooglePlus(String url) {
        Intent shareIntent = new PlusShare.Builder(context)
                .setType("text/plain")
               .setText("Install "+activity.getString(R.string.app_name)+" to earn. Click on given link https://catterpillars.in")
                .setContentUrl(Uri.parse(url))
                .getIntent();

        fragment.startActivityForResult(shareIntent, GOOGLE_PLUS_REQUEST);
    }

    //----------------Facebook like-----------------------


    private void taskFacebookLike() {
//        context.startActivity(new Intent(context, FacebookLogin.class));

        if (context instanceof Home) {
            ((Home) context).fragment = HomeFragment.newInstance(data.getUrl());
            ((HomeFragment) ((Home) context).fragment).setTaskViewFragmentFbLike(context,
                    fragment, data.getTitle());
            ((Home) context).openFragment(((Home) context).fragment, HomeFragment.TAG);
        }

    }



    private void facebookLikeNative() {
        if (!isLoggedIn) {
            loginToFacebook();
        }

        if (accessToken != AccessToken.getCurrentAccessToken()) {
            accessToken();
        } else {
            accessToken = AccessToken.getCurrentAccessToken();
            onSuccessAccessToken();
        }
    }

    private void loginToFacebook() {
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
                        activity.showToast(R.string.doFacebookLoginFirst);
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        activity.showToast(R.string.doFacebookLoginFirst);
                    }
                });


//        LoginManager.getInstance().logInWithReadPermissions(this,
//                Arrays.asList("public_profile"));

        LoginManager.getInstance().logInWithReadPermissions(this,
                Collections.singletonList("public_profile"));
    }

    private void accessToken() {
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
                accessToken = currentAccessToken;
                onSuccessAccessToken();
            }
        };
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();
    }

    private void onSuccessAccessToken() {
        //TODO do facebook like here
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (accessTokenTracker != null) {
            accessTokenTracker.stopTracking();
        }
    }

    //-----------------YouTube subscription---------------

    private void taskYouTubeSubscription() {
        mYouTubeSubscribe = new YouTubeSubscribe(context, fragment);

        channelId = data.getChannelId();//data.chanellId;
        mYouTubeSubscribe.getResultsFromApi(channelId, fragment);
    }

    @Override
    public void startActivityForResult(Intent intent, boolean isChooseAccount) {
        if (isChooseAccount) {
            startActivityForResult(intent, YouTubeSubscribe.REQUEST_ACCOUNT_PICKER);
        } else {
            startActivityForResult(intent, YouTubeSubscribe.RC_SIGN_IN);
        }
    }

    //--------------Ads view task---------------------

    private void taskAdsView() {
        int min = Integer.parseInt(data.getOverallTime().substring(0,
                data.getOverallTime().indexOf(".")));
        int seconds = Integer.parseInt(data.getOverallTime().substring(
                data.getOverallTime().indexOf(".") + 1));

        long time = TimeUnit.MINUTES.toMillis(min);
        time = TimeUnit.SECONDS.toMillis(seconds) + time;

        if (context instanceof Home) {
            ((Home) context).fragment = HomeFragment.newInstance(data.getUrl());
            ((HomeFragment) ((Home) context).fragment).setTaskViewFragment(context, fragment, data.getTitle(),
                    time, data.getuRLList());
            ((Home) context).openFragment(((Home) context).fragment, HomeFragment.TAG);
        }
    }

    //-----------------------------------------------

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case YouTubeSubscribe.REQUEST_GOOGLE_PLAY_SERVICES: // if user don't have google play service
                if (resultCode != RESULT_OK) {
                    Toast.makeText(getActivity(), "This app requires Google Play Services. Please " +
                                    "install Google Play Services on your device and relaunch this app.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    mYouTubeSubscribe.getResultsFromApi(channelId, fragment);
                }
                break;


            case YouTubeSubscribe.REQUEST_ACCOUNT_PICKER: // when user select google account
                if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        new AppPreferencesService(context).setEmailDevice(accountName);

                        mYouTubeSubscribe.mCredential.setSelectedAccountName(accountName);
                        mYouTubeSubscribe.getResultsFromApi(channelId, fragment);
                    }
                }
                break;


            case YouTubeSubscribe.REQUEST_AUTHORIZATION: // when your grant account access permission
                if (resultCode == RESULT_OK) {
                    mYouTubeSubscribe.getResultsFromApi(channelId, fragment);
                }
                break;


            case YouTubeSubscribe.RC_SIGN_IN: // if user do sign in
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    mYouTubeSubscribe.getResultsFromApi(channelId, fragment);
                } else {
                    if(!isErrorTaskCompleted) {
                        isErrorTaskCompleted=true;
                        apis(TASK_COMPLETED);
                    }
                    //TODO check for some device google login
                   // Toast.makeText(context, "Permission Required if granted then check internet connection", Toast.LENGTH_SHORT).show();
                }
                break;
            case GOOGLE_PLUS_REQUEST: // if user do sign in

                if (resultCode==-1) {
                   apis(TASK_COMPLETED);
                } else {
                    apis(TASK_CANCEL);
                  //  Toast.makeText(context, "Task not Completed", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                callbackManager.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    //------------------Easy permission for youtube------------------
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        mYouTubeSubscribe.getResultsFromApi(channelId, fragment);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // user have granted permission so continue
        activity.showToast(R.string.guide_for_subscriotion);
    }

    //------------------Task app download--------------

    private void taskAppDownload() {
            //download and install
            final String appPackageName = data.getPackageName();
            final String appUrl = data.getUrl();


            if(isPackageExit(data.getPackageName())){
                //TODO completed download task
                apis(TASK_COMPLETED);
            }
            else{
                if(appUrl.contains("https://play.google.com/store/apps")) {
                    try {
                        context.startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=" +
                                        appPackageName)));
                    }
                }
                else{
                    try {
                        context.startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse(appUrl)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        anfe.printStackTrace();
                    }
                }
            }


            //task watcher
            if(!TASK_QUEUE.contains(data.getTaskNoId())) {
                new Thread(new TaskWatcher(context, fragment,
                        appPackageName,
                        data.getTaskNoId()
                )).start();
            }
    }

    private boolean isPackageExit(String packageName){
        PackageManager pm = context.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(packageName,PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }

        return app_installed;
    }

    //------fragment call back-----------------
    @Override
    public void onFragmentResume(int status) {
        apis(status);
    }

    //-------APIs-------------------------

    private void apis(int status) {

        switch (status) {
            case TASK_CANCEL:
                taskCallBack.onFailure(context.getString(R.string.task_not_compleated));
                //taskCallBack.onFailure("Task not completed");
                break;

            default:
                taskCallBack.onSuccessTaskComplete(currentPosition, data.getTaskNo());
        }

        Log.e(TAG, "Task success--------:" + status);
    }
}
