package com.solution.catterpillars.ui.home.dashboard.wallet.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.solution.catterpillars.base.BaseViewModel;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 12/14/2018
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 */
public class WalletViewModel extends BaseViewModel {
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("PageIndex")
    @Expose
    private String pageIndex;
    @SerializedName("MaxIndex")
    @Expose
    private String maxIndex;
    @SerializedName("CrAmount")
    @Expose
    private String crAmount;
    @SerializedName("DrAmount")
    @Expose
    private String drAmount;
    @SerializedName("List")
    @Expose
    private java.util.List<List> list = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(String pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getMaxIndex() {
        return maxIndex;
    }

    public void setMaxIndex(String maxIndex) {
        this.maxIndex = maxIndex;
    }

    public String getCrAmount() {
        if (crAmount != null && crAmount.contains(".")) {
            crAmount = crAmount.substring(0, crAmount.indexOf(".") + 3);
            return "\u20B9 "+crAmount;
        }
        return "\u20B9 "+crAmount;
    }

    public void setCrAmount(String crAmount) {
        this.crAmount = crAmount;
    }

    public String getDrAmount() {
        if (drAmount != null && drAmount.contains(".")) {
            drAmount = drAmount.substring(0, drAmount.indexOf(".") + 3);
            return "\u20B9 "+drAmount;
        }
        return "\u20B9 "+drAmount ;
    }

    public void setDrAmount(String drAmount) {
        this.drAmount = drAmount;
    }

    public java.util.List<List> getList() {
        return list;
    }

    public void setList(java.util.List<List> list) {
        this.list = list;
    }
}
