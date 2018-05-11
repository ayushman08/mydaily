package com.smartdata.activities;

import android.app.Application;

import com.smartdata.dataobject.DailiesImage;
import com.smartdata.dataobject.IncidentImages;
import com.smartdata.utils.Constants;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import android.content.Context;

import com.google.android.gms.analytics.HitBuilders;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by Anurag Sethi
 * The class will start once the application will start and will set the Splunk Key for handling
 * Bugsense issues
 */

public class MyApplication extends Application {

    public static GoogleAnalytics analytics;
    public static Tracker tracker;

    private ArrayList<IncidentImages> mIncidentImagesArrayList = new ArrayList<>();
    private ArrayList<DailiesImage> mDailiesImageArrayList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public MyApplication() {
        super();
    }

    /**
     * The method is used to track the times a user visited the mentioned screen
     *
     * @param context    from where the method is called
     * @param screenName name of the screen which needs to be tracked
     */
    public void setAnalyticTracking(Context context, String screenName) {
        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1800);

        tracker = analytics.newTracker(Constants.GoogleAnalytics.trackingID);
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);

        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.AppViewBuilder().build());
        analytics.reportActivityStart((Activity) context);
    }


    // this is used for lower version devices
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //  MultiDex.install(this);
    }

    public void setIncidentImages(ArrayList<IncidentImages> incidentImagesArrayList) {
        this.mIncidentImagesArrayList = incidentImagesArrayList;
    }

    public ArrayList<IncidentImages> getIncidentImages() {
        return mIncidentImagesArrayList;
    }

    public ArrayList<DailiesImage> getmDailiesImageArrayList() {
        return mDailiesImageArrayList;
    }

    public void setmDailiesImageArrayList(ArrayList<DailiesImage> mDailiesImageArrayList) {
        this.mDailiesImageArrayList = mDailiesImageArrayList;
    }
}
