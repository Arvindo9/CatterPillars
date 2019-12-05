package com.solution.catterpillars.ui.lockScreen;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.ui.lockScreen.service.AlarmReceiverService;
import com.solution.catterpillars.ui.lockScreen.service.LockFirebaseJobService;
import com.solution.catterpillars.ui.lockScreen.service.LockJobService;
import com.solution.catterpillars.ui.lockScreen.service.LockScreenService;
import com.solution.catterpillars.util.AppsFunction;

import java.util.Calendar;

import static android.content.Context.JOB_SCHEDULER_SERVICE;

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
public class LockScreen {
    private static LockScreen singleton;
    private Context context;
   // private boolean disableHomeButton = false;
    public AppPreferencesService preferencesService;
    private int LOCK_JOB_ID = 475;
    private String LOCK_JOB_TAG = "CATTERPILLARS_LOCK_SCREEN_JOB";

    public static LockScreen getInstance() {
        if (singleton == null) {
            singleton = new LockScreen();
        }
        return singleton;
    }

    public void init(Context context) {
        this.context = context;
        preferencesService = new AppPreferencesService(context);
    }

   /* public void init(Context context, boolean disableHomeButton) {
        this.context = context;
        this.disableHomeButton = disableHomeButton;
        preferencesService = new AppPreferencesService(context);
    }*/


    public void active() {
        preferencesService.setLockScreenOnOff(true);
        if (context != null) {
            /*if (Build.VERSION.SDK_INT >= 21) {
                if (!isJobServiceOn(context)){
                    activeJob();
                }
            }else{
                if (isGooglePlayServicesAvailable(context)) {
                    if (!isMyServiceRunning(LockFirebaseJobService.class)){
                        activeFirebaseJob();
                    }

                }
            }*/


            if (isGooglePlayServicesAvailable(context)) {
                if (!isMyServiceRunning(LockFirebaseJobService.class)){
                    activeFirebaseJob();
                }
            } else {
                if (Build.VERSION.SDK_INT >= 21) {
                    if (!isJobServiceOn(context)) {
                        activeJob();
                    }
                }
            }
            startNormalService();
            setUpTimer();
            callNightAlarmReceiver();
        }
    }

    public void setUpTimer() {
        if (context != null) {

            Intent intent = new Intent(context, AlarmReceiverService.class);
            intent.putExtra("TIME","Repeat");
            boolean alarmUp = (PendingIntent.getService(context, 14, intent, PendingIntent.FLAG_NO_CREATE) != null);
            if (!alarmUp) {

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.SECOND, 40);
                PendingIntent pintent = PendingIntent.getService(context, 14, intent, 0);
                AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                //Repeating in 3 min
                alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 180000, pintent);
                //alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 900000, pintent);
            }
        }
    }

    public void startNormalService() {
        if (context != null) {
           // Toast.makeText(context,preferencesService.isLockScreenOn()+" is lock",Toast.LENGTH_LONG).show();
            if (!isActive() && preferencesService.isLockScreenOn()) {
                Intent intent = new Intent(context, LockScreenService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent);
                } else {
                    context.startService(intent);
                }
                setUpTimer();
            }
        }
    }

    public void deactivate() {
        if (context != null) {
            preferencesService.setLockScreenOnOff(false);
            Intent intent = new Intent(context, AlarmReceiverService.class);
            intent.putExtra("TIME","Repeat");
            boolean alarmUp = (PendingIntent.getService(context, 14, intent, PendingIntent.FLAG_NO_CREATE) != null);
            if (alarmUp) {
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                PendingIntent pintent = PendingIntent.getService(context, 14, intent, 0);
                alarmManager.cancel(pintent);
            }

            context.stopService(new Intent(context, LockScreenService.class));
            deActiveFirebaseJob();
            deActiveJob();
        }
    }


    public void activeFirebaseJob() {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        Job myJob = dispatcher.newJobBuilder()
                .setService(LockFirebaseJobService.class)
                .setTag(LOCK_JOB_TAG)
                .setRecurring(true)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(0, 5))
                .setReplaceCurrent(true)
                .build();

        dispatcher.mustSchedule(myJob);
    }

    public void deActiveFirebaseJob() {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));

        dispatcher.cancel(LOCK_JOB_TAG);
        dispatcher.cancelAll();

    }


    public void activeJob() {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            ComponentName serviceComponent = new ComponentName(context, LockJobService.class);
            JobInfo.Builder builder = new JobInfo.Builder(LOCK_JOB_ID, serviceComponent);
            builder.setMinimumLatency(1 * 1000); // wait at least
            builder.setOverrideDeadline(3 * 1000); // maximum delay
            builder.setPersisted(true);
            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(builder.build());

        }
    }

    public void deActiveJob() {
        if (context != null && isJobServiceOn(context)) {
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);

                jobScheduler.cancel(LOCK_JOB_ID);
                jobScheduler.cancelAll();
            }
        }

    }

    public boolean isJobServiceOn(Context context) {
        boolean hasBeenScheduled = false;
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            for (JobInfo jobInfo : scheduler.getAllPendingJobs()) {
                if (jobInfo.getId() == LOCK_JOB_ID) {
                    hasBeenScheduled = true;
                    break;
                }
            }
        }

        return hasBeenScheduled;
    }


    public boolean isGooglePlayServicesAvailable(Context context) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(context);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog((Activity) context, status, 2404).show();
            }
            return false;
        }
        return true;
    }


    public boolean isActive() {
        if (context != null) {
            if (isMyServiceRunning(LockScreenService.class)) {
                return true;
            }
        }
        return false;

    }

    public void startServiceIfLockEnable() {
        if (preferencesService.isLockScreenOn()) {
            // if (!isMyServiceRunning(LockScreenService.class)) {

            //AppsFunction.loadLockScreenAds(context, new AppPreferencesService(context).getUMobile());
            active();
            //}
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public void callNightAlarmReceiver() {

        Intent intent = new Intent(context, AlarmReceiverService.class);
        intent.putExtra("TIME","12AM");

        boolean alarmUp = (PendingIntent.getService(context, 54, intent, PendingIntent.FLAG_NO_CREATE) != null);

        if (!alarmUp) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 01);

            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            PendingIntent alarmIntent = PendingIntent.getService(context, 54, intent, 0);
            // Repeat per day.
            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
            //alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
        }
    }

}
