package com.solution.catterpillars.data.remort.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arvindo Mondal on 5/22/2018.
 * Email : arvindomondal@gmail.com
 * Designation : Programmer and Developer
 * Skills : Logic and Algorithm Creator and Inventor
 * Logo : Never Give Up
 */
public class Result {
    @SerializedName(value = "Status", alternate = "STATUS")
    @Expose
    private String status;

//    @SerializedName("Data")
//    @Expose
//    private List<Data> data;
//
//    @SerializedName("UserName")
//    @Expose
//    private List<UserName> userName;
//
//    @SerializedName("JSONObject")
//    @Expose
//    private List<InspectionCallData> dataIC;
//
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public List<Data> getData() {
//        return data;
//    }
//
//    public void setData(List<Data> data) {
//        this.data = data;
//    }
//
//    public List<UserName> getUserName() {
//        return userName;
//    }
//
//    public void setUserName(List<UserName> userName) {
//        this.userName = userName;
//    }
//
//    public List<InspectionCallData> getDataIC() {
//        return dataIC;
//    }
//
//    public void setDataIC(List<InspectionCallData> dataIC) {
//        this.dataIC = dataIC;
//    }
}
