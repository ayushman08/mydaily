package com.smartdata.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.smartdata.adapters.CrewsSelectionListAdapter;
import com.smartdata.adapters.JobSelectionListAdapter;
import com.smartdata.adapters.ProductionListAdapter;
import com.smartdata.adapters.SupervisorSelectionListAdapter;
import com.smartdata.config.PreferenceConfiguration;
import com.smartdata.dataobject.AppInstance;
import com.smartdata.dataobject.BillableItems;
import com.smartdata.dataobject.CrewsData;
import com.smartdata.dataobject.CrewsDataList;
import com.smartdata.dataobject.CrewsDetails;
import com.smartdata.dataobject.DailiesImage;
import com.smartdata.dataobject.ForemanJobData;
import com.smartdata.dataobject.ForemanJobDataList;
import com.smartdata.dataobject.MyDailyData;
import com.smartdata.dataobject.MyDailyDataList;
import com.smartdata.dataobject.MyDailyDetails;
import com.smartdata.dataobject.MyDailyResponse;
import com.smartdata.dataobject.ProductDataList;
import com.smartdata.dataobject.ResponseData;
import com.smartdata.dataobject.SupervisorData;
import com.smartdata.dataobject.SupervisorDataList;
import com.smartdata.dataobject.SupervisorDetails;
import com.smartdata.dialogs.AlertDialogManager;
import com.smartdata.fragments.DailyImagePagerFragment;
import com.smartdata.fragments.ImagePagerFragment;
import com.smartdata.interfaces.DialogActionCallback;
import com.smartdata.interfaces.ExifUtil;
import com.smartdata.interfaces.ServiceRedirection;
import com.smartdata.managers.LoginManager;
import com.smartdata.managers.SupervisorManager;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.RoundRectCornerImageView;
import com.smartdata.utils.Utility;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * Created by Anurag Sethi
 * The activity is used for handling the login screen actions
 */
public class UpdateDailyActivity extends AppActivity implements ServiceRedirection, View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback {

    private Utility mUtility;
    private AppCompatButton mSendButton;
    private EditText mEmailEditText;
    private LoginManager mLoginManager;
    private AlertDialogManager mAleaAlertDialogManager;
    private MyDailyDataList mMyDailyDataList;
    private SupervisorDetails mSupervisorDetails;
    private CrewsDetails mCrewsDetails;
    private ArrayList<BillableItems> mBillableItems;
    private SupervisorManager mSupervisorManager;
    private MyDailyDetails mMyDailyDetails;
    private ArrayList<DailiesImage> mDailiesImageArrayList;
    private MyDailyResponse mMyDailyResponse;
    private ResponseData responseData;

    private TextView mJobSelectionTextView, mWorkDateTextView, mToWorkDateTextView, mProductionTextView, mProductionTextView1, mProductionTextView2, mDailyDateTextView,
            mDailyJobIDTextView, mGPSTextView, mSupervisorTextView, mCrewTextView, mRejectionDetailsTextView;
    private EditText mNotesEditText, mJobMapEditText, mJobLocationTicketEditText, mProductQuantityEditText, mProductQuantityEditText1, mProductQuantityEditText2;
    private LinearLayout mDailyLinearLayout, mDailyOtherLinearLayout;
    private ImageView mAddImageView;
    private RoundRectCornerImageView imageView1, imageView2, imageView3, imageView4, imageView5;
    private AppCompatCheckBox mProductionCheckbox;
    private int mReadPermissionCheck, mWritePermissionCheck, mCameraPermissonCheck, mFineLocation, mCoarseLocation;
    private boolean isProduction = false, isContinue = true;

    private Uri mImageUri;
    private Dialog mDialog;
    private String path, filePath, mProductID1, mProductID2, mProductID3, mCrewID, mSupervisorID = null, mJobID;
    private File mLastFile;

    private ProductionListAdapter mProductionListAdapter;
    private CrewsSelectionListAdapter mCrewsSelectionListAdapter;
    private SupervisorSelectionListAdapter mSupervisorSelectionListAdapter;
    private JobSelectionListAdapter mJobSelectionListAdapter;

    private ArrayList<ForemanJobDataList> mForemanJobDataList;
    private ForemanJobDataList mForemanJobData;

    private ArrayList<SupervisorDataList> mSupervisorDataList;
    private ArrayList<CrewsDataList> mCrewsDataList;
    private ArrayList<ProductDataList> mProductDataList;
    private ArrayList<Bitmap> mAddedImageArrayList = new ArrayList<>();
    private ArrayList<File> mFileArrayList = new ArrayList<>();
    //datepicker components
    private int mYear;
    private int mMonth;
    private int mDay;
    private String month;
    private SimpleDateFormat mDateFormatter;

    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private int mViewID = 0;
    private ProgressBar mProgressBar1, mProgressBar2, mProgressBar3, mProgressBar4, mProgressBar5;
    private LinearLayout mProductionLinearLayout1, mProductionLinearLayout2;
    //  private int mImagePosition = -1;

    //location variable
    private LatLng mLatLng;
    private GoogleApiClient mGoogleApiClient;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    protected static final int REQUEST_CHECK_SETTINGS = 171;

