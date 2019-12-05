package com.solution.catterpillars.ui.login.country_flag;

import android.content.Context;
import android.view.View;

import com.solution.catterpillars.R;
import com.solution.catterpillars.base.BaseAdapter;
import com.solution.catterpillars.databinding.CountryFlagAdapterBinding;
import com.solution.catterpillars.ui.home.Home;
import com.solution.catterpillars.ui.login.Login;
import com.solution.catterpillars.ui.login.forget_password.ForgetPassword;
import com.solution.catterpillars.ui.login.model.List;
import com.solution.catterpillars.ui.registration.Registration;

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
public class CountryFlagAdapter extends BaseAdapter<CountryFlagAdapterBinding, List> {

    private final Context contextLogin;
    private final CountryFlagDialog countryFlagDialog;
    SelectedDataCallBack selectedDataCallBack;

    /**
     * @param context           reference of context
     * @param list              DataList
     * @param countryFlagDialog
     */
    public CountryFlagAdapter(Context context, ArrayList<List> list, CountryFlagDialog countryFlagDialog) {
        super(context, list);
        this.contextLogin = context;
        this.countryFlagDialog = countryFlagDialog;
    }

    public CountryFlagAdapter(Context context, ArrayList<List> list, CountryFlagDialog countryFlagDialog,
                              SelectedDataCallBack selectedDataCallBack) {
        super(context, list);
        this.contextLogin = context;
        this.countryFlagDialog = countryFlagDialog;
        this.selectedDataCallBack = selectedDataCallBack;
    }

    /**
     * This will hide the view on first load
     * left blank if no view has to hide
     *
     * @param binding DataBinding
     */
    @Override
    protected void hideView(CountryFlagAdapterBinding binding) {

    }

    /**
     * This is use to make the hidden view on first load
     * left blank if noting is hidden
     *
     * @param binding DataBinding
     */
    @Override
    protected void showView(CountryFlagAdapterBinding binding) {

    }

    /**
     * @return R.layout.layout_file
     */
    @Override
    protected int getLayout() {
        return R.layout.country_flag_adapter;
    }

    /**
     * Initialised View Holder
     *
     * @param binding DataBinding
     * @return new ViewHolder(binding);
     */
    @Override
    public ViewHolder getViewHolder(CountryFlagAdapterBinding binding) {
        return new ViewHolder(binding) {
            /**
             * If there is anything to do then do here otherwise leave it blank
             *
             * @param binding  layout reference for single view
             * @param data     for single view
             * @param position position of ArrayList
             */
            @Override
            protected void doSomeWorkHere(CountryFlagAdapterBinding binding, List data, int position) {

            }

            /**
             * @param data binding.setData(data);
             */
            @Override
            protected void bindData(List data) {
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
                                          CountryFlagAdapterBinding binding, List data) {
                binding.layout.setOnClickListener(thisContext);
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
            protected ViewHolderClickListener viewHolderReference(CountryFlagAdapterBinding binding,
                                                                  List data, int position) {
                return new ViewHolderClickListener(binding, position) {
                    /**
                     * Called when a view has been clicked.
                     *
                     * @param v The view that was clicked.
                     */
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.layout:
                                if (contextLogin instanceof Login || contextLogin instanceof Registration ||
                                        contextLogin instanceof ForgetPassword) {
                                    ((FlagNavigator) contextLogin).countryCode(data.getPhonePrefix(), data.getId());
                                } else if (contextLogin instanceof Home) {
                                    if (selectedDataCallBack != null) {
                                        selectedDataCallBack.getSelectedData(data.getPhonePrefix(), data.getId());
                                    }
                                }

                                countryFlagDialog.dismiss();
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
        return new FilterClass() {
            @Override
            public ArrayList<List> filterData(CharSequence constraint, ArrayList<List> list,
                                              ArrayList<List> filteredArrayList) {

                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    return list;
                } else {
                    for (List row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getISOCode1().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getCountryName().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getISOCode2().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getPhonePrefix().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getCapital().toLowerCase().contains(charString.toLowerCase())) {

                            filteredArrayList.add(row);
                        }
                    }
                    return filteredArrayList;

                }

            }
        };
    }


    public interface SelectedDataCallBack {
        void getSelectedData(String code, String id);
    }
}
