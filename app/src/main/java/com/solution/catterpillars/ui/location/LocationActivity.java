package com.solution.catterpillars.ui.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.solution.catterpillars.BR;
import com.solution.catterpillars.R;
import com.solution.catterpillars.base.BaseActivity;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.data.remort.APIClient;
import com.solution.catterpillars.data.remort.APIService;
import com.solution.catterpillars.databinding.LocationActivityBinding;
import com.solution.catterpillars.ui.home.Home;
import com.solution.catterpillars.ui.location.model.LocationViewModel;
import com.solution.catterpillars.util.CustomLoader;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.solution.catterpillars.util.ApplicationConstant.REQUEST_CHECK_SETTINGS;

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
public class LocationActivity extends BaseActivity<LocationActivityBinding, LocationViewModel>
        implements LocationNavigator {

    private static final String TAG = LocationActivity.class.getSimpleName();
    private LocationViewModel viewModel;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationManager locationManager;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private double latitudeLast = 0.0;
    private double longitudeLast = 0.0;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;
    private boolean ENABLE_LOCATION_REQUEST = false;
    private static boolean IS_CAPTURED_CURRENT_LOCATION = false;
    private ResolvableApiException resolvable;
    private LocationNavigator locationNavigator;
    private AppPreferencesService preferencesService;
    private String mobile;

    public static Intent newIntent(Context context) {
        return new Intent(context, LocationActivity.class);
    }

    @Override
    protected void initialization(@Nullable Bundle state) {

    }

    @Override
    protected void init() {
        viewModel = new LocationViewModel();
        binding.content.startRippleAnimation();
        locationNavigator = this;

        preferencesService = new AppPreferencesService(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        createLocationRequest();

        currentUpdatedLocationListner();

        mobile = preferencesService.getUMobile();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        createLocationRequest();
    }

    @Override
    protected void onStart() {
        super.onStart();
        createLocationRequest();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    protected int getLayout() {
        return R.layout.location_activity;
    }

    @Override
    public int getBindingVariable() {
        return BR.data;
    }

    @Override
    protected void setTitle() {

    }

    @Override
    public LocationViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void injectDependencies() {

    }

    //----------------Permission---------------

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onPermissionGranted() {
        startLocationUpdates();
    }

    //    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        if (checkPermissions()) {
            if (ENABLE_LOCATION_REQUEST) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                        mLocationCallback,
                        null /* Looper */);

                getLastLocation();
            }
            else {
                createLocationRequest();
            }
        }
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    //---------------Checking for GPS-----------------------------

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        builder.setNeedBle(true);

        Task<LocationSettingsResponse> result =
                LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...

                Log.e(TAG, "onSuccess");
                ENABLE_LOCATION_REQUEST = true;
                startLocationUpdates();

            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        Log.e(TAG, "onFailure");
                        resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(activity,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    //---------------Currently updated location--------------------

    private void currentUpdatedLocationListner(){
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...

                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    locationNavigator.onUpdateCurrentLocation();

                    stopLocationUpdates();
                }
            };
        };
    }

    //---------------Last known location---------------------------

    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Location mLastLocation = task.getResult();

                            latitudeLast = mLastLocation.getLatitude();
                            longitudeLast = mLastLocation.getLongitude();

                            Log.e(TAG + " Latitude last", "onComplete:" + String.valueOf
                                    (latitudeLast));
                            Log.e(TAG + " longitude last", "onComplete:" + String.valueOf
                                    (longitudeLast));

                            locationNavigator.onUpdateLastLocation();

                        } else {
                            Log.e(TAG, " getLastLocation:exception", task.getException());
                        }
                    }
                });

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object

                            latitudeLast = location.getLatitude();
                            longitudeLast = location.getLongitude();

                            locationNavigator.onUpdateLastLocation();

                            Log.e(TAG + " Latitude last", "onSuccess:" + String.valueOf
                                    (latitudeLast));
                            Log.e(TAG + " longitude last", "onSuccess:" + String.valueOf
                                    (longitudeLast));
                        }
                    }
                });
    }

    //----------------On GPS device on-----------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made

                        ENABLE_LOCATION_REQUEST = true;
                        startLocationUpdates();

                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        showSnackbarOnCancel();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    private void showSnackbarOnCancel(){
        if(checkPermissions()) {
            Snackbar mSnackBar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.enable_gps),
                    Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.enable),
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                Log.e(TAG, " showSnackbarOnCancel");
                                resolvable.startResolutionForResult(activity,
                                        REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sendEx) {
                                // Ignore the error.
                            }
                        }
                    });
            mSnackBar.setActionTextColor(getResources().getColor(R.color.snackBarActionColor));
            TextView mainTextView = (TextView) (mSnackBar.getView()).
                    findViewById(android.support.design.R.id.snackbar_text);
            mainTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.snackbar_textsize));
            mSnackBar.show();
        }
    }

    //----------------When location catch--------------------------

    @Override
    public void onUpdateLastLocation() {
        Log.e(TAG + " Latitude", "last:" + String.valueOf(latitudeLast));
        Log.e(TAG + " longitude", "last:" + String.valueOf(longitudeLast));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if(!IS_CAPTURED_CURRENT_LOCATION){
                    latitude = latitudeLast;
                    longitude = longitudeLast;
                    apis();
                }
            }
        }, 5000);
    }

    @Override
    public void onUpdateCurrentLocation() {
        Log.e(TAG + " Latitude", "listener:" + String.valueOf(latitude));
        Log.e(TAG + " longitude", "listener:" + String.valueOf
                (longitude));

        IS_CAPTURED_CURRENT_LOCATION = true;
        apis();
    }

    private void openHomeActivity(){
        preferencesService.setLocationCapture(true);
        Intent intent = Home.newIntent(LocationActivity.this);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("isAppOpen", true);
        startActivity(intent);
        finish();
    }


    private void apis() {
        APIService apiService = APIClient.getConnect().create(APIService.class);
        Call<LocationViewModel> call = null;// = apiService.checkSheetDetails(module, "", "");

        call = apiService.locationCapture(mobile, latitude, longitude);

        if (call != null) {
            call.enqueue(new Callback<LocationViewModel>() {
                @Override
                public void onResponse(@NonNull Call<LocationViewModel> call, @NonNull
                        Response<LocationViewModel> response) {
                    LocationViewModel data = response.body();
                    if (data != null) {
                        if (data.getStatus().equals("1")) {
                            openHomeActivity();
                        }
                        else{
                            showToast(R.string.try_again);
                        }
                    }else{
                        showToast(R.string.try_again);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LocationViewModel> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    call.cancel();
                    showToast(R.string.network_error);
                }
            });
        }

        //tmp remove it
//        openHomeActivity();
    }
}
