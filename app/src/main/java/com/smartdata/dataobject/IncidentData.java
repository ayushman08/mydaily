package com.smartdata.dataobject;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Anurag Sethi
 * The class handles all the objects associated with the User entity
 */
public class IncidentData {


    /**
     * incidents request id
     */
    @SerializedName("_id")
    public String id;

    /**
     * incidents request job_id
     */
    @SerializedName("job_id")
    public String job_id;


    /**
     * incidents request damage_Report
     */
    @SerializedName("damage_Report")
    public String damage_Report;

    /**
     * incidents status is_draft
     */
    @SerializedName("is_draft")
    public boolean is_draft;

    /**
     * incidents request own_It
     */
    @SerializedName("own_It")
    public String own_It;

    /**
     * incidents request address
     */
    @SerializedName("address")
    public String address;

    /**
     * incidents request ticket_No
     */
    @SerializedName("ticket_No")
    public String ticket_No;

    /**
     * incidents request fault
     */
    @SerializedName("fault")
    public String fault;

    /**
     * incidents request mark_Was_It
     */
    @SerializedName("mark_Was_It")
    public String mark_Was_It;

    /**
     * incidents request did_It_Happen
     */
    @SerializedName("did_It_Happen")
    public String did_It_Happen;

    /**
     * incidents request description
     */
    @SerializedName("description")
    public String description;

    /**
     * incidents request user_id
     */
    @SerializedName("user_id")
    public String user_id;

    /**
     * incidents request parent_id
     */
    @SerializedName("parent_id")
    public String parent_id;

    /**
     * incidents data page
     */
    @SerializedName("page")
    public String page;

    /**
     * incidents count
     */
    @SerializedName("count")
    public String count;


    /**
     * incidents status code
     */
    @SerializedName("code")
    public int code;

    /**
     * incidents data status
     */
    @SerializedName("status")
    public String status;

    /**
     * incidents status message
     */
    @SerializedName("message")
    public String message;


    @SerializedName("latitude")
    public String latitude;


    /**
     * job longitude
     */
    @SerializedName("longitude")
    public String longitude;


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

    public ArrayList<IncidentDataList> getIncidentsDataList() {
        return incidentsDataList;
    }

    public void setIncidentsDataList(ArrayList<IncidentDataList> incidentsDataList) {
        this.incidentsDataList = incidentsDataList;
    }

    /**
     * incident data list
     */
    @SerializedName("data")
    public ArrayList<IncidentDataList> incidentsDataList;

}
