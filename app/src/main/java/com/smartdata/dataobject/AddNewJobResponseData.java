package com.smartdata.dataobject;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vishwanath Nahak on 7/25/2017.
 */

public class AddNewJobResponseData {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * job ID in add new job response
     */
    @SerializedName("_id")
    public String id;
}
