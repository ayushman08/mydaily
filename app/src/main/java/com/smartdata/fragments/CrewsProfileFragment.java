package com.smartdata.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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

import com.smartdata.activities.EditCrewActivity;
import com.smartdata.activities.SupervisorCrewDetailsActivity;
import com.smartdata.adapters.CrewsListAdapter;
import com.smartdata.adapters.ForemanListAdapter;
import com.smartdata.config.PreferenceConfiguration;
import com.smartdata.dataobject.AppInstance;
import com.smartdata.dataobject.CrewsData;
import com.smartdata.dataobject.CrewsDataList;
import com.smartdata.dataobject.ForemanData;
import com.smartdata.dataobject.ForemanDataList;
import com.smartdata.interfaces.ServiceRedirection;
import com.smartdata.managers.SupervisorManager;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.Utility;

import java.util.ArrayList;

public class CrewsProfileFragment extends BaseFragment implements ServiceRedirection, SwipeRefreshLayout.OnRefreshListener {

    private final static String TAG = CrewsProfileFragment.class.getSimpleName();
    private Context mContext;
    private View mView;
    private Utility mUtilObj = null;
    private TextView mJObTextView, mForemanNameTextView, mNoCrewsTextView;
    private SupervisorManager mSupervisorManager;
    private ListView mCrewsListView;
    private ArrayList<CrewsDataList> mCrewsDataList;
    private CrewsListAdapter mCrewsListAdapter;

    private ImageView mNoCrewsImageView;
    private LinearLayout mNoCrewsLinearLayout;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mLoadMoreFooterView;


    private boolean isVisible;
    private boolean isStarted;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_crews_profile, container, false);
        mContext = getActivity();

        initViews();
        bindViews();
        //setHasOptionsMenu(false);
        return mView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisible && isStarted) {
            if (isNetworkAvailable(getActivity())) {
                // call API
                getCrewsList();
            } else {
                mNoCrewsLinearLayout.setVisibility(View.VISIBLE);
                mNoCrewsImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.no_network));
                mNoCrewsTextView.setText(getActivity().getResources().getString(R.string.no_network));
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

    private void bindViews() {
        mNoCrewsLinearLayout = (LinearLayout) mView.findViewById(R.id.no_crews_ll);
        mNoCrewsImageView = (ImageView) mView.findViewById(R.id.no_crews_iv);
        mNoCrewsTextView = (TextView) mView.findViewById(R.id.no_crews_tv);


        mJObTextView = (TextView) mView.findViewById(R.id.jobtv);
        mForemanNameTextView = (TextView) mView.findViewById(R.id.nametv);
        mForemanNameTextView.setText(getActivity().getIntent().getExtras().getString(Constants.InputTag.INTENT_KEY_FOREMAN_NAME) + "");
        mJObTextView.setText(getActivity().getIntent().getExtras().getString(Constants.InputTag.INTENT_KEY_FOREMAN_JOB) + "");

        mCrewsListView = (ListView) mView.findViewById(R.id.crews_listView);


        mCrewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intentObj = new Intent(getActivity(), EditCrewActivity.class);
                intentObj.putExtra(Constants.RequestCodes.REQ_CODE_CREW_DATA, mCrewsDataList.get(position));
                if (mCrewsDataList.get(position).getCrewImageDatas() != null) {
                    intentObj.putExtra(Constants.RequestCodes.REQ_CODE_CREW_IMAGE_DATA, mCrewsDataList.get(position).getCrewImageDatas());
                }
                intentObj.putExtra(Constants.RequestCodes.REQ_CODE_CREW_DETAILS, Constants.RequestCodes.REQ_CODE_DETAIL_CREW);
                startActivity(intentObj);
            }
        });

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
        if (isNetworkAvailable(getActivity())) {
            // call API
            getCrewsList();
        } else {
            mNoCrewsLinearLayout.setVisibility(View.VISIBLE);
            mNoCrewsImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.no_network));
            mNoCrewsTextView.setText(getActivity().getResources().getString(R.string.no_network));
        }

    }


    private void initViews() {
        mUtilObj = new Utility(getActivity());
        mSupervisorManager = new SupervisorManager(getActivity(), this);

    }

    @Override
    public void onSuccessRedirection(int taskID) {
        try {
            if (taskID == Constants.TaskID.GET_CREWS_LIST) {
                mUtilObj.stopLoader();
                mSwipeRefreshLayout.setRefreshing(false);
                mCrewsDataList = AppInstance.crewsData.crewsDataList;
                if (mCrewsDataList.size() > 0) {
                    mNoCrewsLinearLayout.setVisibility(View.GONE);
                    mCrewsListAdapter = new CrewsListAdapter(getActivity(), R.layout.foreman_list_item, mCrewsDataList);
                    mCrewsListView.setAdapter(mCrewsListAdapter);
                   // mJObTextView.setText(mCrewsDataList.get(0).getFirstname() + " " + mCrewsDataList.get(0).getLastname());
                } else {
                    mNoCrewsLinearLayout.setVisibility(View.VISIBLE);
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

    private void getCrewsList() {
        CrewsData crewsData = new CrewsData();
        mUtilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
        crewsData.page = "1";
        crewsData.count = "50";
        crewsData.parent_id = getActivity().getIntent().getExtras().getString(Constants.InputTag.INTENT_KEY_FOREMAN_ID) + "";
        mSupervisorManager.getCrewsList(crewsData);
    }

    @Override
    public void onRefresh() {
        getCrewsList();
    }
}
