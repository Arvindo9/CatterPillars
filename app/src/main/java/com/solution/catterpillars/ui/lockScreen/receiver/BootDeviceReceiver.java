package com.solution.catterpillars.ui.lockScreen.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.ui.lockScreen.LockScreen;
import com.solution.catterpillars.util.AppsFunction;

import java.util.Calendar;

public class BootDeviceReceiver extends BroadcastReceiver {

    private static final String TAG_BOOT_BROADCAST_RECEIVER = "BOOT_BROADCAST_RECEIVER";
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        Toast.makeText(context, intent.getAction() + " Boot call", Toast.LENGTH_LONG).show();
        AppsFunction.loadLockScreenAds(context, new AppPreferencesService(context).getUMobile());
        LockScreen.getInstance().init(context);
        LockScreen.getInstance().active();

       // LockScreen.getInstance().callNightAlarmReceiver(context);

    }


}
