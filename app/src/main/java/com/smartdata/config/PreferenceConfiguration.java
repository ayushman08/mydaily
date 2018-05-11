package com.smartdata.config;


import android.content.Context;
import android.content.SharedPreferences;

import com.smartdata.mydaily.R;

/**
 * A configuration class that holds the preference related constants and keys.
 *
 * @author smartdata enterprises
 */
public class PreferenceConfiguration {

    /**
     * An interface to hold the constants used for preference data storing.
     */
    public interface PreferenceConstants {
        // Login related keys
        String USER_TYPE_ID = "user_type";
        String USER_EMAIL = "email";
        String USER_FIRST_NAME = "first_name";
        String USER_LAST_NAME = "last_name";
        String USER_ADDRESS = "address_1";
        String USER_DOB = "dob";
        String AUTH_TOKEN = "_token";
        String REMEMBER_USER = "rememberUser";
        String LOCATION_PREFERENCE = "locationPreference";


        // GCM related keys
        String GCM_ALERT_TITLE = "GCM_ALERT_TITLE";
        String GCM_PRODUCT_TYPE = "GCM_PRODUCT_TYPE";
        String GCM_PRODUCT_ID = "GCM_PRODUCT_ID";
        String IS_FROM_GCM = "IS_FROM_GCM";

        // User credentials
        String USER_PUSH_TOKEN = "USER_PUSH_TOKEN";
        // User flag
        String USER_FLAG = "user_flag";

        String USER_ID = "userId";
        String DEVICE_TOKEN = "deviceToken";
        String PARENT_ID = "parent_id";
        //copy previous job section
        String JOB_DESCRIPTION = "job_description";
        String JOB_LOCATION = "job_location";
        String JOB_ASSIGN_TO_FOREMAN = "job_assign_to_foreman";
        String JOB_PRODUCTION_ITEM1 = "job_production_item1";
        String JOB_PRODUCTION_ITEM2 = "job_production_item2";
        String JOB_PRODUCTION_ITEM3 = "job_production_item3";
        String JOB_PRODUCTION_ID1 = "job_production_id1";
        String JOB_PRODUCTION_ID2 = "job_production_id2";
        String JOB_PRODUCTION_ID3 = "job_production_id3";
        String JOB_LATITUDE = "latitude";
        String JOB_LONGITUDE = "lonitude";

    }

    /**
     * Call this method to get the singleton instance of {@link PreferenceConfiguration} class object.
     */
    public static SharedPreferences getInstance(Context context) {
        return context.getSharedPreferences(
                context.getResources().getString(R.string.app_name), 0);
    }


    /**
     * Call to set the Auth token.
     */
    public static void setAuthToken(String authToken, Context context) {
        getInstance(context).edit().putString(PreferenceConstants.AUTH_TOKEN, authToken).commit();
    }

    /**
     * Call to get the Auth token.
     */
    public static String getAuthToken(Context context) {
        return getInstance(context).getString(PreferenceConstants.AUTH_TOKEN, "");
    }

    /**
     * Call to set the location preference dialog.
     */
    public static void setLocationPreference(boolean isDialogDismiss, Context context) {
        getInstance(context).edit().putBoolean(PreferenceConstants.LOCATION_PREFERENCE, isDialogDismiss).commit();
    }

    /**
     * Call to get the location preference dialog.
     */
    public static boolean isLocationPreference(Context context) {
        return getInstance(context).getBoolean(PreferenceConstants.LOCATION_PREFERENCE, false);
    }

    /**
     * Call to set the remember user.
     */
    public static void setRememberUser(boolean isRememberUserChecked, Context context) {
        getInstance(context).edit().putBoolean(PreferenceConstants.REMEMBER_USER, isRememberUserChecked).commit();
    }

    /**
     * Call to get the remember user.
     */
    public static boolean isRememberUserChecked(Context context) {
        return getInstance(context).getBoolean(PreferenceConstants.REMEMBER_USER, false);
    }

    /**
     * Call to get the DOB.
     */
    public static String getUserDOB(Context context) {
        return getInstance(context).getString(PreferenceConstants.USER_DOB, "");
    }

    /**
     * Call to set the DOB.
     */
    public static void setUserDOB(String userDOB, Context context) {
        getInstance(context).edit().putString(PreferenceConstants.USER_DOB, userDOB).commit();
    }

