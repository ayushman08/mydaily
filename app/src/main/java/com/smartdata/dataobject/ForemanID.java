package com.smartdata.dataobject;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vishwanath Nahak on 9/6/2017.
 */

public class ForemanID {
    /**
     * job id's to submit
     */
    @SerializedName("_id")
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
