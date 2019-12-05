
package com.solution.catterpillars.ui.login.model;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.solution.catterpillars.R;
import com.solution.catterpillars.base.BaseViewModel;
import com.squareup.picasso.Picasso;

public class Data extends BaseViewModel {

    @SerializedName("SessionID")
    @Expose
    private String sessionID;
    @SerializedName("UserID")
    @Expose
    private String userID;
    @SerializedName("UName")
    @Expose
    private String uName;
    @SerializedName("UMobile")
    @Expose
    private String uMobile;
    @SerializedName("IsExist")
    @Expose
    private String isExist;
    @SerializedName("DeviceStatus")
    @Expose
    private String deviceStatus;
    @SerializedName("Key")
    @Expose
    private String key;
    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("UEmail")
    @Expose
    private String email;

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUName() {
        return uName;
    }

    public void setUName(String uName) {
        this.uName = uName;
    }

    public String getUMobile() {
        return uMobile;
    }

    public void setUMobile(String uMobile) {
        this.uMobile = uMobile;
    }

    public String getIsExist() {
        return isExist;
    }

    public void setIsExist(String isExist) {
        this.isExist = isExist;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
