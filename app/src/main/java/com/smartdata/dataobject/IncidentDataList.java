package com.smartdata.dataobject;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Vishwanath Nahak on 8/10/2017.
 */

public class IncidentDataList implements Parcelable {

    /**
     * Incident data id
     */
    @SerializedName("_id")
    public String id;

    /**
     * Incident updatedAt
     */
    @SerializedName("updatedAt")
    public String updatedAt;


    /**
     * Incident createdAt
     */
    @SerializedName("createdAt")
    public String createdAt;

    /**
     * Incident description
     */
    @SerializedName("description")
    public String description;

    /**
     * Incident mark_Was_It
     */
    @SerializedName("mark_Was_It")
    public String mark_Was_It;

    /**
     * Incident fault
     */
    @SerializedName("fault")
    public String fault;

    /**
     * job status is_draft
     */
    @SerializedName("is_draft")
    public boolean is_draft;

    public boolean is_draft() {
        return is_draft;
    }

    public void setIs_draft(boolean is_draft) {
        this.is_draft = is_draft;
    }

    /**
     * Incident address
     */
    @SerializedName("address")
    public String address;

    /**
     * Incident ticket_No
     */
    @SerializedName("ticket_No")
    public String ticket_No;


    /**
     * Incident own_It
     */
    @SerializedName("own_It")
    public String own_It;

    /**
     * Incident damage_Report
     */
    @SerializedName("damage_Report")
    public String damage_Report;

    /**
     * Incident incident_AddedBy
     */
    @SerializedName("user_id")
    public String user_id;

    /**
     * Incident job id
     */
    @SerializedName("job_id")
    public String job_id;

    /**
     * Incident deleted
     */
    @SerializedName("deleted")
    public boolean deleted;

    /**
     * Incident data status
     */
    @SerializedName("status")
    public String status;

    /**
     * Incident date
     */
    @SerializedName("did_It_Happen")
    public String did_It_Happen;


    protected IncidentDataList(Parcel in) {
        id = in.readString();
        updatedAt = in.readString();
        createdAt = in.readString();
        description = in.readString();
        mark_Was_It = in.readString();
        fault = in.readString();
        address = in.readString();
        ticket_No = in.readString();
        own_It = in.readString();
        damage_Report = in.readString();
        user_id = in.readString();
        job_id = in.readString();
        deleted = in.readByte() != 0;
        status = in.readString();
        did_It_Happen = in.readString();
        incidentImages = in.createTypedArrayList(IncidentImages.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(updatedAt);
        dest.writeString(createdAt);
        dest.writeString(description);
        dest.writeString(mark_Was_It);
        dest.writeString(fault);
        dest.writeString(address);
        dest.writeString(ticket_No);
        dest.writeString(own_It);
        dest.writeString(damage_Report);
        dest.writeString(user_id);
        dest.writeString(job_id);
        dest.writeByte((byte) (deleted ? 1 : 0));
        dest.writeString(status);
        dest.writeString(did_It_Happen);
        dest.writeTypedList(incidentImages);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<IncidentDataList> CREATOR = new Creator<IncidentDataList>() {
        @Override
        public IncidentDataList createFromParcel(Parcel in) {
            return new IncidentDataList(in);
        }

        @Override
        public IncidentDataList[] newArray(int size) {
            return new IncidentDataList[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMark_Was_It() {
        return mark_Was_It;
    }

    public void setMark_Was_It(String mark_Was_It) {
        this.mark_Was_It = mark_Was_It;
    }

    public String getFault() {
        return fault;
    }

    public void setFault(String fault) {
        this.fault = fault;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTicket_No() {
        return ticket_No;
    }

    public void setTicket_No(String ticket_No) {
        this.ticket_No = ticket_No;
    }

    public String getOwn_It() {
        return own_It;
    }

    public void setOwn_It(String own_It) {
        this.own_It = own_It;
    }

    public String getDamage_Report() {
        return damage_Report;
    }

    public void setDamage_Report(String damage_Report) {
        this.damage_Report = damage_Report;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDid_It_Happen() {
        return did_It_Happen;
    }

    public void setDid_It_Happen(String did_It_Happen) {
        this.did_It_Happen = did_It_Happen;
    }


    public ArrayList<IncidentImages> getIncidentImages() {
        return incidentImages;
    }

    public void setIncidentImages(ArrayList<IncidentImages> incidentImages) {
        this.incidentImages = incidentImages;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }


    /**
     * Incident images
     */
    @SerializedName("incident_image")
    public ArrayList<IncidentImages> incidentImages;

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public JobDetailsData getJobDetailsData() {
        return jobDetailsData;
    }

    public void setJobDetailsData(JobDetailsData jobDetailsData) {
        this.jobDetailsData = jobDetailsData;
    }

    /**
     * Incident images
     */
    @SerializedName("job_details")
    public JobDetailsData jobDetailsData;

    @Override
    public String toString() {
        // filter the list based on any of the key
        return damage_Report + " " + own_It + " "
                + address + " " + description;
    }
}
