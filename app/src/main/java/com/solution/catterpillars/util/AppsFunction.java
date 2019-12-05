package com.solution.catterpillars.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.solution.catterpillars.ui.lockScreen.LockScreenApisHandler;
import com.solution.catterpillars.ui.lockScreen.LockScreen;

import java.util.List;

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
public class AppsFunction {

    public static final String TAG = AppsFunction.class.getSimpleName();

    public static boolean isAppRunning(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null)
        {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Loading lock screen Ads, if active
     */
    public static void loadLockScreenAds( Context context, String... params) {
        if(LockScreen.getInstance().isActive()){
            new Thread(new LockScreenApisHandler(context, params[0])).start();
            Log.e(TAG, "LockScreen download active");
        }
        else{
            Log.e(TAG, "LockScreen download not active");
        }
    }

    /*public static void launchNewApp(Context context, String packageName, String... params) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + packageName));
            intent.setData(Uri.parse(params[1]));
        }
        intent.putExtra("ActionLink", params[1]);
        intent.putExtra("key", params[0]);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }*/

    public static void launchNewApp(Context context, String packageName, String parameter,
                                    String key, String url) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + packageName));
            intent.putExtra("ActionLink", parameter);
        } else {
            if (parameter != null && !parameter.isEmpty()) {
                intent = new Intent("Up50_News_Detail");
                intent.putExtra("ActionLink", parameter);
            } else {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));

            }
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public static void launchNewApp(Context context, String packageName, String parameter) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + packageName));
//            intent.putExtra("ActionLink", parameter);
        } else {
            intent = new Intent("Up50_News_Home");
//            intent.putExtra("ActionLink", parameter);
        }
        intent.putExtra("ActionLink", parameter);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void getContactBook(Activity context){
//        Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
//        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // S
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(Uri.parse("content://contacts"),
                ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        context.startActivityForResult(intent, ApplicationConstant.PICK_CONTACT);
    }
}