    /**
     * Call to get the user_last_name.
     */
    public static String getUserLastName(Context context) {
        return getInstance(context).getString(PreferenceConstants.USER_LAST_NAME, "");
    }

    /**
     * Call to set the user_last_name.
     */
    public static void setUserLastName(String user_last_name, Context context) {
        getInstance(context).edit().putString(PreferenceConstants.USER_LAST_NAME, user_last_name).commit();
    }

    /**
     * Call to get the user_first_name.
     */
    public static String getUserFirstName(Context context) {
        return getInstance(context).getString(PreferenceConstants.USER_FIRST_NAME, "");
    }

    /**
     * Call to set the user_first_name.
     */
    public static void setUserFirstName(String user_first_name, Context context) {
        getInstance(context).edit().putString(PreferenceConstants.USER_FIRST_NAME, user_first_name).commit();
    }

    /**
     * Call to get the user email.
     */
    public static String getUserEmail(Context context) {
        return getInstance(context).getString(PreferenceConstants.USER_EMAIL, "");
    }

    /**
     * Call to set the user email.
     */
    public static void setUserEmail(String user_email, Context context) {
        getInstance(context).edit().putString(PreferenceConstants.USER_EMAIL, user_email).commit();
    }

    /**
     * Call to get the user type id.
     */
    public static int getUserTypeId(Context context) {
        return getInstance(context).getInt(PreferenceConstants.USER_TYPE_ID, 1);
    }

    /**
     * Call to set the user type id.
     */
    public static void setUserTypeId(int user_code, Context context) {
        getInstance(context).edit().putInt(PreferenceConstants.USER_TYPE_ID, user_code).commit();
    }

    /**
     * Call to get the user Address.
     */
    public static String getUserAddress(Context context) {
        return getInstance(context).getString(PreferenceConstants.USER_ADDRESS, "");
    }

    /**
     * Call to set the user Address.
     */
    public static void setUserAddress(String userAddress, Context context) {
        getInstance(context).edit().putString(PreferenceConstants.USER_ADDRESS, userAddress).commit();
    }

    /**
     * Method to get the Push Token sent by push server.
     */
    public static String getPushToken(Context context) {
        return getInstance(context).getString(PreferenceConstants.USER_PUSH_TOKEN, "");
    }

    /**
     * Method to set the Push Token got from push server.
     */
    public static void setPushToken(String loginToken, Context context) {
        getInstance(context).edit().putString(PreferenceConstants.USER_PUSH_TOKEN, loginToken).commit();
    }

    /**
     * Call to get the user flag.
     */
    public static String getUserFlag(Context context) {
        return getInstance(context).getString(PreferenceConstants.USER_FLAG, "");
    }

    /**
     * Method to set the Push Token got from push server.
     */
    public static void setUserFlag(String loginToken, Context context) {
        getInstance(context).edit().putString(PreferenceConstants.USER_FLAG, loginToken).commit();
    }


    /**
     * Call to set the device token.
     */
    public static void setDeviceToken(String deviceToken, Context context) {
        getInstance(context).edit().putString(PreferenceConstants.DEVICE_TOKEN, deviceToken).commit();
    }

    /**
     * Call to get the device token.
     */
    public static String getDeviceToken(Context context) {
        return getInstance(context).getString(PreferenceConstants.DEVICE_TOKEN, "");
    }


    /**
     * Call to set the userID.
     */
    public static void setUserID(String userID, Context context) {
        getInstance(context).edit().putString(PreferenceConstants.USER_ID, userID).commit();
    }

    /**
     * Call to get the userID.
     */
    public static String getUserID(Context context) {
        return getInstance(context).getString(PreferenceConstants.USER_ID, "");
    }


    /**
     * Call to set the parentID.
     */
    public static void setParentID(String parentID, Context context) {
        getInstance(context).edit().putString(PreferenceConstants.PARENT_ID, parentID).commit();
    }

    /**
     * Call to get the parentID.
     */
    public static String getParentID(Context context) {
        return getInstance(context).getString(PreferenceConstants.PARENT_ID, "");
    }


    /**
     * copy previous job section
     * Call to set the job description.
     */
    public static void setJobDescription(String parentID, Context context) {
        getInstance(context).edit().putString(PreferenceConstants.JOB_DESCRIPTION, parentID).commit();
    }

    /**
     * Call to get the job description.
     */
    public static String getJobDescription(Context context) {
        return getInstance(context).getString(PreferenceConstants.JOB_DESCRIPTION, "");
    }

