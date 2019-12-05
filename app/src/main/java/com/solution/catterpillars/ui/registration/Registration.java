package com.solution.catterpillars.ui.registration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
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
import com.solution.catterpillars.databinding.ActivityRegistrationBinding;
import com.solution.catterpillars.ui.home.Home;
import com.solution.catterpillars.ui.login.country_flag.CountryFlagDialog;
import com.solution.catterpillars.ui.login.country_flag.FlagNavigator;
import com.solution.catterpillars.ui.login.model.List;
import com.solution.catterpillars.ui.login.model.ResultLogin;
import com.solution.catterpillars.ui.registration.model.ResultRegistration;
import com.solution.catterpillars.util.ApplicationConstant;
import com.solution.catterpillars.util.AppsFunction;
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
 * Created on : 02-Nov-18
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 * About : I am a mathematician
 * Quote : Only brain can make anything possible
 * Strength : Never give up
 */
public class Registration extends BaseActivity<ActivityRegistrationBinding, RegistrationViewModel>
        implements FlagNavigator,ActivityCalls {

    private RegistrationViewModel createPasswordViewModel;
    private String password = "";
    private String referenceCode = "";
    private String mobile = "";
    private String userName = "";
    private String email = "";
    private String confirmPassword = "";
    private CustomLoader loader;
    private String selectedCountryCode="+91";
    private String selectedCountryId="89";
    private HashMap<String, Integer> flagSet;
    private ArrayList<List> flagList;
    private String countryCodeMobile;
    private AppPreferencesService preferencesService ;
    private boolean isActivityOpen;

    public static Intent newIntent(Context context) {
        return new Intent(context, Registration.class);
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

        mobile = getIntent().getStringExtra("MobileNumber");
        referenceCode = getIntent().getStringExtra("ReferenceNumber");

        flagSet = new HashMap<>();
        flagList = new ArrayList<>();

        loader = new CustomLoader(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

       /* TypedValue tv = new TypedValue();
        int actionBarHeight=54;
        if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        binding.topView.addView(setAds(actionBarHeight/2,0));*/

        binding.referenceCode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (binding.referenceCode.getRight() -
                            binding.referenceCode.getCompoundDrawables()
                                    [DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        AppsFunction.getContactBook(Registration.this);
                        return true;
                    }
                }
                return false;
            }

        });


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
        return R.layout.activity_registration;
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
//        binding.include.title.setText(getText(R.string.registration));
    }

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    @Override
    public RegistrationViewModel getViewModel() {
        return createPasswordViewModel;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                submitForm();
                break;

            case R.id.tc:
                break;

            case R.id.loginBack:
                finish();
                break;

            case R.id.privacyPolicy:
                break;

            case R.id.getSponserID:
                binding.referenceCode.setText(getString(R.string.defaultReferenceCode));
                break;

            case R.id.flagIcon:
                openFlagDialog();
                break;

            case R.id.back:
                finish();
                break;
        }
    }

    private void openFlagDialog(){
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

    private boolean validateUserName() {
        if (binding.userName.getText().toString().trim().isEmpty()) {
            binding.userName.setError(getString(R.string.errorUserName));
            requestFocus(binding.userName);
            return false;
        } else {
            binding.userName.setError(null);
        }

        return true;
    }

    private boolean validateEmail() {
        if (binding.email.getText().toString().trim().isEmpty() ||
                !binding.email.getText().toString().contains("@") ||
                !binding.email.getText().toString().contains(".") ) {
            binding.email.setError(getString(R.string.errorEmail));
            requestFocus(binding.email);
            return false;
        } else {
            binding.email.setError(null);
        }

        return true;
    }

    private boolean validatePassword() {
        if (binding.password.getText().toString().trim().isEmpty()) {
            binding.password.setError(getString(R.string.errorPassword));
            requestFocus(binding.password);
            return false;
        }
        else if (binding.password.getText().toString().length() < 6) {
            binding.password.setError(getString(R.string.errorPasswordLength));
            requestFocus(binding.password);
            return false;
        }
        else {
            binding.password.setError(null);
        }

        return true;
    }

    private boolean validateMobile() {
        if (binding.mobile.getText().toString().trim().isEmpty() ||
                binding.mobile.getText().toString().length() != 10) {
            binding.mobile.setError(getString(R.string.errorMobile));
            requestFocus(binding.mobile);
            return false;
        } else {
            binding.mobile.setError(null);
        }

        return true;
    }

    private boolean validateReference() {
        if (binding.referenceCode.getText().toString().trim().isEmpty()) {
            binding.referenceCode.setError(getString(R.string.errorReferenceCode));
            requestFocus(binding.referenceCode);
            return false;
        } else {
            binding.referenceCode.setError(null);
        }

        return true;
    }

    private boolean validateConfirmPassword() {
//        String dsf = binding.confirmPasswordE.getText().toString();
        if (binding.confirmPassword.getText().toString().trim().isEmpty() ||
                !binding.confirmPassword.getText().toString().equals(password)) {
            binding.confirmPassword.setError(getString(R.string.errorConfirmPassword));
            requestFocus(binding.confirmPassword);
            return false;
        } else {
            binding.confirmPassword.setError(null);
        }

        return true;
    }


    private boolean validateCountryCode(int value) {
        if (binding.countryCode.getText().toString().trim().isEmpty()) {
            binding.countryCode.setError(getString(R.string.invalidCountryCode));
            requestFocus(binding.countryCode);
            return false;
        }
        else {
            String countryCode = binding.countryCode.getText().toString();
            int i = 0;
            if(flagSet.containsKey("+" + countryCode)) {
                i = flagSet.get("+" + countryCode);
                selectedCountryId=flagList.get(i).getId();
                selectedCountryCode=flagList.get(i).getPhonePrefix();
                List.setFlag(binding.flagIcon, flagList.get(i).getImagePath());
                binding.countryCode.setText("+" + countryCode);
            }
            else if(flagSet.containsKey(countryCode) && !countryCode.contains("+")) {
                i = flagSet.get(countryCode);
                selectedCountryId=flagList.get(i).getId();
                selectedCountryCode=flagList.get(i).getPhonePrefix();
                List.setFlag(binding.flagIcon, flagList.get(i).getImagePath());
                requestFocus(binding.mobile);
                binding.countryCode.setText("+" + countryCode);

            }
            else if(flagSet.containsKey(countryCode)) {
                i = flagSet.get(countryCode);
                selectedCountryId=flagList.get(i).getId();
                selectedCountryCode=flagList.get(i).getPhonePrefix();
                List.setFlag(binding.flagIcon, flagList.get(i).getImagePath());
                //requestFocus(binding.mobile);
            }
            else if(flagSet.containsKey(countryCode.substring(1))){
                requestFocus(binding.mobile);
            }
            else if(countryCode.length() > 1 &&
                    flagSet.containsKey("+" + countryCode.substring(0, 1) +
                            " " + countryCode.substring(1))) {
                String code = "+" + countryCode.substring(0, 1) +
                        " " + countryCode.substring(1);
                i = flagSet.get(code);
                selectedCountryId=flagList.get(i).getId();
                selectedCountryCode=flagList.get(i).getPhonePrefix();
                List.setFlag(binding.flagIcon, flagList.get(i).getImagePath());
                binding.countryCode.setText(code);
            }

            else if(countryCode.length() > 1 &&
                    flagSet.containsKey("+" + countryCode.substring(0, 1) +
                            "-" + countryCode.substring(1))) {
                String code = "+" + countryCode.substring(0, 1) +
                        "-" + countryCode.substring(1);
                i = flagSet.get(code);
                selectedCountryId=flagList.get(i).getId();
                selectedCountryCode=flagList.get(i).getPhonePrefix();
                List.setFlag(binding.flagIcon, flagList.get(i).getImagePath());
                binding.countryCode.setText(code);
            }
            else if(value ==2){
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

    private void submitForm() {
        if (!validateReference()) {
            return;
        }

        if (!validateUserName()) {
            return;
        }

        if(!validateCountryCode(2)){
            return;
        }

        if (!validateMobile()) {
            return;
        }
        if (!validateEmail()) {
            return;
        }if (!validatePassword()) {
            return;
        }


        password = binding.password.getText().toString();
//        if (!validateConfirmPassword()) {
//            return;
//        }

        //enable this when tc will linked
//        if(!binding.checkBox.isChecked()){
//            showToast(R.string.acceptTc);
//            return;
//        }

        referenceCode = binding.referenceCode.getText().toString();
        countryCodeMobile = binding.countryCode.getText().toString();
        mobile = binding.mobile.getText().toString();
        password = binding.password.getText().toString();
        userName = binding.userName.getText().toString();
        email = binding.email.getText().toString();
//        confirmPassword = binding.confirmPassword.getText().toString();
        confirmPassword = password;

        if(isNetworkAvailable()) {
            apis();
        }
        else{
            new CustomAlertDialog(context,isActivityOpen).Error(getString(R.string.network_not_found));
            //showToast(R.string.network_error);
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (ApplicationConstant.PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};

                    Cursor phone = null;
                    if (contactData != null) {
                        phone = getContentResolver().query(contactData,
                                projection, null,
                                null, null);
                    }
                    if (phone != null && phone.moveToFirst()) {
                        int column = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        String number = phone.getString(column);

                        binding.referenceCode.setText(number);
                        phone.close();
                    }
                }
                break;
        }
    }

    private void apis(){
        loader.show();
        loader.setCancelable(false);
        loader.setCanceledOnTouchOutside(false);
//        binding.progressBar.setVisibility(View.VISIBLE);
        APIService apiService = APIClient.getConnect().create(APIService.class);
        Call<ResultRegistration> call = null;// = apiService.checkSheetDetails(module, "", "");

        call = apiService.registration(referenceCode, userName, mobile, email, password, confirmPassword,selectedCountryCode,selectedCountryId);

        if (call != null) {
            call.enqueue(new Callback<ResultRegistration>() {
                @Override
                public void onResponse(@NonNull Call<ResultRegistration> call, @NonNull
                        Response<ResultRegistration> response) {
                    ResultRegistration data = response.body();
                    if(data != null) {
                        if (data.getStatus().equals("1")) {

                            //true
                            AppPreferencesService appPreferencesService =
                                    new AppPreferencesService(getApplicationContext());
//                        appPreferencesService.setSession(true);
                            appPreferencesService.setTaskStatus(false);
//                            appPreferencesService.setUMobile(mobile);
//                            appPreferencesService.setPassword(password);
//                            appPreferencesService.setUName(userName);
//                            appPreferencesService.setUserReferenceCode(referenceCode);
//                            appPreferencesService.setEmail(email);

                            Log.e("REgistration", String.valueOf(appPreferencesService.isSessionActive()));

                            //home
                            loader.dismiss();
                            openLogin();
                        } else {
                            loader.dismiss();
                            errorDialog(data.getMessage());
                        }
                    }
                    else {
                        loader.dismiss();
                        errorDialog(getString(R.string.try_again));
//                        Snackbar.make(findViewById(android.R.id.content), getText(R.string.try_again),
//                                Snackbar.LENGTH_INDEFINITE).show();
                    }

//                    binding.progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(@NonNull Call<ResultRegistration> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    call.cancel();
                    loader.dismiss();
                    if(t.getMessage()!=null && !t.getMessage().isEmpty()) {
                        if (t.getMessage().contains("No address associated with hostname")) {
                            errorDialog(getString(R.string.network_not_found));

                        } else {
                            errorDialog(getString(R.string.try_again));
                        }
                    }/*else{
                        errorDialog(context.getString(R.string.try_again));
                    }*/

                }
            });
        }
    }


    private void apisFlag(){
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
            for(int i = 0; i < data.getList().size(); i++) {
                flagSet.put(data.getList().get(i).getPhonePrefix(), i);
                if (CountryID.length()>0 && data.getList().get(i).getISOCode1().toLowerCase().contains(CountryID.toLowerCase())) {
                    countryPosition = i;
                }
            }
            countryCode(data.getList().get(countryPosition).getPhonePrefix(),data.getList().get(countryPosition).getId());
        }
    }

    private void openLogin() {

        new CustomAlertDialog(context,isActivityOpen).registrationDialog(getString(R.string.registrationSuccessful));
//        new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
//                .setContentText(getString(R.string.registrationSuccessful))
//                .setCustomImage(R.drawable.ic_success)
//                .setConfirmButton("Login now", new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sweetAlertDialog) {
////                        Intent intent = Login.newIntent(Registration.this);
////                        startActivity(intent);
//                        sweetAlertDialog.dismiss();
//                        finish();
//                    }
//                })
//                .show();
    }


    private void errorDialog(String msg) {
        new CustomAlertDialog(context,isActivityOpen).Error(msg);
        /*new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setContentText(msg)
                .setCustomImage(R.drawable.ic_error_red_24dp)
                .show();*/
    }

    private void openHomeActivity(){
        Intent intent = Home.newIntent(Registration.this);
        intent.putExtra("UserId", mobile);
        intent.putExtra("Password",password);
        intent.putExtra("TaskStatus", false);
        intent.putExtra("isAppOpen", true);
        startActivity(intent);
        finish();

//        Intent intent = TaskActivity.newIntent(Registration.this);
//        intent.putExtra("Mobile", mobile);
//        startActivity(intent);
//        finish();
    }

    @Override
    public void countryCode(String code,String id) {
        binding.countryCode.setText(code);
         selectedCountryCode=code;
       selectedCountryId=id;
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