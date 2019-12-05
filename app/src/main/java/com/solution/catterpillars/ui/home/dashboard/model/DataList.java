package com.solution.catterpillars.ui.home.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.solution.catterpillars.base.BaseViewModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by KinG on 10-12-2018.
 * Created by ${EMAIL}.
 */
public class DataList extends BaseViewModel implements Serializable {

    public String VIEW_TYPE = "";

    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Url")
    @Expose
    private String url;
    @SerializedName("Count")
    @Expose
    private String count;
    @SerializedName("Link1")
    @Expose
    private String link1;
    @SerializedName("Link2")
    @Expose
    private String link2;
    @SerializedName("Amount")
    @Expose
    private String amount;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Income")
    @Expose
    private String income;
    @SerializedName("Pending")
    @Expose
    private String pending;
    @SerializedName("Completed")
    @Expose
    private String completed;
    @SerializedName("Total")
    @Expose
    private String total;
    @SerializedName("ButtonText")
    @Expose
    private String buttonText;

    @SerializedName("MobileVerificationLink")
    @Expose
    private List<MobileVerificationLink> mobileVerificationLink = null;

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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getLink1() {
        return link1;
    }

    public void setLink1(String link1) {
        this.link1 = link1;
    }

    public String getLink2() {
        return link2;
    }

    public void setLink2(String link2) {
        this.link2 = link2;
    }

    public String getAmount() {
        if (amount != null && amount.contains(".")) {
            amount = amount.substring(0, amount.indexOf(".") + 3);
            return "\u20B9 "+amount;
        }
        return "\u20B9 "+amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIncome()
    {
        if (income != null && income.contains(".")) {
            income = income.substring(0, income.indexOf(".") + 3);
            return "\u20B9 "+income;
        }
        return "\u20B9 "+income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getPending() {
        return pending;
    }

    public void setPending(String pending) {
        this.pending = pending;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getButtonText() {

        if (buttonText == null && getType().equalsIgnoreCase("Mobile")) {
            return "Verify Mobile No";
        } else if (buttonText == null && getType().equalsIgnoreCase("Email")) {
            return "Verify Email Id";
        } else if (buttonText == null && getType().equalsIgnoreCase("Bank")) {
            return "Verify Bank Detail";
        }
        return buttonText;


    }

    public void setMobileVerificationLink(List<MobileVerificationLink> mobileVerificationLink) {
        this.mobileVerificationLink = mobileVerificationLink;
    }

    public List<MobileVerificationLink> getMobileVerificationLink() {

        return mobileVerificationLink;
    }
}
