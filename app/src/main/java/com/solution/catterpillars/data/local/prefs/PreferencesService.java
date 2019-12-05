package com.solution.catterpillars.data.local.prefs;


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
public interface PreferencesService {
    void setLauncher(boolean value);

    boolean getLauncher();

    String getUserId();

    void setUserId(String userId);

    String getPassword();

    void setPassword(String password);

    boolean isSessionActive();

    boolean getTaskStatus();

    void setTaskStatus(Boolean taskStatus);

    void setSessionID(String sessionID);

    String getCountryCode();

    void setCountryCode(String code);

    String getSessionID();

    void setUName(String uName);

    String getUName();

    void setUMobile(String UMobile);

    String getUMobile();

    void setKey(String Key);

    String getKey();

    void setIcon(String icon);

    String getIcon();

    void setUserReferenceCode(String reference);

    String getUserReferenceCode();


    void setEmail(String email);

    String getEmail();

    void setEmailDevice(String email);

    String getEmailDevice();

    void setLockScreenData(String data);

    String getLockScreenData();

    void setLockScreenDataAvailable(boolean value);

    boolean isLockScreenDataAvailable();

    void setLockScreenOnOff(boolean value);

    boolean isLockScreenOn();

    void setLocationCapture(boolean value);

    boolean isLocationCapture();

    String getTaskData();

    void setTaskData(String data);

    String getDashboardData();

    void setDashboardData(String data);

    String getFlagData();

    void setFlagData(String data);

    boolean isPreviousTaskCompleted();

    void setTaskCompleted(boolean value);

    int getDailyTaskDate();

    void setDailyTaskDate(int date);

    String getDate();

    void setDate(String date);

    String getDateLockEnable();

    void setDateLockEnable(String date);

    String getUserProfileData();

    void setUserProfileData();

     String getSendMobileVerifyLinkId() ;


     void setSendMobileVerifyLinkId(String id);


    boolean getUplineContact();

    void setUplineContact(boolean value);

    boolean getDownlineContact();

    void setDownlineContact(boolean value);


    String getUplineName();

    void setUplineName(String value);

    String getUplineEmail();

    void setUplineEmail(String value);

    String getUplineMobile();

    void setUplineMobile(String value);


    String getAffiletdLink1();

    void setAffiletdLink1(String value);

    String getAffiletdLink2();

    void setAffiletdLink2(String value);

   /* int getAlreadyVisibleAds();

    void setAlreadyVisibleAds(int value);*/
}
