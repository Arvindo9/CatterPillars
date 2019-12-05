package com.solution.catterpillars.notification.service;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.ui.lockScreen.LockScreen;
import com.solution.catterpillars.ui.lockScreen.model.LockScreenViewModel;
import com.solution.catterpillars.ui.lockScreen.model.NewsData;
import com.solution.catterpillars.util.AppsFunction;

import java.util.ArrayList;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private AppPreferencesService preferences;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {


        }

        //message will contain the Push Message
        final String message = remoteMessage.getData().get("message");
        String img = remoteMessage.getData().get("Image");
        final String url = remoteMessage.getData().get("Url");
        final String title = remoteMessage.getData().get("Title");
        final String key = remoteMessage.getData().get("key");
        final String postDate = remoteMessage.getData().get("PostDate");
        final String type = remoteMessage.getData().get("Type");

        if(type.equalsIgnoreCase("News_Notification")) {
            Gson gson = new Gson();
            preferences = new AppPreferencesService(this);
            LockScreenViewModel viewModel = gson.fromJson(preferences.getLockScreenData(), LockScreenViewModel.class);
            if (viewModel != null) {
                ArrayList<NewsData> list = new ArrayList<>(viewModel.getNewsList());
                if (list != null) {
                    NewsData mNewsData = new NewsData();
                    mNewsData.setImage(img);
                    mNewsData.setKey(key);
                    mNewsData.setMessage(message);
                    mNewsData.setPostDate(postDate);
                    mNewsData.setTitle(title);
                    mNewsData.setUrl(url);
                    list.add(mNewsData);
                    if (list.size() > 1) {
                        list.remove(list.size() - 1);
                    }
                    viewModel.setNewsList(list);
                    preferences.setLockScreenData(new Gson().toJson(viewModel));
                }

            }
        }

        LockScreen.getInstance().init(this);
        LockScreen.getInstance().active();
    }
}
