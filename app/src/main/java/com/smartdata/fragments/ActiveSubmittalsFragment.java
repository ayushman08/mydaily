package com.smartdata.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.smartdata.activities.SubmittalDetailsActivity;
import com.smartdata.adapters.SubmittalAdapter;
import com.smartdata.config.PreferenceConfiguration;
import com.smartdata.dataobject.AppInstance;
import com.smartdata.dataobject.MyDailyData;
import com.smartdata.dataobject.MyDailyDataList;
import com.smartdata.interfaces.SearchInterface;
import com.smartdata.interfaces.ServiceRedirection;
import com.smartdata.managers.SupervisorManager;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.Utility;

import java.util.ArrayList;

public class ActiveSubmittalsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, ServiceRedirection {

    private final static String TAG = ActiveSubmittalsFragment.class.getSimpleName();
    private Context mContext;
    private View mView;
    private Utility mUtilObj = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mLoadMoreFooterView;
    private ListView mActiveSubmittalsListView;
    private LinearLayout mNoSubmittalsLinearLayout;
    private ImageView mNoSubmittalsImageView;
    private TextView mNoSubmittalsTextView;
    private SupervisorManager mSupervisorManager;

    private SubmittalAdapter mSubmittalAdapter;
    private ArrayList<MyDailyDataList> mMyDailyDataList;

    private boolean isVisible;
    private boolean isStarted;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_active_submittals, container, false);
        mContext = getActivity();

        initViews();
        bindViews();
        return mView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisible && isStarted) {
            if (isNetworkAvailable(getActivity())) {
                // call API
                getActiveDailies();
            } else {
                mNoSubmittalsLinearLayout.setVisibility(View.VISIBLE);
                mNoSubmittalsImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.no_network));
                mNoSubmittalsTextView.setText(getActivity().getResources().getString(R.string.no_network));
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        isStarted = true;
        // if (isVisible)
    }

    @Override
    public void onStop() {
        super.onStop();
        isStarted = false;
    }

    private void initViews() {
        mUtilObj = new Utility(getActivity());
        mSupervisorManager = new SupervisorManager(getActivity(), this);
    }

    private void bindViews() {
        mNoSubmittalsLinearLayout = (LinearLayout) mView.findViewById(R.id.no_submittals_ll);
        mNoSubmittalsImageView = (ImageView) mView.findViewById(R.id.no_submittals_iv);
        mNoSubmittalsTextView = (TextView) mView.findViewById(R.id.no_submittals_tv);

        mActiveSubmittalsListView = (ListView) mView.findViewById(R.id.active_submittal_listView);
        mLoadMoreFooterView = ((LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.list_footer_item, null, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //mAcceptSubmittalsListView.addFooterView(mLoadMoreFooterView);


        mActiveSubmittalsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intentObj = new Intent(getActivity(), SubmittalDetailsActivity.class);
                intentObj.putExtra(Constants.RequestCodes.REQ_CODE_DAILY_DATA, mMyDailyDataList.get(position));
                intentObj.putExtra(Constants.RequestCodes.REQ_CODE_SUPERVISOR_DAILY_DATA, mMyDailyDataList.get(position).getSupervisorDetails());
                intentObj.putExtra(Constants.RequestCodes.REQ_CODE_CREW_DAILY_DATA, mMyDailyDataList.get(position).getCrews_details());
                if (mMyDailyDataList.get(position).getBillableItemses() != null && mMyDailyDataList.get(position).getBillableItemses().size() > 0) {
                    intentObj.putExtra(Constants.RequestCodes.REQ_CODE_BILLABLE_DAILY_DATA, mMyDailyDataList.get(position).getBillableItemses());

                }
                if (mMyDailyDataList.get(position).getDailiesImageArrayList() != null && mMyDailyDataList.get(position).getDailiesImageArrayList().size() > 0) {
                    intentObj.putExtra(Constants.RequestCodes.REQ_CODE_IMAGE_DAILY_DATA, mMyDailyDataList.get(position).getDailiesImageArrayList());
                }
                intentObj.putExtra(Constants.RequestCodes.REQ_CODE_JOB_DAILY_DATA, mMyDailyDataList.get(position).getMyDailyDetails());
                intentObj.putExtra(Constants.RequestCodes.REQ_CODE_SUBMITTALS, getActivity().getString(R.string.daily));
                startActivityForResult(intentObj, Constants.RequestCodes.REQ_CODE_REJECT_DAILY);
            }
        });

        if (isNetworkAvailable(getActivity())) {
            // call API
            getActiveDailies();
        } else {
            mNoSubmittalsLinearLayout.setVisibility(View.VISIBLE);
            mNoSubmittalsImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.no_network));
            mNoSubmittalsTextView.setText(getActivity().getResources().getString(R.string.no_network));
        }

    }

    private void getActiveDailies() {
        MyDailyData myDailyData = new MyDailyData();
        mUtilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
        myDailyData.page = "1";
        myDailyData.count = "50";
        myDailyData.status = Constants.RequestCodes.REQ_CODE_DAILY_ACTIVE;
        myDailyData.supervisor_id = PreferenceConfiguration.getUserID(getActivity());
        myDailyData.parent_id  = PreferenceConfiguration.getParentID(getActivity());
        mSupervisorManager.getMyDailies(myDailyData);
    }

    @Override
    public void onRefresh() {
        if (isNetworkAvailable(getActivity())) {
            // call API
            getActiveDailies();
        } else {
            mNoSubmittalsLinearLayout.setVisibility(View.VISIBLE);
            mNoSubmittalsImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.no_network));
            mNoSubmittalsTextView.setText(getActivity().getResources().getString(R.string.no_network));
        }
    }

    @Override
    public void onSuccessRedirection(int taskID) {
        try {
            mUtilObj.stopLoader();
            mSwipeRefreshLayout.setRefreshing(false);
            if (taskID == Constants.TaskID.GET_MY_DAILIES) {

                mMyDailyDataList = AppInstance.myDailyData.myDailyDataList;
                if (mMyDailyDataList.size() > 0) {
                    mNoSubmittalsLinearLayout.setVisibility(View.GONE);
                    mSubmittalAdapter = new SubmittalAdapter(getActivity(), R.layout.foreman_list_item, mMyDailyDataList);
                    mActiveSubmittalsListView.setAdapter(mSubmittalAdapter);
                    mSubmittalAdapter.notifyDataSetChanged();
                } else {
                    mActiveSubmittalsListView.setAdapter(null);
                    mNoSubmittalsLinearLayout.setVisibility(View.VISIBLE);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailureRedirection(String errorMessage) {
        try {
            mUtilObj.stopLoader();
            mSwipeRefreshLayout.setRefreshing(false);
            //Snackbar.make(mView.findViewById(R.id.container), errorMessage, Snackbar.LENGTH_SHORT).show();
            mUtilObj.showSnackBarAlert(mView.findViewById(R.id.container), errorMessage, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.RequestCodes.REQ_CODE_REJECT_DAILY:
                getActiveDailies();
                break;
        }
    }


    public void onSearch(String searchKeywords) {
        if (mSubmittalAdapter != null) {
            mSubmittalAdapter.getFilter().filter(searchKeywords);
                    /*if (mIncidentListAdapter.isEmpty()) {
                        mNoIncidentsLinearLayout.setVisibility(View.VISIBLE);
                    }else{
                        mNoIncidentsLinearLayout.setVisibility(View.GONE);
                    }*/
        }
    }
}
