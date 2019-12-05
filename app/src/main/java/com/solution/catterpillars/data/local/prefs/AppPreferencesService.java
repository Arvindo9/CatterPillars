package com.solution.catterpillars.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;

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
public class AppPreferencesService implements PreferencesService {

    private static final String PREF_KEY_CURRENT_USER_ID = "PREF_KEY_CURRENT_USER_ID";
    private static final String PREF_KEY_CURRENT_USER_PASSWORD = "PREF_KEY_CURRENT_USER_PASSWORD";
    private static final String PREF_KEY_SESSON_OK  = "PREF_KEY_SESSION";
    private static final String PREF_KEY_TASK  = "PREF_KEY_TASK";
    private static final String PREF_KEY_SESSION_ID = "PREF_KEY_SESSION_ID";
    private static final String PREF_KEY_U_MOBILE = "PREF_KEY_U_MOBILE";
    private static final String PREF_KEY_U_NAME  = "PREF_KEY_U_NAME";
    private static final String PREF_KEY_KEY  = "PREF_KEY_KEY";
    private static final String PREF_KEY_ICON  = "PREF_KEY_ICON";
    private static final String PREF_KEY_USER_REFERENCE_CODE  = "PREF_KEY_USER_REFERENCE_CODE";
    private static final String PREF_KEY_EMAIL  = "PREF_KEY_EMAIL";
    private static final String PREF_KEY_LAUNCHER  = "PREF_KEY_LAUNCHER";
    private static final String PREF_ACCOUNT_NAME  = "PREF_ACCOUNT_NAME";
    private static final String PREF_LOCK_SCREEN  = "PREF_LOCK_SCREEN";
    private static final String PREF_LOCK_SCREEN_DATA_AVAILABLE  = "PREF_LOCK_SCREEN_DATA_AVAILABLE";
    private static final String PREF_LOCK_SCREEN_ON_OFF  = "PREF_LOCK_SCREEN_ON_OFF";
    private static final String PREF_COUNTRY_CODE  = "PREF_COUNTRY_CODE";
    private static final String PREF_SEND_MOBILE_VERIFY_LINK_ID  = "PREF_SEND_MOBILE_VERIFY_LINK_ID";
    private static final String PREF_LOCATION_CAPTURE  = "PREF_LOCATION_CAPTURE";
    private static final String PREF_Should_LOAD_NEW_TASK  = "PREF_Should_LOAD_NEW_TASK";
    private static final String PREF_TASK_DATA  = "PREF_TASK_DATA";
    private static final String PREF_DASHBOARD_DATA  = "PREF_DASHBOARD_DATA";
    private static final String PREF_FLAG_DATA  = "PREF_FLAG_DATA";
    private static final String PREF_DATA  = "PREF_DATA";
    private static final String PREF_UPLINE_CONTACT  = "PREF_UPLINE_CONTACT";
    private static final String PREF_DOWNLINE_CONTACT  = "PREF_DOWNLINE_CONTACT";
    private static final String PREF_UPLINE_EMAIL  = "PREF_UPLINE_EMAIL";
    private static final String PREF_UPLINE_PHONE  = "PREF_UPLINE_PHONE";
    private static final String PREF_AFFILETED_LINK_1  = "PREF_AFFILETED_LINK_1";
    private static final String PREF_AFFILETED_LINK_2  = "PREF_AFFILETED_LINK_2";
    private static final String PREF_ALREADY_VISIBLE_ADS  = "PREF_ALREADY_VISIBLE_ADS";
    private static final String PREF_UPLINE_NAME  = "PREF_UPLINE_NAME";
    private static final String PREF_DATE_LOCK_ENABLE  = "PREF_DATE_LOCK_ENABLE";
    private static final String PREF_DAILY_TASK_DATE  = "PREF_DAILY_TASK_DATE";
    private static final String PREF_USER_PROFILE_DATA = "PREF_USER_PROFILE_DATA";

    private final SharedPreferences sharedPref;

    private final Context context;

    private static final String PREF_KEY = "USER_DATA_PREF";

    public AppPreferencesService(Context context){
        this.context = context;
        sharedPref = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
    }

//    public AppPreferencesService(Context context, @PreferenceInfo String prefFileName) {
//        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
//    }

    @Override
    public void setLauncher(boolean value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(PREF_KEY_LAUNCHER, value);
        editor.apply();
        editor.commit();
    }

    @Override
    public boolean getLauncher() {
        return sharedPref.getBoolean(PREF_KEY_LAUNCHER, false);
    }

    @Override
    public String getUserId() {
        return sharedPref.getString(PREF_KEY_CURRENT_USER_ID, "");
    }