    private Date mFromDate = null, mToDate = null;
    private boolean isLocation = true, mIsTextChange = true;
    private int mFileSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_daily);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initViews();
        bindViews();
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
        //settingsrequest();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                startLocationUpdates();
            }
        }
    }

    /**
     * Default activity life cycle method
     */
    @Override
    public void onStop() {
        super.onStop();


    }


    private void bindViews() {

        mSendButton = (AppCompatButton) findViewById(R.id.sendButton);
        if (mMyDailyDataList.getStatus() != null && mMyDailyDataList.getStatus().equals(Constants.RequestCodes.REQ_CODE_DAILY_REJECTED)) {
            mSendButton.setText(getResources().getString(R.string.resubmit));
        } else if (mMyDailyDataList.is_draft()) {
            //<Dev comment="Added by kapil">
            mSendButton.setText(getResources().getString(R.string.submit));
            //<Dev comment>

        } else {
            mSendButton.setText(getResources().getString(R.string.update));
        }

        mSendButton.setOnClickListener(this);

        mDailyLinearLayout = (LinearLayout) findViewById(R.id.daily_layout);
        mDailyOtherLinearLayout = (LinearLayout) findViewById(R.id.daily_other_layout);

        mDailyDateTextView = (TextView) findViewById(R.id.daily_date_tv);
        // mDailyDateTextView.setText(new SimpleDateFormat(Constants.InputTag.DATE_FORMAT).format(Calendar.getInstance().getTime()) + "");
        //textViewTextChangeListener(mDailyDateTextView);
        if (mMyDailyDataList != null) {
            mDailyDateTextView.setText("#" + mMyDailyDataList.getDaily_number());
        }

        mDailyJobIDTextView = (TextView) findViewById(R.id.job_id_tv);
        textViewTextChangeListener(mDailyJobIDTextView);

        mGPSTextView = (TextView) findViewById(R.id.gps_tv);
        textViewTextChangeListener(mGPSTextView);
        mGPSTextView.setOnClickListener(this);

        
         //comment by Aniket on 17th Apr 2018
        /*if (mMyDailyDataList.status.equals(Constants.RequestCodes.REQ_CODE_DAILY_REJECTED)) {
            mGPSTextView.setOnClickListener(this);
        }*/
        mSupervisorTextView = (TextView) findViewById(R.id.supervisor_tv);
        textViewTextChangeListener(mSupervisorTextView);
        mSupervisorTextView.setOnClickListener(this);

        mCrewTextView = (TextView) findViewById(R.id.crew_tv);
        textViewTextChangeListener(mCrewTextView);
        mCrewTextView.setOnClickListener(this);

        mJobMapEditText = (EditText) findViewById(R.id.job_map_et);
        mJobLocationTicketEditText = (EditText) findViewById(R.id.job_location_ticket_et);
        mNotesEditText = (EditText) findViewById(R.id.notes_et);

        textChangeListener(mJobMapEditText);
        textChangeListener(mJobLocationTicketEditText);
        textChangeListener(mNotesEditText);

        mProductionLinearLayout1 = (LinearLayout) findViewById(R.id.production_ll1);
        mProductionLinearLayout2 = (LinearLayout) findViewById(R.id.production_ll2);

        mDailyJobIDTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    mSendButton.setBackgroundDrawable(ContextCompat.getDrawable(UpdateDailyActivity.this, R.drawable.button_active));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mProductQuantityEditText = (EditText) findViewById(R.id.product_quantity_et);
        mProductQuantityEditText1 = (EditText) findViewById(R.id.product_quantity_et1);
        mProductQuantityEditText2 = (EditText) findViewById(R.id.product_quantity_et2);
        //mProductQuantityTextView.setOnClickListener(this);
        textChangeListener(mProductQuantityEditText);
        textChangeListener(mProductQuantityEditText1);
        textChangeListener(mProductQuantityEditText2);

        mAddImageView = (ImageView) findViewById(R.id.add_image_iv);
        imageView1 = (RoundRectCornerImageView) findViewById(R.id.imageView1);
        imageView2 = (RoundRectCornerImageView) findViewById(R.id.imageView2);
        imageView3 = (RoundRectCornerImageView) findViewById(R.id.imageView3);
        imageView4 = (RoundRectCornerImageView) findViewById(R.id.imageView4);
        imageView5 = (RoundRectCornerImageView) findViewById(R.id.imageView5);

        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        imageView4.setOnClickListener(this);
        imageView5.setOnClickListener(this);
        mAddImageView.setOnClickListener(this);

        mProgressBar1 = (ProgressBar) findViewById(R.id.progress_bar1);
        mProgressBar2 = (ProgressBar) findViewById(R.id.progress_bar2);
        mProgressBar3 = (ProgressBar) findViewById(R.id.progress_bar3);
        mProgressBar4 = (ProgressBar) findViewById(R.id.progress_bar4);
        mProgressBar5 = (ProgressBar) findViewById(R.id.progress_bar5);

        mJobSelectionTextView = (TextView) findViewById(R.id.job_selection_tv);
        textViewTextChangeListener(mJobSelectionTextView);
        mJobSelectionTextView.setOnClickListener(this);

        mWorkDateTextView = (TextView) findViewById(R.id.work_date_tv);
        textViewTextChangeListener(mWorkDateTextView);
        mWorkDateTextView.setOnClickListener(this);

        mToWorkDateTextView = (TextView) findViewById(R.id.to_work_date_tv);
        textViewTextChangeListener(mToWorkDateTextView);
        mToWorkDateTextView.setOnClickListener(this);

        mWorkDateTextView.setText(new SimpleDateFormat(Constants.InputTag.NEW_DAILY_DATE_FORMAT).format(Calendar.getInstance().getTime()));
        mFromDate = Calendar.getInstance().getTime();

        mProductionTextView = (TextView) findViewById(R.id.production_item_tv);
        textViewTextChangeListener(mProductionTextView);
        mProductionTextView.setOnClickListener(this);

        mProductionTextView1 = (TextView) findViewById(R.id.production_item_tv1);
        textViewTextChangeListener(mProductionTextView1);
        mProductionTextView1.setOnClickListener(this);

        mProductionTextView2 = (TextView) findViewById(R.id.production_item_tv2);
        textViewTextChangeListener(mProductionTextView2);
        mProductionTextView2.setOnClickListener(this);

        mRejectionDetailsTextView = (TextView) findViewById(R.id.rejection_details_text_view);


        setDailyData(mMyDailyDataList);
        setIncidentImages(mDailiesImageArrayList);
    }


    private void initViews() {
        mUtility = new Utility(UpdateDailyActivity.this);

        this.imageLoader = ImageLoader.getInstance();
        this.imageLoader.init(ImageLoaderConfiguration.createDefault(UpdateDailyActivity.this));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_insert_photo)
                .showImageForEmptyUri(R.drawable.ic_insert_photo)
                .showImageOnFail(R.drawable.ic_insert_photo).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();


        mMyDailyDataList = getIntent().getParcelableExtra(Constants.RequestCodes.REQ_CODE_DAILY_DATA);
        if (mMyDailyDataList != null)
            mMyDailyDataList.setIs_draft(getIntent().getBooleanExtra(Constants.RequestCodes.REQ_IS_DRAFT, false));

        if (mMyDailyDataList.getStatus() != null && mMyDailyDataList.getStatus().equals(Constants.RequestCodes.REQ_CODE_DAILY_REJECTED)) {
            mUtility.customActionBar(UpdateDailyActivity.this, true, R.string.rejected_daily);
        } else {
            mUtility.customActionBar(UpdateDailyActivity.this, true, R.string.update_daily);
        }
        mSupervisorDetails = getIntent().getParcelableExtra(Constants.RequestCodes.REQ_CODE_SUPERVISOR_DAILY_DATA);
        mCrewsDetails = getIntent().getParcelableExtra(Constants.RequestCodes.REQ_CODE_CREW_DAILY_DATA);
        mBillableItems = getIntent().getParcelableArrayListExtra(Constants.RequestCodes.REQ_CODE_BILLABLE_DAILY_DATA);
        mDailiesImageArrayList = getIntent().getParcelableArrayListExtra(Constants.RequestCodes.REQ_CODE_IMAGE_DAILY_DATA);
        mMyDailyDetails = getIntent().getParcelableExtra(Constants.RequestCodes.REQ_CODE_JOB_DAILY_DATA);

        MyApplication myApplication = ((MyApplication) getApplicationContext());
        myApplication.setmDailiesImageArrayList(mDailiesImageArrayList);


        mFineLocation = ContextCompat.checkSelfPermission(UpdateDailyActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        mCoarseLocation = ContextCompat.checkSelfPermission(UpdateDailyActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if ((mFineLocation != PackageManager.PERMISSION_GRANTED) && (mCoarseLocation != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(
                    UpdateDailyActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Constants.RequestCodes.REQ_CODE_ACCESS_FINE_LOCATION);
        }


        mSupervisorManager = new SupervisorManager(UpdateDailyActivity.this, this);
        mWritePermissionCheck = ContextCompat.checkSelfPermission(UpdateDailyActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        mReadPermissionCheck = ContextCompat.checkSelfPermission(UpdateDailyActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        mCameraPermissonCheck = ContextCompat.checkSelfPermission(UpdateDailyActivity.this, Manifest.permission.CAMERA);

        mProductionCheckbox = (AppCompatCheckBox) findViewById(R.id.production_checkbox);
        mProductionCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isProduction = true;
                    mDailyLinearLayout.setVisibility(View.GONE);
                    mDailyOtherLinearLayout.setVisibility(View.GONE);
                    mNotesEditText.setHeight(500);
                } else {
                    isProduction = false;
                    mDailyLinearLayout.setVisibility(View.VISIBLE);
                    mDailyOtherLinearLayout.setVisibility(View.VISIBLE);
                    mNotesEditText.setHeight(150);
                }
            }
        });


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

            if (taskID == Constants.TaskID.GET_FOREMAN_JOB_LIST) {
                mForemanJobDataList = AppInstance.foremanJobData.foremanJobDataList;
                if (mForemanJobDataList.size() > 0) {
                    jobSelectionDialog();
                } else {
                    mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.no_jobs), false);
                }
            } else if (taskID == Constants.TaskID.GET_PRODUCTION_LIST) {

                mProductDataList = AppInstance.productData.productDataList;
                if (mProductDataList.size() > 0) {
                    productionItemDialog();
                } else {
                    mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.no_products), false);
                }
            } else if (taskID == Constants.TaskID.ADD_NEW_DAILY) {
                mMyDailyResponse = AppInstance.myDailyResponse;
                if (mMyDailyResponse.getCode() == 200) {
                    if (!isProduction && mFileArrayList != null && mFileArrayList.size() > 0) {
                        if (isContinue) {
                            mUtility.startLoader(UpdateDailyActivity.this, R.drawable.image_for_rotation);

                            for (int i = 0; i < mFileArrayList.size(); i++) {
                                // mFileListSize.add(mFileArrayList.get(i));
                                isContinue = false;
                               /* if (mLastFile.equals(mFileArrayList.get(i))) {
                                    Utility.showAlertDialog(UpdateDailyActivity.this, 0, R.string.your_daily_has_been_submitted, R.string.Ok, 0, new DialogActionCallback() {
                                        @Override
                                        public void doOnPositive() {
                                            //reset screen
                                            finish();
                                        }

                                        @Override
                                        public void doOnNegative() {

                                        }
                                    });

                                }*/
                                uploadDailiesImage(mFileArrayList.get(i), mMyDailyResponse.getMyDailyResponseData().getId());
                            }
                        }
                    } else {
                        callAlertDialog();
                    }

                } else {
                    mUtility.showSnackBarAlert(findViewById(R.id.container), mMyDailyResponse.getMessage(), false);
                }

            } else if (taskID == Constants.TaskID.UPLOAD_DAILIES_IMAGE) {
                if (!isProduction) {
                    responseData = AppInstance.responseData;
                    if (responseData.getCode() == Constants.ResponseCodes.RESPONSE_SUCCESS) {
                        isContinue = true;
                        if (mFileArrayList.size() > 0) {
                            mFileSize++;
                            if (mFileSize == mFileArrayList.size()) {
                                callAlertDialog();
                            }
                        } else {
                            callAlertDialog();
                        }
                    }
                }
            } else if (taskID == Constants.TaskID.GET_CREWS_LIST) {
                mUtility.stopLoader();
                mCrewsDataList = AppInstance.crewsData.crewsDataList;
                if (mCrewsDataList.size() > 0) {
                    crewListDialog();
                } else {
                    mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.no_crew_members), false);
                }
            } else if (taskID == Constants.TaskID.GET_SUPERVISOR_LIST) {
                mSupervisorDataList = AppInstance.supervisorData.getSupervisorDataList();
                if (mSupervisorDataList.size() > 0) {
                    supervisorListDialog();
                } else {
                    mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.no_supervisors), false);
                }
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
                if (mIsTextChange) {
                    Utility.showAlertDialog(UpdateDailyActivity.this, 0, R.string.your_daily_discard_alert_message, R.string.yes, R.string.no, new DialogActionCallback() {
                        @Override
                        public void doOnPositive() {
                            finish();
                        }

                        @Override
                        public void doOnNegative() {

                        }
                    });
                } else {
                    finish();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void addNewDaily() {
        if (mFileArrayList != null) {
            if (mFileArrayList.size() > 0) {
                mLastFile = mFileArrayList.get(mFileArrayList.size() - 1);
            }
        }
        MyDailyData myDailyData = new MyDailyData();
        myDailyData.user_id = PreferenceConfiguration.getUserID(UpdateDailyActivity.this);
        myDailyData.parent_id = PreferenceConfiguration.getParentID(UpdateDailyActivity.this);
        myDailyData.notes = mNotesEditText.getText().toString();
        if (mForemanJobData != null || mJobID != null) {
            if (isProduction) {
                //myDailyData.crews_id = "";
                myDailyData.no_production = isProduction;

                mUtility.startLoader(UpdateDailyActivity.this, R.drawable.image_for_rotation);
                mSupervisorManager.addNewDaily(myDailyData);

            } else {
                myDailyData.no_production = isProduction;
                myDailyData.job_id = mJobID;
                if (!mJobMapEditText.getText().toString().isEmpty()) {
                    myDailyData.job_map = mJobMapEditText.getText().toString();
                }
                if (!mJobLocationTicketEditText.getText().toString().isEmpty()) {
                    myDailyData.job_location = mJobLocationTicketEditText.getText().toString();
                }

                if (mMyDailyDataList.getLatitude() != null && mMyDailyDataList.getLongitude() != null) {
                    myDailyData.latitude = mMyDailyDataList.getLatitude();
                    myDailyData.longitude = mMyDailyDataList.getLongitude();
                }
                if (mWorkDateTextView.getText().toString().isEmpty()) {
                    mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.work_date_validation), false);
                } /*else if (mNotesEditText.getText().toString().isEmpty()) {
                    mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.job_description_validation), false);
                }*/ else {
                    if (!mWorkDateTextView.getText().toString().isEmpty()) {
                        myDailyData.from_date = mUtility.formatDate(mWorkDateTextView.getText().toString(), Constants.InputTag.NEW_DAILY_DATE_FORMAT, Constants.InputTag.DAILY_POST_DATE_FORMAT);
                    }
                    if (!mToWorkDateTextView.getText().toString().isEmpty() && !mToWorkDateTextView.getText().toString().equals(UpdateDailyActivity.this.getResources().getString(R.string.work_date))) {
                        myDailyData.to_date = mUtility.formatDate(mToWorkDateTextView.getText().toString(), Constants.InputTag.NEW_DAILY_DATE_FORMAT, Constants.InputTag.DAILY_POST_DATE_FORMAT);
                    }
                    myDailyData.notes = mNotesEditText.getText().toString();

                    if (!mCrewTextView.getText().toString().isEmpty()) {
                        myDailyData.crews_id = mCrewID;
                    }

                    ProductDataList productDataList = new ProductDataList();
                    ArrayList<ProductDataList> productDataListArrayList = new ArrayList<ProductDataList>();
                    if (!mProductionTextView2.getText().toString().equals(getResources().getString(R.string.items))) {

                        // Log.e("quantity", mProductQuantityEditText.getText().toString() + mProductQuantityEditText1.getText().toString() + mProductQuantityEditText2.getText().toString());

                        productDataList.setId(mProductID1 + "");
                        productDataList.setName(mProductionTextView.getText().toString() + "");
                        //productDataList.setDescription("description");
                        productDataList.setQuantity(mProductQuantityEditText.getText().toString());
                        productDataListArrayList.add(productDataList);

                        ProductDataList productDataList1 = new ProductDataList();
                        productDataList1.setId(mProductID2 + "");
                        productDataList1.setName(mProductionTextView1.getText().toString() + "");
                        //productDataList1.setDescription("description");
                        productDataList1.setQuantity(mProductQuantityEditText1.getText().toString());
                        productDataListArrayList.add(productDataList1);

                        ProductDataList productDataList2 = new ProductDataList();
                        productDataList2.setId(mProductID3 + "");
                        productDataList2.setName(mProductionTextView2.getText().toString() + "");
                        //productDataList2.setDescription("description");
                        productDataList2.setQuantity(mProductQuantityEditText2.getText().toString());
                        productDataListArrayList.add(productDataList2);

                    } else if (!mProductionTextView1.getText().toString().equals(getResources().getString(R.string.items))) {
                        productDataList.setId(mProductID1 + "");
                        productDataList.setName(mProductionTextView.getText().toString());
                        //productDataList.setDescription("description");
                        productDataList.setQuantity(mProductQuantityEditText.getText().toString());
                        productDataListArrayList.add(productDataList);

                        ProductDataList productDataList1 = new ProductDataList();
                        productDataList1.setId(mProductID2 + "");
                        productDataList1.setName(mProductionTextView1.getText().toString());
                        //productDataList1.setDescription("description");
                        productDataList1.setQuantity(mProductQuantityEditText1.getText().toString());
                        productDataListArrayList.add(productDataList1);

                    } else if (!mProductionTextView.getText().toString().isEmpty() && !mProductionTextView.getText().toString().equals(getResources().getString(R.string.items))) {
                        productDataList.setId(mProductID1 + "");
                        productDataList.setName(mProductionTextView.getText().toString() + "");
                        // productDataList.setDescription("description");
                        productDataList.setQuantity(mProductQuantityEditText.getText().toString());
                        productDataListArrayList.add(0, productDataList);
                    }

                    myDailyData.setProductDataList(productDataListArrayList);
                    if (mMyDailyDataList != null) {
                        myDailyData.id = mMyDailyDataList.getId();
                    }


                    if (mSupervisorDetails != null) {
                        if (/*mSupervisorDetails != null && */mSupervisorID != null) {
                            myDailyData.supervisor_id = mSupervisorID;
                        } else {
                            mUtility.showSnackBarAlert(findViewById(R.id.container), UpdateDailyActivity.this.getResources().getString(R.string.please_select_supervisor), false);
                            return;
                        }
                    }

                    //removed for check for supervisor-kapil
//                    if (/*mSupervisorDetails != null && */mSupervisorID != null) {
//                        myDailyData.supervisor_id = mSupervisorID;
//                    }else{
//                        mUtility.showSnackBarAlert(findViewById(R.id.container), UpdateDailyActivity.this.getResources().getString(R.string.please_select_supervisor), false);
//                        return;
//                    }

                    mUtility.startLoader(UpdateDailyActivity.this, R.drawable.image_for_rotation);
                    mSupervisorManager.addNewDaily(myDailyData);
                }
            }
        } else {
            mUtility.showSnackBarAlert(findViewById(R.id.container), UpdateDailyActivity.this.getResources().getString(R.string.select_job), false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_image_iv:
                if (mReadPermissionCheck != PackageManager.PERMISSION_GRANTED && mWritePermissionCheck != PackageManager.PERMISSION_GRANTED && mCameraPermissonCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UpdateDailyActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, Constants.RequestCodes.REQ_CODE_READ_WRITE_FILE);
                } else {
                    if (mFileArrayList.size() < 5) {
                        selectImageDialog();
                    } else {
                        mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.max_image_selection_validation), false);
                    }
                }
                break;

            case R.id.job_selection_tv:
                getForemanJobSelectionList();
                break;
            case R.id.work_date_tv:
                mUtility.hiddenInputMethod(UpdateDailyActivity.this);
                fromDatePickerDialog(mWorkDateTextView);
                break;
            case R.id.to_work_date_tv:
                mUtility.hiddenInputMethod(UpdateDailyActivity.this);
                toDatePickerDialog(mToWorkDateTextView);
                break;
            case R.id.production_item_tv:
                mViewID = 1;
                getProductionList();
                break;
            case R.id.production_item_tv1:
                mViewID = 2;
                getProductionList();
                break;
            case R.id.production_item_tv2:
                mViewID = 3;
                getProductionList();
                break;
            case R.id.sendButton:
                addNewDaily();
                break;
            case R.id.crew_tv:
                getCrewsList();
                break;
            case R.id.supervisor_tv:
                getSupervisorList();
                break;
            case R.id.imageView1:
               /* if (mReadPermissionCheck != PackageManager.PERMISSION_GRANTED && mWritePermissionCheck != PackageManager.PERMISSION_GRANTED && mCameraPermissonCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UpdateDailyActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, Constants.RequestCodes.REQ_CODE_READ_WRITE_FILE);
                } else {
                    // mImagePosition = 0;
                    // selectImageDialog();
                }*/
                startImagePagerActivity(0);
                break;
            case R.id.imageView2:
               /* if (mReadPermissionCheck != PackageManager.PERMISSION_GRANTED && mWritePermissionCheck != PackageManager.PERMISSION_GRANTED && mCameraPermissonCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UpdateDailyActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, Constants.RequestCodes.REQ_CODE_READ_WRITE_FILE);
                } else {
                    //  mImagePosition = 1;
                    // selectImageDialog();
                }*/

                startImagePagerActivity(1);
                break;
            case R.id.imageView3:
              /*  if (mReadPermissionCheck != PackageManager.PERMISSION_GRANTED && mWritePermissionCheck != PackageManager.PERMISSION_GRANTED && mCameraPermissonCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UpdateDailyActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, Constants.RequestCodes.REQ_CODE_READ_WRITE_FILE);
                } else {
                    // mImagePosition = 2;
                    // selectImageDialog();
                }*/
                startImagePagerActivity(2);
                break;
            case R.id.imageView4:
               /* if (mReadPermissionCheck != PackageManager.PERMISSION_GRANTED && mWritePermissionCheck != PackageManager.PERMISSION_GRANTED && mCameraPermissonCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UpdateDailyActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, Constants.RequestCodes.REQ_CODE_READ_WRITE_FILE);
                } else {
                    // mImagePosition = 3;
                    //  selectImageDialog();
                }*/
                startImagePagerActivity(3);
                break;
            case R.id.imageView5:
               /* if (mReadPermissionCheck != PackageManager.PERMISSION_GRANTED && mWritePermissionCheck != PackageManager.PERMISSION_GRANTED && mCameraPermissonCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UpdateDailyActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, Constants.RequestCodes.REQ_CODE_READ_WRITE_FILE);
                } else {
                    // mImagePosition = 4;
                    //  selectImageDialog();
                }*/
                startImagePagerActivity(4);
                break;
            case R.id.gps_tv:
                // Comment By Aniket on 17th Apr 2018
              //  if (mMyDailyDataList.status.equals(Constants.RequestCodes.REQ_CODE_DAILY_REJECTED)) {
                    Utility.showAlertDialog(UpdateDailyActivity.this, 0, R.string.gps_recapture, R.string.yes, R.string.no, new DialogActionCallback() {
                        @Override
                        public void doOnPositive() {
                            //reset screen
                            isLocation = true;
                            settingsrequest();
                        }

                        @Override
                        public void doOnNegative() {

                        }
                    });
