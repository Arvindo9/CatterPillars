package com.solution.catterpillars.ui.home.dashboard;

import android.content.Context;
import android.view.View;

import com.solution.catterpillars.R;
import com.solution.catterpillars.base.BaseAdapter;
import com.solution.catterpillars.databinding.DashboardMemberAdapterBinding;
import com.solution.catterpillars.ui.home.Home;
import com.solution.catterpillars.ui.home.dashboard.income.IncomeFragment;
import com.solution.catterpillars.ui.home.dashboard.member.MemberFragment;
import com.solution.catterpillars.ui.home.dashboard.model.DataList;
import com.solution.catterpillars.ui.home.dashboard.wallet.WalletFragment;

import java.util.ArrayList;

import static com.solution.catterpillars.ui.home.dashboard.income.IncomeFragment.TODAYS_INCOME;
import static com.solution.catterpillars.ui.home.dashboard.member.MemberFragment.THIS_MONTH_MEMBER;
import static com.solution.catterpillars.ui.home.dashboard.member.MemberFragment.TODAYS_MEMBER;
import static com.solution.catterpillars.ui.home.dashboard.member.MemberFragment.TOTAL_MEMBER;
import static com.solution.catterpillars.ui.home.dashboard.wallet.WalletFragment.PAYMENT_WALLET;
import static com.solution.catterpillars.ui.home.dashboard.wallet.WalletFragment.SHOPING_WALLET;
import static com.solution.catterpillars.ui.home.dashboard.wallet.WalletFragment.WALLET;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 12/10/2018
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 */
public class DashboardDataListAdapter extends BaseAdapter<DashboardMemberAdapterBinding, DataList> {

    private final String dataName;

    public DashboardDataListAdapter(Context context, String dataName, ArrayList<DataList> data) {
        super(context, data);
        this.dataName = dataName;
    }

    /**
     * This will hide the view on first load
     * left blank if no view has to hide
     *
     * @param binding DataBinding
     */
    @Override
    protected void hideView(DashboardMemberAdapterBinding binding) {

    }

    /**
     * This is use to make the hidden view on first load
     * left blank if noting is hidden
     *
     * @param binding DataBinding
     */
    @Override
    protected void showView(DashboardMemberAdapterBinding binding) {

    }

    /**
     * @return R.layout.layout_file
     */
    @Override
    protected int getLayout() {
        return R.layout.dashboard_member_adapter;
    }

    /**
     * Initialised View Holder
     *
     * @param binding DataBinding
     * @return new ViewHolder(binding);
     */
    @Override
    public ViewHolder getViewHolder(DashboardMemberAdapterBinding binding) {
        return new ViewHolder(binding){
            /**
             * If there is anything to do then do here otherwise leave it blank
             *
             * @param binding  layout reference for single view
             * @param data     for single view
             * @param position position of ArrayList
             */
            @Override
            protected void doSomeWorkHere(DashboardMemberAdapterBinding binding, DataList data, int position) {
                binding.title.setText(data.getTitle());
                if (dataName.equalsIgnoreCase("Wallet")) {
                    binding.layout.setBackgroundResource(R.drawable.ic_box_blue);
                    binding.count.setText(data.getAmount());
                } else if (dataName.equalsIgnoreCase("Member")) {
                    binding.layout.setBackgroundResource(R.drawable.ic_box_orange);
                    binding.count.setText(data.getCount());
                } else if (dataName.equalsIgnoreCase("Income")) {
                    binding.layout.setBackgroundResource(R.drawable.ic_box_green);
                    binding.count.setText(data.getIncome());
                }
            }

            /**
             * @param data binding.setData(data);
             */
            @Override
            protected void bindData(DataList data) {
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
            public void setClickListeners(ViewHolderClickListener thisContext, DashboardMemberAdapterBinding binding, DataList data) {
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
            protected ViewHolderClickListener viewHolderReference(DashboardMemberAdapterBinding binding,
                                                                  DataList data, int position) {
                return new ViewHolderClickListener(binding, position) {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.layout:
                                switch (data.getTitle()){
                                    case "Wallet":
                                        ((Home) context).fragment =
                                                WalletFragment.newInstance(data.getTitle(), WALLET);
                                        ((Home) context).openFragment(((Home) context).fragment,
                                                WalletFragment.TAG);
                                        break;
                                    case "Payment Wallet":
                                        ((Home) context).fragment =
                                                WalletFragment.newInstance(data.getTitle(), PAYMENT_WALLET);
                                        ((Home) context).openFragment(((Home) context).fragment,
                                                WalletFragment.TAG);
                                        break;
                                    case "Shopping Wallet":
                                        ((Home) context).fragment =
                                                WalletFragment.newInstance(data.getTitle(), SHOPING_WALLET);
                                        ((Home) context).openFragment(((Home) context).fragment,
                                                WalletFragment.TAG);
                                        break;

                                    case "Today's Income":
                                        ((Home) context).fragment =
                                                IncomeFragment.newInstance(data.getTitle(), TODAYS_INCOME);
                                        ((Home) context).openFragment(((Home) context).fragment,
                                                IncomeFragment.TAG);
                                        break;

                                    case "Today's Member":
                                        ((Home) context).fragment =
                                                MemberFragment.newInstance(data.getTitle(), TODAYS_MEMBER);
                                        ((Home) context).openFragment(((Home) context).fragment,
                                                MemberFragment.TAG);
                                        break;

                                    case "This Month's Member":
                                        ((Home) context).fragment =
                                                MemberFragment.newInstance(data.getTitle(), THIS_MONTH_MEMBER);
                                        ((Home) context).openFragment(((Home) context).fragment,
                                                MemberFragment.TAG);
                                        break;

                                    case "Total Member":
                                        ((Home) context).fragment =
                                                MemberFragment.newInstance(data.getTitle(), TOTAL_MEMBER);
                                        ((Home) context).openFragment(((Home) context).fragment,
                                                MemberFragment.TAG);
                                        break;


                                }

//                                if (dataName.equalsIgnoreCase("Wallet")) {
//
//
//                                } else if (dataName.equalsIgnoreCase("Member")) {
//                                } else if (dataName.equalsIgnoreCase("Income")) {
//                                }


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
