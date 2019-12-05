package com.solution.catterpillars.youtube_subscription;

/**
 * Created by admin on 16-Oct-17.
 */

public interface YouTubeSubscribeCallBack {
    void onSubscribetionSuccess(String title);

    void onSubscribetionFail(String msg , boolean isRedirect);

}
