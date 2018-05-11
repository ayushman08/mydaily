package com.smartdata.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.smartdata.activities.DashboardActivity;
import com.smartdata.activities.NewIncidentDetailsActivity;
import com.smartdata.activities.SettingActivity;
import com.smartdata.activities.UpdateDailyActivity;
import com.smartdata.adapters.CrewsListAdapter;
import com.smartdata.adapters.IncidentListAdapter;
import com.smartdata.config.PreferenceConfiguration;
import com.smartdata.dataobject.AppInstance;
import com.smartdata.dataobject.CrewsData;
import com.smartdata.dataobject.IncidentData;
import com.smartdata.dataobject.IncidentDataList;
import com.smartdata.interfaces.DialogActionCallback;
import com.smartdata.interfaces.ServiceRedirection;
import com.smartdata.managers.SupervisorManager;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.Utility;

import java.util.ArrayList;

public class IncidentReportsFragment extends BaseFragment implements ServiceRedirection, SwipeRefreshLayout.OnRefreshListener {

    private final static String TAG = IncidentReportsFragment.class.getSimpleName();
    private Context mContext;
    private View mView;
    private Utility mUtilObj = null;
    private FloatingActionButton mAddIncident;
    private ListView mIncidentListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mLoadMoreFooterView;
    private SupervisorManager mSupervisorManager;

    private LinearLayout mNoIncidentsLinearLayout;
    private ImageView mNoIncidentsImageView;
    private TextView mNoIncidentsTextView;
    private ArrayList<IncidentDataList> mIncidentDataList;
    private IncidentListAdapter mIncidentListAdapter;
    private int mPosition;

    //custom toolbar components
    private Toolbar mToolbar;
    private ImageView mSettingImageView, mClearImageView, mSearchImageView;
    private LinearLayout mSearchLinearLayout;
    private TextView mTitleTextView;
    private EditText mSearchIncidentEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_incident_reports, container, false);
        mContext = getActivity();
        setToolbar();
        initViews();
        bindViews();
        return mView;
    }

    private void bindViews() {
        mAddIncident = (FloatingActionButton) mView.findViewById(R.id.add_incident);
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

        if (isNetworkAvailable(getActivity())) {
            // call API
            getIncidentList();
        } else {
            mNoIncidentsLinearLayout.setVisibility(View.VISIBLE);
            mNoIncidentsImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.no_network));
            mNoIncidentsTextView.setText(getActivity().getResources().getString(R.string.no_network));
        }


        mIncidentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intentObj = new Intent(getActivity(), NewIncidentDetailsActivity.class);
                intentObj.putExtra(Constants.RequestCodes.INCIDENT_DETAILS, mIncidentDataList.get(position));
                intentObj.putExtra(Constants.RequestCodes.INCIDENT_IS_DRAFT, mIncidentDataList.get(position).is_draft());
                intentObj.putExtra(Constants.RequestCodes.INCIDENT_IMAGES, mIncidentDataList.get(position).getIncidentImages());
                intentObj.putExtra(Constants.RequestCodes.INCIDENT_JOB_DETAILS, mIncidentDataList.get(position).getJobDetailsData());

                startActivityForResult(intentObj, Constants.RequestCodes.REQ_CODE_EDIT_INCIDENT);

            }
        });

        mIncidentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                view.setSelected(true);
                Utility.showAlertDialog(getActivity(), 0, R.string.incident_delete_confirmation_message, R.string.delete, R.string.cancel, new DialogActionCallback() {
                    @Override
                    public void doOnPositive() {
                        deleteIncident(mIncidentDataList.get(position).getId());
                        mPosition = position;

                    }

                    @Override
                    public void doOnNegative() {

                    }
                });

                return true;
            }
        });


        mAddIncident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentObj = new Intent(getActivity(), NewIncidentDetailsActivity.class);
                startActivityForResult(intentObj, Constants.RequestCodes.REQ_CODE_NEW_INCIDENT);
            }
        });
    }

    private void initViews() {
        mUtilObj = new Utility(getActivity());
        mSupervisorManager = new SupervisorManager(getActivity(), this);
    }

    private void getIncidentList() {
        IncidentData incidentData = new IncidentData();
        mUtilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
        incidentData.page = "1";
        incidentData.count = "50";
        incidentData.user_id = PreferenceConfiguration.getUserID(getActivity());
        mSupervisorManager.getIncidentList(incidentData);
    }

    @Override
    public void onSuccessRedirection(int taskID) {
        try {
            mUtilObj.stopLoader();
            if (taskID == Constants.TaskID.GET_INCIDENT_LIST) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (AppInstance.incidentData.code == Constants.ResponseCodes.RESPONSE_SUCCESS) {
                    mIncidentDataList = AppInstance.incidentData.incidentsDataList;
                    if (mIncidentDataList.size() > 0) {
                        mNoIncidentsLinearLayout.setVisibility(View.GONE);
                        mIncidentListAdapter = new IncidentListAdapter(getActivity(), R.layout.incident_list_item, mIncidentDataList);
                        mIncidentListView.setAdapter(mIncidentListAdapter);
                    } else {
                        mNoIncidentsLinearLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    mUtilObj.showSnackBarAlert(mView.findViewById(R.id.container), AppInstance.incidentData.message, false);
                }
            } else if (taskID == Constants.TaskID.DELETE_INCIDENT) {
                mIncidentListAdapter.remove(mIncidentListAdapter.getItem(mPosition));
                mIncidentListView.invalidateViews();
                mIncidentListAdapter.notifyDataSetChanged();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.RequestCodes.REQ_CODE_NEW_INCIDENT:
                getIncidentList();
                break;
            case Constants.RequestCodes.REQ_CODE_EDIT_INCIDENT:
                getIncidentList();
                break;
        }
    }

    public void deleteIncident(String id) {
        mUtilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
        mSupervisorManager.deleteIncident(id);
    }


    @Override
    public void onRefresh() {
        if (isNetworkAvailable(getActivity())) {
            // call API
            getIncidentList();
        } else {
            mNoIncidentsLinearLayout.setVisibility(View.VISIBLE);
            mNoIncidentsImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.no_network));
            mNoIncidentsTextView.setText(getActivity().getResources().getString(R.string.no_network));
        }

    }

    public void setToolbar() {
        mToolbar = (Toolbar) mView.findViewById(R.id.search_actionbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mSettingImageView = (ImageView) mView.findViewById(R.id.action_settings);
        mSearchImageView = (ImageView) mView.findViewById(R.id.search_view);
        mClearImageView = (ImageView) mView.findViewById(R.id.clear_view);
        mSearchLinearLayout = (LinearLayout) mView.findViewById(R.id.searchLinear);
        mTitleTextView = (TextView) mView.findViewById(R.id.title);
        mTitleTextView.setText(getActivity().getResources().getString(R.string.incident_reports));

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

                if (mIncidentListAdapter != null) {
                    mIncidentListAdapter.getFilter().filter(s.toString());
                    /*if (mIncidentListAdapter.isEmpty()) {
                        mNoIncidentsLinearLayout.setVisibility(View.VISIBLE);
                    }else{
                        mNoIncidentsLinearLayout.setVisibility(View.GONE);
                    }*/
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
