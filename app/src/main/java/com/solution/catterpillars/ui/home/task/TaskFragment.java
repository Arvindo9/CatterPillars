package com.solution.catterpillars.ui.home.task;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import com.solution.catterpillars.BR;
import com.solution.catterpillars.R;
import com.solution.catterpillars.base.BaseFragment;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.databinding.TaskFragmentBinding;
import com.solution.catterpillars.ui.home.Home;
import com.solution.catterpillars.ui.home.model.List;
import com.solution.catterpillars.ui.home.task.model.AppList;
import com.solution.catterpillars.ui.home.task.model.ResultTask;
import com.solution.catterpillars.util.ApplicationConstant;
import com.solution.catterpillars.util.CustomLoader;

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
public class TaskFragment extends BaseFragment<TaskFragmentBinding, TaskViewModel> {

    public static final String TAG = TaskFragment.class.getSimpleName();
    private static final String DATA_KEY = "keyData";
    private TaskViewModel viewModel;
    private Home activity;

    private ArrayList<AppList> list;
    private TaskAdapterMain adapter;
    private CustomLoader loader;
    public int taskSize;

    public static TaskFragment newInstance(Home taskActivity, List data) {
//        activity = taskActivity;
        Bundle args = new Bundle();
        TaskFragment fragment = new TaskFragment();
        args.putSerializable(DATA_KEY, data);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * @return R.layout.layout_file
     */
    @Override
    public int getLayout() {
        return R.layout.task_fragment;
    }

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    @Override
    public TaskViewModel getViewModel() {
//        viewModel = new TaskViewModel();
        this.activity = (Home) super.activity;
        this.viewModel = new TaskViewModel();
        return viewModel;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");

        taskChecking();
    }

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    @Override
    public int getBindingVariable() {
        return BR.data;
    }

    @Override
    public int setTitle() {
        return R.string.task;
    }

    /**
     * @return String value null or "" then this title will not show otherwise it will show
     */
    @Override
    protected String setTitleString() {
        return null;
    }

    /**
     * Write rest of the code of fragment in onCreateView after view created
     */
    @Override
    protected void init() {
        loader = new CustomLoader(context, 0);
//        if(activity instanceof TaskActivity){
//            activity = (TaskActivity) activity;
//        }

//        this.activity = (TaskActivity) super.activity;

      /*  binding.taskCompleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPreferencesService pref = new AppPreferencesService(context);
                if(pref.getTaskStatus()){
                    HomeNavigator navigator = (HomeNavigator) activity;
                    navigator.onActivityEvent("1");

                    Log.e(TAG, "Task Done");
                }
                Log.e(TAG, "Task Done  2222");
            }
        });*/
        list = (ArrayList<AppList>) getArguments().getSerializable("AppList");
        ResultTask data = (ResultTask) getArguments().getSerializable("Data");
        taskSize = (int) getArguments().getInt("TaskSize");
        binding.listView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        binding.listView.setLayoutManager(mLayoutManager);

        adapter = new TaskAdapterMain(activity, TaskFragment.this, list, viewModel);
        binding.listView.setAdapter(adapter);

        adapter.setValues(data);
        adapter.notifyDataSetChanged();
//        viewModel.installedPackageList(context, adapter);
        //apis();

        // for next button
//        AppPreferencesService pref = new AppPreferencesService(context);
//        if(pref.getTaskStatus()){
//
//            nextButtonVisibility(View.VISIBLE);
//           // binding.taskCompleteBtn.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public void onClick(View v) {
    }

    public void nextButtonVisibility(int value){
        if(context instanceof Home){
            ((Home)context).nextBtnVisibility(value);
        }
    }

    public void taskChecking() {
        AppPreferencesService pref = new AppPreferencesService(context);
        if (pref.getTaskStatus()){
            nextButtonVisibility(View.VISIBLE);
            //binding.taskCompleteBtn.setVisibility(View.VISIBLE);
        }
        else if(ApplicationConstant.TASK_COMPLETION){
            pref.setTaskStatus(true);
            nextButtonVisibility(View.VISIBLE);
            //binding.taskCompleteBtn.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }
}