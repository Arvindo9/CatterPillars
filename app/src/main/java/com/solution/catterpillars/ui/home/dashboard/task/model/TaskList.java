package com.solution.catterpillars.ui.home.dashboard.task.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 12/8/2018
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 */
public class TaskList implements Serializable {

    @SerializedName("SetId")
    @Expose
    private String setId;
    @SerializedName("TaskId")
    @Expose
    private String taskId;
    @SerializedName("TaskNoId")
    @Expose
    private String taskNoId;
    @SerializedName("CampaignId")
    @Expose
    private String campaignId;
    @SerializedName("TaskNo")
    @Expose
    private String taskNo;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Url")
    @Expose
    private String url;
    @SerializedName("OverallTime")
    @Expose
    private String overallTime;
    @SerializedName("Status")
    @Expose
    private String status;

    @SerializedName("Instruction")
    @Expose
    private String instruction;
    @SerializedName("ChannelId")
    @Expose
    private String channelId;
    @SerializedName("URLList")
    @Expose
    private List<String> uRLList = null;


    @SerializedName("PackageName")
    @Expose
    private String packageName;

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getSetId() {
        return setId;
    }

    public void setSetId(String setId) {
        this.setId = setId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskNoId() {
        return taskNoId;
    }

    public void setTaskNoId(String taskNoId) {
        this.taskNoId = taskNoId;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOverallTime() {
        return overallTime;
    }

    public void setOverallTime(String overallTime) {
        this.overallTime = overallTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getuRLList() {
        return uRLList;
    }

    public void setuRLList(List<String> uRLList) {
        this.uRLList = uRLList;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
