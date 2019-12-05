package com.solution.catterpillars.ui.splash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.solution.catterpillars.R;
import com.solution.catterpillars.ui.login.Login;
import com.solution.catterpillars.ui.registration.Registration;
import com.solution.catterpillars.util.ApplicationConstant;

public class StartAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_app);

        MobileAds.initialize(this, ApplicationConstant.INSTANCE.ADMOB_APP_ID);

        AdView adView = findViewById(R.id.adView);
        adView.loadAd(new AdRequest.Builder().build());

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Login.newIntent(StartAppActivity.this);
                startActivity(intent);
                //finish();
            }
        });

        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Registration.newIntent(StartAppActivity.this);
                startActivity(intent);
               // finish();
            }
        });
    }
}