    @Override
    public void setUserId(String userId) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_KEY_CURRENT_USER_ID, userId);
        editor.apply();
        editor.commit();
    }

    @Override
    public String getPassword() {
        return sharedPref.getString(PREF_KEY_CURRENT_USER_PASSWORD, "");
    }


    @Override
    public void setPassword(String password) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_KEY_CURRENT_USER_PASSWORD, password);
        editor.apply();
        editor.commit();
    }

    public void setSession(boolean session){
//        Activity activity = (Activity) context;
//        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(PREF_KEY_SESSON_OK, session);
        editor.apply();
        editor.commit();
    }

    @Override
    public boolean isSessionActive() {
        return sharedPref.getBoolean(PREF_KEY_SESSON_OK, false);
    }

    @Override
    public boolean getTaskStatus() {
        return sharedPref.getBoolean(PREF_KEY_TASK, false);
    }

    @Override
    public void setTaskStatus(Boolean taskStatus) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(PREF_KEY_TASK, taskStatus);
        editor.apply();
        editor.commit();
    }

    @Override
    public void setSessionID(String sessionID) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_KEY_SESSION_ID, sessionID);
        editor.apply();
        editor.commit();
    }

    @Override
    public String getCountryCode() {
        return sharedPref.getString(PREF_COUNTRY_CODE, "");
    }

    @Override
    public void setCountryCode(String code) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_COUNTRY_CODE, code);
        editor.apply();
        editor.commit();
    }

    @Override
    public String getSendMobileVerifyLinkId() {
        return sharedPref.getString(PREF_SEND_MOBILE_VERIFY_LINK_ID, "");
    }

    @Override
    public void setSendMobileVerifyLinkId(String id) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_SEND_MOBILE_VERIFY_LINK_ID, id);
        editor.apply();
        editor.commit();
    }

    @Override
    public boolean getUplineContact() {
        return sharedPref.getBoolean(PREF_UPLINE_CONTACT, false);
    }

    @Override
    public void setUplineContact(boolean value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(PREF_UPLINE_CONTACT, value);
        editor.apply();
        editor.commit();
    }

    @Override
    public boolean getDownlineContact() {
        return sharedPref.getBoolean(PREF_DOWNLINE_CONTACT, false);
    }

    @Override
    public void setDownlineContact(boolean value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(PREF_DOWNLINE_CONTACT, value);
        editor.apply();
        editor.commit();
    }

    @Override
    public String getUplineName() {
        return sharedPref.getString(PREF_UPLINE_NAME, "");
    }

    @Override
    public void setUplineName(String value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_UPLINE_NAME, value);
        editor.apply();
        editor.commit();
    }

    @Override
    public String getUplineEmail() {
        return sharedPref.getString(PREF_UPLINE_EMAIL, "");
    }

    @Override
    public void setUplineEmail(String value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_UPLINE_EMAIL, value);
        editor.apply();
        editor.commit();
    }

    @Override
    public String getUplineMobile() {
        return sharedPref.getString(PREF_UPLINE_PHONE, "");
    }

    @Override
    public void setUplineMobile(String value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_UPLINE_PHONE, value);
        editor.apply();
        editor.commit();
    }

    @Override
    public String getAffiletdLink1() {
        return sharedPref.getString(PREF_AFFILETED_LINK_1, "");
    }

    @Override
    public void setAffiletdLink1(String value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_AFFILETED_LINK_1, value);
        editor.apply();
        editor.commit();
    }

    @Override
    public String getAffiletdLink2() {
        return sharedPref.getString(PREF_AFFILETED_LINK_2, "");
    }

    @Override
    public void setAffiletdLink2(String value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_AFFILETED_LINK_2, value);
        editor.apply();
        editor.commit();
    }

   /* @Override
    public int getAlreadyVisibleAds() {
        return sharedPref.getInt(PREF_ALREADY_VISIBLE_ADS, 1);
    }

    @Override
    public void setAlreadyVisibleAds(int value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(PREF_ALREADY_VISIBLE_ADS, value);
        editor.apply();
        editor.commit();
    }*/

    @Override
    public String getSessionID() {
        return sharedPref.getString(PREF_KEY_SESSION_ID, "");
    }

    @Override
    public void setUName(String uName) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_KEY_U_NAME, uName);
        editor.apply();
        editor.commit();
    }

    @Override
    public String getUName() {
        return sharedPref.getString(PREF_KEY_U_NAME, "");
    }

    @Override
    public void setUMobile(String UMobile) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_KEY_U_MOBILE, UMobile);
        editor.apply();
        editor.commit();
    }

    @Override
    public String getUMobile() {
        return sharedPref.getString(PREF_KEY_U_MOBILE, "");
    }

    @Override
    public void setKey(String Key) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_KEY_KEY, Key);
        editor.apply();
        editor.commit();
    }

    @Override
    public String getKey() {
        return sharedPref.getString(PREF_KEY_KEY, "");
    }

    @Override
    public void setIcon(String icon) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_KEY_ICON, icon);
        editor.apply();
        editor.commit();
    }

    @Override
    public String getIcon() {
        return sharedPref.getString(PREF_KEY_ICON, "");
    }

    @Override
    public void setUserReferenceCode(String reference) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_KEY_USER_REFERENCE_CODE, reference);
        editor.apply();
        editor.commit();
    }

    @Override
    public String getUserReferenceCode() {
        return sharedPref.getString(PREF_KEY_USER_REFERENCE_CODE, "");
    }

    @Override
    public void setEmail(String email) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_KEY_EMAIL, email);
        editor.apply();
        editor.commit();
    }

    @Override
    public String getEmail() {
        return sharedPref.getString(PREF_KEY_EMAIL, "");
    }

    @Override
    public void setEmailDevice(String email) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_ACCOUNT_NAME, email);
        editor.apply();
        editor.commit();
    }

    @Override
    public String getEmailDevice() {
        return sharedPref.getString(PREF_ACCOUNT_NAME, "");
    }

    @Override
    public void setLockScreenData(String data) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_LOCK_SCREEN, data);
        editor.apply();
        editor.commit();
    }

    @Override
    public String getLockScreenData() {
        return sharedPref.getString(PREF_LOCK_SCREEN, "");
    }

    @Override
    public void setLockScreenDataAvailable(boolean value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(PREF_LOCK_SCREEN_DATA_AVAILABLE, value);
        editor.apply();
        editor.commit();
    }

    @Override
    public boolean isLockScreenDataAvailable() {
        return sharedPref.getBoolean(PREF_LOCK_SCREEN_DATA_AVAILABLE, false);
    }

    @Override
    public void setLockScreenOnOff(boolean value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(PREF_LOCK_SCREEN_ON_OFF, value);
        editor.apply();
        editor.commit();
    }

    @Override
    public boolean isLockScreenOn() {
        return sharedPref.getBoolean(PREF_LOCK_SCREEN_ON_OFF, false);
    }

    @Override
    public void setLocationCapture(boolean value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(PREF_LOCATION_CAPTURE, value);
        editor.apply();
        editor.commit();
    }

    @Override
    public boolean isLocationCapture() {
        return sharedPref.getBoolean(PREF_LOCATION_CAPTURE, false);
    }

    @Override
    public String getTaskData() {
        return sharedPref.getString(PREF_TASK_DATA, "");
    }

    @Override
    public void setTaskData(String data) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_TASK_DATA, data);
        editor.apply();
        editor.commit();
    }


    @Override
    public String getDashboardData() {
        return sharedPref.getString(PREF_DASHBOARD_DATA, "");
    }

    @Override
    public void setDashboardData(String data) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_DASHBOARD_DATA, data);
        editor.apply();
        editor.commit();
    }

    @Override
    public String getFlagData() {
        return sharedPref.getString(PREF_FLAG_DATA, "");
    }

    @Override
    public void setFlagData(String data) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_FLAG_DATA, data);
        editor.apply();
        editor.commit();
    }

    @Override
    public boolean isPreviousTaskCompleted() {
        return sharedPref.getBoolean(PREF_Should_LOAD_NEW_TASK, true);
    }

    @Override
    public void setTaskCompleted(boolean value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(PREF_Should_LOAD_NEW_TASK, value);
        editor.apply();
        editor.commit();
    }

    @Override
    public int getDailyTaskDate() {
        return sharedPref.getInt(PREF_DAILY_TASK_DATE, 0);
    }

    @Override
    public void setDailyTaskDate(int date) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(PREF_DAILY_TASK_DATE, date);
        editor.apply();
        editor.commit();
    }

    @Override
    public String getDate() {
        return sharedPref.getString(PREF_DATA, "20180101");
    }

    @Override
    public void setDate(String date) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_DATA, date);
        editor.apply();
        editor.commit();
    }

    @Override
    public String getDateLockEnable() {
        return sharedPref.getString(PREF_DATE_LOCK_ENABLE, "20180101");
    }

    @Override
    public void setDateLockEnable(String date) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_DATE_LOCK_ENABLE, date);
        editor.apply();
        editor.commit();
    }

    @Override
    public String getUserProfileData() {
        return sharedPref.getString(PREF_USER_PROFILE_DATA, "");
    }

    @Override
    public void setUserProfileData() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_DATA, PREF_USER_PROFILE_DATA);
        editor.apply();
        editor.commit();
    }
}