package com.smartdata.dataobject;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Anurag Sethi
 * The class handles all the objects associated with the User entity
 */
public class JobData {

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
    @SerializedName("parent_id")
    public String parent_id;

    public String getUsers_id() {
        return users_id;
    }

    public void setUsers_id(String users_id) {
        this.users_id = users_id;
    }

    /**
     * job request users_id
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

    /**
     * job status message
     */
    @SerializedName("message")
    public String message;

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
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

    public ArrayList<JobDataList> getJobDataList() {
        return jobDataList;
    }

    public void setJobDataList(ArrayList<JobDataList> jobDataList) {
        this.jobDataList = jobDataList;
    }

    /**
     * job status message
     */
    @SerializedName("data")
    public ArrayList<JobDataList> jobDataList;

}
