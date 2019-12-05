package com.solution.catterpillars.ui.home.dashboard.task;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.RewardedVideoAdExtendedListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.gson.Gson;
import com.solution.catterpillars.BuildConfig;
import com.solution.catterpillars.R;
import com.solution.catterpillars.base.BaseFragment;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.data.remort.APIClient;
import com.solution.catterpillars.data.remort.APIService;
import com.solution.catterpillars.databinding.TaskViewFragmentBinding;
import com.solution.catterpillars.ui.home.Home;
import com.solution.catterpillars.ui.home.content.HomeFragment;
import com.solution.catterpillars.ui.home.dashboard.ShareRefralDialog;
import com.solution.catterpillars.ui.home.dashboard.model.DataList;
import com.solution.catterpillars.ui.home.dashboard.task.model.TaskList;
import com.solution.catterpillars.ui.home.dashboard.task.model.TaskViewModel;
import com.solution.catterpillars.ui.home.dashboard.task.task_detail.TaskDetailFragment;
import com.solution.catterpillars.util.ApplicationConstant;
import com.solution.catterpillars.util.CustomAlertDialog;
import com.solution.catterpillars.util.CustomLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 12/8/2018
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 */
public class TaskViewFragment extends BaseFragment<TaskViewFragmentBinding, DataList> implements RewardedVideoAdListener {


    public static final String TAG = TaskViewFragment.class.getSimpleName();
    public AppPreferencesService preferencesService;
    private CustomLoader loader;
    private DataList taskStatusData;
    private ArrayList<TaskList> list, completedList, pendingList;
    private TaskViewModel viewModel;
    private RewardedVideoAd mRewardedVideoAd;
    private com.facebook.ads.RewardedVideoAd rewardedFbVideoAd;
    private String currentDate;
    private int completedTask = 1, pendingTask = 2;
    private boolean isFragmentOpen = false;

