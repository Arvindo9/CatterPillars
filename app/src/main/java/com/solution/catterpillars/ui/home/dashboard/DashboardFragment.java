package com.solution.catterpillars.ui.home.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.solution.catterpillars.BR;
import com.solution.catterpillars.R;
import com.solution.catterpillars.base.BaseFragment;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.data.remort.APIClient;
import com.solution.catterpillars.data.remort.APIService;
import com.solution.catterpillars.databinding.DashboardFragmentBinding;
import com.solution.catterpillars.ui.home.Home;
import com.solution.catterpillars.ui.home.dashboard.callbackInterface.VerifyDataCallBack;
import com.solution.catterpillars.ui.home.dashboard.model.DashboardViewModel;
import com.solution.catterpillars.ui.home.dashboard.model.DataList;
import com.solution.catterpillars.ui.home.dashboard.model.DataType;
import com.solution.catterpillars.ui.home.dashboard.task.model.TaskList;
import com.solution.catterpillars.ui.home.dashboard.task.model.TaskViewModel;
import com.solution.catterpillars.util.ApplicationConstant;
import com.solution.catterpillars.util.CustomAlertDialog;
import com.solution.catterpillars.util.CustomLoader;
import com.solution.catterpillars.util.RecyclerViewItemDecoration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by KinG on 10-12-2018.
 * Created by ${EMAIL}.
 */
