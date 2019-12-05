package com.solution.catterpillars.ui.home.dashboard.member.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.solution.catterpillars.base.BaseViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 12/15/2018
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 */
public class List extends BaseViewModel {

    DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
    Date date = null;
    @SerializedName("MemberName")
    @Expose
    private String memberName;
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("RegDate")
    @Expose
    private String regDate="";

    @SerializedName("RegTime")
    @Expose
    private String regTime;
    @SerializedName("SignupTaskStatus")
    @Expose
    private String signupTaskStatus;
    @SerializedName("DailyTaskStatus")
    @Expose
    private String dailyTaskStatus;
    @SerializedName("UserCount")
    @Expose
    private String userCount;


    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegDate() {
        regDate= regDate!=null?regDate:"";
        try {
            date = inputFormat.parse(regDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return regDate;
        }
        return outputFormat.format(date);
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getSignupTaskStatus() {
        return signupTaskStatus;
    }

    public void setSignupTaskStatus(String signupTaskStatus) {
        this.signupTaskStatus = signupTaskStatus;
    }

    public String getDailyTaskStatus() {
        return dailyTaskStatus;
    }

    public void setDailyTaskStatus(String dailyTaskStatus) {
        this.dailyTaskStatus = dailyTaskStatus;
    }

    public String getUserCount() {
        return userCount;
    }

    public void setUserCount(String userCount) {
        this.userCount = userCount;

    }
}
