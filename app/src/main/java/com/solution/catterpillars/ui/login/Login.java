package com.solution.catterpillars.ui.login;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.solution.catterpillars.BR;
import com.solution.catterpillars.R;
import com.solution.catterpillars.base.ActivityCalls;
import com.solution.catterpillars.base.BaseActivity;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.data.remort.APIClient;
import com.solution.catterpillars.data.remort.APIService;
import com.solution.catterpillars.databinding.ActivityLoginBinding;
import com.solution.catterpillars.ui.home.Home;
import com.solution.catterpillars.ui.location.LocationActivity;
import com.solution.catterpillars.ui.login.country_flag.CountryFlagDialog;
import com.solution.catterpillars.ui.login.country_flag.FlagNavigator;
import com.solution.catterpillars.ui.login.forget_password.ForgetPassword;
import com.solution.catterpillars.ui.login.model.List;
import com.solution.catterpillars.ui.login.model.ResultLogin;
import com.solution.catterpillars.ui.registration.Registration;
import com.solution.catterpillars.util.ApplicationConstant;
import com.solution.catterpillars.util.CustomAlertDialog;
import com.solution.catterpillars.util.CustomLoader;
import com.solution.catterpillars.util.DeviceId;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.solution.catterpillars.util.ApplicationConstant.ACCESS_TOKEN;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 02-Nov-18
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 * About : I am a mathematician
 * Quote : Only brain can make anything possible
 * Strength : Never give up
 */
