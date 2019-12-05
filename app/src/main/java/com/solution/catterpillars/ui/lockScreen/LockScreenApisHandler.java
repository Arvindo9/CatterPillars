package com.solution.catterpillars.ui.lockScreen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.data.remort.APIClient;
import com.solution.catterpillars.data.remort.APIService;
import com.solution.catterpillars.ui.lockScreen.model.LockScreenViewModel;

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
public class LockScreenApisHandler implements Runnable {

    public static final String TAG = LockScreenApisHandler.class.getSimpleName();
    private Context context;
    private AppPreferencesService preferences;
    private final String[] params;

    public LockScreenApisHandler(@NonNull Context context, String... params){
        this.context = context;
        this.preferences = new AppPreferencesService(context);
        this.params = params;
    }

    @Override
    public void run() {
        APIService apiService = APIClient.getConnectUp50().create(APIService.class);
        Call<LockScreenViewModel> call = null;// = apiService.checkSheetDetails(module, "", "");

        call = apiService.lockScreen(5, 12);

        if (call != null) {
            call.enqueue(new Callback<LockScreenViewModel>() {
                @Override
                public void onResponse(@NonNull Call<LockScreenViewModel> call, @NonNull
                        Response<LockScreenViewModel> response) {
                    LockScreenViewModel data = response.body();
                    if(data != null) {
                        if (data.getStatus().equals("1")) {
                            preferences.setLockScreenData(new Gson().toJson(data));
                            preferences.setLockScreenDataAvailable(true);
                        }

                        Log.e(TAG, data.toString());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LockScreenViewModel> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    call.cancel();
                }
            });
        }
    }
}
