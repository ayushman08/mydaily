package com.smartdata.dataobject;

import com.smartdata.utils.Constants;

import debug.LogUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anurag Sethi
 * The class is defined on single instance.
 */
public class AppInstance {

    private static AppInstance appInstance = null;
    public static LogUtility logObj;
    public static User userObj;
    public static ForemanData foremanData;
    public static CrewsData crewsData;
    public static JobData jobData;
    public static ProductData productData;
    public static SupervisorAddJobData supervisorAddJobData;
    public static ForemanJobData foremanJobData;
    public static Media mediaObj;
    public static SupervisorJobDetails supervisorJobDetails;
    public static MediaGallery mediaGalleryObj;
    public static MyDailyData myDailyData;
    public static IncidentData incidentData;
    public static MyDailyResponse myDailyResponse;
    public static ResponseData responseData;
    public static SupervisorData supervisorData;
    public static List<SwipeRefresh> swipeRefreshListObj;
    public static FeedbackData feedbackData;

    /**
     * To initialize the appInstance Object
     *
     * @return singleton instance
     */

    public static AppInstance getAppInstance() {
        if (appInstance == null) {
            appInstance = new AppInstance();

            /**
             * The object will manage the User information
             */
            userObj = new User();

            /**
             * The object will manage the Media information
             */
            mediaObj = new Media();

            /**
             * The object will manage the media gallery information
             */
            mediaGalleryObj = new MediaGallery();

            /**
             * The object will manage the swipeToRefresh arrayList
             */
            swipeRefreshListObj = new ArrayList<SwipeRefresh>();

            /**
             * the object will manage the logs in the logcat
             */
            logObj = new LogUtility(Constants.DebugLog.APP_MODE, Constants.DebugLog.APP_TAG);

            /**
             * the object will manage the foreman list
             */
            foremanData = new ForemanData();

            /**
             * the object will manage the job list
             */
            jobData = new JobData();

            /**
             * the object will manage the crews list
             */
            crewsData = new CrewsData();

            /**
             * the object will manage the product data list
             */
            productData = new ProductData();

            /**
             * the object will manage the add new job
             */
            supervisorAddJobData = new SupervisorAddJobData();

            /**
             * the object will manage to get foreman job list
             */
            foremanJobData = new ForemanJobData();

            /**
             * the object will manage to get supervisor job details
             */
            supervisorJobDetails = new SupervisorJobDetails();

            /**
             * the object will manage to get foreman daily list
             */
            myDailyData = new MyDailyData();

            /**
             * the object will manage to get incident list
             */
            incidentData = new IncidentData();

            /**
             * the object will manage to get response from new daily submit
             */
            myDailyResponse = new MyDailyResponse();

            /**
             * the object will manage to get response from new daily submit
             */
            responseData = new ResponseData();

            /**
             * the object will manage to supervisor list data
             */
            supervisorData = new SupervisorData();

            /**
             * the object will manage to send feedback
             */
            feedbackData = new FeedbackData();

        }

        return appInstance;
    }

}
