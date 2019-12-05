package com.solution.catterpillars.ui.home.setting.edit_profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.solution.catterpillars.R;
import com.solution.catterpillars.base.BaseDialog;
import com.solution.catterpillars.ui.home.setting.edit_profile.model.EditProfileViewModel;
import com.solution.catterpillars.ui.login.country_flag.CountryFlagAdapter;
import com.solution.catterpillars.ui.login.country_flag.CountryFlagDialog;
import com.solution.catterpillars.databinding.EditProfileDialogBinding;
import com.solution.catterpillars.ui.login.model.List;

import java.util.ArrayList;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 12/14/2018
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 */
public class ProfileDialog extends DialogFragment {

    public static final String TAG = ProfileDialog.class.getSimpleName();
    private Context context;
    private ArrayList<EditProfileViewModel> list;
    private RecyclerView mRecyclerView;
    private ProfileAdapter adapter;

    public ProfileDialog(){
        super();
    }

    @SuppressLint("ValidFragment")
    public ProfileDialog(Context context, ArrayList<EditProfileViewModel> list){
        this();
        this.context = context;
        this.list = list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate layout with recycler view
        View v = inflater.inflate(R.layout.edit_profile_dialog, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.listView);
        View backBtn = v.findViewById(R.id.backBtn);
        View clearBtn = v.findViewById(R.id.clearBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        //setadapter
        adapter = new ProfileAdapter(context, list);
        mRecyclerView.setAdapter(adapter);


        return v;
    }

}
