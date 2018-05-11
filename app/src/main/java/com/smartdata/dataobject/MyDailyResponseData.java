package com.smartdata.dataobject;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vishwanath Nahak on 8/17/2017.
 */

public class MyDailyResponseData {
    /**
     * job status id
     */
    @SerializedName("_id")
    public String id;

    /**
     * job status crew_id
     */
    @SerializedName("crew_id")
    public String crew_id;

    public String getCrew_id() {
        return crew_id;
    }

    public void setCrew_id(String crew_id) {
        this.crew_id = crew_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
