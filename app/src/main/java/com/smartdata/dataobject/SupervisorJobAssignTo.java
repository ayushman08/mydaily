package com.smartdata.dataobject;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vishwanath Nahak on 8/17/2017.
 */

public class SupervisorJobAssignTo {
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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * supervisor firstname
     */
    @SerializedName("firstname")
    public String firstname;

    /**
     * supervisor lastname
     */
    @SerializedName("lastname")
    public String lastname;


}
