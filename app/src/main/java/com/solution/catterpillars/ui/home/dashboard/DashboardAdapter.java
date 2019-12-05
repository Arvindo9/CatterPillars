package com.solution.catterpillars.ui.home.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.solution.catterpillars.R;
import com.solution.catterpillars.base.BaseAdapter;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.databinding.DashboardTaskAdapterBinding;
import com.solution.catterpillars.ui.home.Home;
import com.solution.catterpillars.ui.home.dashboard.callbackInterface.VerifyDataCallBack;
import com.solution.catterpillars.ui.home.dashboard.model.DataList;
import com.solution.catterpillars.ui.home.dashboard.model.DataType;
import com.solution.catterpillars.ui.home.dashboard.task.TaskViewFragment;
import com.solution.catterpillars.util.RecyclerViewItemDecoration;

import java.util.ArrayList;

/**
 * Created by KinG on 10-12-2018.
 * Created by ${EMAIL}.
 */
public class DashboardAdapter extends BaseAdapter<DashboardTaskAdapterBinding, DataType> {
    VerifyDataCallBack mVerifyDataCallBack;
    public static final String TAG = DashboardAdapter.class.getSimpleName();
    private final ArrayList<DataType> list;
    private final DashboardFragment dashboardFragment;
    private AppPreferencesService preferencesService;
    private final int VERIFICATION_DATA = 0;
    private final int ADS_DATA = 1;
    private final int SHARE = 2;
    private final int DOWNLINE_UPLINE_DATA = 3;
    private final int TASK_STUTUS_DATA = 4;
    private final int INCOME_DATA = 5;
    private final int MEMBER_DATA = 6;
    private final int WALLET_DATA = 7;
    RecyclerView recyclerView;


    String whatsAppPackage = "com.whatsapp";
    String facebookPackage = "com.facebook.katana";
    String gmailPackage = "com.google.android.gm";
    String twitterPackage = "com.twitter.android";
    String googlePlusPackage = "com.google.android.gms.plus";
    private DashboardTaskAdapterBinding bindingLocal;


    public DashboardAdapter(Context context, ArrayList<DataType> list,
                            DashboardFragment dashboardFragment, VerifyDataCallBack mVerifyDataCallBack, RecyclerView recyclerView) {
        super(context, list);
        this.mVerifyDataCallBack = mVerifyDataCallBack;
        this.list = list;
        this.recyclerView = recyclerView;
        this.dashboardFragment = dashboardFragment;
        preferencesService = new AppPreferencesService(context);


    }

    @Override
    protected void hideView(DashboardTaskAdapterBinding binding) {
        /*recyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (list.get(VERIFICATION_DATA).getData() != null && ((ArrayList<DataList>) list.get(VERIFICATION_DATA).getData()).size() > 0) {

                } else {
                    recyclerView.addItemDecoration(new RecyclerViewItemDecoration((int) 0,
                            RecyclerViewItemDecoration.VERTICAL, false), VERIFICATION_DATA);
                }
                if (preferencesService.getUplineEmail() != null && !preferencesService.getUplineEmail().isEmpty() &&
                        preferencesService.getUplineMobile() != null && !preferencesService.getUplineMobile().isEmpty()
                        && preferencesService.getUplineName() != null && !preferencesService.getUplineName().isEmpty()) {

                } else {
                    recyclerView.addItemDecoration(new RecyclerViewItemDecoration((int) 0,
                            RecyclerViewItemDecoration.VERTICAL, false), VERIFICATION_DATA);
                }
            }
        });*/

    }


    @Override
    protected void showView(DashboardTaskAdapterBinding binding) {


        getInstalledApp(whatsAppPackage, binding.whatsapp, binding.whatsappView);
        getInstalledApp(facebookPackage, binding.facebook, binding.facebookView);
        getInstalledApp(gmailPackage, binding.gmail, binding.gmailView);
        getInstalledApp(twitterPackage, binding.twitter, binding.twitterView);
        getInstalledApp(googlePlusPackage, binding.googlePlus, binding.googlePlusView);
    }

