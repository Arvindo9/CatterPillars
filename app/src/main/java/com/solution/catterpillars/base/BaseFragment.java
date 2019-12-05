package com.solution.catterpillars.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.solution.catterpillars.R;

/**
 * Created by Arvindo Mondal on 29-Oct-18.
 * Roundpay Techno Media OPC Pvt Ltd
 * I am a mathematician
 * arvindomondal@gmail.com
 * Only intelligence can make anything possible
 * Never give up
 */
public abstract class BaseFragment<B extends ViewDataBinding, V extends BaseViewModel>
        extends Fragment
        implements View.OnClickListener{

    protected BaseActivity activity;
    protected Context context;
    private View view;
    public B binding;
    protected V viewModel;

    public interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.activity = activity;
            this.context = context;
            activity.onFragmentAttached();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
//        performDependencyInjection();
        super.onCreate(savedInstanceState);
        viewModel = getViewModel();
        setHasOptionsMenu(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        view = binding.getRoot();
        init();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setVariable(getBindingVariable(), viewModel);
        binding.executePendingBindings();

        if(setTitle() != 0) {
            activity.setTitle(setTitle());
        }
        if(setTitleString() != null && !setTitleString().equals("")){
            activity.setTitle(setTitleString());
        }
    }

    /**
     *
     * @return R.layout.layout_file
     */
    @LayoutRes
    public abstract int getLayout();

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    public abstract V getViewModel();

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    public abstract int getBindingVariable();

    /**
     *
     * @return R.strings.text
     */
    public abstract int setTitle();

    /**
     *
     * @return String value null or "" then this title will not show otherwise it will show
     */
    protected abstract String setTitleString();

//    /**
//     *
//     * @return R.strings.text
//     */
//    public abstract int setSubTitle();

    @Override
    public void onDetach() {
        activity = null;
        super.onDetach();

        System.gc();
    }

    /**
     * Write rest of the code of fragment in onCreateView after view created
     */
    protected abstract void init();

    public BaseActivity getBaseActivity() {
        return activity;
    }

    public B getDataBinding() {
        return binding;
    }

    @Override
    public abstract void onClick(View v);

    private void performDependencyInjection() {
    }

    public void hideKeyboard() {
        if (activity != null) {
            activity.hideKeyboard();
        }
    }

    public boolean isNetworkConnected() {
        return activity != null && activity.isNetworkAvailable();
    }


    public void setAds(final int height, final int margin, final ViewGroup hideView, boolean isBig) {

        if (hideView != null) {
            hideView.setVisibility(View.GONE);
        }
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float densityDpi = metrics.density;
        int width = metrics.widthPixels;
        AdView adView = new AdView(context);
        if (isBig) {
            adView.setAdSize(new AdSize((int) ((width - margin) / densityDpi), height));
        } else {
            adView.setAdSize(AdSize.LARGE_BANNER);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            adView.setForegroundGravity(Gravity.CENTER);
        }
        adView.setAdUnitId(context.getResources().getString(R.string.adsBannerSplash));
        adView.loadAd(new AdRequest.Builder().build());
        //Log.e("AdsError", context.getResources().getString(R.string.adsBannerSplash));
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                if (hideView != null) {
                    hideView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                if (hideView != null) {
                    hideView.setVisibility(View.GONE);
                }

                if (errorCode == 3) {
                    setAds(height, margin, hideView, false);
                }
                Log.e("AdsError", errorCode + "");
            }
        });
        hideView.addView(adView);
    }

    public static boolean isNetworkAvaliable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                || (connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState() == NetworkInfo.State.CONNECTED)) {
            return true;
        } else {
            return false;
        }
    }
}
