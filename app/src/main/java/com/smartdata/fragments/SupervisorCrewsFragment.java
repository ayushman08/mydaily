package com.smartdata.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.smartdata.activities.SettingActivity;
import com.smartdata.activities.SupervisorCrewDetailsActivity;
import com.smartdata.adapters.ForemanListAdapter;
import com.smartdata.config.PreferenceConfiguration;
import com.smartdata.dataobject.AppInstance;
import com.smartdata.dataobject.ForemanData;
import com.smartdata.dataobject.ForemanDataList;
import com.smartdata.interfaces.ServiceRedirection;
import com.smartdata.managers.SupervisorManager;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.Utility;

import java.util.ArrayList;

public class SupervisorCrewsFragment extends BaseFragment implements ServiceRedirection, SwipeRefreshLayout.OnRefreshListener {

    private final static String TAG = SupervisorCrewsFragment.class.getSimpleName();
    private Context mContext;
    private View mView;
    private Utility mUtilObj = null;
    private ListView mCrewsListView;
    private ForemanListAdapter mForemanListAdapter;
    private ArrayList<ForemanDataList> mForemanArrayList;
    private SupervisorManager mSupervisorManager;
    private AutoCompleteTextView searchEditText;
    private SearchView searchView;
    private ForemanDataList mForemanDataList;
    private boolean isSearch = false;
    private LinearLayout mNoCrewsLinearLayout;
    private ImageView mNoCrewsImageView;
    private TextView mNoCrewsTextView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mLoadMoreFooterView;

    //custom toolbar components
    private Toolbar mToolbar;
    private ImageView mSettingImageView, mClearImageView, mSearchImageView;
    private LinearLayout mSearchLinearLayout;
    private TextView mTitleTextView;
    private EditText mSearchIncidentEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_my_crews, container, false);
        mContext = getActivity();

        setToolbar();
        initViews();
        bindViews();
        return mView;
    }


    private void initViews() {
        mUtilObj = new Utility(getActivity());
        mSupervisorManager = new SupervisorManager(getActivity(), this);
        mForemanArrayList = new ArrayList<>();
    }

    private void bindViews() {
        mNoCrewsLinearLayout = (LinearLayout) mView.findViewById(R.id.no_crews_ll);
        mNoCrewsImageView = (ImageView) mView.findViewById(R.id.no_crews_iv);
        mNoCrewsTextView = (TextView) mView.findViewById(R.id.no_crews_tv);

        mCrewsListView = (ListView) mView.findViewById(R.id.crews_listView);
        mCrewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mForemanDataList = mForemanArrayList.get(position);
                Intent intentObj = new Intent(getActivity(), SupervisorCrewDetailsActivity.class);
                intentObj.putExtra(Constants.InputTag.INTENT_KEY_FOREMAN_NAME, mForemanDataList.firstname + " " + mForemanDataList.getLastname());
                intentObj.putExtra(Constants.InputTag.INTENT_KEY_FOREMAN_JOB, mForemanDataList.getEmail());
                intentObj.putExtra(Constants.InputTag.INTENT_KEY_FOREMAN_ID, mForemanDataList.getId());
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
        //mCrewsListView.addFooterView(mLoadMoreFooterView);


        if (isNetworkAvailable(getActivity())) {
            // call API
            getForemanList();
        } else {
            mNoCrewsLinearLayout.setVisibility(View.VISIBLE);
            mNoCrewsImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.no_network));
            mNoCrewsTextView.setText(getActivity().getResources().getString(R.string.no_network));
        }
    }

    private void getForemanList() {
        isSearch = false;
        ForemanData foremanData = new ForemanData();
        mUtilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
        foremanData.page = "1";
        foremanData.count = "20";
        foremanData.parent_id = PreferenceConfiguration.getParentID(getActivity());
        mSupervisorManager.getForemanList(foremanData);
    }


    private void searchForemanList() {
        isSearch = true;
        ForemanData foremanData = new ForemanData();
        // mUtilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
        foremanData.page = "1";
        foremanData.count = "10";
        foremanData.parent_id = PreferenceConfiguration.getParentID(getActivity());
        foremanData.searchText = searchEditText.getText().toString();
        mSupervisorManager.getForemanList(foremanData);
    }

    @Override
    public void onSuccessRedirection(int taskID) {
        try {
            if (taskID == Constants.TaskID.GET_FOREMAN_LIST) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (!isSearch) {
                    mUtilObj.stopLoader();
                    mSwipeRefreshLayout.setRefreshing(false);
                    mForemanArrayList = AppInstance.foremanData.foremanDataList;
                    if (mForemanArrayList.size() > 0) {
                        mNoCrewsLinearLayout.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(false);
                        mForemanListAdapter = new ForemanListAdapter(getActivity(), R.layout.foreman_list_item, mForemanArrayList);
                        mCrewsListView.setAdapter(mForemanListAdapter);
                    } else {
                        mNoCrewsLinearLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    mForemanListAdapter.setDataList(mForemanArrayList);
                    mForemanListAdapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailureRedirection(String errorMessage) {
        try {
            if (!isSearch) {
                mUtilObj.stopLoader();
            }
            //Snackbar.make(mView.findViewById(R.id.container), errorMessage, Snackbar.LENGTH_SHORT).show();
            mUtilObj.showSnackBarAlert(mView.findViewById(R.id.container), errorMessage, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();

        if (searchView != null &&
                !searchView.getQuery().toString().isEmpty()) {
            searchEditText.setText("");
            searchView.setIconified(true);
        }
    }

    @Override
    public void onRefresh() {
        getForemanList();
    }

    public void setToolbar() {
        mToolbar = (Toolbar) mView.findViewById(R.id.search_actionbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mSettingImageView = (ImageView) mView.findViewById(R.id.action_settings);
        mSearchImageView = (ImageView) mView.findViewById(R.id.search_view);
        mClearImageView = (ImageView) mView.findViewById(R.id.clear_view);
        mSearchLinearLayout = (LinearLayout) mView.findViewById(R.id.searchLinear);
        mTitleTextView = (TextView) mView.findViewById(R.id.title);
        mTitleTextView.setText(getActivity().getResources().getString(R.string.my_crews));
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

                if (mForemanListAdapter != null) {
                    mForemanListAdapter.getFilter().filter(s.toString());
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
