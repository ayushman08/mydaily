package com.smartdata.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.smartdata.activities.SettingActivity;
import com.smartdata.activities.UpdateDailyActivity;
import com.smartdata.adapters.MyDailiesAdapter;
import com.smartdata.config.PreferenceConfiguration;
import com.smartdata.dataobject.AppInstance;
import com.smartdata.dataobject.MyDailyData;
import com.smartdata.dataobject.MyDailyDataList;
import com.smartdata.interfaces.DialogActionCallback;
import com.smartdata.interfaces.ServiceRedirection;
import com.smartdata.managers.SupervisorManager;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.Utility;

import java.util.ArrayList;

public class MyDailiesFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, ServiceRedirection {

    private final static String TAG = MyDailiesFragment.class.getSimpleName();
    private Context mContext;
    private View mView;
    private Utility mUtilObj = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mLoadMoreFooterView;
    private ListView mMyDailiesListView;
    private LinearLayout mNoSubmittalsLinearLayout;
    private ImageView mNoJobImageView;
    private TextView mNoDailiesTextView;
    private SupervisorManager mSupervisorManager;
    private MyDailiesAdapter mMyDailiesAdapter;
    private ArrayList<MyDailyDataList> mMyDailyDataList;
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

        mView = inflater.inflate(R.layout.fragment_my_dailies, container, false);
        mContext = getActivity();
        setToolbar();
        initViews();
        bindViews();
        //swipeMenuCreator();
        return mView;
    }


    private void bindViews() {
        mNoSubmittalsLinearLayout = (LinearLayout) mView.findViewById(R.id.no_dailies_ll);
        mNoJobImageView = (ImageView) mView.findViewById(R.id.no_job_iv);
        mNoDailiesTextView = (TextView) mView.findViewById(R.id.no_dailies_tv);
        mMyDailiesListView = (ListView) mView.findViewById(R.id.my_dailies_listView);

        mMyDailiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intentObj = new Intent(getActivity(), UpdateDailyActivity.class);
                intentObj.putExtra(Constants.RequestCodes.REQ_CODE_DAILY_DATA, mMyDailyDataList.get(position));
                //<Dev comment="Added by kapil">
                intentObj.putExtra(Constants.RequestCodes.REQ_IS_DRAFT, mMyDailyDataList.get(position).is_draft);
                //<Dev comment>
                intentObj.putExtra(Constants.RequestCodes.REQ_CODE_SUPERVISOR_DAILY_DATA, mMyDailyDataList.get(position).getSupervisorDetails());
                intentObj.putExtra(Constants.RequestCodes.REQ_CODE_CREW_DAILY_DATA, mMyDailyDataList.get(position).getCrews_details());
                if (mMyDailyDataList.get(position).getBillableItemses() != null && mMyDailyDataList.get(position).getBillableItemses().size() > 0) {
                    intentObj.putExtra(Constants.RequestCodes.REQ_CODE_BILLABLE_DAILY_DATA, mMyDailyDataList.get(position).getBillableItemses());

                }
                if (mMyDailyDataList.get(position).getDailiesImageArrayList() != null && mMyDailyDataList.get(position).getDailiesImageArrayList().size() > 0) {
                    intentObj.putExtra(Constants.RequestCodes.REQ_CODE_IMAGE_DAILY_DATA, mMyDailyDataList.get(position).getDailiesImageArrayList());
                }
                intentObj.putExtra(Constants.RequestCodes.REQ_CODE_JOB_DAILY_DATA, mMyDailyDataList.get(position).getMyDailyDetails());
                startActivityForResult(intentObj, Constants.RequestCodes.REQ_CODE_EDIT_DAILY);
            }
        });

        mMyDailiesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                view.setSelected(true);
                Utility.showAlertDialog(getActivity(), 0, R.string.daily_delete_confirmation_message, R.string.delete, R.string.cancel, new DialogActionCallback() {
                    @Override
                    public void doOnPositive() {
                        deleteDaily(mMyDailyDataList.get(position).getId());
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
        if (isNetworkAvailable(getActivity())) {
            // call API
            getMyDailies();
        } else {
            mNoSubmittalsLinearLayout.setVisibility(View.VISIBLE);
            mNoJobImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.no_network));
            mNoDailiesTextView.setText(getActivity().getResources().getString(R.string.no_network));
        }
    }


    private void initViews() {
        mUtilObj = new Utility(mContext);
        mSupervisorManager = new SupervisorManager(getActivity(), this);
    }

    @Override
    public void onRefresh() {
        if (isNetworkAvailable(getActivity())) {
            // call API
            getMyDailies();
        } else {
            mNoSubmittalsLinearLayout.setVisibility(View.VISIBLE);
            mNoJobImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.no_network));
            mNoDailiesTextView.setText(getActivity().getResources().getString(R.string.no_network));
        }
    }

    private void getMyDailies() {
        MyDailyData myDailyData = new MyDailyData();
        mUtilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
        myDailyData.page = "1";
        myDailyData.count = "50";
        //myDailyData.status = Constants.RequestCodes.REQ_CODE_DAILY_ACTIVE;
        myDailyData.user_id = PreferenceConfiguration.getUserID(getActivity());
        mSupervisorManager.getMyDailies(myDailyData);
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
                    mMyDailiesAdapter = new MyDailiesAdapter(getActivity(), R.layout.foreman_list_item, mMyDailyDataList);
                    mMyDailiesListView.setAdapter(mMyDailiesAdapter);
                } else {
                    mNoSubmittalsLinearLayout.setVisibility(View.VISIBLE);
                }
            } else if (taskID == Constants.TaskID.DELETE_DAILY) {
                mMyDailiesAdapter.remove(mMyDailiesAdapter.getItem(mPosition));
                mMyDailiesListView.invalidateViews();
                mMyDailiesAdapter.notifyDataSetChanged();
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

  /*  private void swipeMenuCreator() {
        mSwipeMenuCreator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xFF,
                        0xFF, 0xFF)));
                // set item width
                openItem.setWidth(mMyDailiesListView.dp2px(90));
                // set item title
                //openItem.setTitle(getResources().getString(R.string.edit));
                openItem.setIcon(R.drawable.edit);
                // set item title fontsize
                // openItem.setTitleSize(18);
                // set item title font color
                //  openItem.setTitleColor(Color.GRAY);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xFF,
                        0xFF, 0xFF)));
                // set item width
                deleteItem.setWidth(mMyDailiesListView.dp2px(90));
                // deleteItem.setTitle(getResources().getString(R.string.delete));
                // set item title fontsize
                // deleteItem.setTitleSize(18);
                // set item title font color
                // deleteItem.setTitleColor(Color.GRAY);
                // set a icon
                deleteItem.setIcon(R.drawable.trash);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };


        // set creator
        mMyDailiesListView.setMenuCreator(mSwipeMenuCreator);
        mMyDailiesListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                switch (index) {
                    case 0:
                        // edit
                        Intent intentObj = new Intent(getActivity(), UpdateDailyActivity.class);
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
                        startActivityForResult(intentObj, Constants.RequestCodes.REQ_CODE_EDIT_DAILY);
                        break;
                    case 1:
                        // call API for delete
                        deleteDaily(mMyDailyDataList.get(position).getId());
                        mPosition = position;
                        // mCrewsListAdapter.remove(mCrewsListAdapter.getItem(position));
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }*/

    public void deleteDaily(String id) {
        mUtilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
        mSupervisorManager.deleteDaily(id);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.RequestCodes.REQ_CODE_EDIT_DAILY:
                getMyDailies();
                break;
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
        mTitleTextView.setText(getActivity().getResources().getString(R.string.my_dailies));
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

                if (mMyDailiesAdapter != null) {
                    mMyDailiesAdapter.getFilter().filter(s.toString());
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
