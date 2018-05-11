package com.smartdata.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smartdata.config.PreferenceConfiguration;
import com.smartdata.fragments.ActiveSubmittalsFragment;
import com.smartdata.fragments.ForemanCrewsFragment;
import com.smartdata.fragments.IncidentReportsFragment;
import com.smartdata.fragments.MyDailiesFragment;
import com.smartdata.fragments.ForemanJobsFragment;
import com.smartdata.fragments.NewDailyFragment;
import com.smartdata.fragments.NewJobFragment;
import com.smartdata.fragments.SubmittalFragment;
import com.smartdata.fragments.SupervisorCrewsFragment;
import com.smartdata.fragments.SupervisorJobsFragment;
import com.smartdata.interfaces.DialogActionCallback;
import com.smartdata.interfaces.MyDailyRedirection;
import com.smartdata.interfaces.SearchInterface;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Utility;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.lang.reflect.Field;


/**
 * Created by Anurag Sethi
 */
public class DashboardActivity extends AppActivity implements View.OnClickListener, MyDailyRedirection {


    private ActionBar mActionBar;
    private Utility mUtility;

    LinearLayout mForemanLinearLayout, mSupervisorLinearLayout, mSupervisorSubmittalsLinearLayout,
            mSupervisorNewJobLinearLayout, mSupervisorCrewsLinearLayout, mSupervisorJobsLinearLayout, mForemanDailiesLinearLayout,
            mForemanJobsLinearLayout, mForemanNewDailiesLinearLayout, mForemanIncidentsLinearLayout,
            mForemanCrewsLinearLayout;

    ImageView mSupervisorSubmittalsImageView, mSupervisorNewJobImageView, mSupervisorCrewsImageView, mSupervisorJobsImageView,
            mForemanDailiesImageView, mForemanJobsImageView, mForemanNewDailiesImageView,
            mForemanIncidentsImageView, mForemanCrewsImageView;

    TextView mSupervisorSubmittalsTextView, mSupervisorCrewsTextView, mSupervisorJobsTextView,
            mForemanDailiesTextView, mForemanJobsTextView,
            mForemanIncidentsTextView, mForemanCrewsTextView;

    private Long backPressed = 0L;
    private boolean isNewDaily = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        initData();
        bindControls();
        bindViewListener();
        ((MyApplication) getApplicationContext()).setAnalyticTracking(this, "dashboard");