public class Login extends BaseActivity<ActivityLoginBinding, LoginViewModel> implements
        FlagNavigator, ActivityCalls {

    private static final String TAG = Login.class.getSimpleName();
    private LoginViewModel loginViewModel;
    private String mobile = "";
    private String password = "";
    private String androidID = "";
    private String tokenCode = "";
    private CustomLoader loader;
    private HashMap<String, Integer> flagSet;
    private ArrayList<List> flagList;
    private String countryCodeMobile;
    private String selectedCountryCode="+91";
    private String selectedCountryId="89";
    private AppPreferencesService preferencesService ;
    private boolean isActivityOpen=false;

    public static Intent newIntent(Context context) {
        return new Intent(context, Login.class);
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
        preferencesService = new AppPreferencesService(context);
        MobileAds.initialize(this, ApplicationConstant.INSTANCE.ADMOB_APP_ID);
        binding.adView.loadAd(new AdRequest.Builder().build());

        flagSet = new HashMap<>();
        flagList = new ArrayList<>();

        char c = 160;
        tokenCode = String.valueOf(c);

        loader = new CustomLoader(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);




        /*TypedValue tv = new TypedValue();
        int actionBarHeight = 54;
        if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
                    getResources().getDisplayMetrics());
        }
        binding.topView.addView(setAds(actionBarHeight / 2, 0));*/

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
        return R.layout.activity_login;
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
     * Set the title here or leave it blank
     */
    @Override
    protected void setTitle() {
//        binding.include.title.setText(getText(R.string.login));
    }

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    @Override
    public LoginViewModel getViewModel() {
        return loginViewModel;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                submitForm();
                break;

//            case R.id.forgetPass:
//                break;

            case R.id.signUp:
                openRegistration();
//                finish();
                break;

            case R.id.flagIcon:
                openFlagDialog();
                break;

            case R.id.forgotPass:
                startActivity(ForgetPassword.newIntent(Login.this));
        }
    }

    private void openFlagDialog() {
        CountryFlagDialog flagDialog = new CountryFlagDialog(this, flagList,null);
        flagDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.full_screen_dialog);
        flagDialog.show(getSupportFragmentManager(), "data");
    }

    /**
     * Injecting dependencies
     */
    @Override
    protected void injectDependencies() {

    }

    @Override
    public void countryCode(String code,String id) {
        binding.countryCode.setText(code);
        selectedCountryCode=code;
        selectedCountryId = id;
    }

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
               // requestFocus(binding.mobile);
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

    private boolean validatePassword() {
        if (binding.password.getText().toString().trim().isEmpty()) {
            binding.password.setError(getString(R.string.errorPassword));
            requestFocus(binding.password);
            return false;
        } else {
            binding.password.setError(null);
        }

        return true;
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

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
    }

    @Override
    public void onPermissionGranted() {
        androidID = DeviceId.INSTANCE.getDeviceId(context);
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

        if (!validatePassword()) {
            return;
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                        == PackageManager.PERMISSION_GRANTED) {
            androidID = DeviceId.INSTANCE.getDeviceId(context);
        } else {
            checkAllPermission();
            return;
        }

        countryCodeMobile = binding.countryCode.getText().toString();
        mobile = binding.mobile.getText().toString();
        password = binding.password.getText().toString();

        if (isNetworkAvailable()) {
            String userId = ACCESS_TOKEN + tokenCode + androidID + tokenCode + mobile;
            apis(userId);

            Log.e(TAG + " userId", userId);
            Log.e(TAG + " accessToken", ACCESS_TOKEN);
            Log.e(TAG + " tokenCode", tokenCode);
            Log.e(TAG + " androidID", androidID);
            Log.e(TAG + " mobile", mobile);
        } else {
            new CustomAlertDialog(context,isActivityOpen).Error(getString(R.string.network_not_found));
            //showToast(R.string.network_error);
        }
    }

    private void apis(String userId) {
        loader.show();
        loader.setCancelable(false);
        loader.setCanceledOnTouchOutside(false);

        APIService apiService = APIClient.getConnect().create(APIService.class);
        Call<ResultLogin> call = null;// = apiService.checkSheetDetails(module, "", "");

        call = apiService.login(userId, password, androidID,selectedCountryCode);

        if (call != null) {
            call.enqueue(new Callback<ResultLogin>() {
                @Override
                public void onResponse(@NonNull Call<ResultLogin> call, @NonNull
                        Response<ResultLogin> response) {
                    ResultLogin data = response.body();
                    if (data != null) {
                        if (data.getStatus().equals("1")) {
                            AppPreferencesService appPreferencesService = new AppPreferencesService(context);
                            appPreferencesService.setSession(true);
                            appPreferencesService.setUserId(data.getData().getUserID());
                            appPreferencesService.setSessionID(data.getData().getSessionID());
                            appPreferencesService.setUName(data.getData().getUName());
                            appPreferencesService.setKey(data.getData().getKey());
                            appPreferencesService.setIcon(data.getData().getIcon());
                            appPreferencesService.setUMobile(data.getData().getUMobile());
                            appPreferencesService.setPassword(password);
                            appPreferencesService.setEmail(data.getData().getEmail());
                            appPreferencesService.setEmailDevice(data.getData().getEmail());
                            appPreferencesService.setCountryCode(countryCodeMobile);

                            appPreferencesService.setTaskStatus(false);

                            //home

                            loader.dismiss();
                            openLocationActivity();
                            //openHomeActivity();
                        } else {
                            loader.dismiss();
                            new CustomAlertDialog(context,isActivityOpen).Error(data.getMessage());
                            //errorDialog(data.getMessage());
                        }
                    } else {
                        loader.dismiss();
                        new CustomAlertDialog(context,isActivityOpen).Error(getString(R.string.try_again));
                       // errorDialog(getString(R.string.try_again));
                    }

//                    binding.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(@NonNull Call<ResultLogin> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    call.cancel();
//                    binding.progressBar.setVisibility(View.GONE);
                    loader.dismiss();
                    if(t.getMessage()!=null && !t.getMessage().isEmpty()) {
                        if (t.getMessage().contains("No address associated with hostname")) {
                            new CustomAlertDialog(context,isActivityOpen).Error(getString(R.string.network_not_found));

                        } else {
                            new CustomAlertDialog(context,isActivityOpen).Error(getString(R.string.try_again));
                        }
                    }/*else{
                        new CustomAlertDialog(context,isActivityOpen).Error(context.getString(R.string.try_again));
                    }*/
                    //errorDialog(getString(R.string.try_again));
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

    /*private void errorDialog(String msg) {
        new CustomAlertDialog(context,isActivityOpen).Error(msg);
        *//*new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setContentText(msg)
                .setCustomImage(R.drawable.ic_error_red_24dp)
                .show();*//*
    }*/

    private void openTaskActivity() {

    }

    private void openLocationActivity() {
        Intent intent = LocationActivity.newIntent(Login.this);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void openHomeActivity() {
        Intent intent = Home.newIntent(Login.this);
        intent.putExtra("isAppOpen", true);
        startActivity(intent);
        finish();
    }

    private void openRegistration() {
        Intent intent = Registration.newIntent(Login.this);
        startActivity(intent);
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