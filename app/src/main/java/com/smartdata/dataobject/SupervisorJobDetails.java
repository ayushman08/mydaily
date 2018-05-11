package com.smartdata.dataobject;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vishwanath Nahak on 7/19/2017.
 */

public class SupervisorJobDetails {

    /**
     * job details code
     */
    @SerializedName("code")
    public int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JobDetailsOnID getJobDetailsData() {
        return jobDetailsData;
    }

    public void setJobDetailsData(JobDetailsOnID jobDetailsData) {
        this.jobDetailsData = jobDetailsData;
    }

    /**
     * job details message
     */
    @SerializedName("message")
    public String message;

    /**
     * job details data
     */
    @SerializedName("data")
    public JobDetailsOnID jobDetailsData;

}