    /**
     * Call to set the job location.
     */
    public static void setJobLocation(String parentID, Context context) {
        getInstance(context).edit().putString(PreferenceConstants.JOB_LOCATION, parentID).commit();
    }

    /**
     * Call to get the job location.
     */
    public static String getJobLocation(Context context) {
        return getInstance(context).getString(PreferenceConstants.JOB_LOCATION, "");
    }


    /**
     * Call to set the job assign to foreman.
     */
    public static void setJobAssignToForeman(String parentID, Context context) {
        getInstance(context).edit().putString(PreferenceConstants.JOB_ASSIGN_TO_FOREMAN, parentID).commit();
    }

    /**
     * Call to get the job assign to foreman.
     */
    public static String getJobAssignToForeman(Context context) {
        return getInstance(context).getString(PreferenceConstants.JOB_ASSIGN_TO_FOREMAN, "");
    }


    /**
     * Call to set the job production item1.
     */
    public static void setJobProductionItem1(String parentID, Context context) {
        getInstance(context).edit().putString(PreferenceConstants.JOB_PRODUCTION_ITEM1, parentID).commit();
    }

    /**
     * Call to get the job production item1.
     */
    public static String getJobProductionItem1(Context context) {
        return getInstance(context).getString(PreferenceConstants.JOB_PRODUCTION_ITEM1, "");
    }


    /**
     * Call to set the job production id1.
     */
    public static void setJobProductionID1(String parentID, Context context) {
        getInstance(context).edit().putString(PreferenceConstants.JOB_PRODUCTION_ID1, parentID).commit();
    }

    /**
     * Call to get the job production id1.
     */
    public static String getJobProductionID1(Context context) {
        return getInstance(context).getString(PreferenceConstants.JOB_PRODUCTION_ID1, "");
    }


    /**
     * Call to set the job production item1.
     */
    public static void setJobProductionItem2(String parentID, Context context) {
        getInstance(context).edit().putString(PreferenceConstants.JOB_PRODUCTION_ITEM2, parentID).commit();
    }

    /**
     * Call to get the job production item1.
     */
    public static String getJobProductionItem2(Context context) {
        return getInstance(context).getString(PreferenceConstants.JOB_PRODUCTION_ITEM2, "");
    }


    /**
     * Call to set the job production id2.
     */
    public static void setJobProductionID2(String parentID, Context context) {
        getInstance(context).edit().putString(PreferenceConstants.JOB_PRODUCTION_ID2, parentID).commit();
    }

    /**
     * Call to get the job production id2.
     */
    public static String getJobProductionID2(Context context) {
        return getInstance(context).getString(PreferenceConstants.JOB_PRODUCTION_ID2, "");
    }

    /**
     * Call to set the job production item1.
     */
    public static void setJobProductionItem3(String parentID, Context context) {
        getInstance(context).edit().putString(PreferenceConstants.JOB_PRODUCTION_ITEM3, parentID).commit();
    }

    /**
     * Call to get the job production item1.
     */
    public static String getJobProductionItem3(Context context) {
        return getInstance(context).getString(PreferenceConstants.JOB_PRODUCTION_ITEM3, "");
    }


    /**
     * Call to set the job production id3.
     */
    public static void setJobProductionID3(String parentID, Context context) {
        getInstance(context).edit().putString(PreferenceConstants.JOB_PRODUCTION_ID3, parentID).commit();
    }

    /**
     * Call to get the job production id3.
     */
    public static String getJobProductionID3(Context context) {
        return getInstance(context).getString(PreferenceConstants.JOB_PRODUCTION_ID3, "");
    }


    /**
     * Call to set the job production latitude.
     */
    public static void setJobLatitude(String latitude, Context context) {
        getInstance(context).edit().putString(PreferenceConstants.JOB_LATITUDE, latitude).commit();
    }

    /**
     * Call to get the job production latitude.
     */
    public static String getJobLatitude(Context context) {
        return getInstance(context).getString(PreferenceConstants.JOB_LATITUDE, "");
    }

    /**
     * Call to set the job production logitude.
     */
    public static void setJobLongitude(String logitude, Context context) {
        getInstance(context).edit().putString(PreferenceConstants.JOB_LONGITUDE, logitude).commit();
    }

    /**
     * Call to get the job production latitude.
     */
    public static String getJobLongitude(Context context) {
        return getInstance(context).getString(PreferenceConstants.JOB_LONGITUDE, "");
    }
}
