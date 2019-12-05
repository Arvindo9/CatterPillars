package com.solution.catterpillars.ui.home.dashboard.wallet.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.solution.catterpillars.base.BaseViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 12/14/2018
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 */
public class List extends BaseViewModel {


    DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
    Date date = null;

    @SerializedName("TransactionId")
    @Expose
    private String transactionId;
    @SerializedName("TransactionDate")
    @Expose
    private String transactionDate;
    @SerializedName("TransactionTime")
    @Expose
    private String transactionTime;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("DebitAmount")
    @Expose
    private String debitAmount;
    @SerializedName("CreditAmount")
    @Expose
    private String creditAmount;
    @SerializedName("CurrentBalance")
    @Expose
    private String currentBalance;
    @SerializedName("FromUser")
    @Expose
    private String fromUser;
    @SerializedName("FromUserMobile")
    @Expose
    private String fromUserMobile;

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getFromUserMobile() {
        return fromUserMobile;
    }

    public void setFromUserMobile(String fromUserMobile) {
        this.fromUserMobile = fromUserMobile;
    }


    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionDate() {

        try {
            date = inputFormat.parse(transactionDate!=null?transactionDate:"");
        } catch (ParseException e) {
            e.printStackTrace();
            return transactionDate;
        }
        return outputFormat.format(date);
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }
    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDebitAmount() {
        if (debitAmount != null && debitAmount.contains(".")) {
            debitAmount = debitAmount.substring(0, debitAmount.indexOf(".") + 3);
            return "\u20B9 "+debitAmount;
        }
        return "\u20B9 "+debitAmount;
    }

    public void setDebitAmount(String debitAmount) {
        this.debitAmount = debitAmount;
    }

    public String getCreditAmount() {
        if (creditAmount != null && creditAmount.contains(".")) {
            creditAmount = creditAmount.substring(0, creditAmount.indexOf(".") + 3);
            return "\u20B9 "+creditAmount;
        }
        return "\u20B9 "+creditAmount;
    }

    public void setCreditAmount(String creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String getCurrentBalance() {
        if (currentBalance != null && currentBalance.contains(".")) {
            currentBalance = currentBalance.substring(0, currentBalance.indexOf(".") + 3);
            return "\u20B9 "+currentBalance;
        }
        return "\u20B9 "+currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }
}
