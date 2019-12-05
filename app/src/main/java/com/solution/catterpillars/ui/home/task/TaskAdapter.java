package com.solution.catterpillars.ui.home.task;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.solution.catterpillars.R;
import com.solution.catterpillars.base.BaseAdapter;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.data.remort.APIClient;
import com.solution.catterpillars.data.remort.APIService;
import com.solution.catterpillars.databinding.AdviewItemBinding;
import com.solution.catterpillars.databinding.TaskAdapterBinding;
import com.solution.catterpillars.ui.home.Home;
import com.solution.catterpillars.ui.home.task.model.AppList;
import com.solution.catterpillars.ui.home.task.model.ResultTask;
import com.solution.catterpillars.util.TaskWatcher;
import retrofit2.Call;
import retrofit2.Response;

import static com.solution.catterpillars.util.TaskWatcher.TASK_QUEUE;

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
public class TaskAdapter extends BaseAdapter<TaskAdapterBinding, AppList> implements TaskNavigator {

    private static final String TAG = TaskAdapter.class.getSimpleName();
    private static final int AD_TYPE = 1;
    private static final int CONTENT_TYPE = 2;
    private ResultTask mainData;
    private List<ApplicationInfo> packagesList;
    private final TaskFragment taskFragment;
    private TaskViewModel viewModel;
    private Home currentActivity;
    private int isAlreadyInstalledCount=0;
    private AdviewItemBinding bindingAds;

    /**
     * @param context reference of context
     * @param taskFragment
     * @param list    DataList
     * @param viewModel
     */
    public TaskAdapter(Context context, TaskFragment taskFragment, ArrayList<AppList> list,
                       TaskViewModel viewModel) {
        super(context, list);
        this.taskFragment = taskFragment;
        this.viewModel = viewModel;
        this.currentActivity = (Home) context;
    }

    /**
     * This will hide the view on first load
     * left blank if no view has to hide
     *
     * @param binding DataBinding
     */
    @Override
    protected void hideView(TaskAdapterBinding binding) {
    }

    /**
     * This is use to make the hidden view on first load
     * left blank if noting is hidden
     *
     * @param binding DataBinding
     */
    @Override
    protected void showView(TaskAdapterBinding binding) {

    }

    /**
     * @return R.layout.layout_file
     */
    @Override
    protected int getLayout() {
        return R.layout.task_adapter;
    }


    /**
     * Initialised View Holder
     *
     * @param binding DataBinding
     * @return new ViewHolder(context, this, binding);
     */
    @Override
    public ViewHolder getViewHolder(TaskAdapterBinding binding) {
        return new ViewHolder(binding){
            /**
             * If there is anything to do then do here otherwise leave it blank
             * @param binding layout reference for single view
             * @param data for single view
             * @param position position of ArrayList
             */
            @Override
            protected void doSomeWorkHere(TaskAdapterBinding binding, AppList data, int position) {
                loadTaskForUser(binding, data, position);
            }

            /**
             * @param data     binding.setData(data);
             */
            @Override
            protected void bindData(AppList data) {
                binding.setData(data);
            }

            /**
             * Method to set click listeners on view holder or groups
             *
             * @param thisContext set it on method setOnClickListener(thisContext)
             * @param binding     DataBinding
             * @param data        data
             */
            @Override
            public void setClickListeners(ViewHolderClickListener thisContext,
                                          TaskAdapterBinding binding, AppList data) {
                binding.downloadBtn.setOnClickListener(thisContext);
            }

            /**
             * Initialised holder by new operator
             *
             * @param binding  DataBinding
             * @param data     dataList
             * @param position of adapter
             * @return new ViewHolderClickListener(context, binding, data, position)
             */
            @Override
            protected ViewHolderClickListener viewHolderReference(TaskAdapterBinding binding,
                                                                  AppList data, int position) {
                return new ViewHolderClickListener(binding, position){
                    /**
                     * Called when a view has been clicked.
                     *
                     * @param v The view that was clicked.
                     */
                    @Override
                    public void onClick(View v) {
                        if (context instanceof Home) {
                            ((Home) context).networkErrorViewVisible(View.GONE);
                        }
                        switch (v.getId()) {
                            case R.id.downloadBtn:
                                Log.e(TAG, "Package status 1:" + data.isPackagePresent());

                                if(!data.isPackagePresent()){
                                    //download and install
                                    final String appPackageName = data.getPackagename();
                                    final String appUrl = data.getUrl();

                                    if(appUrl.contains("https://play.google.com/store/apps")) {
                                        try {
                                            context.startActivity(new Intent(Intent.ACTION_VIEW,
                                                    Uri.parse("market://details?id=" + appPackageName)));
                                        } catch (android.content.ActivityNotFoundException anfe) {
                                            context.startActivity(new Intent(Intent.ACTION_VIEW,
                                                    Uri.parse("https://play.google.com/store/apps/details?id=" +
                                                            appPackageName)));
                                        }
                                    }
                                    else{
                                        try {
                                            context.startActivity(new Intent(Intent.ACTION_VIEW,
                                                    Uri.parse(appUrl)));
                                        } catch (android.content.ActivityNotFoundException anfe) {
                                            anfe.printStackTrace();
                                        }
                                    }

                                    //task watcher
                                    if(!TASK_QUEUE.contains(data.getTaskid())) {
                                        new Thread(new TaskWatcher(context, TaskAdapter.this,
                                                appPackageName,
                                                currentActivity.preferencesService.getUserId(),
                                                data.getTaskid(),
                                                mainData.getTaskName(),
                                                data.getSetid())).start();
                                    }
                                }
                                break;
                        }
                    }
                };
            }
        };
    }

