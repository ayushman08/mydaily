package com.smartdata.activities;

import android.Manifest;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
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
import com.smartdata.adapters.JobSelectionListAdapter;
import com.smartdata.config.PreferenceConfiguration;
import com.smartdata.dataobject.AppInstance;
import com.smartdata.dataobject.ForemanJobData;
import com.smartdata.dataobject.ForemanJobDataList;
import com.smartdata.dataobject.IncidentData;
import com.smartdata.dataobject.IncidentDataList;
import com.smartdata.dataobject.IncidentImages;
import com.smartdata.dataobject.JobDetailsData;
import com.smartdata.dataobject.MyDailyResponse;
import com.smartdata.dataobject.ResponseData;
import com.smartdata.dialogs.AlertDialogManager;
import com.smartdata.fragments.ForemanCrewsFragment;
import com.smartdata.fragments.ImagePagerFragment;
import com.smartdata.interfaces.DialogActionCallback;
import com.smartdata.interfaces.ExifUtil;
import com.smartdata.interfaces.ServiceRedirection;
import com.smartdata.managers.SupervisorManager;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.CustomAutoCompleteTextview;
import com.smartdata.utils.PlaceDetailsJSONParser;
import com.smartdata.utils.PlaceJSONParser;
import com.smartdata.utils.RoundRectCornerImageView;
import com.smartdata.utils.Utility;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.smartdata.utils.Constants.WebServices.GOOGLE_PLACE_API_KEY;
import static com.smartdata.utils.Constants.WebServices.GOOGLE_PLACE_AUTOCOMPLETE_API;
import static com.smartdata.utils.Constants.WebServices.GOOGLE_PLACE_DETAILS_API;
import static com.smartdata.utils.Utility.downloadUrl;


/**
 * Created by Anurag Sethi
 * The activity is used for handling the login screen actions
 */
public class NewIncidentDetailsActivity extends AppActivity implements ServiceRedirection, View.OnClickListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener,ResultCallback {

    private ActionBar mActionBar;
    private Utility mUtility;
    private AlertDialogManager mAleaAlertDialogManager;
    private SupervisorManager mSupervisorManager;

    private RoundRectCornerImageView mRoundRectCornerImageView1, mRoundRectCornerImageView2, mRoundRectCornerImageView3,
            mRoundRectCornerImageView4, mRoundRectCornerImageView5;
    private ProgressBar mProgressBar1, mProgressBar2, mProgressBar3, mProgressBar4, mProgressBar5;

    private TextView mIncidentDateTextView, mIsOurFaultTextView, mDateTextView, mJobSelectionTextView,mGPSTextView;
    private EditText mWhatDamageEditText, mWhoOwnsEditText, mTicketNumberEditText, mMarkEditText, mNotesEditText;
    private CustomAutoCompleteTextview mAddressEditText;
    private ProgressBar mProgressBar;
    private ImageView mAddIncidentImageView;
    private Uri mImageUri;
    private String path, filePath;
    private ArrayList<Bitmap> mAddedImageArrayList = new ArrayList<>(5);
    private Dialog mDialog;
    private ArrayList<ForemanJobDataList> mForemanJobDataList;
    private ForemanJobDataList mForemanJobData;
    private JobSelectionListAdapter mJobSelectionListAdapter;
    private String mJobId;
    private AppCompatButton mSubmitButton;
    private ArrayList<File> mFileArrayList = new ArrayList<>(5);
    private MyDailyResponse mMyDailyResponse;
    // Member variable : hold current date info
    private int mCurrentYear;
    private int mCurrentMonth;
    private int mCurrentDay;
    private int mYear;
    private int mMonth;
    private int mDay;
    private String month;
    private Calendar mCurrentDateTime;
    int mDateOf;
    private int mDateOfBirth;
    private SimpleDateFormat mDateFormatter, mDateTimeFormatter;
    private boolean isContinue = true;
    private int mFileSize = 0;
    private ArrayList<File> mFileListSize = new ArrayList<>();
    private ResponseData responseData;
    private File mLastFile;

    private IncidentDataList mIncidentDataList;
    private ArrayList<IncidentImages> mIncidentImages;
    private JobDetailsData mJobDetailsData;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    final int PLACES = 0;
    final int PLACES_DETAILS = 1;
    private DownloadTask placesDownloadTask;
    private DownloadTask placeDetailsDownloadTask;
    private ParserTask placesParserTask;
    private ParserTask placeDetailsParserTask;
    private String mIncidentDate = null;
    private boolean mIsDraft = false, mIsTextChange = true;
    // private int mImagePosition = -1;

    // Added by Aniket on 17th Apr 2018

