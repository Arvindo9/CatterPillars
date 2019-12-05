package com.solution.catterpillars.ui.home.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MobileVerificationLink {

    @SerializedName("ID")
    @Expose
    private String id;
    @SerializedName("ApiName")
    @Expose
    private String apiName;

    public String getId() {
        return id;
    }

    public String getApiName() {
        return apiName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }
}
