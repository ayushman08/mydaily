package com.smartdata.dataobject;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Anurag Sethi
 * The class handles all the objects associated with the User entity
 */
public class JobDetailsOnID {

    /**
     * job details data
     */
    @SerializedName("job_details")
    public JobDetailsData jobDetailsData;

    public JobDetailsData getJobDetailsData() {
        return jobDetailsData;
    }

    public void setJobDetailsData(JobDetailsData jobDetailsData) {
        this.jobDetailsData = jobDetailsData;
    }

    public ArrayList<ForemanDataList> getForemanDetailsArrayList() {
        return foremanDetailsArrayList;
    }

    public void setForemanDetailsArrayList(ArrayList<ForemanDataList> foremanDetailsArrayList) {
        this.foremanDetailsArrayList = foremanDetailsArrayList;
    }

    /**
     * foreman details data
     */
    @SerializedName("foreman_details")

    public ArrayList<ForemanDataList> foremanDetailsArrayList;

}
