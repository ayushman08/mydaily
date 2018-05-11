package com.smartdata.dataobject;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Vishwanath Nahak on 8/11/2017.
 */

public class BillableInfo {
    /**
     * job status
     */
    @SerializedName("billable_items")
    public ArrayList<ProductDataList> billableItems;

    public ArrayList<ProductDataList> getBillableItems() {
        return billableItems;
    }

    public void setBillableItems(ArrayList<ProductDataList> billableItems) {
        this.billableItems = billableItems;
    }
}
