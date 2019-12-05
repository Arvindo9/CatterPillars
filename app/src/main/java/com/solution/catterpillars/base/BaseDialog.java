package com.solution.catterpillars.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.solution.catterpillars.R;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 12/14/2018
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 */
public abstract class BaseDialog<B extends ViewDataBinding> extends DialogFragment {
    protected B binding;
    private int mNum;

//    static BaseDialog newInstance(int num) {
//        BaseDialog dialog = new BaseDialog();
//        Bundle args = new Bundle();
//        args.putInt("num", num);
//        dialog.setArguments(args);
//        return dialog;
//    }

    public BaseDialog() {
        super();
    }

    @SuppressLint("ValidFragment")
    public BaseDialog(Context context) {
        this();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments().getInt("num", 1);

        // Pick a style based on the num.
        int style = DialogFragment.STYLE_NORMAL, theme = 0;
        switch ((mNum-1)%6) {
            case 1: style = DialogFragment.STYLE_NO_TITLE; break;
            case 2: style = DialogFragment.STYLE_NO_FRAME; break;
            case 3: style = DialogFragment.STYLE_NO_INPUT; break;
            case 4: style = DialogFragment.STYLE_NORMAL; break;
            case 5: style = DialogFragment.STYLE_NORMAL; break;
            case 6: style = DialogFragment.STYLE_NO_TITLE; break;
            case 7: style = DialogFragment.STYLE_NO_FRAME; break;
            case 8: style = DialogFragment.STYLE_NORMAL; break;
        }
        switch ((mNum-1)%6) {
            case 4: theme = android.R.style.Theme_Holo; break;
            case 5: theme = android.R.style.Theme_Holo_Light_Dialog; break;
            case 6: theme = android.R.style.Theme_Holo_Light; break;
            case 7: theme = android.R.style.Theme_Holo_Light_Panel; break;
            case 8: theme = android.R.style.Theme_Holo_Light; break;
        }
        setStyle(style, theme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater,
                getLayout(), container, false);

        init(inflater, container, savedInstanceState);

        return binding.getRoot();
    }

    /**
     *
     * @param inflater LayoutInflater
     * @param container LayoutInflater
     * @param savedInstanceState Bundle
     */
    protected abstract void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);


    /**
     *
     * @return R.layout.layout_file
     */
    @LayoutRes
    protected abstract int getLayout();
}
