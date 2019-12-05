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
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Toast;

import com.solution.catterpillars.R;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.ui.home.Home;
import com.solution.catterpillars.ui.lockScreen.LockScreen;
import com.solution.catterpillars.ui.lockScreen.LockScreenActivity;
import com.solution.catterpillars.ui.lockScreen.receiver.RestartService;
import com.solution.catterpillars.util.AppsFunction;

public class AlarmReceiverService extends Service {
    public AppPreferencesService preferencesService;
    public static final String TAG = LockScreenService.class.getSimpleName();
    private int mServiceStartId = 0;
    private Context mContext = null;
    private WindowManager mWindowManager;
    View mView;
    Animation mAnimation;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        preferencesService = new AppPreferencesService(mContext);
        //Toast.makeText(this,"Service on Create Call",Toast.LENGTH_LONG).show();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String extra = intent.getStringExtra("TIME");
       // Toast.makeText(this, extra+" Alarm Service on Start command", Toast.LENGTH_LONG).show();
        //AppsFunction.loadLockScreenAds(this, new AppPreferencesService(this).getUMobile());
        LockScreen.getInstance().init(this);
        LockScreen.getInstance().active();
        stopSelf();
        return START_NOT_STICKY;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        // Toast.makeText(this, "Service Trim Memory Call", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
       // Toast.makeText(this, "Service Low Memory Call", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onDestroy() {

       // Toast.makeText(this, "Service On destroy", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {


       // Toast.makeText(this, "On Task Removed", Toast.LENGTH_LONG).show();
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public boolean onUnbind(Intent intent) {

        //  Toast.makeText(this, "On Unbined", Toast.LENGTH_LONG).show();
        return super.onUnbind(intent);
    }


    public class LocalBinder extends Binder {
        public AlarmReceiverService getService() {
            // Return this instance of LocalService so clients can call public methods
            return AlarmReceiverService.this;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return new AlarmReceiverService.LocalBinder();
    }


}
