package com.solution.catterpillars.ui.home.task;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.solution.catterpillars.base.BaseViewModel;
import com.solution.catterpillars.data.remort.APIClient;
import com.solution.catterpillars.data.remort.APIService;
import com.solution.catterpillars.ui.home.task.model.AppList;

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
public class TaskViewModel extends BaseViewModel<TaskNavigator> {

    private static final String TAG = TaskViewModel.class.getSimpleName();
    private APIService apiService;

    public TaskViewModel(APIService apiService){
        this.apiService = APIClient.getConnect().create(APIService.class);
    }

    public TaskViewModel() {

    }


    public void installedPackageList(Context context, TaskAdapterMain adapter, ArrayList<AppList> list) {
        new Thread(new InstalledPackageList(context, adapter, list)).start();
    }


    private class InstalledPackageList implements Runnable{
        private Context context;
        private TaskAdapterMain adapter;
        private ArrayList<AppList> list;

        public InstalledPackageList(Context context, TaskAdapterMain adapter, ArrayList<AppList> list) {
            this.context = context;
            this.adapter = adapter;
            this.list = list;
        }

        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            final PackageManager pm = context.getPackageManager();
            List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

            for (ApplicationInfo packageInfo : packages) {
                Log.d(TAG, "Installed package :" + packageInfo.packageName);
                Log.d(TAG, "Source dir : " + packageInfo.sourceDir);

                for(int i = 0; i < list.size(); i++) {
                    String packageName = list.get(i).getPackagename();

                    if(packageInfo.packageName.equals(packageName)){
                        list.get(i).setPackagePresent(true);
                        Log.e(TAG, "Installed package P: " + packageInfo.packageName);
                        Log.e(TAG, "TaskWatcher package P: " + packageName);
                    }
                }
            }

//            TaskNavigator.LoadPackage dd4d = (TaskNavigator.LoadPackage) adapter;
//            TaskNavigator.LoadPackage adapterLoadPackage = adapter;
//            adapterLoadPackage.onPackageLoad(packages);

        }
    }
}
