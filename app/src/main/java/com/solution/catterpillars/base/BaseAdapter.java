package com.solution.catterpillars.base;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 12-Nov-18
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 * About : I am a mathematician
 * Quote : Only brain can make anything possible
 * Strength : Never give up
 */
public abstract class BaseAdapter<B extends ViewDataBinding, D extends ViewModel> extends
        RecyclerView.Adapter<BaseAdapter.ViewHolder>
        implements Filterable {

    protected B binding;
    protected ArrayList<D> filterList;
    private ArrayList<D> mainList;
    protected Context context;
    protected Activity activity;
    private FilterClass filter = null;

    /**
     * @param activity reference of Activity
     */
    public BaseAdapter(Activity activity) {
        this.activity = activity;
        this.context = activity;
    }

    /**
     * @param context reference of Context
     */
    public BaseAdapter(Context context) {
        this.context = context;
        this.activity = (Activity) context;
    }

    /**
     * @param context  reference of Context
     * @param activity reference of Activity
     */
    public BaseAdapter(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    /**
     * @param activity reference of Activity
     * @param list     DataList
     */
    public BaseAdapter(Activity activity, ArrayList<D> list) {
        this.activity = activity;
        this.context = activity;
        this.filterList = list;
        this.mainList = list;
    }

    /**
     * @param context reference of context
     * @param list    DataList
     */
    public BaseAdapter(Context context, ArrayList<D> list) {
        this.activity = (Activity) context;
        this.context = context;
        this.filterList = list;
        this.mainList = list;
    }

    /**
     * @param context  reference of context
     * @param activity reference of Activity
     * @param list     DataList
     */
    public BaseAdapter(Context context, Activity activity, ArrayList<D> list) {
        this.context = context;
        this.activity = activity;
        this.filterList = list;
        this.mainList = list;
    }

    @NonNull
    @Override
    public BaseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        binding = DataBindingUtil.inflate(inflater,
                getLayout(), parent, false);

        showView(binding);
        hideView(binding);

        doJobInOnCreate(parent, viewType, binding);

//        View v = inflater.inflate(getLayout(), parent, false);
        // set the view's size, margins, paddings and layout parameters
        return getViewHolder(binding);
    }

    protected void doJobInOnCreate(ViewGroup viewGroup, int viewType, B binding) {

    }

    @Override
    public void onBindViewHolder(@NonNull BaseAdapter.ViewHolder holder, int position) {
        holder.bind(filterList.get(position), position);
    }

    @Override
    public int getItemCount() {
        if (filterList != null)
            return filterList.size();
        else
            return 0;
    }

    /**
     * This will hide the view on first load
     * left blank if no view has to hide
     *
     * @param binding DataBinding
     */
    protected abstract void hideView(B binding);

    /**
     * This is use to make the hidden view on first load
     * left blank if noting is hidden
     *
     * @param binding DataBinding
     */
    protected abstract void showView(B binding);

    /**
     * @return R.layout.layout_file
     */
    @LayoutRes
    protected abstract int getLayout();

    /**
     * Initialised View Holder
     *
     * @param binding DataBinding
     * @return new ViewHolder(binding);
     */
    public abstract ViewHolder getViewHolder(B binding);

    //-----------------filter/search in adapter---------------------

    /**
     * @return new FilterClass();
     */
    protected abstract FilterClass initialisedFilterClass();

    /**
     * <p>Returns a filter that can be used to constrain data with a filtering
     * pattern.</p>
     * <p>
     * <p>This method is usually implemented by {@link Adapter}
     * classes.</p>
     *
     * @return a filter used to constrain data
     */
    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = initialisedFilterClass();
        return filter;
    }

    /**
     * class use for the filter of adapter view
     */
    public abstract class FilterClass extends Filter {
        private ActivityCalls activityCalls;
        //private ArrayList<D> filteredArrayList;

        protected FilterClass() {
            try {
                this.activityCalls = (ActivityCalls) context;
            } catch (ClassCastException cce) {

            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            constraint = constraint.toString().toLowerCase();

            if (constraint.length() <= 0) {
                filterList = mainList;
            } else {
                filterList = new ArrayList<>();
                filterList = filterData(constraint, mainList, filterList);

                filterResults.count = filterList.size();
                filterResults.values = filterList;
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            filterList = (ArrayList<D>) results.values;

            if (constraint == null || constraint.length() <= 0) {
                filterList = mainList;
            }
            try {
                if (activityCalls != null) {
                    activityCalls.callBackToActivity(String.valueOf(filterList.size()));
                }
            } catch (ClassCastException cce) {

            }


            notifyDataSetChanged();
        }

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
        public abstract ArrayList<D> filterData(CharSequence constraint, ArrayList<D> list,
                                                ArrayList<D> filteredArrayList);
    }

    //--------------------------------------------------------------

    public void showToast(int index) {
        Toast.makeText(context, context.getString(index), Toast.LENGTH_SHORT).show();
    }

    public void showToastLong(int index) {
        Toast.makeText(context, context.getString(index), Toast.LENGTH_LONG).show();
    }

    //------------------view Holder---------------------------------

    public abstract class ViewHolder extends RecyclerView.ViewHolder {
        protected int position;
        protected B binding;

        /**
         * @param binding layout reference
         */
        protected ViewHolder(B binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(D data, int position) {
            this.position = position;
            bindData(data);
            setClickListeners(data, position);
            doSomeWorkHere(binding, data, position);
        }

        /**
         * If there is anything to do then do here otherwise leave it blank
         *
         * @param binding  layout reference for single view
         * @param data     for single view
         * @param position position of ArrayList
         */
        protected abstract void doSomeWorkHere(B binding, D data, int position);

        /**
         * @param data binding.setData(data);
         */
        protected abstract void bindData(D data);

        protected void setClickListeners(D data, int position) {
            ViewHolderClickListener viewHolderClickListener = viewHolderReference(binding, data, position);
            setClickListeners(viewHolderClickListener, binding, data);
        }

        /**
         * Method to set click listeners on view holder or groups
         *
         * @param thisContext set it on method : binding.layout.setOnClickListener(thisContext);
         * @param binding     DataBinding
         * @param data        data
         */
        public abstract void setClickListeners(ViewHolderClickListener thisContext, B binding, D data);


        /**
         * Initialised holder by new operator
         *
         * @param binding  DataBinding
         * @param data     dataList
         * @param position of adapter
         * @return new ViewHolderClickListener(binding, data, position)
         */
        protected abstract ViewHolderClickListener viewHolderReference(B binding, D data, int position);
    }

    //----------------------------ViewHolder Click Listener---------

    public abstract class ViewHolderClickListener implements View.OnClickListener {
        protected final int position;
        protected B binding;

        /**
         * @param position of a adapter in current view
         */
        public ViewHolderClickListener(B binding, int position) {
            this.position = position;
            this.binding = binding;
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public abstract void onClick(View v);
    }

    //--------------------------------------------------------------
}
