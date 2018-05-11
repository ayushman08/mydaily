package com.smartdata.dataobject;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Anurag Sethi
 * The class handles all the objects associated with the User entity
 */
public class ForemanData {

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

    public ArrayList<ForemanDataList> getForemanDataList() {
        return foremanDataList;
    }

    public void setForemanDataList(ArrayList<ForemanDataList> foremanDataList) {
        this.foremanDataList = foremanDataList;
    }

    /**
     * foreman request parent_id
     */
    @SerializedName("parent_id")
    public String parent_id;

    /**
     * foreman data page
     */
    @SerializedName("page")
    public String page;

    /**
     * foreman count
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
     * foreman search text
     */
    @SerializedName("searchText")
    public String searchText;


    /**
     * foreman status code
     */
    @SerializedName("code")
    public int code;

    /**
     * foreman data status
     */
    @SerializedName("status")
    public String status;

    /**
     * foreman status message
     */
    @SerializedName("message")
    public String message;

    /**
     * foreman status message
     */
    @SerializedName("data")
    public ArrayList<ForemanDataList> foremanDataList;

}
