package com.solution.catterpillars.youtube_subscription;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SubscriptionListResponse;
import com.solution.catterpillars.R;

import java.util.HashMap;

/**
 * Created by admin on 16-Oct-17.
 */

class YouSubscribedChannelList {
    private final YouTubeAlreadySubscribeCallBack view;
    private final Activity activity;

    YouSubscribedChannelList(YouTubeAlreadySubscribeCallBack view, Context activity) {
        this.view = view;
        this.activity = (Activity) activity;
    }

    void subscribeToYouTubeChannel(GoogleAccountCredential mCredential, String channelId) {

        new MakeRequestTask(mCredential, channelId).execute(); // creating AsyncTask for channel subscribe

    }

    private class MakeRequestTask extends AsyncTask<Object, Object, SubscriptionListResponse> {
        private YouTube mService = null;
        private String channelId;

        MakeRequestTask(GoogleAccountCredential credential, String channelId) {
            this.channelId = channelId;
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new YouTube.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName(activity.getResources().getString(R.string.app_name))
                    .build();
        }

        @Override
        protected SubscriptionListResponse doInBackground(Object... params) {
            // code for channel subscribe
            SubscriptionListResponse response = null;
            try {
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("part", "snippet");
                parameters.put("mine", "true");
                parameters.put("forChannelId", channelId);

                YouTube.Subscriptions.List subscriptionsListMySubscriptionsRequest = mService.subscriptions().list(parameters.get("part").toString());
                if (parameters.containsKey("mine") && parameters.get("mine") != "") {
                    boolean mine = (parameters.get("mine") == "true") ? true : false;
                    subscriptionsListMySubscriptionsRequest.setMine(mine);
                }
                if (parameters.containsKey("forChannelId") && parameters.get("forChannelId") != "") {
                    String chanelid = (parameters.get("forChannelId") == channelId) ? channelId : "";
                    subscriptionsListMySubscriptionsRequest.setForChannelId(chanelid);
                }

                response = subscriptionsListMySubscriptionsRequest.execute();
                //System.out.println(response);
            } catch (GoogleJsonResponseException e) {
                e.printStackTrace();
                view.onSubscribetionFail();
                System.err.println("There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
            } catch (Throwable t) {
                t.printStackTrace();
                view.onSubscribetionFail();
            }

            return response;
        }

        @Override
        protected void onPostExecute(SubscriptionListResponse subscription) {
            super.onPostExecute(subscription);
            if (subscription != null) {
                if (subscription.getItems().size() > 0) {
                    boolean isExist = false;
                    String Title = "";
                    for (int i = 0; i < subscription.getItems().size(); i++) {
                        if (subscription.getItems().get(i).getSnippet().getResourceId().getChannelId().equals(channelId)) {
                            isExist = true;
                            Title = subscription.getItems().get(i).getSnippet().getTitle();
                            break;
                        }
                    }
                    if (isExist) {
                        view.onAlreadySubscribe(true, Title);
                    } else {
                        view.onAlreadySubscribe(false, "");
                    }

                } else {
                    view.onAlreadySubscribe(false, "");
                }

            } else {
                view.onSubscribetionFail();
            }
        }
    }
}
