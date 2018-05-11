package com.smartdata.dataobject;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vishwanath Nahak on 8/9/2017.
 */

public class MyDailyDetails implements Parcelable {
    /**
     * job data id
     */
    @SerializedName("_id")
    public String id;
    /**
     * job data job_id
     */
    @SerializedName("job_id")
    public String job_id;
    /**
     * job data client
     */
    @SerializedName("client")
    public String client;

    protected MyDailyDetails(Parcel in) {
        id = in.readString();
        job_id = in.readString();
        client = in.readString();
    }

    public static final Creator<MyDailyDetails> CREATOR = new Creator<MyDailyDetails>() {
        @Override
        public MyDailyDetails createFromParcel(Parcel in) {
            return new MyDailyDetails(in);
        }

        @Override
        public MyDailyDetails[] newArray(int size) {
            return new MyDailyDetails[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(job_id);
        dest.writeString(client);
    }
}
