package com.solution.catterpillars.ui.registration.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 02-Nov-18
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 * About : I am a mathematician
 * Quote : Only brain can make anything possible
 * Strength : Never give up
 */
public class ResultRegistration {
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Message")
    @Expose
    private String message;

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
}
