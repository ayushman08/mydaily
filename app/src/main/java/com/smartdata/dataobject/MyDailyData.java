package com.smartdata.dataobject;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Anurag Sethi
 * The class handles all the objects associated with the User entity
 */
public class MyDailyData {


    /**
     * job id
     */
    @SerializedName("_id")
    public String id;

    /**
     * job id
     */
    @SerializedName("job_id")
    public String job_id;

    /**
     * job crews_id
     */
    @SerializedName("crews_id")
    public String crews_id;

    /**
     * job no production
     */
    @SerializedName("no_production")
    public boolean no_production;

    /**
     * job notes
     */
    @SerializedName("notes")
    public String notes;


    /**
     * job reject_notes
     */
    @SerializedName("reject_notes")
    public String reject_notes;

    /**
     * job job_map
     */
    @SerializedName("job_map")
    public String job_map;


    /**
     * job latitude
     */
    @SerializedName("latitude")
    public String latitude;


    /**
     * job longitude
     */
    @SerializedName("longitude")
    public String longitude;

    /**
     * job job_location
     */
    @SerializedName("job_location")
    public String job_location;


    /**
     * supervisor id
     */
    @SerializedName("supervisor_id")
    public String supervisor_id;


    /**
     * job data page
     */
    @SerializedName("page")
    public String page;

    /**
     * job data from_date
     */
    @SerializedName("from_date")
    public String from_date;

    /**
     * job data to_date
     */
    @SerializedName("to_date")
    public String to_date;

    /**
     * job count
     */
    @SerializedName("count")
    public String count;

    /**
     * job parent_id
     */
    @SerializedName("parent_id")
    public String parent_id;

    /**
     * job users_id
     */
    @SerializedName("user_id")
    public String user_id;


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

    /**
     * job status is_draft
     */
    @SerializedName("is_draft")
    public boolean is_draft;


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

    public ArrayList<MyDailyDataList> getMyDailyDataList() {
        return myDailyDataList;
    }

    public void setMyDailyDataList(ArrayList<MyDailyDataList> myDailyDataList) {
        this.myDailyDataList = myDailyDataList;
    }

    /**
     * job status data
     */
    @SerializedName("data")
    public ArrayList<MyDailyDataList> myDailyDataList = new ArrayList<>();

    public ArrayList<ProductDataList> getProductDataList() {
        return productDataList;
    }

    public void setProductDataList(ArrayList<ProductDataList> productDataList) {
        this.productDataList = productDataList;
    }

    /**
     * foremen id for job
     */

    @SerializedName("billable_items")
    public ArrayList<ProductDataList> productDataList;
}
