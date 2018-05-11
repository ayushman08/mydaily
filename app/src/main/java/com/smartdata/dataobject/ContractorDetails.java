package com.smartdata.dataobject;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContractorDetails implements Parcelable {

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

    protected ContractorDetails(Parcel in) {
        id = in.readString();
        firstname = in.readString();
        lastname = in.readString();
    }

    public static final Creator<ContractorDetails> CREATOR = new Creator<ContractorDetails>() {
        @Override
        public ContractorDetails createFromParcel(Parcel in) {
            return new ContractorDetails(in);
        }

        @Override
        public ContractorDetails[] newArray(int size) {
            return new ContractorDetails[size];
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
