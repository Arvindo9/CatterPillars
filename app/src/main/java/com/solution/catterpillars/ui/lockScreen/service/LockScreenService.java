package com.solution.catterpillars.ui.lockScreen.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Toast;

import com.solution.catterpillars.R;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.ui.lockScreen.receiver.RestartService;
import com.solution.catterpillars.ui.home.Home;
import com.solution.catterpillars.ui.lockScreen.LockScreenActivity;
import com.solution.catterpillars.util.CustomAlertDialog;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 01-Nov-18
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 * About : I am a mathematician
 * Quote : Only brain can make anything possible
 * Strength : Never give up
 */
public class LockScreenService extends Service {

    public AppPreferencesService preferencesService;
    public static final String TAG = LockScreenService.class.getSimpleName();
    private int mServiceStartId = 0;
    private Context mContext = null;
    private WindowManager mWindowManager;
    View mView;
    Animation mAnimation;

    private BroadcastReceiver mLockscreenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != context) {
                if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

                    startLockscreenActivity(context);
                }
            }
        }
    };


    private void stateReceiver(boolean isStartReceiver) {
        try {
            if (isStartReceiver) {
                IntentFilter filter = new IntentFilter();
                filter.addAction(Intent.ACTION_SCREEN_OFF);
                registerReceiver(mLockscreenReceiver, filter);
            } else {
                if (null != mLockscreenReceiver) {
                    unregisterReceiver(mLockscreenReceiver);
                }
            }
        }
        catch (IllegalArgumentException iae){

        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        preferencesService = new AppPreferencesService(mContext);
       // Toast.makeText(this,"Lock Screen Service on Create Call",Toast.LENGTH_LONG).show();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mServiceStartId = startId;
        stateReceiver(true);
       // Toast.makeText(this,"Lock Screen Service on Start command Call",Toast.LENGTH_LONG).show();
        startForgroundService();

        return START_STICKY;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        //Toast.makeText(this,"Service Trim Memory Call",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //Toast.makeText(this,"Service Low Memory Call",Toast.LENGTH_LONG).show();
    }


    private void startForgroundService(){
        String CHANNEL_ID = getPackageName();
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, getPackageName() + " Service",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
        }*/
            Intent intent = new Intent(this, Home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.app_icon_round)
                    .setContentTitle(getString(R.string.app_name)+" is running")
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setContentText("Tap for more information or to stop the app.")
                    .setContentIntent(pendingIntent).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_MIN;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, getPackageName() + " Service", importance);
            notificationManager.createNotificationChannel(mChannel);
        }
            startForeground(1, notification);
        //notificationManager.notify(1,notification);
        /*} else {
            startForeground(1, new Notification());
        }*/
    }

    @Override
    public void onDestroy() {
        if(preferencesService.isLockScreenOn()) {
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("restartservice");
            broadcastIntent.setClass(this, RestartService.class);
           sendBroadcast(broadcastIntent);
        }
        stateReceiver(false);
      //Toast.makeText(this, "Service On destroy", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        if(preferencesService.isLockScreenOn()) {
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("restartservice");
            broadcastIntent.setClass(this, RestartService.class);
            sendBroadcast(broadcastIntent);
        }
        stateReceiver(false);
       // Toast.makeText(this, "On Task Removed", Toast.LENGTH_LONG).show();
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public boolean onUnbind(Intent intent) {

      //  Toast.makeText(this, "On Unbined", Toast.LENGTH_LONG).show();
        return super.onUnbind(intent);
    }


    private void startLockscreenActivity(Context context) {

        Intent startLockscreenActIntent = new Intent(mContext, LockScreenActivity.class);
        startLockscreenActIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startLockscreenActIntent);
    }



    public class LocalBinder extends Binder {
        public LockScreenService getService() {
            // Return this instance of LocalService so clients can call public methods
            return LockScreenService.this;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }


}
