package com.solution.catterpillars.ui.home.dashboard.member;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
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
import android.widget.TextView;

import com.google.gson.Gson;
import com.solution.catterpillars.BR;
import com.solution.catterpillars.R;
import com.solution.catterpillars.base.ActivityCalls;
import com.solution.catterpillars.base.BaseFragment;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.data.remort.APIClient;
import com.solution.catterpillars.data.remort.APIService;
import com.solution.catterpillars.databinding.FragmentMemberDetailBinding;
import com.solution.catterpillars.ui.home.Home;
import com.solution.catterpillars.ui.home.dashboard.member.model.List;
import com.solution.catterpillars.ui.home.dashboard.member.model.MemberViewModel;
import com.solution.catterpillars.ui.interfaces.DateInterface;
import com.solution.catterpillars.ui.login.country_flag.FlagNavigator;
import com.solution.catterpillars.ui.login.country_flag.CountryFlagDialog;
import com.solution.catterpillars.ui.login.model.ResultLogin;
import com.solution.catterpillars.util.CustomAlertDialog;
import com.solution.catterpillars.util.CustomLoader;
import com.solution.catterpillars.util.DatePickerFragment;
import com.solution.catterpillars.util.bubbleDialog.BubbleDialog;

import java.text.ParseException;
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
public class MemberFragment extends BaseFragment<FragmentMemberDetailBinding, MemberViewModel>
        implements FlagNavigator, ActivityCalls, DateInterface {

    public static final String TAG = MemberFragment.class.getSimpleName();
    private final int FIRST_API_LOAD = 1;
    private final int FILTER_API_LOAD = 2;
    private final int PAGE_WISE_API_LOAD = 3;

    public static final String TODAYS_MEMBER = "1";
    public static final String THIS_MONTH_MEMBER = "2";
    public static final String TOTAL_MEMBER = "3";

    private String dataType = "";
    private int pageIndex = 1, maxIndex = 1;
    private HashMap<String, Integer> flagSet;
    private ArrayList<com.solution.catterpillars.ui.login.model.List> flagList;
    private String countryCodeMobile;
    private String selectedCountryCode = "+91";
    private String selectedCountryId = "89";
    private CustomLoader loader;
    private AppPreferencesService preferencesService;
    private ArrayList<List> list;
    private MemberViewModel viewModel;
    private MemberAdapter adapter;
    private InputMethodManager kryPadManager;
    private int screenWidth, screenHeight;
    private Home activityHome;
    private AppCompatEditText startDateD, countryCodeET, topFilterCountEditText;
    private AppCompatEditText endDateD;
    private AppCompatEditText transactionIdD;
    private AppCompatEditText mobileChildD;
    private AppCompatEditText signUpTaskStatusEditText;
    private AppCompatEditText dailyTaskStatusEditText;
    private AppCompatImageView flagIcon;
    private String startDateS = "";
    private String endDateS = "";
    private String transactionIdS = "";
    private String mobileChildS = "";
    private String signnUpTaskStatusS = "";
    private String dailyTaskStatusS = "";
    private String topListCountS = "Show 10 Member";
    String selectedSignUpTaskStatusId = "", selectedDailyTaskStatusId = "", selectedTopListCountId = "10";
    private Date date;
    private String userId = "";
    private String title;
    private int visibleThreshold = 10;
    private int lastVisibleItem, totalItemCount;
    boolean userScrolled = false;
    int pastVisiblesItems, visibleItemCount;
    private HashMap<String, String> taskStatusMap = new HashMap<>();
    private String[] taskStatusStringArray = {"Completed", "Pending"};

    private HashMap<String, String> listCountStatusMap = new HashMap<>();
    private String[] topCountStringArray = {"Show 10 Member", "Show 50 Member", "Show All Member"};

    private int signUpTaskSelectedIndex = -1;
    private int dailyTaskSelectedIndex = -1;
    private int topListCountSelectedIndex = 0;
    private String taskType = "";
    private boolean isFragmentOpen = false;


    public static MemberFragment newInstance(String title, String dataType) {
        MemberFragment fragment = new MemberFragment();
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
    public MemberViewModel getViewModel() {
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
        flagSet = new HashMap<>();
        flagList = new ArrayList<>();
        list = new ArrayList<>();
        viewModel = new MemberViewModel();
        preferencesService = new AppPreferencesService(context);
        loader = new CustomLoader(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        binding.accountbalanceView.setVisibility(View.GONE);

        binding.listView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        binding.listView.setLayoutManager(mLayoutManager);

        adapter = new MemberAdapter(context, MemberFragment.this, list, viewModel);
        binding.listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        taskStatusMap.put("Completed", "1");
        taskStatusMap.put("Pending", "0");

        listCountStatusMap.put("Show 10 Member", "10");
        listCountStatusMap.put("Show 50 Member", "50");
        listCountStatusMap.put("Show All Member", "0");
        recyclerViewScrollListner();
        searchFunction();
        openFilterDialog();

        initialisingData();

        api(FIRST_API_LOAD, false);

        ResultLogin mResultLogin = new Gson().fromJson(preferencesService.getFlagData(), ResultLogin.class);
        if (mResultLogin != null && mResultLogin.getList().size() > 0) {
            // setFlagData(mResultLogin);
        } else {
            apisFlag();
        }
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
        try {
            date = new Date();
            //   endDateS = new SimpleDateFormat("MM/dd/yyyy").format(date);
            endDateS = new SimpleDateFormat("dd/MMM/yyyy").format(date);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, -30);
            date = cal.getTime();

            //startDateS = new SimpleDateFormat("MM/dd/yyyy").format(date);
            startDateS = new SimpleDateFormat("dd/MMM/yyyy").format(date);
        } catch (Exception e) {

        }
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
        transactionIdD = view.findViewById(R.id.transactionId);
        mobileChildD = view.findViewById(R.id.mobile);
        LinearLayout mobileLyaout = view.findViewById(R.id.mobileL);
        signUpTaskStatusEditText = view.findViewById(R.id.signUpTaskStatus);
        dailyTaskStatusEditText = view.findViewById(R.id.dailyTaskStatus);
        Button filter = view.findViewById(R.id.filter);
        flagIcon = view.findViewById(R.id.flagIcon);
        countryCodeET = view.findViewById(R.id.countryCode);

        startDateD.setText(startDateS);
        endDateD.setText(endDateS);
        transactionIdD.setVisibility(View.GONE);
        transactionIdD.setText(transactionIdS);
        mobileChildD.setText(mobileChildS);
        mobileChildD.setSelection(mobileChildS.length());

        signUpTaskStatusEditText.setText(signnUpTaskStatusS);
        dailyTaskStatusEditText.setText(dailyTaskStatusS);
        topFilterCountEditText.setText(topListCountS);
        startDateD.setFocusable(false);
        endDateD.setFocusable(false);
        if (dataType != null && !dataType.isEmpty()) {
            if (dataType.equalsIgnoreCase("1")) {
                startDateD.setVisibility(View.GONE);
                endDateD.setVisibility(View.GONE);
            } else {
                startDateD.setVisibility(View.VISIBLE);
                endDateD.setVisibility(View.VISIBLE);
            }
        }
        startDateD.setOnClickListener(v -> onDateClick(1));
        endDateD.setOnClickListener(v -> onDateClick(2));
        signUpTaskStatusEditText.setOnClickListener(v -> onSignUpTaskStatusFilter());
        dailyTaskStatusEditText.setOnClickListener(v -> onDailyTaskStatusFilter());
        topFilterCountEditText.setOnClickListener(v -> onListCountFilter());
        flagIcon.setOnClickListener(v -> openFlagDialog());
        filter.setOnClickListener(v -> onAdvanceSearchCalls(dialog));

        countryCodeET.addTextChangedListener(new MyTextWatcher(countryCodeET));
        ResultLogin mResultLogin = new Gson().fromJson(preferencesService.getFlagData(), ResultLogin.class);
        if (mResultLogin != null && mResultLogin.getList().size() > 0) {
            setFlagData(mResultLogin);
        } else {
            apisFlag();
        }
    }

    private void onSignUpTaskStatusFilter() {
        if (signUpTaskStatusEditText.getText().toString().length() == 0) {
            signUpTaskSelectedIndex = -1;
        } else {
            signUpTaskSelectedIndex = Arrays.asList(taskStatusStringArray).indexOf(signUpTaskStatusEditText.getText().toString());
        }

        final AlertDialog.Builder builde = new AlertDialog.Builder(context);
        builde.setTitle("Select SignUp Task status");

        if (taskStatusStringArray.length == 0) {
            builde.setMessage(Html.fromHtml("<font color='#646464'>Please select status</font>"));
        }
        builde.setSingleChoiceItems(taskStatusStringArray, signUpTaskSelectedIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int selectedIndex) {
                signUpTaskSelectedIndex = selectedIndex;
            }
        });
        builde.setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (signUpTaskSelectedIndex != -1) {
                    signUpTaskStatusEditText.setText(taskStatusStringArray[signUpTaskSelectedIndex]);
                    signUpTaskStatusEditText.setError(null);
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

    private void onDailyTaskStatusFilter() {
        if (dailyTaskStatusEditText.getText().toString().length() == 0) {
            dailyTaskSelectedIndex = -1;
        } else {
            dailyTaskSelectedIndex = Arrays.asList(taskStatusStringArray).indexOf(dailyTaskStatusEditText.getText().toString());
        }

        final AlertDialog.Builder builde = new AlertDialog.Builder(context);
        builde.setTitle("Select SignUp Task status");

        if (taskStatusStringArray.length == 0) {
            builde.setMessage(Html.fromHtml("<font color='#646464'>Please select status</font>"));
        }
        builde.setSingleChoiceItems(taskStatusStringArray, dailyTaskSelectedIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int selectedIndex) {
                dailyTaskSelectedIndex = selectedIndex;
            }
        });
        builde.setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (dailyTaskSelectedIndex != -1) {
                    dailyTaskStatusEditText.setText(taskStatusStringArray[dailyTaskSelectedIndex]);
                    dailyTaskStatusEditText.setError(null);
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


    private void onListCountFilter() {
        if (topFilterCountEditText.getText().toString().length() == 0) {
            topListCountSelectedIndex = 0;
        } else {
            topListCountSelectedIndex = Arrays.asList(topCountStringArray).indexOf(topFilterCountEditText.getText().toString());
        }

        final AlertDialog.Builder builde = new AlertDialog.Builder(context);
        builde.setTitle("Select Top Member List Count");

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

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (countryCodeET.getText().toString().trim().isEmpty()) {
                openFlagDialog();
            }
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.countryCode:
                    validateCountryCode(1);
                    break;
            }
        }
    }

    private void onDailyFilterClick(int i) {

    }

    private void showDropDown(final CharSequence[] singleChoiceItems, final TextView textView,
                              String msg, final String type) {


    }

    private void onDateClick(int i) {
        DatePickerFragment dateFragment = new DatePickerFragment(context, (DateInterface) this,
                String.valueOf(i));
        dateFragment.show(activity.getSupportFragmentManager(), DatePickerFragment.TAG);
    }

    private void onAdvanceSearchCalls(Dialog dialog) {
        pageIndex = 1;
        maxIndex = 1;
        startDateS = startDateD.getText().toString();
        endDateS = endDateD.getText().toString();
        transactionIdS = transactionIdD.getText().toString();
        mobileChildS = mobileChildD.getText().toString();
        signnUpTaskStatusS = signUpTaskStatusEditText.getText().toString();
        dailyTaskStatusS = dailyTaskStatusEditText.getText().toString();
        topListCountS = topFilterCountEditText.getText().toString();

        if (signUpTaskStatusEditText != null && signUpTaskStatusEditText.getText().length() == 0) {
            selectedSignUpTaskStatusId = "";
        } else if (signUpTaskStatusEditText != null && signUpTaskStatusEditText.getText().length() > 0) {
            selectedSignUpTaskStatusId = taskStatusMap.get(signnUpTaskStatusS);
        }
        if (dailyTaskStatusEditText != null && dailyTaskStatusEditText.getText().length() == 0) {
            selectedDailyTaskStatusId = "";
        } else if (dailyTaskStatusEditText != null && dailyTaskStatusEditText.getText().length() > 0) {
            selectedDailyTaskStatusId = taskStatusMap.get(dailyTaskStatusS);
        }

        if (topFilterCountEditText != null && topFilterCountEditText.getText().length() == 0) {
            selectedTopListCountId = "10";
        } else if (topFilterCountEditText != null && topFilterCountEditText.getText().length() > 0) {
            selectedTopListCountId = listCountStatusMap.get(topListCountS);
        }

        dialog.dismiss();

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
        Call<MemberViewModel> call = null;// = apiService.checkSheetDetails(module, "", "");

        if (index == FIRST_API_LOAD) {
            call = apiService.membersDetails(preferencesService.getUMobile(), mobileChildS, startDateS, endDateS, dataType, String.valueOf(pageIndex), selectedTopListCountId, selectedSignUpTaskStatusId, selectedDailyTaskStatusId);
        } else {
            call = apiService.membersDetails(preferencesService.getUMobile(), mobileChildS, startDateS, endDateS, "", String.valueOf(pageIndex), selectedTopListCountId, selectedSignUpTaskStatusId, selectedDailyTaskStatusId);
        }

        if (call != null) {
            call.enqueue(new retrofit2.Callback<MemberViewModel>() {
                @Override
                public void onResponse(@NonNull Call<MemberViewModel> call, @NonNull
                        Response<MemberViewModel> response) {
                    viewModel = response.body();
                    if (viewModel != null && viewModel.getStatus().equalsIgnoreCase("1")) {
                        if (isFilterApply) {
                            list.clear();
                        }
                        if (viewModel.getPageIndex() != null && viewModel.getMaxIndex() != null) {
                            pageIndex = Integer.parseInt(viewModel.getPageIndex());
                            maxIndex = Integer.parseInt(viewModel.getMaxIndex());
                        }
                        list.addAll(viewModel.getList());
                        adapter.notifyDataSetChanged();
                        binding.noDataText.setVisibility(View.GONE);
                    } else {
                        list.clear();
                        setErrorText(R.drawable.ic_team, context.getResources().getColor(R.color.grayDark), title + " is Empty");

                    }
                    activityHome.binding.toolbarView.search.setText("");
                    activityHome.binding.toolbarView.clearBtn.performClick();
                    loader.dismiss();
                   /* new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loader.dismiss();
                        }
                    },300);*/

                }

                @Override
                public void onFailure(@NonNull Call<MemberViewModel> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    call.cancel();
                    loader.dismiss();
                   /* if (t.getMessage().contains("No address associated with hostname")) {
                        new CustomAlertDialog(context,isFragmentOpen).Error(getString(R.string.network_not_found));
                        showNetworkErrorView(index);
                    } else {
                        new CustomAlertDialog(context,isFragmentOpen).Error(getString(R.string.try_again));
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
                        new CustomAlertDialog(context,isFragmentOpen).Error(getString(R.string.try_again));
                         setErrorText(R.drawable.ic_api_error, Color.RED, getString(R.string.try_again));
                    }*/
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
            try {
                Date mDate = new Date(date);
                date = new SimpleDateFormat("dd/MMM/yyyy").format(mDate);
            } catch (Exception e) {

            }
            if (params[0].equalsIgnoreCase("1")) {
                startDateD.setText(date);
            } else {
                endDateD.setText(date);
            }
        }
    }


    private void apisFlag() {
        loader.show();
        loader.setCancelable(false);
        loader.setCanceledOnTouchOutside(false);
        APIService apiService = APIClient.getConnect().create(APIService.class);
        Call<ResultLogin> call = null;// = apiService.checkSheetDetails(module, "", "");

        call = apiService.countryCode();

        if (call != null) {
            call.enqueue(new retrofit2.Callback<ResultLogin>() {
                @Override
                public void onResponse(@NonNull Call<ResultLogin> call, @NonNull
                        Response<ResultLogin> response) {
                    ResultLogin data = response.body();
                    if (data != null) {
                        setFlagData(data);
                    }
                    loader.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<ResultLogin> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    call.cancel();
                    loader.dismiss();
                }
            });
        }
    }

    private void setFlagData(ResultLogin data) {
        if (data.getStatus().equals("1")) {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //getNetworkCountryIso
            int countryPosition = 0;
            String CountryID = manager.getSimCountryIso().toUpperCase();

            flagList.addAll(data.getList());
            for (int i = 0; i < data.getList().size(); i++) {
                flagSet.put(data.getList().get(i).getPhonePrefix(), i);
                if (CountryID.length() > 0 && data.getList().get(i).getISOCode1().toLowerCase().contains(CountryID.toLowerCase())) {
                    countryPosition = i;
                }
            }

            countryCode(data.getList().get(countryPosition).getPhonePrefix(),
                    data.getList().get(countryPosition).getId());


        }
    }

    private void openFlagDialog() {
        CountryFlagDialog flagDialog = new CountryFlagDialog(context, flagList, this);
        flagDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.full_screen_dialog);
        flagDialog.show(activity.getSupportFragmentManager(), "data");
    }

    private boolean validateCountryCode(int value) {
        if (countryCodeET != null && flagIcon != null) {
            if (countryCodeET.getText().toString().trim().isEmpty()) {
                countryCodeET.setError(getString(R.string.invalidCountryCode));
                return false;
            } else {
                String countryCode = countryCodeET.getText().toString();
                int i = 0;
                if (flagSet.containsKey("+" + countryCode)) {
                    i = flagSet.get("+" + countryCode);
                    com.solution.catterpillars.ui.login.model.List.setFlag(flagIcon, flagList.get(i).getImagePath());
                    selectedCountryId = flagList.get(i).getId();
                    selectedCountryCode = flagList.get(i).getPhonePrefix();
                    countryCodeET.setText("+" + countryCode);
                } else if (flagSet.containsKey(countryCode) && !countryCode.contains("+")) {
                    i = flagSet.get(countryCode);
                    com.solution.catterpillars.ui.login.model.List.setFlag(flagIcon, flagList.get(i).getImagePath());

                    selectedCountryId = flagList.get(i).getId();
                    selectedCountryCode = flagList.get(i).getPhonePrefix();
                    countryCodeET.setText("+" + countryCode);

                } else if (flagSet.containsKey(countryCode)) {
                    i = flagSet.get(countryCode);
                    com.solution.catterpillars.ui.login.model.List.setFlag(flagIcon, flagList.get(i).getImagePath());
                    selectedCountryId = flagList.get(i).getId();
                    selectedCountryCode = flagList.get(i).getPhonePrefix();
                    //requestFocus(binding.mobile);
                } else if (flagSet.containsKey(countryCode.substring(1))) {

                } else if (countryCode.length() > 1 &&
                        flagSet.containsKey("+" + countryCode.substring(0, 1) +
                                " " + countryCode.substring(1))) {
                    String code = "+" + countryCode.substring(0, 1) +
                            " " + countryCode.substring(1);
                    i = flagSet.get(code);
                    com.solution.catterpillars.ui.login.model.List.setFlag(flagIcon, flagList.get(i).getImagePath());
                    selectedCountryId = flagList.get(i).getId();
                    countryCodeET.setText(code);
                    selectedCountryCode = flagList.get(i).getPhonePrefix();
                } else if (countryCode.length() > 1 &&
                        flagSet.containsKey("+" + countryCode.substring(0, 1) +
                                "-" + countryCode.substring(1))) {
                    String code = "+" + countryCode.substring(0, 1) +
                            "-" + countryCode.substring(1);
                    i = flagSet.get(code);
                    com.solution.catterpillars.ui.login.model.List.setFlag(flagIcon, flagList.get(i).getImagePath());
                    selectedCountryId = flagList.get(i).getId();
                    selectedCountryCode = flagList.get(i).getPhonePrefix();
                    countryCodeET.setText(code);
                } else if (value == 2) {
                    countryCodeET.setError(getString(R.string.invalidCountryCode));

                    return false;
                }

                countryCodeET.setSelection(countryCodeET.getText().length());
                countryCodeET.setError(null);
            }
        }

        return true;
    }

    @Override
    public void countryCode(String code, String id) {
        if (code != null && !code.isEmpty()) {
            countryCodeET.setText(code);
            selectedCountryCode = code;
            selectedCountryId = id;
        } else {
            countryCodeET.setText("+91");
            selectedCountryCode = "+91";
            selectedCountryId = "89";
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        isFragmentOpen = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isFragmentOpen = false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        isFragmentOpen = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isFragmentOpen = false;
        activityHome.binding.toolbarView.searchBar.setVisibility(View.GONE);
        activityHome.binding.toolbarView.searchIcon.setVisibility(View.GONE);
    }
}
