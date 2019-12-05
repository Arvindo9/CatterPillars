package com.solution.catterpillars.ui.home.task;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.solution.catterpillars.R;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.data.remort.APIClient;
import com.solution.catterpillars.data.remort.APIService;
import com.solution.catterpillars.databinding.AdviewItemBinding;
import com.solution.catterpillars.databinding.TaskAdapterBinding;
import com.solution.catterpillars.ui.home.Home;
import com.solution.catterpillars.ui.home.task.model.AppList;
import com.solution.catterpillars.ui.home.task.model.ResultTask;
import com.solution.catterpillars.util.TaskWatcher;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.solution.catterpillars.util.TaskWatcher.TASK_QUEUE;

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
public class TaskAdapterMain extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements TaskNavigator {

    private static final String TAG = TaskAdapterMain.class.getSimpleName();
    private static final int AD_TYPE = 1;
    private static final int CONTENT_TYPE = 2;
    public Context context;
    private ResultTask mainData;
    private List<ApplicationInfo> packagesList;
    private final TaskFragment taskFragment;
    private ArrayList<AppList> list;
    private TaskViewModel viewModel;
    private Home currentActivity;
    private int isAlreadyInstalledCount=0;
    private AdviewItemBinding bindingAds;
    private TaskAdapterBinding binding;

    public TaskAdapterMain(Context context, TaskFragment taskFragment, ArrayList<AppList> list,
                           TaskViewModel viewModel) {
        this.context = context;
        this.taskFragment = taskFragment;
        this.list = list;
        this.viewModel = viewModel;
        this.currentActivity = (Home) context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == AD_TYPE){
                AdView adview = new AdView(context);
                adview.setAdSize(AdSize.LARGE_BANNER);

                // this is the good adview
                adview.setAdUnitId(context.getString(R.string.adsBannerSplash));

                float density = context.getResources().getDisplayMetrics().density;
                int height = Math.round(AdSize.LARGE_BANNER.getHeight() * density);
                AbsListView.LayoutParams params = new
                        AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, height);
                adview.setLayoutParams(params);

                // dont use below if testing on a device
                // follow https://developers.google.com/admob/android/quick-start?hl=en to setup testing device
                AdRequest request = new AdRequest.Builder().build();
                adview.loadAd(request);
                return new AdviewHolder(adview);
        }
        else {
            binding = DataBindingUtil.inflate(inflater,
                    R.layout.task_adapter, parent, false);

            // set the view's size, margins, paddings and layout parameters
            return new ViewHolder(binding);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
       // holder.bind(list.get(position), position);
        if(holder instanceof ViewHolder){
            ((ViewHolder)holder).bind(list.get(position), position);
        }
        else{
            ((AdviewHolder)holder).bind(null, position);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position){
        if (list.get(position) == null){
            return AD_TYPE;
        }
        return CONTENT_TYPE;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TaskAdapterBinding binding;
        private int position;

        ViewHolder(@NonNull TaskAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(AppList data, int position) {
            this.position = position;
            binding.setData(data);
            setClickListeners(data, position);
            doSomeWorkHere(binding, data, position);
        }

        void setClickListeners(AppList data, int position) {
            ViewHolderClickListener viewHolderClickListener =
                    viewHolderReference(binding, data, position);
            setClickListeners(viewHolderClickListener, binding, data);
        }

        void setClickListeners(ViewHolderClickListener thisContext,
                                      TaskAdapterBinding binding, AppList data){
            binding.downloadBtn.setOnClickListener(thisContext);
        }


        ViewHolderClickListener viewHolderReference(TaskAdapterBinding binding,
                                                                       AppList data, int position){
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
                                    new Thread(new TaskWatcher(context, TaskAdapterMain.this,
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

        public abstract class ViewHolderClickListener implements View.OnClickListener{
            protected final int position;
            protected TaskAdapterBinding binding;

            /**
             *
             * @param position of a adapter in current view
             */
            public ViewHolderClickListener(TaskAdapterBinding binding, int position) {
                this.position = position;
                this.binding = binding;
            }

            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public abstract void onClick(View v);
        }


        void doSomeWorkHere(TaskAdapterBinding binding, AppList data, int position) {
            loadTaskForUser(binding, data, position);
        }
    }


    public void setValues(ResultTask data) {
        this.mainData = data;
    }

    private void loadTaskForUser(TaskAdapterBinding binding, AppList data, int position) {
        Log.e(TAG, "Load time---------------------");
        if(!data.isPackagePresent()) {
            String packageName = data.getPackagename();
            if (isPackageExit(packageName)) {
                list.get(position).setPackagePresent(true);
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
            else{
                binding.downloadBtn.setBackgroundDrawable(context.getResources().
                        getDrawable(R.drawable.rounded_button_selected));
                binding.downloadBtn.setText("Install this app");
            }
        }
        else{
            binding.downloadBtn.setBackgroundDrawable(context.getResources().
                    getDrawable(R.drawable.rounded_button_unselected));
            binding.downloadBtn.setText("App Installed");
            //  Log.e(TAG, "L ******************** " + packageName);
        }

        if(isAlreadyInstalledCount == taskFragment.taskSize){
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

            for(int i = 0; i < list.size(); i++){
                if (list.get(i) != null && list.get(i).getTaskid().equals(taskId)) {
                    list.get(i).setPackagePresent(true);
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

    private class AdviewHolder extends RecyclerView.ViewHolder {
        private int position;

        public AdviewHolder(View bindingAds) {
            super(bindingAds);
        }

        void bind(AppList data, int position) {
//            this.position = position;
//            binding.setData(data);

        }
    }
}
