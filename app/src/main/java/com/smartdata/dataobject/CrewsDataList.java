package com.smartdata.dataobject;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Anurag Sethi
 * The class handles all the objects associated with the User entity
 */


public class CrewsDataList implements Parcelable {

    /**
     * crews id
     */
    @SerializedName("_id")
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * crews email
     */
    @SerializedName("email")
    public String email;

    /**
     * crews lastname
     */
    @SerializedName("lastname")
    public String lastname;

    /**
     * crews firstname
     */
    @SerializedName("firstname")
    public String firstname;

    /**
     * crews status
     */
    @SerializedName("status")
    public String status;


    /**
     * crews profile_image
     */
    @SerializedName("crewsImage")
    public CrewImageData crewImageDatas;

    public CrewImageData getCrewImageDatas() {
        return crewImageDatas;
    }

    public void setCrewImageDatas(CrewImageData crewImageDatas) {
        this.crewImageDatas = crewImageDatas;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @SuppressWarnings("rawtypes")
    public static final Creator CREATOR = new Creator() {
        public CrewsDataList createFromParcel(Parcel in) {
            return new CrewsDataList(in);
        }

        public CrewsDataList[] newArray(int size) {
            return new CrewsDataList[size];
        }
    };

    public CrewsDataList(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(firstname);
        dest.writeString(lastname);
        dest.writeString(email);

    }

    public void readFromParcel(Parcel in) {
        this.id = in.readString();
        this.firstname = in.readString();
        this.lastname = in.readString();
        this.email = in.readString();
    }

    @Override
    public String toString() {
        return firstname + " " + lastname + " "
                + email;
    }
}


