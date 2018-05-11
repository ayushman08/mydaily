package com.smartdata.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smartdata.adapters.ForemanSelectionListAdapter;
import com.smartdata.adapters.ProductionListAdapter;
import com.smartdata.dataobject.AppInstance;
import com.smartdata.dataobject.ForemanDataList;
import com.smartdata.dataobject.ProductDataList;
import com.smartdata.dataobject.SupervisorJobDetails;
import com.smartdata.dialogs.AlertDialogManager;
import com.smartdata.interfaces.ServiceRedirection;
import com.smartdata.managers.SupervisorManager;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.Utility;

import java.util.ArrayList;


/**
 * Created by Anurag Sethi
 * The activity is used for handling the login screen actions
 */
public class SupervisorJobDetailsActivity extends AppActivity implements ServiceRedirection, OnMapReadyCallback {

    private ActionBar mActionBar;
    private Utility mUtility;
    private AlertDialogManager mAleaAlertDialogManager;
    private SupervisorManager mSupervisorManager;
    private TextView mAssignForemanTextView, mProductionItem1, mProductionItem2, mProductionItem3, mDateTextView,
            mJobDescriptionTextView, mJobIDTextView, mLocationTextView, mClientTextView;
    private ListView mForemanSelectionListView, mProductSelectionListView;
    private Dialog mDialog;
    private int mViewID = 0;
    private ArrayList<ForemanDataList> mForemanArrayList;
    private ArrayList<ProductDataList> mProductDataList;
    private ForemanSelectionListAdapter mForemanSelectionListAdapter;
    private ProductionListAdapter mProductionListAdapter;
    private GoogleMap mGoogleMap;
    final int PLACES = 0;
    final int PLACES_DETAILS = 1;
    private LatLng mLatLng;
    private ProgressBar mProgressBar;
    private AppCompatCheckBox mAppCompatCheckBox;
    private LinearLayout mMapLinearLayout;
    private String mForemanID = "", mProductID1 = "", mProductID2 = "", mProductID3 = "";
    private String mJobId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_supervisor_job_details);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initData();
        bindControls();
        initMapIfNecessary();
    }


    /**
     * Default method of activity life cycle to handle the actions required once the activity starts
     * checks if the network is available or not
     *
     * @return none
     */

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();


    }

    /**
     * Default activity life cycle method
     */
    @Override
    public void onStop() {
        super.onStop();


    }


    /**
     * Initializes the objects
     *
     * @return none
     */
    @Override
    public void initData() {
        mJobId = getIntent().getStringExtra(Constants.InputTag.JOB_ID);
        mUtility = new Utility(SupervisorJobDetailsActivity.this);
        mSupervisorManager = new SupervisorManager(SupervisorJobDetailsActivity.this, this);
        mUtility.customActionBar(SupervisorJobDetailsActivity.this, true, R.string.job_details);
        mAleaAlertDialogManager = new AlertDialogManager();
    }

    /**
     * Binds the UI controls
     *
     * @return none
     */
    @Override
    public void bindControls() {
        mClientTextView = (TextView) findViewById(R.id.client_tv);
        mMapLinearLayout = (LinearLayout) findViewById(R.id.map_layout);
        mJobDescriptionTextView = (TextView) findViewById(R.id.job_description_et);
        mDateTextView = (TextView) findViewById(R.id.date_tv);
        // mDateTextView.setText(new SimpleDateFormat(Constants.InputTag.DATE_FORMAT).format(Calendar.getInstance().getTime()) + "");
        mJobIDTextView = (TextView) findViewById(R.id.job_id_et);

        mLocationTextView = (TextView) findViewById(R.id.location_ac_tv);
        mProgressBar = (ProgressBar) findViewById(R.id.search_bar);
        mAssignForemanTextView = (TextView) findViewById(R.id.assign_foreman_tv);
        mAssignForemanTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getForemanList();
            }
        });

        mProductionItem1 = (TextView) findViewById(R.id.production_item_tv1);
        mProductionItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewID = 1;
                // getProductionList();
            }
        });

        mProductionItem2 = (TextView) findViewById(R.id.production_item_tv2);
        mProductionItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewID = 2;
                // getProductionList();
            }
        });

        mProductionItem3 = (TextView) findViewById(R.id.production_item_tv3);
        mProductionItem3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewID = 3;
                // getProductionList();
            }
        });

    }

    /**
     * The method handles the result from the Facebook
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * The interface method implemented in the java files
     *
     * @param taskID the id based on which the relevant action is performed
     * @return none
     */
    @Override
    public void onSuccessRedirection(int taskID) {
        try {
            // mUtility.stopLoader();
            if (taskID == Constants.TaskID.JOB_DETAILS) {
                setJobDetails(AppInstance.supervisorJobDetails);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The interface method implemented in the java files
     *
     * @param errorMessage the error message to be displayed
     * @return none
     */

    @Override
    public void onFailureRedirection(String errorMessage) {
        try {
            mUtility.stopLoader();
            //Snackbar.make(findViewById(R.id.container), errorMessage, Snackbar.LENGTH_SHORT).show();
            mUtility.showSnackBarAlert(findViewById(R.id.container), errorMessage, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        initMapSettings();
        getJobDetails(mJobId);
    }


    private void initMapSettings() {
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        //mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        if (ContextCompat.checkSelfPermission(SupervisorJobDetailsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //  mGoogleMap.setMyLocationEnabled(true);
        }
    }

    private void initCamera() {
        if (mLatLng != null) {
            CameraPosition position = CameraPosition.builder()
                    .target(mLatLng)
                    .zoom(getInitialMapZoomLevel())
                    .build();

            MarkerOptions options = new MarkerOptions();
            options.position(mLatLng);
            options.title(mLocationTextView.getText().toString());
            //options.snippet("Latitude:" + mLatLng.latitude + ",Longitude:" + mLatLng.longitude);
            if (mGoogleMap != null) {
                // Adding the marker in the Google Map
                mGoogleMap.clear();
                mGoogleMap.addMarker(options);
                //mGoogleMap.addMarker(new MarkerOptions().flat(true)
                //.anchor(0.5f, 0.5f).position(mLatLng));
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), null);
            }
        }

    }


    private float getInitialMapZoomLevel() {
        return 5.f;
    }


    protected void initMapIfNecessary() {
        if (mGoogleMap != null) {
            return;
        }
        //mGoogleMap = ( (MapFragment) getFragmentManager().findFragmentById( R.id.map ) ).getMap();
        SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.location_map));
        mapFragment.getMapAsync(this);
    }

    public void setJobDetails(SupervisorJobDetails supervisorJobDetails) {
        if (supervisorJobDetails != null && supervisorJobDetails.getJobDetailsData() != null && supervisorJobDetails.getJobDetailsData().getJobDetailsData() != null) {

            if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getStart_date() != null) {
                mDateTextView.setText(mUtility.changeDate(supervisorJobDetails.getJobDetailsData().getJobDetailsData().getStart_date()));
            }
            if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getJob_id() != null) {
                mJobIDTextView.setText(supervisorJobDetails.getJobDetailsData().getJobDetailsData().getJob_id());
            }

            if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getClient() != null) {
                mClientTextView.setText(supervisorJobDetails.getJobDetailsData().getJobDetailsData().getClient());
            }
            if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getJob_location().isEmpty()) {
                mLocationTextView.setText(getResources().getString(R.string.location_hint));

            } else {
                mLocationTextView.setText(supervisorJobDetails.getJobDetailsData().getJobDetailsData().getJob_location());
            }

            if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getDescription() != null) {
                mJobDescriptionTextView.setText(supervisorJobDetails.getJobDetailsData().getJobDetailsData().getDescription());
            } else {
                mJobDescriptionTextView.setText(getResources().getString(R.string.description_hint));
            }
            if (supervisorJobDetails.getJobDetailsData().getForemanDetailsArrayList().size() > 0 && supervisorJobDetails.getJobDetailsData().getForemanDetailsArrayList().get(0).getFirstname() != null) {
                mAssignForemanTextView.setText(supervisorJobDetails.getJobDetailsData().getForemanDetailsArrayList().get(0).getFirstname() + " " + supervisorJobDetails.getJobDetailsData().getForemanDetailsArrayList().get(0).getLastname());
                mForemanID = supervisorJobDetails.getJobDetailsData().getForemanDetailsArrayList().get(0).getId() + "";
            }


            if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo() != null) {

                if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().size() == 1) {
                    if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().get(0).getName().isEmpty()) {
                        mProductionItem1.setText(getResources().getString(R.string.production_items));
                    } else {
                        mProductionItem1.setText(supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().get(0).getName());
                        //mProductID1 = PreferenceConfiguration.getJobProductionID1(SupervisorAddNewJobActivity.this);
                        // mProductionItem2.setVisibility(View.VISIBLE);
                    }
                }

                if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().size() == 2) {

                    if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().get(0).getName().isEmpty()) {
                        mProductionItem1.setText(getResources().getString(R.string.production_items));
                    } else {
                        mProductionItem1.setText(supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().get(0).getName());
                        //mProductID1 = PreferenceConfiguration.getJobProductionID1(SupervisorAddNewJobActivity.this);
                        // mProductionItem2.setVisibility(View.VISIBLE);
                    }

                    if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().get(1).getName().isEmpty()) {
                        mProductionItem2.setText(getResources().getString(R.string.production_items));
                    } else {
                        mProductionItem2.setVisibility(View.VISIBLE);
                        mProductionItem2.setText(supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().get(1).getName());
                        // mProductID2 = PreferenceConfiguration.getJobProductionID2(SupervisorAddNewJobActivity.this);

                    }
                }

                if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().size() == 3) {

                    if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().get(0).getName().isEmpty()) {
                        mProductionItem1.setText(getResources().getString(R.string.production_items));
                    } else {
                        mProductionItem1.setText(supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().get(0).getName());
                        //mProductID1 = PreferenceConfiguration.getJobProductionID1(SupervisorAddNewJobActivity.this);
                        // mProductionItem2.setVisibility(View.VISIBLE);
                    }

                    if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().get(1).getName().isEmpty()) {
                        mProductionItem2.setText(getResources().getString(R.string.production_items));
                    } else {
                        mProductionItem2.setVisibility(View.VISIBLE);
                        mProductionItem2.setText(supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().get(1).getName());
                        // mProductID2 = PreferenceConfiguration.getJobProductionID2(SupervisorAddNewJobActivity.this);

                    }

                    if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().get(2).getName().isEmpty()) {
                        mProductionItem3.setText(getResources().getString(R.string.production_items));
                    } else {
                        mProductionItem3.setVisibility(View.VISIBLE);
                        mProductionItem3.setText(supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().get(2).getName());

                        // mProductID3 = PreferenceConfiguration.getJobProductionID3(SupervisorAddNewJobActivity.this);
                    }
                }

            }
            if (!supervisorJobDetails.getJobDetailsData().getJobDetailsData().getLatitude().isEmpty() && !supervisorJobDetails.getJobDetailsData().getJobDetailsData().getLongitude().isEmpty()) {
                mLatLng = new LatLng(Double.parseDouble(supervisorJobDetails.getJobDetailsData().getJobDetailsData().getLatitude()), Double.parseDouble(supervisorJobDetails.getJobDetailsData().getJobDetailsData().getLongitude()));
                initCamera();
            }

        } else {
            mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.invalid_message), false);
        }
    }

    private void getJobDetails(String id) {
        if (id != null) {
            //mUtility.startLoader(SupervisorJobDetailsActivity.this, R.drawable.image_for_rotation);
            mSupervisorManager.getJobDetails(id);
        }
    }

}
