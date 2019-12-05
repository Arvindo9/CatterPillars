package com.solution.catterpillars.data.remort;

import com.solution.catterpillars.util.ApplicationConstant;

import java.util.ArrayList;
import java.util.Collection;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Arvindo Mondal on 2/26/2018.
 * Email : arvindomondal@gmail.com
 * Designation : Programmer and Developer
 * Skills : Logic and Algorithm Creator and Inventor
 * Logo : Never Give Up
 */

public class APIClient implements GsonResponseListener {

    public static Retrofit getConnect() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        return new Retrofit.Builder()
                .baseUrl(ApplicationConstant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public static Retrofit getConnectUp50() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        return new Retrofit.Builder()
                .baseUrl(ApplicationConstant.BASE_URL_UP_50)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    /**
     * @param type         response object class type
     * @param responseBody parsed response
     */
    @Override
    public void onCacheableResponse(Class type, Object responseBody) {
//        if (responseBody instanceof Collection)
//            BoxManager.getStore().boxFor(type).put((ArrayList) responseBody);
//        else BoxManager.getStore().boxFor(type).put(responseBody);
    }
}
