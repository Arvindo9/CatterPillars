package com.solution.catterpillars.ui.login.country_flag;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.solution.catterpillars.R;
import com.solution.catterpillars.ui.login.model.List;

import java.util.ArrayList;

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
public class CountryFlagDialog extends DialogFragment {

    private Context context;
    private ArrayList<List> flagList;
    private String TAG = CountryFlagDialog.class.getSimpleName();
    FlagNavigator flagNavigator;

    public CountryFlagDialog() {
        super();
    }

  /*  @SuppressLint("ValidFragment")
    public CountryFlagDialog(Context context, ArrayList<List> flagList){
        this();
        this.context = context;
        this.flagList = flagList;
    }*/

    @SuppressLint("ValidFragment")
    public CountryFlagDialog(Context context, ArrayList<List> flagList, FlagNavigator flagNavigator) {
        this();
        this.context = context;
        this.flagList = flagList;
        this.flagNavigator = flagNavigator;
    }

    @Override
    public void onResume() {
        super.onResume();
       /* ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);*/
    }

    private RecyclerView mRecyclerView;
    private CountryFlagAdapter adapter;
    EditText searchEditText;

    // this method create view for your Dialog
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate layout with recycler view
        View v = inflater.inflate(R.layout.dialog_country_flag, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.listView);
        View backBtn = v.findViewById(R.id.backBtn);
        View clearBtn = v.findViewById(R.id.clearBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEditText.setText("");
            }
        });

        searchEditText = v.findViewById(R.id.search);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        //setadapter
        if (flagNavigator != null) {
            adapter = new CountryFlagAdapter(context, flagList, CountryFlagDialog.this,
                    new CountryFlagAdapter.SelectedDataCallBack() {
                @Override
                public void getSelectedData(String code, String id) {
                    flagNavigator.countryCode(code, id);
                }
            });
        } else {
            adapter = new CountryFlagAdapter(context, flagList, CountryFlagDialog.this);
        }
        mRecyclerView.setAdapter(adapter);
        //get your recycler view and populate it.
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (clearBtn.getVisibility() == View.GONE) {
                        clearBtn.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (clearBtn.getVisibility() == View.VISIBLE) {
                        clearBtn.setVisibility(View.GONE);
                    }
                }
                adapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return v;
    }


}
