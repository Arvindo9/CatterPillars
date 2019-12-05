package com.solution.catterpillars.ui.lockScreen.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.solution.catterpillars.R;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.ui.home.Home;
import com.solution.catterpillars.ui.lockScreen.LockScreen;
import com.solution.catterpillars.ui.lockScreen.LockScreenActivity;
import com.solution.catterpillars.util.AppsFunction;

public class LockFirebaseJobService extends JobService {


    /*private BroadcastReceiver mLockscreenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != context) {
                if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

                            startLockscreenActivity(context);
                    Toast.makeText(context,"Screen lock",Toast.LENGTH_LONG).show();
                    //Log.i("Test Job", "show Lock Screen called");
                }
            }
        }
    };


    private void stateReceiver(boolean isStartReceiver) {
        if (isStartReceiver) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            registerReceiver(mLockscreenReceiver, filter);
        } else {
            if (null != mLockscreenReceiver) {
                unregisterReceiver(mLockscreenReceiver);
            }
        }
    }*/

    @Override
    public boolean onStartJob(JobParameters job) {
        // Do some work here
       // Log.i("Test Job", "onStartJob called");
        //Toast.makeText(this,"Firebase Job Service Call",Toast.LENGTH_LONG).show();
        //AppsFunction.loadLockScreenAds(this, new AppPreferencesService(this).getUMobile());
        LockScreen.getInstance().init(this);
        LockScreen.getInstance().startNormalService();
        //  CentralContainer.getStore(getApplicationContext()).recordResult(job, result);
        return true; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
       // Log.i("Test Job", "onStopJob called");
        return true; // Answers the question: "Should this job be retried?"
    }

   /* @Override
    public void onDestroy() {
        Log.i("Test Job", "onDestroy called");
        super.onDestroy();
    }*/


    /*private void showNotification() {
        String CHANNEL_ID = getPackageName();
        Intent intent = new Intent(this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1 *//* Request code *//*, intent, PendingIntent.FLAG_ONE_SHOT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.app_icon_round)
                .setContentTitle(getString(R.string.app_name)+" is running")
                .setAutoCancel(true)
                .setOngoing(true)
                .setContentText("Tap for more information or to stop the app.")
                .setContentIntent(pendingIntent).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_MIN;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, getPackageName() + " Service", importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        notificationManager.notify(1,notification);

    }

    private void startLockscreenActivity(Context context) {
        Intent startLockscreenActIntent = new Intent(this, LockScreenActivity.class);
        startLockscreenActIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startLockscreenActIntent);
    }*/
}