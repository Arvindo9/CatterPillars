package com.solution.catterpillars.ui.lockScreen;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.solution.catterpillars.R;
import com.solution.catterpillars.base.BaseAdapter;
import com.solution.catterpillars.databinding.LockScreenNewsAdapterBinding;
import com.solution.catterpillars.ui.lockScreen.model.NewsData;
import com.solution.catterpillars.util.ApplicationConstant;
import com.solution.catterpillars.util.AppsFunction;

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
public class LockScreenNewsAdapter extends BaseAdapter<LockScreenNewsAdapterBinding, NewsData> {

    private final LockScreenActivity activityLockScreen;

    public LockScreenNewsAdapter(Context context, LockScreenActivity lockScreenActivity,
                                 ArrayList<NewsData> list) {
        super(context, list);
        this.activityLockScreen= lockScreenActivity;
    }

    /**
     * This will hide the view on first load
     * left blank if no view has to hide
     *
     * @param binding DataBinding
     */
    @Override
    protected void hideView(LockScreenNewsAdapterBinding binding) {

    }

    /**
     * This is use to make the hidden view on first load
     * left blank if noting is hidden
     *
     * @param binding DataBinding
     */
    @Override
    protected void showView(LockScreenNewsAdapterBinding binding) {

    }

    /**
     * @return R.layout.layout_file
     */
    @Override
    protected int getLayout() {
        return R.layout.lock_screen_news_adapter;
    }

    /**
     * Initialised View Holder
     *
     * @param binding DataBinding
     * @return new ViewHolder(context, this, binding);
     */
    @Override
    public ViewHolder getViewHolder(LockScreenNewsAdapterBinding binding) {
        return new ViewHolder(binding){
            /**
             * If there is anything to do then do here otherwise leave it blank
             *
             * @param binding  layout reference for single view
             * @param data     for single view
             * @param position position of ArrayList
             */
            @Override
            protected void doSomeWorkHere(LockScreenNewsAdapterBinding binding, NewsData data, int position) {

            }

            /**
             * @param data binding.setData(data);
             */
            @Override
            protected void bindData(NewsData data) {
                binding.setData(data);
            }

            /**
             * Method to set click listeners on view holder or groups
             *
             * @param thisContext set it on method : binding.layout.setOnClickListener(thisContext);
             * @param binding     DataBinding
             * @param data        data
             */
            @Override
            public void setClickListeners(ViewHolderClickListener thisContext,
                                          LockScreenNewsAdapterBinding binding, NewsData data) {
                binding.newCard.setOnClickListener(thisContext);
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
            protected ViewHolderClickListener viewHolderReference(LockScreenNewsAdapterBinding binding,
                                                                  NewsData data, int position) {
                return new ViewHolderClickListener(binding, position){
                    /**
                     * Called when a view has been clicked.
                     *
                     * @param v The view that was clicked.
                     */
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()){
                            case R.id.newCard:
                                //TODO on news card click

//                                AppsFunction.launchNewApp(context, ApplicationConstant.UP50_PACKAGE_NAME,
//                                        data.getKey(), ApplicationConstant.INSTANCE.BASE_URL_UP_50 +
//                                                "/" +
//                                                data.getUrl());

                                AppsFunction.launchNewApp(context, ApplicationConstant.UP50_PACKAGE_NAME,
                                        data.getUrl(),
                                        data.getKey(), ApplicationConstant.INSTANCE.BASE_URL_UP_50 +
                                                "/" +
                                                data.getUrl());
                                break;
                        }
                    }
                };
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
