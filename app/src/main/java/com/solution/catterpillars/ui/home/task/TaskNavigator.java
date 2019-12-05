package com.solution.catterpillars.ui.home.task;


import android.content.pm.ApplicationInfo;

import java.util.List;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 12-Nov-18
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 * About : I am a mathematician
 * Quote : Only brain can make anything possible
 * Strength : Never give up
 */
public interface TaskNavigator {

    /**
     * This method will only be called when the take will be completed
     * @param params contains values in String Array format
     */
    void OnTaskComplete(String... params);


    interface LoadPackage{
        /**
         *
         * @param packagesList contains the list of packages installed in system
         */
        void onPackageLoad(List<ApplicationInfo> packagesList);
    }
}
