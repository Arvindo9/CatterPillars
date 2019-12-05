package com.solution.catterpillars.ui.home.setting.edit_profile;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.solution.catterpillars.R;
import com.solution.catterpillars.base.BaseActivity;
import com.solution.catterpillars.base.BaseAdapter;
import com.solution.catterpillars.base.BaseViewModel;
import com.solution.catterpillars.databinding.EditProfileAdapterBinding;
import com.solution.catterpillars.ui.home.setting.edit_profile.model.EditProfileViewModel;

import java.util.ArrayList;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 12/14/2018
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 */
public class ProfileAdapter extends BaseAdapter<EditProfileAdapterBinding, EditProfileViewModel> {

    /**
     * @param context reference of context
     * @param list    DataList
     */
    public ProfileAdapter(Context context, ArrayList<EditProfileViewModel> list) {
        super(context, list);
    }

    /**
     * This will hide the view on first load
     * left blank if no view has to hide
     *
     * @param binding DataBinding
     */
    @Override
    protected void hideView(EditProfileAdapterBinding binding) {

    }

    /**
     * This is use to make the hidden view on first load
     * left blank if noting is hidden
     *
     * @param binding DataBinding
     */
    @Override
    protected void showView(EditProfileAdapterBinding binding) {

    }

    /**
     * @return R.layout.layout_file
     */
    @Override
    protected int getLayout() {
        return R.layout.edit_profile_adapter;
    }

    /**
     * Initialised View Holder
     *
     * @param binding DataBinding
     * @return new ViewHolder(binding);
     */
    @Override
    public ViewHolder getViewHolder(EditProfileAdapterBinding binding) {
        return new ViewHolder(binding){
            /**
             * If there is anything to do then do here otherwise leave it blank
             *
             * @param binding  layout reference for single view
             * @param data     for single view
             * @param position position of ArrayList
             */
            @Override
            protected void doSomeWorkHere(EditProfileAdapterBinding binding, EditProfileViewModel data, int position) {

            }

            /**
             * @param data binding.setData(data);
             */
            @Override
            protected void bindData(EditProfileViewModel data) {

            }

            /**
             * Method to set click listeners on view holder or groups
             *
             * @param thisContext set it on method : binding.layout.setOnClickListener(thisContext);
             * @param binding     DataBinding
             * @param data        data
             */
            @Override
            public void setClickListeners(ViewHolderClickListener thisContext, EditProfileAdapterBinding binding, EditProfileViewModel data) {

            }

            /**
             * Initialised holder by new operator
             *
             * @param binding  DataBinding
             * @param data     dataList
             * @param position of adapter
             * @return new ViewHolderClickListener(binding, data, position)
             */
            @Override
            protected ViewHolderClickListener viewHolderReference(EditProfileAdapterBinding binding, EditProfileViewModel data, int position) {
                return null;
            }
        };
    }

    /**
     * @return new FilterClass();
     */
    @Override
    protected FilterClass initialisedFilterClass() {
        return null;
    }
}
