package com.solution.catterpillars.ui.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.solution.catterpillars.BR;
import com.solution.catterpillars.BuildConfig;
import com.solution.catterpillars.R;
import com.solution.catterpillars.base.BaseActivity;
import com.solution.catterpillars.databinding.LocationActivityBinding;
import com.solution.catterpillars.ui.home.Home;
import com.solution.catterpillars.ui.lockScreen.model.LockScreenViewModel;

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
public class LocationActivity5 extends BaseActivity<LocationActivityBinding, LockScreenViewModel>
    implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener{

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private final static int File_INTENT_CLICK = 120;
    private static final String TAG = LocationActivity5.class.getSimpleName();
    private LocationManager locationManager;
    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;
    private double latitude = 0.0;
    private double longitude = 0.0;

    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;

    private LockScreenViewModel viewModel;

    public static Intent newIntent(Context context) {
        return new Intent(context, LocationActivity5.class);
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
        viewModel = new LockScreenViewModel();
        binding.content.startRippleAnimation();
       /* binding.centerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.content.startRippleAnimation();
            }
        });*/

        checkLocationPermission();
        //Check if Google Play Services Available or not
        if (!CheckGooglePlayServices()) {
            Log.e("onCreate", "Finishing test case since Google Play Services are not available");
        }
        else {
            Log.e("onCreate","Google Play Services available.");
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        buildGoogleApiClient();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getLastLocation();
        }
    }

    /**
     * @return R.layout.layout_file
     */
    @Override
    protected int getLayout() {
        return R.layout.location_activity;
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
    public LockScreenViewModel getViewModel() {
        return viewModel;
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

    //----------------------location------------------------------------------------
    //---------------------permission------------------------------

    private boolean checkProvider() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        assert locationManager != null;
        this.isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        this.isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return !(!isNetworkEnabled && !isGPSEnabled);
    }

    private boolean onGps() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        assert locationManager != null;
        this.isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        this.isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isNetworkEnabled && !isGPSEnabled) {
            // btnShowLocation.setChecked(false);
            AlertDialog.Builder d = new AlertDialog.Builder(this);
            d.setTitle(getResources().getString(R.string.gps_off));
            d.setMessage(getResources().getString(R.string.gps_setting));
            d.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                }
            });
            d.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface
                    .OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            d.show();
            return false;

        }
        return true;
    }

    //-------------------location-----------------------

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
//        mLocationRequest.setInterval(0);
//        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        }
        Log.e("dfgfdg", "fhhgjhj");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");

        mLastLocation = location;

        //Place current location marker
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        Log.e("Latitude", String.valueOf(latitude));
        Log.e("longitude", String.valueOf(longitude));

        Log.e("onLocationChanged", String.format("latitude:%.3f longitude:%.3f",latitude,
                longitude));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.e("onLocationChanged", "Removing Location Updates");
        }
        Log.e("onLocationChanged", "Exit");



    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //---------------last known location---------------------------
    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();

                            latitude = mLastLocation.getLatitude();
                            longitude = mLastLocation.getLongitude();

                            Log.e("Latitude last", String.valueOf(latitude));
                            Log.e("longitude last", String.valueOf(longitude));
                            //Call location update api
                            openHomeActivity();
                        } else {
                            Log.e(TAG, "getLastLocation:exception", task.getException());
                        }
                    }
                });
    }

    //---------------------permission------------------------------

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);
        if (shouldProvideRationale) {
            Log.e(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            Log.e(TAG, "Requesting permission");
            startLocationPermissionRequest();
        }
    }
    //---------------------------------

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                this.findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    public void checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                Log.e(TAG, "Displaying permission rationale to provide additional context.");
                showSnackbar(R.string.permission_rationale,
                        android.R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Request permission
                                ActivityCompat.requestPermissions(activity,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        });
            }
            else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length <= 0) {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Log.i(TAG, "User interaction was cancelled.");
                }
                else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        //write--------------
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }

                    }

                } else {
                    showSnackbar(R.string.permission_denied_explanation,
                            R.string.setting, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Build intent that displays the App settings screen.
                                    Intent intent = new Intent();
                                    intent.setAction(
                                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package",
                                            BuildConfig.APPLICATION_ID, null);
                                    intent.setData(uri);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });
                }
            }
            break;

            case REQUEST_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length <= 0) {
                    Log.i(TAG, "User interaction was cancelled.");
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation();
                } else {
                    showSnackbar(R.string.permission_denied_explanation, R.string.setting,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Build intent that displays the App settings screen.
                                    Intent intent = new Intent();
                                    intent.setAction(
                                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package",
                                            BuildConfig.APPLICATION_ID, null);
                                    intent.setData(uri);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });
                }
        }
    }

    private void openHomeActivity() {
        Intent intent = Home.newIntent(LocationActivity5.this);
        intent.putExtra("isAppOpen", true);
        startActivity(intent);
        finish();
    }

}
