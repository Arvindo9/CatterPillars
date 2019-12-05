package com.solution.catterpillars.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import java.lang.ref.WeakReference;

import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.data.remort.APIClient;
import com.solution.catterpillars.data.remort.APIService;
import com.solution.catterpillars.ui.home.task.TaskAdapterMain;
import com.solution.catterpillars.ui.home.task.TaskNavigator;
import com.solution.catterpillars.ui.home.task.model.ResultTask;
import retrofit2.Call;
import retrofit2.Response;

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
public class TaskCompletion implements Runnable {
    private static WeakReference<Object> context = null;
    private final WeakReference<Context> activityContext;
    private String[] params;
    private static final String TAG = TaskCompletion.class.getSimpleName();
    private String userId;
    private String taskId;
    private String taskName;
    private String setId;

    public TaskCompletion(Context context2, Object context1, String... params){
        context = new WeakReference<>(context1);
        activityContext = new WeakReference<>(context2);
        this.params = params;
        if(params.length>4){
            this.userId = params[1];
            this.taskId = params[2];
            this.taskName = params[3];
            this.setId = params[4];
        }
    }


    @Override
    public void run() {
        APIService apiService = APIClient.getConnect().create(APIService.class);
        Call<ResultTask> call = null;// = apiService.checkSheetDetails(module, "", "");

        call = apiService.responseTaskComplete(userId, taskId, taskName, setId, 0);
        Log.e(TAG, params[0] + "connecting ");

        if (call != null) {
            call.enqueue(new retrofit2.Callback<ResultTask>() {
                @Override
                public void onResponse(@NonNull Call<ResultTask> call,
                                       @NonNull Response<ResultTask> response) {
                    ResultTask data = response.body();
                    if(data != null) {
                        //String overAllStatus = "0", for all task completion;
                        if(data.getAppStatus().equals("0")) {
                            if (context != null && context.get() != null) {
                                if (context.get() instanceof TaskAdapterMain) {
                                    params = new String[2];
                                    params[0] = "100";
                                    TaskNavigator taskNavigator = (TaskNavigator) context.get();
                                    taskNavigator.OnTaskComplete(params);
                                }

                                ApplicationConstant.TASK_COMPLETION = true;
                                AppPreferencesService pref =
                                        new AppPreferencesService(activityContext.get());
                                pref.setTaskStatus(true);

                                Log.e(TAG, " connecting to completion 0");
                            }
                        }

                        //if overAllStatus = 1, and status 1 means single task completed but not complete task,
                        //update the adapter class UI
                        //send param 200
                        //adapter OnTaskComplete
                        else if(data.getUpdateStatus().equals("1")) {
                            if (context != null && context.get() != null) {
                                if (context.get() instanceof TaskAdapterMain) {
                                    params = new String[2];
                                    params[0] = "200";
                                    params[1] = taskId;
                                    TaskNavigator taskNavigator = (TaskNavigator) context.get();
                                    taskNavigator.OnTaskComplete(params);

                                    Log.e(TAG, params[0] + "connecting to single completion ");
                                    Log.e(TAG, "Recentely completed task send 0");
                                }

                                if(context.get() instanceof TaskAdapterMain){
                                    Log.e(TAG, "instance of TaskAdapterMain true");
                                }
                            }
                        }

                        Log.e(TAG, "Task completion:" + data.getAppStatus());
                        Log.e(TAG, "Task completion:" + data.getUpdateStatus());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResultTask> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    call.cancel();
                }
            });
        }
    }

}
