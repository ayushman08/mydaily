package com.smartdata.dataobject;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Anurag Sethi
 * The class handles all the objects associated with the User entity
 */


public class ForemanJobDataList {

    /**
     * job id
     */
    @SerializedName("_id")
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ForemanJobListDetails getJob_detail() {
        return job_detail;
    }

    public void setJob_detail(ForemanJobListDetails job_detail) {
        this.job_detail = job_detail;
    }

    /**
     * job id
     */
    @SerializedName("job_detail")
    public ForemanJobListDetails job_detail;

}


