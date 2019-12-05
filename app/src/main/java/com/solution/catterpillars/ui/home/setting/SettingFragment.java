package com.solution.catterpillars.ui.home.setting;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.solution.catterpillars.BR;
import com.solution.catterpillars.R;
import com.solution.catterpillars.base.BaseFragment;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.data.remort.APIClient;
import com.solution.catterpillars.data.remort.APIService;
import com.solution.catterpillars.databinding.SettingFregmentBinding;
import com.solution.catterpillars.ui.home.Home;
import com.solution.catterpillars.ui.home.content.HomeFragment;
import com.solution.catterpillars.ui.home.setting.change_password.ChangePassword;
import com.solution.catterpillars.ui.lockScreen.LockScreen;
import com.solution.catterpillars.util.AppsFunction;
import com.solution.catterpillars.util.CustomAlertDialog;
import com.solution.catterpillars.util.CustomLoader;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 01-Nov-18
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 */
public class SettingFragment extends BaseFragment<SettingFregmentBinding, SettingViewModel>{

    private AppPreferencesService preferencesService;
    public static final String TAG = SettingFragment.class.getSimpleName();
    private SettingViewModel viewModel;
    public SettingFragment settingFragment;
    private Home activityHome;
    CustomLoader loader;
    boolean isFromUser=false;
    private boolean isFragmentOpen=false;
    //boolean isUserCheckedSwitch=false;

    public static SettingFragment newInstance() {
//        activity = taskActivity;
        Bundle args = new Bundle();
        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * @return R.layout.layout_file
     */
    @Override
    public int getLayout() {
        return R.layout.setting_fregment;
    }

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    @Override
    public SettingViewModel getViewModel() {
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
        return R.string.setting;
    }

    /**
     * @return String value null or "" then this title will not show otherwise it will show
     */
    @Override
    protected String setTitleString() {
        return null;
    }

    /**
     * Write rest of the code of fragment in onCreateView after view created
     */
    @Override
    protected void init() {
        loader = new CustomLoader(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        preferencesService = new AppPreferencesService(context);
        if(activity instanceof Home) {
            activityHome = (Home)activity;
        }
        settingFragment = this;

        LockScreen.getInstance().init(getContext());
        if(LockScreen.getInstance().isActive()){
            binding.lockAdsCheck.setChecked(true);
        }else{
            //isUserCheckedSwitch=false;
            binding.lockAdsCheck.setChecked(false);
        }

        binding.lockAdsCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.lockAdsCheck.isChecked()){
                    apiDownLineUpLine("1", "3", new callBackResponse() {
                        @Override
                        public void onSucess() {
                            LockScreen.getInstance().active();

                           // AppsFunction.loadLockScreenAds(getContext(), new AppPreferencesService(getBaseActivity()).getUMobile());
                            binding.lockAdsCheck.setChecked(true);
                        }

                        @Override
                        public void onError() {
                            if(binding.lockAdsCheck.isChecked()) {
                                binding.lockAdsCheck.setChecked(false);
                            }else{
                                binding.lockAdsCheck.setChecked(true);
                            }
                        }
                    });

                }else{
                    //if(isUserCheckedSwitch) {
                    showWarnigAlert();
                    //  isUserCheckedSwitch=true;
                    // }

                }
            }
        });



        binding.downLineCheck.setChecked(preferencesService.getDownlineContact());
        binding.upLineCheck.setChecked(preferencesService.getUplineContact());
        // 2 for downline
        // 1 for Upline

        binding.downLineCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.downLineCheck.isChecked()){
                    apiDownLineUpLine("1", "2", new callBackResponse() {
                        @Override
                        public void onSucess() {
                            preferencesService.setDownlineContact(true);
                            binding.downLineCheck.setChecked(true);
                        }

                        @Override
                        public void onError() {
                            if(binding.downLineCheck.isChecked()) {
                                binding.downLineCheck.setChecked(false);
                            }else{
                                binding.downLineCheck.setChecked(true);
                            }
                        }
                    });
                }else{
                    apiDownLineUpLine("0","2", new callBackResponse() {
                        @Override
                        public void onSucess() {
                            preferencesService.setDownlineContact(false);
                            binding.downLineCheck.setChecked(false);
                        }

                        @Override
                        public void onError() {
                            if(binding.downLineCheck.isChecked()) {
                                binding.downLineCheck.setChecked(false);
                            }else{
                                binding.downLineCheck.setChecked(true);
                            }
                        }
                    });
                }
            }
        });


        binding.upLineCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( binding.upLineCheck.isChecked()){
                    apiDownLineUpLine("1","1", new callBackResponse() {
                        @Override
                        public void onSucess() {
                            binding.upLineCheck.setChecked(true);
                            preferencesService.setUplineContact(true);
                        }

                        @Override
                        public void onError() {
                            if(binding.upLineCheck.isChecked()) {
                                binding.upLineCheck.setChecked(false);
                            }else{
                                binding.upLineCheck.setChecked(true);
                            }
                        }
                    });
                }else{
                    apiDownLineUpLine("0","1", new callBackResponse() {
                        @Override
                        public void onSucess() {
                            preferencesService.setUplineContact(false);
                            binding.upLineCheck.setChecked(false);
                        }

                        @Override
                        public void onError() {
                            if(binding.upLineCheck.isChecked()) {
                                binding.upLineCheck.setChecked(false);
                            }else{
                                binding.upLineCheck.setChecked(true);
                            }
                        }
                    });
                }
            }
        });


