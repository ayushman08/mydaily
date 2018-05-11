package com.smartdata.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.smartdata.activities.EditCrewActivity;
import com.smartdata.activities.SettingActivity;
import com.smartdata.adapters.CrewsListAdapter;
import com.smartdata.config.PreferenceConfiguration;
import com.smartdata.dataobject.AppInstance;
import com.smartdata.dataobject.CrewsData;
import com.smartdata.dataobject.CrewsDataList;
import com.smartdata.interfaces.DialogActionCallback;
import com.smartdata.interfaces.ServiceRedirection;
import com.smartdata.managers.SupervisorManager;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.Utility;

import java.util.ArrayList;

public class ForemanCrewsFragment extends BaseFragment implements ServiceRedirection, SwipeRefreshLayout.OnRefreshListener {
    private final static String TAG = ForemanCrewsFragment.class.getSimpleName();
    private Context mContext;
    private View mView;
    private Utility mUtilObj = null;
    private SupervisorManager mSupervisorManager;
    private ListView mForemanCrewsListView;
    private ArrayList<CrewsDataList> mCrewsDataList;
    private CrewsListAdapter mCrewsListAdapter;
    private TextView mNameTextView, mJobTextView, mNoCrewsTextView;
    private ImageView mEditImageView, mNoCrewsImageView;
    private LinearLayout mNoCrewsLinearLayout;
    private FloatingActionButton mAddNewCrewFloatingActionButton;
    private int mPosition;

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

        mView = inflater.inflate(R.layout.fragment_foreman_crews, container, false);
        mContext = getActivity();
        setToolbar();
        initViews();
        bindViews();
        return mView;
    }

    private void bindViews() {
        mAddNewCrewFloatingActionButton = (FloatingActionButton) mView.findViewById(R.id.add_new_crew);
        mAddNewCrewFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentObj = new Intent(getActivity(), EditCrewActivity.class);
                intentObj.putExtra(Constants.RequestCodes.REQ_CODE_CREW_DETAILS, Constants.RequestCodes.REQ_CODE_ADD_CREW);
                startActivityForResult(intentObj, Constants.RequestCodes.REQ_CODE_ADD_CREWS);

            }
        });

        mNoCrewsLinearLayout = (LinearLayout) mView.findViewById(R.id.no_crews_ll);
        mNoCrewsImageView = (ImageView) mView.findViewById(R.id.no_crews_iv);
        mNoCrewsTextView = (TextView) mView.findViewById(R.id.no_crews_tv);

        mNameTextView = (TextView) mView.findViewById(R.id.name_tv);
        mJobTextView = (TextView) mView.findViewById(R.id.job_tv);
        mNameTextView.setText(PreferenceConfiguration.getUserFirstName(getActivity()) + " " + PreferenceConfiguration.getUserLastName(getActivity()));
        mJobTextView.setText(PreferenceConfiguration.getUserEmail(getActivity()));
        mEditImageView = (ImageView) mView.findViewById(R.id.edit_iv);
        mEditImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mUtilObj.showSnackBarAlert(mView.findViewById(R.id.container), "Edit in progress!", false);
            }
        });
        mForemanCrewsListView = (ListView) mView.findViewById(R.id.foreman_crews_listView);

        mForemanCrewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intentObj = new Intent(getActivity(), EditCrewActivity.class);
                intentObj.putExtra(Constants.RequestCodes.REQ_CODE_CREW_DATA, mCrewsDataList.get(position));
                if (mCrewsDataList.get(position).getCrewImageDatas() != null) {
                    intentObj.putExtra(Constants.RequestCodes.REQ_CODE_CREW_IMAGE_DATA, mCrewsDataList.get(position).getCrewImageDatas());
                }
                intentObj.putExtra(Constants.RequestCodes.REQ_CODE_CREW_DETAILS, Constants.RequestCodes.REQ_CODE_EDIT_CREW);
                startActivityForResult(intentObj, Constants.RequestCodes.REQ_CODE_EDIT_CREWS);
            }
        });

        mForemanCrewsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                view.setSelected(true);
                Utility.showAlertDialog(getActivity(), 0, R.string.crew_delete_confirmation_message, R.string.delete, R.string.cancel, new DialogActionCallback() {
                    @Override
                    public void doOnPositive() {
                        deleteCrew(mCrewsDataList.get(position).getId());
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
            mUtilObj.stopLoader();
            mSwipeRefreshLayout.setRefreshing(false);
            if (taskID == Constants.TaskID.GET_CREWS_LIST) {

                mCrewsDataList = AppInstance.crewsData.crewsDataList;
                if (mCrewsDataList.size() > 0) {
                    mNoCrewsLinearLayout.setVisibility(View.GONE);
                    //  if (AppInstance.crewsData.equals(Constants.ResponseCodes.RESPONSE_SUCCESS)) {
                    mCrewsListAdapter = new CrewsListAdapter(getActivity(), R.layout.foreman_list_item, mCrewsDataList);
                    mForemanCrewsListView.setAdapter(mCrewsListAdapter);
                   // mJobTextView.setText(mCrewsDataList.get(0).getFirstname() + " " + mCrewsDataList.get(0).getLastname());
                } else {
                    mNoCrewsLinearLayout.setVisibility(View.VISIBLE);
                }
            } else if (taskID == Constants.TaskID.DELETE_CREW) {
                mCrewsListAdapter.remove(mCrewsListAdapter.getItem(mPosition));
                mForemanCrewsListView.invalidateViews();
                mCrewsListAdapter.notifyDataSetChanged();

                //getCrewsList();
            }
        } catch (Exception e) {
            //mUtilObj.stopLoader();
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
        crewsData.parent_id = PreferenceConfiguration.getUserID(getActivity()) + "";
        mSupervisorManager.getCrewsList(crewsData);
    }

    @Override
    public void onRefresh() {
        if (isNetworkAvailable(getActivity())) {
            // call API
            getCrewsList();
        } else {
            mNoCrewsLinearLayout.setVisibility(View.VISIBLE);
            mNoCrewsImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.no_network));
            mNoCrewsTextView.setText(getActivity().getResources().getString(R.string.no_network));
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.RequestCodes.REQ_CODE_ADD_CREWS:
                getCrewsList();
                break;
            case Constants.RequestCodes.REQ_CODE_EDIT_CREWS:
                getCrewsList();
                break;
        }
    }

    public void deleteCrew(String id) {
        mUtilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
        mSupervisorManager.deleteCrew(id);
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

                if (mCrewsListAdapter != null) {
                    mCrewsListAdapter.getFilter().filter(s.toString());
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
