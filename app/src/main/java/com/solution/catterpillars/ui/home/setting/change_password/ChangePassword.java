package com.solution.catterpillars.ui.home.setting.change_password;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.solution.catterpillars.R;
import com.solution.catterpillars.base.BaseActivity;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.data.remort.APIClient;
import com.solution.catterpillars.data.remort.APIService;
import com.solution.catterpillars.databinding.ActivityChangePasswordBinding;
import com.solution.catterpillars.ui.home.setting.change_password.model.ChangePasswordViewModel;
import com.solution.catterpillars.util.ApplicationConstant;
import com.solution.catterpillars.util.CustomAlertDialog;
import com.solution.catterpillars.util.CustomLoader;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 12/8/2018
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 */
public class ChangePassword extends BaseActivity<ActivityChangePasswordBinding, ChangePasswordViewModel> {

    private ChangePasswordViewModel viewModel;
    private String newPassword;
    private String oldPassword;
    private String confirmPassword;
    private CustomLoader loader;
    public AppPreferencesService preferencesService;
    private boolean isActivityOpen = false;


    public static Intent newIntent(Context context) {
        return new Intent(context, ChangePassword.class);
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
        loader = new CustomLoader(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
    }

    /**
     * @return R.layout.layout_file
     */
    @Override
    protected int getLayout() {
        return R.layout.activity_change_password;
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
    public ChangePasswordViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update:
                submitForm();
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

    private boolean validateOldPassword() {
        if (binding.oldPassword.getText().toString().trim().isEmpty()) {
            binding.oldPassword.setError(getString(R.string.errorPassword));
            requestFocus(binding.oldPassword);
            return false;
        } else {
            binding.oldPassword.setError(null);
        }

        return true;
    }

    private boolean validateNewPassword() {
        if (binding.newPassword.getText().toString().trim().isEmpty()) {
            binding.newPassword.setError(getString(R.string.errorPassword));
            requestFocus(binding.newPassword);
            return false;
        } else if (binding.newPassword.getText().toString().length() < 6) {
            binding.newPassword.setError(getString(R.string.errorPasswordLength));
            requestFocus(binding.newPassword);
            return false;
        } else {
            binding.newPassword.setError(null);
        }

        return true;
    }

    private boolean validateConfirmPassword() {
//        String dsf = binding.confirmPasswordE.getText().toString();
        if (binding.confirmPassword.getText().toString().trim().isEmpty() ||
                !binding.confirmPassword.getText().toString().equals(newPassword)) {
            binding.confirmPassword.setError(getString(R.string.errorConfirmPassword));
            requestFocus(binding.confirmPassword);
            return false;
        } else {
            binding.confirmPassword.setError(null);
        }

        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
    }


    private void submitForm() {

        if (!validateOldPassword()) {
            return;
        }

        if (!validateNewPassword()) {
            return;
        }

        newPassword = binding.newPassword.getText().toString();

        if (!validateConfirmPassword()) {
            return;
        }

        oldPassword = binding.oldPassword.getText().toString();
        confirmPassword = binding.confirmPassword.getText().toString();

        if (isNetworkAvailable()) {
            apis();
        } else {
            showToast(R.string.network_error);
        }
    }


    private void apis() {
        loader.show();
        loader.setCancelable(false);
        loader.setCanceledOnTouchOutside(false);
//        binding.progressBar.setVisibility(View.VISIBLE);
        APIService apiService = APIClient.getConnect().create(APIService.class);
        Call<ChangePasswordViewModel> call = null;// = apiService.checkSheetDetails(module, "", "");

        call = apiService.changePassword(preferencesService.getUMobile(), oldPassword, newPassword, confirmPassword);

        if (call != null) {
            call.enqueue(new Callback<ChangePasswordViewModel>() {
                @Override
                public void onResponse(@NonNull Call<ChangePasswordViewModel> call, @NonNull
                        Response<ChangePasswordViewModel> response) {
                    ChangePasswordViewModel data = response.body();
                    if (data != null && data.getStatus().equals("1")) {
                        if (data.getStatus().equals("1")) {
                            new CustomAlertDialog(context, isActivityOpen).SuccessfulWithFinsh(data.getMessage());
                        } else {
                            new CustomAlertDialog(context, isActivityOpen).Error(data.getMessage());
                        }
                    } else {
                        if (data != null && data.getStatus().equals("0")) {
                            new CustomAlertDialog(context, isActivityOpen).Error(data.getMessage());
                        } else {
                            new CustomAlertDialog(context, isActivityOpen).Error(getString(R.string.data_null_error));
                        }
                    }
                    loader.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<ChangePasswordViewModel> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    call.cancel();
                    loader.dismiss();
                    /*if (t.getMessage().contains("No address associated with hostname")) {
                        new CustomAlertDialog(context,isActivityOpen).Error(getString(R.string.network_not_found));

                    } else {
                        new CustomAlertDialog(context,isActivityOpen).Error(getString(R.string.try_again));
                    }*/

                    if (t.getMessage() != null && !t.getMessage().isEmpty()) {
                        if (t.getMessage().contains("No address associated with hostname")) {
                            new CustomAlertDialog(context, isActivityOpen).Error(getString(R.string.network_not_found));

                        } else {
                            new CustomAlertDialog(context, isActivityOpen).Error(getString(R.string.try_again));
                        }
                    }/*else{
                        new CustomAlertDialog(context,isActivityOpen).Error(getString(R.string.try_again));
                    }*/
                }
            });
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        isActivityOpen = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActivityOpen = false;
    }
}
