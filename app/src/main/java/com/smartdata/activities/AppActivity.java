package com.smartdata.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.smartdata.helpers.TransparentProgressDialog;

import sdei.support.lib.communication.GetWebServiceData;


/**
 * Created by Anurag Sethi
 * The class will be extended by all the other activities and will contain the code
 * which is common for all the activities
 */
public class AppActivity extends AppCompatActivity {

    TransparentProgressDialog progressDialogObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * Initializes the objects
     *
     * @return none
     */
    public void initData() {

    }

    /**
     * Binds the UI controls
     *
     * @return none
     */
    public void bindControls() {

    }

    /**
     * The method will return the network availability
     *
     * @param context context of the activity from which the method is called
     * @return true if network is available else false
     */
    public boolean isNetworkAvailable(Context context) {
        GetWebServiceData getWebServiceDataObj = new GetWebServiceData(context);
        return getWebServiceDataObj.isNetworkAvailable();
    }
}
