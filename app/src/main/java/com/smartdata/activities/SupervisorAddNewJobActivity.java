package com.smartdata.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smartdata.adapters.ForemanSelectionListAdapter;
import com.smartdata.adapters.ProductionListAdapter;
import com.smartdata.config.PreferenceConfiguration;
import com.smartdata.dataobject.AppInstance;
import com.smartdata.dataobject.ForemanData;
import com.smartdata.dataobject.ForemanDataList;
import com.smartdata.dataobject.ForemanID;
import com.smartdata.dataobject.ProductData;
import com.smartdata.dataobject.ProductDataList;
import com.smartdata.dataobject.SupervisorAddJobData;
import com.smartdata.dataobject.SupervisorJobDetails;
import com.smartdata.dataobject.User;
import com.smartdata.dialogs.AlertDialogManager;
import com.smartdata.interfaces.DialogActionCallback;
import com.smartdata.interfaces.ServiceRedirection;
import com.smartdata.managers.LoginManager;
import com.smartdata.managers.SupervisorManager;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.CustomAutoCompleteTextview;
import com.smartdata.utils.CustomScrollView;
import com.smartdata.utils.PlaceDetailsJSONParser;
import com.smartdata.utils.PlaceJSONParser;
import com.smartdata.utils.Utility;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static com.smartdata.utils.Utility.downloadUrl;


/**
 * Created by Anurag Sethi
 * The activity is used for handling the login screen actions
 */
public class SupervisorAddNewJobActivity extends AppActivity implements ServiceRedirection, OnMapReadyCallback, CompoundButton.OnCheckedChangeListener {

    private ActionBar mActionBar;
    private Utility mUtility;
    private AlertDialogManager mAleaAlertDialogManager;
    private SupervisorManager mSupervisorManager;
    private TextView mAssignForemanTextView, mProductionItem1, mProductionItem2, mProductionItem3, mDateTextView;
    private ListView mForemanSelectionListView, mProductSelectionListView;
    private Dialog mDialog;
    private CustomAutoCompleteTextview mLocationEditText;
    private AppCompatButton mSendAppCompatButton;
    private int mViewID = 0;
    private ArrayList<ForemanDataList> mForemanArrayList;
    private ArrayList<ProductDataList> mProductDataList;
    private ForemanSelectionListAdapter mForemanSelectionListAdapter;
    private ProductionListAdapter mProductionListAdapter;
    private GoogleMap mGoogleMap;

    private DownloadTask placesDownloadTask;
    private DownloadTask placeDetailsDownloadTask;
    private ParserTask placesParserTask;
    private ParserTask placeDetailsParserTask;
    final int PLACES = 0;
    final int PLACES_DETAILS = 1;
    private LatLng mLatLng;
    private ProgressBar mProgressBar;
    private EditText mJobIDEditText, mJobDescriptionEditText, mClientNameEditText;
    private AppCompatCheckBox mAppCompatCheckBox;
    private LinearLayout mMapLinearLayout;
    private String mForemanID = "", mProductID1 = "", mProductID2 = "", mProductID3 = "";
    private String mJobId, mForemanNames, mProductionItemNames;
    private String element[];
    private boolean[] checkedItems;

    private String productionElement[];
    private boolean[] productionCheckedItems;
    private SupervisorJobDetails mSupervisorJobDetails;

