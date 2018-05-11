package com.smartdata.dataobject;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Anurag Sethi
 * The class handles all the objects associated with the User entity
 */
public class ProductData {

    /**
     * product status code
     */
    @SerializedName("code")
    public int code;


    /**
     * product status message
     */
    @SerializedName("message")
    public String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<ProductDataList> getProductDataList() {
        return productDataList;
    }

    public void setProductDataList(ArrayList<ProductDataList> productDataList) {
        this.productDataList = productDataList;
    }

    /**
     * crews status message
     */
    @SerializedName("data")
    public ArrayList<ProductDataList> productDataList;


}
