package com.smartdata.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.smartdata.activities.DashboardActivity;

import sdei.support.lib.communication.GetWebServiceData;


public class BaseFragment extends Fragment {

    public void setTitle(String title) {
        getActivity().setTitle(title);
    }


    public void commitAddToStack(BaseFragment fragment, String tag) {

        ((DashboardActivity) getActivity()).commitAndAddToBackStack(fragment, tag);
    }

    public void commit(BaseFragment fragment) {

        ((DashboardActivity) getActivity()).commitFragment(fragment);
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    /**
     * The method will return the network availability
     * @param context context of the activity from which the method is called
     * @return true if network is available else false
     */
    public boolean isNetworkAvailable(Context context) {
        GetWebServiceData getWebServiceDataObj = new GetWebServiceData(context);
        return getWebServiceDataObj.isNetworkAvailable();
    }
}
