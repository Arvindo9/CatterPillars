
package com.solution.catterpillars.ui.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.solution.catterpillars.base.BaseViewModel;

import java.io.Serializable;

public class List extends BaseViewModel implements Serializable {

    @SerializedName("MenuName")
    @Expose
    private String menuName;
    @SerializedName("MenuUrl")
    @Expose
    private String menuUrl;

    public List(String menuName, String menuUrl) {
        this.menuName=menuName;
        this.menuUrl=menuUrl;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

}
