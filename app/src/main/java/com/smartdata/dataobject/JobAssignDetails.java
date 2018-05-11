package com.smartdata.dataobject;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vishwanath Nahak on 7/19/2017.
 */

public class JobAssignDetails implements Parcelable {

    /**
     * JAssign Job id
     */
    @SerializedName("_id")
    public String id = "";

    /**
     * Assign person firstname
     */
    @SerializedName("firstname")
    public String firstname = "";

    /**
     * Assign person lastname
     */
    @SerializedName("lastname")
    public String lastname = "";


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Assign person email
     */
    @SerializedName("email")
    public String email = "";


    /**
     * Job isDeleted
     */
    @SerializedName("deleted")
    public boolean isDeleted = false;

    /**
     * Job status
     */
    @SerializedName("status")
    public String status = "";


    /**
     * Job location
     */
    @SerializedName("job_location")
    public String job_location = "";

    /**
     * Job latitude
     */
    @SerializedName("latitude")
    public String latitude = "";

    public String getJob_location() {
        return job_location;
    }

    public void setJob_location(String job_location) {
        this.job_location = job_location;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getJob_unique_id() {
        return job_unique_id;
    }

    public void setJob_unique_id(String job_unique_id) {
        this.job_unique_id = job_unique_id;
    }

    /**
     * Job longitude
     */

    @SerializedName("longitude")
    public String longitude = "";

    /**
     * Job unique id
     */
    @SerializedName("job_unique_id")
    public String job_unique_id = "";

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    /**
     * Job  id
     */
    @SerializedName("job_id")
    public String job_id = "";


    /**
     * Job client
     */

    @SerializedName("client")
    public String client = "";

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Job description
     */
    @SerializedName("description")
    public String description = "";

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    /**
     * Job start_date
     */
    @SerializedName("start_date")
    public String start_date = "";

    @Override
    public int describeContents() {
        return 0;
    }


    @SuppressWarnings("rawtypes")
    public static final Creator CREATOR = new Creator() {
        public JobAssignDetails createFromParcel(Parcel in) {
            return new JobAssignDetails(in);
        }

        public JobAssignDetails[] newArray(int size) {
            return new JobAssignDetails[size];
        }
    };

    public JobAssignDetails(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(firstname);
        dest.writeString(lastname);
        dest.writeString(email);
        dest.writeString(job_location);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(job_unique_id);
        dest.writeString(job_id);
        dest.writeString(client);
        dest.writeString(description);
        dest.writeString(start_date);
    }

    public void readFromParcel(Parcel in) {
        this.id = in.readString();
        this.firstname = in.readString();
        this.lastname = in.readString();
        this.email = in.readString();
        this.job_location = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.job_unique_id = in.readString();
        this.job_id = in.readString();
        this.client = in.readString();
        this.description = in.readString();
        this.start_date = in.readString();

    }

    @Override
    public String toString() {
        return firstname + " " + lastname + " "
                + email + " " + client + " " + description;
    }
}