//        binding.editProfile.setOnClickListener(this);
//        binding.editProfileE.setOnClickListener(this);
        binding.changePassword.setOnClickListener(this);
        binding.editProfile.setOnClickListener(this);
        binding.bankView.setOnClickListener(this);



        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String version = pInfo.versionName;
            binding.version.setText("Version: "+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        String mobile = preferencesService.getUMobile();
        String password = preferencesService.getPassword();
        switch (v.getId()){
            case R.id.editProfile:
                ((Home) context).fragment = HomeFragment.newInstance("https://www.catterpillars.in/Home/MobileLogin?username="+mobile+"&password="+password+"&type=1");

                ((HomeFragment) ((Home) context).fragment).setFragmentTitle("Edit Profile");
                ((Home) context).openFragment(((Home) context).fragment, HomeFragment.TAG);
               // context.startActivity(EditProfile.newIntent(context));
                break;

            case R.id.changePassword:
                context.startActivity(ChangePassword.newIntent(context));
                break;


            case R.id.bankView:
                ((Home) context).fragment = HomeFragment.newInstance("https://www.catterpillars.in/Home/MobileLogin?username="+mobile+"&password="+password+"&type=2");
                ((HomeFragment) ((Home)
                        context).fragment).setFragmentTitle("Change Bank Detail");

                ((Home) context).openFragment(((Home) context).fragment, HomeFragment.TAG);
               // context.startActivity(EditProfile.newIntent(context));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(LockScreen.getInstance().isActive()){
            binding.lockAdsCheck.setChecked(true);
        }else{
            binding.lockAdsCheck.setChecked(false);
            LockScreen.getInstance().deactivate();
        }
    }

    void showWarnigAlert() {
        new CustomAlertDialog(context,isFragmentOpen).EnableLockScreenWithCallBack(new CustomAlertDialog.DialogCallBack() {
            @Override
            public void onPositiveClick() {

                apiDownLineUpLine("1", "3", new callBackResponse() {
                    @Override
                    public void onSucess() {
                        LockScreen.getInstance().active();

                        //AppsFunction.loadLockScreenAds(getContext(), new AppPreferencesService(getBaseActivity()).getUMobile());
                        binding.lockAdsCheck.setChecked(true);
                    }

                    @Override
                    public void onError() {
                        if(binding.lockAdsCheck.isChecked()) {
                            binding.lockAdsCheck.setChecked(false);
                        }else{
                            binding.lockAdsCheck.setChecked(true);
                        }
                    }
                });
            }

            @Override
            public void onNegativeClick() {
                apiDownLineUpLine("0","2", new callBackResponse() {
                    @Override
                    public void onSucess() {
                        LockScreen.getInstance().deactivate();
                        binding.lockAdsCheck.setChecked(false);
                    }

                    @Override
                    public void onError() {
                        if(binding.lockAdsCheck.isChecked()) {
                            binding.lockAdsCheck.setChecked(false);
                        }else{
                            binding.lockAdsCheck.setChecked(true);
                        }
                    }
                });

            }
        });
    }



    void apiDownLineUpLine(String status, String type,callBackResponse mCallBackResponse){
        loader.show();
        loader.setCancelable(false);
        loader.setCanceledOnTouchOutside(false);

        APIService apiService = APIClient.getConnect().create(APIService.class);
        Call<SettingViewModel> call = null;// = apiService.checkSheetDetails(module, "", "");

      
            call = apiService.updateStatus(preferencesService.getUMobile(), type, status);
       

        if (call != null) {
            call.enqueue(new retrofit2.Callback<SettingViewModel>() {
                @Override
                public void onResponse(@NonNull Call<SettingViewModel> call, @NonNull
                        Response<SettingViewModel> response) {
                    viewModel = response.body();
                    if (viewModel != null && viewModel.getStatus().equalsIgnoreCase("1")) {
                        //list.clear();
                        mCallBackResponse.onSucess();
                    } else {
                        mCallBackResponse.onError();

                    }

                    loader.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<SettingViewModel> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    call.cancel();
                    mCallBackResponse.onError();
                    loader.dismiss();
                    /*if (t.getMessage().contains("No address associated with hostname")) {
                        new CustomAlertDialog(context,isFragmentOpen).Error(getString(R.string.network_not_found));

                    } else {
                        new CustomAlertDialog(context,isFragmentOpen).Error(getString(R.string.try_again));
                    }*/

                    if(t.getMessage()!=null && !t.getMessage().isEmpty()) {
                        if (t.getMessage().contains("No address associated with hostname")) {
                            new CustomAlertDialog(context,isFragmentOpen).Error(getString(R.string.network_not_found));

                        } else {
                            new CustomAlertDialog(context,isFragmentOpen).Error(getString(R.string.try_again));
                        }
                    }/*else{
                        new CustomAlertDialog(context,isFragmentOpen).Error(getString(R.string.try_again));
                    }*/
                }
            });
        }
    }


    private interface callBackResponse{
        void onSucess();
        void onError();
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
    }
}
