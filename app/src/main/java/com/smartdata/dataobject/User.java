package com.smartdata.dataobject;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Anurag Sethi
 * The class handles all the objects associated with the User entity
 */
public class User {

    /**
     * User's email address
     */
    @SerializedName("email")
    public String email;

    /**
     * User's password
     */
    @SerializedName("password")
    public String password;

    /**
     * deviceType
     */
    @SerializedName("deviceType")
    public String deviceType;

    /**
     * deviceId
     */
    @SerializedName("deviceId")
    public String deviceId;


    /**
     * deviceToken
     */
    @SerializedName("deviceToken")
    public String deviceToken;


    @SerializedName("code")
    public int code;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("status")
    public String status;

    @SerializedName("userId")
    public String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    public ResponseData getDataReturned() {
        return dataReturned;
    }

    public void setDataReturned(ResponseData dataReturned) {
        this.dataReturned = dataReturned;
    }

    @SerializedName("message")

    public String message;

    @SerializedName("data")
    public ResponseData dataReturned;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }


    public class ResponseData {

        @SerializedName("_id")
        public String id;

        @SerializedName("parent_id")
        public String parent_id;

        @SerializedName("role")
        public String role;

        @SerializedName("email")
        public String email;

        @SerializedName("lastname")
        public String lastname;

        @SerializedName("firstname")
        public String firstname;

        @SerializedName("saved_card")
        public ArrayList<String> saved_card;

        @SerializedName("is_stripe_acc_verified")
        public boolean isStripeAccVerified;

        @SerializedName("token")
        public String token;

        @SerializedName("profile_image")
        public String profile_image;

        @SerializedName("role_code")
        public String role_code;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
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

        public ArrayList<String> getSaved_card() {
            return saved_card;
        }

        public void setSaved_card(ArrayList<String> saved_card) {
            this.saved_card = saved_card;
        }

        public boolean isStripeAccVerified() {
            return isStripeAccVerified;
        }

        public void setStripeAccVerified(boolean stripeAccVerified) {
            isStripeAccVerified = stripeAccVerified;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getProfile_image() {
            return profile_image;
        }

        public void setProfile_image(String profile_image) {
            this.profile_image = profile_image;
        }

        public String getRole_code() {
            return role_code;
        }

        public void setRole_code(String role_code) {
            this.role_code = role_code;
        }
    }


    /**
     * Download data
     */

    @SerializedName("user_id")
    public String user_id;

    @SerializedName("supervisor_id")
    public String supervisor_id;

    @SerializedName("firstname")
    public String firstname;

    @SerializedName("lastname")
    public String lastname;

}
