package com.smartdata.dataobject;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Vishwanath Nahak on 7/26/2017.
 */

class ForemanJobBillingInfo {


    /**
     * billable item list
     */
    @SerializedName("billable_items")
    public ArrayList<ProductDataList> billableItemList;

    /**
     * billing email
     */
    @SerializedName("billing_email")
    public String billing_email;

    public ArrayList<ProductDataList> getBillableItemList() {
        return billableItemList;
    }

    public void setBillableItemList(ArrayList<ProductDataList> billableItemList) {
        this.billableItemList = billableItemList;
    }

    public String getBilling_email() {
        return billing_email;
    }

    public void setBilling_email(String billing_email) {
        this.billing_email = billing_email;
    }

    public String getClient_billing_contact() {
        return client_billing_contact;
    }

    public void setClient_billing_contact(String client_billing_contact) {
        this.client_billing_contact = client_billing_contact;
    }

    /**

     * client billing contact
     */
    @SerializedName("client_billing_contact")
    public String client_billing_contact;

}
