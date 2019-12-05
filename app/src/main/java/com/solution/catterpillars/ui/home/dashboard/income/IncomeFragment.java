package com.solution.catterpillars.ui.home.dashboard.income;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.solution.catterpillars.BR;
import com.solution.catterpillars.R;
import com.solution.catterpillars.base.ActivityCalls;
import com.solution.catterpillars.base.BaseFragment;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.data.remort.APIClient;
import com.solution.catterpillars.data.remort.APIService;
import com.solution.catterpillars.databinding.FragmentMemberDetailBinding;
import com.solution.catterpillars.ui.home.Home;
import com.solution.catterpillars.ui.home.dashboard.income.model.IncomeViewModel;
import com.solution.catterpillars.ui.home.dashboard.wallet.WalletFragment;
import com.solution.catterpillars.ui.home.dashboard.wallet.model.List;
import com.solution.catterpillars.ui.interfaces.DateInterface;
import com.solution.catterpillars.util.CustomAlertDialog;
import com.solution.catterpillars.util.CustomLoader;
import com.solution.catterpillars.util.DatePickerFragment;
import com.solution.catterpillars.util.bubbleDialog.BubbleDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 12/15/2018
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 */
public class IncomeFragment extends BaseFragment<FragmentMemberDetailBinding, IncomeViewModel>
        implements ActivityCalls, DateInterface {
    public static final String TAG = WalletFragment.class.getSimpleName();
    private final int FIRST_API_LOAD = 1;
    private final int FILTER_API_LOAD = 2;
    private final int PAGE_WISE_API_LOAD = 3;

    public static final String TODAYS_INCOME = "1";

    private String dataType = "";
    private int pageIndex = 1;
    private int maxIndex = 1;
    private CustomLoader loader;
    private AppPreferencesService preferencesService;
    private ArrayList<List> list;
    private IncomeViewModel viewModel;
    private IncomeAdapter adapter;
    private InputMethodManager kryPadManager;
    private int screenWidth, screenHeight;
    private Home activityHome;
    private AppCompatEditText startDateD, topFilterCountEditText;
    private AppCompatEditText endDateD;
    private AppCompatEditText transactionIdD;
    private String startDateS = "";
    private String endDateS = "";
    private String transactionIdS = "";
    private String topListCountS = "Show 10 Income";
    private String selectedTopListCountId = "10";
    private HashMap<String, String> listCountStatusMap = new HashMap<>();
    private String[] topCountStringArray = {"Show 10 Income", "Show 50 Income", "Show All Income"};
    private int topListCountSelectedIndex = 0;
    private Date date;
    private String userId = "";
    private String title;
    private int visibleThreshold = 4;
    private int lastVisibleItem, totalItemCount;
    boolean userScrolled = false;
    int pastVisiblesItems, visibleItemCount;
    private boolean isFragmentOpen = false;

    public static IncomeFragment newInstance(String title, String dataType) {
        IncomeFragment fragment = new IncomeFragment();
        Bundle args = new Bundle();
        args.putString("Title", title);
        args.putString("DataType", dataType);
        fragment.setArguments(args);
        return fragment;
    }


    /**
     * @return R.layout.layout_file
     */
    @Override
    public int getLayout() {
        return R.layout.fragment_member_detail;
    }

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    @Override
    public IncomeViewModel getViewModel() {
        title = getArguments().getString("Title");
        dataType = getArguments().getString("DataType");
        return viewModel;
    }

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    @Override
    public int getBindingVariable() {
        return BR.data;
    }

    /**
     * @return R.strings.text
     */
    @Override
    public int setTitle() {
        return 0;
    }

    /**
     * @return String value null or "" then this title will not show otherwise it will show
     */
    @Override
    protected String setTitleString() {
        return title;
    }

    /**
     * Write rest of the code of fragment in onCreateView after view created
     */
    @Override
    protected void init() {
        activityHome = (Home) context;
        if (context instanceof Home) {
            ((Home) context).networkErrorViewVisible(View.GONE);
        }
        list = new ArrayList<>();
        viewModel = new IncomeViewModel();
        preferencesService = new AppPreferencesService(context);
        loader = new CustomLoader(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        binding.noDataText.setText(title + " is Empty");
        listCountStatusMap.put("Show 10 Income", "10");
        listCountStatusMap.put("Show 50 Income", "50");
        listCountStatusMap.put("Show All Income", "0");
        binding.listView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        binding.listView.setLayoutManager(mLayoutManager);

        adapter = new IncomeAdapter(context, list);
        binding.listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerViewScrollListner();
        searchFunction();

        openFilterDialog();

        initialisingData();

        api(FIRST_API_LOAD, false);
    }

    void recyclerViewScrollListner() {
        if (binding.listView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) binding.listView.getLayoutManager();


            binding.listView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    // If scroll state is touch scroll then set userScrolled
                    // true
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        userScrolled = true;
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx,
                                       int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    // Now check if userScrolled is true and also check if
                    // the item is end then update recycler view and set
                    // userScrolled to false
                    if (userScrolled && (visibleItemCount + pastVisiblesItems) == totalItemCount) {
                        userScrolled = false;
                        pageIndex++;
                        if (pageIndex <= maxIndex) {
                            api(FILTER_API_LOAD, false);
                        }

                    }
                }
            });
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void initialisingData() {
        //Date format MM/dd/yyyy
        date = new Date();
        endDateS = new SimpleDateFormat("dd/MMM/yyyy").format(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -30);
        date = cal.getTime();

        startDateS = new SimpleDateFormat("dd/MMM/yyyy").format(date);

        userId = preferencesService.getUserId();
    }

    void searchFunction() {
        activityHome.binding.toolbarView.searchIcon.setVisibility(View.VISIBLE);
        activityHome.binding.toolbarView.searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityHome.binding.toolbarView.searchBar.setVisibility(View.VISIBLE);
            }
        });

        activityHome.binding.toolbarView.clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (activityHome.binding.toolbarView.search.getText().length() > 0) {
                    activityHome.binding.toolbarView.search.setText("");
                } else {
                    activityHome.binding.toolbarView.searchBar.setVisibility(View.GONE);
                }
            }
        });

        activityHome.binding.toolbarView.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence newText, int start, int before, int count) {
                if (adapter != null && !newText.equals("")) {
                    adapter.getFilter().filter(newText);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void openFilterDialog() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

        binding.fab.setOnClickListener(view -> {
            showDialog();
           /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();*/
        });
    }

    private void showDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_member_detail_filter, null);
        int reduceSize = (int) (screenHeight / 3.7);

        if (screenHeight <= 1000) {
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    (screenHeight - reduceSize)));
        }

        BubbleDialog dialog = new BubbleDialog(context);
        dialog.addContentView(view);
        dialog.setClickedView(binding.fab);
        dialog.calBar(true);
        dialog.show();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        topFilterCountEditText = view.findViewById(R.id.topCountFilter);
        startDateD = view.findViewById(R.id.startDate);
        endDateD = view.findViewById(R.id.endDate);
        if (dataType != null && !dataType.isEmpty()) {
            if (dataType.equalsIgnoreCase("1")) {
                startDateD.setVisibility(View.GONE);
                endDateD.setVisibility(View.GONE);
            } else {
                startDateD.setVisibility(View.VISIBLE);
                endDateD.setVisibility(View.VISIBLE);
            }
        }
        transactionIdD = view.findViewById(R.id.transactionId);
        AppCompatEditText mobile = view.findViewById(R.id.mobile);
        LinearLayout mobileLyaout = view.findViewById(R.id.mobileL);
        AppCompatEditText signUpTaskStatus = view.findViewById(R.id.signUpTaskStatus);
        AppCompatEditText dailyTaskStatus = view.findViewById(R.id.dailyTaskStatus);
        Button filter = view.findViewById(R.id.filter);

        mobileLyaout.setVisibility(View.GONE);
        signUpTaskStatus.setVisibility(View.GONE);
        dailyTaskStatus.setVisibility(View.GONE);


        startDateD.setText(startDateS);
        endDateD.setText(endDateS);
        transactionIdD.setText(transactionIdS);
        transactionIdD.setSelection(transactionIdD.length());
        topFilterCountEditText.setText(topListCountS);
        startDateD.setFocusable(false);
        endDateD.setFocusable(false);

        startDateD.setOnClickListener(v -> onDateClick(1));
        endDateD.setOnClickListener(v -> onDateClick(2));
        topFilterCountEditText.setOnClickListener(v -> onListCountFilter());
        filter.setOnClickListener(v -> onAdvanceSearchCalls(dialog));
    }

    private void onDateClick(int i) {
        DatePickerFragment dateFragment = new DatePickerFragment(context, (DateInterface) this,
                String.valueOf(i));
        dateFragment.show(activity.getSupportFragmentManager(), DatePickerFragment.TAG);
    }

    private void onListCountFilter() {
        if (topFilterCountEditText.getText().toString().length() == 0) {
            topListCountSelectedIndex = 0;
        } else {
            topListCountSelectedIndex = Arrays.asList(topCountStringArray).indexOf(topFilterCountEditText.getText().toString());
        }

        final AlertDialog.Builder builde = new AlertDialog.Builder(context);
        builde.setTitle("Select Top Income List Count");

        if (topCountStringArray.length == 0) {
            builde.setMessage(Html.fromHtml("<font color='#646464'>Please select count</font>"));
        }
        builde.setSingleChoiceItems(topCountStringArray, topListCountSelectedIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int selectedIndex) {
                topListCountSelectedIndex = selectedIndex;
            }
        });
        builde.setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (topListCountSelectedIndex != -1) {
                    topFilterCountEditText.setText(topCountStringArray[topListCountSelectedIndex]);
                    topFilterCountEditText.setError(null);
                }
                dialog.dismiss();
            }
        });
        builde.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builde.show();
    }

    private void onAdvanceSearchCalls(Dialog dialog) {
        startDateS = startDateD.getText().toString();
        endDateS = endDateD.getText().toString();
        transactionIdS = transactionIdD.getText().toString();
        topListCountS = topFilterCountEditText.getText().toString();

        if (topFilterCountEditText != null && topFilterCountEditText.getText().length() == 0) {
            selectedTopListCountId = "10";
        } else if (topFilterCountEditText != null && topFilterCountEditText.getText().length() > 0) {
            selectedTopListCountId = listCountStatusMap.get(topListCountS);
        }
        dialog.dismiss();
        pageIndex = 1;
        maxIndex = 1;
        api(FILTER_API_LOAD, true);
    }

    @Override
    public void onClick(View v) {

    }

    void api(int index, boolean isFilterApply) {
        loader.show();
        loader.setCancelable(false);
        loader.setCanceledOnTouchOutside(false);

        APIService apiService = APIClient.getConnect().create(APIService.class);
        Call<IncomeViewModel> call = null;// = apiService.checkSheetDetails(module, "", "");

        if (index == FIRST_API_LOAD) {
            call = apiService.incomeDetails(preferencesService.getUMobile(), startDateS, endDateS,
                    dataType, String.valueOf(pageIndex), selectedTopListCountId, transactionIdS);
        } else {
            call = apiService.incomeDetails(preferencesService.getUMobile(), startDateS, endDateS,
                    dataType, String.valueOf(pageIndex), selectedTopListCountId, transactionIdS);
        }

        if (call != null) {
            call.enqueue(new retrofit2.Callback<IncomeViewModel>() {
                @Override
                public void onResponse(@NonNull Call<IncomeViewModel> call, @NonNull
                        Response<IncomeViewModel> response) {
                    viewModel = response.body();

                    if (viewModel != null && viewModel.getStatus().equalsIgnoreCase("1")) {
                        if (viewModel.getPageIndex() != null && viewModel.getMaxIndex() != null) {
                            pageIndex = Integer.parseInt(viewModel.getPageIndex());
                            maxIndex = Integer.parseInt(viewModel.getMaxIndex());
                        }
                        if (isFilterApply) {
                            list.clear();
                        }
                        binding.accountbalanceView.setVisibility(View.VISIBLE);
                        binding.creaditAmount.setText(viewModel.getCrAmount());
                        binding.debitAmount.setText(viewModel.getDrAmount());
                        binding.noDataText.setVisibility(View.GONE);
                        list.addAll(viewModel.getList());
                        adapter.notifyDataSetChanged();
                    } else {
                        list.clear();
                        binding.accountbalanceView.setVisibility(View.GONE);
                        setErrorText(R.drawable.ic_wallet, context.getResources().getColor(R.color.grayDark), title + " is Empty");
                    }


                    activityHome.binding.toolbarView.search.setText("");
                    activityHome.binding.toolbarView.clearBtn.performClick();
                    loader.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<IncomeViewModel> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    call.cancel();
                    loader.dismiss();
                    /*if (t.getMessage().contains("No address associated with hostname")) {
                        new CustomAlertDialog(context).Error(getString(R.string.network_not_found));
                        showNetworkErrorView(index);
                    } else {
                        new CustomAlertDialog(context).Error(getString(R.string.try_again));
                        setErrorText(R.drawable.ic_api_error, Color.RED, getString(R.string.try_again));
                    }*/

                    if (t.getMessage() != null && !t.getMessage().isEmpty()) {
                        if (t.getMessage().contains("No address associated with hostname")) {
                            new CustomAlertDialog(context, isFragmentOpen).Error(getString(R.string.network_not_found));
                            showNetworkErrorView(index, isFilterApply);

                        } else {
                            new CustomAlertDialog(context, isFragmentOpen).Error(getString(R.string.try_again));
                            setErrorText(R.drawable.ic_api_error, Color.RED, getString(R.string.try_again));
                        }
                    }/*else{
                        new CustomAlertDialog(context).Error(getString(R.string.try_again));
                        setErrorText(R.drawable.ic_api_error, Color.RED, getString(R.string.try_again));
                    }*/
                    binding.accountbalanceView.setVisibility(View.GONE);
                }
            });
        }
    }


    void showNetworkErrorView(int index, boolean isFilterApply) {
        setErrorText(R.drawable.ic_network_error, Color.RED, getString(R.string.network_error));
        if (context instanceof Home) {
            ((Home) context).networkErrorViewVisible(View.VISIBLE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Home) context).networkErrorViewVisible(View.GONE);
                    binding.noDataText.setVisibility(View.GONE);
                    api(index, isFilterApply);
                }
            });
        }
    }


    void setErrorText(int icon, int color, String msg) {
        binding.noDataText.setVisibility(View.VISIBLE);
        binding.noDataText.setCompoundDrawablesWithIntrinsicBounds(0,
                icon, 0, 0);
        binding.noDataText.setText(msg);
        binding.noDataText.setTextColor(color);
    }

    /**
     * @param data it will only be called when filter will occur in searchBar
     */
    @Override
    public void callBackToActivity(String data) {

    }

    /**
     * @param date   date will return in string format
     * @param params date will return in int format
     */
    @Override
    public void date(String date, String... params) {
        if (date != null) {
            Date mDate = new Date(date);
            date = new SimpleDateFormat("dd/MMM/yyyy").format(mDate);
            if (params[0].equalsIgnoreCase("1")) {
                startDateD.setText(date);
            } else {
                endDateD.setText(date);
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isFragmentOpen = false;
        activityHome.binding.toolbarView.searchBar.setVisibility(View.GONE);
        activityHome.binding.toolbarView.searchIcon.setVisibility(View.GONE);
    }


    @Override
    public void onStart() {
        super.onStart();
        isFragmentOpen = true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        isFragmentOpen = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isFragmentOpen = false;
    }
}