public class DashboardFragment extends BaseFragment<DashboardFragmentBinding, DashboardViewModel>
        implements VerifyDataCallBack {

    public static final String TAG = DashboardFragment.class.getSimpleName();
    private boolean shouldLoadApi = false;
    private DashboardViewModel viewModel;
    private AppPreferencesService preferencesService;
    private CustomLoader loader;
    private ArrayList<DataType> list;
    private DashboardAdapter adapter;
    private DataList mAffiliateLink;
    public int completed = 0;
    public int pending = 0;
    public boolean isFragmentOpen=false;

    public static DashboardFragment newInstance() {
        Bundle args = new Bundle();
        DashboardFragment fragment = new DashboardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.dashboard_fragment;
    }

    @Override
    public DashboardViewModel getViewModel() {
        return viewModel = new DashboardViewModel();
    }

    @Override
    public int getBindingVariable() {
        return BR.data;
    }

    @Override
    public int setTitle() {
        return R.string.dashboard;
    }

    @Override
    protected String setTitleString() {
        return null;
    }

    @Override
    protected void init() {

        MobileAds.initialize(context, ApplicationConstant.INSTANCE.ADMOB_APP_ID);
        binding.adView.loadAd(new AdRequest.Builder().build());
        preferencesService = new AppPreferencesService(context);

        loader = new CustomLoader(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        list = new ArrayList<>();
        adapter = new DashboardAdapter(context, list, DashboardFragment.this, this,binding.recyclerView);

        binding.recyclerView.addItemDecoration(new RecyclerViewItemDecoration((int)
                context.getResources().getDimension(R.dimen._12sdp),
                RecyclerViewItemDecoration.VERTICAL, false));

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.setAdapter(adapter);

        checkBackgroundData();
//
        apis();


//        checkBackgroundData();
    }

    private void checkBackgroundData() {
        ArrayList<TaskList> list = new ArrayList<>();
        completed = 0;
        pending = 0;

        // Use an activity context to get the rewarded video instance.
        Date date = new Date();
        String currentDate1 = new SimpleDateFormat("yyyyMMdd").format(date);
        int currentDate = Integer.parseInt(currentDate1);
        int dailyTaskDate = preferencesService.getDailyTaskDate();

        TaskViewModel taskViewModel = null;

        if (!preferencesService.isPreviousTaskCompleted() &&
                preferencesService.getTaskData() != null) {
            Gson gson = new Gson();
            taskViewModel = gson.fromJson(preferencesService.getTaskData(),
                    TaskViewModel.class);
            list.addAll(taskViewModel.getTaskList());

            for (TaskList taskList : list) {
                if (taskList.getStatus().equals("1")) {
                    completed++;
                } else {
                    pending++;
                }
            }
        } else if (!preferencesService.isPreviousTaskCompleted() &&
                currentDate == dailyTaskDate &&
                preferencesService.getTaskData() != null) {
            Gson gson = new Gson();
            taskViewModel = gson.fromJson(preferencesService.getTaskData(),
                    TaskViewModel.class);
            list.addAll(taskViewModel.getTaskList());

            for (TaskList taskList : list) {
                if (taskList.getStatus().equals("1")) {
                    completed++;
                } else {
                    pending++;
                }
            }
        }

        /*DashboardViewModel dataModel = new Gson().fromJson(preferencesService.getDashboardData(),
                DashboardViewModel.class);
        if (dataModel != null) {
            dataModel.getTaskStatus().setPending(pending+"");
            dataModel.getTaskStatus().setCompleted(completed+"");
            preferencesService.setDashboardData(new Gson().toJson(dataModel));
        }*/
    }

    private void apis() {


        if (((Home) context).isAppOpen) {
            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);
            ((Home) context).isAppOpen = false;
            APIService apiService = APIClient.getConnect().create(APIService.class);
            Call<DashboardViewModel> call = null;// = apiService.checkSheetDetails(module, "", "");

            call = apiService.getDashboardData(preferencesService.getUserId());

            if (call != null) {
                call.enqueue(new retrofit2.Callback<DashboardViewModel>() {
                    @Override
                    public void onResponse(@NonNull Call<DashboardViewModel> call, @NonNull
                            Response<DashboardViewModel> response) {
                        DashboardViewModel data = response.body();

                        ((Home) context).isOnTaskCompleted = false;
                        if (data != null && data.getStatus().equalsIgnoreCase("1")) {

                            preferencesService.setDashboardData(new Gson().toJson(data));
                            if (data.getIsShowDownlineContact().equalsIgnoreCase("0")) {
                                preferencesService.setDownlineContact(false);
                            } else {
                                preferencesService.setDownlineContact(true);
                            }


                            if (data.getIsShowUplineContact().equalsIgnoreCase("0")) {
                                preferencesService.setUplineContact(false);
                            } else {
                                preferencesService.setUplineContact(true);
                            }

                            preferencesService.setAffiletdLink1(data.getAffiliateLink().getLink1());
                            preferencesService.setAffiletdLink2(data.getAffiliateLink().getLink2());
                            preferencesService.setUplineEmail(data.getUplineEmailId());
                            preferencesService.setUplineMobile(data.getUplineMobile());
                            preferencesService.setUplineName(data.getUplineName());

                            setUiByApiData(data);
                        } else {

                            new CustomAlertDialog(context,isFragmentOpen).Error(getString(R.string.data_null_error));
                        }
                        if(loader != null && loader.isShowing()) {
                            loader.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<DashboardViewModel> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        call.cancel();
                        if(loader != null && loader.isShowing()) {
                            loader.dismiss();
                        }
                        /*if (t.getMessage().contains("No address associated with hostname")) {
                            new CustomAlertDialog(context,isFragmentOpen).Error(getString(R.string.network_not_found));
                            showNetworkErrorView();
                        } else {
                            new CustomAlertDialog(context,isFragmentOpen).Error(getString(R.string.try_again));
                        }*/

                        if(t.getMessage()!=null && !t.getMessage().isEmpty()) {
                            if (t.getMessage().contains("No address associated with hostname")) {
                                new CustomAlertDialog(context,isFragmentOpen).Error(getString(R.string.network_not_found));
                                showNetworkErrorView();

                            } else {
                                new CustomAlertDialog(context,isFragmentOpen).Error(getString(R.string.try_again));
                            }
                        }/*else{
                        new CustomAlertDialog(context,isFragmentOpen).Error(getString(R.string.try_again));
                    }*/
                    }
                });
            }
        } else {
            //Show locale store data
            DashboardViewModel dataModel = new Gson().fromJson(preferencesService.getDashboardData(),
                    DashboardViewModel.class);
            if (dataModel != null) {
                setUiByApiData(dataModel);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(loader != null && loader.isShowing()) {
            loader.dismiss();
        }
        
        isFragmentOpen=false;
    }

    void showNetworkErrorView() {
        if (context instanceof Home) {
            ((Home) context).networkErrorViewVisible(View.VISIBLE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Home) context).networkErrorViewVisible(View.GONE);
                    ((Home) context).isAppOpen=true;
                    apis();
                }
            });
        }
    }


    private void setUiByApiData(DashboardViewModel data) {
        if (data != null && data.getVerifyData().size() > 0 && data.getNotification() != null
                && !data.getNotification().isEmpty()) {
            binding.notificationView.setVisibility(View.VISIBLE);
            binding.noticeText.setText(data.getNotification());
            binding.noticeText.setSelected(true);
        } else {
            binding.notificationView.setVisibility(View.GONE);
        }
        list.clear();


        list.add(new DataType(data.getVerifyData(), "array", "VerifyData"));
        list.add(new DataType(null, "single", "Ads"));
        list.add(new DataType(data.getAffiliateLink(), "single", "Share"));
       /* if(data.getUplineEmailId()!=null && data.getUplineMobile()!=null && data.getUplineName()!=null){*/
        list.add(new DataType(null, "single", "DownLineUpLineData"));
        /* }*/

        list.add(new DataType(data.getTaskStatus(), "single", "TaskStatus"));
        list.add(new DataType(data.getData().getTodayIncome(), "array", "Income"));
        list.add(new DataType(data.getData().getMember(), "array", "Member"));
        list.add(new DataType(data.getData().getWallet(), "array", "Wallet"));

       /* mAffiliateLink = data.getAffiliateLink();
        if (mAffiliateLink != null) {
            if (context instanceof Home) {
                ((Home) context).shareButtonVisible(View.VISIBLE, mAffiliateLink);
            }
        }*/

       checkingTaskData(data.getTaskStatus());
    }

    private void checkingTaskData(DataList taskStatus){
        Gson gson = new Gson();
        TaskViewModel viewModel = gson.fromJson(preferencesService.getTaskData(),
                TaskViewModel.class);
        if(viewModel != null) {
            ArrayList<TaskList> list = new ArrayList<>(viewModel.getTaskList());
            ArrayList<TaskList> completedList = new ArrayList<>();
            ArrayList<TaskList> pendingList = new ArrayList<>();

            for (TaskList taskList : list) {
                if (taskList.getStatus().equals("1")) {
                    completedList.add(taskList);
                } else {
                    pendingList.add(taskList);
                }
            }

            if (taskStatus.getTitle().equalsIgnoreCase("Sign Up Task")) {
                if (taskStatus.getTitle().equalsIgnoreCase(viewModel.getTitle()) ||
                        viewModel.getTitle().equalsIgnoreCase("SignUp Task") ||
                        viewModel.getTitle().equalsIgnoreCase("Sign Up Task")) {
                    //If sign up task in server and app both
                    if (Integer.parseInt(taskStatus.getPending()) < pendingList.size()) {
                        //If sign up task completed from server
                        //refresh task
                        preferencesService.setTaskCompleted(true);
                        completed = Integer.parseInt(taskStatus.getCompleted());
                        pending = Integer.parseInt(taskStatus.getPending());
                    }
                } else {
                    //If sign up task get completed and daily task loaded
                    //refresh task
                    preferencesService.setTaskCompleted(true);
                    completed = Integer.parseInt(taskStatus.getCompleted());
                    pending = Integer.parseInt(taskStatus.getPending());
                }
            } else if (taskStatus.getTitle().equalsIgnoreCase("Daily Task")) {
                if (taskStatus.getTitle().equalsIgnoreCase(viewModel.getTitle()) ||
                        viewModel.getTitle().equalsIgnoreCase("Daily Task") ||
                        viewModel.getTitle().equalsIgnoreCase("DailyTask")) {
                    //If sign up task in server and app both
                    if (Integer.parseInt(taskStatus.getPending()) < pendingList.size()) {
                        //If sign up task completed from server
                        //refresh task
                        preferencesService.setTaskCompleted(true);
                        completed = Integer.parseInt(taskStatus.getCompleted());
                        pending = Integer.parseInt(taskStatus.getPending());
                    }
                } else {
                    //If sign up task get completed and daily task loaded
                    //refresh task
                    preferencesService.setTaskCompleted(true);
                    completed = Integer.parseInt(taskStatus.getCompleted());
                    pending = Integer.parseInt(taskStatus.getPending());
                }
            }
        }
    }


    @Override
    public void onClick(View v) {

    }


    @Override
    public void onSuccess() {
        ((Home) context).isAppOpen=true;
        apis();
    }

    @Override
    public void onError() {

    }


    @Override
    public void onStart() {
        super.onStart();
        isFragmentOpen=true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isFragmentOpen=false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        isFragmentOpen=true;
    }
}
