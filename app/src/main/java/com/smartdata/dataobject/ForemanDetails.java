package com.smartdata.dataobject;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vishwanath Nahak on 8/11/2017.
 */

public class ForemanDetails implements Parcelable {

    /**
     * job data id
     */
    @SerializedName("_id")
    public String id;
    /**
     * job data firstname
     */
    @SerializedName("firstname")
    public String firstname;

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
     * job data lastname
     */
    @SerializedName("lastname")
    public String lastname;

    protected ForemanDetails(Parcel in) {
        id = in.readString();
        firstname = in.readString();
        lastname = in.readString();
    }

    public static final Creator<ForemanDetails> CREATOR = new Creator<ForemanDetails>() {
        @Override
        public ForemanDetails createFromParcel(Parcel in) {
            return new ForemanDetails(in);
        }

        @Override
        public ForemanDetails[] newArray(int size) {
            return new ForemanDetails[size];
        }
    };

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