    private LatLng mLatLng;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private boolean isLocation = true;
    protected static final int REQUEST_CHECK_SETTINGS = 171;
    private int mReadPermissionCheck, mWritePermissionCheck, mCameraPermissonCheck, mFineLocation, mCoarseLocation;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_incident);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initData();
        bindControls();
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

        // Check Location Permission

        mFineLocation = ContextCompat.checkSelfPermission(NewIncidentDetailsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        mCoarseLocation = ContextCompat.checkSelfPermission(NewIncidentDetailsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if ((mFineLocation != PackageManager.PERMISSION_GRANTED) && (mCoarseLocation != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(NewIncidentDetailsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Constants.RequestCodes.REQ_CODE_ACCESS_FINE_LOCATION);

        }

        this.imageLoader = ImageLoader.getInstance();
        this.imageLoader.init(ImageLoaderConfiguration.createDefault(NewIncidentDetailsActivity.this));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_insert_photo)
                .showImageForEmptyUri(R.drawable.ic_insert_photo)
                .showImageOnFail(R.drawable.ic_insert_photo).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        mIncidentDataList = getIntent().getParcelableExtra(Constants.RequestCodes.INCIDENT_DETAILS);
        //<Dev comment="Added by kapil">
        if (mIncidentDataList != null) {
            mIncidentDataList.setIs_draft(getIntent().getBooleanExtra(Constants.RequestCodes.INCIDENT_IS_DRAFT, false));
        }
        //<Dev comment>
        mIncidentImages = getIntent().getParcelableArrayListExtra(Constants.RequestCodes.INCIDENT_IMAGES);
        mJobDetailsData = getIntent().getParcelableExtra(Constants.RequestCodes.INCIDENT_JOB_DETAILS);

        mWritePermissionCheck = ContextCompat.checkSelfPermission(NewIncidentDetailsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        mReadPermissionCheck = ContextCompat.checkSelfPermission(NewIncidentDetailsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        mCameraPermissonCheck = ContextCompat.checkSelfPermission(NewIncidentDetailsActivity.this, Manifest.permission.CAMERA);

        mUtility = new Utility(NewIncidentDetailsActivity.this);
        mSupervisorManager = new SupervisorManager(NewIncidentDetailsActivity.this, this);
        mUtility.customActionBar(NewIncidentDetailsActivity.this, true, R.string.incident);
//        mUtility.setToolbar(IncidentDetailsActivity.this, getResources().getString(R.string.edit_crews), getResources().getString(R.string.submittals_search_hint), true, false);
        mAleaAlertDialogManager = new AlertDialogManager();

    }

    /**
     * Binds the UI controls
     *
     * @return none
     */
    @Override
    public void bindControls() {
        mSubmitButton = (AppCompatButton) findViewById(R.id.add_new_incident_Button);
        mSubmitButton.setOnClickListener(this);

        mRoundRectCornerImageView1 = (RoundRectCornerImageView) findViewById(R.id.incident_image_view1);
        mRoundRectCornerImageView2 = (RoundRectCornerImageView) findViewById(R.id.incident_image_view2);
        mRoundRectCornerImageView3 = (RoundRectCornerImageView) findViewById(R.id.incident_image_view3);
        mRoundRectCornerImageView4 = (RoundRectCornerImageView) findViewById(R.id.incident_image_view4);
        mRoundRectCornerImageView5 = (RoundRectCornerImageView) findViewById(R.id.incident_image_view5);

        mGPSTextView = (TextView) findViewById(R.id.gps_tv);
        mGPSTextView.setOnClickListener(this);

        if (mIncidentDataList != null) {
            MyApplication myApplication = ((MyApplication) getApplicationContext());
            myApplication.setIncidentImages(mIncidentImages);
            //<Dev comment="Added by kapil">
            if (mIncidentDataList.is_draft())
                mSubmitButton.setText(getResources().getString(R.string.submit));
            else
                mSubmitButton.setText(getResources().getString(R.string.update));
            //</Dev comment>
            mSubmitButton.setBackgroundDrawable(ContextCompat.getDrawable(NewIncidentDetailsActivity.this, R.drawable.button_active));

            mRoundRectCornerImageView1.setOnClickListener(this);
            mRoundRectCornerImageView2.setOnClickListener(this);
            mRoundRectCornerImageView3.setOnClickListener(this);
            mRoundRectCornerImageView4.setOnClickListener(this);
            mRoundRectCornerImageView5.setOnClickListener(this);
        }

        mIncidentDateTextView = (TextView) findViewById(R.id.incident_date_tv);
        //Log.e("date", new SimpleDateFormat(Constants.InputTag.GET_INCIDENT_DATE_FORMAT).format(Calendar.getInstance().getTime()) + "");
        mIncidentDateTextView.setText(new SimpleDateFormat(Constants.InputTag.GET_INCIDENT_DATE_FORMAT).format(Calendar.getInstance().getTime()) + "");

        mJobSelectionTextView = (TextView) findViewById(R.id.job_selection_tv);
        mJobSelectionTextView.setOnClickListener(this);
        textViewTextChangeListener(mJobSelectionTextView);

        mWhatDamageEditText = (EditText) findViewById(R.id.what_damage_et);
        mWhoOwnsEditText = (EditText) findViewById(R.id.who_owns_et);

        textChangeListener(mWhatDamageEditText);
        textChangeListener(mWhoOwnsEditText);

        mAddressEditText = (CustomAutoCompleteTextview) findViewById(R.id.address_et);
        mProgressBar = (ProgressBar) findViewById(R.id.search_bar);

        mAddressEditText.setThreshold(1);
        mAddressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                placesDownloadTask = new DownloadTask(PLACES);
                mProgressBar.setVisibility(View.VISIBLE);
                mIsTextChange = true;
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


        mAddressEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                mUtility.hiddenInputMethod(NewIncidentDetailsActivity.this);
            }
        });


        mTicketNumberEditText = (EditText) findViewById(R.id.ticket_number_et);
        textChangeListener(mTicketNumberEditText);
        mIsOurFaultTextView = (TextView) findViewById(R.id.our_fault_tv);
        mIsOurFaultTextView.setOnClickListener(this);
        textViewTextChangeListener(mIsOurFaultTextView);

        mMarkEditText = (EditText) findViewById(R.id.mark_et);
        textChangeListener(mMarkEditText);
        mDateTextView = (TextView) findViewById(R.id.date_tv);
        mDateTextView.setOnClickListener(this);
        textViewTextChangeListener(mDateTextView);

        mNotesEditText = (EditText) findViewById(R.id.notes_et);
        textChangeListener(mNotesEditText);

        mAddIncidentImageView = (ImageView) findViewById(R.id.add_incident_iv);
        mAddIncidentImageView.setOnClickListener(this);

        mProgressBar1 = (ProgressBar) findViewById(R.id.progress_bar1);
        mProgressBar2 = (ProgressBar) findViewById(R.id.progress_bar2);
        mProgressBar3 = (ProgressBar) findViewById(R.id.progress_bar3);
        mProgressBar4 = (ProgressBar) findViewById(R.id.progress_bar4);
        mProgressBar5 = (ProgressBar) findViewById(R.id.progress_bar5);

        setIncidentDetails(mIncidentDataList, mJobDetailsData);
        setIncidentImages(mIncidentImages);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_draft, menu);
        final MenuItem item = menu.findItem(R.id.action_draft);
        item.setActionView(R.layout.menu_item_save);
        TextView saveTextView = (TextView) item.getActionView();
        saveTextView.setPadding(0, 0, 50, 0);
        saveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAddNewIncident(true);
            }
        });

        return true;
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
                    Utility.showAlertDialog(NewIncidentDetailsActivity.this, 0, R.string.your_incident_discard_alert_message, R.string.yes, R.string.no, new DialogActionCallback() {
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
            case R.id.action_draft:
                callAddNewIncident(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

            if (taskID == Constants.TaskID.GET_FOREMAN_JOB_LIST) {
                mUtility.stopLoader();
                mForemanJobDataList = AppInstance.foremanJobData.foremanJobDataList;
                if (mForemanJobDataList.size() > 0) {
                    jobSelectionDialog();
                } else {
                    mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.no_jobs), false);
                }
            } else if (taskID == Constants.TaskID.ADD_NEW_INCIDENT) {
                mUtility.stopLoader();
                mMyDailyResponse = AppInstance.myDailyResponse;
                if (mMyDailyResponse.getCode() == 200) {
                    if (mFileArrayList != null && mFileArrayList.size() > 0) {
                        if (isContinue) {
                            mUtility.startLoader(NewIncidentDetailsActivity.this, R.drawable.image_for_rotation);

                            for (int i = 0; i < mFileArrayList.size(); i++) {
                                mFileListSize.add(mFileArrayList.get(i));
                                isContinue = false;
                              /*  if (mIncidentImages != null && mIncidentImages.size() > 0) {
                                    updateIncidentImage(mFileArrayList.get(i), mMyDailyResponse.getMyDailyResponseData().getId(), mIncidentImages.get(i).getId());
                                } else {*/
                                uploadIncidentImage(mFileArrayList.get(i), mMyDailyResponse.getMyDailyResponseData().getId());
                                // }
                            }
                        }
                    } else {
                        callAlertDialog(mIsDraft);
                    }
                } else {
                    mUtility.showSnackBarAlert(findViewById(R.id.container), mMyDailyResponse.getMessage(), false);
                }

            } else if (taskID == Constants.TaskID.UPLOAD_INCIDENT_IMAGE || taskID == Constants.TaskID.UPDATE_INCIDENT_IMAGE) {
                responseData = AppInstance.responseData;
                if (responseData.getCode() == Constants.ResponseCodes.RESPONSE_SUCCESS) {
                    isContinue = true;
                    if (mFileArrayList.size() > 0) {
                        mFileSize++;
                        Log.e("file size out", mFileSize + "");
                        if (mFileSize == mFileArrayList.size()) {
                            Log.e("file size in", mFileSize + "");

                            callAlertDialog(mIsDraft);
                        }
                    } else {
                        callAlertDialog(mIsDraft);
                    }
                }

            }
        } catch (Exception e) {
            mUtility.stopLoader();
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
            //Snackbar.make(mView.findViewById(R.id.container), errorMessage, Snackbar.LENGTH_SHORT).show();
            mUtility.showSnackBarAlert(findViewById(R.id.container), errorMessage, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.RequestCodes.REQ_CODE_READ_WRITE_FILE:
                selectImageDialog();
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_incident_iv:

                if (mReadPermissionCheck != PackageManager.PERMISSION_GRANTED && mWritePermissionCheck != PackageManager.PERMISSION_GRANTED && mCameraPermissonCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(NewIncidentDetailsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, Constants.RequestCodes.REQ_CODE_READ_WRITE_FILE);
                } else {
                    if (mFileArrayList.size() < 5) {
                        //mImagePosition = -1;
                        selectImageDialog();
                    } else {
                        mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.max_image_selection_validation), false);
                    }
                }
                break;
            case R.id.add_new_incident_Button:
                callAddNewIncident(false);
                break;
            case R.id.job_selection_tv:
                mUtility.hiddenInputMethod(NewIncidentDetailsActivity.this);
                getForemanJobSelectionList();
                break;
            case R.id.date_tv:
                datePickerDialog();
                break;
            case R.id.our_fault_tv:
                faultSelectionDialog();
                break;
            case R.id.incident_image_view1:
              /*  if (mReadPermissionCheck != PackageManager.PERMISSION_GRANTED && mWritePermissionCheck != PackageManager.PERMISSION_GRANTED && mCameraPermissonCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(NewIncidentDetailsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, Constants.RequestCodes.REQ_CODE_READ_WRITE_FILE);
                } else {*/
                //mImagePosition = 0;
                // selectImageDialog();
                startImagePagerActivity(0);
                //}
                break;
            case R.id.incident_image_view2:
              /*  if (mReadPermissionCheck != PackageManager.PERMISSION_GRANTED && mWritePermissionCheck != PackageManager.PERMISSION_GRANTED && mCameraPermissonCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(NewIncidentDetailsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, Constants.RequestCodes.REQ_CODE_READ_WRITE_FILE);
                } else {*/
                //  mImagePosition = 1;
                // selectImageDialog();
                startImagePagerActivity(1);
                //}
                break;
            case R.id.incident_image_view3:
               /* if (mReadPermissionCheck != PackageManager.PERMISSION_GRANTED && mWritePermissionCheck != PackageManager.PERMISSION_GRANTED && mCameraPermissonCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(NewIncidentDetailsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, Constants.RequestCodes.REQ_CODE_READ_WRITE_FILE);
                } else {*/
                // mImagePosition = 2;
                //  selectImageDialog();
                startImagePagerActivity(2);
                // }
                break;
            case R.id.incident_image_view4:
              /*  if (mReadPermissionCheck != PackageManager.PERMISSION_GRANTED && mWritePermissionCheck != PackageManager.PERMISSION_GRANTED && mCameraPermissonCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(NewIncidentDetailsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, Constants.RequestCodes.REQ_CODE_READ_WRITE_FILE);
                } else {*/
                //  mImagePosition = 3;
                //  selectImageDialog();
                startImagePagerActivity(3);
                // }
                break;
            case R.id.incident_image_view5:
               /* if (mReadPermissionCheck != PackageManager.PERMISSION_GRANTED && mWritePermissionCheck != PackageManager.PERMISSION_GRANTED && mCameraPermissonCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(NewIncidentDetailsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, Constants.RequestCodes.REQ_CODE_READ_WRITE_FILE);
                } else {*/
                //  mImagePosition = 4;
                //  selectImageDialog();
                startImagePagerActivity(4);
                // }
                break;

            case R.id.gps_tv:
                settingsrequest();
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
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


    public void settingsrequest() {
        mGoogleApiClient = new GoogleApiClient.Builder(NewIncidentDetailsActivity.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(NewIncidentDetailsActivity.this)
                .addOnConnectionFailedListener(NewIncidentDetailsActivity.this).build();
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
        result.setResultCallback(NewIncidentDetailsActivity.this);
    }


    protected void startLocationUpdates() {
        if (mGoogleApiClient != null) {

            if (ActivityCompat.checkSelfPermission(NewIncidentDetailsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(NewIncidentDetailsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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


    public void selectImageDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(NewIncidentDetailsActivity.this)
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                              /*  } else {
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


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public void setImageList(ArrayList<Bitmap> addedImageArrayList) {
        if (addedImageArrayList != null) {
            if (addedImageArrayList.size() == 1) {
                mRoundRectCornerImageView1.setImageBitmap(addedImageArrayList.get(0));
                mRoundRectCornerImageView1.setPadding(1, 1, 1, 1);
            }

            if (addedImageArrayList.size() == 2) {
                mRoundRectCornerImageView2.setImageBitmap(addedImageArrayList.get(1));
                mRoundRectCornerImageView2.setPadding(1, 1, 1, 1);
            }

            if (addedImageArrayList.size() == 3) {
                mRoundRectCornerImageView3.setImageBitmap(addedImageArrayList.get(2));
                mRoundRectCornerImageView3.setPadding(1, 1, 1, 1);
            }

            if (addedImageArrayList.size() == 4) {
                mRoundRectCornerImageView4.setImageBitmap(addedImageArrayList.get(3));
                mRoundRectCornerImageView4.setPadding(1, 1, 1, 1);
            }

            if (addedImageArrayList.size() == 5) {
                mRoundRectCornerImageView5.setImageBitmap(addedImageArrayList.get(4));
                mRoundRectCornerImageView5.setPadding(1, 1, 1, 1);
            }
        }
    }

    public void callAddNewIncident(Boolean isDraft) {
        mIsDraft = isDraft;
        if (mFileArrayList != null) {
            if (mFileArrayList.size() > 0) {
                mLastFile = mFileArrayList.get(mFileArrayList.size() - 1);
            }
        }

        IncidentData incidentData = new IncidentData();
        if (mFileArrayList == null && !isDraft) {
            mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.add_incident_images), false);
        } else if (mJobId == null) {
            mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.select_job), false);
        } else if (mWhatDamageEditText.getText().toString().isEmpty() && !isDraft) {
            mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.what_was_damaged), false);
        } else if (mWhoOwnsEditText.getText().toString().isEmpty() && !isDraft) {
            mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.who_owns_it), false);
        } else if (mAddressEditText.getText().toString().isEmpty() && !isDraft) {
            mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.location_validation), false);
        } else if (mTicketNumberEditText.getText().toString().isEmpty() && !isDraft) {
            mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.ticket_number_validation), false);
        } else if (mIsOurFaultTextView.getText().toString().isEmpty() && !isDraft) {
            mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.was_it_our_fault), false);
        } else if (mMarkEditText.getText().toString().isEmpty() && !isDraft) {
            mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.how_far_off_the_mark_was_it), false);
        } else if (mDateTextView.getText().toString().isEmpty() && !isDraft) {
            mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.When_did_it_happen), false);
        } else {

            incidentData.job_id = mJobId;
            incidentData.is_draft = isDraft;
            incidentData.user_id = PreferenceConfiguration.getUserID(NewIncidentDetailsActivity.this);
            incidentData.parent_id = PreferenceConfiguration.getParentID(NewIncidentDetailsActivity.this);
            incidentData.damage_Report = mWhatDamageEditText.getText().toString();
            incidentData.own_It = mWhoOwnsEditText.getText().toString();
            incidentData.address = mAddressEditText.getText().toString();
            incidentData.ticket_No = mTicketNumberEditText.getText().toString();
            incidentData.fault = mIsOurFaultTextView.getText().toString();
            incidentData.mark_Was_It = mMarkEditText.getText().toString();
            if (mLatLng != null) {
                incidentData.latitude = String.valueOf(mLatLng.latitude);
                incidentData.longitude = String.valueOf(mLatLng.longitude);
            }
            if (mIncidentDate != null) {
                incidentData.did_It_Happen = mIncidentDate;
            } else {
                incidentData.did_It_Happen = mUtility.formatDate(mDateTextView.getText().toString(), Constants.InputTag.NEW_DAILY_DATE_FORMAT, Constants.InputTag.POST_DATE_FORMAT);
            }
            incidentData.description = mNotesEditText.getText().toString();
            if (mIncidentDataList != null) {
                incidentData.id = mIncidentDataList.getId();
            }
            mUtility.startLoader(NewIncidentDetailsActivity.this, R.drawable.image_for_rotation);
            mSupervisorManager.addNewIncident(incidentData);
        }
    }

    public void getForemanJobSelectionList() {
        ForemanJobData foremanJobData = new ForemanJobData();
        mUtility.startLoader(NewIncidentDetailsActivity.this, R.drawable.image_for_rotation);
        foremanJobData.page = "1";
        foremanJobData.count = "20";
        foremanJobData.job_status = Constants.RequestCodes.REQ_CODE_PROGRESS;
        foremanJobData.users_id = PreferenceConfiguration.getUserID(NewIncidentDetailsActivity.this);
        mSupervisorManager.getForemanJobList(foremanJobData);
    }

    private void jobSelectionDialog() {

        // retrieve display dimensions
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        // inflate and adjust layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_job_selection, null);
        layout.setMinimumWidth((int) (displayRectangle.width() * 0.8f));
        layout.setMinimumHeight((int) (displayRectangle.height() * 0.8f));
        mDialog = new Dialog(NewIncidentDetailsActivity.this, R.style.alertDialogCustom);
        mDialog.setContentView(layout);

        //  mDialog.setContentView(R.layout.dialog_foreman_selection);
        ListView jobSelectionListView = (ListView) mDialog.findViewById(R.id.job_selection_lv);

        // mDialog.setTitle(getResources().getString(R.string.foreman_list));
        mDialog.show();


        mJobSelectionListAdapter = new JobSelectionListAdapter(NewIncidentDetailsActivity.this, R.layout.job_selection_list_item, mForemanJobDataList);
        jobSelectionListView.setAdapter(mJobSelectionListAdapter);

        jobSelectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mForemanJobData = mForemanJobDataList.get(position);
                mJobSelectionTextView.setText(mForemanJobDataList.get(position).getJob_detail().getJob_id());
                mJobId = mForemanJobDataList.get(position).getJob_detail().getId() + "";
                mSubmitButton.setBackgroundDrawable(ContextCompat.getDrawable(NewIncidentDetailsActivity.this, R.drawable.button_active));
                mDialog.dismiss();
            }
        });
    }


    private void faultSelectionDialog() {

        // retrieve display dimensions
        // Rect displayRectangle = new Rect();
        // Window window = getWindow();
        // window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        // inflate and adjust layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_fault_selection, null);
        // layout.setMinimumWidth((int) (displayRectangle.width() * 0.8f));
        // layout.setMinimumHeight((int) (displayRectangle.height() * 0.8f));
        mDialog = new Dialog(NewIncidentDetailsActivity.this, R.style.alertDialogCustom);
        mDialog.setContentView(layout);

        //  mDialog.setContentView(R.layout.dialog_foreman_selection);
        TextView yesTextView = (TextView) mDialog.findViewById(R.id.yes_tv);
        TextView noTextView = (TextView) mDialog.findViewById(R.id.no_tv);

        // mDialog.setTitle(getResources().getString(R.string.foreman_list));
        mDialog.show();

        yesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsOurFaultTextView.setText(getResources().getString(R.string.yes));
                mDialog.dismiss();
            }
        });
        noTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsOurFaultTextView.setText(getResources().getString(R.string.no));
                mDialog.dismiss();
            }
        });
    }


    public void datePickerDialog() {
        final Calendar mCalendar = Calendar.getInstance();
        mDateFormatter = new SimpleDateFormat(Constants.InputTag.NEW_DAILY_DATE_FORMAT, Locale.US);
        mDateTimeFormatter = new SimpleDateFormat(Constants.InputTag.GET_DATE_FORMAT, Locale.US);
        DatePickerDialog datePickerDialog = new DatePickerDialog(NewIncidentDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                mIncidentDate = mDateTimeFormatter.format(mCalendar.getTime());
                mDateTextView.setText(mDateFormatter.format(mCalendar.getTime()));
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }

    public void uploadIncidentImage(File file, String id) {
        try {

            // create RequestBody instance from file
            RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file", file.getName(), fbody);

            RequestBody requestid = RequestBody.create(MediaType.parse("multipart/form-data"), id);

            //if (_id != null) {
            // RequestBody request_id = RequestBody.create(MediaType.parse("multipart/form-data"), _id);
            // }

            mSupervisorManager.uploadIncidentImage(body, requestid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateIncidentImage(File file, String id, String _id) {
        try {

            // create RequestBody instance from file
            RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file", file.getName(), fbody);

            RequestBody requestid = RequestBody.create(MediaType.parse("multipart/form-data"), id);

            //if (_id != null) {
            RequestBody request_id = RequestBody.create(MediaType.parse("multipart/form-data"), _id);
            // }

            mSupervisorManager.updateIncidentImage(body, requestid, request_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setIncidentDetails(IncidentDataList incidentDetails, JobDetailsData jobDetailsData) {
        if (incidentDetails != null) {
            //mJobSelectionTextView.setText(incidentDetails.getDescription());
            //mJobId = incidentDetails.getId();
            mWhatDamageEditText.setText(incidentDetails.getDamage_Report());
            mWhoOwnsEditText.setText(incidentDetails.getOwn_It());
            mAddressEditText.setText(incidentDetails.getAddress());
            mTicketNumberEditText.setText(incidentDetails.getTicket_No());
            mIsOurFaultTextView.setText(incidentDetails.getFault());
            mMarkEditText.setText(incidentDetails.getMark_Was_It());
            if (incidentDetails.getDid_It_Happen() != null) {
                mDateTextView.setText(mUtility.formatDate(incidentDetails.getDid_It_Happen(), Constants.InputTag.GET_DATE_FORMAT, Constants.InputTag.NEW_DAILY_DATE_FORMAT));
            }
            mNotesEditText.setText(incidentDetails.getDescription());

        }
        if (jobDetailsData != null) {
            mJobSelectionTextView.setText(jobDetailsData.getJob_id());
            mJobId = jobDetailsData.get_id() + "";
        }
        mIsTextChange = false;
    }

    public void setIncidentImages(ArrayList<IncidentImages> incidentImages) {
        if (incidentImages != null) {
            if (incidentImages.size() == 1) {
                imageLoader.displayImage(Constants.WebServices.BASE_URL
                        + incidentImages.get(0).getImage_path(), mRoundRectCornerImageView1, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mProgressBar1.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mProgressBar1.setVisibility(View.GONE);
                        mRoundRectCornerImageView1.setImageResource(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mProgressBar1.setVisibility(View.GONE);
                        mRoundRectCornerImageView1.setPadding(1, 1, 1, 1);
                    }
                });
            }

            if (incidentImages.size() == 2) {
                imageLoader.displayImage(Constants.WebServices.BASE_URL
                        + incidentImages.get(0).getImage_path(), mRoundRectCornerImageView1, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mProgressBar1.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mProgressBar1.setVisibility(View.GONE);
                        mRoundRectCornerImageView1.setImageResource(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mProgressBar1.setVisibility(View.GONE);
                        mRoundRectCornerImageView1.setPadding(1, 1, 1, 1);
                    }
                });

                imageLoader.displayImage(Constants.WebServices.BASE_URL
                        + incidentImages.get(1).getImage_path(), mRoundRectCornerImageView2, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mProgressBar2.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mProgressBar2.setVisibility(View.GONE);
                        mRoundRectCornerImageView2.setImageResource(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mProgressBar2.setVisibility(View.GONE);
                        mRoundRectCornerImageView2.setPadding(1, 1, 1, 1);
                    }
                });
            }

            if (incidentImages.size() == 3) {
                imageLoader.displayImage(Constants.WebServices.BASE_URL
                        + incidentImages.get(0).getImage_path(), mRoundRectCornerImageView1, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mProgressBar1.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mProgressBar1.setVisibility(View.GONE);
                        mRoundRectCornerImageView1.setImageResource(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mProgressBar1.setVisibility(View.GONE);
                        mRoundRectCornerImageView1.setPadding(1, 1, 1, 1);
                    }
                });

                imageLoader.displayImage(Constants.WebServices.BASE_URL
                        + incidentImages.get(1).getImage_path(), mRoundRectCornerImageView2, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mProgressBar2.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mProgressBar2.setVisibility(View.GONE);
                        mRoundRectCornerImageView2.setImageResource(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mProgressBar2.setVisibility(View.GONE);
                        mRoundRectCornerImageView2.setPadding(1, 1, 1, 1);
                    }
                });

                imageLoader.displayImage(Constants.WebServices.BASE_URL
                        + incidentImages.get(2).getImage_path(), mRoundRectCornerImageView3, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mProgressBar3.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mProgressBar3.setVisibility(View.GONE);
                        mRoundRectCornerImageView3.setImageResource(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mProgressBar3.setVisibility(View.GONE);
                        mRoundRectCornerImageView3.setPadding(1, 1, 1, 1);
                    }
                });
            }

            if (incidentImages.size() == 4) {
                imageLoader.displayImage(Constants.WebServices.BASE_URL
                        + incidentImages.get(0).getImage_path(), mRoundRectCornerImageView1, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mProgressBar1.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mProgressBar1.setVisibility(View.GONE);
                        mRoundRectCornerImageView1.setImageResource(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mProgressBar1.setVisibility(View.GONE);
                        mRoundRectCornerImageView1.setPadding(1, 1, 1, 1);
                    }
                });

                imageLoader.displayImage(Constants.WebServices.BASE_URL
                        + incidentImages.get(1).getImage_path(), mRoundRectCornerImageView2, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mProgressBar2.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mProgressBar2.setVisibility(View.GONE);
                        mRoundRectCornerImageView2.setImageResource(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mProgressBar2.setVisibility(View.GONE);
                        mRoundRectCornerImageView2.setPadding(1, 1, 1, 1);
                    }
                });

                imageLoader.displayImage(Constants.WebServices.BASE_URL
                        + incidentImages.get(2).getImage_path(), mRoundRectCornerImageView3, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mProgressBar3.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mProgressBar3.setVisibility(View.GONE);
                        mRoundRectCornerImageView3.setImageResource(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mProgressBar3.setVisibility(View.GONE);
                        mRoundRectCornerImageView3.setPadding(1, 1, 1, 1);
                    }
                });

                imageLoader.displayImage(Constants.WebServices.BASE_URL
                        + incidentImages.get(3).getImage_path(), mRoundRectCornerImageView4, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mProgressBar4.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mProgressBar4.setVisibility(View.GONE);
                        mRoundRectCornerImageView4.setImageResource(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mProgressBar4.setVisibility(View.GONE);
                        mRoundRectCornerImageView4.setPadding(1, 1, 1, 1);
                    }
                });
            }
            if (incidentImages.size() > 4) {
                imageLoader.displayImage(Constants.WebServices.BASE_URL
                        + incidentImages.get(0).getImage_path(), mRoundRectCornerImageView1, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mProgressBar1.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mProgressBar1.setVisibility(View.GONE);
                        mRoundRectCornerImageView1.setImageResource(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mProgressBar1.setVisibility(View.GONE);
                        mRoundRectCornerImageView1.setPadding(1, 1, 1, 1);
                    }
                });

                imageLoader.displayImage(Constants.WebServices.BASE_URL
                        + incidentImages.get(1).getImage_path(), mRoundRectCornerImageView2, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mProgressBar2.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mProgressBar2.setVisibility(View.GONE);
                        mRoundRectCornerImageView2.setImageResource(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mProgressBar2.setVisibility(View.GONE);
                        mRoundRectCornerImageView2.setPadding(1, 1, 1, 1);
                    }
                });

                imageLoader.displayImage(Constants.WebServices.BASE_URL
                        + incidentImages.get(2).getImage_path(), mRoundRectCornerImageView3, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mProgressBar3.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mProgressBar3.setVisibility(View.GONE);
                        mRoundRectCornerImageView3.setImageResource(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mProgressBar3.setVisibility(View.GONE);
                        mRoundRectCornerImageView3.setPadding(1, 1, 1, 1);
                    }
                });

                imageLoader.displayImage(Constants.WebServices.BASE_URL
                        + incidentImages.get(3).getImage_path(), mRoundRectCornerImageView4, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mProgressBar4.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mProgressBar4.setVisibility(View.GONE);
                        mRoundRectCornerImageView4.setImageResource(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mProgressBar4.setVisibility(View.GONE);
                        mRoundRectCornerImageView4.setPadding(1, 1, 1, 1);
                    }
                });

                imageLoader.displayImage(Constants.WebServices.BASE_URL
                        + incidentImages.get(4).getImage_path(), mRoundRectCornerImageView5, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mProgressBar5.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mProgressBar5.setVisibility(View.GONE);
                        mRoundRectCornerImageView5.setImageResource(R.drawable.ic_insert_photo);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mProgressBar5.setVisibility(View.GONE);
                        mRoundRectCornerImageView5.setPadding(1, 1, 1, 1);
                    }
                });
            }
        }
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imageFile = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
        //Uri photoURI = Uri.fromFile(createImageFile());
        mImageUri = FileProvider.getUriForFile(NewIncidentDetailsActivity.this, getApplicationContext().getPackageName() + ".provider", imageFile);

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
                .start(NewIncidentDetailsActivity.this);

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
                .start(NewIncidentDetailsActivity.this);
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
                    status.startResolutionForResult(NewIncidentDetailsActivity.this, REQUEST_CHECK_SETTINGS);
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
                        mAddressEditText.setAdapter(adapter);
                    }
                    break;
                case PLACES_DETAILS:
                    HashMap<String, String> hm = result.get(0);

                    // Getting latitude from the parsed data
                    double latitude = Double.parseDouble(hm.get("lat"));

                    // Getting longitude from the parsed data
                    double longitude = Double.parseDouble(hm.get("lng"));

                    // mLatLng = new LatLng(latitude, longitude);
                    // initCamera();


                    break;
            }
        }

    }

    private String getPlaceDetailsUrl(String ref) {

        // Obtain browser key from https://code.google.com/apis/console
        String key = "key=AIzaSyA4Q27nW0z0r7Wjeo_QfH3peUTfIMG-klc";//

        // reference of place
        String reference = "reference=" + ref;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = reference + "&" + sensor + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = GOOGLE_PLACE_DETAILS_API + output + "?" + parameters;

        return url;
    }


    private String getAutoCompleteUrl(String place) {

        // Obtain browser key from https://code.google.com/apis/console
        String key = "key=AIzaSyA4Q27nW0z0r7Wjeo_QfH3peUTfIMG-klc";//

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
        String url = GOOGLE_PLACE_AUTOCOMPLETE_API + output + "?" + parameters;

        return url;
    }

    protected void startImagePagerActivity(int position) {
        if (mIncidentImages != null) {
            if (mIncidentImages.size() > 0) {
                Intent intent = new Intent(NewIncidentDetailsActivity.this, ViewImageActivity.class);
                intent.putExtra(Constants.ButtonTags.FRAGMENT_INDEX, ImagePagerFragment.INDEX);
                intent.putExtra(Constants.ButtonTags.IMAGE_POSITION, position);
                startActivity(intent);
            } else {
                mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.no_image_found), false);
            }
        } else {
            mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.no_image_found), false);
        }

    }

    public void callAlertDialog(boolean isDraft) {
        mUtility.stopLoader();
        if (isDraft) {
            Utility.showAlertDialog(NewIncidentDetailsActivity.this, 0, R.string.your_incident_has_been_saved, R.string.Ok, 0, new DialogActionCallback() {
                @Override
                public void doOnPositive() {
                    finish();
                }

                @Override
                public void doOnNegative() {

                }
            });
        } else {
            Utility.showAlertDialog(NewIncidentDetailsActivity.this, 0, R.string.your_incident_has_been_submitted, R.string.Ok, 0, new DialogActionCallback() {
                @Override
                public void doOnPositive() {
                    finish();
                }

                @Override
                public void doOnNegative() {

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (mIsTextChange) {
            Utility.showAlertDialog(NewIncidentDetailsActivity.this, 0, R.string.your_incident_discard_alert_message, R.string.yes, R.string.no, new DialogActionCallback() {
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
