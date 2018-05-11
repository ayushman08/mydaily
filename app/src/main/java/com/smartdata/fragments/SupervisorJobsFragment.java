package com.smartdata.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.smartdata.activities.SettingActivity;
import com.smartdata.activities.SupervisorAddNewJobActivity;
import com.smartdata.adapters.SupervisorJobListAdapter;
import com.smartdata.config.PreferenceConfiguration;
import com.smartdata.dataobject.AppInstance;
import com.smartdata.dataobject.JobData;
import com.smartdata.dataobject.JobDataList;
import com.smartdata.interfaces.DialogActionCallback;
import com.smartdata.interfaces.ServiceRedirection;
import com.smartdata.managers.SupervisorManager;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.Utility;

import java.util.ArrayList;

public class SupervisorJobsFragment extends BaseFragment implements ServiceRedirection, SwipeRefreshLayout.OnRefreshListener {

    private final static String TAG = SupervisorJobsFragment.class.getSimpleName();
    private View mView;
    private Utility mUtilObj = null;
    private SupervisorManager mSupervisorManager;
    private ArrayList<JobDataList> mJobDataArrayList;
    private ArrayList<JobDataList> mTempJobDataLists;
    private ListView mJobListView;
    private SupervisorJobListAdapter mSupervisorJobListAdapter;
    private FloatingActionButton mAddNewJob;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mLoadMoreFooterView;
    private LinearLayout mNoJobsLinearLayout;
    private ImageView mNoJobsImageView;
    private TextView mNoJobsTextView;
    private int mPosition;

    //custom toolbar components
    private Toolbar mToolbar;
    private ImageView mSettingImageView, mClearImageView, mSearchImageView;
    private LinearLayout mSearchLinearLayout;
    private TextView mTitleTextView;
    private EditText mSearchIncidentEditText;

