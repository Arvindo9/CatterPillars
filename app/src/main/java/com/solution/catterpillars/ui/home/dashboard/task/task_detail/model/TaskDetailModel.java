package com.solution.catterpillars.ui.home.dashboard.task.task_detail.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.solution.catterpillars.base.BaseViewModel;
import com.solution.catterpillars.ui.home.dashboard.task.model.TaskList;

import java.util.List;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 12/8/2018
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 */
public class TaskDetailModel extends BaseViewModel {
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("TaskList")
    @Expose
    private List<TaskList> taskList = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TaskList> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<TaskList> taskList) {
        this.taskList = taskList;
    }
}
