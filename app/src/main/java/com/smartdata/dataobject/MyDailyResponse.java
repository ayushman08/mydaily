package com.smartdata.dataobject;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Anurag Sethi
 * The class handles all the objects associated with the User entity
 */
public class MyDailyResponse {


    /**
     * job status code
     */
    @SerializedName("code")
    public int code;

    /**
     * job data status
     */
    @SerializedName("status")
    public String status;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    /**
     * job status message
     */
    @SerializedName("message")
    public String message;


    public MyDailyResponseData getMyDailyResponseData() {
        return myDailyResponseData;
    }

    public void setMyDailyResponseData(MyDailyResponseData myDailyResponseData) {
        this.myDailyResponseData = myDailyResponseData;
    }

    /**
     * job status data
     */
    @SerializedName("data")
    public MyDailyResponseData myDailyResponseData;
}
