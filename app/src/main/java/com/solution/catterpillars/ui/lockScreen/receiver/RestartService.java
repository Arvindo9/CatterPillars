package com.solution.catterpillars.ui.lockScreen.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.ui.lockScreen.LockScreen;
import com.solution.catterpillars.util.AppsFunction;

public class RestartService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Broadcast Listened", "Service tried to stop");
       // Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();
       // AppsFunction.loadLockScreenAds(context, new AppPreferencesService(context).getUMobile());
        LockScreen.getInstance().init(context);
        LockScreen.getInstance().active();


    }
}