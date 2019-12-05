package com.solution.catterpillars.ui.home.dashboard.task.task_detail;

/**
 * Created by KinG on 09-12-2018.
 * Created by ${EMAIL}.
 */
public interface TaskCallBack {

    void onSuccessTaskComplete(int position, String taskNo);

    void onFailure(String errorMessage);
}
