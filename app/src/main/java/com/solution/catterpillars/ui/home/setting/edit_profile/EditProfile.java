package com.solution.catterpillars.ui.home.setting.edit_profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.solution.catterpillars.R;
import com.solution.catterpillars.base.BaseActivity;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.data.remort.APIClient;
import com.solution.catterpillars.data.remort.APIService;
import com.solution.catterpillars.databinding.EditProfileBinding;
import com.solution.catterpillars.ui.home.setting.edit_profile.model.EditProfileViewModel;
import com.solution.catterpillars.util.CustomAlertDialog;
import com.solution.catterpillars.util.CustomLoader;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 12/8/2018
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 */
public class EditProfile extends BaseActivity<EditProfileBinding, EditProfileViewModel> {

    private static final String TAG = EditProfile.class.getSimpleName();
    private AppPreferencesService preferencesService;
    private EditProfileViewModel viewModel;
    private CustomLoader loader;
    private final int GET_DATA = 1;
    private final int SEND_DATA = 2;
    private boolean isActivityOpen;

    public static Intent newIntent(Context context) {
        return new Intent(context, EditProfile.class);
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
        viewModel = new EditProfileViewModel();

        loader = new CustomLoader(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);



        //binding.gender.setEnabled(false);
    }

    /**
     * @return R.layout.layout_file
     */
    @Override
    protected int getLayout() {
        return R.layout.edit_profile;
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
    public EditProfileViewModel getViewModel() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.update:
                api(SEND_DATA);
                break;

            case R.id.gender:

                ProfileDialog flagDialog = new ProfileDialog(context, null);
                flagDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.full_screen_dialog);
                flagDialog.show(getSupportFragmentManager(), TAG);
                break;
        }

    }

    /**
     * Injecting dependencies
     */
    @Override
    protected void injectDependencies() {

    }

    private void settingUi(){
        Gson gson = new Gson();
        viewModel = gson.fromJson(preferencesService.getUserProfileData(),
                EditProfileViewModel.class);

        binding.userName.setText(preferencesService.getUName());
        binding.email.setText(preferencesService.getEmail());
        binding.gender.setText("");
        binding.dob.setText("");
        binding.address.setText("");
        binding.country.setText("");
        binding.state.setText("");
        binding.city.setText("");
        binding.area.setText("");
    }


    void api(int index) {
        loader.show();
        loader.setCancelable(false);
        loader.setCanceledOnTouchOutside(false);

        APIService apiService = APIClient.getConnect().create(APIService.class);
        Call<EditProfileViewModel> call = null;// = apiService.checkSheetDetails(module, "", "");


        call = apiService.setUserProfileData("", "");

        if (call != null) {
            call.enqueue(new retrofit2.Callback<EditProfileViewModel>() {
                @Override
                public void onResponse(@NonNull Call<EditProfileViewModel> call, @NonNull
                        Response<EditProfileViewModel> response) {
                    viewModel = response.body();
                    if (viewModel != null) {

                    }

                    Log.e(TAG, "new Task Loaded");
                }

                @Override
                public void onFailure(@NonNull Call<EditProfileViewModel> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    call.cancel();
                    loader.dismiss();
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
    protected void onDestroy() {
        super.onDestroy();
        isActivityOpen = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActivityOpen = true;
    }
}
