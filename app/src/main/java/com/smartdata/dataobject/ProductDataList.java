package com.smartdata.dataobject;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Anurag Sethi
 * The class handles all the objects associated with the User entity
 */
public class ProductDataList implements Parcelable {

    public ProductDataList() {

    }

    public ProductDataList(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        quantity = in.readString();
    }

    public static final Creator<ProductDataList> CREATOR = new Creator<ProductDataList>() {
        @Override
        public ProductDataList createFromParcel(Parcel in) {
            return new ProductDataList(in);
        }

        @Override
        public ProductDataList[] newArray(int size) {
            return new ProductDataList[size];
        }
    };

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

    /**
     * product ID
     */
    @SerializedName("_id")
    public String id;


    /**
     * product name
     */
    @SerializedName("name")
    public String name;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * product description
     */
    @SerializedName("description")
    public String description;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    /**
     * product quantity
     */
    @SerializedName("quantity")
    public String quantity;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(quantity);
    }
}