    private ArrayList<String> mSelectedItemID = null;
    private ArrayList<String> mSelectedProductionItemID = null;
    private ArrayList<String> mSelectedProductionItemName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_supervisor_add_new_job);
        initData();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
        mUtility = new Utility(SupervisorAddNewJobActivity.this);
        mSupervisorManager = new SupervisorManager(SupervisorAddNewJobActivity.this, this);
        if (mJobId != null) {
            mUtility.customActionBar(SupervisorAddNewJobActivity.this, true, R.string.update_job);
        } else {
            mUtility.customActionBar(SupervisorAddNewJobActivity.this, true, R.string.new_job);
        }
        mAleaAlertDialogManager = new AlertDialogManager();
    }

    /**
     * Binds the UI controls
     *
     * @return none
     */
    @Override
    public void bindControls() {
        mClientNameEditText = (EditText) findViewById(R.id.client_et);

        mMapLinearLayout = (LinearLayout) findViewById(R.id.map_layout);
        mAppCompatCheckBox = (AppCompatCheckBox) findViewById(R.id.production_checkbox);
        mAppCompatCheckBox.setOnCheckedChangeListener(this);
        mJobDescriptionEditText = (EditText) findViewById(R.id.job_description_et);
        mDateTextView = (TextView) findViewById(R.id.date_tv);
        mDateTextView.setText(new SimpleDateFormat(Constants.InputTag.DATE_FORMAT).format(Calendar.getInstance().getTime()) + "");
        mJobIDEditText = (EditText) findViewById(R.id.job_id_et);

        mSendAppCompatButton = (AppCompatButton) findViewById(R.id.sendButton);
        if (mJobId != null) {
            mSendAppCompatButton.setText(getResources().getString(R.string.update));
            mSendAppCompatButton.setBackgroundDrawable(ContextCompat.getDrawable(SupervisorAddNewJobActivity.this, R.drawable.button_active));
        }
        mLocationEditText = (CustomAutoCompleteTextview) findViewById(R.id.location_ac_tv);
        mProgressBar = (ProgressBar) findViewById(R.id.search_bar);
        mAssignForemanTextView = (TextView) findViewById(R.id.assign_foreman_tv);
        mAssignForemanTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForemanList();
            }
        });


        mProductionItem1 = (TextView) findViewById(R.id.production_item_tv1);
        mProductionItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewID = 1;
                getProductionList();
            }
        });

        mProductionItem2 = (TextView) findViewById(R.id.production_item_tv2);
        mProductionItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewID = 2;
                getProductionList();
            }
        });

        mProductionItem3 = (TextView) findViewById(R.id.production_item_tv3);
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


        mLocationEditText.setThreshold(1);
        mLocationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mMapLinearLayout.setVisibility(View.VISIBLE);
                placesDownloadTask = new DownloadTask(PLACES);
                mProgressBar.setVisibility(View.VISIBLE);
                // Getting url to the Google Places Autocomplete api
                String url = getAutoCompleteUrl(charSequence.toString());

                // Start downloading Google Places
                // This causes to execute doInBackground() of DownloadTask class
                placesDownloadTask.execute(url);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mJobIDEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    mSendAppCompatButton.setBackgroundDrawable(ContextCompat.getDrawable(SupervisorAddNewJobActivity.this, R.drawable.button_active));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mLocationEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int index, long id) {
                ListView lv = (ListView) arg0;
                SimpleAdapter adapter = (SimpleAdapter) arg0.getAdapter();

                HashMap<String, String> hm = (HashMap<String, String>) adapter.getItem(index);

                // Creating a DownloadTask to download Places details of the selected place
                placeDetailsDownloadTask = new DownloadTask(PLACES_DETAILS);

                // Getting url to the Google Places details api
                String url = getPlaceDetailsUrl(hm.get("reference"));

                // Start downloading Google Place Details
                // This causes to execute doInBackground() of DownloadTask class
                placeDetailsDownloadTask.execute(url);
                // mLocationEditText.clearFocus();
                mUtility.hiddenInputMethod(SupervisorAddNewJobActivity.this);
            }
        });

        getJobDetails(mJobId);
    }

    private String getPlaceDetailsUrl(String ref) {

        // Obtain browser key from https://code.google.com/apis/console
        String key = "key=AIzaSyA4Q27nW0z0r7Wjeo_QfH3peUTfIMG-klc";

        // reference of place
        String reference = "reference=" + ref;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = reference + "&" + sensor + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/details/" + output + "?" + parameters;

        return url;
    }


    private String getAutoCompleteUrl(String place) {

        // Obtain browser key from https://code.google.com/apis/console
        String key = "key=AIzaSyA4Q27nW0z0r7Wjeo_QfH3peUTfIMG-klc";

        // place to be be searched
        String input = "input=" + place;

        // place type to be searched
        String types = "types=geocode";

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = input + "&" + types + "&" + sensor + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;

        return url;
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
            mUtility.stopLoader();
            if (taskID == Constants.TaskID.GET_FOREMAN_LIST) {
                mForemanArrayList = AppInstance.foremanData.foremanDataList;
                if (mForemanArrayList.size() > 0) {
                    //foremanSelectionDialog();
                    foremanMultiSelectionDialog(mForemanArrayList);
                } else {
                    mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.no_foremans), false);
                }
            } else if (taskID == Constants.TaskID.GET_PRODUCTION_LIST) {
                mProductDataList = AppInstance.productData.productDataList;
                if (mProductDataList.size() > 0) {
                    //productionItemDialog();
                    productionMultiSelectionDialog(mProductDataList);
                } else {
                    mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.no_products), false);
                }
            } else if (taskID == Constants.TaskID.ADD_JOB) {
                if (AppInstance.supervisorAddJobData.getCode() == Constants.ResponseCodes.RESPONSE_SUCCESS) {
                    //mUtility.showAlert(SupervisorAddNewJobActivity.this, "", AppInstance.supervisorAddJobData.getMessage());

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SupervisorAddNewJobActivity.this);
                    alertDialog.setTitle("");
                    // Setting Dialog Message
                    //alertDialog.setMessage(AppInstance.supervisorAddJobData.getMessage() + "");
                    alertDialog.setMessage(AppInstance.supervisorAddJobData.getMessage() + "");

                    alertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();

                } else {
                    mUtility.showSnackBarAlert(findViewById(R.id.container), AppInstance.supervisorAddJobData.getMessage(), false);
                }
            } else if (taskID == Constants.TaskID.JOB_DETAILS) {

                mSupervisorJobDetails = AppInstance.supervisorJobDetails;
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
                // AttendeesActivity.this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void getForemanList() {
        ForemanData foremanData = new ForemanData();
        mUtility.startLoader(SupervisorAddNewJobActivity.this, R.drawable.image_for_rotation);
        foremanData.page = "1";
        foremanData.count = "10";
        foremanData.parent_id = PreferenceConfiguration.getParentID(SupervisorAddNewJobActivity.this);
        mSupervisorManager.getForemanList(foremanData);
    }

    private void getProductionList() {
        mUtility.startLoader(SupervisorAddNewJobActivity.this, R.drawable.image_for_rotation);
        mSupervisorManager.getProductionList(PreferenceConfiguration.getParentID(SupervisorAddNewJobActivity.this));
    }

    private void addJob() {
        if (mJobIDEditText.getText().toString().equals("")) {
            // TSnackbar.make(findViewById(R.id.container), getResources().getString(R.string.job_id_validation), TSnackbar.LENGTH_SHORT).show();
            mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.job_id_validation), false);
        } else if (mLocationEditText.getText().toString().equals("")) {
            // TSnackbar.make(findViewById(R.id.container), getResources().getString(R.string.job_id_validation), TSnackbar.LENGTH_SHORT).show();
            mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.location_validation), false);

        }/* else if (mJobDescriptionEditText.getText().toString().equals("")) {
            // TSnackbar.make(findViewById(R.id.container), getResources().getString(R.string.job_id_validation), TSnackbar.LENGTH_SHORT).show();
            mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.job_description_validation), false);
        }*/ else if (mAssignForemanTextView.getText().toString().equals("") || mAssignForemanTextView.getText().toString().equals(getResources().getString(R.string.assign_to_foreman))) {
            // TSnackbar.make(findViewById(R.id.container), getResources().getString(R.string.job_id_validation), TSnackbar.LENGTH_SHORT).show();
            mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.job_assign_to_foreman_validation), false);
        } else if (mProductionItem1.getText().toString().equals("") || mAssignForemanTextView.getText().toString().equals(getResources().getString(R.string.production_items))) {
            // TSnackbar.make(findViewById(R.id.container), getResources().getString(R.string.job_id_validation), TSnackbar.LENGTH_SHORT).show();
            mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.product_select_validation), false);
        } else if (mClientNameEditText.getText().toString().isEmpty()) {
            // TSnackbar.make(findViewById(R.id.container), getResources().getString(R.string.job_id_validation), TSnackbar.LENGTH_SHORT).show();
            mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.client_name_validation), false);
        } else {
            SupervisorAddJobData supervisorAddJobData = new SupervisorAddJobData();
            PreferenceConfiguration.setJobLocation(mLocationEditText.getText().toString(), SupervisorAddNewJobActivity.this);

            if (!mJobDescriptionEditText.getText().toString().equals("")) {
                PreferenceConfiguration.setJobDescription(mJobDescriptionEditText.getText().toString(), SupervisorAddNewJobActivity.this);
                supervisorAddJobData.description = mJobDescriptionEditText.getText().toString() + "";
            }
            PreferenceConfiguration.setJobAssignToForeman(mAssignForemanTextView.getText().toString(), SupervisorAddNewJobActivity.this);
            PreferenceConfiguration.setJobProductionItem1(mProductionItem1.getText().toString(), SupervisorAddNewJobActivity.this);
            PreferenceConfiguration.setJobProductionItem2(mProductionItem2.getText().toString(), SupervisorAddNewJobActivity.this);
            PreferenceConfiguration.setJobProductionItem3(mProductionItem3.getText().toString(), SupervisorAddNewJobActivity.this);


            if (mLatLng != null) {
                PreferenceConfiguration.setJobLatitude(mLatLng.latitude + "", SupervisorAddNewJobActivity.this);
                PreferenceConfiguration.setJobLongitude(mLatLng.longitude + "", SupervisorAddNewJobActivity.this);
                supervisorAddJobData.latitude = mLatLng.latitude + "";
                supervisorAddJobData.longitude = mLatLng.longitude + "";
            }

            ArrayList<ProductDataList> productDataListArrayList = new ArrayList<ProductDataList>();
            ArrayList<ForemanID> foremanIDArrayList = new ArrayList<ForemanID>();
            mUtility.startLoader(SupervisorAddNewJobActivity.this, R.drawable.image_for_rotation);

            supervisorAddJobData.client = mClientNameEditText.getText().toString();
            supervisorAddJobData.job_id = mJobIDEditText.getText().toString() + "";
            supervisorAddJobData.start_date = new SimpleDateFormat(Constants.InputTag.POST_DATE_FORMAT).format(Calendar.getInstance().getTime()) + "";
            supervisorAddJobData.job_location = mLocationEditText.getText().toString() + "";
            supervisorAddJobData.daily_sumbission_report = Constants.InputTag.STATUS_ONE;
            supervisorAddJobData.job_assign_by = PreferenceConfiguration.getParentID(SupervisorAddNewJobActivity.this);
            supervisorAddJobData.job_assign_to = PreferenceConfiguration.getUserID(SupervisorAddNewJobActivity.this);
            //supervisorAddJobData.foremen_id = mForemanID;
            if (mSelectedItemID != null) {
                for (int i = 0; i < mSelectedItemID.size(); i++) {
                    ForemanID foremanID = new ForemanID();
                    foremanID.id = mSelectedItemID.get(i);
                    foremanIDArrayList.add(foremanID);
                }
                supervisorAddJobData.setForemanIDs(foremanIDArrayList);
            }

            if (mJobId != null) {
                supervisorAddJobData.id = mJobId;
            }

           /* if (!mProductionItem3.getText().toString().isEmpty() && !mProductionItem3.getText().toString().equals(getResources().getString(R.string.production_items))) {

                productDataList.setId(mProductID1 + "");
                productDataList.setName(mProductionItem1.getText().toString() + "");
                PreferenceConfiguration.setJobProductionID1(mProductID1 + "", SupervisorAddNewJobActivity.this);
                productDataList.setDescription("description");
                productDataListArrayList.add(productDataList);

                ProductDataList productDataList1 = new ProductDataList();
                productDataList1.setId(mProductID2 + "");
                PreferenceConfiguration.setJobProductionID2(mProductID2 + "", SupervisorAddNewJobActivity.this);
                productDataList1.setName(mProductionItem2.getText().toString() + "");
                productDataList1.setDescription("description");
                productDataListArrayList.add(productDataList1);

                ProductDataList productDataList2 = new ProductDataList();
                productDataList2.setId(mProductID3 + "");
                PreferenceConfiguration.setJobProductionID3(mProductID3 + "", SupervisorAddNewJobActivity.this);
                productDataList2.setName(mProductionItem3.getText().toString() + "");
                productDataList2.setDescription("description");
                productDataListArrayList.add(productDataList2);

            } else if (!mProductionItem2.getText().toString().isEmpty() && !mProductionItem2.getText().toString().equals(getResources().getString(R.string.production_items))) {
                productDataList.setId(mProductID1 + "");
                productDataList.setName(mProductionItem1.getText().toString() + "");
                PreferenceConfiguration.setJobProductionID1(mProductID1 + "", SupervisorAddNewJobActivity.this);
                productDataList.setDescription("description");
                productDataListArrayList.add(productDataList);

                ProductDataList productDataList1 = new ProductDataList();
                productDataList1.setId(mProductID2 + "");
                PreferenceConfiguration.setJobProductionID2(mProductID2 + "", SupervisorAddNewJobActivity.this);
                productDataList1.setName(mProductionItem2.getText().toString() + "");
                productDataList1.setDescription("description");
                productDataListArrayList.add(productDataList1);

            } else if (!mProductionItem1.getText().toString().isEmpty() && !mProductionItem1.getText().toString().equals(getResources().getString(R.string.production_items))) {

                productDataList.setId(mProductID1 + "");
                productDataList.setName(mProductionItem1.getText().toString() + "");
                PreferenceConfiguration.setJobProductionID1(mProductID1 + "", SupervisorAddNewJobActivity.this);
                productDataList.setDescription("description");
                productDataListArrayList.add(productDataList);

            }*/
            if (mSelectedProductionItemID != null) {
                for (int i = 0; i < mSelectedProductionItemID.size(); i++) {
                    ProductDataList productDataList = new ProductDataList();
                    productDataList.id = mSelectedProductionItemID.get(i);
                    productDataList.name = mSelectedProductionItemName.get(i);
                    productDataList.setDescription("description");
                    productDataListArrayList.add(productDataList);
                }
            } else if (mProductDataList != null) {
                for (int i = 0; i < mProductDataList.size(); i++) {
                    ProductDataList productDataList = new ProductDataList();
                    productDataList.id = mProductDataList.get(i).getId();
                    productDataList.name = mProductDataList.get(i).getName();
                    productDataList.setDescription("description");
                    productDataListArrayList.add(productDataList);
                }
            }

            supervisorAddJobData.setProductDataList(productDataListArrayList);

            mSupervisorManager.addNewJob(supervisorAddJobData);

        }
    }

    private void foremanSelectionDialog() {

        // retrieve display dimensions
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        // inflate and adjust layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_foreman_selection, null);
        layout.setMinimumWidth((int) (displayRectangle.width() * 0.8f));
        layout.setMinimumHeight((int) (displayRectangle.height() * 0.8f));
        mDialog = new Dialog(SupervisorAddNewJobActivity.this, R.style.alertDialogCustom);
        mDialog.setContentView(layout);

        //  mDialog.setContentView(R.layout.dialog_foreman_selection);
        mForemanSelectionListView = (ListView) mDialog.findViewById(R.id.foreman_selection_lv);

        // mDialog.setTitle(getResources().getString(R.string.foreman_list));
        mDialog.show();


        mForemanSelectionListAdapter = new ForemanSelectionListAdapter(SupervisorAddNewJobActivity.this, R.layout.foreman_list_item, mForemanArrayList);
        mForemanSelectionListView.setAdapter(mForemanSelectionListAdapter);
        mForemanSelectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAssignForemanTextView.setText(mForemanArrayList.get(position).getFirstname() + " " + mForemanArrayList.get(position).getLastname());
                mForemanID = mForemanArrayList.get(position).getId() + "";
                mDialog.dismiss();
            }
        });
    }

    private void productionItemDialog() {
        // retrieve display dimensions
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        // inflate and adjust layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_product_selection, null);
        layout.setMinimumWidth((int) (displayRectangle.width() * 0.8f));
        layout.setMinimumHeight((int) (displayRectangle.height() * 0.8f));
        mDialog = new Dialog(SupervisorAddNewJobActivity.this, R.style.alertDialogCustom);
        mDialog.setContentView(layout);

        mProductSelectionListView = (ListView) mDialog.findViewById(R.id.product_selection_lv);

        // mDialog.setTitle(getResources().getString(R.string.product_list));
        mDialog.show();


        mProductionListAdapter = new ProductionListAdapter(SupervisorAddNewJobActivity.this, R.layout.product_selection_list_item, mProductDataList);
        mProductSelectionListView.setAdapter(mProductionListAdapter);
        mProductSelectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mViewID == 1) {
                    mProductionItem1.setText(mProductDataList.get(position).getName());
                    mProductID1 = mProductDataList.get(position).getId() + "";
                    mProductionItem1.setTextColor(ContextCompat.getColor(SupervisorAddNewJobActivity.this, R.color.black));
                    mProductionItem2.setVisibility(View.VISIBLE);
                } else if (mViewID == 2) {
                    mProductionItem2.setText(mProductDataList.get(position).getName());
                    mProductID2 = mProductDataList.get(position).getId() + "";
                    mProductionItem2.setTextColor(ContextCompat.getColor(SupervisorAddNewJobActivity.this, R.color.black));
                    mProductionItem3.setVisibility(View.VISIBLE);
                } else if (mViewID == 3) {
                    mProductionItem3.setText(mProductDataList.get(position).getName());
                    mProductID3 = mProductDataList.get(position).getId() + "";
                    mProductionItem3.setTextColor(ContextCompat.getColor(SupervisorAddNewJobActivity.this, R.color.black));
                }
                mDialog.dismiss();
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        initMapSettings();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        copyPreviousJob(isChecked);
    }


    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        private int downloadType = 0;

        // Constructor
        public DownloadTask(int type) {
            this.downloadType = type;
        }

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            switch (downloadType) {
                case PLACES:
                    // Creating ParserTask for parsing Google Places
                    placesParserTask = new ParserTask(PLACES);

                    // Start parsing google places json data
                    // This causes to execute doInBackground() of ParserTask class
                    placesParserTask.execute(result);

                    break;

                case PLACES_DETAILS:
                    // Creating ParserTask for parsing Google Places
                    placeDetailsParserTask = new ParserTask(PLACES_DETAILS);

                    // Starting Parsing the JSON string
                    // This causes to execute doInBackground() of ParserTask class
                    placeDetailsParserTask.execute(result);
            }
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        int parserType = 0;

        public ParserTask(int type) {
            this.parserType = type;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<HashMap<String, String>> list = null;

            try {
                jObject = new JSONObject(jsonData[0]);

                switch (parserType) {
                    case PLACES:
                        PlaceJSONParser placeJsonParser = new PlaceJSONParser();
                        // Getting the parsed data as a List construct
                        list = placeJsonParser.parse(jObject);
                        break;
                    case PLACES_DETAILS:
                        PlaceDetailsJSONParser placeDetailsJsonParser = new PlaceDetailsJSONParser();
                        // Getting the parsed data as a List construct
                        list = placeDetailsJsonParser.parse(jObject);
                }

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            switch (parserType) {
                case PLACES:
                    String[] from = new String[]{"description"};
                    int[] to = new int[]{android.R.id.text1};
                    mProgressBar.setVisibility(View.GONE);
                    // Creating a SimpleAdapter for the AutoCompleteTextView
                    SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);

                    // Setting the adapter
                    if (result != null) {
                        mLocationEditText.setAdapter(adapter);
                    }
                    break;
                case PLACES_DETAILS:
                    HashMap<String, String> hm = result.get(0);

                    // Getting latitude from the parsed data
                    double latitude = Double.parseDouble(hm.get("lat"));

                    // Getting longitude from the parsed data
                    double longitude = Double.parseDouble(hm.get("lng"));

                    mLatLng = new LatLng(latitude, longitude);
                    initCamera();


                    break;
            }
        }

    }

    private void initMapSettings() {
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        //mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        //if (ContextCompat.checkSelfPermission(SupervisorAddNewJobActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
        //   == PackageManager.PERMISSION_GRANTED) {
        //  mGoogleMap.setMyLocationEnabled(true);
        //}
    }

    private void initCamera() {
        if (mLatLng != null) {
            CameraPosition position = CameraPosition.builder()
                    .target(mLatLng)
                    .zoom(getInitialMapZoomLevel())
                    .build();

            MarkerOptions options = new MarkerOptions();
            options.position(mLatLng);
            options.title(mLocationEditText.getText().toString());
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
        return 17.0f;
    }


    protected void initMapIfNecessary() {
        if (mGoogleMap != null) {
            return;
        }
        //mGoogleMap = ( (MapFragment) getFragmentManager().findFragmentById( R.id.map ) ).getMap();
        SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.location_map));
        mapFragment.getMapAsync(this);
    }

    public void copyPreviousJob(boolean isChecked) {

        if (isChecked && PreferenceConfiguration.getJobLocation(SupervisorAddNewJobActivity.this).equals("")) {
            mLocationEditText.setHint(getResources().getString(R.string.location_hint));

        } else if (!isChecked && PreferenceConfiguration.getJobLocation(SupervisorAddNewJobActivity.this).equals("")) {
            mLocationEditText.setHint(getResources().getString(R.string.location_hint));

        } else {
            mLocationEditText.setText(PreferenceConfiguration.getJobLocation(SupervisorAddNewJobActivity.this) + "");
        }

        if (isChecked && PreferenceConfiguration.getJobDescription(SupervisorAddNewJobActivity.this).equals("")) {
            mJobDescriptionEditText.setHint(getResources().getString(R.string.description_hint));
        } else if (!isChecked && PreferenceConfiguration.getJobDescription(SupervisorAddNewJobActivity.this).equals("")) {
            mJobDescriptionEditText.setHint(getResources().getString(R.string.description_hint));
        } else {
            mJobDescriptionEditText.setText(PreferenceConfiguration.getJobDescription(SupervisorAddNewJobActivity.this) + "");
        }

        if (isChecked && PreferenceConfiguration.getJobAssignToForeman(SupervisorAddNewJobActivity.this).equals("")) {
            mAssignForemanTextView.setText(getResources().getString(R.string.assign_to_foreman));
        } else if (!isChecked && PreferenceConfiguration.getJobAssignToForeman(SupervisorAddNewJobActivity.this).equals("")) {
            mAssignForemanTextView.setText(getResources().getString(R.string.assign_to_foreman));
        } else {
            mAssignForemanTextView.setText(PreferenceConfiguration.getJobAssignToForeman(SupervisorAddNewJobActivity.this) + "");
        }

        if (isChecked && PreferenceConfiguration.getJobProductionItem1(SupervisorAddNewJobActivity.this).equals("")) {
            mProductionItem1.setText(getResources().getString(R.string.production_items));
        } else if (!isChecked && PreferenceConfiguration.getJobProductionItem1(SupervisorAddNewJobActivity.this).equals("")) {
            mProductionItem1.setText(getResources().getString(R.string.production_items));
        } else {
            mProductionItem1.setText(PreferenceConfiguration.getJobProductionItem1(SupervisorAddNewJobActivity.this) + "");
            mProductID1 = PreferenceConfiguration.getJobProductionID1(SupervisorAddNewJobActivity.this);
            mProductionItem2.setVisibility(View.VISIBLE);
        }

        if (isChecked && PreferenceConfiguration.getJobProductionItem2(SupervisorAddNewJobActivity.this).equals("")) {
            mProductionItem2.setText(getResources().getString(R.string.production_items));
        } else if (!isChecked && PreferenceConfiguration.getJobProductionItem2(SupervisorAddNewJobActivity.this).equals("")) {
            mProductionItem2.setText(getResources().getString(R.string.production_items));
        } else {
            mProductionItem2.setText(PreferenceConfiguration.getJobProductionItem2(SupervisorAddNewJobActivity.this) + "");
            mProductID2 = PreferenceConfiguration.getJobProductionID2(SupervisorAddNewJobActivity.this);
            mProductionItem3.setVisibility(View.VISIBLE);
        }

        if (isChecked && PreferenceConfiguration.getJobProductionItem3(SupervisorAddNewJobActivity.this).equals("")) {
            mProductionItem3.setText(getResources().getString(R.string.production_items));
        } else if (!isChecked && PreferenceConfiguration.getJobProductionItem3(SupervisorAddNewJobActivity.this).equals("")) {
            mProductionItem3.setText(getResources().getString(R.string.production_items));
        } else {
            mProductionItem3.setText(PreferenceConfiguration.getJobProductionItem3(SupervisorAddNewJobActivity.this) + "");
            mProductID3 = PreferenceConfiguration.getJobProductionID3(SupervisorAddNewJobActivity.this);
        }
        if (!PreferenceConfiguration.getJobLatitude(SupervisorAddNewJobActivity.this).equals("") && !PreferenceConfiguration.getJobLongitude(SupervisorAddNewJobActivity.this).equals("")) {
            mLatLng = new LatLng(Double.parseDouble(PreferenceConfiguration.getJobLatitude(SupervisorAddNewJobActivity.this)), Double.parseDouble(PreferenceConfiguration.getJobLongitude(SupervisorAddNewJobActivity.this)));
            initCamera();
        }
    }


    private void getJobDetails(String id) {
        if (id != null) {
            mUtility.startLoader(SupervisorAddNewJobActivity.this, R.drawable.image_for_rotation);
            mSupervisorManager.getJobDetails(id);
        }
    }

    public void setJobDetails(SupervisorJobDetails supervisorJobDetails) {
        if (supervisorJobDetails != null) {

            if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getStart_date() != null) {
                mDateTextView.setText(mUtility.changeDate(supervisorJobDetails.getJobDetailsData().getJobDetailsData().getStart_date()));
            }

            if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getJob_id() != null) {
                mJobIDEditText.setText(supervisorJobDetails.getJobDetailsData().getJobDetailsData().getJob_id());
            }

            if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getClient() != null) {
                mClientNameEditText.setText(supervisorJobDetails.getJobDetailsData().getJobDetailsData().getClient());
            }

            if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getJob_location() != null) {
                mLocationEditText.setText(supervisorJobDetails.getJobDetailsData().getJobDetailsData().getJob_location());
            } else {
                mLocationEditText.setText(getResources().getString(R.string.location_hint));
            }

            if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getDescription() != null) {
                mJobDescriptionEditText.setText(supervisorJobDetails.getJobDetailsData().getJobDetailsData().getDescription());
            } else {
                mJobDescriptionEditText.setText(getResources().getString(R.string.description_hint));
            }

            final ArrayList<String> jobFunctionList = new ArrayList<String>();
            final ArrayList<String> jobFunctionId = new ArrayList<String>();
            if (supervisorJobDetails.getJobDetailsData().getForemanDetailsArrayList() != null && supervisorJobDetails.getJobDetailsData().getForemanDetailsArrayList().size() > 0) {
                mForemanArrayList = supervisorJobDetails.getJobDetailsData().getForemanDetailsArrayList();
                for (int i = 0; i < mForemanArrayList.size(); i++) {
                    jobFunctionList.add(mForemanArrayList.get(i).getFirstname() + " " + mForemanArrayList.get(i).getLastname());
                    jobFunctionId.add(mForemanArrayList.get(i).getId());
                    checkedItems = new boolean[jobFunctionId.size()];
                }
                mForemanNames = TextUtils.join(",", jobFunctionList);
                mAssignForemanTextView.setText(mForemanNames);
            } else {
                mAssignForemanTextView.setText(getResources().getString(R.string.assign_to_foreman));
            }


            final ArrayList<String> productionList = new ArrayList<String>();
            final ArrayList<String> productionId = new ArrayList<String>();
            if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo() != null && supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().size() > 0) {
                mProductDataList = supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems();
                for (int i = 0; i < mProductDataList.size(); i++) {
                    productionList.add(mProductDataList.get(i).getName());
                    productionId.add(mProductDataList.get(i).getId());
                    productionCheckedItems = new boolean[productionId.size()];
                }
                mProductionItemNames = TextUtils.join(",", productionList);
                mProductionItem1.setText(mProductionItemNames);
            } else {
                mProductionItem1.setText(getResources().getString(R.string.production_items));
            }


           /* if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo() != null) {
                if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().size() == 1) {

                    if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().get(0).getName() != null) {
                        mProductionItem1.setText(supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().get(0).getName());
                    } else {
                        mProductionItem1.setText(getResources().getString(R.string.production_items));
                    }
                }
                if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().size() == 2) {
                    if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().get(0).getName() != null) {
                        mProductionItem1.setText(supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().get(0).getName());
                    } else {
                        mProductionItem1.setText(getResources().getString(R.string.production_items));
                    }
                    if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().get(1).getName() != null) {
                        mProductionItem2.setVisibility(View.VISIBLE);
                        mProductionItem2.setText(supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().get(1).getName());
                    } else {
                        mProductionItem2.setText(getResources().getString(R.string.production_items));
                    }
                }
                if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().size() > 2) {
                    if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().get(0).getName() != null) {
                        mProductionItem1.setText(supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().get(0).getName());
                    } else {
                        mProductionItem1.setText(getResources().getString(R.string.production_items));
                    }
                    if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().get(1).getName() != null) {
                        mProductionItem2.setVisibility(View.VISIBLE);
                        mProductionItem2.setText(supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().get(1).getName());
                    } else {
                        mProductionItem2.setText(getResources().getString(R.string.production_items));
                    }
                    if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().get(2).getName() != null) {
                        mProductionItem3.setVisibility(View.VISIBLE);
                        mProductionItem3.setText(supervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().get(2).getName());
                    } else {
                        mProductionItem3.setText(getResources().getString(R.string.production_items));
                    }
                }

            }*/
            if (supervisorJobDetails.getJobDetailsData().getJobDetailsData().getLatitude() != null && supervisorJobDetails.getJobDetailsData().getJobDetailsData().getLongitude() != null) {
                mLatLng = new LatLng(Double.parseDouble(supervisorJobDetails.getJobDetailsData().getJobDetailsData().getLatitude()), Double.parseDouble(supervisorJobDetails.getJobDetailsData().getJobDetailsData().getLongitude()));
                initCamera();
            }

        }
    }

    public void foremanMultiSelectionDialog(ArrayList<ForemanDataList> mForemanArrayList) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(SupervisorAddNewJobActivity.this);
        dialog.setTitle(getResources().getString(R.string.foreman_list));
        final ArrayList<String> jobFunctionList = new ArrayList<String>();
        final ArrayList<String> jobFunctionId = new ArrayList<String>();
        final ArrayList<String> jobFunctionId1 = new ArrayList<String>();
        final ArrayList<String> mSelectedItems = new ArrayList();
        mSelectedItemID = new ArrayList();
        for (int i = 0; i < mForemanArrayList.size(); i++) {
            jobFunctionList.add(mForemanArrayList.get(i).getFirstname() + " " + mForemanArrayList.get(i).getLastname());
            jobFunctionId.add(mForemanArrayList.get(i).getId());
        }

        element = jobFunctionList.toArray(new String[jobFunctionList.size()]);
        checkedItems = new boolean[jobFunctionId.size()];
        if (mSupervisorJobDetails != null) {

            for (int i = 0; i < mSupervisorJobDetails.getJobDetailsData().getForemanDetailsArrayList().size(); i++) {
                jobFunctionId1.add(mSupervisorJobDetails.getJobDetailsData().getForemanDetailsArrayList().get(i).getId());
            }

            Collections.sort(jobFunctionId);
            Collections.reverse(jobFunctionId);
            Collections.sort(jobFunctionId1);
            jobFunctionId1.retainAll(jobFunctionId);

            for (int i = 0; i < jobFunctionId1.size(); i++) {

                for (int j = 0; j < jobFunctionId.size(); j++)

                    if (jobFunctionId1.get(i).contains(jobFunctionId.get(j))) {
                        checkedItems[j] = true;
                        //Log.e("position:", i + " <-i j->" + j);
                        mSelectedItems.add(jobFunctionList.get(j));
                        mSelectedItemID.add(jobFunctionId.get(j));
                    }
            }

        }


        dialog.setMultiChoiceItems(element, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                // checkedItems[which] = isChecked;
               /* checkedItems[which] = false;
                ((AlertDialog) dialog).getListView().setItemChecked(which, false);*/
                if (isChecked) {
                    // If the user checked the item, build an array containing the selected items _id's.
                    mSelectedItems.add(jobFunctionList.get(which));
                    mSelectedItemID.add(jobFunctionId.get(which));
                } else {
                    // Else, if the user changes his mind and de-selects an item, remove it
                    mSelectedItems.remove(jobFunctionList.get(which));
                    mSelectedItemID.remove(jobFunctionId.get(which));
                }

            }
        });
        dialog.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Log.e("mJobFunctions", mJobFunctions);
                // mJobID = TextUtils.join(",", mSelectedItemID);
                //Log.e("mJobFunctions", mJobID);
                if (mSelectedItems.size() > 0) {
                    mForemanNames = TextUtils.join(",", mSelectedItems);
                    mAssignForemanTextView.setText(mForemanNames);
                } else {
                    mAssignForemanTextView.setText(getResources().getString(R.string.assign_to_foreman));
                }
            }
        });

        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSelectedItems.clear();
                mSelectedItemID.clear();

            }
        });
        dialog.show();
    }

    public void productionMultiSelectionDialog(ArrayList<ProductDataList> productDataList) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(SupervisorAddNewJobActivity.this);
        dialog.setTitle(getResources().getString(R.string.product_list));
        final ArrayList<String> jobFunctionList = new ArrayList<String>();
        final ArrayList<String> jobFunctionId = new ArrayList<String>();
        final ArrayList<String> jobFunctionId1 = new ArrayList<String>();
        final ArrayList<String> selectedItems = new ArrayList();
        mSelectedProductionItemID = new ArrayList();
        mSelectedProductionItemName = new ArrayList<>();
        for (int i = 0; i < productDataList.size(); i++) {
            jobFunctionList.add(productDataList.get(i).getName());
            jobFunctionId.add(productDataList.get(i).getId());
        }

        productionElement = jobFunctionList.toArray(new String[jobFunctionList.size()]);
        productionCheckedItems = new boolean[jobFunctionId.size()];
        if (mSupervisorJobDetails != null) {
            if (mSupervisorJobDetails.getJobDetailsData().getForemanDetailsArrayList() != null && mSupervisorJobDetails.getJobDetailsData().getForemanDetailsArrayList().size() > 0) {

                for (int i = 0; i < mSupervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().size(); i++) {
                    jobFunctionId1.add(mSupervisorJobDetails.getJobDetailsData().getJobDetailsData().getBillableInfo().getBillableItems().get(i).getId());
                }

                Collections.sort(jobFunctionId);
                Collections.reverse(jobFunctionId);
                Collections.sort(jobFunctionId1);
                jobFunctionId1.retainAll(jobFunctionId);

                for (int i = 0; i < jobFunctionId1.size(); i++) {

                    for (int j = 0; j < jobFunctionId.size(); j++)

                        if (jobFunctionId1.get(i).contains(jobFunctionId.get(j))) {
                            productionCheckedItems[j] = true;
                            // Log.e("position:", i + " <-i j->" + j);
                            selectedItems.add(jobFunctionList.get(j));
                            mSelectedProductionItemID.add(jobFunctionId.get(j));
                            mSelectedProductionItemName.add(jobFunctionList.get(j));
                        }
                }
            }
        }


        dialog.setMultiChoiceItems(productionElement, productionCheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                // checkedItems[which] = isChecked;
               /* checkedItems[which] = false;
                ((AlertDialog) dialog).getListView().setItemChecked(which, false);*/
                if (isChecked) {
                    // If the user checked the item, build an array containing the selected items _id's.
                    selectedItems.add(jobFunctionList.get(which));
                    mSelectedProductionItemID.add(jobFunctionId.get(which));
                    mSelectedProductionItemName.add(jobFunctionList.get(which));
                } else {
                    // Else, if the user changes his mind and de-selects an item, remove it
                    selectedItems.remove(jobFunctionList.get(which));
                    mSelectedProductionItemID.remove(jobFunctionId.get(which));
                    mSelectedProductionItemName.remove(jobFunctionList.get(which));
                }

            }
        });
        dialog.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Log.e("mJobFunctions", mJobFunctions);
                // mJobID = TextUtils.join(",", mSelectedItemID);
                //Log.e("mJobFunctions", mJobID);
                if (selectedItems.size() > 0) {
                    mProductionItemNames = TextUtils.join(",", selectedItems);
                    mProductionItem1.setText(mProductionItemNames);
                    // mProductID1 = mProductDataList.get(0).getId() + "";
                    mProductionItem1.setTextColor(ContextCompat.getColor(SupervisorAddNewJobActivity.this, R.color.black));
                } else {
                    mProductionItem1.setText(getResources().getString(R.string.production_items));
                }
            }
        });

        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedItems.clear();
                mSelectedProductionItemID.clear();
                mSelectedProductionItemName.clear();
            }
        });
        dialog.show();
    }
}
