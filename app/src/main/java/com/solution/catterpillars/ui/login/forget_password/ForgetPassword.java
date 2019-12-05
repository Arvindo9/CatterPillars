package com.solution.catterpillars.ui.login.forget_password;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.solution.catterpillars.R;
import com.solution.catterpillars.base.ActivityCalls;
import com.solution.catterpillars.base.BaseActivity;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.data.remort.APIClient;
import com.solution.catterpillars.data.remort.APIService;
import com.solution.catterpillars.databinding.ActivityForgotPasswordBinding;
import com.solution.catterpillars.ui.login.country_flag.FlagNavigator;
import com.solution.catterpillars.ui.login.country_flag.CountryFlagDialog;
import com.solution.catterpillars.ui.login.model.List;
import com.solution.catterpillars.ui.login.model.ResultLogin;
import com.solution.catterpillars.util.ApplicationConstant;
import com.solution.catterpillars.util.CustomAlertDialog;
import com.solution.catterpillars.util.CustomLoader;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 12/14/2018
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 */
public class ForgetPassword extends BaseActivity<ActivityForgotPasswordBinding,
        ForgetPasswordViewModel> implements FlagNavigator, ActivityCalls {

    private CustomLoader loader;
    private HashMap<String, Integer> flagSet;
    private ArrayList<List> flagList;
    private String countryCodeMobile;
    private String selectedCountryCode="+91";
    private String selectedCountryId="89";
    private ForgetPasswordViewModel viewModel;
    private AppPreferencesService preferencesService ;
    private boolean isActivityOpen=false;

    public static Intent newIntent(Context context) {
        return new Intent(context, ForgetPassword.class);
    }

    /**
     * @param state Initialise any thing here before binding
     */
    @Override
    protected void initialization(@Nullable Bundle state) {

    }

    /**
     * Do anything on onCreate after binding
     */
    @Override
    protected void init() {
        MobileAds.initialize(this, ApplicationConstant.INSTANCE.ADMOB_APP_ID);
        binding.adView.loadAd(new AdRequest.Builder().build());
        preferencesService = new AppPreferencesService(context);
        viewModel = new ForgetPasswordViewModel();
        flagSet = new HashMap<>();
        flagList = new ArrayList<>();


        loader = new CustomLoader(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        binding.countryCode.addTextChangedListener(new
                MyTextWatcher(binding.countryCode));

        ResultLogin mResultLogin = new Gson().fromJson(preferencesService.getFlagData(), ResultLogin.class);
        if(mResultLogin!=null && mResultLogin.getList().size()>0){
            setFlagData(mResultLogin);
        }else{
            apisFlag();
        }


    }

    /**
     * @return R.layout.layout_file
     */
    @Override
    protected int getLayout() {
        return R.layout.activity_forgot_password;
    }

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    @Override
    public int getBindingVariable() {
        return 0;
    }

    /**
     * Set the title here or leave it blank
     */
    @Override
    protected void setTitle() {

    }

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    @Override
    public ForgetPasswordViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update:
                submitForm();
                break;

            case R.id.flagIcon:
                openFlagDialog();
                break;

            case R.id.back:
                finish();
                break;

        }
    }

    /**
     * Injecting dependencies
     */
    @Override
    protected void injectDependencies() {

    }

    private void openFlagDialog() {
        CountryFlagDialog flagDialog = new CountryFlagDialog(this, flagList,null);
        flagDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.full_screen_dialog);
        flagDialog.show(getSupportFragmentManager(), "data");
    }


    @Override
    public void countryCode(String code,String id) {
        binding.countryCode.setText(code);
        selectedCountryCode=code;
        selectedCountryId = id;
    }

    /**
     * @param data it will only be called when filter will occur in searchBar
     */
    @Override
    public void callBackToActivity(String data) {

    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (binding.countryCode.getText().toString().trim().isEmpty()) {
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


    private boolean validateMobile() {
        if (binding.mobile.getText().toString().trim().isEmpty() ||
                binding.mobile.getText().toString().length() != 10) {
            binding.mobile.setError(getString(R.string.errorMobile));
            //binding.mobileLayout.setError(getString(R.string.errorMobile));
            requestFocus(binding.mobile);
            return false;
        } else {
            binding.mobile.setError(null);
        }

        return true;
    }

    private boolean validateCountryCode(int value) {
        if (binding.countryCode.getText().toString().trim().isEmpty()) {
            binding.countryCode.setError(getString(R.string.invalidCountryCode));
            requestFocus(binding.countryCode);
            return false;
        } else {
            String countryCode = binding.countryCode.getText().toString();
            int i = 0;
            if (flagSet.containsKey("+" + countryCode)) {
                i = flagSet.get("+" + countryCode);
                List.setFlag(binding.flagIcon, flagList.get(i).getImagePath());
                selectedCountryId=flagList.get(i).getId();
                selectedCountryCode=flagList.get(i).getPhonePrefix();
                binding.countryCode.setText("+" + countryCode);
            } else if (flagSet.containsKey(countryCode) && !countryCode.contains("+")) {
                i = flagSet.get(countryCode);
                List.setFlag(binding.flagIcon, flagList.get(i).getImagePath());
                requestFocus(binding.mobile);
                selectedCountryId=flagList.get(i).getId();
                selectedCountryCode=flagList.get(i).getPhonePrefix();
                binding.countryCode.setText("+" + countryCode);

            } else if (flagSet.containsKey(countryCode)) {
                i = flagSet.get(countryCode);
                List.setFlag(binding.flagIcon, flagList.get(i).getImagePath());
                selectedCountryId=flagList.get(i).getId();
                selectedCountryCode=flagList.get(i).getPhonePrefix();
                //requestFocus(binding.mobile);
            } else if (flagSet.containsKey(countryCode.substring(1))) {
                requestFocus(binding.mobile);
            } else if (countryCode.length() > 1 &&
                    flagSet.containsKey("+" + countryCode.substring(0, 1) +
                            " " + countryCode.substring(1))) {
                String code = "+" + countryCode.substring(0, 1) +
                        " " + countryCode.substring(1);
                i = flagSet.get(code);
                List.setFlag(binding.flagIcon, flagList.get(i).getImagePath());
                selectedCountryId=flagList.get(i).getId();
                binding.countryCode.setText(code);
                selectedCountryCode=flagList.get(i).getPhonePrefix();
            } else if (countryCode.length() > 1 &&
                    flagSet.containsKey("+" + countryCode.substring(0, 1) +
                            "-" + countryCode.substring(1))) {
                String code = "+" + countryCode.substring(0, 1) +
                        "-" + countryCode.substring(1);
                i = flagSet.get(code);
                List.setFlag(binding.flagIcon, flagList.get(i).getImagePath());
                selectedCountryId=flagList.get(i).getId();
                selectedCountryCode=flagList.get(i).getPhonePrefix();
                binding.countryCode.setText(code);
            } else if (value == 2) {
                binding.countryCode.setError(getString(R.string.invalidCountryCode));
                requestFocus(binding.countryCode);
                return false;
            }

            binding.countryCode.setError(null);
        }

        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
    }


    /**
     * Validating form
     */
    private void submitForm() {
        if (!validateCountryCode(2)) {
            return;
        }

        if (!validateMobile()) {
            return;
        }


        countryCodeMobile = binding.countryCode.getText().toString();
        String mobile = binding.mobile.getText().toString();

        if (isNetworkAvailable()) {
            apis(mobile);
        } else {
            new CustomAlertDialog(context,isActivityOpen).Error(getString(R.string.network_not_found));
        }
    }


    private void apis(String mobile) {
        loader.show();
        loader.setCancelable(false);
        loader.setCanceledOnTouchOutside(false);

        APIService apiService = APIClient.getConnect().create(APIService.class);
        Call<ResultLogin> call = null;// = apiService.checkSheetDetails(module, "", "");

        call = apiService.forgetPassword(mobile,selectedCountryCode);

        if (call != null) {
            call.enqueue(new Callback<ResultLogin>() {
                @Override
                public void onResponse(@NonNull Call<ResultLogin> call, @NonNull
                        Response<ResultLogin> response) {
                    ResultLogin data = response.body();
                    if (data != null && data.getStatus().equals("1")) {
                        if (data.getStatus().equals("1")) {
                            new CustomAlertDialog(context,isActivityOpen).SuccessfulWithFinsh(data.getMessage());
                        } else {
                            new CustomAlertDialog(context,isActivityOpen).Error(data.getMessage());
                        }
                    } else {
                        if (data != null && data.getStatus().equals("0")) {
                            new CustomAlertDialog(context,isActivityOpen).Error(data.getMessage());
                        }else {
                            new CustomAlertDialog(context,isActivityOpen).Error(getString(R.string.data_null_error));
                        }
                    }
                    loader.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<ResultLogin> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    call.cancel();
//                    binding.progressBar.setVisibility(View.GONE);
                    loader.dismiss();
                   /* if (t.getMessage().contains("No address associated with hostname")) {
                        new CustomAlertDialog(context,isActivityOpen).Error(getString(R.string.network_not_found));

                    } else {
                        new CustomAlertDialog(context,isActivityOpen).Error(getString(R.string.try_again));
                    }*/

                    if(t.getMessage()!=null && !t.getMessage().isEmpty()) {
                        if (t.getMessage().contains("No address associated with hostname")) {
                            new CustomAlertDialog(context,isActivityOpen).Error(getString(R.string.network_not_found));

                        } else {
                            new CustomAlertDialog(context,isActivityOpen).Error(getString(R.string.try_again));
                        }
                    }/*else{
                        new CustomAlertDialog(context,isActivityOpen).Error(getString(R.string.try_again));
                    }*/
                }
            });
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
            call.enqueue(new Callback<ResultLogin>() {
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

    private void setFlagData(ResultLogin data){
        if (data.getStatus().equals("1")) {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //getNetworkCountryIso
            int countryPosition = 0;
            String CountryID = manager.getSimCountryIso().toUpperCase();

            flagList.addAll(data.getList());
            for (int i = 0; i < data.getList().size(); i++) {
                flagSet.put(data.getList().get(i).getPhonePrefix(), i);
                if (CountryID.length()>0 && data.getList().get(i).getISOCode1().toLowerCase().contains(CountryID.toLowerCase())) {
                    countryPosition = i;
                }
            }

            countryCode(data.getList().get(countryPosition).getPhonePrefix(),
                    data.getList().get(countryPosition).getId());


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActivityOpen=false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActivityOpen=true;
    }

}
