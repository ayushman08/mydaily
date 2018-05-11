package com.smartdata.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.smartdata.dataobject.CrewsDataList;
import com.smartdata.dataobject.CrewsDetails;
import com.smartdata.dataobject.DailiesImage;
import com.smartdata.dataobject.ForemanJobDataList;
import com.smartdata.dataobject.MyDailyData;
import com.smartdata.dataobject.MyDailyDataList;
import com.smartdata.dataobject.MyDailyDetails;
import com.smartdata.dataobject.MyDailyResponse;
import com.smartdata.dataobject.ProductDataList;
import com.smartdata.dataobject.ResponseData;
import com.smartdata.dataobject.SupervisorDataList;
import com.smartdata.dataobject.SupervisorDetails;
import com.smartdata.dialogs.AlertDialogManager;
import com.smartdata.fragments.DailyImagePagerFragment;
import com.smartdata.interfaces.ServiceRedirection;
import com.smartdata.managers.LoginManager;
import com.smartdata.managers.SupervisorManager;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.RoundRectCornerImageView;
import com.smartdata.utils.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by Anurag Sethi
 * The activity is used for handling the login screen actions
 */
public class SubmittalDetailsActivity extends AppActivity implements ServiceRedirection, View.OnClickListener {

    private Utility mUtility;
    private AppCompatButton mAcceptAppCompatButton, mRejectAppCompatButton;
    private MyDailyDataList mMyDailyDataList;
    private SupervisorDetails mSupervisorDetails;
    private CrewsDetails mCrewsDetails;
    private ArrayList<BillableItems> mBillableItems;
    private SupervisorManager mSupervisorManager;
    private MyDailyDetails mMyDailyDetails;
    private ArrayList<DailiesImage> mDailiesImageArrayList;

    private TextView mJobSelectionTextView, mWorkDateTextView, mToWorkDateTextView, mProductionTextView, mProductionTextView1, mProductionTextView2,
            mDailyDateTextView, mDailyJobIDTextView, mGPSTextView, mSupervisorTextView, mCrewTextView, mNotesTextView, mJobMapTextView,
            mJobLocationTicketTextView, mProductQuantityTextView, mProductQuantityTextView1, mProductQuantityTextView2;
    private LinearLayout mDailyLinearLayout;
    private ImageView mAddImageView;
    private RoundRectCornerImageView imageView1, imageView2, imageView3, imageView4, imageView5;
    private boolean isContinue = true, isProduction = false;

