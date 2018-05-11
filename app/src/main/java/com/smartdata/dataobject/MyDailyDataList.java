package com.smartdata.dataobject;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Vishwanath Nahak on 8/9/2017.
 */

public class MyDailyDataList implements Parcelable {

    /**
     * job data id
     */
    @SerializedName("_id")
    public String id;

    /**
     * job data notes
     */
    @SerializedName("notes")
    public String notes;

    /**
     * job data no_production
     */
    @SerializedName("no_production")
    public boolean no_production;


    /**
     * job status
     */
    @SerializedName("status")
    public String status;

    /**
     * job status is_draft
     */
    @SerializedName("is_draft")
    public boolean is_draft;

    /**
     * job reject_notes
     */
    @SerializedName("reject_notes")
    public String reject_notes;


    /**
     * contractor_details
     */
    @SerializedName("contractor_details")


    public ContractorDetails contractorDetails;


    /**
     * job data daily_number
     */
    @SerializedName("daily_number")
    public String daily_number;


    /**
     * job data to_date
     */
    @SerializedName("to_date")
    public String to_date;

    /**
     * job data from_date
     */
    @SerializedName("from_date")
    public String from_date;



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReject_notes() {
        return reject_notes;
    }

    public void setReject_notes(String reject_notes) {
        this.reject_notes = reject_notes;
    }


    public ContractorDetails getContractorDetails() {
        return contractorDetails;
    }

    public void setContractorDetails(ContractorDetails contractorDetails) {
        this.contractorDetails = contractorDetails;
    }

    public boolean is_draft() {
        return is_draft;
    }

    public void setIs_draft(boolean is_draft) {
        this.is_draft = is_draft;
    }

    public String getDaily_number() {
        return daily_number;
    }

    public void setDaily_number(String daily_number) {
        this.daily_number = daily_number;
    }


    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    protected MyDailyDataList(Parcel in) {
        id = in.readString();
        notes = in.readString();
        no_production = in.readByte() != 0;
        longitude = in.readString();
        latitude = in.readString();
        job_location = in.readString();
        job_map = in.readString();
        job_na = in.readString();
        work_date = in.readString();
        to_date = in.readString();
        from_date = in.readString();
        daily_number = in.readString();
        status = in.readString();
        reject_notes = in.readString();

    }

    public static final Creator<MyDailyDataList> CREATOR = new Creator<MyDailyDataList>() {
        @Override
        public MyDailyDataList createFromParcel(Parcel in) {
            return new MyDailyDataList(in);
        }

        @Override
        public MyDailyDataList[] newArray(int size) {
            return new MyDailyDataList[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public MyDailyDetails getMyDailyDetails() {
        return myDailyDetails;
    }

    public void setMyDailyDetails(MyDailyDetails myDailyDetails) {
        this.myDailyDetails = myDailyDetails;
    }

    /**
     * job data job_detail
     */
    @SerializedName("job_detail")
    public MyDailyDetails myDailyDetails;


    /**
     * job data longitude
     */
    @SerializedName("longitude")
    public String longitude;


    /**
     * job data latitude
     */
    @SerializedName("latitude")
    public String latitude;

    /**
     * job data job_location
     */
    @SerializedName("job_location")
    public String job_location;

    /**
     * job data job_map
     */
    @SerializedName("job_map")
    public String job_map;

    /**
     * job data job_na
     */
    @SerializedName("job_na")
    public String job_na;

    public boolean isNo_production() {
        return no_production;
    }

    public void setNo_production(boolean no_production) {
        this.no_production = no_production;
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

    public String getJob_map() {
        return job_map;
    }

    public void setJob_map(String job_map) {
        this.job_map = job_map;
    }

    public String getJob_na() {
        return job_na;
    }

    public void setJob_na(String job_na) {
        this.job_na = job_na;
    }

    public String getWork_date() {
        return work_date;
    }

    public void setWork_date(String work_date) {
        this.work_date = work_date;
    }

    /**
     * job data work_date
     */
    @SerializedName("work_date")
    public String work_date;


    /**
     * job data supervisor_details
     */
    @SerializedName("supervisor_details")
    public SupervisorDetails supervisorDetails;

    /**
     * job data foremen_details
     */
    @SerializedName("foremen_details")
    public ForemanDetails foremanDetails;


    /**
     * job data crews_details
     */
    @SerializedName("crews_details")
    public CrewsDetails crews_details;

    /**
     * job data dailiesImage
     */
    @SerializedName("dailiesImage")
    public ArrayList<DailiesImage> dailiesImageArrayList;

    public SupervisorDetails getSupervisorDetails() {
        return supervisorDetails;
    }

    public void setSupervisorDetails(SupervisorDetails supervisorDetails) {
        this.supervisorDetails = supervisorDetails;
    }

    public ForemanDetails getForemanDetails() {
        return foremanDetails;
    }

    public void setForemanDetails(ForemanDetails foremanDetails) {
        this.foremanDetails = foremanDetails;
    }

    public CrewsDetails getCrews_details() {
        return crews_details;
    }

    public void setCrews_details(CrewsDetails crews_details) {
        this.crews_details = crews_details;
    }

    public ArrayList<DailiesImage> getDailiesImageArrayList() {
        return dailiesImageArrayList;
    }

    public void setDailiesImageArrayList(ArrayList<DailiesImage> dailiesImageArrayList) {
        this.dailiesImageArrayList = dailiesImageArrayList;
    }

    public ArrayList<BillableItems> getBillableItemses() {
        return billableItemses;
    }

    public void setBillableItemses(ArrayList<BillableItems> billableItemses) {
        this.billableItemses = billableItemses;
    }

    /**
     * job data billable_items
     */
    @SerializedName("billable_items")
    public ArrayList<BillableItems> billableItemses;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(notes);
        dest.writeByte((byte) (no_production ? 1 : 0));
        dest.writeString(longitude);
        dest.writeString(latitude);
        dest.writeString(job_location);
        dest.writeString(job_map);
        dest.writeString(job_na);
        dest.writeString(work_date);
        dest.writeString(to_date);
        dest.writeString(from_date);
        dest.writeString(daily_number);
        dest.writeString(status);

        dest.writeString(reject_notes);
    }

    @Override
    public String toString() {
        // filter the list based on any of the key
        return job_location + " " + notes + " "
                + daily_number;
    }
}
