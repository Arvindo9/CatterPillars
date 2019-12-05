package com.solution.catterpillars.youtube_subscription;

/**
 * Created by admin on 16-Oct-17.
 */

public interface YouTubeAlreadySubscribeCallBack {
    void onAlreadySubscribe(boolean value, String title);

    void onSubscribetionFail();

}
