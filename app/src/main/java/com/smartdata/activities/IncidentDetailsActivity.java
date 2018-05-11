package com.smartdata.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.smartdata.adapters.EditCrewsListAdapter;
import com.smartdata.config.PreferenceConfiguration;
import com.smartdata.dataobject.AppInstance;
import com.smartdata.dataobject.CrewsData;
import com.smartdata.dataobject.CrewsDataList;
import com.smartdata.dataobject.IncidentDataList;
import com.smartdata.dataobject.IncidentImages;
import com.smartdata.dataobject.JobDetailsData;
import com.smartdata.dialogs.AlertDialogManager;
import com.smartdata.fragments.ImagePagerFragment;
import com.smartdata.interfaces.ServiceRedirection;
import com.smartdata.managers.SupervisorManager;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.RoundRectCornerImageView;
import com.smartdata.utils.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by Anurag Sethi
 * The activity is used for handling the login screen actions
 */
public class IncidentDetailsActivity extends AppActivity implements ServiceRedirection, View.OnClickListener {

    private ActionBar mActionBar;
    private Utility mUtility;
    private AlertDialogManager mAleaAlertDialogManager;
    private SupervisorManager mSupervisorManager;
    private IncidentDataList mIncidentDataList;
    private ArrayList<IncidentImages> mIncidentImages;
    private JobDetailsData mJobDetailsData;

    private RoundRectCornerImageView mRoundRectCornerImageView1, mRoundRectCornerImageView2, mRoundRectCornerImageView3,
            mRoundRectCornerImageView4, mRoundRectCornerImageView5;
    private ProgressBar mProgressBar1, mProgressBar2, mProgressBar3, mProgressBar4, mProgressBar5;

    private TextView mIncidentDateTextView, mWhatDamageTextView, mWhoOwnsTextView, mAddressTextView, mTicketNumberTextView,
            mIsOurFaultTextView, mMarkTextView, mDateTextView, mNotesTextView, mJobSelectionTextView;

    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_details);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
        mIncidentDataList = getIntent().getParcelableExtra(Constants.RequestCodes.INCIDENT_DETAILS);
        mIncidentImages = getIntent().getParcelableArrayListExtra(Constants.RequestCodes.INCIDENT_IMAGES);
        mJobDetailsData = getIntent().getParcelableExtra(Constants.RequestCodes.INCIDENT_JOB_DETAILS);

        MyApplication myApplication = ((MyApplication) getApplicationContext());
        myApplication.setIncidentImages(mIncidentImages);


        mUtility = new Utility(IncidentDetailsActivity.this);
        mSupervisorManager = new SupervisorManager(IncidentDetailsActivity.this, this);
        mUtility.customActionBar(IncidentDetailsActivity.this, true, R.string.incident);