    @Override
    protected void doJobInOnCreate(ViewGroup viewGroup, int viewType, DashboardTaskAdapterBinding binding) {
        switch (viewType) {
            case VERIFICATION_DATA:

                if (list.get(VERIFICATION_DATA).getData() != null && ((ArrayList<DataList>) list.get(VERIFICATION_DATA).getData()).size() > 0) {
                    binding.parentView.setVisibility(View.VISIBLE);
                    binding.verifyRecyclerView.setVisibility(View.VISIBLE);
                    binding.shareView.setVisibility(View.GONE);
                    binding.upDownLineView.setVisibility(View.GONE);
                    binding.adsView.setVisibility(View.GONE);
                    binding.taskView.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.titleText.setVisibility(View.VISIBLE);
                    binding.titleText.setText("Required Verification");
                    binding.titleText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error_black,
                            0, 0, 0);
                    binding.verifyRecyclerView.addItemDecoration(new RecyclerViewItemDecoration(8, RecyclerViewItemDecoration.GRID, true));
                    binding.verifyRecyclerView.setLayoutManager(new GridLayoutManager(context, 3));
                    binding.verifyRecyclerView.setAdapter(new
                            DashboardVerifyAdapter(context,
                            (ArrayList<DataList>) list.get(VERIFICATION_DATA).getData(), mVerifyDataCallBack,dashboardFragment));
                } else {
                    binding.parentView.setVisibility(View.GONE);
                    binding.upDownLineView.setVisibility(View.GONE);
                    binding.shareView.setVisibility(View.GONE);
                    binding.adsView.setVisibility(View.GONE);
                    binding.taskView.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.titleText.setVisibility(View.GONE);
                }
                break;
            case ADS_DATA:
                binding.parentView.setVisibility(View.VISIBLE);
                binding.verifyRecyclerView.setVisibility(View.GONE);
                binding.shareView.setVisibility(View.GONE);
                binding.upDownLineView.setVisibility(View.GONE);
                binding.adsView.setVisibility(View.VISIBLE);
                binding.taskView.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.GONE);
                binding.titleText.setVisibility(View.GONE);
                dashboardFragment.setAds(140, 0, binding.adsView, true);
                break;
            case SHARE:
                binding.parentView.setVisibility(View.VISIBLE);
                binding.shareView.setVisibility(View.VISIBLE);
                binding.verifyRecyclerView.setVisibility(View.GONE);
                binding.upDownLineView.setVisibility(View.GONE);
                binding.adsView.setVisibility(View.GONE);
                binding.taskView.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.GONE);
                binding.titleText.setVisibility(View.VISIBLE);
                binding.titleText.setText("Share");
                binding.titleText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_share_black, 0,
                        0, 0);
                DataList dataAffilated = (DataList) list.get(SHARE).getData();
                if (dataAffilated != null && dataAffilated.getLink2() != null) {

                    binding.linkView.setText(dataAffilated.getLink2());

                    binding.chaeckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                binding.linkView.setText(dataAffilated.getLink1());
                            } else {
                                binding.linkView.setText(dataAffilated.getLink2());
                            }
                        }
                    });
                }

                break;
            case DOWNLINE_UPLINE_DATA:
                if (preferencesService.getUplineEmail() != null && !preferencesService.getUplineEmail().isEmpty() &&
                        preferencesService.getUplineMobile() != null && !preferencesService.getUplineMobile().isEmpty()
                        && preferencesService.getUplineName() != null && !preferencesService.getUplineName().isEmpty()) {
                    binding.parentView.setVisibility(View.VISIBLE);
                    binding.verifyRecyclerView.setVisibility(View.GONE);
                    binding.shareView.setVisibility(View.GONE);
                    binding.upDownLineView.setVisibility(View.VISIBLE);
                    binding.adsView.setVisibility(View.GONE);
                    binding.taskView.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.titleText.setVisibility(View.VISIBLE);
                    DataList dataList = (DataList) list.get(DOWNLINE_UPLINE_DATA).getData();

                    binding.titleText.setText("Upline Contact");
                    binding.titleText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_user_up_line_symbol, 0,
                            0, 0);
                    binding.upLineName.setText(preferencesService.getUplineName());
                    //binding.statusDownLine.setTextColor(preferencesService.getDownlineContact()?context.getResources().getColor(R.color.dark_green):context.getResources().getColor(android.R.color.holo_red_dark));
                    binding.upLineEmail.setText(preferencesService.getUplineEmail());
                    //  binding.statusUpline.setTextColor(preferencesService.getUplineContact()?context.getResources().getColor(R.color.dark_green):context.getResources().getColor(android.R.color.holo_red_dark));
                    binding.upLineMobile.setText(preferencesService.getUplineMobile());

                } else {
                    binding.parentView.setVisibility(View.GONE);
                    binding.verifyRecyclerView.setVisibility(View.GONE);
                    binding.shareView.setVisibility(View.GONE);
                    binding.upDownLineView.setVisibility(View.GONE);
                    binding.adsView.setVisibility(View.GONE);
                    binding.taskView.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.titleText.setVisibility(View.GONE);
                }
                break;
            case TASK_STUTUS_DATA:
                binding.parentView.setVisibility(View.VISIBLE);
                binding.taskView.setVisibility(View.VISIBLE);
                binding.shareView.setVisibility(View.GONE);
                binding.upDownLineView.setVisibility(View.GONE);
                binding.adsView.setVisibility(View.GONE);
                binding.verifyRecyclerView.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.GONE);
                binding.titleText.setVisibility(View.VISIBLE);
                DataList data1 = (DataList) list.get(TASK_STUTUS_DATA).getData();

                binding.titleText.setText(data1.getTitle());
                binding.titleText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_task, 0,
                        0, 0);
                binding.completeCount.setText(data1.getCompleted());
                binding.pendingCount.setText(data1.getPending());
                break;
            case INCOME_DATA:
                binding.parentView.setVisibility(View.VISIBLE);
                binding.taskView.setVisibility(View.GONE);
                binding.upDownLineView.setVisibility(View.GONE);
                binding.shareView.setVisibility(View.GONE);
                binding.adsView.setVisibility(View.GONE);
                binding.verifyRecyclerView.setVisibility(View.GONE);
                binding.titleText.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.titleText.setText("Income");
                binding.titleText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_rupee, 0,
                        0, 0);
                changeLayoutManager(binding.recyclerView, ((ArrayList<DataList>) list.get(INCOME_DATA).getData()).size());
                binding.recyclerView.setAdapter(new DashboardDataListAdapter(context,
                        list.get(INCOME_DATA).getDataName(),
                        (ArrayList<DataList>) list.get(INCOME_DATA).getData()));
                break;
            case MEMBER_DATA:
                binding.parentView.setVisibility(View.VISIBLE);
                binding.taskView.setVisibility(View.GONE);
                binding.shareView.setVisibility(View.GONE);
                binding.upDownLineView.setVisibility(View.GONE);
                binding.adsView.setVisibility(View.GONE);
                binding.titleText.setVisibility(View.VISIBLE);
                binding.verifyRecyclerView.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);

                binding.titleText.setText("Member");
                binding.titleText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_member, 0, 0, 0);
                changeLayoutManager(binding.recyclerView, ((ArrayList<DataList>) list.get(MEMBER_DATA).getData()).size());
                binding.recyclerView.setAdapter(new DashboardDataListAdapter(context,
                        list.get(MEMBER_DATA).getDataName(),
                        (ArrayList<DataList>) list.get(MEMBER_DATA).getData()));
                break;
            default://wallet
                binding.parentView.setVisibility(View.VISIBLE);
                binding.taskView.setVisibility(View.GONE);
                binding.upDownLineView.setVisibility(View.GONE);
                binding.shareView.setVisibility(View.GONE);
                binding.adsView.setVisibility(View.GONE);
                binding.titleText.setVisibility(View.VISIBLE);
                binding.verifyRecyclerView.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.titleText.setText("Wallet");
                binding.titleText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_rupee,
                        0, 0, 0);

                changeLayoutManager(binding.recyclerView, ((ArrayList<DataList>) list.get(WALLET_DATA).getData()).size());
                binding.recyclerView.setAdapter(new DashboardDataListAdapter(context,
                        list.get(WALLET_DATA).getDataName(),
                        (ArrayList<DataList>) list.get(WALLET_DATA).getData()));
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.dashboard_task_adapter;
    }

    @Override
    public int getItemViewType(int position) {
        switch (list.get(position).getDataName()) {
            case "VerifyData":
                return VERIFICATION_DATA;
            case "Share":
                return SHARE;
            case "DownLineUpLineData":
                return DOWNLINE_UPLINE_DATA;
            case "Ads":
                return ADS_DATA;
            case "TaskStatus":
                return TASK_STUTUS_DATA;
            case "Income":
                return INCOME_DATA;
            case "Member":
                return MEMBER_DATA;
            default:
                return WALLET_DATA;
        }
    }

    /**
     * Initialised View Holder
     *
     * @param binding DataBinding
     * @return new ViewHolder(binding);
     */
    @Override
    public ViewHolder getViewHolder(DashboardTaskAdapterBinding binding) {
        return new ViewHolder(binding) {
            /**
             * If there is anything to do then do here otherwise leave it blank
             *
             * @param binding  layout reference for single view
             * @param data     for single view
             * @param position position of ArrayList
             */
            @Override
            protected void doSomeWorkHere(DashboardTaskAdapterBinding binding,
                                          DataType data, int position) {
                if (dashboardFragment.pending != 0 && dashboardFragment.completed != 0) {
                    binding.pendingCount.setText(String.valueOf(dashboardFragment.pending));
                    binding.completeCount.setText(String.valueOf(dashboardFragment.completed));
                }
            }

            /**
             * @param data binding.setData(data);
             */
            @Override
            protected void bindData(DataType data) {
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
                                          DashboardTaskAdapterBinding binding, DataType data) {
                binding.btnTask.setOnClickListener(thisContext);
                binding.whatsapp.setOnClickListener(thisContext);
                binding.facebook.setOnClickListener(thisContext);
                binding.gmail.setOnClickListener(thisContext);
                binding.twitter.setOnClickListener(thisContext);
                binding.googlePlus.setOnClickListener(thisContext);
                binding.copyLink.setOnClickListener(thisContext);
                binding.shareNow.setOnClickListener(thisContext);
                ;
            }

            /**
             * Initialised holder by new operator
             *
             * @param bindingData  DataBinding
             * @param data     dataList
             * @param position of adapter
             * @return new ViewHolderClickListener(binding, data, position)
             */
            @Override
            protected ViewHolderClickListener viewHolderReference(DashboardTaskAdapterBinding bindingData,
                                                                  DataType data, int position) {
                return new ViewHolderClickListener(bindingData, position) {
                    @Override
                    public void onClick(View v) {
                        bindingLocal = bindingData;
                        if (context instanceof Home) {
                            ((Home) context).networkErrorViewVisible(View.GONE);
                        }
                        switch (v.getId()) {
                            case R.id.btnTask:
                                if (context instanceof Home) {
                                    if (data.getData() instanceof DataList) {

                                        ((Home) context).fragment =
                                                TaskViewFragment.newInstance(((DataList) data.getData()));
                                        ((Home)
                                                context).isOnTaskCompleted = false;
//                                        TaskViewFragment taskViewFragment = new TaskViewFragment();
//                                        Bundle arg = new Bundle();
//                                        arg.putSerializable("TaskStatus", ((DataList) data.getData()));
//                                        taskViewFragment.setArguments(arg);
                                        ((Home) context).openFragment(((Home) context).fragment,
                                                TaskViewFragment.TAG);
                                    }
                                }

                                break;
                            case R.id.whatsapp:
                                shareContent(whatsAppPackage);
                                break;
                            case R.id.facebook:
                                shareContent(facebookPackage);
                                break;
                            case R.id.gmail:
                                shareContent(gmailPackage);
                                break;
                            case R.id.twitter:
                                shareContent(twitterPackage);
                                break;
                            case R.id.google_plus:
                                shareContent(googlePlusPackage);
                                break;
                            case R.id.copyLink:
                                setClipboard(bindingData.linkView.getText().toString());
                                break;

                            case R.id.shareNow:
                                shareContent(null);
                                break;


                        }
                    }
                };
            }
        };
    }

    @Override
    protected FilterClass initialisedFilterClass() {
        return null;
    }

    void changeLayoutManager(RecyclerView mRecyclerView, int size) {
        //Change 11 Dec
        mRecyclerView.addItemDecoration(new RecyclerViewItemDecoration(10, RecyclerViewItemDecoration.GRID, true));

        GridLayoutManager manager = new GridLayoutManager(context, 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (size % 2 != 0) {
                    return (position == size - 1) ? 2 : 1;
                } else {
                    return 1;
                }
            }
        });
        mRecyclerView.setLayoutManager(manager);
    }

    private void shareContent(String packageName) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String extraText = "<h>Hey your friend</h> <b>" + preferencesService.getUName() +
                "</b> <h>Inviting you to join</h> <b>" + context.getResources().getString(R.string.app_name) +
                "</b> <h>click on given link " + bindingLocal.linkView.getText().toString() +
                " and create account with using </h><b>" +
                bindingLocal.linkView.getText().toString().replace("https://catterpillars.in/home/useraddreferal/", "") +
                "</b> <h>as a sponserid</h>";
        sendIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(extraText).toString());
        sendIntent.setType("text/plain");
        String subText = context.getResources().getString(R.string.app_name) + " Refral link";
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subText);
        if (packageName != null) {
            sendIntent.setPackage(packageName);
        }
        context.startActivity(Intent.createChooser(sendIntent, "Share via"));
    }


    private void getInstalledApp(String packageName, ImageView appIcon, LinearLayout view) {
        try {

            Drawable icon = context.getPackageManager().getApplicationIcon(packageName);
            appIcon.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException ne) {
            appIcon.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        }
    }


    private void setClipboard(String text) {

        try {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(text);
            } else {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Share Link", text);
                clipboard.setPrimaryClip(clip);
            }

            Toast.makeText(context, "Text Copied to clipboard", Toast.LENGTH_LONG).show();
        } catch (Exception e) {

        }
    }
}