//                } else {
//                    settingsrequest();
//                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.RequestCodes.REQ_CODE_READ_WRITE_FILE:
                selectImageDialog();
                break;
            case Constants.RequestCodes.REQ_CODE_ACCESS_FINE_LOCATION:
                // settingsrequest();
                break;
            default:
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.RequestCodes.REQ_CODE_GALLERY:
                    onSelectFromGalleryResult(data);
                    break;
                case Constants.RequestCodes.REQ_CODE_CAMERA:
                    onCaptureImageResult(data);
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK) {
                        Uri resultUri = result.getUri();
                        Bitmap bm = null;
                        if (resultUri != null) {
                            try {
                                bm = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);

                                // if (mImagePosition == -1) {
                                mAddedImageArrayList.add(bm);
                                mFileArrayList.add(new File(resultUri.getPath()));
                                mIsTextChange = true;
                               /* } else {
                                    mAddedImageArrayList.add(mImagePosition, bm);
                                    mFileArrayList.add(mImagePosition, new File(resultUri.getPath()));
                                }*/
                                setImageList(mAddedImageArrayList);

                            } catch (IOException e) {
                                //log.log(Level.SEVERE, e.getMessage(), e);
                            }


                        }
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error = result.getError();
                    }
                    break;
            }
        }
    }

    public void setImageList(ArrayList<Bitmap> addedImageArrayList) {
        if (addedImageArrayList != null) {
            if (addedImageArrayList.size() == 1) {
                imageView1.setImageBitmap(addedImageArrayList.get(0));
                imageView1.setPadding(1, 1, 1, 1);
            }

            if (addedImageArrayList.size() == 2) {
                imageView2.setImageBitmap(addedImageArrayList.get(1));
                imageView2.setPadding(1, 1, 1, 1);
            }

            if (addedImageArrayList.size() == 3) {
                imageView3.setImageBitmap(addedImageArrayList.get(2));
                imageView3.setPadding(1, 1, 1, 1);
            }

            if (addedImageArrayList.size() == 4) {
                imageView4.setImageBitmap(addedImageArrayList.get(3));
                imageView4.setPadding(1, 1, 1, 1);
            }

            if (addedImageArrayList.size() == 5) {
                imageView5.setImageBitmap(addedImageArrayList.get(4));
                imageView5.setPadding(1, 1, 1, 1);
            }
        }
    }

    public void selectImageDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(UpdateDailyActivity.this)
                .setTitle(R.string.select_image)
                .setPositiveButton(R.string.from_gallery, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        galleryIntent();
                    }
                })
                .setNegativeButton(R.string.from_camera, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        cameraIntent();
                    }
                })
                .show();
    }

    public void performCrop(Uri picUri) {

        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, Constants.RequestCodes.REQ_PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            Toast toast = Toast
                    .makeText(UpdateDailyActivity.this, UpdateDailyActivity.this.getResources().getString(R.string.crop_not_supported), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void savePicture(Bitmap b, String filePath) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.PNG, 100, bytes);

            File newFile = new File(filePath);
            newFile.createNewFile();
            FileOutputStream fo = new FileOutputStream(newFile);
            fo.write(bytes.toByteArray());
            // remember close de FileOutput
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = UpdateDailyActivity.this.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }


    private void jobSelectionDialog() {

        // retrieve display dimensions
        Rect displayRectangle = new Rect();
        Window window = UpdateDailyActivity.this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        // inflate and adjust layout
        LayoutInflater inflater = (LayoutInflater) UpdateDailyActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_job_selection, null);
        layout.setMinimumWidth((int) (displayRectangle.width() * 0.8f));
        layout.setMinimumHeight((int) (displayRectangle.height() * 0.8f));
        mDialog = new Dialog(UpdateDailyActivity.this, R.style.alertDialogCustom);
        mDialog.setContentView(layout);

        //  mDialog.setContentView(R.layout.dialog_foreman_selection);
        ListView jobSelectionListView = (ListView) mDialog.findViewById(R.id.job_selection_lv);

        // mDialog.setTitle(getResources().getString(R.string.foreman_list));
        mDialog.show();


        mJobSelectionListAdapter = new JobSelectionListAdapter(UpdateDailyActivity.this, R.layout.job_selection_list_item, mForemanJobDataList);
        jobSelectionListView.setAdapter(mJobSelectionListAdapter);

        jobSelectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mForemanJobData = mForemanJobDataList.get(position);
                mJobSelectionTextView.setText(mForemanJobDataList.get(position).getJob_detail().getClient());
                mDailyJobIDTextView.setText(mForemanJobDataList.get(position).getJob_detail().getJob_id());
                mJobID = mForemanJobDataList.get(position).getJob_detail().getId();
                mDialog.dismiss();
            }
        });
    }

    public void getForemanJobSelectionList() {
        ForemanJobData foremanJobData = new ForemanJobData();
        mUtility.startLoader(UpdateDailyActivity.this, R.drawable.image_for_rotation);
        foremanJobData.page = "1";
        foremanJobData.count = "20";
        foremanJobData.job_status = Constants.RequestCodes.REQ_CODE_PROGRESS;
        foremanJobData.users_id = PreferenceConfiguration.getUserID(UpdateDailyActivity.this);
        mSupervisorManager.getForemanJobList(foremanJobData);
    }


    //To pick date
    public void toDatePickerDialog(final TextView dateTextView) {
        final Calendar mCalendar = Calendar.getInstance();
        mDateFormatter = new SimpleDateFormat(Constants.InputTag.NEW_DAILY_DATE_FORMAT, Locale.US);
        DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateDailyActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mCalendar.set(year, monthOfYear, dayOfMonth);
                mYear = year;
                mMonth = monthOfYear + 1; // moth starts with 0, there for need to
                // add 1;
                mDay = dayOfMonth;

                String dateSetter = (new StringBuilder().append(mYear).append("-")
                        .append(mMonth).append("-").append(mDay).append(""))
                        .toString();
                if (dateSetter != null) {
                    try {
                        mCalendar.setTime(mDateFormatter.parse(dateSetter));
                    } catch (java.text.ParseException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    SimpleDateFormat month_date = new SimpleDateFormat("MMM");
                    month = month_date.format(mCalendar.getTime());

                }
                mToDate = mCalendar.getTime();
                if (mToDate != null && mFromDate != null) {
                    if (mFromDate.equals(mToDate) || mFromDate.before(mToDate)) {
                        dateTextView.setText(mDateFormatter.format(mCalendar.getTime()));
                    } else {
                        mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.date_validation), false);
                    }
                }
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }

    //from date
    public void fromDatePickerDialog(final TextView dateTextView) {
        final Calendar mCalendar = Calendar.getInstance();
        mDateFormatter = new SimpleDateFormat(Constants.InputTag.NEW_DAILY_DATE_FORMAT, Locale.US);
        DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateDailyActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mCalendar.set(year, monthOfYear, dayOfMonth);
                mYear = year;
                mMonth = monthOfYear + 1; // moth starts with 0, there for need to
                // add 1;
                mDay = dayOfMonth;

                String dateSetter = (new StringBuilder().append(mYear).append("-")
                        .append(mMonth).append("-").append(mDay).append(""))
                        .toString();
                if (dateSetter != null) {
                    try {
                        mCalendar.setTime(mDateFormatter.parse(dateSetter));
                    } catch (java.text.ParseException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    SimpleDateFormat month_date = new SimpleDateFormat("MMM");
                    month = month_date.format(mCalendar.getTime());

                }
                mFromDate = mCalendar.getTime();
                dateTextView.setText(mDateFormatter.format(mCalendar.getTime()));
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }


    private void getProductionList() {
        mUtility.startLoader(UpdateDailyActivity.this, R.drawable.image_for_rotation);
        mSupervisorManager.getProductionList(PreferenceConfiguration.getParentID(UpdateDailyActivity.this));
    }


    private void productionItemDialog() {

        // retrieve display dimensions
        Rect displayRectangle = new Rect();
        Window window = UpdateDailyActivity.this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        // inflate and adjust layout
        LayoutInflater inflater = (LayoutInflater) UpdateDailyActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_product_selection, null);
        layout.setMinimumWidth((int) (displayRectangle.width() * 0.8f));
        layout.setMinimumHeight((int) (displayRectangle.height() * 0.8f));
        mDialog = new Dialog(UpdateDailyActivity.this, R.style.alertDialogCustom);
        mDialog.setContentView(layout);

        ListView productSelectionListView = (ListView) mDialog.findViewById(R.id.product_selection_lv);

        // mDialog.setTitle(getResources().getString(R.string.product_list));
        mDialog.show();


        mProductionListAdapter = new ProductionListAdapter(UpdateDailyActivity.this, R.layout.product_selection_list_item, mProductDataList);
        productSelectionListView.setAdapter(mProductionListAdapter);
        productSelectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mViewID == 1) {
                    mProductionTextView.setText(mProductDataList.get(position).getName());
                    mProductID1 = mProductDataList.get(position).getId() + "";
                    mProductionTextView.setTextColor(ContextCompat.getColor(UpdateDailyActivity.this, R.color.black));
                    mProductionLinearLayout1.setVisibility(View.VISIBLE);
                } else if (mViewID == 2) {
                    mProductionTextView1.setText(mProductDataList.get(position).getName());
                    mProductID2 = mProductDataList.get(position).getId() + "";
                    mProductionTextView1.setTextColor(ContextCompat.getColor(UpdateDailyActivity.this, R.color.black));
                    mProductionLinearLayout2.setVisibility(View.VISIBLE);

                } else if (mViewID == 3) {
                    mProductionTextView2.setText(mProductDataList.get(position).getName());
                    mProductID3 = mProductDataList.get(position).getId() + "";
                    mProductionTextView2.setTextColor(ContextCompat.getColor(UpdateDailyActivity.this, R.color.black));
                }

                mDialog.dismiss();
            }
        });
    }

    public void uploadDailiesImage(File file, String id) {
        try {
            if (file != null) {
                // create RequestBody instance from file
                RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                // MultipartBody.Part is used to send also the actual file name
                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file", file.getName(), fbody);

                RequestBody requestid = RequestBody.create(MediaType.parse("multipart/form-data"), id);

                // RequestBody request_id = RequestBody.create(MediaType.parse("multipart/form-data"), _id);

                mSupervisorManager.uploadDailiesImage(body, requestid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCrewsList() {
        CrewsData crewsData = new CrewsData();
        mUtility.startLoader(UpdateDailyActivity.this, R.drawable.image_for_rotation);
        crewsData.page = "1";
        crewsData.count = "50";
        crewsData.parent_id = PreferenceConfiguration.getUserID(UpdateDailyActivity.this) + "";
        mSupervisorManager.getCrewsList(crewsData);
    }

    private void getSupervisorList() {
        if (mJobID != null) {
            SupervisorData supervisorData = new SupervisorData();
            mUtility.startLoader(UpdateDailyActivity.this, R.drawable.image_for_rotation);
            supervisorData.jobId = mJobID;
            mSupervisorManager.getSupervisorList(supervisorData);
        } else {
            mUtility.showSnackBarAlert(findViewById(R.id.container), UpdateDailyActivity.this.getResources().getString(R.string.select_job), false);
        }
    }

    private void crewListDialog() {

        // retrieve display dimensions
        Rect displayRectangle = new Rect();
        Window window = UpdateDailyActivity.this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        // inflate and adjust layout
        LayoutInflater inflater = (LayoutInflater) UpdateDailyActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_crew_selection, null);
        layout.setMinimumWidth((int) (displayRectangle.width() * 0.8f));
        layout.setMinimumHeight((int) (displayRectangle.height() * 0.8f));
        mDialog = new Dialog(UpdateDailyActivity.this, R.style.alertDialogCustom);
        mDialog.setContentView(layout);

        ListView crewsListView = (ListView) mDialog.findViewById(R.id.crew_selection_lv);

        // mDialog.setTitle(getResources().getString(R.string.product_list));
        mDialog.show();
        mCrewsSelectionListAdapter = new CrewsSelectionListAdapter(UpdateDailyActivity.this, R.layout.foreman_list_item, mCrewsDataList);
        crewsListView.setAdapter(mCrewsSelectionListAdapter);
        crewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCrewTextView.setText(mCrewsDataList.get(position).getFirstname() + " " + mCrewsDataList.get(position).getLastname());
                mCrewID = mCrewsDataList.get(position).getId() + "";
                mProductionTextView.setTextColor(ContextCompat.getColor(UpdateDailyActivity.this, R.color.black));
                mDialog.dismiss();
            }
        });
    }


    private void supervisorListDialog() {

        // retrieve display dimensions
        Rect displayRectangle = new Rect();
        Window window = UpdateDailyActivity.this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        // inflate and adjust layout
        LayoutInflater inflater = (LayoutInflater) UpdateDailyActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_supervisor_selection, null);
        layout.setMinimumWidth((int) (displayRectangle.width() * 0.8f));
        layout.setMinimumHeight((int) (displayRectangle.height() * 0.8f));
        mDialog = new Dialog(UpdateDailyActivity.this, R.style.alertDialogCustom);
        mDialog.setContentView(layout);

        ListView supervisorListView = (ListView) mDialog.findViewById(R.id.supervisor_selection_lv);

        // mDialog.setTitle(getResources().getString(R.string.product_list));
        mDialog.show();
        mSupervisorSelectionListAdapter = new SupervisorSelectionListAdapter(UpdateDailyActivity.this, R.layout.foreman_list_item, mSupervisorDataList);
        supervisorListView.setAdapter(mSupervisorSelectionListAdapter);

        supervisorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSupervisorTextView.setText(mSupervisorDataList.get(position).getSupervisorJobAssignTo().getFirstname() + " " + mSupervisorDataList.get(position).getSupervisorJobAssignTo().getLastname());
                mSupervisorID = mSupervisorDataList.get(position).getSupervisorJobAssignTo().getId() + "";
                //mSupervisorTextView.setTextColor(ContextCompat.getColor(UpdateDailyActivity.this, R.color.black));

                mDialog.dismiss();
            }
        });
    }

    public void resetImageList() {
        imageView1.setImageDrawable(ContextCompat.getDrawable(UpdateDailyActivity.this, R.drawable.ic_insert_photo));
        imageView1.setPadding(20, 20, 20, 20);

        imageView2.setImageDrawable(ContextCompat.getDrawable(UpdateDailyActivity.this, R.drawable.ic_insert_photo));
        imageView2.setPadding(20, 20, 20, 20);

        imageView3.setImageDrawable(ContextCompat.getDrawable(UpdateDailyActivity.this, R.drawable.ic_insert_photo));
        imageView3.setPadding(20, 20, 20, 20);

        imageView4.setImageDrawable(ContextCompat.getDrawable(UpdateDailyActivity.this, R.drawable.ic_insert_photo));
        imageView4.setPadding(20, 20, 20, 20);

        imageView5.setImageDrawable(ContextCompat.getDrawable(UpdateDailyActivity.this, R.drawable.ic_insert_photo));
        imageView5.setPadding(20, 20, 20, 20);
    }

    public void setDailyData(MyDailyDataList myDailyDataList) {
        if (myDailyDataList != null) {
            if (myDailyDataList.getReject_notes() != null) {
                mRejectionDetailsTextView.setVisibility(View.VISIBLE);
                mRejectionDetailsTextView.setText(myDailyDataList.getReject_notes());
            }
            mNotesEditText.setText(myDailyDataList.getNotes());
            isProduction = myDailyDataList.isNo_production();
            if (isProduction) {
                mProductionCheckbox.setChecked(isProduction);
                mDailyLinearLayout.setVisibility(View.GONE);
                mDailyOtherLinearLayout.setVisibility(View.GONE);
            } else {
                mDailyLinearLayout.setVisibility(View.VISIBLE);
                mDailyOtherLinearLayout.setVisibility(View.VISIBLE);
            }
            if (mMyDailyDetails != null) {
                mJobSelectionTextView.setText(mMyDailyDetails.getClient());
                mDailyJobIDTextView.setText(mMyDailyDetails.getJob_id());
                mJobID = mMyDailyDetails.getId();
            }
            if (myDailyDataList.getLatitude() != null) {
                mGPSTextView.setText(myDailyDataList.getLatitude() + " " + myDailyDataList.getLongitude());
                mGPSTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.gps_active, 0);
            } else {
                mGPSTextView.setOnClickListener(this);
            }

            if (mSupervisorDetails != null) {
                mSupervisorTextView.setText(mSupervisorDetails.getFirstname() + " " + mSupervisorDetails.getLastname());
                mSupervisorID = mSupervisorDetails.getId();
            }

            if (mCrewsDetails != null) {
                mCrewTextView.setText(mCrewsDetails.getFirstname() + " " + mCrewsDetails.getLastname());
                mCrewID = mCrewsDetails.getId();
            }

            mJobMapEditText.setText(myDailyDataList.getJob_map());
            mJobLocationTicketEditText.setText(myDailyDataList.getJob_location());

            mWorkDateTextView.setText(mUtility.formatDate(myDailyDataList.getFrom_date(), Constants.InputTag.GET_DATE_FORMAT, Constants.InputTag.NEW_DAILY_DATE_FORMAT));
            mToWorkDateTextView.setText(mUtility.formatDate(myDailyDataList.getTo_date(), Constants.InputTag.GET_DATE_FORMAT, Constants.InputTag.NEW_DAILY_DATE_FORMAT));


            if (mBillableItems != null) {

                if (mBillableItems.size() == 1) {
                    if (mBillableItems.get(0).getName().isEmpty()) {
                        mProductionTextView.setText(getResources().getString(R.string.production_items));
                        mProductQuantityEditText.setText(getResources().getString(R.string.quantity));
                    } else {
                        mProductionTextView.setText(mBillableItems.get(0).getName());
                        mProductQuantityEditText.setText(mBillableItems.get(0).getQuantity());
                        mProductionLinearLayout1.setVisibility(View.VISIBLE);
                        mProductID1 = mBillableItems.get(0).getId();
                    }
                }
                if (mBillableItems.size() == 2) {

                    if (mBillableItems.get(0).getName().isEmpty()) {
                        mProductionTextView.setText(getResources().getString(R.string.production_items));
                        mProductQuantityEditText.setText(getResources().getString(R.string.quantity));
                    } else {
                        mProductionTextView.setText(mBillableItems.get(0).getName());
                        mProductQuantityEditText.setText(mBillableItems.get(0).getQuantity());
                        mProductID1 = mBillableItems.get(0).getId();
                    }

                    if (mBillableItems.get(1).getName().isEmpty()) {
                        mProductionTextView1.setText(getResources().getString(R.string.production_items));
                        mProductQuantityEditText1.setText(getResources().getString(R.string.quantity));
                    } else {
                        mProductionTextView1.setText(mBillableItems.get(1).getName());
                        mProductQuantityEditText1.setText(mBillableItems.get(1).getQuantity());
                        mProductID2 = mBillableItems.get(1).getId();
                    }
                    mProductionLinearLayout2.setVisibility(View.VISIBLE);
                }
                if (mBillableItems.size() > 2) {
                    if (mBillableItems.get(0).getName().isEmpty()) {
                        mProductionTextView.setText(getResources().getString(R.string.production_items));
                        mProductQuantityEditText.setText(getResources().getString(R.string.quantity));
                    } else {
                        mProductionTextView.setText(mBillableItems.get(0).getName());
                        mProductQuantityEditText.setText(mBillableItems.get(0).getQuantity());
                        mProductID1 = mBillableItems.get(0).getId();
                    }

                    if (mBillableItems.get(1).getName().isEmpty()) {
                        mProductionTextView1.setText(getResources().getString(R.string.production_items));
                        mProductQuantityEditText1.setText(getResources().getString(R.string.quantity));
                    } else {
                        mProductionTextView1.setText(mBillableItems.get(1).getName());
                        mProductQuantityEditText1.setText(mBillableItems.get(1).getQuantity());
                        mProductID2 = mBillableItems.get(1).getId();
                    }

                    if (mBillableItems.get(2).getName().isEmpty()) {
                        mProductionTextView2.setText(getResources().getString(R.string.production_items));
                        mProductQuantityEditText2.setText(getResources().getString(R.string.quantity));
                    } else {
                        mProductionTextView2.setText(mBillableItems.get(2).getName());
                        mProductQuantityEditText2.setText(mBillableItems.get(2).getQuantity());
                        mProductID3 = mBillableItems.get(2).getId();
                    }
                    mProductionLinearLayout1.setVisibility(View.VISIBLE);
                    mProductionLinearLayout2.setVisibility(View.VISIBLE);
                }

            }
            mIsTextChange = false;
        }
    }

    public void setIncidentImages(ArrayList<DailiesImage> dailiesImages) {
        if (dailiesImages != null) {
            if (dailiesImages.size() == 1) {
                imageLoader.displayImage(Constants.WebServices.BASE_URL
                        + dailiesImages.get(0).getImage_path(), imageView1, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mProgressBar1.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mProgressBar1.setVisibility(View.GONE);
                        imageView1.setImageResource(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mProgressBar1.setVisibility(View.GONE);
                        imageView1.setPadding(1, 1, 1, 1);
                    }
                });
            }

            if (dailiesImages.size() == 2) {
                imageLoader.displayImage(Constants.WebServices.BASE_URL
                        + dailiesImages.get(0).getImage_path(), imageView1, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mProgressBar1.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mProgressBar1.setVisibility(View.GONE);
                        imageView1.setImageResource(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mProgressBar1.setVisibility(View.GONE);
                        imageView1.setPadding(1, 1, 1, 1);
                    }
                });

                imageLoader.displayImage(Constants.WebServices.BASE_URL
                        + dailiesImages.get(1).getImage_path(), imageView2, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mProgressBar2.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mProgressBar2.setVisibility(View.GONE);
                        imageView2.setImageResource(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mProgressBar2.setVisibility(View.GONE);
                        imageView2.setPadding(1, 1, 1, 1);
                    }
                });
            }

            if (dailiesImages.size() == 3) {
                imageLoader.displayImage(Constants.WebServices.BASE_URL
                        + dailiesImages.get(0).getImage_path(), imageView1, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mProgressBar1.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mProgressBar1.setVisibility(View.GONE);
                        imageView1.setImageResource(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mProgressBar1.setVisibility(View.GONE);
                        imageView1.setPadding(1, 1, 1, 1);
                    }
                });

                imageLoader.displayImage(Constants.WebServices.BASE_URL
                        + dailiesImages.get(1).getImage_path(), imageView2, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mProgressBar2.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mProgressBar2.setVisibility(View.GONE);
                        imageView2.setImageResource(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mProgressBar2.setVisibility(View.GONE);
                        imageView2.setPadding(1, 1, 1, 1);
                    }
                });

                imageLoader.displayImage(Constants.WebServices.BASE_URL
                        + dailiesImages.get(2).getImage_path(), imageView3, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mProgressBar3.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mProgressBar3.setVisibility(View.GONE);
                        imageView3.setImageResource(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mProgressBar3.setVisibility(View.GONE);
                        imageView3.setPadding(1, 1, 1, 1);
                    }
                });
            }

            if (dailiesImages.size() == 4) {
                imageLoader.displayImage(Constants.WebServices.BASE_URL
                        + dailiesImages.get(0).getImage_path(), imageView1, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mProgressBar1.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mProgressBar1.setVisibility(View.GONE);
                        imageView1.setImageResource(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mProgressBar1.setVisibility(View.GONE);
                        imageView1.setPadding(1, 1, 1, 1);
                    }
                });

                imageLoader.displayImage(Constants.WebServices.BASE_URL
                        + dailiesImages.get(1).getImage_path(), imageView2, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mProgressBar2.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mProgressBar2.setVisibility(View.GONE);
                        imageView2.setImageResource(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mProgressBar2.setVisibility(View.GONE);
                        imageView2.setPadding(1, 1, 1, 1);
                    }
                });

                imageLoader.displayImage(Constants.WebServices.BASE_URL
                        + dailiesImages.get(2).getImage_path(), imageView3, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mProgressBar3.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mProgressBar3.setVisibility(View.GONE);
                        imageView3.setImageResource(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mProgressBar3.setVisibility(View.GONE);
                        imageView3.setPadding(1, 1, 1, 1);
                    }
                });

                imageLoader.displayImage(Constants.WebServices.BASE_URL
                        + dailiesImages.get(3).getImage_path(), imageView4, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mProgressBar4.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mProgressBar4.setVisibility(View.GONE);
                        imageView4.setImageResource(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mProgressBar4.setVisibility(View.GONE);
                        imageView4.setPadding(1, 1, 1, 1);
                    }
                });
            }
            if (dailiesImages.size() == 5) {
                imageLoader.displayImage(Constants.WebServices.BASE_URL
                        + dailiesImages.get(0).getImage_path(), imageView1, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mProgressBar1.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mProgressBar1.setVisibility(View.GONE);
                        imageView1.setImageResource(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mProgressBar1.setVisibility(View.GONE);
                        imageView1.setPadding(1, 1, 1, 1);
                    }
                });

                imageLoader.displayImage(Constants.WebServices.BASE_URL
                        + dailiesImages.get(1).getImage_path(), imageView2, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mProgressBar2.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mProgressBar2.setVisibility(View.GONE);
                        imageView2.setImageResource(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mProgressBar2.setVisibility(View.GONE);
                        imageView2.setPadding(1, 1, 1, 1);
                    }
                });

                imageLoader.displayImage(Constants.WebServices.BASE_URL
                        + dailiesImages.get(2).getImage_path(), imageView3, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mProgressBar3.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mProgressBar3.setVisibility(View.GONE);
                        imageView3.setImageResource(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mProgressBar3.setVisibility(View.GONE);
                        imageView3.setPadding(1, 1, 1, 1);
                    }
                });

                imageLoader.displayImage(Constants.WebServices.BASE_URL
                        + dailiesImages.get(3).getImage_path(), imageView4, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mProgressBar4.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mProgressBar4.setVisibility(View.GONE);
                        imageView4.setImageResource(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mProgressBar4.setVisibility(View.GONE);
                        imageView4.setPadding(1, 1, 1, 1);
                    }
                });

                imageLoader.displayImage(Constants.WebServices.BASE_URL
                        + dailiesImages.get(4).getImage_path(), imageView5, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mProgressBar5.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mProgressBar5.setVisibility(View.GONE);
                        imageView5.setImageResource(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mProgressBar5.setVisibility(View.GONE);
                        imageView5.setPadding(1, 1, 1, 1);
                    }
                });
            }
        }
    }

  /*private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(intent, Constants.RequestCodes.REQ_CODE_CAMERA);
    }*/

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imageFile = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
        //Uri photoURI = Uri.fromFile(createImageFile());
        mImageUri = FileProvider.getUriForFile(UpdateDailyActivity.this, getApplicationContext().getPackageName() + ".provider", imageFile);

        //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(intent, Constants.RequestCodes.REQ_CODE_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, getString(R.string.selectfile)),
                Constants.RequestCodes.REQ_CODE_GALLERY);
    }


    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(), data
                        .getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        CropImage.activity(data.getData())
                .start(UpdateDailyActivity.this);

    }

    private void onCaptureImageResult(Intent data) {

        File f = new File(Environment.getExternalStorageDirectory().toString());

        for (File temp : f.listFiles()) {
            if (temp.getName().equals("temp.jpg")) {
                f = temp;
                break;
            }
        }

        Bitmap bitmap;
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = 2;
        bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                bitmapOptions);

        CropImage.activity(mImageUri)
                .start(UpdateDailyActivity.this);
    }

    public void settingsrequest() {
        mGoogleApiClient = new GoogleApiClient.Builder(UpdateDailyActivity.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1);
        mLocationRequest.setFastestInterval(1);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient
        // builder.setNeedBle(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(this);
    }

    protected void startLocationUpdates() {
        if (mGoogleApiClient != null) {

            if (ActivityCompat.checkSelfPermission(UpdateDailyActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(UpdateDailyActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
              /*  ActivityCompat.requestPermissions(
                        getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, Constants.RequestCodes.REQ_CODE_ACCESS_FINE_LOCATION);*/
            } else {
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                mLocationRequest.setInterval(1);
                mLocationRequest.setFastestInterval(1);

                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);
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
    public void onResult(@NonNull Result result) {

        final Status status = result.getStatus();
        // final LocationSettingsStates state = result.getLocationSettingsStates();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // All location settings are satisfied. The client can initialize location
                // requests here.
                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                // Location settings are not satisfied. But could be fixed by showing the user
                // a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    status.startResolutionForResult(UpdateDailyActivity.this, REQUEST_CHECK_SETTINGS);
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

    @Override
    public void onLocationChanged(Location location) {
        try {
            if (location != null && isLocation) {
                isLocation = false;
                location.setAccuracy(1);
                mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                mGPSTextView.setText(mLatLng.latitude + " " + mLatLng.longitude);
                mGPSTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.gps_active, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void startImagePagerActivity(int position) {
        if (mDailiesImageArrayList != null) {
            if (mDailiesImageArrayList.size() > 0) {
                Intent intent = new Intent(UpdateDailyActivity.this, ViewDailyImageActivity.class);
                intent.putExtra(Constants.ButtonTags.FRAGMENT_INDEX, DailyImagePagerFragment.INDEX);
                intent.putExtra(Constants.ButtonTags.IMAGE_POSITION, position);
                startActivity(intent);
            } else {
                mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.no_image_found), false);
            }
        } else {
            mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.no_image_found), false);
        }

    }

    public void callAlertDialog() {
        mUtility.stopLoader();

        Utility.showAlertDialog(UpdateDailyActivity.this, 0, R.string.your_daily_has_been_submitted, R.string.Ok, 0, new DialogActionCallback() {
            @Override
            public void doOnPositive() {
                finish();
            }

            @Override
            public void doOnNegative() {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mIsTextChange) {
            Utility.showAlertDialog(UpdateDailyActivity.this, 0, R.string.your_daily_discard_alert_message, R.string.yes, R.string.no, new DialogActionCallback() {
                @Override
                public void doOnPositive() {
                    finish();
                }

                @Override
                public void doOnNegative() {

                }
            });
        } else {
            finish();
        }
    }

    public void textChangeListener(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mIsTextChange = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void textViewTextChangeListener(TextView textView) {
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mIsTextChange = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
