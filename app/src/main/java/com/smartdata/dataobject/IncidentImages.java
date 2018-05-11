package com.smartdata.dataobject;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vishwanath Nahak on 8/10/2017.
 */

public class IncidentImages implements Parcelable {

    /**
     * Incident _id
     */
    @SerializedName("_id")
    public String id;

    /**
     * Incident incident_id
     */
    @SerializedName("incident_id")
    public String incident_id;

    /**
     * Incident image_name
     */
    @SerializedName("image_name")
    public String image_name;

    /**
     * Incident image_path
     */
    @SerializedName("image_path")
    public String image_path;

    protected IncidentImages(Parcel in) {
        id = in.readString();
        incident_id = in.readString();
        image_name = in.readString();
        image_path = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(incident_id);
        dest.writeString(image_name);
        dest.writeString(image_path);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<IncidentImages> CREATOR = new Creator<IncidentImages>() {
        @Override
        public IncidentImages createFromParcel(Parcel in) {
            return new IncidentImages(in);
        }

        @Override
        public IncidentImages[] newArray(int size) {
            return new IncidentImages[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIncident_id() {
        return incident_id;
    }

    public void setIncident_id(String incident_id) {
        this.incident_id = incident_id;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
