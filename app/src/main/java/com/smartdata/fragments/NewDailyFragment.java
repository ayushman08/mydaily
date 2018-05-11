package com.smartdata.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import com.smartdata.activities.SettingActivity;
import com.smartdata.adapters.CrewsSelectionListAdapter;
import com.smartdata.adapters.JobSelectionListAdapter;
import com.smartdata.adapters.ProductionListAdapter;
import com.smartdata.adapters.SupervisorSelectionListAdapter;
import com.smartdata.config.PreferenceConfiguration;
import com.smartdata.dataobject.AppInstance;
import com.smartdata.dataobject.CrewsData;
import com.smartdata.dataobject.CrewsDataList;
import com.smartdata.dataobject.ForemanJobData;
import com.smartdata.dataobject.ForemanJobDataList;
import com.smartdata.dataobject.MyDailyData;
import com.smartdata.dataobject.MyDailyResponse;
import com.smartdata.dataobject.ProductDataList;
import com.smartdata.dataobject.ResponseData;
import com.smartdata.dataobject.SupervisorData;
import com.smartdata.dataobject.SupervisorDataList;
import com.smartdata.interfaces.DialogActionCallback;
import com.smartdata.interfaces.MyDailyRedirection;
import com.smartdata.interfaces.ServiceRedirection;
import com.smartdata.managers.SupervisorManager;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.RoundRectCornerImageView;
import com.smartdata.utils.Utility;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

public class NewDailyFragment extends BaseFragment implements ServiceRedirection, View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback {

    private View mView;
    private Utility mUtilObj = null;
    private Uri mImageUri;
    private Dialog mDialog;
    private RoundRectCornerImageView imageView1, imageView2, imageView3, imageView4, imageView5;
    private ArrayList<Bitmap> mAddedImageArrayList = new ArrayList<>();
    private ArrayList<File> mFileArrayList = new ArrayList<>();
    private SupervisorManager mSupervisorManager;
    private ArrayList<ForemanJobDataList> mForemanJobDataList;
    private ForemanJobDataList mForemanJobData;
    private TextView mJobSelectionTextView, mWorkDateTextView, mToWorkDateTextView, mProductionTextView, mProductionTextView1, mProductionTextView2, mDailyDateTextView,
            mDailyJobIDTextView, mGPSTextView, mSupervisorTextView, mCrewTextView;
    private ArrayList<ProductDataList> mProductDataList;
    private ProductionListAdapter mProductionListAdapter;
    private CrewsSelectionListAdapter mCrewsSelectionListAdapter;
    private SupervisorSelectionListAdapter mSupervisorSelectionListAdapter;
    private String mProductID1 = "", mProductID2 = "", mProductID3 = "", mCrewID, mSupervisorID = null, mJobID;
    private boolean isProduction = false;
    private EditText mNotesEditText, mJobMapEditText, mJobLocationTicketEditText, mProductQuantityEditText, mProductQuantityEditText1, mProductQuantityEditText2;
    private AppCompatButton mSendButton;
    private ArrayList<CrewsDataList> mCrewsDataList;
    private ArrayList<SupervisorDataList> mSupervisorDataList;
    private MyDailyResponse mMyDailyResponse;
    private boolean isContinue = true;
    // private ArrayList<File> mFileListSize = new ArrayList<>();
    private File mLastFile;
    private ResponseData responseData;
    // Member variable : hold current date info
    private int mCurrentYear;
    private int mCurrentMonth;
    private int mCurrentDay;
    private int mYear;
    private int mMonth;
    private int mDay;
    private String month, descFirst, descSecond, descThird;
    private Calendar mCurrentDateTime;
    int mDateOf;
    private int mDateOfBirth;
    private SimpleDateFormat mDateFormatter;
    private LinearLayout mDailyLinearLayout, mDailyOtherLinearLayout;

    //location variable
    private int mReadPermissionCheck, mWritePermissionCheck, mCameraPermissonCheck, mFineLocation, mCoarseLocation;
    private LatLng mLatLng;
    private GoogleApiClient mGoogleApiClient;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    protected static final int REQUEST_CHECK_SETTINGS = 171;

    private MyDailyRedirection mMyDailyRedirection;
    private int mViewID = 0;
    private LinearLayout mProductionLinearLayout1, mProductionLinearLayout2;

