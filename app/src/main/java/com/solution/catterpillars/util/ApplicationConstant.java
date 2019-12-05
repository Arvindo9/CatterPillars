package com.solution.catterpillars.util;


import com.solution.catterpillars.BuildConfig;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 02-Nov-18
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 * About : I am a mathematician
 * Quote : Only brain can make anything possible
 * Strength : Never give up
 */
public enum  ApplicationConstant {
    INSTANCE;

    public static final String BASE_URL = BuildConfig.BASE_URL ;
    public static final String BASE_URL_UP_50 = "https://up50.in";
    public static final String TOPIC_SUBSCRIPTION_LOCKSCREEN = "TOPIC_SUBSCRIPTION_LOCKSCREEN";
    public final String WEB_URL = "catterpillars";
    public static final String APP_PACKAGE_NAME = "com.solution.catterpillars";
    public static boolean TASK_COMPLETION = false;

    public static final String UP50_PACKAGE_NAME = "com.solution.up50";


   public final String ADMOB_APP_ID = BuildConfig.DEBUG?"ca-app-pub-3940256099942544~3347511713":"ca-app-pub-8423837804323248~2937034279";
    public final String FB_ADS_PLACEMENT_ID=BuildConfig.DEBUG?"YOUR_PLACEMENT_ID":"2189559427977049_2208359526097039";
    //public final String ADMOB_APP_ID = "ca-app-pub-8423837804323248~2937034279";

    public static final int PICK_CONTACT = 1000;
    public static final int REQUEST_CHECK_SETTINGS = 1001;

    public static final String ACCESS_TOKEN = "ba30bfcb690cfc5ce3bc0ffe8bb554d1";
}
