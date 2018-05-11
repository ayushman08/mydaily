package com.smartdata.dataobject;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vishwanath Nahak on 8/11/2017.
 */

public class DailiesImage implements Parcelable {
    /**
     * job data id
     */
    @SerializedName("_id")
    public String id;
    /**
     * job data mydailies_id
     */
    @SerializedName("mydailies_id")
    public String mydailies_id;

    /**
     * job data image_name
     */
    @SerializedName("image_name")
    public String image_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMydailies_id() {
        return mydailies_id;
    }

    public void setMydailies_id(String mydailies_id) {
        this.mydailies_id = mydailies_id;
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

    /**
     * job data image_path
     */

    @SerializedName("image_path")
    public String image_path;

    protected DailiesImage(Parcel in) {
        id = in.readString();
        mydailies_id = in.readString();
        image_name = in.readString();
        image_path = in.readString();
    }

    public static final Creator<DailiesImage> CREATOR = new Creator<DailiesImage>() {
        @Override
        public DailiesImage createFromParcel(Parcel in) {
            return new DailiesImage(in);
        }

        @Override
        public DailiesImage[] newArray(int size) {
            return new DailiesImage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(mydailies_id);
        dest.writeString(image_name);
        dest.writeString(image_path);
    }
}
