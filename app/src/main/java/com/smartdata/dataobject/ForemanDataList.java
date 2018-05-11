package com.smartdata.dataobject;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Anurag Sethi
 * The class handles all the objects associated with the User entity
 */


public class ForemanDataList {

    /**
     * foreman id
     */
    @SerializedName("_id")
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * foreman email
     */
    @SerializedName("email")
    public String email;

    /**
     * foreman lastname
     */
    @SerializedName("lastname")
    public String lastname;

    /**
     * foreman firstname
     */
    @SerializedName("firstname")
    public String firstname;

    /**
     * foreman status
     */
    @SerializedName("status")
    public String status;


    @Override
    public String toString() {
        // filter the list based on any of the key
        return firstname + " " + lastname + " " + email;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ForemanDataList) && this.id == ((ForemanDataList) obj).getId();
    }
}