//        mUtility.setToolbar(IncidentDetailsActivity.this, getResources().getString(R.string.edit_crews), getResources().getString(R.string.submittals_search_hint), true, false);
        mAleaAlertDialogManager = new AlertDialogManager();

        this.imageLoader = ImageLoader.getInstance();
        this.imageLoader.init(ImageLoaderConfiguration.createDefault(IncidentDetailsActivity.this));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_insert_photo)
                .showImageForEmptyUri(R.drawable.ic_insert_photo)
                .showImageOnFail(R.drawable.ic_insert_photo).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

    }

    /**
     * Binds the UI controls
     *
     * @return none
     */
    @Override
    public void bindControls() {
        mJobSelectionTextView = (TextView) findViewById(R.id.job_selection_tv);
        mIncidentDateTextView = (TextView) findViewById(R.id.incident_date_tv);
        mWhatDamageTextView = (TextView) findViewById(R.id.what_damage_tv);
        mWhoOwnsTextView = (TextView) findViewById(R.id.who_owns_tv);
        mAddressTextView = (TextView) findViewById(R.id.address_tv);
        mTicketNumberTextView = (TextView) findViewById(R.id.ticket_number_tv);
        mIsOurFaultTextView = (TextView) findViewById(R.id.our_fault_tv);
        mMarkTextView = (TextView) findViewById(R.id.mark_tv);
        mDateTextView = (TextView) findViewById(R.id.date_tv);
        mNotesTextView = (TextView) findViewById(R.id.notes_tv);

        mRoundRectCornerImageView1 = (RoundRectCornerImageView) findViewById(R.id.incident_image_view1);
        mRoundRectCornerImageView2 = (RoundRectCornerImageView) findViewById(R.id.incident_image_view2);
        mRoundRectCornerImageView3 = (RoundRectCornerImageView) findViewById(R.id.incident_image_view3);
        mRoundRectCornerImageView4 = (RoundRectCornerImageView) findViewById(R.id.incident_image_view4);
        mRoundRectCornerImageView5 = (RoundRectCornerImageView) findViewById(R.id.incident_image_view5);

        mRoundRectCornerImageView1.setOnClickListener(this);
        mRoundRectCornerImageView2.setOnClickListener(this);
        mRoundRectCornerImageView3.setOnClickListener(this);
        mRoundRectCornerImageView4.setOnClickListener(this);
        mRoundRectCornerImageView5.setOnClickListener(this);

        mProgressBar1 = (ProgressBar) findViewById(R.id.progress_bar1);
        mProgressBar2 = (ProgressBar) findViewById(R.id.progress_bar2);
        mProgressBar3 = (ProgressBar) findViewById(R.id.progress_bar3);
        mProgressBar4 = (ProgressBar) findViewById(R.id.progress_bar4);
        mProgressBar5 = (ProgressBar) findViewById(R.id.progress_bar5);

        setIncidentDetails(mIncidentDataList);
        setIncidentImages(mIncidentImages);
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


    /**
     * The interface method implemented in the java files
     *
     * @param taskID the id based on which the relevant action is performed
     * @return none
     */

    @Override
    public void onSuccessRedirection(int taskID) {
        try {

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
            //Snackbar.make(mView.findViewById(R.id.container), errorMessage, Snackbar.LENGTH_SHORT).show();
            mUtility.showSnackBarAlert(findViewById(R.id.container), errorMessage, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setIncidentDetails(IncidentDataList incidentDetails) {
        if (incidentDetails != null) {
            mWhatDamageTextView.setText(incidentDetails.getDamage_Report());
            mWhoOwnsTextView.setText(incidentDetails.getOwn_It());
            mAddressTextView.setText(incidentDetails.getAddress());
            mTicketNumberTextView.setText(incidentDetails.getTicket_No());
            mIsOurFaultTextView.setText(incidentDetails.getFault());
            mMarkTextView.setText(incidentDetails.getMark_Was_It());
            mNotesTextView.setText(incidentDetails.getDescription());

            if (incidentDetails.getDid_It_Happen() != null) {
                mDateTextView.setText(mUtility.formatDate(incidentDetails.getDid_It_Happen(), Constants.InputTag.GET_DATE_FORMAT, Constants.InputTag.NEW_DAILY_DATE_FORMAT));
                mIncidentDateTextView.setText(mUtility.formatDate(incidentDetails.getDid_It_Happen(), Constants.InputTag.GET_DATE_FORMAT, Constants.InputTag.GET_INCIDENT_DATE_FORMAT));
            }

            if (mJobDetailsData != null) {
                mJobSelectionTextView.setText(mJobDetailsData.getJob_id());
                //mJobId = jobDetailsData.get_id() + "";
            }
        }
    }

    public void setIncidentImages(ArrayList<IncidentImages> incidentImages) {
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
        if (incidentImages.size() >= 5) {
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

    protected void startImagePagerActivity(int position) {
        if (mIncidentImages != null) {
            if (mIncidentImages.size() > 0) {
                Intent intent = new Intent(IncidentDetailsActivity.this, ViewImageActivity.class);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.incident_image_view1:
                startImagePagerActivity(0);
                break;
            case R.id.incident_image_view2:
                startImagePagerActivity(1);
                break;
            case R.id.incident_image_view3:
                startImagePagerActivity(2);
                break;
            case R.id.incident_image_view4:
                startImagePagerActivity(3);
                break;
            case R.id.incident_image_view5:
                startImagePagerActivity(4);
                break;
        }
    }
}
