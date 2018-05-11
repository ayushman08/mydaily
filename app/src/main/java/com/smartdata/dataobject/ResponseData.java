package com.smartdata.dataobject;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Anurag Sethi
 * The class handles all the objects associated with the User entity
 */
public class ResponseData {

    /**
     * job status code
     */
    @SerializedName("code")
    public int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * job status message
     */
    @SerializedName("message")
    public String message;


}
