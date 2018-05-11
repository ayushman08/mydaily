package com.smartdata.dataobject;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vishwanath Nahak on 8/18/2017.
 */

public class CrewImageData implements Parcelable {

    /**
     * crews image id
     */
    @SerializedName("_id")
    public String id;

    protected CrewImageData(Parcel in) {
        id = in.readString();
        crews_id = in.readString();
        image_name = in.readString();
        image_path = in.readString();
    }

    public static final Creator<CrewImageData> CREATOR = new Creator<CrewImageData>() {
        @Override
        public CrewImageData createFromParcel(Parcel in) {
            return new CrewImageData(in);
        }

        @Override
        public CrewImageData[] newArray(int size) {
            return new CrewImageData[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCrews_id() {
        return crews_id;
    }

    public void setCrews_id(String crews_id) {
        this.crews_id = crews_id;
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
     * crews image crews_id
     */
    @SerializedName("crews_id")
    public String crews_id;

    /**
     * crews image image_name
     */
    @SerializedName("image_name")
    public String image_name;


    /**
     * crews image image_path
     */
    @SerializedName("image_path")
    public String image_path;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(crews_id);
        dest.writeString(image_name);
        dest.writeString(image_path);
    }
}