    private Dialog mDialog;
    private String path, filePath, mProductID1, mProductID2, mProductID3, mCrewID, mSupervisorID, mJobID, mTitle;

    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private int mViewID = 0;
    private ProgressBar mProgressBar1, mProgressBar2, mProgressBar3, mProgressBar4, mProgressBar5;
    private LinearLayout mProductionLinearLayout1, mProductionLinearLayout2;
    private Vibrator mVibrator = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submittal_details);
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
    }


    private void bindViews() {

        mAcceptAppCompatButton = (AppCompatButton) findViewById(R.id.acceptButton);
        mAcceptAppCompatButton.setOnClickListener(this);

        mRejectAppCompatButton = (AppCompatButton) findViewById(R.id.rejectButton);
        mRejectAppCompatButton.setOnClickListener(this);

        mAddImageView = (ImageView) findViewById(R.id.add_image_iv);
        mAddImageView.setVisibility(View.GONE);

        if (mTitle.equals(getResources().getString(R.string.accepted))) {
            mAcceptAppCompatButton.setVisibility(View.GONE);
            mRejectAppCompatButton.setVisibility(View.GONE);

        } else {
            mAcceptAppCompatButton.setVisibility(View.VISIBLE);
            mRejectAppCompatButton.setVisibility(View.VISIBLE);
        }

        mDailyLinearLayout = (LinearLayout) findViewById(R.id.daily_layout);


        mDailyDateTextView = (TextView) findViewById(R.id.daily_date_tv);
        //mDailyDateTextView.setText(new SimpleDateFormat(Constants.InputTag.DATE_FORMAT).format(Calendar.getInstance().getTime()) + "");
        if (mMyDailyDataList != null) {
            mDailyDateTextView.setText("#" + mMyDailyDataList.getDaily_number());
        }
        mDailyJobIDTextView = (TextView) findViewById(R.id.job_id_tv);
        mGPSTextView = (TextView) findViewById(R.id.gps_tv);
        mGPSTextView.setOnClickListener(this);

        mSupervisorTextView = (TextView) findViewById(R.id.supervisor_tv);
        mSupervisorTextView.setOnClickListener(this);

        mCrewTextView = (TextView) findViewById(R.id.crew_tv);
        mCrewTextView.setOnClickListener(this);

        mJobMapTextView = (TextView) findViewById(R.id.job_map_et);
        mJobLocationTicketTextView = (TextView) findViewById(R.id.job_location_ticket_et);
        mNotesTextView = (TextView) findViewById(R.id.notes_et);

        mProductionLinearLayout1 = (LinearLayout) findViewById(R.id.production_ll1);
        mProductionLinearLayout2 = (LinearLayout) findViewById(R.id.production_ll2);

        mProductQuantityTextView = (TextView) findViewById(R.id.product_quantity_et);
        mProductQuantityTextView1 = (TextView) findViewById(R.id.product_quantity_et1);
        mProductQuantityTextView2 = (TextView) findViewById(R.id.product_quantity_et2);
        //mProductQuantityTextView.setOnClickListener(this);

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
        mJobSelectionTextView.setOnClickListener(this);

        mWorkDateTextView = (TextView) findViewById(R.id.work_date_tv);
        mWorkDateTextView.setOnClickListener(this);

        mToWorkDateTextView = (TextView) findViewById(R.id.to_work_date_tv);
        mToWorkDateTextView.setOnClickListener(this);

        mWorkDateTextView.setText(new SimpleDateFormat(Constants.InputTag.NEW_DAILY_DATE_FORMAT).format(Calendar.getInstance().getTime()));

        mProductionTextView = (TextView) findViewById(R.id.production_item_tv);
        mProductionTextView.setOnClickListener(this);
        mProductionTextView1 = (TextView) findViewById(R.id.production_item_tv1);
        mProductionTextView1.setOnClickListener(this);
        mProductionTextView2 = (TextView) findViewById(R.id.production_item_tv2);
        mProductionTextView2.setOnClickListener(this);

        setDailyData(mMyDailyDataList);
        setIncidentImages(mDailiesImageArrayList);
    }


    private void initViews() {
        mUtility = new Utility(SubmittalDetailsActivity.this);

        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        mTitle = getIntent().getStringExtra(Constants.RequestCodes.REQ_CODE_SUBMITTALS);
        if (mTitle.equals(getResources().getString(R.string.daily))) {
            mUtility.customActionBar(SubmittalDetailsActivity.this, true, R.string.daily);
        } else if (mTitle.equals(getResources().getString(R.string.accepted))) {
            mUtility.customActionBar(SubmittalDetailsActivity.this, true, R.string.accepted);
        }

        this.imageLoader = ImageLoader.getInstance();
        this.imageLoader.init(ImageLoaderConfiguration.createDefault(SubmittalDetailsActivity.this));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_insert_photo)
                .showImageForEmptyUri(R.drawable.ic_insert_photo)
                .showImageOnFail(R.drawable.ic_insert_photo).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();


        mMyDailyDataList = getIntent().getParcelableExtra(Constants.RequestCodes.REQ_CODE_DAILY_DATA);
        mSupervisorDetails = getIntent().getParcelableExtra(Constants.RequestCodes.REQ_CODE_SUPERVISOR_DAILY_DATA);
        mCrewsDetails = getIntent().getParcelableExtra(Constants.RequestCodes.REQ_CODE_CREW_DAILY_DATA);
        mBillableItems = getIntent().getParcelableArrayListExtra(Constants.RequestCodes.REQ_CODE_BILLABLE_DAILY_DATA);
        mDailiesImageArrayList = getIntent().getParcelableArrayListExtra(Constants.RequestCodes.REQ_CODE_IMAGE_DAILY_DATA);
        mMyDailyDetails = getIntent().getParcelableExtra(Constants.RequestCodes.REQ_CODE_JOB_DAILY_DATA);
        mSupervisorManager = new SupervisorManager(SubmittalDetailsActivity.this, this);

        MyApplication myApplication = ((MyApplication) getApplicationContext());
        myApplication.setmDailiesImageArrayList(mDailiesImageArrayList);
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
            if (taskID == Constants.TaskID.UPDATE_DAILY_STATUS) {
               /* if (AppInstance.myDailyResponse.getStatus().equals(Constants.ResponseCodes.RESPONSE_SUCCESS)) {
                    mUtility.showSnackBarAlert(findViewById(R.id.container), AppInstance.myDailyResponse.getMessage(), false);
                } else {
                    mUtility.showSnackBarAlert(findViewById(R.id.container), AppInstance.myDailyResponse.getMessage(), false);
                }*/
                finish();
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
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.acceptButton:
                updateDailyStatus(Constants.RequestCodes.REQ_CODE_DAILY_ACCEPTED, null);
                break;

            case R.id.rejectButton:
                showDailyRejectionDialog();
                break;
            case R.id.imageView1:
                startImagePagerActivity(0);
                break;
            case R.id.imageView2:
                startImagePagerActivity(1);
                break;
            case R.id.imageView3:
                startImagePagerActivity(2);
                break;
            case R.id.imageView4:
                startImagePagerActivity(3);
                break;
            case R.id.imageView5:
                startImagePagerActivity(4);
                break;
            case R.id.gps_tv:
                Intent intentObj = new Intent(SubmittalDetailsActivity.this, MapViewActivity.class);
                intentObj.putExtra(Constants.InputTag.LATITUDE, mMyDailyDataList.getLatitude());
                intentObj.putExtra(Constants.InputTag.LONGITUDE, mMyDailyDataList.getLongitude());
                startActivity(intentObj);
                break;
        }
    }


    public void setDailyData(MyDailyDataList myDailyDataList) {
        if (myDailyDataList != null) {
            mNotesTextView.setText(myDailyDataList.getNotes());
            isProduction = myDailyDataList.isNo_production();
            if (isProduction) {
                mDailyLinearLayout.setVisibility(View.GONE);
            } else {
                mDailyLinearLayout.setVisibility(View.VISIBLE);
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

            mJobMapTextView.setText(myDailyDataList.getJob_map());
            mJobLocationTicketTextView.setText(myDailyDataList.getJob_location());

            mWorkDateTextView.setText(mUtility.formatDate(myDailyDataList.getFrom_date(), Constants.InputTag.GET_DATE_FORMAT, Constants.InputTag.NEW_DAILY_DATE_FORMAT));
            mToWorkDateTextView.setText(mUtility.formatDate(myDailyDataList.getTo_date(), Constants.InputTag.GET_DATE_FORMAT, Constants.InputTag.NEW_DAILY_DATE_FORMAT));


            if (mBillableItems != null) {

                if (mBillableItems.size() == 1) {
                    if (mBillableItems.get(0).getName().isEmpty()) {
                        mProductionTextView.setText(getResources().getString(R.string.production_items));
                        mProductQuantityTextView.setText(getResources().getString(R.string.quantity));
                    } else {
                        mProductionTextView.setText(mBillableItems.get(0).getName());
                        mProductQuantityTextView.setText(mBillableItems.get(0).getQuantity());
                        // mProductionLinearLayout1.setVisibility(View.VISIBLE);
                        mProductID1 = mBillableItems.get(0).getId();
                    }
                }
                if (mBillableItems.size() == 2) {

                    if (mBillableItems.get(0).getName().isEmpty()) {
                        mProductionTextView.setText(getResources().getString(R.string.production_items));
                        mProductQuantityTextView.setText(getResources().getString(R.string.quantity));
                    } else {
                        mProductionTextView.setText(mBillableItems.get(0).getName());
                        mProductQuantityTextView.setText(mBillableItems.get(0).getQuantity());
                        mProductID1 = mBillableItems.get(0).getId();
                    }

                    if (mBillableItems.get(1).getName().isEmpty()) {
                        mProductionTextView1.setText(getResources().getString(R.string.production_items));
                        mProductQuantityTextView1.setText(getResources().getString(R.string.quantity));
                    } else {
                        mProductionTextView1.setText(mBillableItems.get(1).getName());
                        mProductQuantityTextView1.setText(mBillableItems.get(1).getQuantity());
                        mProductID2 = mBillableItems.get(1).getId();
                    }
                    mProductionLinearLayout1.setVisibility(View.VISIBLE);
                }
                if (mBillableItems.size() > 2) {
                    if (mBillableItems.get(0).getName().isEmpty()) {
                        mProductionTextView.setText(getResources().getString(R.string.production_items));
                        mProductQuantityTextView.setText(getResources().getString(R.string.quantity));
                    } else {
                        mProductionTextView.setText(mBillableItems.get(0).getName());
                        mProductQuantityTextView.setText(mBillableItems.get(0).getQuantity());
                        mProductID1 = mBillableItems.get(0).getId();
                    }

                    if (mBillableItems.get(1).getName().isEmpty()) {
                        mProductionTextView1.setText(getResources().getString(R.string.production_items));
                        mProductQuantityTextView1.setText(getResources().getString(R.string.quantity));
                    } else {
                        mProductionTextView1.setText(mBillableItems.get(1).getName());
                        mProductQuantityTextView1.setText(mBillableItems.get(1).getQuantity());
                        mProductID2 = mBillableItems.get(1).getId();
                    }

                    if (mBillableItems.get(2).getName().isEmpty()) {
                        mProductionTextView2.setText(getResources().getString(R.string.production_items));
                        mProductQuantityTextView2.setText(getResources().getString(R.string.quantity));
                    } else {
                        mProductionTextView2.setText(mBillableItems.get(2).getName());
                        mProductQuantityTextView2.setText(mBillableItems.get(2).getQuantity());
                        mProductID3 = mBillableItems.get(2).getId();
                    }
                    mProductionLinearLayout1.setVisibility(View.VISIBLE);
                    mProductionLinearLayout2.setVisibility(View.VISIBLE);
                }

            }

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


    private void updateDailyStatus(String status, String rejectNotes) {
        MyDailyData myDailyData = new MyDailyData();
        mUtility.startLoader(SubmittalDetailsActivity.this, R.drawable.image_for_rotation);
        myDailyData.id = mMyDailyDataList.getId();
        myDailyData.status = status;
        if (rejectNotes != null) {
            myDailyData.reject_notes = rejectNotes;
        }
        mSupervisorManager.updateDailyStatus(myDailyData);
    }


    private void showDailyRejectionDialog() {

        // retrieve display dimensions
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        // inflate and adjust layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_daily_rejection, null);
        layout.setMinimumWidth((int) (displayRectangle.width() * 0.8f));
        layout.setMinimumHeight((int) (displayRectangle.height() * 0.8f));
        mDialog = new Dialog(SubmittalDetailsActivity.this, R.style.alertDialogCustom);
        mDialog.setContentView(layout);

        mDialog.setTitle(getResources().getString(R.string.foreman_list));
        mDialog.show();

        final EditText rejectionNoteEditText = (EditText) mDialog.findViewById(R.id.rejection_notes_et);
        AppCompatButton submitButton = (AppCompatButton) mDialog.findViewById(R.id.submitButton);
        AppCompatButton cancelButton = (AppCompatButton) mDialog.findViewById(R.id.cancelButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rejectionNoteEditText.getText().toString().isEmpty()) {
                    // mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.daily_rejection_details), false);
                    Animation shake = AnimationUtils.loadAnimation(SubmittalDetailsActivity.this, R.anim.shake);
                    mVibrator.vibrate(25);
                    rejectionNoteEditText.startAnimation(shake);
                    rejectionNoteEditText.setHintTextColor(Color.RED);

                } else {
                    updateDailyStatus(Constants.RequestCodes.REQ_CODE_DAILY_REJECTED, rejectionNoteEditText.getText().toString());
                    mDialog.dismiss();
                }

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    protected void startImagePagerActivity(int position) {
        if (mDailiesImageArrayList != null) {
            if (mDailiesImageArrayList.size() > 0) {
                Intent intent = new Intent(SubmittalDetailsActivity.this, ViewDailyImageActivity.class);
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

}
