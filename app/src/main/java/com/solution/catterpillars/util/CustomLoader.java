package com.solution.catterpillars.util;

import android.app.Dialog;
import android.content.Context;

import com.solution.catterpillars.R;


/**
 * Created by admin on 4/9/2016.
 */


public class CustomLoader extends Dialog {



   public CustomLoader(Context context, int theme) {
       super(context, theme);
       // TODO Auto-generated constructor stub
      setContentView(R.layout.custom_loader_dialog);
   }



}
