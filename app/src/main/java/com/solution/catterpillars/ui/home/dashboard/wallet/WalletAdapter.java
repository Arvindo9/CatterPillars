package com.solution.catterpillars.ui.home.dashboard.wallet;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;

import com.solution.catterpillars.R;
import com.solution.catterpillars.base.ActivityCalls;
import com.solution.catterpillars.base.BaseActivity;
import com.solution.catterpillars.base.BaseAdapter;
import com.solution.catterpillars.databinding.WalletListAdapterBinding;
import com.solution.catterpillars.ui.home.dashboard.wallet.model.List;
import com.solution.catterpillars.ui.home.dashboard.wallet.model.WalletViewModel;

import java.util.ArrayList;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 12/14/2018
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 */
public class WalletAdapter extends BaseAdapter<WalletListAdapterBinding, List> {

    public WalletAdapter(Context context, WalletFragment walletFragment, ArrayList<List> list,
                         WalletViewModel viewModel) {
        super(context, list);
    }

    protected void hideView(WalletListAdapterBinding binding) {

    }

    @Override
    protected void showView(WalletListAdapterBinding binding) {

    }

    /**
     * @return R.layout.layout_file
     */
    @Override
    protected int getLayout() {
        return R.layout.wallet_list_adapter;
    }

    /**
     * Initialised View Holder
     *
     * @param binding DataBinding
     * @return new ViewHolder(binding);
     */
    @Override
    public ViewHolder getViewHolder(WalletListAdapterBinding binding) {
        return new ViewHolder(binding) {
            /**
             * If there is anything to do then do here otherwise leave it blank
             *
             * @param binding  layout reference for single view
             * @param data     for single view
             * @param position position of ArrayList
             */
            @Override
            protected void doSomeWorkHere(WalletListAdapterBinding binding, List data, int position) {
                binding.fromUserViewL.setVisibility(View.GONE);

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
            public void setClickListeners(ViewHolderClickListener thisContext, WalletListAdapterBinding binding, List data) {

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
            protected ViewHolderClickListener viewHolderReference(WalletListAdapterBinding binding, List data, int position) {
                return null;
            }
        };
    }

    /**
     * @return new FilterClass();
     */
    @Override
    protected FilterClass initialisedFilterClass() {
        return new FilterClass() {
            /**
             * @param constraint        DashboardViewModel
             *                          for (L data : list) {
             *                          if (data.getRefNo().toLowerCase().contains(constraint) ||
             *                          data.getZone().toLowerCase().contains(constraint))
             *                          filteredArrayList.add(data);
             *                          }
             * @param list              only one time, use in for loop
             * @param filteredArrayList This list will return with added data
             */
            @Override
            public ArrayList<List> filterData(CharSequence constraint, ArrayList<List> list,
                                              ArrayList<List> filteredArrayList) {
                for (List data : list) {
                   if (data.getTransactionId().toLowerCase().contains(constraint) ||
                       data.getDescription().toLowerCase().contains(constraint))
                   filteredArrayList.add(data);
                }
                return filteredArrayList;
            }
        };
    }


}
