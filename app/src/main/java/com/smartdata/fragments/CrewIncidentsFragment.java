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

import com.smartdata.activities.IncidentDetailsActivity;
import com.smartdata.activities.NewIncidentDetailsActivity;
import com.smartdata.adapters.IncidentListAdapter;
import com.smartdata.config.PreferenceConfiguration;
import com.smartdata.dataobject.AppInstance;
import com.smartdata.dataobject.IncidentData;
import com.smartdata.dataobject.IncidentDataList;
import com.smartdata.interfaces.ServiceRedirection;
import com.smartdata.managers.SupervisorManager;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.Utility;

import java.util.ArrayList;

public class CrewIncidentsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, ServiceRedirection {

    private final static String TAG = CrewIncidentsFragment.class.getSimpleName();
    private Context mContext;
    private View mView;
    private Utility mUtilObj = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mLoadMoreFooterView;
    private ListView mIncidentListView;
    private LinearLayout mNoIncidentsLinearLayout;
    private ImageView mNoIncidentsImageView;
    private TextView mNoIncidentsTextView;
    private SupervisorManager mSupervisorManager;
    private IncidentListAdapter mIncidentListAdapter;
    private ArrayList<IncidentDataList> mIncidentDataList;

    private boolean isVisible;
    private boolean isStarted;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_crews_incidents, container, false);
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
                getIncidentList();
            } else {
                mNoIncidentsLinearLayout.setVisibility(View.VISIBLE);
                mNoIncidentsImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.no_network));
                mNoIncidentsTextView.setText(getActivity().getResources().getString(R.string.no_network));
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        isStarted = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isStarted = false;
    }

    private void bindViews() {

        mIncidentListView = (ListView) mView.findViewById(R.id.incidents_listView);

        mNoIncidentsLinearLayout = (LinearLayout) mView.findViewById(R.id.no_incident_ll);
        mNoIncidentsImageView = (ImageView) mView.findViewById(R.id.no_incident_iv);
        mNoIncidentsTextView = (TextView) mView.findViewById(R.id.no_incident_tv);

        mLoadMoreFooterView = ((LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.list_footer_item, null, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //mForemanActiveJobListView.addFooterView(mLoadMoreFooterView);

        mIncidentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentObj = new Intent(getActivity(), IncidentDetailsActivity.class);
                intentObj.putExtra(Constants.RequestCodes.INCIDENT_DETAILS, mIncidentDataList.get(position));
                intentObj.putExtra(Constants.RequestCodes.INCIDENT_IMAGES, mIncidentDataList.get(position).getIncidentImages());
                intentObj.putExtra(Constants.RequestCodes.INCIDENT_JOB_DETAILS, mIncidentDataList.get(position).getJobDetailsData());
                startActivity(intentObj);
            }
        });

        if (isNetworkAvailable(getActivity())) {
            // call API
            getIncidentList();
        } else {
            mNoIncidentsLinearLayout.setVisibility(View.VISIBLE);
            mNoIncidentsImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.no_network));
            mNoIncidentsTextView.setText(getActivity().getResources().getString(R.string.no_network));
        }
    }

    private void initViews() {
        mUtilObj = new Utility(getActivity());
        mSupervisorManager = new SupervisorManager(getActivity(), this);
    }

    @Override
    public void onRefresh() {
        getIncidentList();
    }

    private void getIncidentList() {
        IncidentData incidentData = new IncidentData();
        mUtilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
        incidentData.page = "1";
        incidentData.count = "50";
        incidentData.user_id = getActivity().getIntent().getExtras().getString(Constants.InputTag.INTENT_KEY_FOREMAN_ID);
        mSupervisorManager.getIncidentList(incidentData);
    }

    @Override
    public void onSuccessRedirection(int taskID) {
        try {
            if (taskID == Constants.TaskID.GET_INCIDENT_LIST) {
                mUtilObj.stopLoader();
                mSwipeRefreshLayout.setRefreshing(false);
                mIncidentDataList = AppInstance.incidentData.incidentsDataList;
                if (mIncidentDataList.size() > 0) {
                    mNoIncidentsLinearLayout.setVisibility(View.GONE);
                    mIncidentListAdapter = new IncidentListAdapter(getActivity(), R.layout.incident_list_item, mIncidentDataList);
                    mIncidentListView.setAdapter(mIncidentListAdapter);
                } else {
                    mNoIncidentsLinearLayout.setVisibility(View.VISIBLE);
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
            mUtilObj.showSnackBarAlert(mView.findViewById(R.id.container), errorMessage, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
