package com.solution.catterpillars.ui.home;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.solution.catterpillars.R;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 10-Nov-18
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 * About : I am a mathematician
 * Quote : Only brain can make anything possible
 * Strength : Never give up
 */
public class HomeTemp extends AppCompatActivity {
    public static Intent newIntent(Context context) {
        return new Intent(context, HomeTemp.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

//        NavHeaderBinding _bind = DataBindingUtil.inflate(getLayoutInflater(), R.layout.nav_header, binding
//                .navView, false);
    }
}
