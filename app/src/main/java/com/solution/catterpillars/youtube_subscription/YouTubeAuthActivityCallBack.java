package com.solution.catterpillars.youtube_subscription;

import android.content.Intent;

/**
 * Created by admin on 16-Oct-17.
 */

public interface YouTubeAuthActivityCallBack {
    void startActivityForResult(Intent intent, boolean isChooseAccount);

}