        /*if (savedInstanceState == null) {
            if (PreferenceConfiguration.getUserTypeId(DashboardActivity.this) == 1) {
                //mUtility.setToolbar(DashboardActivity.this, getResources().getString(R.string.submittals), getResources().getString(R.string.search_hint), true, true);
                mForemanLinearLayout.setVisibility(View.GONE);
                mSupervisorLinearLayout.setVisibility(View.VISIBLE);
                setResource(mSupervisorLinearLayout, mSupervisorSubmittalsImageView, R.drawable.ic_action_dailies_s, mSupervisorSubmittalsTextView);
                commitFragment(new SubmittalFragment());
            } else {
                // mUtility.setToolbar(DashboardActivity.this, getResources().getString(R.string.my_dailies), getResources().getString(R.string.search_hint), true, true);
                mForemanLinearLayout.setVisibility(View.VISIBLE);
                mSupervisorLinearLayout.setVisibility(View.GONE);
                setResource(mForemanDailiesLinearLayout, mForemanDailiesImageView, R.drawable.ic_action_dailies_s, mForemanDailiesTextView);
                commitFragment(new MyDailiesFragment());
            }

        }*/

    }

    private void bindViewListener() {
        mSupervisorSubmittalsLinearLayout.setOnClickListener(this);
        mSupervisorNewJobLinearLayout.setOnClickListener(this);
        mSupervisorCrewsLinearLayout.setOnClickListener(this);
        mSupervisorJobsLinearLayout.setOnClickListener(this);

        mForemanDailiesLinearLayout.setOnClickListener(this);
        mForemanJobsLinearLayout.setOnClickListener(this);
        mForemanNewDailiesLinearLayout.setOnClickListener(this);
        mForemanIncidentsLinearLayout.setOnClickListener(this);
        mForemanCrewsLinearLayout.setOnClickListener(this);

        mSupervisorSubmittalsImageView.setOnClickListener(this);
        mSupervisorNewJobImageView.setOnClickListener(this);
        mSupervisorCrewsImageView.setOnClickListener(this);
        mSupervisorJobsImageView.setOnClickListener(this);

        mForemanDailiesImageView.setOnClickListener(this);
        mForemanJobsImageView.setOnClickListener(this);
        mForemanNewDailiesImageView.setOnClickListener(this);
        mForemanIncidentsImageView.setOnClickListener(this);
        mForemanCrewsImageView.setOnClickListener(this);

        mSupervisorSubmittalsTextView.setOnClickListener(this);
        mSupervisorCrewsTextView.setOnClickListener(this);
        mSupervisorJobsTextView.setOnClickListener(this);

        mForemanDailiesTextView.setOnClickListener(this);
        mForemanJobsTextView.setOnClickListener(this);
        mForemanIncidentsTextView.setOnClickListener(this);
        mForemanCrewsTextView.setOnClickListener(this);

        if (PreferenceConfiguration.getUserTypeId(DashboardActivity.this) == 1) {

            mForemanLinearLayout.setVisibility(View.GONE);
            mSupervisorLinearLayout.setVisibility(View.VISIBLE);
            setResource(mSupervisorLinearLayout, mSupervisorSubmittalsImageView, R.drawable.ic_action_dailies_s, mSupervisorSubmittalsTextView);
            commitFragment(new SubmittalFragment());
        } else {
            mForemanLinearLayout.setVisibility(View.VISIBLE);
            mSupervisorLinearLayout.setVisibility(View.GONE);
            setResource(mForemanDailiesLinearLayout, mForemanDailiesImageView, R.drawable.ic_action_dailies_s, mForemanDailiesTextView);
            commitFragment(new MyDailiesFragment());
        }
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
     * Initializes the objects
     *
     * @return none
     */
    public void initData() {
        mUtility = new Utility(DashboardActivity.this);
    }

    /**
     * Binds the UI controls
     *
     * @return none
     */
    public void bindControls() {
        mForemanLinearLayout = (LinearLayout) findViewById(R.id.foreman_ll);
        mSupervisorLinearLayout = (LinearLayout) findViewById(R.id.supervisor_ll);

        mSupervisorSubmittalsLinearLayout = (LinearLayout) findViewById(R.id.supervisor_submittal_ll);
        mSupervisorNewJobLinearLayout = (LinearLayout) findViewById(R.id.supervisor_add_ll);
        mSupervisorCrewsLinearLayout = (LinearLayout) findViewById(R.id.supervisor_crews_ll);
        mSupervisorJobsLinearLayout = (LinearLayout) findViewById(R.id.supervisor_job_ll);

        mForemanDailiesLinearLayout = (LinearLayout) findViewById(R.id.foreman_dailies_ll);
        mForemanJobsLinearLayout = (LinearLayout) findViewById(R.id.foreman_jobs_ll);
        mForemanNewDailiesLinearLayout = (LinearLayout) findViewById(R.id.foreman_add_ll);
        mForemanIncidentsLinearLayout = (LinearLayout) findViewById(R.id.foreman_incidents_ll);
        mForemanCrewsLinearLayout = (LinearLayout) findViewById(R.id.foreman_crews_ll);

        mSupervisorSubmittalsImageView = (ImageView) findViewById(R.id.supervisor_submittal_iv);
        mSupervisorNewJobImageView = (ImageView) findViewById(R.id.supervisor_add_iv);
        mSupervisorCrewsImageView = (ImageView) findViewById(R.id.supervisor_crews_iv);
        mSupervisorJobsImageView = (ImageView) findViewById(R.id.supervisor_jobs_iv);

        mForemanDailiesImageView = (ImageView) findViewById(R.id.foreman_dailies_iv);
        mForemanJobsImageView = (ImageView) findViewById(R.id.foreman_jobs_iv);
        mForemanNewDailiesImageView = (ImageView) findViewById(R.id.foreman_add_iv);
        mForemanIncidentsImageView = (ImageView) findViewById(R.id.foreman_incidents_iv);
        mForemanCrewsImageView = (ImageView) findViewById(R.id.foreman_crews_iv);

        mSupervisorSubmittalsTextView = (TextView) findViewById(R.id.supervisor_submittal_tv);
        mSupervisorCrewsTextView = (TextView) findViewById(R.id.supervisor_crews_tv);
        mSupervisorJobsTextView = (TextView) findViewById(R.id.supervisor_jobs_tv);

        mForemanDailiesTextView = (TextView) findViewById(R.id.foreman_dailies_tv);
        mForemanJobsTextView = (TextView) findViewById(R.id.foreman_jobs_tv);
        mForemanIncidentsTextView = (TextView) findViewById(R.id.foreman_incidents_tv);
        mForemanCrewsTextView = (TextView) findViewById(R.id.foreman_crews_tv);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * the menu layout has the 'add/new' menu item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            case R.id.action_settings:
                Intent intentObj = new Intent(DashboardActivity.this, SettingActivity.class);
                startActivity(intentObj);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void commitFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();

       /* fragmentManager.beginTransaction().setCustomAnimations(R.anim.fadein, R.anim
                .fadeout)
                .replace(R.id.content_frame, fragment, null).commit();*/
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, null).commit();
    }

    public void commitAndAddToBackStack(Fragment fragment, String tag) {

        try {
            FragmentManager fragmentManager = getSupportFragmentManager();

            if (fragmentManager.getBackStackEntryCount() > 0) {
                String lastAddedFragment = fragmentManager.getBackStackEntryAt(fragmentManager
                        .getBackStackEntryCount() - 1).getName();
                if (lastAddedFragment != null && fragment != null && lastAddedFragment
                        .equalsIgnoreCase(fragment.getClass().getSimpleName())) {
                    return;
                }
            }

            fragmentManager.beginTransaction().setCustomAnimations(R.anim.fadein, R
                    .anim.fadeout)
                    .replace(R.id.content_frame, fragment, tag).addToBackStack
                    (fragment.getClass().getSimpleName()).commit();


        } catch (Exception e) {
            // Log.e(TAG, "Exception caught");
            e.printStackTrace();
        }
    }

    private void setResource(LinearLayout selectedLinearLayout, ImageView iView, int imageID, TextView tView) {

        mSupervisorSubmittalsLinearLayout.setEnabled(true);
        mSupervisorNewJobLinearLayout.setEnabled(true);
        mSupervisorCrewsLinearLayout.setEnabled(true);
        mForemanDailiesLinearLayout.setEnabled(true);
        mForemanJobsLinearLayout.setEnabled(true);
        mForemanNewDailiesLinearLayout.setEnabled(true);
        mForemanIncidentsLinearLayout.setEnabled(true);
        mForemanCrewsLinearLayout.setEnabled(true);
        mSupervisorJobsLinearLayout.setEnabled(true);

        mSupervisorSubmittalsImageView.setEnabled(true);
        mSupervisorNewJobImageView.setEnabled(true);
        mSupervisorCrewsImageView.setEnabled(true);
        mForemanDailiesImageView.setEnabled(true);
        mForemanJobsImageView.setEnabled(true);
        mForemanNewDailiesImageView.setEnabled(true);
        mForemanIncidentsImageView.setEnabled(true);
        mForemanCrewsImageView.setEnabled(true);
        mSupervisorJobsImageView.setEnabled(true);

        mSupervisorSubmittalsImageView.setImageResource(R.drawable.ic_action_dailies);
        mSupervisorNewJobImageView.setImageResource(R.drawable.ic_action_add);
        mSupervisorCrewsImageView.setImageResource(R.drawable.ic_action_crews);
        mForemanDailiesImageView.setImageResource(R.drawable.ic_action_dailies);
        mForemanJobsImageView.setImageResource(R.drawable.ic_action_jobs);
        mForemanNewDailiesImageView.setImageResource(R.drawable.ic_action_add);
        mForemanIncidentsImageView.setImageResource(R.drawable.ic_action_incidents);
        mForemanCrewsImageView.setImageResource(R.drawable.ic_action_crews);
        mSupervisorJobsImageView.setImageResource(R.drawable.ic_action_jobs);

        mSupervisorSubmittalsTextView.setEnabled(true);
        mSupervisorCrewsTextView.setEnabled(true);
        mForemanDailiesTextView.setEnabled(true);
        mForemanJobsTextView.setEnabled(true);
        mForemanIncidentsTextView.setEnabled(true);
        mForemanCrewsTextView.setEnabled(true);
        mSupervisorJobsTextView.setEnabled(true);

        mSupervisorSubmittalsTextView.setTextColor(ContextCompat.getColor(DashboardActivity.this, R.color.black));
        mSupervisorCrewsTextView.setTextColor(ContextCompat.getColor(DashboardActivity.this, R.color.black));
        mForemanDailiesTextView.setTextColor(ContextCompat.getColor(DashboardActivity.this, R.color.black));
        mForemanJobsTextView.setTextColor(ContextCompat.getColor(DashboardActivity.this, R.color.black));
        mForemanIncidentsTextView.setTextColor(ContextCompat.getColor(DashboardActivity.this, R.color.black));
        mForemanCrewsTextView.setTextColor(ContextCompat.getColor(DashboardActivity.this, R.color.black));
        mSupervisorJobsTextView.setTextColor(ContextCompat.getColor(DashboardActivity.this, R.color.black));
        if (selectedLinearLayout != null) {
            iView.setImageResource(imageID);
            selectedLinearLayout.setEnabled(false);
            iView.setEnabled(false);
            if (tView != null) {
                tView.setTextColor(ContextCompat.getColor(DashboardActivity.this, R.color.colorPrimary));
                tView.setEnabled(false);
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.supervisor_submittal_ll:
            case R.id.supervisor_submittal_iv:
            case R.id.supervisor_submittal_tv:
                setResource(mSupervisorLinearLayout, mSupervisorSubmittalsImageView, R.drawable.ic_action_dailies_s, mSupervisorSubmittalsTextView);
                commitFragment(new SubmittalFragment());
                break;
            case R.id.supervisor_add_ll:
            case R.id.supervisor_add_iv:
                setResource(mSupervisorNewJobLinearLayout, mSupervisorNewJobImageView, R.drawable.ic_action_add, null);
                commitFragment(new NewJobFragment());
                break;
            case R.id.supervisor_job_ll:
            case R.id.supervisor_jobs_iv:
            case R.id.supervisor_jobs_tv:
                setResource(mSupervisorJobsLinearLayout, mSupervisorJobsImageView, R.drawable.ic_action_jobs_s, mSupervisorJobsTextView);
                commitFragment(new SupervisorJobsFragment());
                break;

            case R.id.supervisor_crews_ll:
            case R.id.supervisor_crews_iv:
            case R.id.supervisor_crews_tv:
                setResource(mSupervisorCrewsLinearLayout, mSupervisorCrewsImageView, R.drawable.ic_action_crews_s, mSupervisorCrewsTextView);
                commitFragment(new SupervisorCrewsFragment());
                break;
            case R.id.foreman_dailies_ll:
            case R.id.foreman_dailies_iv:
            case R.id.foreman_dailies_tv:
                if (isNewDaily) {
                    Utility.showAlertDialog(DashboardActivity.this, 0, R.string.your_daily_discard_alert_message, R.string.yes, R.string.no, new DialogActionCallback() {
                        @Override
                        public void doOnPositive() {
                            isNewDaily = false;
                            setResource(mForemanDailiesLinearLayout, mForemanDailiesImageView, R.drawable.ic_action_dailies_s, mForemanDailiesTextView);
                            commitFragment(new MyDailiesFragment());
                        }

                        @Override
                        public void doOnNegative() {

                        }
                    });
                } else {
                    setResource(mForemanDailiesLinearLayout, mForemanDailiesImageView, R.drawable.ic_action_dailies_s, mForemanDailiesTextView);
                    commitFragment(new MyDailiesFragment());
                }
                break;
            case R.id.foreman_jobs_ll:
            case R.id.foreman_jobs_iv:
            case R.id.foreman_jobs_tv:
                if (isNewDaily) {
                    Utility.showAlertDialog(DashboardActivity.this, 0, R.string.your_daily_discard_alert_message, R.string.yes, R.string.no, new DialogActionCallback() {
                        @Override
                        public void doOnPositive() {
                            isNewDaily = false;
                            setResource(mForemanJobsLinearLayout, mForemanJobsImageView, R.drawable.ic_action_jobs_s, mForemanJobsTextView);
                            commitFragment(new ForemanJobsFragment());
                        }

                        @Override
                        public void doOnNegative() {

                        }
                    });
                } else {
                    setResource(mForemanJobsLinearLayout, mForemanJobsImageView, R.drawable.ic_action_jobs_s, mForemanJobsTextView);
                    commitFragment(new ForemanJobsFragment());
                }
                break;
            case R.id.foreman_add_ll:
            case R.id.foreman_add_iv:
                isNewDaily = true;
                setResource(mForemanNewDailiesLinearLayout, mForemanNewDailiesImageView, R.drawable.ic_action_add, null);
                commitFragment(new NewDailyFragment());
                break;
            case R.id.foreman_incidents_ll:
            case R.id.foreman_incidents_iv:
            case R.id.foreman_incidents_tv:
                if (isNewDaily) {
                    Utility.showAlertDialog(DashboardActivity.this, 0, R.string.your_daily_discard_alert_message, R.string.yes, R.string.no, new DialogActionCallback() {
                        @Override
                        public void doOnPositive() {
                            isNewDaily = false;
                            setResource(mForemanIncidentsLinearLayout, mForemanIncidentsImageView, R.drawable.ic_action_incidents_s, mForemanIncidentsTextView);
                            commitFragment(new IncidentReportsFragment());
                        }

                        @Override
                        public void doOnNegative() {

                        }
                    });
                } else {
                    setResource(mForemanIncidentsLinearLayout, mForemanIncidentsImageView, R.drawable.ic_action_incidents_s, mForemanIncidentsTextView);
                    commitFragment(new IncidentReportsFragment());
                }
                break;
            case R.id.foreman_crews_ll:
            case R.id.foreman_crews_iv:
            case R.id.foreman_crews_tv:
                if (isNewDaily) {
                    Utility.showAlertDialog(DashboardActivity.this, 0, R.string.your_daily_discard_alert_message, R.string.yes, R.string.no, new DialogActionCallback() {
                        @Override
                        public void doOnPositive() {
                            isNewDaily = false;
                            setResource(mForemanCrewsLinearLayout, mForemanCrewsImageView, R.drawable.ic_action_crews_s, mForemanCrewsTextView);
                            commitFragment(new ForemanCrewsFragment());
                        }

                        @Override
                        public void doOnNegative() {

                        }
                    });
                } else {
                    setResource(mForemanCrewsLinearLayout, mForemanCrewsImageView, R.drawable.ic_action_crews_s, mForemanCrewsTextView);
                    commitFragment(new ForemanCrewsFragment());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isNewDaily) {
            Utility.showAlertDialog(DashboardActivity.this, 0, R.string.your_daily_discard_alert_message, R.string.yes, R.string.no, new DialogActionCallback() {
                @Override
                public void doOnPositive() {
                    isNewDaily = false;
                    setResource(mForemanDailiesLinearLayout, mForemanDailiesImageView, R.drawable.ic_action_dailies_s, mForemanDailiesTextView);
                    commitFragment(new MyDailiesFragment());

                }

                @Override
                public void doOnNegative() {

                }
            });
        } else {
            if (backPressed + 2000L > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.back_press_again), Toast.LENGTH_SHORT).show();
            }
            backPressed = System.currentTimeMillis();
        }
    }


    

    @Override
    public void onMyDailyRedirection() {
        setResource(mForemanDailiesLinearLayout, mForemanDailiesImageView, R.drawable.ic_action_dailies_s, mForemanDailiesTextView);
        commitFragment(new MyDailiesFragment());
        isNewDaily = false;
    }
}
