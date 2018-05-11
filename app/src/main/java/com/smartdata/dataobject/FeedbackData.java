package com.smartdata.dataobject;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Anurag Sethi
 * The class handles all the objects associated with the User entity
 */
public class FeedbackData {


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
