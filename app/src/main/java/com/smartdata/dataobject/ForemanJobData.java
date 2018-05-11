package com.smartdata.dataobject;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Anurag Sethi
 * The class handles all the objects associated with the User entity
 */
public class ForemanJobData {

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
     * job request parent_id
     */
    @SerializedName("users_id")
    public String users_id;

    /**
     * job data page
     */
    @SerializedName("page")
    public String page;

    /**
     * job count
     */
    @SerializedName("count")
    public String count;


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

    public String getJob_status() {
        return job_status;
    }

    public void setJob_status(String job_status) {
        this.job_status = job_status;
    }

    /**
     * job status
     */
    @SerializedName("job_status")
    public String job_status;

    /**
     * job status message
     */
    @SerializedName("message")
    public String message;

    public String getUsers_id() {
        return users_id;
    }

    public void setUsers_id(String users_id) {
        this.users_id = users_id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public ArrayList<ForemanJobDataList> getForemanJobDataList() {
        return foremanJobDataList;
    }

    public void setForemanJobDataList(ArrayList<ForemanJobDataList> foremanJobDataList) {
        this.foremanJobDataList = foremanJobDataList;
    }

    /**
     * job status message
     */
    @SerializedName("data")
    public ArrayList<ForemanJobDataList> foremanJobDataList;

}
