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

import com.smartdata.activities.SupervisorJobDetailsActivity;
import com.smartdata.adapters.ForemanJobListAdapter;
import com.smartdata.dataobject.AppInstance;
import com.smartdata.dataobject.ForemanJobData;
import com.smartdata.dataobject.ForemanJobDataList;
import com.smartdata.interfaces.ServiceRedirection;
import com.smartdata.managers.SupervisorManager;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.Utility;

import java.util.ArrayList;

public class CrewJobsFragment extends BaseFragment implements ServiceRedirection, SwipeRefreshLayout.OnRefreshListener {

    private final static String TAG = CrewJobsFragment.class.getSimpleName();
    private Context mContext;
    private View mView;
    private Utility mUtilObj = null;
    private SupervisorManager mSupervisorManager;
    private ListView mCrewsJobListView;
    private ArrayList<ForemanJobDataList> mForemanJobDataList;
    private ForemanJobListAdapter mForemanJobListAdapter;

    private LinearLayout mNoJobLinearLayout;
    private ImageView mNoJobsImageView;
    private TextView mNoJobsTextView;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mLoadMoreFooterView;

    private boolean isVisible;
    private boolean isStarted;

    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_crews_jobs, container, false);
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
                getForemanJobList();
            } else {
                mNoJobLinearLayout.setVisibility(View.VISIBLE);
                mNoJobsImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.no_network));
                mNoJobsTextView.setText(getActivity().getResources().getString(R.string.no_network));
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
        mCrewsJobListView = (ListView) mView.findViewById(R.id.crews_job_listView);
        mNoJobLinearLayout = (LinearLayout) mView.findViewById(R.id.no_job_ll);
        mNoJobsImageView = (ImageView) mView.findViewById(R.id.no_jobs_iv);
        mNoJobsTextView = (TextView) mView.findViewById(R.id.no_jobs_tv);

        mLoadMoreFooterView = ((LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.list_footer_item, null, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //mCrewsJobListView.addFooterView(mLoadMoreFooterView);

        mCrewsJobListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intentObj = new Intent(getActivity(), SupervisorJobDetailsActivity.class);
                intentObj.putExtra(Constants.InputTag.JOB_ID, mForemanJobDataList.get(position).getJob_detail().getId());
                startActivity(intentObj);
            }
        });

    }

    @Override
    public void onSuccessRedirection(int taskID) {
        try {
            if (taskID == Constants.TaskID.GET_FOREMAN_JOB_LIST) {
                mUtilObj.stopLoader();
                mSwipeRefreshLayout.setRefreshing(false);
                mForemanJobDataList = AppInstance.foremanJobData.foremanJobDataList;

                if (mForemanJobDataList.size() > 0) {
                    mNoJobLinearLayout.setVisibility(View.GONE);
                    mForemanJobListAdapter = new ForemanJobListAdapter(getActivity(), R.layout.foreman_job_list_item, mForemanJobDataList);
                    mCrewsJobListView.setAdapter(mForemanJobListAdapter);
                } else {
                    mNoJobLinearLayout.setVisibility(View.VISIBLE);
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

    public void getForemanJobList() {
        ForemanJobData foremanJobData = new ForemanJobData();
        mUtilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
        foremanJobData.page = "1";
        foremanJobData.count = "10";
        foremanJobData.users_id = getActivity().getIntent().getExtras().getString(Constants.InputTag.INTENT_KEY_FOREMAN_ID) + "";
        foremanJobData.job_status = "Progress";
        mSupervisorManager.getForemanJobList(foremanJobData);
    }

    @Override
    public void onRefresh() {
        getForemanJobList();
    }
}
