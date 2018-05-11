package com.smartdata.dataobject;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vishwanath Nahak on 7/26/2017.
 */

public class ForemanJobListDetails {
    /**
     * id
     */
    @SerializedName("_id")
    public String id;

    /**
     * client
     */
    @SerializedName("client")
    public String client;

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
     * job id
     */
    @SerializedName("job_id")
    public String job_id;

    /**
     * job billing_info
     */
    @SerializedName("billing_info")
    public ForemanJobBillingInfo foremanJobBillingInfo;

    /**
     * job unique id
     */
    @SerializedName("job_unique_id")
    public String job_unique_id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public ForemanJobBillingInfo getForemanJobBillingInfo() {
        return foremanJobBillingInfo;
    }

    public void setForemanJobBillingInfo(ForemanJobBillingInfo foremanJobBillingInfo) {
        this.foremanJobBillingInfo = foremanJobBillingInfo;
    }

    public String getJob_unique_id() {
        return job_unique_id;
    }

    public void setJob_unique_id(String job_unique_id) {
        this.job_unique_id = job_unique_id;
    }

    public String getActual_end_date() {
        return actual_end_date;
    }

    public void setActual_end_date(String actual_end_date) {
        this.actual_end_date = actual_end_date;
    }

    public String getProjected_end_date() {
        return projected_end_date;
    }

    public void setProjected_end_date(String projected_end_date) {
        this.projected_end_date = projected_end_date;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    /**
     * job actual end date
     */
    @SerializedName("actual_end_date")

    public String actual_end_date;


    /**
     * projected end date
     */
    @SerializedName("projected_end_date")
    public String projected_end_date;


    /**
     * start date
     */
    @SerializedName("start_date")
    public String start_date;

    /**
     * job location
     */
    @SerializedName("job_location")
    public String job_location;

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
     * job description
     */
    @SerializedName("description")
    public String description;


    public String getJob_location() {
        return job_location;
    }

    public void setJob_location(String job_location) {
        this.job_location = job_location;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * job is deleted
     */
    @SerializedName("deleted")
    public boolean deleted;

    @Override
    public String toString() {
        return client + "" + job_id + "" + job_location;
    }
}
