package com.solution.catterpillars.util;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import com.solution.catterpillars.R;
import com.solution.catterpillars.ui.home.dashboard.wallet.WalletFragment;
import com.solution.catterpillars.ui.interfaces.DateInterface;

import java.util.Calendar;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 12/15/2018
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 */
public class DatePickerFragment extends DialogFragment
        implements android.app.DatePickerDialog.OnDateSetListener {

    public static final String TAG = DatePickerFragment.class.getSimpleName();
    private DateInterface dateInterface;
    private Context context;
    private String[] params;
    private int selectedDate;

    public DatePickerFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public DatePickerFragment(Context context, DateInterface object) {
        this();
        this.dateInterface = object;
        this.context = context;
    }

    @SuppressLint("ValidFragment")
    public DatePickerFragment(Context context, DateInterface object, String... params) {
        this();
        this.dateInterface = object;
        this.context = context;
        this.params = params;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new android.app.DatePickerDialog(context, R.style.MainTheme_Dialog,
                this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        month++;
        String monthStr = "";
        String dayStr = String.valueOf(day);

        selectedDate = year;
        selectedDate=selectedDate*100 + month;
        selectedDate=selectedDate*100 + day;

        switch (month) {
            case 1:
                monthStr = "Jan";
                break;
            case 2:
                monthStr = "Feb";
                break;
            case 3:
                monthStr = "Mar";
                break;
            case 4:
                monthStr = "Apr";
                break;
            case 5:
                monthStr = "May";
                break;
            case 6:
                monthStr = "Jun";
                break;
            case 7:
                monthStr = "Jul";
                break;
            case 8:
                monthStr = "Aug";
                break;
            case 9:
                monthStr = "Sep";
                break;
            case 10:
                monthStr = "Oct";
                break;
            case 11:
                monthStr = "Nov";
                break;
            case 12:
                monthStr = "Dec";
                break;
        }

        if (dayStr.length() < 2) {
            dayStr = "0" + String.valueOf(day);
        }

//        String date = monthStr + " " + dayStr + ", " + String.valueOf(year);
        String date = month + "/" + dayStr + "/" + String.valueOf(year);
        Log.e("date select", date);
        dateInterface.date(date, params);
        this.dismiss();
    }
}
