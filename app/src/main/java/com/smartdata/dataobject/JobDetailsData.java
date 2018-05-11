package com.smartdata.dataobject;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Vishwanath Nahak on 8/4/2017.
 */

public class JobDetailsData implements Parcelable{

    protected JobDetailsData(Parcel in) {
        _id = in.readString();
        longitude = in.readString();
        latitude = in.readString();
        job_location = in.readString();
        address = in.readString();
        client = in.readString();
        job_id = in.readString();
        job_added_by = in.readString();
        job_unique_id = in.readString();
        status = in.readString();
        description = in.readString();
        deleted = in.readByte() != 0;
        projected_end_date = in.readString();
        start_date = in.readString();
        actual_end_date = in.readString();
    }

    public static final Creator<JobDetailsData> CREATOR = new Creator<JobDetailsData>() {
        @Override
        public JobDetailsData createFromParcel(Parcel in) {
            return new JobDetailsData(in);
        }

        @Override
        public JobDetailsData[] newArray(int size) {
            return new JobDetailsData[size];
        }
    };

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getJob_location() {
        return job_location;
    }

    public void setJob_location(String job_location) {
        this.job_location = job_location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getJob_added_by() {
        return job_added_by;
    }

    public void setJob_added_by(String job_added_by) {
        this.job_added_by = job_added_by;
    }

    public String getJob_unique_id() {
        return job_unique_id;
    }

    public void setJob_unique_id(String job_unique_id) {
        this.job_unique_id = job_unique_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public BillableInfo getBillableInfo() {
        return billableInfo;
    }

    public void setBillableInfo(BillableInfo billableInfo) {
        this.billableInfo = billableInfo;
    }

    /**
     * job id
     */

    @SerializedName("_id")
    public String _id;

    /**
     * job longitude
     */
    @SerializedName("longitude")
    public String longitude;

    /**
     * job latitude
     */
    @SerializedName("latitude")
    public String latitude;


    /**
     * job job_location
     */
    @SerializedName("job_location")
    public String job_location;


    /**
     * job address
     */
    @SerializedName("address")
    public String address;

    /**
     * job client
     */
    @SerializedName("client")
    public String client;

    /**
     * job id
     */
    @SerializedName("job_id")
    public String job_id;

    /**
     * job added_by
     */
    @SerializedName("job_added_by")
    public String job_added_by;

    /**
     * job unique_id
     */
    @SerializedName("job_unique_id")
    public String job_unique_id;

    /**
     * job status
     */
    @SerializedName("status")
    public String status;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * job description
     */

    @SerializedName("description")
    public String description;


    /**
     * job deleted
     */
    @SerializedName("deleted")
    public boolean deleted;

    /**
     * job status
     */
    @SerializedName("billing_info")
    public BillableInfo billableInfo;

    /**
     * job projected_end_date
     */
    @SerializedName("projected_end_date")
    public String projected_end_date;

    /**
     * job start_date
     */
    @SerializedName("start_date")
    public String start_date;

    /**
     * job actual_end_date
     */
    @SerializedName("actual_end_date")
    public String actual_end_date;

    public String getProjected_end_date() {
        return projected_end_date;
    }

    public void setProjected_end_date(String projected_end_date) {
        this.projected_end_date = projected_end_date;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getActual_end_date() {
        return actual_end_date;
    }

    public void setActual_end_date(String actual_end_date) {
        this.actual_end_date = actual_end_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(longitude);
        dest.writeString(latitude);
        dest.writeString(job_location);
        dest.writeString(address);
        dest.writeString(client);
        dest.writeString(job_id);
        dest.writeString(job_added_by);
        dest.writeString(job_unique_id);
        dest.writeString(status);
        dest.writeString(description);
        dest.writeByte((byte) (deleted ? 1 : 0));
        dest.writeString(projected_end_date);
        dest.writeString(start_date);
        dest.writeString(actual_end_date);
    }
}
