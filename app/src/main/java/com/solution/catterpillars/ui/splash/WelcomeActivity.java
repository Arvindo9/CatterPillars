package com.solution.catterpillars.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.solution.catterpillars.BR;
import com.solution.catterpillars.R;
import com.solution.catterpillars.base.BaseActivity;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.data.remort.APIClient;
import com.solution.catterpillars.data.remort.APIService;
import com.solution.catterpillars.databinding.WelcomeActivityBinding;
import com.solution.catterpillars.ui.home.Home;
import com.solution.catterpillars.ui.login.Login;
import com.solution.catterpillars.ui.login.model.ResultLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 01-Nov-18
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 * About : I am a mathematician
 * Quote : Only brain can make anything possible
 * Strength : Never give up
 */
public class WelcomeActivity extends BaseActivity<WelcomeActivityBinding, WelcomeViewModel> implements
        WelcomeNavigator {

    public static final int REQUEST_READ_PHONE_STATE = 10;
    private static final int NUM_PAGES = 3;
    private static final String TAG = WelcomeActivity.class.getSimpleName();
    private WelcomeViewModel splashViewModel;
    private int count = 0;
    private TextView[] mdots;
    private WelcomeAdapter myadapter;
  //  private int mCureentPage = 0;
    //    private Timer timer;
    private final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    private final long PERIOD_MS = 1500; // time in milliseconds between successive task executions.
    private AppPreferencesService preferencesService;
    private Handler handler = new Handler();
    private Runnable Update;


    public static Intent newIntent(Context context) {
        return new Intent(context, WelcomeActivity.class);
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
        REQUEST_PERMISSION_FOR_ACTIVITY = false;

        splashViewModel = new WelcomeViewModel();
        splashViewModel.setNavigator(this);

        preferencesService = new AppPreferencesService(context);
//        if(!preferencesService.getLauncher()){
//            launcherScreen();
//        }
//        else{
//
//        }
        apisFlag();
        launcherScreen();

    }

    private void launcherScreen() {
        myadapter = new WelcomeAdapter(this);
        binding.viewpager.setAdapter(myadapter);
        adddots(0);

        binding.viewpager.addOnPageChangeListener(viewlistener);

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.nextBtn.getText().equals(getText(R.string.start))) {

                    openStartAppActivity();


                } else {
                    binding.viewpager.setCurrentItem(binding.viewpager.getCurrentItem() + 1);
                }
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openStartAppActivity();
                // binding.viewpager.setCurrentItem(mCureentPage-1);
            }
        });

      //  handlingBackground();
    }

    private void handlingBackground() {
        /*After setting the adapter use the timer */
        handler.postDelayed(Update = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(Update, PERIOD_MS);
                if (binding.viewpager.getCurrentItem() == NUM_PAGES-1) {
                    binding.viewpager.setCurrentItem(0);
                    openStartAppActivity();
                } else {
                    try {
                        binding.viewpager.setCurrentItem(binding.viewpager.getCurrentItem()+1, true);
                    } catch (NullPointerException ignore) {
                    }

                }

                Log.e(TAG, "Background UI Task");
            }
        }, PERIOD_MS);

    }

    @Override
    protected void onDestroy() {
//        timer.cancel();
        if(handler!=null && Update!=null) {
            handler.removeCallbacks(Update);
        }
        super.onDestroy();
    }

    /**
     * @return R.layout.layout_file
     */
    @Override
    protected int getLayout() {
        return R.layout.welcome_activity;
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

    }

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    @Override
    public WelcomeViewModel getViewModel() {
        return splashViewModel;
    }

    @Override
    public void onClick(View v) {
    }

    /**
     * Injecting dependencies
     */
    @Override
    protected void injectDependencies() {

    }

    @Override
    public void openStartAppActivity() {
        if(handler!=null && Update!=null) {
            handler.removeCallbacks(Update);
        }
        preferencesService.setLauncher(true);
        Intent intent = new Intent(WelcomeActivity.this, StartAppActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

//        Intent intent = Home.newIntent(WelcomeActivity.this);
//        intent.putExtra("UserId", userId);
//        intent.putExtra("Password",password);
        // intent.putExtra("isAppOpen", true);
//        startActivity(intent);
//        finish();
    }

    @Override
    public void openHomeActivity(String userId, String password, boolean taskStatus) {
        handler.removeCallbacks(Update);
        Intent intent = Home.newIntent(WelcomeActivity.this);
        intent.putExtra("UserId", userId);
        intent.putExtra("Password", password);
        intent.putExtra("TaskStatus", taskStatus);
        intent.putExtra("isAppOpen", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void adddots(int i) {

        mdots = new TextView[NUM_PAGES];
        binding.dots.removeAllViews();

        for (int x = 0; x < mdots.length; x++) {

            mdots[x] = new TextView(this);
            mdots[x].setText(Html.fromHtml("&#8226;"));
            mdots[x].setTextSize(35);
            mdots[x].setTextColor(getResources().getColor(R.color.gray));

            binding.dots.addView(mdots[x]);
        }
        if (mdots.length > 0) {

            mdots[i].setTextColor(getResources().getColor(R.color.white));

        }

    }

    ViewPager.OnPageChangeListener viewlistener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            adddots(position);
            //mCureentPage = position;
            if (position == mdots.length - 1) {
                binding.nextBtn.setText(getText(R.string.start));
                binding.backBtn.setText("");
                binding.backBtn.setClickable(false);
                // binding.backBtn.setVisibility(View.GONE);
            } else {
                binding.nextBtn.setText(getText(R.string.next));
                binding.backBtn.setText(getText(R.string.skip));
                binding.backBtn.setClickable(true);
                // binding.backBtn.setVisibility(View.VISIBLE);
            }

           /* if (position==0){
                binding.nextBtn.setEnabled(true);
                binding.backBtn.setEnabled(false);
                binding.backBtn.setVisibility(View.INVISIBLE);

                binding.nextBtn.setText(getText(R.string.next));
                binding.backBtn.setText("");
            }
            else if(position==mdots.length-1){

                binding.nextBtn.setEnabled(true);
                binding.backBtn.setEnabled(true);
                binding.backBtn.setVisibility(View.VISIBLE);

                binding.nextBtn.setText(getText(R.string.finish));
                binding.backBtn.setText(getText(R.string.skip));

            }
            else {
                binding.nextBtn.setEnabled(true);
                binding.backBtn.setEnabled(true );
                binding.backBtn.setVisibility(View.VISIBLE);

                binding.nextBtn.setText(getText(R.string.next));
                binding.backBtn.setText(getText(R.string.skip));
            }*/
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    private void apisFlag() {

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
                        if (data.getStatus().equals("1")) {
                            preferencesService.setFlagData(new Gson().toJson(data));
                        }
                    }

                }

                @Override
                public void onFailure(@NonNull Call<ResultLogin> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    call.cancel();

                }
            });
        }
    }
}
