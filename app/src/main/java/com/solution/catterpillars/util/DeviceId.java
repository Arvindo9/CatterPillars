package com.solution.catterpillars.util;


import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

import static com.solution.catterpillars.base.BaseActivity.REQUEST_PERMISSIONS;
import static com.solution.catterpillars.ui.splash.WelcomeActivity.REQUEST_READ_PHONE_STATE;


/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 03-Nov-18
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 * About : I am a mathematician
 * Quote : Only brain can make anything possible
 * Strength : Never give up
 */
public enum DeviceId {

    INSTANCE;

    public String getDeviceId(Context context) {

        int permissionCheck = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_PHONE_STATE);

        Activity activity = (Activity) context;
        String deviceId = "";

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_PERMISSIONS);
        } else {
            //TODO
            TelephonyManager telephonyManager = (TelephonyManager)
                    context.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = telephonyManager.getDeviceId();
        }

        return deviceId;
    }

    public String getDeviceId2(Context context) {

        int permissionCheckReadContact = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_CONTACTS);
        int permissionCheckReadPhoneState = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_PHONE_STATE);

        Activity activity = (Activity) context;
        String deviceId = "";

        if (permissionCheckReadPhoneState == PackageManager.PERMISSION_GRANTED &&
                permissionCheckReadContact != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_PHONE_STATE);
        } else if (permissionCheckReadPhoneState != PackageManager.PERMISSION_GRANTED && permissionCheckReadContact == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_READ_PHONE_STATE);
        } else if (permissionCheckReadContact != PackageManager.PERMISSION_GRANTED && permissionCheckReadPhoneState != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE},
                    REQUEST_READ_PHONE_STATE);
        } else {
//TODO
            TelephonyManager telephonyManager = (TelephonyManager)
                    context.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = telephonyManager.getDeviceId();
        }

        return deviceId;
    }

    public String getIMEI(Context context) {

        String IMEI = "";

        int permissionCheck = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_PHONE_STATE);

        Activity activity = (Activity) context;

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_READ_PHONE_STATE);
        } else {
            //TODO
            TelephonyManager telephonyManager = (TelephonyManager)
                    context.getSystemService(Context.TELEPHONY_SERVICE);
            IMEI = telephonyManager.getDeviceId();
        }

//        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        IMEI = "IMEI:"+telephonyManager.getDeviceId();

        return IMEI;
    }

    public static String androidId(Application context){
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static boolean CheckPermission(Context context, String Permission) {
        return ContextCompat.checkSelfPermission(context,
                Permission) == PackageManager.PERMISSION_GRANTED;
    }
}