    private ImageView mDraftImageView, mSettingImageView;
    private Toolbar mToolbar;
    private TextView mTitleTextView, mSaveTextView;
    private Date mFromDate = null, mToDate = null;
    private boolean isLocation = true;
    private int mFileSize = 0;
    private boolean mIsDraft = false;

    public NewDailyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_new_daily, container, false);
        setToolbar();
        initViews();
        bindViews();
        return mView;
    }

    private void bindViews() {

        mSendButton = (AppCompatButton) mView.findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(this);

        mDailyLinearLayout = (LinearLayout) mView.findViewById(R.id.daily_layout);
        mDailyOtherLinearLayout = (LinearLayout) mView.findViewById(R.id.daily_other_layout);

        mDailyDateTextView = (TextView) mView.findViewById(R.id.daily_date_tv);
        mDailyDateTextView.setText(new SimpleDateFormat(Constants.InputTag.DATE_FORMAT).format(Calendar.getInstance().getTime()) + "");
        mDailyJobIDTextView = (TextView) mView.findViewById(R.id.job_id_tv);
        mGPSTextView = (TextView) mView.findViewById(R.id.gps_tv);
        mGPSTextView.setOnClickListener(this);

        mSupervisorTextView = (TextView) mView.findViewById(R.id.supervisor_tv);
        mSupervisorTextView.setOnClickListener(this);

        mCrewTextView = (TextView) mView.findViewById(R.id.crew_tv);
        mCrewTextView.setOnClickListener(this);

        mJobMapEditText = (EditText) mView.findViewById(R.id.job_map_et);
        mJobLocationTicketEditText = (EditText) mView.findViewById(R.id.job_location_ticket_et);
        mNotesEditText = (EditText) mView.findViewById(R.id.notes_et);

        mProductionLinearLayout1 = (LinearLayout) mView.findViewById(R.id.production_ll1);
        mProductionLinearLayout2 = (LinearLayout) mView.findViewById(R.id.production_ll2);

        mDailyJobIDTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    mSendButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.button_active));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mProductQuantityEditText = (EditText) mView.findViewById(R.id.product_quantity_et);
        mProductQuantityEditText1 = (EditText) mView.findViewById(R.id.product_quantity_et1);
        mProductQuantityEditText2 = (EditText) mView.findViewById(R.id.product_quantity_et2);
        //mProductQuantityTextView.setOnClickListener(this);

        ImageView mAddImageView = (ImageView) mView.findViewById(R.id.add_image_iv);
        imageView1 = (RoundRectCornerImageView) mView.findViewById(R.id.imageView1);
        imageView2 = (RoundRectCornerImageView) mView.findViewById(R.id.imageView2);
        imageView3 = (RoundRectCornerImageView) mView.findViewById(R.id.imageView3);
        imageView4 = (RoundRectCornerImageView) mView.findViewById(R.id.imageView4);
        imageView5 = (RoundRectCornerImageView) mView.findViewById(R.id.imageView5);
        mAddImageView.setOnClickListener(this);

        mJobSelectionTextView = (TextView) mView.findViewById(R.id.job_selection_tv);
        mJobSelectionTextView.setOnClickListener(this);

        mWorkDateTextView = (TextView) mView.findViewById(R.id.work_date_tv);
        mWorkDateTextView.setOnClickListener(this);

        mToWorkDateTextView = (TextView) mView.findViewById(R.id.to_work_date_tv);
        mToWorkDateTextView.setOnClickListener(this);

        mWorkDateTextView.setText(new SimpleDateFormat(Constants.InputTag.NEW_DAILY_DATE_FORMAT).format(Calendar.getInstance().getTime()));
        mFromDate = Calendar.getInstance().getTime();

        mProductionTextView = (TextView) mView.findViewById(R.id.production_item_tv);
        mProductionTextView1 = (TextView) mView.findViewById(R.id.production_item_tv1);
        mProductionTextView2 = (TextView) mView.findViewById(R.id.production_item_tv2);
        mProductionTextView.setOnClickListener(this);
        mProductionTextView1.setOnClickListener(this);
        mProductionTextView2.setOnClickListener(this);
    }


    private void initViews() {
        mUtilObj = new Utility(getActivity());

        mFineLocation = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        mCoarseLocation = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);
        if ((mFineLocation != PackageManager.PERMISSION_GRANTED) && (mCoarseLocation != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Constants.RequestCodes.REQ_CODE_ACCESS_FINE_LOCATION);
        }


        mSupervisorManager = new SupervisorManager(getActivity(), this);

        mWritePermissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        mReadPermissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        mCameraPermissonCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);

        AppCompatCheckBox mProductionCheckbox = (AppCompatCheckBox) mView.findViewById(R.id.production_checkbox);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the options menu from XML
        //menu.clear();
        // inflater.inflate(R.menu.menu_clear_setting, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                //Snackbar.make(mView.findViewById(R.id.container), "Active submittals Search menu is in progress!", Snackbar.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_image_iv:

                if (mReadPermissionCheck != PackageManager.PERMISSION_GRANTED && mWritePermissionCheck != PackageManager.PERMISSION_GRANTED && mCameraPermissonCheck != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, Constants.RequestCodes.REQ_CODE_READ_WRITE_FILE);
                } else {
                    if (mFileArrayList.size() < 5) {
                        selectImageDialog();
                    } else {
                        mUtilObj.showSnackBarAlert(mView.findViewById(R.id.container), getResources().getString(R.string.max_image_selection_validation), false);
                    }
                }
                break;

            case R.id.job_selection_tv:
                getForemanJobSelectionList();
                break;
            case R.id.work_date_tv:
                fromDatePickerDialog(mWorkDateTextView);
                break;
            case R.id.to_work_date_tv:
                toDatePickerDialog(mToWorkDateTextView);
                break;
            case R.id.production_item_tv:
                mViewID = 1;
                getBillableItem();
                break;
            case R.id.production_item_tv1:
                mViewID = 2;
                getBillableItem();
                break;
            case R.id.production_item_tv2:
                mViewID = 3;
                getBillableItem();
                break;
            case R.id.sendButton:
                addNewDaily(false);
                break;
            case R.id.crew_tv:
                getCrewsList();
                break;
            case R.id.supervisor_tv:
                getSupervisorList();
                break;
            case R.id.gps_tv:
                settingsrequest();
                break;
        }

    }

    @Override
    public void onSuccessRedirection(int taskID) {
        try {
            mUtilObj.stopLoader();
            if (taskID == Constants.TaskID.GET_FOREMAN_JOB_LIST) {
                mForemanJobDataList = AppInstance.foremanJobData.foremanJobDataList;
                if (mForemanJobDataList.size() > 0) {
                    jobSelectionDialog();
                } else {
                    mUtilObj.showSnackBarAlert(mView.findViewById(R.id.container), getActivity().getResources().getString(R.string.no_jobs), false);
                }
            } else if (taskID == Constants.TaskID.GET_PRODUCTION_LIST) {
                mProductDataList = AppInstance.productData.productDataList;
                if (mProductDataList.size() > 0) {
                    productionItemDialog();
                } else {
                    mUtilObj.showSnackBarAlert(mView.findViewById(R.id.container), getActivity().getResources().getString(R.string.no_products), false);
                }
            } else if (taskID == Constants.TaskID.GET_BILLABLE_ITEM) {

                mProductDataList = AppInstance.productData.productDataList;
                if (mProductDataList.size() > 0) {
                    productionItemDialog();
                } else {
                    mUtilObj.showSnackBarAlert(mView.findViewById(R.id.container), getActivity().getResources().getString(R.string.no_products), false);
                }
            } else if (taskID == Constants.TaskID.ADD_NEW_DAILY) {
                mMyDailyResponse = AppInstance.myDailyResponse;
                if (mMyDailyResponse.getCode() == 200) {
                    if (!isProduction && mFileArrayList != null && mFileArrayList.size() > 0) {
                        if (isContinue) {
                            mUtilObj.startLoader(getActivity(), R.drawable.image_for_rotation);

                            for (int i = 0; i < mFileArrayList.size(); i++) {
                                // mFileListSize.add(mFileArrayList.get(i));
                                isContinue = false;
                                /*if (mLastFile.equals(mFileArrayList.get(i))) {
                                    Utility.showAlertDialog(getActivity(), 0, R.string.your_daily_has_been_submitted, R.string.Ok, 0, new DialogActionCallback() {
                                        @Override
                                        public void doOnPositive() {
                                            mMyDailyRedirection.onMyDailyRedirection();
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
                        callAlertDialog(mIsDraft);
                    }

                } else {
                    mUtilObj.showSnackBarAlert(mView.findViewById(R.id.container), mMyDailyResponse.getMessage(), false);
                }
            } else if (taskID == Constants.TaskID.UPLOAD_DAILIES_IMAGE) {
                // if (!isProduction) {
                responseData = AppInstance.responseData;
                if (responseData.getCode() == Constants.ResponseCodes.RESPONSE_SUCCESS) {
                    isContinue = true;
                    if (mFileArrayList.size() > 0) {
                        mFileSize++;
                        if (mFileSize == mFileArrayList.size()) {
                            callAlertDialog(mIsDraft);
                        }
                    } else {
                        callAlertDialog(mIsDraft);
                    }
                }

            } else if (taskID == Constants.TaskID.GET_CREWS_LIST) {
                mUtilObj.stopLoader();
                mCrewsDataList = AppInstance.crewsData.crewsDataList;
                if (mCrewsDataList.size() > 0) {
                    crewListDialog();
                } else {
                    mUtilObj.showSnackBarAlert(mView.findViewById(R.id.container), getResources().getString(R.string.no_crew_members), false);
                }
            } else if (taskID == Constants.TaskID.GET_SUPERVISOR_LIST) {
                mSupervisorDataList = AppInstance.supervisorData.getSupervisorDataList();
                if (mSupervisorDataList.size() > 0) {
                    supervisorListDialog();
                } else {
                    mUtilObj.showSnackBarAlert(mView.findViewById(R.id.container), getResources().getString(R.string.no_supervisors), false);
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
            mUtilObj.showSnackBarAlert(mView.findViewById(R.id.container), errorMessage, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.RequestCodes.REQ_CODE_READ_WRITE_FILE:
                //android.os.Process.killProcess(android.os.Process.myPid());
                selectImageDialog();
                break;
            case Constants.RequestCodes.REQ_CODE_ACCESS_FINE_LOCATION:
                // settingsrequest();
                break;
            default:
                break;
        }
    }

    @Override
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
                                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
                                mAddedImageArrayList.add(bm);
                                mFileArrayList.add(new File(resultUri.getPath()));
                                setImageList(mAddedImageArrayList);
                            } catch (IOException e) {
                                e.printStackTrace();
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

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
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

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private void jobSelectionDialog() {

        // retrieve display dimensions
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        // inflate and adjust layout
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_job_selection, null);
        layout.setMinimumWidth((int) (displayRectangle.width() * 0.8f));
        layout.setMinimumHeight((int) (displayRectangle.height() * 0.8f));
        mDialog = new Dialog(getActivity(), R.style.alertDialogCustom);
        mDialog.setContentView(layout);

        //  mDialog.setContentView(R.layout.dialog_foreman_selection);
        ListView jobSelectionListView = (ListView) mDialog.findViewById(R.id.job_selection_lv);

        // mDialog.setTitle(getResources().getString(R.string.foreman_list));
        mDialog.show();

        JobSelectionListAdapter mJobSelectionListAdapter = new JobSelectionListAdapter(getActivity(), R.layout.job_selection_list_item, mForemanJobDataList);
        jobSelectionListView.setAdapter(mJobSelectionListAdapter);

        jobSelectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mForemanJobData = mForemanJobDataList.get(position);
                mJobSelectionTextView.setText(mForemanJobDataList.get(position).getJob_detail().getClient());
                mDailyJobIDTextView.setText(mForemanJobDataList.get(position).getJob_detail().getJob_id());
                mJobID = mForemanJobDataList.get(position).getJob_detail().getId();
                if (mForemanJobData.getJob_detail().getDaily_sumbission_report().equals("1")) {
                    mSupervisorTextView.setVisibility(View.VISIBLE);
                } else if (mForemanJobData.getJob_detail().getDaily_sumbission_report().equals("3")) {
                    mSupervisorTextView.setVisibility(View.GONE);
                }

                mDialog.dismiss();
            }
        });
    }

    public void getForemanJobSelectionList() {
        ForemanJobData foremanJobData = new ForemanJobData();
        mUtilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
        foremanJobData.page = "1";
        foremanJobData.count = "20";
        foremanJobData.job_status = Constants.RequestCodes.REQ_CODE_PROGRESS;
        foremanJobData.users_id = PreferenceConfiguration.getUserID(getActivity());
        mSupervisorManager.getForemanJobList(foremanJobData);
    }


    //To pick date
    public void toDatePickerDialog(final TextView dateTextView) {
        final Calendar mCalendar = Calendar.getInstance();
        mDateFormatter = new SimpleDateFormat(Constants.InputTag.NEW_DAILY_DATE_FORMAT, Locale.US);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
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
                        mUtilObj.showSnackBarAlert(mView.findViewById(R.id.container), getActivity().getResources().getString(R.string.date_validation), false);
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {



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

    /* private void getProductionList() {
         mUtilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
         mSupervisorManager.getProductionList(PreferenceConfiguration.getParentID(getActivity()));
     }*/

    private void getBillableItem() {
        if (mJobID != null) {
            mUtilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
            ProductDataList productDataList = new ProductDataList();
            productDataList.id = mJobID;
            mSupervisorManager.getBillableItem(productDataList);
        } else {
            mUtilObj.showSnackBarAlert(mView.findViewById(R.id.container), getActivity().getResources().getString(R.string.select_job), false);
        }

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
        mDialog = new Dialog(getActivity(), R.style.alertDialogCustom);
        mDialog.setContentView(layout);

        ListView productSelectionListView = (ListView) mDialog.findViewById(R.id.product_selection_lv);

        // mDialog.setTitle(getResources().getString(R.string.product_list));
        mDialog.show();


        mProductionListAdapter = new ProductionListAdapter(getActivity(), R.layout.product_selection_list_item, mProductDataList);
        productSelectionListView.setAdapter(mProductionListAdapter);
        productSelectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mViewID == 1) {
                    mProductionTextView.setText(mProductDataList.get(position).getName());
                    mProductID1 = mProductDataList.get(position).getId() + "";
                    descFirst = mProductDataList.get(position).getDescription() + "";
                    mProductionTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                    mProductionLinearLayout1.setVisibility(View.VISIBLE);
                } else if (mViewID == 2) {
                    mProductionTextView1.setText(mProductDataList.get(position).getName());
                    mProductID2 = mProductDataList.get(position).getId() + "";
                    descSecond = mProductDataList.get(position).getDescription() + "";

                    mProductionTextView1.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                    mProductionLinearLayout2.setVisibility(View.VISIBLE);
                } else if (mViewID == 3) {
                    mProductionTextView2.setText(mProductDataList.get(position).getName());
                    mProductID3 = mProductDataList.get(position).getId() + "";
                    descThird = mProductDataList.get(position).getDescription() + "";

                    mProductionTextView2.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                }
                mDialog.dismiss();
            }
        });
    }

    public void addNewDaily(boolean isDraft) {
        mIsDraft = isDraft;
        if (mFileArrayList != null) {
            if (mFileArrayList.size() > 0) {
                mLastFile = mFileArrayList.get(mFileArrayList.size() - 1);
            }
        }
        MyDailyData myDailyData = new MyDailyData();
        myDailyData.is_draft = isDraft;
        myDailyData.user_id = PreferenceConfiguration.getUserID(getActivity());
        myDailyData.parent_id = PreferenceConfiguration.getParentID(getActivity());
        myDailyData.notes = mNotesEditText.getText().toString();
        if (mForemanJobData != null) {
            if (isProduction) {
                //myDailyData.crews_id = "";
                myDailyData.no_production = isProduction;
                myDailyData.job_id = mForemanJobData.getJob_detail().getId();


                if (mWorkDateTextView.getText().toString().isEmpty()) {
                    mUtilObj.showSnackBarAlert(mView.findViewById(R.id.container), getResources().getString(R.string.work_date_validation), false);
                } else {
                    if (!mWorkDateTextView.getText().toString().isEmpty()) {
                        myDailyData.from_date = mUtilObj.formatDate(mWorkDateTextView.getText().toString(), Constants.InputTag.NEW_DAILY_DATE_FORMAT, Constants.InputTag.DAILY_POST_DATE_FORMAT);
                    }
                    if (!mToWorkDateTextView.getText().toString().isEmpty() && !mToWorkDateTextView.getText().toString().equals(getActivity().getResources().getString(R.string.work_date))) {
                        myDailyData.to_date = mUtilObj.formatDate(mToWorkDateTextView.getText().toString(), Constants.InputTag.NEW_DAILY_DATE_FORMAT, Constants.InputTag.DAILY_POST_DATE_FORMAT);
                    }
                    myDailyData.notes = mNotesEditText.getText().toString();

                }
                mUtilObj.startLoader(getActivity(), R.drawable.image_for_rotation);

                mSupervisorManager.addNewDaily(myDailyData);


            } else {
                myDailyData.no_production = isProduction;
                myDailyData.job_id = mForemanJobData.getJob_detail().getId();
                if (!mJobMapEditText.getText().toString().isEmpty()) {
                    myDailyData.job_map = mJobMapEditText.getText().toString();
                }
                if (!mJobLocationTicketEditText.getText().toString().isEmpty()) {
                    myDailyData.job_location = mJobLocationTicketEditText.getText().toString();
                }

                if (mLatLng != null) {
                    myDailyData.latitude = String.valueOf(mLatLng.latitude);
                    myDailyData.longitude = String.valueOf(mLatLng.longitude);
                }
                if (mWorkDateTextView.getText().toString().isEmpty()) {
                    mUtilObj.showSnackBarAlert(mView.findViewById(R.id.container), getResources().getString(R.string.work_date_validation), false);
                } else {
                    if (!mWorkDateTextView.getText().toString().isEmpty()) {
                        myDailyData.from_date = mUtilObj.formatDate(mWorkDateTextView.getText().toString(), Constants.InputTag.NEW_DAILY_DATE_FORMAT, Constants.InputTag.DAILY_POST_DATE_FORMAT);
                    }
                    if (!mToWorkDateTextView.getText().toString().isEmpty() && !mToWorkDateTextView.getText().toString().equals(getActivity().getResources().getString(R.string.work_date))) {
                        myDailyData.to_date = mUtilObj.formatDate(mToWorkDateTextView.getText().toString(), Constants.InputTag.NEW_DAILY_DATE_FORMAT, Constants.InputTag.DAILY_POST_DATE_FORMAT);
                    }
                    myDailyData.notes = mNotesEditText.getText().toString();

                    if (!mCrewTextView.getText().toString().isEmpty()) {
                        myDailyData.crews_id = mCrewID;
                    }
                    ProductDataList productDataList = new ProductDataList();
                    ArrayList<ProductDataList> productDataListArrayList = new ArrayList<ProductDataList>();


                    if (!mProductionTextView2.getText().toString().equals(getResources().getString(R.string.items))) {

                        // Log.e("quantity", mProductQuantityEditText.getText().toString() + mProductQuantityEditText1.getText().toString() + mProductQuantityEditText2.getText().toString());

                        // productDataList.setId(mProductID1 + "");
                        productDataList.setName(mProductionTextView.getText().toString() + "");
                        productDataList.setDescription(descFirst);
                        productDataList.setQuantity(mProductQuantityEditText.getText().toString());
                        productDataListArrayList.add(productDataList);

                        ProductDataList productDataList1 = new ProductDataList();
                        // productDataList1.setId(mProductID2 + "");
                        productDataList1.setName(mProductionTextView1.getText().toString() + "");
                        productDataList1.setDescription(descSecond);
                        productDataList1.setQuantity(mProductQuantityEditText1.getText().toString());
                        productDataListArrayList.add(productDataList1);

                        ProductDataList productDataList2 = new ProductDataList();
                        //productDataList2.setId(mProductID3 + "");
                        productDataList2.setName(mProductionTextView2.getText().toString() + "");
                        // productDataList2.setDescription("description");
                        productDataList2.setDescription(descThird);

                        productDataList2.setQuantity(mProductQuantityEditText2.getText().toString());
                        productDataListArrayList.add(productDataList2);

                    } else if (!mProductionTextView1.getText().toString().equals(getResources().getString(R.string.items))) {
                        //productDataList.setId(mProductID1 + "");
                        productDataList.setName(mProductionTextView.getText().toString());
                        // productDataList.setDescription("description");
                        productDataList.setDescription(descFirst);
                        productDataList.setQuantity(mProductQuantityEditText.getText().toString());
                        productDataListArrayList.add(productDataList);

                        ProductDataList productDataList1 = new ProductDataList();
                        //productDataList1.setId(mProductID2 + "");
                        productDataList1.setName(mProductionTextView1.getText().toString());
                        // productDataList1.setDescription("description");
                        productDataList1.setDescription(descSecond);
                        productDataList1.setQuantity(mProductQuantityEditText1.getText().toString());
                        productDataListArrayList.add(productDataList1);

                    } else if (!mProductionTextView.getText().toString().equals(getResources().getString(R.string.items))) {
                        //productDataList.setId(mProductID1 + "");
                        productDataList.setName(mProductionTextView.getText().toString());
                        // productDataList.setDescription("description");
                        productDataList.setDescription(descFirst);
                        productDataList.setQuantity(mProductQuantityEditText.getText().toString());
                        productDataListArrayList.add(productDataList);
                    }

                    myDailyData.setProductDataList(productDataListArrayList);
                    if (!mProductionTextView.getText().toString().equals(getActivity().getResources().getString(R.string.items)) && mProductQuantityEditText.getText().toString().isEmpty()) {
                        mUtilObj.showSnackBarAlert(mView.findViewById(R.id.container), getActivity().getResources().getString(R.string.product_quantity_validation), false);
                    } else if (!mProductionTextView1.getText().toString().equals(getActivity().getResources().getString(R.string.items)) && mProductQuantityEditText.getText().toString().isEmpty() && mProductQuantityEditText1.getText().toString().isEmpty()) {
                        mUtilObj.showSnackBarAlert(mView.findViewById(R.id.container), getActivity().getResources().getString(R.string.product_quantity_validation), false);
                    } else if (!mProductionTextView2.getText().toString().equals(getActivity().getResources().getString(R.string.items)) && mProductQuantityEditText.getText().toString().isEmpty() && mProductQuantityEditText1.getText().toString().isEmpty() && mProductQuantityEditText2.getText().toString().isEmpty()) {
                        mUtilObj.showSnackBarAlert(mView.findViewById(R.id.container), getActivity().getResources().getString(R.string.product_quantity_validation), false);
                    } else if (mForemanJobData.getJob_detail().getDaily_sumbission_report().equals("1")) {
                        //added boolean check for supervisor-kapil
                        if (mSupervisorID != null || mIsDraft) {
                            mUtilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
                            myDailyData.supervisor_id = mSupervisorID;
                            mSupervisorManager.addNewDaily(myDailyData);

                        } else {
                            mUtilObj.showSnackBarAlert(mView.findViewById(R.id.container), getResources().getString(R.string.please_select_supervisor), false);
                        }
                    } else {
                        mUtilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
                        mSupervisorManager.addNewDaily(myDailyData);
                    }
                }
            }
        } else {
            mUtilObj.showSnackBarAlert(mView.findViewById(R.id.container), getActivity().getResources().getString(R.string.select_job), false);
        }
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
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onStart() {
        super.onStart();
        // settingsrequest();
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
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
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

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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


    public void uploadDailiesImage(File file, String id) {
        try {
            if (file != null) {
                // create RequestBody instance from file
                RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                // MultipartBody.Part is used to send also the actual file name
                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file", file.getName(), fbody);

                RequestBody requestid = RequestBody.create(MediaType.parse("multipart/form-data"), id);

                mSupervisorManager.uploadDailiesImage(body, requestid);
            }
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

    private void getSupervisorList() {
        if (mJobID != null) {
            SupervisorData supervisorData = new SupervisorData();
            mUtilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
            supervisorData.jobId = mJobID;
            mSupervisorManager.getSupervisorList(supervisorData);
        } else {
            mUtilObj.showSnackBarAlert(mView.findViewById(R.id.container), getActivity().getResources().getString(R.string.select_job), false);
        }
    }

    private void crewListDialog() {

        // retrieve display dimensions
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        // inflate and adjust layout
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_crew_selection, null);
        layout.setMinimumWidth((int) (displayRectangle.width() * 0.8f));
        layout.setMinimumHeight((int) (displayRectangle.height() * 0.8f));
        mDialog = new Dialog(getActivity(), R.style.alertDialogCustom);
        mDialog.setContentView(layout);

        ListView crewsListView = (ListView) mDialog.findViewById(R.id.crew_selection_lv);

        // mDialog.setTitle(getResources().getString(R.string.product_list));
        mDialog.show();
        mCrewsSelectionListAdapter = new CrewsSelectionListAdapter(getActivity(), R.layout.foreman_list_item, mCrewsDataList);
        crewsListView.setAdapter(mCrewsSelectionListAdapter);
        crewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCrewTextView.setText(mCrewsDataList.get(position).getFirstname() + " " + mCrewsDataList.get(position).getLastname());
                mCrewID = mCrewsDataList.get(position).getId() + "";
                mProductionTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                mDialog.dismiss();
            }
        });
    }


    private void supervisorListDialog() {

        // retrieve display dimensions
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        // inflate and adjust layout
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_supervisor_selection, null);
        layout.setMinimumWidth((int) (displayRectangle.width() * 0.8f));
        layout.setMinimumHeight((int) (displayRectangle.height() * 0.8f));
        mDialog = new Dialog(getActivity(), R.style.alertDialogCustom);
        mDialog.setContentView(layout);

        ListView supervisorListView = (ListView) mDialog.findViewById(R.id.supervisor_selection_lv);

        // mDialog.setTitle(getResources().getString(R.string.product_list));
        mDialog.show();
        mSupervisorSelectionListAdapter = new SupervisorSelectionListAdapter(getActivity(), R.layout.foreman_list_item, mSupervisorDataList);
        supervisorListView.setAdapter(mSupervisorSelectionListAdapter);

        supervisorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSupervisorTextView.setText(mSupervisorDataList.get(position).getSupervisorJobAssignTo().getFirstname() + " " + mSupervisorDataList.get(position).getSupervisorJobAssignTo().getLastname());
                mSupervisorID = mSupervisorDataList.get(position).getSupervisorJobAssignTo().getId() + "";
                //mSupervisorTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                mDialog.dismiss();
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mMyDailyRedirection = (MyDailyRedirection) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement Callback");
        }
    }

 /*   private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(intent, Constants.RequestCodes.REQ_CODE_CAMERA);
    }*/

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imageFile = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
        mImageUri = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", imageFile);
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
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data
                        .getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        CropImage.activity(data.getData())
                .start(getContext(), this);

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

        try {
            Thread.sleep(4000); // Changes Made By Aniket
            CropImage.activity(mImageUri)
                    .start(getContext(), this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void setToolbar() {
        mToolbar = (Toolbar) mView.findViewById(R.id.new_daily_actionbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mDraftImageView = (ImageView) mView.findViewById(R.id.draft_view);
        mSettingImageView = (ImageView) mView.findViewById(R.id.new_daily_settings);
        mTitleTextView = (TextView) mView.findViewById(R.id.new_daily_title);
        mSaveTextView = (TextView) mView.findViewById(R.id.save_view);
        mTitleTextView.setText(getActivity().getResources().getString(R.string.new_daily));
        mSettingImageView.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                Intent intentObj = new Intent(getActivity(), SettingActivity.class);
                startActivity(intentObj);
            }
        });

        mDraftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewDaily(true);
            }
        });
        mSaveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewDaily(true);
            }
        });

    }

    public void callAlertDialog(boolean isDraft) {
        mUtilObj.stopLoader();
        if (isDraft) {
            Utility.showAlertDialog(getActivity(), 0, R.string.your_daily_has_been_saved, R.string.Ok, 0, new DialogActionCallback() {
                @Override
                public void doOnPositive() {
                    mMyDailyRedirection.onMyDailyRedirection();
                }

                @Override
                public void doOnNegative() {

                }
            });
        } else {
            Utility.showAlertDialog(getActivity(), 0, R.string.your_daily_has_been_submitted, R.string.Ok, 0, new DialogActionCallback() {
                @Override
                public void doOnPositive() {
                    mMyDailyRedirection.onMyDailyRedirection();
                }

                @Override
                public void doOnNegative() {

                }
            });
        }
    }

}