    public static TaskViewFragment newInstance(DataList dataList) {
        TaskViewFragment fragment = new TaskViewFragment();
        Bundle args = new Bundle();
        args.putSerializable("TaskStatus", dataList);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * @return R.layout.layout_file
     */
    @Override
    public int getLayout() {
        return R.layout.task_view_fragment;
    }

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    @Override
    public DataList getViewModel() {
        taskStatusData = (DataList) getArguments().getSerializable("TaskStatus");
        return taskStatusData;
    }

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    @Override
    public int getBindingVariable() {
        return 0;
    }

    /**
     * @return R.strings.text
     */
    @Override
    public int setTitle() {
        return 0;
    }

    /**
     * @return String value null or "" then this title will not show otherwise it will show
     */
    @Override
    protected String setTitleString() {
        return taskStatusData.getTitle();
    }

    /**
     * Write rest of the code of fragment in onCreateView after view created
     */
    @Override
    protected void init() {
        // setDashboardData();
        list = new ArrayList<>();
        completedList = new ArrayList<>();
        pendingList = new ArrayList<>();
        viewModel = new TaskViewModel();
        preferencesService = new AppPreferencesService(context);
        loader = new CustomLoader(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        setAds(100, 0, binding.adsContatinerTop, true);
        setAds(140, 0, binding.adsContatinerBottom, true);

        // Use an activity context to get the rewarded video instance.
        Date date = new Date();
        currentDate = new SimpleDateFormat("yyyyMMdd").format(date);
        String lastDate = preferencesService.getDate();

        if (Integer.parseInt(currentDate) > Integer.parseInt(lastDate)) {

            loadRewardedVideoAd();
        }
//        binding.completedCount.setText(taskStatusData.getCompleted() + " Task Completed");
//        binding.pendingCount.setText(taskStatusData.getPending() + " Task Pending");
        binding.setData(taskStatusData);

        binding.viewCompletedTask.setOnClickListener(v -> completeTaskButton());

        binding.viewPendingTask.setOnClickListener(v -> pendingTaskButton());

        //call task api
        if (taskStatusData.getTitle().equalsIgnoreCase("Sign Up Task") ||
                taskStatusData.getUrl().contains("SignUp Task")) {
            api(1);
//            api(2);     //tmp only for testing
        } else if (taskStatusData.getTitle().equalsIgnoreCase("Daily Task")
                || taskStatusData.getUrl().contains("DailyTask")) {
            api(2);
        }
    }

    @Override
    public void onClick(View v) {

    }

    public void completeTaskButton() {
        if (!completedList.isEmpty()) {
            if (context instanceof Home) {
                ((Home) context).networkErrorViewVisible(View.GONE);
                showVideoAds();
                ((Home) context).fragment = TaskDetailFragment.newInstance(taskStatusData.getTitle(),
                        list, completedList, pendingList, completedTask);
                ((TaskDetailFragment) ((Home) context).fragment).setTaskViewFragment(TaskViewFragment.this);
                ((Home) context).openFragment(((Home) context).fragment, TaskDetailFragment.TAG);
            }
        } else {
            activity.showToast(R.string.noTaskToView);
        }
    }

    public void pendingTaskButton() {
        if (!pendingList.isEmpty()) {
            if (context instanceof Home) {
                ((Home) context).networkErrorViewVisible(View.GONE);
                showVideoAds();
                ((Home) context).fragment = TaskDetailFragment.newInstance(taskStatusData.getTitle(),
                        list, completedList, pendingList, pendingTask);
                ((TaskDetailFragment) ((Home) context).fragment).setTaskViewFragment(TaskViewFragment.this);
                ((Home) context).openFragment(((Home) context).fragment, TaskDetailFragment.TAG);
            }
        } else {
            activity.showToast(R.string.noTaskToView);
        }
    }


    //------------Reward video ads------------

    /**
     * for video ads
     */
    private void loadRewardedVideoAd() {
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        mRewardedVideoAd.loadAd(getString(R.string.adsVideoHome), new AdRequest.Builder().build());

        rewardedFbVideoAd = new com.facebook.ads.RewardedVideoAd(context, ApplicationConstant.INSTANCE.FB_ADS_PLACEMENT_ID);
        rewardedFbVideoAd.setAdListener(new RewardedVideoAdExtendedListener() {
            @Override
            public void onRewardedVideoActivityDestroyed() {

            }

            @Override
            public void onRewardedVideoCompleted() {
                preferencesService.setDate(currentDate);
            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }

            @Override
            public void onRewardedVideoClosed() {

            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }
        });
        if (BuildConfig.DEBUG) {
            AdSettings.addTestDevice("0e7cac3c-373a-435c-babc-e3955a6d95a7");
        }
        rewardedFbVideoAd.loadAd();
    }

    public void showVideoAds() {
        final int random = new Random().nextInt(50);
        if (random%2 == 0) {
            if (mRewardedVideoAd != null && mRewardedVideoAd.isLoaded()) {
                mRewardedVideoAd.show();
            }
        }else {

            if (rewardedFbVideoAd != null && rewardedFbVideoAd.isAdLoaded()) {
                rewardedFbVideoAd.show();
            }
        }
    }


    public void settingMainUI(int completeTaskSize, int pendingTaskSize) {
        binding.completedCount.setText(String.valueOf(completeTaskSize));
        binding.pendingCount.setText(String.valueOf(pendingTaskSize));
    }

    public void settingMainData(ArrayList<TaskList> completedList, ArrayList<TaskList> pendingList) {
        ArrayList<TaskList> taskLists = new ArrayList<>(completedList);
        taskLists.addAll(pendingList);
        if (viewModel != null) {
            viewModel.setTaskList(taskLists);
            preferencesService.setTaskData(new Gson().toJson(viewModel));
        }

        checkTaskComplete();
    }

    public void checkTaskComplete() {
        if (pendingList.isEmpty()) {
            /*
              preferencesService.setTaskCompleted(true);
             */

            if (!completedList.isEmpty()) {

                if (!viewModel.getStatus().equalsIgnoreCase("Completed")) {
                    apiOnSuccessTask(completedList.get(0).getTaskId());
                }
            }

            //tmp only for testing
            /*preferencesService.setTaskCompleted(true);
            if (((Home) context).fragment instanceof TaskDetailFragment) {
                activity.onBackPressed();
            }

            activity.onBackPressed();*/

//            preferencesService.setTaskCompleted(true);
//            ((Home) context).isAppOpen = true;
//            ((Home) context).isOnTaskCompleted = true;

            Log.e(TAG, "Task completed");
            Log.e(TAG, "Task completed");
        } else {
            Log.e(TAG, "Task pending");
        }
    }

    //--------------apis----------------------
    //index = 1, sign up
    //index = 2, daily
    void api(int index) {
        loader.show();
        loader.setCancelable(false);
        loader.setCanceledOnTouchOutside(false);

//        Date date1 = new Date();
//        String currentDate1 = new SimpleDateFormat("yyyyMMdd").format(date1);
        int currentDate = Integer.parseInt(this.currentDate);

        int dailyTaskDate = preferencesService.getDailyTaskDate();
        Log.e(TAG, String.valueOf(dailyTaskDate));

        //sign up task
        if (index == 1 && !preferencesService.isPreviousTaskCompleted() &&
                preferencesService.getTaskData() != null) {
            Gson gson = new Gson();
            viewModel = gson.fromJson(preferencesService.getTaskData(),
                    TaskViewModel.class);
            list.addAll(viewModel.getTaskList());

            for (TaskList taskList : list) {
                if (taskList.getStatus().equals("1")) {
                    completedList.add(taskList);
                } else {
                    if (taskList.getTitle().equalsIgnoreCase("App download") &&
                            isPackageExit(taskList.getPackageName())) {
                        taskList.setStatus("1");
                        completedList.add(taskList);
                    } else {
                        pendingList.add(taskList);
                    }
                }
            }

            //TODO maintain the list from web

            if (Integer.parseInt(taskStatusData.getPending()) != pendingList.size() &&
                    ((Home) context).isTaskViewFragmentOpen) {
                // call API with syncronise

                ((Home) context).isTaskViewFragmentOpen = false;
                api(11);
                return;
            }

            settingMainUI(completedList.size(), pendingList.size());
            checkTaskComplete();    //check if task completed


            loader.dismiss();
        } else if (index == 2 && !preferencesService.isPreviousTaskCompleted() &&
                currentDate == dailyTaskDate &&
                preferencesService.getTaskData() != null) {
            //daily task
            Gson gson = new Gson();
            viewModel = gson.fromJson(preferencesService.getTaskData(),
                    TaskViewModel.class);
            list.addAll(viewModel.getTaskList());

            for (TaskList taskList : list) {
                if (taskList.getStatus().equals("1")) {
                    completedList.add(taskList);
                } else {
                    pendingList.add(taskList);
                }
            }

            //TODO maintain the list from web
            if (Integer.parseInt(taskStatusData.getPending()) != pendingList.size() &&
                    ((Home) context).isTaskViewFragmentOpen) {
                // call API with syncronise

                ((Home) context).isTaskViewFragmentOpen = false;
                api(12);
                return;
            }

            settingMainUI(completedList.size(), pendingList.size());
            checkTaskComplete();    //check if task completed
            loader.dismiss();
        } else if (!((Home) context).isOnTaskCompleted) {
//            if((index == 11 || index == 12) && ((Home) context).isTaskViewFragmentOpen) {
            APIService apiService = APIClient.getConnect().create(APIService.class);
            Call<TaskViewModel> call = null;// = apiService.checkSheetDetails(module, "", "");

            if (index == 1 || index == 11) {
                call = apiService.getSignUpTaskList(preferencesService.getUserId());
            } else {
                call = apiService.getDailyTaskList(preferencesService.getUserId());
            }

            if (call != null) {
                call.enqueue(new retrofit2.Callback<TaskViewModel>() {
                    @Override
                    public void onResponse(@NonNull Call<TaskViewModel> call, @NonNull
                            Response<TaskViewModel> response) {
                        viewModel = response.body();
                        if (viewModel != null) {
                            list.clear();
                            if (index < 10) {
                                list.addAll(viewModel.getTaskList());

                                for (TaskList taskList : list) {
                                    if (taskList.getStatus().equals("1")) {
                                        completedList.add(taskList);
                                    } else {
                                        if (taskList.getTitle().equalsIgnoreCase("App download") &&
                                                isPackageExit(taskList.getPackageName())) {
                                            taskList.setStatus("1");
                                            completedList.add(taskList);
                                        } else {
                                            pendingList.add(taskList);
                                        }
                                    }
                                }

                                preferencesService.setTaskData(new Gson().toJson(viewModel));
                                preferencesService.setTaskCompleted(false);
                                preferencesService.setDailyTaskDate(currentDate);
                            } else {
                                syncroniseAppWithWebTask(index);
                            }
                        } else {

                            ((Home) context).isTaskViewFragmentOpen = true;
                            new CustomAlertDialog(context, isFragmentOpen).Error(getString(R.string.data_null_error));
                        }

                        settingMainUI(completedList.size(), pendingList.size());
                        loader.dismiss();

                        Log.e(TAG, "new Task Loaded");
                    }

                    @Override
                    public void onFailure(@NonNull Call<TaskViewModel> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        call.cancel();
                        loader.dismiss();

                        ((Home) context).isTaskViewFragmentOpen = true;

                        if (t.getMessage() != null && !t.getMessage().isEmpty()) {
                            if (t.getMessage().contains("No address associated with hostname")) {
                                new CustomAlertDialog(context, isFragmentOpen).Error(getString(R.string.network_not_found));
                                showNetworkErrorView(false, "", index);
                            } else {
                                new CustomAlertDialog(context, isFragmentOpen).Error(getString(R.string.try_again));
                            }
                        }
                    }
                });
            }
//            }
        } else if (preferencesService.isPreviousTaskCompleted()) {
            if (loader.isShowing()) {
                loader.dismiss();
            }

            Gson gson = new Gson();
            viewModel = gson.fromJson(preferencesService.getTaskData(),
                    TaskViewModel.class);
            list.addAll(viewModel.getTaskList());

            for (TaskList taskList : list) {
                if (taskList.getStatus().equals("1")) {
                    completedList.add(taskList);
                } else {
                    if (taskList.getTitle().equalsIgnoreCase("App download") &&
                            isPackageExit(taskList.getPackageName())) {
                        taskList.setStatus("1");
                        completedList.add(taskList);
                    } else {
                        pendingList.add(taskList);
                    }
                }
            }

            settingMainUI(completedList.size(), pendingList.size());
        }
    }

    private void syncroniseAppWithWebTask(int index) {
        list.addAll(viewModel.getTaskList());

        HashMap<String, Integer> localMap = new HashMap<>();

        for (int i = 0; i < pendingList.size(); i++) {
            TaskList data = pendingList.get(i);
            localMap.put(data.getTaskNoId(), i);
        }


        for (int i = 0; i < list.size(); i++) {
            TaskList data = list.get(i);
            if (list.get(i).getStatus().equalsIgnoreCase("1")) {
                //completed list
                if (localMap.containsKey(data.getTaskNoId())) {
                    int j = localMap.get(data.getTaskNoId());
                    TaskList data1 = pendingList.get(j);
                    pendingList.remove(j);
                    data1.setStatus("1");
                    completedList.add(data);

                    localMap.clear();
                    for (int k = 0; k < pendingList.size(); k++) {
                        TaskList data2 = pendingList.get(k);
                        localMap.put(data2.getTaskNoId(), k);
                    }
                }
            }
        }

        settingMainUI(completedList.size(), pendingList.size());
        settingMainData(completedList, pendingList);
    }


    private void apiOnSuccessTask(String taskId) {
        if (!((Home) context).isOnTaskCompleted) {
            ((Home) context).isOnTaskCompleted = true;
            APIService apiService = APIClient.getConnect().create(APIService.class);
            Call<TaskViewModel> call = null;// = apiService.checkSheetDetails(module, "", "");

            call = apiService.setTaskComplete(preferencesService.getUserId(), taskId);

            if (call != null) {
                call.enqueue(new retrofit2.Callback<TaskViewModel>() {
                    @Override
                    public void onResponse(@NonNull Call<TaskViewModel> call, @NonNull
                            Response<TaskViewModel> response) {
                        viewModel = response.body();
                        if (viewModel != null) {
                            if (viewModel.getStatus().equals("1")) {
                                preferencesService.setTaskCompleted(true);
                                ((Home) context).isAppOpen = true;

                           /* if (((Home) context).fragmentManager.findFragmentByTag(TaskDetailFragment.TAG)
                                    instanceof TaskDetailFragment) {
                                TaskDetailFragment hf = (TaskDetailFragment)
                                        ((Home) context).fragmentManager.findFragmentById(R.id.frame);
                                if (hf != null) {
                                    ((Home) context).onBackPressed();
                                }
                            }
                            if (((Home) context).fragment instanceof TaskDetailFragment) {
                                ((Home) context).onBackPressed();
                            }
                                ((Home) context).onBackPressed();*/


                                if (context != null) {
                                    if (taskStatusData != null) {
                                        if (taskStatusData.getTitle().equalsIgnoreCase("Sign Up Task") ||
                                                taskStatusData.getUrl().contains("SignUp Task")) {
                                            ShareRefralDialog shareDialog = new ShareRefralDialog(context, 0);
                                            shareDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.full_screen_dialog);
                                            shareDialog.show(((Home) context).getSupportFragmentManager(), ShareRefralDialog.TAG);

                                        } else if (taskStatusData.getTitle().equalsIgnoreCase("Daily Task")
                                                || taskStatusData.getUrl().contains("DailyTask")) {
                                            ShareRefralDialog shareDialog = new ShareRefralDialog(context, 1);
                                            shareDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.full_screen_dialog);
                                            shareDialog.show(((Home) context).getSupportFragmentManager(), ShareRefralDialog.TAG);
                                        }
                                    }

                                }
                                if (viewModel.getMessage() != null && !viewModel.getMessage().isEmpty()) {
                                    ((Home) context).showToast(viewModel.getMessage());
                                } else {
                                    ((Home) context).showToast(R.string.successfullyTaskCompleted);
                                }
                                Log.e(TAG, "Task completed");
                            } else {
                                ((Home) context).showToast(viewModel.getMessage());
                            }
                        } else {

                            new CustomAlertDialog(context, isFragmentOpen).Error(getString(R.string.data_null_error));
                        }
                        Log.e(TAG, "new Task Loaded");
                    }

                    @Override
                    public void onFailure(@NonNull Call<TaskViewModel> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        call.cancel();
                        loader.dismiss();

                        if (t.getMessage() != null && !t.getMessage().isEmpty()) {
                            if (t.getMessage().contains("No address associated with hostname")) {
                                new CustomAlertDialog(context, isFragmentOpen).Error(getString(R.string.network_not_found));
                                showNetworkErrorView(true, taskId, 0);
                            } else {
                                new CustomAlertDialog(context, isFragmentOpen).Error(getString(R.string.try_again));
                            }
                        }
                    }
                });
            }
        }
    }


