package com.solution.catterpillars.facebook;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONObject;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 12/12/2018
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 */
public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    public static void like(AccessToken accessToken){
        /* make the API call */
        new GraphRequest(
                accessToken,
                "roundpayvoicetech/photos/likes",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        /* handle the result */
                        Log.e(TAG, response.toString());
                    }
                }
        ).executeAsync();
    }

    public static void fd(AccessToken accessToken){
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Insert your code here
                        Log.e(TAG, object.toString());
                    }
                });

        Bundle parameters = new Bundle();
//        parameters.putString("fields", "id,name");
        parameters.putString("fields", "likes");
        request.setParameters(parameters);
        request.executeAsync();


        Log.e(TAG, accessToken.getToken());

    }



}
