package com.solution.catterpillars.ui.home.dashboard.task.task_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.google.gson.Gson;
import com.solution.catterpillars.R;
import com.solution.catterpillars.base.BaseFragment;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.databinding.TaskDetailFragmentBinding;
import com.solution.catterpillars.ui.home.Home;
import com.solution.catterpillars.ui.home.dashboard.task.TaskViewFragment;
import com.solution.catterpillars.ui.home.dashboard.task.model.TaskList;
import com.solution.catterpillars.ui.home.dashboard.task.model.TaskViewModel;
import com.solution.catterpillars.util.CustomAlertDialog;
import com.solution.catterpillars.util.CustomLoader;
import com.solution.catterpillars.youtube_subscription.YouTubeAuthActivityCallBack;
import com.solution.catterpillars.youtube_subscription.YouTubeSubscribe;

import java.util.ArrayList;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 12/8/2018
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 */
public class TaskDetailFragment extends BaseFragment<TaskDetailFragmentBinding, TaskViewModel>
        implements TaskScreenNavigator, YouTubeAuthActivityCallBack {

    public static final String TAG = TaskDetailFragment.class.getSimpleName();
    public AppPreferencesService preferencesService;
    private CustomLoader loader;
    private ArrayList<TaskList> list, completedList, pendingList;
    private int currentPosition;
    private int previousPosition;
    private int taskType;
    private String title = "";
    private int completedTask = 1, pendingTask = 2;
    private TaskViewFragment taskViewFragment;
    private TaskScreen taskScreen;

    boolean isFragmentOpen = false;

    public static TaskDetailFragment newInstance(String title, ArrayList<TaskList> list,
                                                 ArrayList<TaskList> completedList,
                                                 ArrayList<TaskList> pendingList,
                                                 int completedTask) {
        TaskDetailFragment fragment = new TaskDetailFragment();
        Bundle args = new Bundle();
        args.putString("Title", title);
        args.putSerializable("TaskList", list);
        args.putSerializable("CompletedList", completedList);
        args.putSerializable("PendingList", pendingList);
        args.putInt("TaskType", completedTask);
        fragment.setArguments(args);
        return fragment;
    }

    public void setTaskViewFragment(TaskViewFragment taskViewFragment) {
        this.taskViewFragment = taskViewFragment;
    }

    /**
     * @return R.layout.layout_file
     */
    @Override
    public int getLayout() {
        return R.layout.task_detail_fragment;
    }

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    @Override
    public TaskViewModel getViewModel() {
        title = getArguments().getString("Title");
//        mTaskStatus = (TaskStatus) getArguments().getSerializable("TaskStatus");
        list = (ArrayList<TaskList>) getArguments().getSerializable("TaskList");
        completedList = (ArrayList<TaskList>) getArguments().getSerializable("CompletedList");
        pendingList = (ArrayList<TaskList>) getArguments().getSerializable("PendingList");
        taskType = getArguments().getInt("TaskType");
//        this.viewModel =mTaskStatus;
        return viewModel;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        isFragmentOpen = false;
        if (taskScreen != null) {
            taskScreen.onDestroy();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        isFragmentOpen = true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        isFragmentOpen=true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isFragmentOpen=false;
    }

    /**
     * @return String value null or "" then this title will not show otherwise it will show
     */
    @Override
    protected String setTitleString() {
        return title;
    }

    /**
     * Write rest of the code of fragment in onCreateView after view created
     */
    @Override
    protected void init() {
        preferencesService = new AppPreferencesService(context);
        loader = new CustomLoader(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        setAds(100, 0, binding.adsContatinerTop, true);
        setAds(140, 0, binding.adsContatinerBottom, true);

        currentPosition = 0;
        previousPosition = 0;

        if (taskType == completedTask) {
            binding.taskAction.setVisibility(View.GONE);
            binding.parentView.setBackgroundResource(R.drawable.ic_box_green);
        } else {
            //TODO enable this comment
            binding.bottomView.setVisibility(View.GONE);
        }
        settingUi();

        binding.nextTask.setOnClickListener(v -> nextTask());
        binding.previousTask.setOnClickListener(v -> previousTask());
        binding.taskAction.setOnClickListener(v -> executionCurrentTask());
    }

    private void executionCurrentTask() {
        //TODO open task screen to execute the task

        if (isNetworkAvaliable(context)) {
            if(pendingList!=null && pendingList.size()>0) {
                goToTaskScreen(taskCallBack, pendingList.get(currentPosition), currentPosition);
            }
        } else {
            new CustomAlertDialog(context, isFragmentOpen).Error(getString(R.string.network_not_found));
        }
    }

    private void previousTask() {
        setAds(100, 0, binding.adsContatinerTop, true);
        setAds(140, 0, binding.adsContatinerBottom, true);
        if (currentPosition == 0) {
            activity.showToast(R.string.noTaskToView);
        } else {
            currentPosition--;
            settingUi();
        }
    }

    private void nextTask() {
        setAds(100, 0, binding.adsContatinerTop, true);
        setAds(140, 0, binding.adsContatinerBottom, true);
        if (taskType == completedTask) {
            if (currentPosition + 1 >= completedList.size()) {
                activity.showToast(R.string.noTaskToView);
            } else {
                currentPosition++;
                settingUi();
            }
        } else {
            if (currentPosition + 1 >= pendingList.size()) {
                activity.showToast(R.string.noTaskToView);
            } else {
                currentPosition++;
                settingUi();
            }
        }
    }

    private void settingUi() {
        binding.pendingText.setText(pendingList.size() + " pending");
        binding.completedText.setText(completedList.size() + " completed");

        if (taskType == completedTask) {
            if (!completedList.isEmpty()) {
                if (currentPosition >= completedList.size()) {
                    currentPosition--;
                }
                binding.taskName.setText((currentPosition + 1) + "-  " +
                        completedList.get(currentPosition).getTitle());


                binding.taskDetailText.setText(completedList.get(currentPosition).getInstruction());
            }
            if (currentPosition <= 0) {
                binding.previousTask.setVisibility(View.GONE);
                binding.nextTask.setVisibility(View.VISIBLE);
            } else if (currentPosition >= completedList.size() - 1) {
                binding.nextTask.setVisibility(View.GONE);
                binding.previousTask.setVisibility(View.VISIBLE);
            } else {
                binding.nextTask.setVisibility(View.VISIBLE);
                binding.previousTask.setVisibility(View.VISIBLE);
            }
        } else {
            if (!pendingList.isEmpty()) {
                if (currentPosition >= pendingList.size()) {
                    currentPosition--;
                }
                binding.taskName.setText(pendingList.get(currentPosition).getTitle());
               /* binding.taskName.setText((currentPosition + 1) + "- " +
                        pendingList.get(currentPosition).getTitle());*/

                binding.taskDetailText.setText(pendingList.get(currentPosition).getInstruction());
            }
            if (currentPosition <= 0) {
                binding.previousTask.setVisibility(View.GONE);
                binding.nextTask.setVisibility(View.VISIBLE);
            } else if (currentPosition >= pendingList.size() - 1) {
                binding.nextTask.setVisibility(View.GONE);
                binding.previousTask.setVisibility(View.VISIBLE);
            } else {
                binding.nextTask.setVisibility(View.VISIBLE);
                binding.previousTask.setVisibility(View.VISIBLE);
            }
        }
    }

    TaskCallBack taskCallBack = new TaskCallBack() {
        @Override
        public void onSuccessTaskComplete(int position, String taskNo) {
            try {
                TaskList data = pendingList.get(position);
                pendingList.remove(position);
                data.setStatus("1");
                completedList.add(data);

                ((Home) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                    activity.showToast(R.string.taskCompleted);
                        try {
                            new CustomAlertDialog(context, isFragmentOpen).Successful(getString(R.string.task_complete));
                            settingUi();
                            taskViewFragment.settingMainUI(completedList.size(), pendingList.size());
                            taskViewFragment.settingMainData(completedList, pendingList);
                        } catch (IllegalStateException ise) {

                        }

                    }
                });
                ((Home) context).onBackPressed();
            }catch (IllegalStateException ise){

            }catch(IndexOutOfBoundsException iobe){

            }
        }

        @Override
        public void onFailure(String errorMessage) {
            try {
                new CustomAlertDialog(context, isFragmentOpen).Warning(errorMessage);
                 /* new CustomAlertDialog(context, isFragmentOpen).WarningWithCallBack(getString(R.string.task_not_compleated), new CustomAlertDialog.DialogCallBack() {
                    @Override
                    public void onPositiveClick() {

                    }

                    @Override
                    public void onNegativeClick() {

                    }
                });*/
            } catch (IllegalStateException ise) {

            }
            //activity.showToast(errorMessage);
        }
    };

    @Override
    public void onClick(View v) {

    }

    //--------------go to task------------
    private void goToTaskScreen(TaskCallBack taskCallBack, TaskList taskList, int currentPosition) {
        //tmp only for testing
       /*taskCallBack.onSuccessTaskComplete(currentPosition, taskList.getTaskNo());*/

        //This is geniun code
        taskScreen = TaskScreen.newTaskScreen();
        taskScreen.setValuesToTaskScreen(context, activity, taskCallBack,
                taskList, currentPosition, TaskDetailFragment.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (taskScreen != null) {
            taskScreen.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onFragmentResume(int status) {
        taskScreen.onFragmentResume(status);
    }

    @Override
    public void startActivityForResult(Intent intent, boolean isChooseAccount) {
//        taskScreen.startActivityForResult(intent, isChooseAccount);
        try {
            if (isChooseAccount) {
                startActivityForResult(intent, YouTubeSubscribe.REQUEST_ACCOUNT_PICKER);
            } else {
                startActivityForResult(intent, YouTubeSubscribe.RC_SIGN_IN);
            }
        }catch (IllegalStateException ise){

        }
    }


}
