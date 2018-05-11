package com.smartdata.dataobject;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Anurag Sethi
 * The class handles all the objects associated with the User entity
 */


public class SupervisorDataList {

    /**
     * supervisor id
     */
    @SerializedName("_id")
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SupervisorJobAssignTo getSupervisorJobAssignTo() {
        return supervisorJobAssignTo;
    }

    public void setSupervisorJobAssignTo(SupervisorJobAssignTo supervisorJobAssignTo) {
        this.supervisorJobAssignTo = supervisorJobAssignTo;
    }

    /**
     * supervisor id
     */

    @SerializedName("job_assign_to")
    public SupervisorJobAssignTo supervisorJobAssignTo;

}


