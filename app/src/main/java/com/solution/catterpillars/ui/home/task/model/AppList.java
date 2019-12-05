package com.solution.catterpillars.ui.home.task.model;

import android.arch.lifecycle.ViewModel;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

import com.solution.catterpillars.R;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 01-Nov-18
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 * About : I am a mathematician
 * Quote : Only brain can make anything possible
 * Strength : Never give up
 */
public class AppList extends ViewModel implements Serializable {
    @SerializedName("AppName")
    @Expose
    private String AppName;
    
    @SerializedName("packagePresent")
    @Expose
    private boolean packagePresent;
    @SerializedName("setid")
    @Expose
    private String setid;
    @SerializedName("urlid")
    @Expose
    private String urlid;
    @SerializedName("taskid")
    @Expose
    private String taskid;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("packagename")
    @Expose
    private String packagename;
    @SerializedName("imgurl")
    @Expose
    private String imgurl;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("appname")
    @Expose
    private String appname;

    public String getSetid() {
        return setid;
    }

    public void setSetid(String setid) {
        this.setid = setid;
    }

    public String getUrlid() {
        return urlid;
    }

    public void setUrlid(String urlid) {
        this.urlid = urlid;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @BindingAdapter({"bind:imgurl"})
    public static void loadImage(ImageView view, String imgurl) {
        Picasso.get()
                .load(imgurl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(view);
    }

    public boolean isPackagePresent() {
        return packagePresent;
    }

    public void setPackagePresent(boolean packagePresent) {
        this.packagePresent = packagePresent;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }
}
