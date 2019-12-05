package com.solution.catterpillars.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.List;

import com.solution.catterpillars.ui.home.dashboard.task.task_detail.TaskScreen;
import com.solution.catterpillars.ui.home.dashboard.task.task_detail.TaskScreenNavigator;
import com.solution.catterpillars.ui.home.task.TaskAdapterMain;

import static com.solution.catterpillars.ui.home.dashboard.task.task_detail.TaskScreen.TASK_COMPLETED;

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
public class TaskWatcher implements Runnable {

    private static final String TAG = TaskWatcher.class.getSimpleName();
    private static WeakReference<Context> context;
    private static WeakReference<Object> context2;
    private final String packageName;
    public static HashSet<String> TASK_QUEUE = new HashSet<>();

    private TaskScreenNavigator taskScreenNavigator;

    private final String taskId;
    private String userId;
    private String taskName;
    private String setId;

    public TaskWatcher(Context context1, Object context3, String packageName, String taskId){
        context = new WeakReference<>(context1);
        context2 = new WeakReference<>(context3);
        this.packageName = packageName;
        TASK_QUEUE.add(taskId);
        this.taskId = taskId;
    }

    public TaskWatcher(Context context1, Object context3, String packageName,
                       String userId, String taskId, String taskName, String setId){
        context = new WeakReference<>(context1);
        context2 = new WeakReference<>(context3);
        this.packageName = packageName;
        this.userId = userId;
        this.taskName = taskName;
        this.setId = setId;
        this.taskId = taskId;
        TASK_QUEUE.add(taskId);
    }

    public TaskWatcher(Context context1, Fragment fragment, String packageName, String taskId) {
        context = new WeakReference<>(context1);
        context2 = new WeakReference<>(fragment);
        this.packageName = packageName;
        this.taskId = taskId;
        TASK_QUEUE.add(taskId);
    }

    @Override
    public void run() {

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean ok = false;
        while (context.get() != null && !ok && AppsFunction.isAppRunning(context.get(),
                ApplicationConstant.APP_PACKAGE_NAME)){

            final PackageManager pm = context.get().getPackageManager();
            List<ApplicationInfo> packages =
                    pm.getInstalledApplications(PackageManager.GET_META_DATA);

            for(int i = 0; i < packages.size() && !ok; i++) {
                ApplicationInfo packageInfo = packages.get(i);

                if (packageInfo.packageName.equals(packageName)) {
                    Log.e(TAG, "Installed package P: " + packageInfo.packageName);
                    ok = true;

                    //send task completion data to server
//                    new Thread(new TaskCompletion(context2, "200", taskId)).start();

                    /*if(context2.get() instanceof TaskAdapterMain){
                        Log.e(TAG, "instance of TaskAdapterMain true");
                    }

                    new Thread(new TaskCompletion(context.get(), context2.get(),
                            "3",
                            userId,
                            taskId,
                            taskName,
                            setId)).start();*/

                    if(context2.get() != null){
                        this.taskScreenNavigator = (TaskScreenNavigator) context2.get();
                        taskScreenNavigator.onFragmentResume(TASK_COMPLETED);
                    }
                }
            }


            Log.e(TAG, "to install package: " + packageName);

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(!ok){
            Log.e(TAG, "Loop terminate " + ok);
        }
        if(context.get() != null){
            Log.e(TAG, "Loop terminate " + "context.get()");
        }
        if(AppsFunction.isAppRunning(context.get(),
                ApplicationConstant.APP_PACKAGE_NAME)){
            Log.e(TAG, "Loop terminate " + "isAppRunning");
        }

        TASK_QUEUE.remove(taskId);

        Log.e(TAG, "Loop terminate " + packageName);
    }


}