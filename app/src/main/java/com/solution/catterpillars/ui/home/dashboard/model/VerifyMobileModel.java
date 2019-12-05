package com.solution.catterpillars.ui.home.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyMobileModel {
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Message")
    @Expose
    private String message;

    @SerializedName("CurrentApiIndex")
    @Expose
    private String currentApiIndex;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCurrentApiIndex(String currentApiIndex) {
        this.currentApiIndex = currentApiIndex;
    }

    public String getCurrentApiIndex() {

        return currentApiIndex;
    }
}
