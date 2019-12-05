package com.solution.catterpillars.ui.splash;


import android.content.Context;
import android.util.Log;

import com.solution.catterpillars.base.BaseViewModel;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 30-Oct-18
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 * About : I am a mathematician
 * Quote : Only brain can make anything possible
 * Strength : Never give up
 */
public class WelcomeViewModel extends BaseViewModel<WelcomeNavigator> {


    public void checkLogin(Context context) {
        AppPreferencesService appPreferencesService = new
                AppPreferencesService(context.getApplicationContext());

        Log.e("Spalas", String.valueOf(appPreferencesService.isSessionActive()));

        if(appPreferencesService.isSessionActive()){
            String userId = appPreferencesService.getUserId();
            String pass = appPreferencesService.getPassword();
            boolean taskStatus = appPreferencesService.getTaskStatus();
            getNavigator().openHomeActivity(userId, pass, taskStatus);
        }
        else{
            getNavigator().openStartAppActivity();
        }
    }
}