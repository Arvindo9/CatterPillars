package com.solution.catterpillars.base;

import android.app.Application;
import android.content.res.Configuration;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.solution.catterpillars.util.ApplicationConstant;

import io.fabric.sdk.android.Fabric;


/**
 * Created by Arvindo Mondal on 29-Oct-18.
 * Roundpay Techno Media OPC Pvt Ltd
 * I am a mathematician
 * arvindomondal@gmail.com
 * Only intelligence can make anything possible
 * Never give up
 */
public class BaseApplication extends Application {
    public boolean lockScreenShow = false;
    public int notificationId = 1989;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        FirebaseCrash.log("Activity created");
        FirebaseMessaging.getInstance().subscribeToTopic(ApplicationConstant.INSTANCE.TOPIC_SUBSCRIPTION_LOCKSCREEN);
    }

   /* @Override
    public void onTerminate() {
        super.onTerminate();
        Toast.makeText(this, "On Terminate", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Toast.makeText(this, "On LowMemory", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

        Toast.makeText(this, "On Trim memory", Toast.LENGTH_LONG).show();
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Toast.makeText(this, "On Config", Toast.LENGTH_LONG).show();
    }*/
}