    //load more components
    private int mPage = 1;
    private int mLimit = 10;
    private int mLastFirstVisibleItem = 0;
    private Handler mHandler;
    private boolean isLoadMore = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_supervisor_jobs, container, false);
        setToolbar();
        initViews();
        bindViews();
        return mView;
    }

    private void bindViews() {
        mNoJobsLinearLayout = (LinearLayout) mView.findViewById(R.id.no_job_ll);
        mNoJobsImageView = (ImageView) mView.findViewById(R.id.no_job_iv);
        mNoJobsTextView = (TextView) mView.findViewById(R.id.no_job_tv);

        mJobListView = (ListView) mView.findViewById(R.id.job_listView);
        mJobListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Intent intentObj = new Intent(getActivity(), SupervisorJobDetailsActivity.class);

                Intent intentObj = new Intent(getActivity(), SupervisorAddNewJobActivity.class);
                intentObj.putExtra(Constants.InputTag.JOB_ID, mJobDataArrayList.get(position).getJobAssignTo().getId());
                startActivityForResult(intentObj, Constants.RequestCodes.REQ_CODE_JOB);
            }
        });

        mJobListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                view.setSelected(true);
                Utility.showAlertDialog(getActivity(), 0, R.string.job_delete_confirmation_message, R.string.delete, R.string.cancel, new DialogActionCallback() {
                    @Override
                    public void doOnPositive() {
                        deleteJob(mJobDataArrayList.get(position).getJobAssignTo().getId());
                        mPosition = position;

                    }

                    @Override
                    public void doOnNegative() {

                    }
                });

                return true;
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
        mJobListView.addFooterView(mLoadMoreFooterView);
        mLoadMoreFooterView.setVisibility(View.GONE);

        mAddNewJob = (FloatingActionButton) mView.findViewById(R.id.add_new_job);
        mAddNewJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentObj = new Intent(getActivity(), SupervisorAddNewJobActivity.class);
                startActivityForResult(intentObj, Constants.RequestCodes.REQ_CODE_JOB);
            }
        });

        if (isNetworkAvailable(getActivity())) {
            // call API
            getJobList(String.valueOf(mPage), isLoadMore);
        } else {
            mNoJobsLinearLayout.setVisibility(View.VISIBLE);
            mNoJobsImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.no_network));
            mNoJobsTextView.setText(getActivity().getResources().getString(R.string.no_network));
        }


        mJobListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                final int lastItem = firstVisibleItem + visibleItemCount;
                if (totalItemCount > 0 && lastItem == totalItemCount && totalItemCount % 11 == 0) {
                    mPage += 1;
                    isLoadMore = true;
                    mJobListView.addFooterView(mLoadMoreFooterView);
                    mLoadMoreFooterView.setVisibility(View.VISIBLE);
                    mLastFirstVisibleItem = firstVisibleItem;
                    getJobList(String.valueOf(mPage), isLoadMore);

                } else {
                    mLoadMoreFooterView.setVisibility(View.GONE);
                    mJobListView.removeFooterView(mLoadMoreFooterView);
                }


            }
        });


    }


    private void initViews() {
        mUtilObj = new Utility(getActivity());
        mHandler = new Handler();
        mSupervisorManager = new SupervisorManager(getActivity(), this);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the options menu from XML
        //menu.clear();
        //inflater.inflate(R.menu.menu_setting, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                //Snackbar.make(mView.findViewById(R.id.container), "Active submittals Search menu is in progress!", Snackbar.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getJobList(String page, boolean isLoadMore) {
        JobData jobData = new JobData();
        if (!isLoadMore) {
            mUtilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
        }
        jobData.page = page;
        jobData.count = String.valueOf(mLimit);
        jobData.users_id = PreferenceConfiguration.getUserID(getActivity());
        mSupervisorManager.getJobList(jobData);
    }


    @Override
    public void onSuccessRedirection(int taskID) {
        try {

            if (taskID == Constants.TaskID.GET_JOB_LIST) {

                if (!isLoadMore) {
                    mUtilObj.stopLoader();
                    isLoadMore = false;
                } else {
                    mLoadMoreFooterView.setVisibility(View.GONE);
                    mJobListView.removeFooterView(mLoadMoreFooterView);
                }
                mTempJobDataLists = new ArrayList<>();
                mTempJobDataLists = AppInstance.jobData.jobDataList;

                if (mTempJobDataLists.size() > 0) {
                    if (mPage == 1) {
                        mJobDataArrayList = mTempJobDataLists;
                        mNoJobsLinearLayout.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(false);
                        mSupervisorJobListAdapter = new SupervisorJobListAdapter(getActivity(), R.layout.foreman_list_item, mJobDataArrayList);
                        mJobListView.setAdapter(mSupervisorJobListAdapter);
                        mSwipeRefreshLayout.setRefreshing(false);
                    } else {
                        if (mJobDataArrayList != null && mJobDataArrayList.size() > 0) {
                            mJobDataArrayList.addAll(mTempJobDataLists);
                            mSupervisorJobListAdapter = new SupervisorJobListAdapter(getActivity(), R.layout.foreman_list_item, mJobDataArrayList);
                            mJobListView.setAdapter(mSupervisorJobListAdapter);
                            mJobListView.setSelection(mLastFirstVisibleItem);
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mSupervisorJobListAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                }






               /* mJobDataArrayList = AppInstance.jobData.jobDataList;
                if (mJobDataArrayList.size() > 0) {
                    mNoJobsLinearLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mSupervisorJobListAdapter = new SupervisorJobListAdapter(getActivity(), R.layout.foreman_list_item, mJobDataArrayList);
                    mJobListView.setAdapter(mSupervisorJobListAdapter);
                } else {
                    mNoJobsLinearLayout.setVisibility(View.VISIBLE);
                }*/
            } else if (taskID == Constants.TaskID.DELETE_JOB) {
                mUtilObj.stopLoader();
                mSupervisorJobListAdapter.remove(mSupervisorJobListAdapter.getItem(mPosition));
                mJobListView.invalidateViews();
                mSupervisorJobListAdapter.notifyDataSetChanged();
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
            mLoadMoreFooterView.setVisibility(View.GONE);
            mJobListView.removeFooterView(mLoadMoreFooterView);
            //Snackbar.make(mView.findViewById(R.id.container), errorMessage, Snackbar.LENGTH_SHORT).show();
            mUtilObj.showSnackBarAlert(mView.findViewById(R.id.container), errorMessage, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.RequestCodes.REQ_CODE_JOB:
                getJobList(String.valueOf(mPage), isLoadMore);
                break;
        }
    }

    public void deleteJob(String id) {
        mUtilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
        mSupervisorManager.deleteJob(id);
    }

    @Override
    public void onRefresh() {
        if (mJobDataArrayList != null && mJobDataArrayList.size() > 0) {
            mJobDataArrayList.clear();
        }
        isLoadMore = false;
        mPage = 1;
        getJobList(String.valueOf(mPage), isLoadMore);
    }

    public void setToolbar() {
        mToolbar = (Toolbar) mView.findViewById(R.id.search_actionbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mSettingImageView = (ImageView) mView.findViewById(R.id.action_settings);
        mSearchImageView = (ImageView) mView.findViewById(R.id.search_view);
        mClearImageView = (ImageView) mView.findViewById(R.id.clear_view);
        mSearchLinearLayout = (LinearLayout) mView.findViewById(R.id.searchLinear);
        mTitleTextView = (TextView) mView.findViewById(R.id.title);
        mTitleTextView.setText(getActivity().getResources().getString(R.string.my_jobs));
        mSearchIncidentEditText = (EditText) mView.findViewById(R.id.search_et);
        mSearchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mToolbar.setVisibility(View.GONE);
                mSearchLinearLayout.setVisibility(View.VISIBLE);
                mSearchIncidentEditText.requestFocus();
                mUtilObj.showVirtualKeyboard(getActivity());
            }
        });

        mClearImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchIncidentEditText.getText().toString().isEmpty()) {
                    mSearchLinearLayout.setVisibility(View.GONE);
                    mToolbar.setVisibility(View.VISIBLE);
                    mSearchIncidentEditText.clearFocus();
                    mUtilObj.hiddenInputMethod(getActivity());
                } else {
                    mSearchIncidentEditText.setText("");
                }
            }
        });

        mSettingImageView.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                Intent intentObj = new Intent(getActivity(), SettingActivity.class);
                startActivity(intentObj);
            }
        });

        mSearchIncidentEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                if (mSupervisorJobListAdapter != null) {
                    mSupervisorJobListAdapter.getFilter().filter(s.toString());
                    if (mSupervisorJobListAdapter.getCount() > 0) {
                        mNoJobsLinearLayout.setVisibility(View.GONE);
                    } else {
                        mNoJobsLinearLayout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }
}
