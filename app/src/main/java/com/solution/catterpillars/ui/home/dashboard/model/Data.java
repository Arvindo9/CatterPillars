
package com.solution.catterpillars.ui.home.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("Member")
    @Expose
    private List<DataList> member = null;
    @SerializedName("Wallet")
    @Expose
    private List<DataList> wallet = null;
    @SerializedName("TodayIncome")
    @Expose
    private List<DataList> todayIncome = null;

    public List<DataList> getMember() {
        return member;
    }

    public void setMember(List<DataList> member) {
        this.member = member;
    }

    public List<DataList> getWallet() {
        return wallet;
    }

    public void setWallet(List<DataList> wallet) {
        this.wallet = wallet;
    }

    public List<DataList> getTodayIncome() {
        return todayIncome;
    }

    public void setTodayIncome(List<DataList> todayIncome) {
        this.todayIncome = todayIncome;
    }
}
