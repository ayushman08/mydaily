package com.smartdata.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.smartdata.activities.DashboardActivity;
import com.smartdata.activities.SettingActivity;
import com.smartdata.adapters.ForemanSelectionListAdapter;
import com.smartdata.adapters.SupervisorJobListAdapter;
import com.smartdata.adapters.ProductionListAdapter;
import com.smartdata.config.PreferenceConfiguration;
import com.smartdata.dataobject.AppInstance;
import com.smartdata.dataobject.ForemanData;
import com.smartdata.dataobject.ForemanDataList;
import com.smartdata.dataobject.JobData;
import com.smartdata.dataobject.JobDataList;
import com.smartdata.dataobject.ProductDataList;
import com.smartdata.dataobject.SupervisorAddJobData;
import com.smartdata.interfaces.ServiceRedirection;
import com.smartdata.managers.SupervisorManager;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.CustomScrollView;
import com.smartdata.utils.Utility;

import java.util.ArrayList;

public class NewJobFragment extends BaseFragment implements ServiceRedirection, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final static String TAG = NewJobFragment.class.getSimpleName();
    private Context mContext;
    private View mView;
    private Utility mUtilObj = null;
    private SupervisorManager mSupervisorManager;
    private ArrayList<JobDataList> mJobDataArrayList;
    private ListView mForemanSelectionListView, mProductSelectionListView;
    private SupervisorJobListAdapter mSupervisorJobListAdapter;
    private EditText mLocationEditText;
    private AppCompatButton mSendAppCompatButton;
    private CustomScrollView mScrollView;
    private GoogleMap mGoogleMap;
    private LatLng mCenterLocation;
    private GoogleApiClient mGoogleApiClient;
    protected static final int REQUEST_CHECK_SETTINGS = 1;
    private LocationManager locationManager;
    private LocationRequest locationRequest;
    private ArrayList<ForemanDataList> mForemanArrayList;
    private Dialog mDialog;
    private ForemanSelectionListAdapter mForemanSelectionListAdapter;
    private ProductionListAdapter mProductionListAdapter;
    private ArrayList<ProductDataList> mProductDataList;
    private TextView mAssignForemanTextView, mProductionItem1, mProductionItem2, mProductionItem3;
    private int mViewID = 0;
    private Toolbar mToolbar;
    private ImageView mSettingImageView, mClearImageView, mSearchImageView;
    private LinearLayout mSearchLinearLayout;
    private TextView mTitleTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_new_job, container, false);
        mContext = getActivity();

        setToolbar();
        initViews();
        bindViews();
        initMapIfNecessary();

        return mView;
    }

    private void bindViews() {
        mSendAppCompatButton = (AppCompatButton) mView.findViewById(R.id.sendButton);
        mLocationEditText = (EditText) mView.findViewById(R.id.location_ac_tv);
        mScrollView = (CustomScrollView) mView.findViewById(R.id.new_job_sv);

        mAssignForemanTextView = (TextView) mView.findViewById(R.id.assign_foreman_tv);
        mAssignForemanTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForemanList();
            }
        });

        mProductionItem1 = (TextView) mView.findViewById(R.id.production_item_tv1);
        mProductionItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewID = 1;
                getProductionList();
            }
        });

        mProductionItem2 = (TextView) mView.findViewById(R.id.production_item_tv2);
        mProductionItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewID = 2;
                getProductionList();
            }
        });

        mProductionItem3 = (TextView) mView.findViewById(R.id.production_item_tv3);
        mProductionItem3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewID = 3;
                getProductionList();
            }
        });

        mSendAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addJob();
            }
        });
        //getJobList();
    }

    protected void initMapIfNecessary() {
        if (mGoogleMap != null) {
            return;
        }
        //mGoogleMap = ( (MapFragment) getFragmentManager().findFragmentById( R.id.map ) ).getMap();
        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.location_map));
        mapFragment.getMapAsync(this);
    }

    private void initViews() {

        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCheck1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);
        if ((permissionCheck != PackageManager.PERMISSION_GRANTED) && (permissionCheck1 != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(
                    getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Constants.RequestCodes.REQ_CODE_ACCESS_FINE_LOCATION);
        }

        mUtilObj = new Utility(getActivity());
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

    private void getJobList() {
        JobData jobData = new JobData();
        mUtilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
        jobData.page = "1";
        jobData.count = "10";
        jobData.parent_id = PreferenceConfiguration.getUserID(getActivity());
        mSupervisorManager.getJobList(jobData);
    }

    private void getForemanList() {
        ForemanData foremanData = new ForemanData();
        mUtilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
        foremanData.page = "1";
        foremanData.count = "10";
        foremanData.parent_id = PreferenceConfiguration.getParentID(getActivity());
        mSupervisorManager.getForemanList(foremanData);
    }

    private void getProductionList() {
        mUtilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
        mSupervisorManager.getProductionList(PreferenceConfiguration.getParentID(getActivity()));
    }

    private void addJob() {
        SupervisorAddJobData supervisorAddJobData = new SupervisorAddJobData();
        mUtilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
        supervisorAddJobData.client = "";
        supervisorAddJobData.job_id = "ALG101";
        supervisorAddJobData.address = "";
        supervisorAddJobData.start_date = "";
        supervisorAddJobData.projected_end_date = "";
        supervisorAddJobData.actual_end_date = "";
        supervisorAddJobData.job_location = "";
        mSupervisorManager.addNewJob(supervisorAddJobData);
    }

    @Override
    public void onSuccessRedirection(int taskID) {
        try {
            mUtilObj.stopLoader();
            if (taskID == Constants.TaskID.GET_FOREMAN_LIST) {
                mForemanArrayList = AppInstance.foremanData.foremanDataList;
                if (mForemanArrayList.size() > 0) {
                    foremanSelectionDialog();
                }
            } else if (taskID == Constants.TaskID.GET_PRODUCTION_LIST) {
                mProductDataList = AppInstance.productData.productDataList;
                if (mProductDataList.size() > 0) {
                    productionItemDialog();
                }
            } else if (taskID == Constants.TaskID.ADD_JOB) {
                if (AppInstance.supervisorAddJobData.getCode() == Constants.ResponseCodes.RESPONSE_SUCCESS) {
                    mUtilObj.showAlert(getActivity(), "", AppInstance.supervisorAddJobData.getMessage());
                } else {
                    //Snackbar.make(getActivity().findViewById(R.id.container), AppInstance.supervisorAddJobData.getMessage(), Snackbar.LENGTH_SHORT).show();
                    mUtilObj.showSnackBarAlert(mView.findViewById(R.id.container), AppInstance.supervisorAddJobData.getMessage(), false);
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
            //Snackbar.make(mView.findViewById(R.id.container), errorMessage, Snackbar.LENGTH_SHORT).show();
            mUtilObj.showSnackBarAlert(mView.findViewById(R.id.container), AppInstance.supervisorAddJobData.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        initMapSettings();
        initCamera();
    }

    private void initMapSettings() {
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    private void initCamera() {
        if (mCenterLocation != null) {
            CameraPosition position = CameraPosition.builder()
                    .target(mCenterLocation)
                    .zoom(getInitialMapZoomLevel())
                    .build();
      /*  mGoogleMap.addMarker(new MarkerOptions().flat(true)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.green_marker)).anchor(0.5f, 0.5f).position(mCenterLocation));*/
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), null);
        }

    }


    private float getInitialMapZoomLevel() {
        return 0.7f;
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onStart() {
        super.onStart();
        settingsRequest();
    }

    public void settingsRequest() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1 * 1000);
        locationRequest.setFastestInterval(1 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient
        // builder.setNeedBle(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }


    protected void startLocationUpdates() {
        if (mGoogleApiClient != null) {

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, Constants.RequestCodes.REQ_CODE_ACCESS_FINE_LOCATION);
            } else {
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, locationRequest, this);
            }

        }
    }

    protected void stopLocationUpdates() {
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(
                        mGoogleApiClient, this);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        startLocationUpdates();

                        break;
                    case Activity.RESULT_CANCELED:
                        settingsRequest();//keep asking if imp or do whatever
                        break;
                }
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            startLocationUpdates();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mCenterLocation = new LatLng(location.getLatitude(), location.getLongitude());
    }


    private void foremanSelectionDialog() {

        // retrieve display dimensions
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        // inflate and adjust layout
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_foreman_selection, null);
        layout.setMinimumWidth((int) (displayRectangle.width() * 0.8f));
        layout.setMinimumHeight((int) (displayRectangle.height() * 0.8f));
        mDialog = new Dialog(getActivity());
        mDialog.setContentView(layout);

        //  mDialog.setContentView(R.layout.dialog_foreman_selection);
        mForemanSelectionListView = (ListView) mDialog.findViewById(R.id.foreman_selection_lv);

        mDialog.setTitle(getResources().getString(R.string.foreman_list));
        mDialog.show();


        mForemanSelectionListAdapter = new ForemanSelectionListAdapter(getActivity(), R.layout.foreman_list_item, mForemanArrayList);
        mForemanSelectionListView.setAdapter(mForemanSelectionListAdapter);
        mForemanSelectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAssignForemanTextView.setText(mForemanArrayList.get(position).getFirstname() + " " + mForemanArrayList.get(position).getLastname());

                mDialog.dismiss();
            }
        });
    }

    private void productionItemDialog() {

        // retrieve display dimensions
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        // inflate and adjust layout
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_product_selection, null);
        layout.setMinimumWidth((int) (displayRectangle.width() * 0.8f));
        layout.setMinimumHeight((int) (displayRectangle.height() * 0.8f));
        mDialog = new Dialog(getActivity());
        mDialog.setContentView(layout);

        mProductSelectionListView = (ListView) mDialog.findViewById(R.id.product_selection_lv);

        mDialog.setTitle(getResources().getString(R.string.product_list));
        mDialog.show();


        mProductionListAdapter = new ProductionListAdapter(getActivity(), R.layout.product_selection_list_item, mProductDataList);
        mProductSelectionListView.setAdapter(mProductionListAdapter);
        mProductSelectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mViewID == 1) {
                    mProductionItem1.setText(mProductDataList.get(position).getName());
                    mProductionItem1.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                    mProductionItem2.setVisibility(View.VISIBLE);
                } else if (mViewID == 2) {
                    mProductionItem2.setText(mProductDataList.get(position).getName());
                    mProductionItem2.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                    mProductionItem3.setVisibility(View.VISIBLE);
                } else if (mViewID == 3) {
                    mProductionItem3.setText(mProductDataList.get(position).getName());
                    mProductionItem3.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                }
                mDialog.dismiss();
            }
        });
    }

    public void setToolbar() {
        mToolbar = (Toolbar) mView.findViewById(R.id.search_actionbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mSettingImageView = (ImageView) mView.findViewById(R.id.action_settings);
        mSearchImageView = (ImageView) mView.findViewById(R.id.search_view);
        mClearImageView = (ImageView) mView.findViewById(R.id.clear_view);
        mSearchLinearLayout = (LinearLayout) mView.findViewById(R.id.searchLinear);
        mTitleTextView = (TextView) mView.findViewById(R.id.title);
        mTitleTextView.setText(getActivity().getResources().getString(R.string.new_job));

        mSearchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mToolbar.setVisibility(View.GONE);
                mSearchLinearLayout.setVisibility(View.VISIBLE);
                //searchEditText.requestFocus();
                // showVirtualKeyboard(appCompatActivity);
            }
        });

        mClearImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchLinearLayout.setVisibility(View.GONE);
                mToolbar.setVisibility(View.VISIBLE);
                // searchEditText.clearFocus();
                // appCompatActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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

    }
}
