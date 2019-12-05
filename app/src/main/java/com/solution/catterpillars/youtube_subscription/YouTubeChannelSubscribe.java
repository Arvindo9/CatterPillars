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
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.Subscription;
import com.google.api.services.youtube.model.SubscriptionSnippet;
import com.solution.catterpillars.R;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by admin on 16-Oct-17.
 */

class YouTubeChannelSubscribe {
    private final YouTubeSubscribeCallBack view;
    private final Activity activity;
    private String msg = "";
    boolean isRedirect=false;

    YouTubeChannelSubscribe(YouTubeSubscribeCallBack view, Context activity) {
        this.view = view;
        this.activity = (Activity) activity;
    }

    void subscribeToYouTubeChannel(GoogleAccountCredential mCredential, String channelId) {

        new MakeRequestTask(mCredential, channelId).execute(); // creating AsyncTask for channel subscribe

    }

    private class MakeRequestTask extends AsyncTask<Object, Object, Subscription> {
        private com.google.api.services.youtube.YouTube mService = null;
        private String channelId;

        MakeRequestTask(GoogleAccountCredential credential, String channelId) {
            this.channelId = channelId;
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.youtube.YouTube.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName(activity.getResources().getString(R.string.app_name))
                    .build();
        }

        @Override
        protected Subscription doInBackground(Object... params) {
            // code for channel subscribe
            Subscription response = null;
            try {
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("part", "snippet,contentDetails");
                //  parameters.put("forChannelId", channelId);
                // if you could not able to import the classes then check the dependency in build.gradle
                Subscription subscription = new Subscription();
                SubscriptionSnippet snippet = new SubscriptionSnippet();
                ResourceId resourceId = new ResourceId();
                resourceId.setChannelId(channelId);
                resourceId.setKind("youtube#channel");

                snippet.setResourceId(resourceId);
                subscription.setSnippet(snippet);

                YouTube.Subscriptions.Insert subscriptionsInsertRequest = null;

                subscriptionsInsertRequest = mService.subscriptions().insert("snippet,contentDetails", subscription);
                response = subscriptionsInsertRequest.execute();
            } catch (GoogleJsonResponseException e) {
                System.err.println("GoogleJsonResponseException code: " + e.getDetails().getCode() + " : "
                        + e.getDetails().getMessage());
                e.printStackTrace();
                if(e.getDetails().getMessage().contains("Subscribing to your own channel is not supported.")){
                    msg = e.getDetails().getMessage();
                    isRedirect=false;
                }else{
                    msg = e.getDetails().getMessage();
                    isRedirect=true;
                }


            } catch (IOException e) {
                System.err.println("IOException: " + e.getMessage());
                e.printStackTrace();
            } catch (Throwable t) {
                System.err.println("Throwable: " + t.getMessage());
                t.printStackTrace();
            }


            return response;
        }

        @Override
        protected void onPostExecute(Subscription subscription) {
            super.onPostExecute(subscription);
            if (subscription != null) {
                view.onSubscribetionSuccess(subscription.getSnippet().getTitle());
            } else {
                view.onSubscribetionFail(msg, isRedirect);
            }
        }
    }
}
