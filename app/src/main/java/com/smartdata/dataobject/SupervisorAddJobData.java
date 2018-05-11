package com.smartdata.dataobject;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Anurag Sethi
 * The class handles all the objects associated with the User entity
 */
public class SupervisorAddJobData {

    /**
     * client information to add new job
     */
    @SerializedName("client")
    public String client;

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getProjected_end_date() {
        return projected_end_date;
    }

    public void setProjected_end_date(String projected_end_date) {
        this.projected_end_date = projected_end_date;
    }

    public String getActual_end_date() {
        return actual_end_date;
    }

    public void setActual_end_date(String actual_end_date) {
        this.actual_end_date = actual_end_date;
    }

    public String getJob_location() {
        return job_location;
    }

    public void setJob_location(String job_location) {
        this.job_location = job_location;
    }

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

    public AddNewJobResponseData getData() {
        return data;
    }

    public void setData(AddNewJobResponseData data) {
        this.data = data;
    }

    /**
     * job ID to update add new id
     */
    @SerializedName("_id")
    public String id;
    /**
     * /**
     * job ID to add new job
     */
    @SerializedName("job_id")
    public String job_id;
    /**
     * address information to add new job
     */
    @SerializedName("address")
    public String address;
    /**
     * start date information to add new job
     */
    @SerializedName("start_date")
    public String start_date;
    /**
     * date information to add new job
     */
    @SerializedName("projected_end_date")
    public String projected_end_date;
    /**
     * date information to add new job
     */
    @SerializedName("actual_end_date")
    public String actual_end_date;

    /**
     * location data to add new job
     */
    @SerializedName("job_location")
    public String job_location;

    /**
     * code data to add new job response
     */
    @SerializedName("code")
    public int code;

    /**
     * message data to add new job response
     */
    @SerializedName("message")
    public String message;

    /**
     * daily_sumbission_report
     */
    @SerializedName("daily_sumbission_report")
    public String daily_sumbission_report;

    public String getDaily_sumbission_report() {
        return daily_sumbission_report;
    }

    public void setDaily_sumbission_report(String daily_sumbission_report) {
        this.daily_sumbission_report = daily_sumbission_report;
    }

    /**
     * data on add new job response
     */
    @SerializedName("data")
    public AddNewJobResponseData data;


    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getJob_assign_by() {
        return job_assign_by;
    }

    public void setJob_assign_by(String job_assign_by) {
        this.job_assign_by = job_assign_by;
    }

    public String getJob_assign_to() {
        return job_assign_to;
    }

    public void setJob_assign_to(String job_assign_to) {
        this.job_assign_to = job_assign_to;
    }

    public String getForemen_id() {
        return foremen_id;
    }

    public void setForemen_id(String foremen_id) {
        this.foremen_id = foremen_id;
    }

    public ArrayList<ProductDataList> getProductDataList() {
        return productDataList;
    }

    public void setProductDataList(ArrayList<ProductDataList> productDataList) {
        this.productDataList = productDataList;
    }

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
     * job assign by
     */
    @SerializedName("job_assign_by")
    public String job_assign_by;

    /**
     * job assign to
     */
    @SerializedName("job_assign_to")
    public String job_assign_to;

    /**
     * foremen id for job
     */
    @SerializedName("foremen_id")
    public String foremen_id;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * job description
     */
    @SerializedName("description")
    public String description;

    /**
     * foremen id for job
     */
    @SerializedName("billable_items")
    public ArrayList<ProductDataList> productDataList;


    /**
     * foremen id to submit
     */
    @SerializedName("foremens")
    public ArrayList<ForemanID> foremanIDs;

    public ArrayList<ForemanID> getForemanIDs() {
        return foremanIDs;
    }

    public void setForemanIDs(ArrayList<ForemanID> foremanIDs) {
        this.foremanIDs = foremanIDs;
    }
}
