package com.smartdata.dataobject;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vishwanath Nahak on 8/11/2017.
 */

public class CrewsDetails implements Parcelable {
    /**
     * job data id
     */
    @SerializedName("_id")
    public String id;

    protected CrewsDetails(Parcel in) {
        id = in.readString();
        firstname = in.readString();
        lastname = in.readString();
    }

    public static final Creator<CrewsDetails> CREATOR = new Creator<CrewsDetails>() {
        @Override
        public CrewsDetails createFromParcel(Parcel in) {
            return new CrewsDetails(in);
        }

        @Override
        public CrewsDetails[] newArray(int size) {
            return new CrewsDetails[size];
        }
    };

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

    /**
     * job data firstname

     */
    @SerializedName("firstname")
    public String firstname;

    /**
     * job data lastname
     */
    @SerializedName("lastname")
    public String lastname;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(firstname);
        dest.writeString(lastname);
    }
}
