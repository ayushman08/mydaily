package com.smartdata.dataobject;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Anurag Sethi
 * The class handles all the objects associated with the User entity
 */


public class JobDataList {

    /**
     * job id
     */
    @SerializedName("_id")
    public String id;

    /**
     * job name
     */
    @SerializedName("name")
    public String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public ArrayList<BillableItems> getBillableItems() {
        return billableItems;
    }

    public void setBillableItems(ArrayList<BillableItems> billableItems) {
        this.billableItems = billableItems;
    }

    public JobAssignDetails getJobAssignTo() {
        return jobAssignTo;
    }

    public void setJobAssignTo(JobAssignDetails jobAssignTo) {
        this.jobAssignTo = jobAssignTo;
    }

    /**
     * job job assign to user details
     */
    @SerializedName("job_assign_to")
    public JobAssignDetails jobAssignTo;

    /**
     * job job assign to user billable item list
     */
    @SerializedName("billable_items")
    public ArrayList<BillableItems> billableItems;

    @Override
    public String toString() {
        // filter the list based on any of the key
        return name;
    }
}


