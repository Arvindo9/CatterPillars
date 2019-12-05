
package com.solution.catterpillars.ui.home.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.solution.catterpillars.base.BaseViewModel;

import java.util.List;

public class DashboardViewModel extends BaseViewModel {

    @SerializedName("VerifyData")
    @Expose
    private List<DataList> verifyData = null;
    @SerializedName("TaskStatus")
    @Expose
    private DataList taskStatus;
    @SerializedName("Data")
    @Expose
    private Data data;
    @SerializedName("AffiliateLink")
    @Expose
    private DataList affiliateLink;

    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Notification")
    @Expose
    private String notification;

    @SerializedName("IsShowUplineContact")
    @Expose
    private String isShowUplineContact;

    @SerializedName("IsShowDownlineContact")
    @Expose
    private String isShowDownlineContact;

    @SerializedName("IsShowScreenLock")
    @Expose
    private String isShowScreenLock;


    @SerializedName("UplineEmailId")
    @Expose
    private String uplineEmailId;

    @SerializedName("UplineMobile")
    @Expose
    private String uplineMobile;

    @SerializedName("UplineName")
    @Expose
    private String uplineName;




    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public List<DataList> getVerifyData() {
        return verifyData;
    }

    public void setVerifyData(List<DataList> verifyData) {
        this.verifyData = verifyData;
    }


    public DataList getAffiliateLink() {
        return affiliateLink;
    }

    public void setAffiliateLink(DataList affiliateLink) {
        this.affiliateLink = affiliateLink;
    }

    public DataList getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(DataList taskStatus) {
        this.taskStatus = taskStatus;
    }


    public String getIsShowUplineContact() {
        return isShowUplineContact;
    }

    public String getIsShowDownlineContact() {
        return isShowDownlineContact;
    }

    public String getIsShowScreenLock() {
        return isShowScreenLock;
    }

    public void setIsShowUplineContact(String isShowUplineContact) {
        this.isShowUplineContact = isShowUplineContact;
    }

    public void setIsShowDownlineContact(String isShowDownlineContact) {
        this.isShowDownlineContact = isShowDownlineContact;
    }

    public void setIsShowScreenLock(String isShowScreenLock) {
        this.isShowScreenLock = isShowScreenLock;
    }

    public String getUplineEmailId() {
        return uplineEmailId;
    }

    public String getUplineMobile() {
        return uplineMobile;
    }

    public String getUplineName() {
        return uplineName;
    }


    public void setUplineEmailId(String uplineEmailId) {
        this.uplineEmailId = uplineEmailId;
    }

    public void setUplineMobile(String uplineMobile) {
        this.uplineMobile = uplineMobile;
    }

    public void setUplineName(String uplineName) {
        this.uplineName = uplineName;
    }
}
