package com.solution.catterpillars.ui.home.dashboard;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.solution.catterpillars.R;
import com.solution.catterpillars.base.BaseAdapter;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.data.remort.APIClient;
import com.solution.catterpillars.data.remort.APIService;
import com.solution.catterpillars.databinding.DashboardVerifyItemAdapterBinding;
import com.solution.catterpillars.ui.home.Home;
import com.solution.catterpillars.ui.home.content.HomeFragment;
import com.solution.catterpillars.ui.home.dashboard.callbackInterface.VerifyDataCallBack;
import com.solution.catterpillars.ui.home.dashboard.model.DataList;
import com.solution.catterpillars.ui.home.dashboard.model.VerifyMobileModel;
import com.solution.catterpillars.util.ApplicationConstant;
import com.solution.catterpillars.util.CustomAlertDialog;
import com.solution.catterpillars.util.CustomLoader;
import com.solution.catterpillars.util.PinEntryEditText;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 01-Nov-18
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 */
public class DashboardVerifyAdapter extends BaseAdapter<DashboardVerifyItemAdapterBinding, DataList> {

    private CustomLoader loader;
    private AppPreferencesService preferencesService;
    private CountDownTimer countDownTimer;
    VerifyDataCallBack mVerifyDataCallBack;
    DashboardFragment dashboardFragment;
    /**
     * @param context          reference of context
     * @param mVerifyListItems DataList
     */
    public DashboardVerifyAdapter(Context context, ArrayList<DataList> mVerifyListItems, VerifyDataCallBack mVerifyDataCallBack,DashboardFragment dashboardFragment) {
        super(context, mVerifyListItems);
        this.dashboardFragment=dashboardFragment;
        this.mVerifyDataCallBack = mVerifyDataCallBack;
        MobileAds.initialize(context, ApplicationConstant.INSTANCE.ADMOB_APP_ID);
        preferencesService = new AppPreferencesService(context);
        loader = new CustomLoader(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
    }

    /**
     * This will hide the view on first load
     * left blank if no view has to hide
     *
     * @param binding DataBinding
     */
    @Override
    protected void hideView(DashboardVerifyItemAdapterBinding binding) {

    }

    /**
     * This is use to make the hidden view on first load
     * left blank if noting is hidden
     *
     * @param binding DataBinding
     */
    @Override
    protected void showView(DashboardVerifyItemAdapterBinding binding) {

    }

    /**
     * @return R.layout.layout_file
     */
    @Override
    protected int getLayout() {
        return R.layout.dashboard_verify_item_adapter;
    }

    /**
     * Initialised View Holder
     *
     * @param binding DataBinding
     * @return new ViewHolder(binding);
     */
    @Override
    public ViewHolder getViewHolder(DashboardVerifyItemAdapterBinding binding) {
        return new ViewHolder(binding) {
            @Override
            protected void doSomeWorkHere(DashboardVerifyItemAdapterBinding binding,
                                          DataList data, int position) {
                if (data.getType().equalsIgnoreCase("Mobile")) {
                    binding.icon.setImageResource(R.drawable.ic_mobile);
                }
                if (data.getType().equalsIgnoreCase("Email")) {
                    binding.icon.setImageResource(R.drawable.ic_email);
                }
                if (data.getType().equalsIgnoreCase("Bank")) {
                    binding.icon.setImageResource(R.drawable.ic_bank);
                }
            }

            @Override
            protected void bindData(DataList data) {
                binding.setData(data);
            }

            @Override
            public void setClickListeners(ViewHolderClickListener thisContext, DashboardVerifyItemAdapterBinding binding, DataList data) {
                binding.layout.setOnClickListener(thisContext);
            }

            @Override
            protected ViewHolderClickListener viewHolderReference(DashboardVerifyItemAdapterBinding binding,
                                                                  DataList data, int position) {
                return new ViewHolderClickListener(binding, position) {
                    @Override
                    public void onClick(View v) {
                        if (context instanceof Home) {
                            ((Home) context).networkErrorViewVisible(View.GONE);
                        }
                        switch (v.getId()) {
                            case R.id.layout:
                                if (data.getType().equalsIgnoreCase("Mobile")) {
                                    new CustomAlertDialog(context,dashboardFragment.isFragmentOpen).verifyPhone(context.getString(R.string.verify_mobile_msg), new CustomAlertDialog.DialogCallBack() {
                                        @Override
                                        public void onPositiveClick() {
                                            if (data.getMobileVerificationLink() != null && data.getMobileVerificationLink().size() > 0) {
                                                getMobileVerificationLinkId(preferencesService.getUMobile(), data);

                                            } else {
                                                apisVerifyMobile(preferencesService.getUMobile(), "1", data);
                                            }


                                        }

                                        @Override
                                        public void onNegativeClick() {

                                        }
                                    });
                                }
                                if (data.getType().equalsIgnoreCase("Email")) {
                                    emailVerify(preferencesService.getUMobile());
                                }
                                if (data.getType().equalsIgnoreCase("Bank")) {
                                    if(context instanceof Home) {
                                        String mobile = preferencesService.getUMobile();
                                        String password = preferencesService.getPassword();
                                        ((Home) context).fragment = HomeFragment.newInstance("https://www.catterpillars.in/Home/MobileLogin?username=" + mobile + "&password=" + password + "&type=2");
                                        ((HomeFragment) ((Home)
                                                context).fragment).setFragmentTitle("Verify Bank");

                                        ((Home) context).openFragment(((Home) context).fragment, HomeFragment.TAG);
                                    }
                                }
                                break;

                        }
                    }
                };
            }
        };
    }


    private void getMobileVerificationLinkId(String mobile, DataList data) {
        Random random = new Random();
        int randomIndex = random.nextInt(data.getMobileVerificationLink().size());
        String apiIndex = "0";
        if (preferencesService.getSendMobileVerifyLinkId().equalsIgnoreCase(data.getMobileVerificationLink().get(randomIndex).getId())) {
            getMobileVerificationLinkId(mobile, data);
        } else {
            apiIndex = data.getMobileVerificationLink().get(randomIndex).getId();
            preferencesService.setSendMobileVerifyLinkId(data.getMobileVerificationLink().get(randomIndex).getId());
            apisVerifyMobile(mobile, apiIndex, data);
        }

    }


    private void apisVerifyMobile(String mobile, String apiIndex, DataList mDataList) {
        loader.show();
        loader.setCancelable(false);
        loader.setCanceledOnTouchOutside(false);

        APIService apiService = APIClient.getConnect().create(APIService.class);
        Call<VerifyMobileModel> call = null;// = apiService.checkSheetDetails(module, "", "");

        call = apiService.sendMobileOtp(mobile, apiIndex);

        if (call != null) {
            call.enqueue(new Callback<VerifyMobileModel>() {
                @Override
                public void onResponse(@NonNull Call<VerifyMobileModel> call, @NonNull
                        Response<VerifyMobileModel> response) {
                    final VerifyMobileModel data = response.body();
                    if (data != null && data.getStatus().equals("1")) {
                        if (data.getStatus().equals("1")) {
                            new CustomAlertDialog(context,dashboardFragment.isFragmentOpen).SuccessfulWithCallBack(data.getMessage(), new CustomAlertDialog.DialogCallBack() {
                                @Override
                                public void onPositiveClick() {
                                    openOtpVerificationDialog(mobile, mDataList);

                                }

                                @Override
                                public void onNegativeClick() {

                                }
                            });
                        } else {
                            new CustomAlertDialog(context,dashboardFragment.isFragmentOpen).Error(data.getMessage());
                        }
                    } else {
                        if (data != null && data.getStatus().equals("0")) {
                            new CustomAlertDialog(context,dashboardFragment.isFragmentOpen).Error(data.getMessage());
                        } else {
                            new CustomAlertDialog(context,dashboardFragment.isFragmentOpen).Error(context.getString(R.string.data_null_error));
                        }
                    }
                    loader.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<VerifyMobileModel> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    call.cancel();
//                    binding.progressBar.setVisibility(View.GONE);
                    loader.dismiss();
                    /*if (t.getMessage().contains("No address associated with hostname")) {
                        new CustomAlertDialog(context,dashboardFragment.isFragmentOpen).Error(context.getString(R.string.network_not_found));

                    } else {
                        new CustomAlertDialog(context,dashboardFragment.isFragmentOpen).Error(context.getString(R.string.try_again));
                    }*/
                    if(t.getMessage()!=null && !t.getMessage().isEmpty()) {
                        if (t.getMessage().contains("No address associated with hostname")) {
                            new CustomAlertDialog(context,dashboardFragment.isFragmentOpen).Error(context.getString(R.string.network_not_found));

                        } else {
                            new CustomAlertDialog(context,dashboardFragment.isFragmentOpen).Error(context.getString(R.string.try_again));
                        }
                    }/*else{
                        new CustomAlertDialog(context,dashboardFragment.isFragmentOpen).Error(context.getString(R.string.try_again));
                    }*/
                }
            });
        }
    }


    private void openOtpVerificationDialog(final String phoneNumber, DataList data) {

        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        dialog.setContentView(R.layout.dialog_otp_verification);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View currentOpenScreenMainView = dialog.findViewById(R.id.main_layout);

        LinearLayout mAdView = dialog.findViewById(R.id.adView);

        TextView phone = dialog.findViewById(R.id.phone_text);
        phone.setText(phoneNumber);
        final TextView timer = dialog.findViewById(R.id.timer);
        timer.setVisibility(View.VISIBLE);
        final TextView resendcode = dialog.findViewById(R.id.resendCode);
        PinEntryEditText otp_editText = dialog.findViewById(R.id.txt_pin_entry);
        final Button continueBtn = dialog.findViewById(R.id.verifyBtn);
        ImageView closeImage = dialog.findViewById(R.id.closeImage);
        dialog.show();

        otp_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (otp_editText.getText().length() >= 6) {
                        continueBtn.setTextColor(Color.WHITE);
                        continueBtn.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorAccent));

                    } else {
                        continueBtn.setTextColor(Color.BLACK);
                        continueBtn.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.gray_btn_bg_pressed_color));
                    }
                }
            }
        });

        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        resendcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.setVisibility(View.VISIBLE);
                resendcode.setVisibility(View.GONE);
                dialog.dismiss();
                getMobileVerificationLinkId(phoneNumber, data);


            }
        });


        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoard(currentOpenScreenMainView);
                if (otp_editText != null && otp_editText.getText().length() == 0 || otp_editText.getText().length() < 6) {
                    otp_editText.setError("Wrong OTP");
                } else if (otp_editText != null && otp_editText.getText().length() > 0) {
                    submitOtp( otp_editText.getText().toString(),phoneNumber,dialog);

                }
            }


        });

        setTimer(timer, resendcode);

        AdView adView = new AdView(context);
        adView.setAdSize(AdSize.LARGE_BANNER);
        adView.setAdUnitId(context.getString(R.string.adsBannerSplash));
        adView.loadAd(new AdRequest.Builder().build());
        mAdView.addView(adView);
    }


    private void hideKeyBoard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void setTimer(TextView timer, TextView resendcode) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timer.setText("Resend Code in 00:00");
        countDownTimer = new CountDownTimer(60000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                timer.setText("Resend Code in " + String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                timer.setVisibility(View.GONE);
                resendcode.setVisibility(View.VISIBLE);
            }
        }.start();
    }


    private void submitOtp(String otp, String mobile,Dialog dialog) {
        loader.show();
        loader.setCancelable(false);
        loader.setCanceledOnTouchOutside(false);

        APIService apiService = APIClient.getConnect().create(APIService.class);
        Call<VerifyMobileModel> call = null;// = apiService.checkSheetDetails(module, "", "");

        call = apiService.verifyMobileOtp(mobile, otp);

        if (call != null) {
            call.enqueue(new Callback<VerifyMobileModel>() {
                @Override
                public void onResponse(@NonNull Call<VerifyMobileModel> call, @NonNull
                        Response<VerifyMobileModel> response) {
                    final VerifyMobileModel data = response.body();
                    if (data != null && data.getStatus().equals("1")) {
                        if (data.getStatus().equals("1")) {
                            dialog.dismiss();
                            mVerifyDataCallBack.onSuccess();
                            new CustomAlertDialog(context,dashboardFragment.isFragmentOpen).Successful(data.getMessage());
                        } else {
                            new CustomAlertDialog(context,dashboardFragment.isFragmentOpen).Error(data.getMessage());
                        }
                    } else {
                        if (data != null && data.getStatus().equals("0")) {
                            new CustomAlertDialog(context,dashboardFragment.isFragmentOpen).Error(data.getMessage());
                        } else {
                            new CustomAlertDialog(context,dashboardFragment.isFragmentOpen).Error(context.getString(R.string.data_null_error));
                        }
                    }
                    loader.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<VerifyMobileModel> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    call.cancel();
//                    binding.progressBar.setVisibility(View.GONE);
                    loader.dismiss();
                   /* if (t.getMessage().contains("No address associated with hostname")) {
                        new CustomAlertDialog(context,dashboardFragment.isFragmentOpen).Error(context.getString(R.string.network_not_found));

                    } else {
                        new CustomAlertDialog(context,dashboardFragment.isFragmentOpen).Error(context.getString(R.string.try_again));
                    }*/

                    if(t.getMessage()!=null && !t.getMessage().isEmpty()) {
                        if (t.getMessage().contains("No address associated with hostname")) {
                            new CustomAlertDialog(context,dashboardFragment.isFragmentOpen).Error(context.getString(R.string.network_not_found));

                        } else {
                            new CustomAlertDialog(context,dashboardFragment.isFragmentOpen).Error(context.getString(R.string.try_again));
                        }
                    }/*else{
                        new CustomAlertDialog(context,dashboardFragment.isFragmentOpen).Error(context.getString(R.string.try_again));
                    }*/
                }
            });
        }
    }


    private void emailVerify(String mobile) {
        loader.show();
        loader.setCancelable(false);
        loader.setCanceledOnTouchOutside(false);

        APIService apiService = APIClient.getConnect().create(APIService.class);
        Call<VerifyMobileModel> call = null;// = apiService.checkSheetDetails(module, "", "");

        call = apiService.verifyEmail(mobile);

        if (call != null) {
            call.enqueue(new Callback<VerifyMobileModel>() {
                @Override
                public void onResponse(@NonNull Call<VerifyMobileModel> call, @NonNull
                        Response<VerifyMobileModel> response) {
                    final VerifyMobileModel data = response.body();
                    if (data != null && data.getStatus().equals("1")) {
                        if (data.getStatus().equals("1")) {
                            mVerifyDataCallBack.onSuccess();
                            new CustomAlertDialog(context,dashboardFragment.isFragmentOpen).Successful(data.getMessage());
                        } else {
                            new CustomAlertDialog(context,dashboardFragment.isFragmentOpen).Error(data.getMessage());
                        }
                    } else {
                        if (data != null && data.getStatus().equals("0")) {
                            new CustomAlertDialog(context,dashboardFragment.isFragmentOpen).Error(data.getMessage());
                        } else {
                            new CustomAlertDialog(context,dashboardFragment.isFragmentOpen).Error(context.getString(R.string.data_null_error));
                        }
                    }
                    loader.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<VerifyMobileModel> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    call.cancel();

//                    binding.progressBar.setVisibility(View.GONE);
                    loader.dismiss();
                    if(t.getMessage()!=null && !t.getMessage().isEmpty()) {
                        if (t.getMessage().contains("No address associated with hostname")) {
                            new CustomAlertDialog(context,dashboardFragment.isFragmentOpen).Error(context.getString(R.string.network_not_found));

                        } else {
                            new CustomAlertDialog(context,dashboardFragment.isFragmentOpen).Error(context.getString(R.string.try_again));
                        }
                    }/*else{
                        new CustomAlertDialog(context,dashboardFragment.isFragmentOpen).Error(context.getString(R.string.try_again));
                    }*/
                }
            });
        }
    }

    /**
     * @return new FilterClass();
     */
    @Override
    protected FilterClass initialisedFilterClass() {
        return null;
    }

   /* @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }*/
}