    void showNetworkErrorView(boolean isSucessApi, String taskId, int index) {
        if (context instanceof Home) {
            ((Home) context).networkErrorViewVisible(View.VISIBLE)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((Home) context).networkErrorViewVisible(View.GONE);
                            if (!isSucessApi) {
                                api(index);
                            } else {
                                apiOnSuccessTask(taskId);
                            }
                        }
                    });
        }
    }

    //-----------Video ads--------------------

    @Override
    public void onRewarded(RewardItem reward) {
//        Toast.makeText(this, "onRewarded! currency: " + reward.getType() + "  amount: " +
//                reward.getAmount(), Toast.LENGTH_SHORT).show();
        // Reward the user.
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
//        Toast.makeText(this, "onRewardedVideoAdLeftApplication",
//                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        //preferencesService.setDate(currentDate);
//        Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        Log.e(TAG, "video ads failed:" + errorCode);
//        Toast.makeText(this, "onRewardedVideoAdFailedToLoad:" + errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {
//        Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
//        Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoCompleted() {
        preferencesService.setDate(currentDate);
//        Toast.makeText(this, "onRewardedVideoCompleted", Toast.LENGTH_SHORT).show();
    }

    private boolean isPackageExit(String packageName) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }

        return app_installed;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        isFragmentOpen = false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        isFragmentOpen = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        isFragmentOpen = true;
    }

    @Override
    public void onResume() {
        if (mRewardedVideoAd != null) {
            mRewardedVideoAd.resume(context);
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mRewardedVideoAd != null) {
            mRewardedVideoAd.pause(context);
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mRewardedVideoAd != null) {
            mRewardedVideoAd.destroy(context);
        }
        isFragmentOpen = false;

        if (rewardedFbVideoAd != null) {
            rewardedFbVideoAd.destroy();
            rewardedFbVideoAd = null;
        }
        super.onDestroy();
    }
}
