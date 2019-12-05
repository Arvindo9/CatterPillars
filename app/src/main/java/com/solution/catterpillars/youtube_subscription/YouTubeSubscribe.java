package com.solution.catterpillars.youtube_subscription;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTubeScopes;
import com.solution.catterpillars.R;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.ui.home.content.HomeFragment;
import com.solution.catterpillars.ui.home.content.HomeFragmentNavigator;
import com.solution.catterpillars.ui.home.dashboard.task.task_detail.TaskDetailFragment;
import com.solution.catterpillars.ui.home.dashboard.task.task_detail.TaskScreenNavigator;
import com.solution.catterpillars.util.CustomAlertDialog;
import com.solution.catterpillars.util.CustomLoader;

import java.util.Arrays;

import pub.devrel.easypermissions.EasyPermissions;

import static com.solution.catterpillars.ui.home.dashboard.task.task_detail.TaskScreen.TASK_COMPLETED;

public class YouTubeSubscribe implements
        YouTubeSubscribeCallBack, YouTubeAlreadySubscribeCallBack {
    // if you are using YouTubePlayerView in xml then activity must extend YouTubeBaseActivity

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    public static final int REQUEST_ACCOUNT_PICKER = 1000;
    public static final int REQUEST_AUTHORIZATION = 1001;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    public static final int RC_SIGN_IN = 12311;

    private static final String PREF_ACCOUNT_NAME = "accountName";
    ;
    private final Context context;
    private final CustomLoader loader;
    private  String emailId;
    private String youtubeKey = "AIzaSyBESw6DuRRejZT1cAbiz3zaOC4TSL_mPgo";// paste your youtube key here
    // AIzaSyA-F-SoBTgtZr-em965Po5Wzw_Upxrf4U8
    public GoogleAccountCredential mCredential;
    private ProgressDialog pDialog;
    private YouSubscribedChannelList channelList;
    private YouTubeChannelSubscribe subscribeAction;
    private int counter = 0;
    private AppPreferencesService preferencesService;
    YouTubeAuthActivityCallBack authActivityCallBack;
    private String channelId;
    private HomeFragmentNavigator homeFragmentNavigator;
    private TaskScreenNavigator taskScreenNavigator;

    public YouTubeSubscribe(Context context, YouTubeAuthActivityCallBack authActivityCallBack) {
        this.context = context;
        this.authActivityCallBack = authActivityCallBack;
        loader = new CustomLoader(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        channelList = new YouSubscribedChannelList(this, context);
        subscribeAction = new YouTubeChannelSubscribe(this, context);

        //user login email

        preferencesService = new AppPreferencesService(context);

         emailId = "mgss.harshna@gmail.com";
       // emailId = preferencesService.getEmailDevice();

        mCredential = GoogleAccountCredential.usingOAuth2(context, Arrays.asList(YouTubeScopes.YOUTUBE))
                .setBackOff(new ExponentialBackOff());

        if(emailId.toLowerCase().contains("gmail.com")){
            mCredential.setSelectedAccountName(emailId);
        }
        // getResultsFromApi();
    }


    public void getResultsFromApi(String channelId, HomeFragmentNavigator homeFragmentNavigator) {
        this.channelId = channelId;
        this.homeFragmentNavigator = homeFragmentNavigator;

        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount(channelId);
        } else {
            loader.show();
            loader.setCancelable(true);

            // UC_x5XG1OV2P6uZZ5FSM9Ttw
            //UCMqVnnpvc1S1oeC_teoa67Q
            channelList.subscribeToYouTubeChannel(mCredential, channelId); // pass youtube channelId as second parameter
        }

    }

    public void getResultsFromApi(String channelId, TaskScreenNavigator taskScreenNavigator) {
        this.channelId = channelId;
        this.taskScreenNavigator = taskScreenNavigator;

        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount(channelId);
        } else {
            loader.show();
            loader.setCancelable(true);

            // UC_x5XG1OV2P6uZZ5FSM9Ttw
            //UCMqVnnpvc1S1oeC_teoa67Q
            channelList.subscribeToYouTubeChannel(mCredential, channelId); // pass youtube channelId as second parameter
        }

    }

    // checking google play service is available on phone or not
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(context);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }


    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(context);

        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            Dialog dialog = apiAvailability.getErrorDialog(
                    (Activity) context,  // showing dialog to user for getting google play service
                    connectionStatusCode,
                    REQUEST_GOOGLE_PLAY_SERVICES);
            dialog.show();
        }
    }


    public void chooseAccount(String channelId) {
        emailId = preferencesService.getEmailDevice();
        if (EasyPermissions.hasPermissions(context, android.Manifest.permission.GET_ACCOUNTS)) {
            if (emailId != null && !emailId.isEmpty()) {
                mCredential.setSelectedAccountName(emailId);
                if (mCredential.getSelectedAccountName() == null) {
                    authActivityCallBack.startActivityForResult(mCredential.newChooseAccountIntent()
                            .putExtra("overrideTheme", 1),true);
                    /*((Activity) context).startActivityForResult(mCredential.newChooseAccountIntent()
                            .putExtra("overrideTheme", 1), REQUEST_ACCOUNT_PICKER);*/
                } else {
                    if(authActivityCallBack instanceof TaskDetailFragment) {
                        getResultsFromApi(channelId, (TaskDetailFragment) authActivityCallBack);
                    }
                    else {
                        getResultsFromApi(channelId, (HomeFragment) authActivityCallBack);
                    }
                }

            } else {
                // Start a dialog from which the user can choose an account
                authActivityCallBack.startActivityForResult(mCredential.newChooseAccountIntent()
                        .putExtra("overrideTheme", 1),true);
                /*((Activity) context).startActivityForResult(mCredential.newChooseAccountIntent()
                        .putExtra("overrideTheme", 1), REQUEST_ACCOUNT_PICKER);*/
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    ((Activity) context),
                    "This app needs to access your Google account for YouTube channel subscription.",
                    REQUEST_PERMISSION_GET_ACCOUNTS, android.Manifest.permission.GET_ACCOUNTS);
        }

    }


    @Override  // responce from presenter on success
    public void onSubscribetionSuccess(String title) {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        showToast("Successfully subscribed to " + title);
        taskScreenNavigator.onFragmentResume(TASK_COMPLETED);
       //Toast.makeText(context, "Successfully subscribe to " + title, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAlreadySubscribe(boolean value, String Title) {

        if (value) {

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            showToast("Already subscribed " + Title);
            taskScreenNavigator.onFragmentResume(TASK_COMPLETED);

        } else {
            subscribeAction.subscribeToYouTubeChannel(mCredential, channelId);
        }
    }

    @Override
    public void onSubscribetionFail() {
        loader.dismiss();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        // user don't have youtube channel subscribe permission so grant it form him
        // as we have not taken at the time of sign in
        if (counter < 3) {
            counter++; // attempt three times on failure
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestScopes(new Scope("https://www.googleapis.com/auth/youtube")) // require this scope for youtube channel subscribe
                    .build();

            GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);

            authActivityCallBack.startActivityForResult(signInIntent,false);
            /* ((Activity) context).startActivityForResult(signInIntent, RC_SIGN_IN);*/
        } else {

            //TODO check for some device
            showToast("goto following link and enable the youtube api access\n" +
                    "https://console.developers.google.com/apis/api/youtube.googleapis.com/overview?project=YOUR_PROJECT_ID");

        }
    }

    @Override // responce from presenter on failure
    public void onSubscribetionFail(String msg , boolean isRedirect) {

        if(isRedirect){
            onSubscribetionFail();
        }else{
            taskScreenNavigator.onFragmentResume(TASK_COMPLETED);

            showToast(msg);
        }

    }

    private void showToast(String msg){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                loader.dismiss();
                Toast.makeText(context, msg,
                        Toast.LENGTH_LONG).show();
            }
        });
    }




}