    /**
     * @return new FilterClass();
     */
    @Override
    protected FilterClass initialisedFilterClass() {
        return null;
    }

    public void setValues(ResultTask data) {
        this.mainData = data;
    }

    private void loadTaskForUser(TaskAdapterBinding binding, AppList data, int position) {
        Log.e(TAG, "Load time---------------------");
        if(!data.isPackagePresent()) {
            String packageName = data.getPackagename();
            if (isPackageExit(packageName)) {
                filterList.get(position).setPackagePresent(true);
                // Log.e(TAG, "L =====================" + packageInfo.packageName);
                Log.e(TAG, "L --------------------- " + packageName);

                binding.downloadBtn.setBackgroundDrawable(context.getResources().
                        getDrawable(R.drawable.rounded_button_unselected));
                binding.downloadBtn.setText("App Installed");
                //ok = true;
                isAlreadyInstalledCount++;
                //send task completion data to server
//                    new Thread(new TaskCompletion(TaskAdapter.this, "2")).start();
                //1 if App is already installed
                sendToServer(currentActivity.preferencesService.getUserId(),
                        data.getTaskid(),
                        mainData.getTaskName(),
                        data.getSetid(),
                        1
                );
            }
        }
        /*final PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        boolean ok = false;
        String packageName = data.getPackagename();
        if(!data.isPackagePresent()) {
            for (int i = 0; i < packages.size() && !ok; i++) {
                ApplicationInfo packageInfo = packages.get(i);

                if (packageInfo.packageName.equals(packageName)) {
                    list.get(position).setPackagePresent(true);
                    Log.e(TAG, "L =====================" + packageInfo.packageName);
                    Log.e(TAG, "L --------------------- " + packageName);

                    binding.downloadBtn.setBackgroundDrawable(context.getResources().
                            getDrawable(R.drawable.rounded_button_unselected));
                    ok = true;
                    inAlreadyInstalledCount++;
                    //send task completion data to server
//                    new Thread(new TaskCompletion(TaskAdapter.this, "2")).start();
                    //1 if App is already installed
                    sendToServer(currentActivity.preferencesService.getUserId(),
                            data.getTaskid(),
                            mainData.getTaskName(),
                            data.getSetid(),
                            1
                    );

//                    new Thread(new TaskCompletion(context,TaskAdapter.this,
//                            "2",
//                            currentActivity.preferencesService.getUserId(),
//                            data.getTaskid(),
//                            mainData.getTaskName(),
//                            data.getSetid())).start();
                }
            }


        }*/
        else{
            binding.downloadBtn.setBackgroundDrawable(context.getResources().
                    getDrawable(R.drawable.rounded_button_unselected));
            binding.downloadBtn.setText("App Installed");
            Log.e(TAG, "L ******************** " + data.getPackagename());
        }

        if(isAlreadyInstalledCount==filterList.size()){
            taskFragment.nextButtonVisibility(View.VISIBLE);
            AppPreferencesService pref =
                    new AppPreferencesService(context);
            pref.setTaskStatus(true);
        }
    }


    private boolean isPackageExit(String packageName){
        PackageManager pm = context.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(packageName,PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }

        return app_installed;
    }
    /**
     * This method will only be called when the take will be completed
     *
     * @param params contains values in String Array format
     */
    @Override
    public void OnTaskComplete(String... params) {
        Log.e(TAG, "OnTaskComplete");

        //this will only call from TaskCompletion if all task will completed otherwise not
        //Here over all task completion status must be 100
        if(params.length > 1 && params[0].equals("100")){
            Log.e(TAG, "enable btn");
            AppPreferencesService pref = new AppPreferencesService(context);
            pref.setTaskStatus(true);
           // taskFragment.binding.taskCompleteBtn.setVisibility(View.VISIBLE);
            taskFragment.nextButtonVisibility(View.VISIBLE);

            Log.e(TAG, "OnTaskComplete 1");
        }

        if(params.length > 1 && params[0].equals("200")){
            String taskId = params[1];

            for(int i = 0; i < filterList.size(); i++){
                if(filterList.get(i).getTaskid().equals(taskId)){
                    filterList.get(i).setPackagePresent(true);
                }
            }

            Log.e(TAG, "OnTaskComplete 2");
        }


        taskFragment.taskChecking();
    }

    private void sendToServer(String userId, String taskId, String taskName, String setId, int isAlreadyInstalled){
        APIService apiService = APIClient.getConnect().create(APIService.class);
        Call<ResultTask> call = null;// = apiService.checkSheetDetails(module, "", "");

        call = apiService.responseTaskComplete(userId, taskId, taskName, setId, isAlreadyInstalled);
        Log.e(TAG, "connecting mnm");

        if (call != null) {
            call.enqueue(new retrofit2.Callback<ResultTask>() {
                @Override
                public void onResponse(@NonNull Call<ResultTask> call,
                                       @NonNull Response<ResultTask> response) {
                    ResultTask data = response.body();
                    if(data != null) {
                        //String overAllStatus = "0", for all task completion;
                        if(data.getAppStatus().equals("0")) {
                            AppPreferencesService pref =
                                    new AppPreferencesService(context);
                            pref.setTaskStatus(true);

                            Log.e(TAG, "Already completed task send " + isAlreadyInstalled);
                            taskFragment.nextButtonVisibility(View.VISIBLE);
                            //taskFragment.binding.taskCompleteBtn.setVisibility(View.VISIBLE);
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
