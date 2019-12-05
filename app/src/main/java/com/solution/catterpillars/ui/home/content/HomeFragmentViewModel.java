package com.solution.catterpillars.ui.home.content;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.solution.catterpillars.base.BaseViewModel;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 12-Nov-18
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 * About : I am a mathematician
 * Quote : Only brain can make anything possible
 * Strength : Never give up
 */
public class HomeFragmentViewModel extends BaseViewModel {
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Url")
    @Expose
    private String url;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
