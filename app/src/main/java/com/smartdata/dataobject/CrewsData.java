package com.smartdata.dataobject;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Anurag Sethi
 * The class handles all the objects associated with the User entity
 */
public class CrewsData {

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

    /**
     * crews id
     */
    @SerializedName("_id")
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * crews request parent_id
     */
    @SerializedName("parent_id")
    public String parent_id;

    /**
     * crews data page
     */
    @SerializedName("page")
    public String page;

    /**
     * crews count
     */
    @SerializedName("count")
    public String count;

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    /**
     * crews search text
     */
    @SerializedName("searchText")
    public String searchText;


    /**
     * crews status code
     */
    @SerializedName("code")
    public int code;

    /**
     * crews data status
     */
    @SerializedName("status")
    public String status;

    /**
     * crews status message
     */
    @SerializedName("message")
    public String message;

    /**
     * crews status message
     */
    @SerializedName("data")
    public ArrayList<CrewsDataList> crewsDataList;

    public ArrayList<CrewsDataList> getCrewsDataList() {
        return crewsDataList;
    }

    public void setCrewsDataList(ArrayList<CrewsDataList> crewsDataList) {
        this.crewsDataList = crewsDataList;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * crews status firsname
     */
    @SerializedName("firstname")
    public String firstname;

    /**
     * crews status lastname
     */
    @SerializedName("lastname")
    public String lastname;
    /**
     * crews status email
     */
    @SerializedName("email")
    public String email;
}
