package com.smartdata.dataobject;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vishwanath Nahak on 7/19/2017.
 */

public class BillableItems implements Parcelable {
    /**
     * job id
     */
    @SerializedName("_id")
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * job Billable Items name
     */
    @SerializedName("name")
    public String name;
    /**
     * job Billable Items description
     */
    @SerializedName("description")
    public String description;

    /**
     * job Billable Items quantity
     */
    @SerializedName("quantity")
    public String quantity;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @SuppressWarnings("rawtypes")
    public static final Creator CREATOR = new Creator() {
        public BillableItems createFromParcel(Parcel in) {
            return new BillableItems(in);
        }

        public BillableItems[] newArray(int size) {
            return new BillableItems[size];
        }
    };

    public BillableItems(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(quantity);
    }

    public void readFromParcel(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.quantity = in.readString();
    }

}
