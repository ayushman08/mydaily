package com.smartdata.dataobject;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Anurag Sethi
 * The class handles all the objects associated with the User entity
 */
public class SupervisorData {


    /**
     * supervisor jobId
     */
    @SerializedName("jobId")
    public String jobId;


    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    /**
     * supervisor status code
     */
    @SerializedName("code")
    public int code;


    /**
     * supervisor status message
     */
    @SerializedName("message")
    public String message;

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

    public ArrayList<SupervisorDataList> getSupervisorDataList() {
        return supervisorDataList;
    }

    public void setSupervisorDataList(ArrayList<SupervisorDataList> supervisorDataList) {
        this.supervisorDataList = supervisorDataList;
    }

    /**
     * supervisor status message
     */
    @SerializedName("data")
    public ArrayList<SupervisorDataList> supervisorDataList;

}
