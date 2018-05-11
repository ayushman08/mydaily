package com.smartdata.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.crashlytics.android.Crashlytics;
import com.smartdata.config.PreferenceConfiguration;
import com.smartdata.dataobject.AppInstance;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;

import io.fabric.sdk.android.Fabric;


/**
 * Created by Anurag Sethi
 * The activity is used for handling the splash screen operations along with redirection to login screen
 * after the splash screen delay is exhausted
 */
public class SplashActivity extends AppActivity {
    private Intent intentObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics());
        getSupportActionBar().hide();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);


        //initializing the data
        initData();

        //handler to close the splash activity after the set time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (PreferenceConfiguration.getAuthToken(SplashActivity.this).length() > 0) {
                    //Intent to call the Activity
                    if (PreferenceConfiguration.isRememberUserChecked(SplashActivity.this)) {
                        intentObj = new Intent(SplashActivity.this, DashboardActivity.class);
                    } else {
                        intentObj = new Intent(SplashActivity.this, MainActivity.class);
                    }
                } else {
                    intentObj = new Intent(SplashActivity.this, MainActivity.class);

                }
                startActivity(intentObj);
                SplashActivity.this.finish();
                SplashActivity.this.overridePendingTransition(R.anim.splashfadein, R.anim.splashfadeout);
            }
        }, Constants.SplashScreen.SPLASH_DELAY_LENGTH);
    }


    /**
     * The method is used to initialize the objects
     *
     * @return none
     */
    public void initData() {

        AppInstance.getAppInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
