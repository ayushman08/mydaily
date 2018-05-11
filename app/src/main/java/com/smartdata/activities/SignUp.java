package com.smartdata.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.WindowManager;

import com.smartdata.mydaily.R;
import com.smartdata.utils.Utility;

/**
 * Created by Anurag Sethi on 08-04-2015.
 * The activity will handle the SignUp screens actions
 */
public class SignUp extends AppActivity {
    private ActionBar mActionBar;
    private Utility mUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initData();
    }

    public void initData() {
        //mActionBar = getSupportActionBar();
        //mActionBar.setDisplayHomeAsUpEnabled(true);
        mUtility = new Utility(SignUp.this);
        mUtility.customActionBar(SignUp.this, true, R.string.registration);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
