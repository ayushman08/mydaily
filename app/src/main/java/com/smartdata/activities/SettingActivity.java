package com.smartdata.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartdata.config.PreferenceConfiguration;
import com.smartdata.dataobject.User;
import com.smartdata.interfaces.DialogActionCallback;
import com.smartdata.interfaces.ServiceRedirection;
import com.smartdata.managers.LoginManager;
import com.smartdata.managers.SupervisorManager;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.Utility;

import java.util.Calendar;


/**
 * Created by Anurag Sethi
 * The activity is used for handling the login screen actions
 */
public class SettingActivity extends AppActivity implements ServiceRedirection {

    private static final String TAG = SettingActivity.class.getSimpleName();
    private Utility mUtility;
    LinearLayout mNotificationLinearLayout, mLocationLinearLayout, mReminderLinearLayout;
    TextView mDailiesTextView, mDailiesSubmittedTextView, mLogoutTextView,
            mShareTextView, mTermsTextView, mPrivacyTextView, mFeedbackTextView;

    AppCompatCheckBox mNotificationAppCompatCheckBox, mReminderAppCompatCheckBox, mLocationAppCompatCheckBox;
    private LoginManager mLoginManager;
    private User mUser;
    private SupervisorManager mSupervisorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);
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
        mUtility = new Utility(SettingActivity.this);
        mUtility.customActionBar(SettingActivity.this, true, R.string.action_settings);
        mSupervisorManager = new SupervisorManager(SettingActivity.this, this);
        mLoginManager = new LoginManager(SettingActivity.this, this);
    }

    /**
     * Binds the UI controls
     *
     * @return none
     */
    @Override
    public void bindControls() {
        mNotificationAppCompatCheckBox = (AppCompatCheckBox) findViewById(R.id.notification_cb);
        mNotificationAppCompatCheckBox.setChecked(true);

        mReminderAppCompatCheckBox = (AppCompatCheckBox) findViewById(R.id.reminder_cb);

        mLocationAppCompatCheckBox = (AppCompatCheckBox) findViewById(R.id.location_cb);
        mLocationAppCompatCheckBox.setChecked(true);

        mShareTextView = (TextView) findViewById(R.id.shareTextView);
        mTermsTextView = (TextView) findViewById(R.id.termsTextView);
        mPrivacyTextView = (TextView) findViewById(R.id.privacyTextView);
        mFeedbackTextView = (TextView) findViewById(R.id.feedbackTextView);

        mNotificationLinearLayout = (LinearLayout) findViewById(R.id.notification_ll);
        mLocationLinearLayout = (LinearLayout) findViewById(R.id.location_ll);
        mReminderLinearLayout = (LinearLayout) findViewById(R.id.reminder_ll);

        mDailiesTextView = (TextView) findViewById(R.id.dailies_tv);
        mDailiesSubmittedTextView = (TextView) findViewById(R.id.dailes_submitted_tv);
        // mDailiesTextView.setVisibility(View.GONE);
        // mDailiesSubmittedTextView.setVisibility(View.GONE);

        if (PreferenceConfiguration.getUserTypeId(SettingActivity.this) == 1) {
            mLocationLinearLayout.setVisibility(View.GONE);
            mReminderLinearLayout.setVisibility(View.GONE);
            mDailiesTextView.setVisibility(View.GONE);
        } else {
            mDailiesSubmittedTextView.setVisibility(View.GONE);
        }

        mDailiesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadDailies();
            }
        });

        mDailiesSubmittedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadDailies();
            }
        });


        mLogoutTextView = (TextView) findViewById(R.id.logout_tv);
        mLogoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //logout confirmation dialog
                /*ConfirmationDialog.getConfirmDialog(SettingActivity.this, getResources().getString(R.string.app_name),
                        getResources().getString(R.string.sign_out_confirmation_message), getResources().getString(R.string.logout),
                        getResources().getString(R.string.cancel), false, new ConfirmationInterfaces() {
                            @Override
                            public void PositiveMethod(DialogInterface dialog, int id) {
                                //Clearing the user logged in session
                                dialog.dismiss();
                                callLogOutWebService();
                            }

                            @Override
                            public void NegativeMethod(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });*/

                Utility.showAlertDialog(SettingActivity.this, 0, R.string.sign_out_confirmation_message, R.string.logout, R.string.cancel, new DialogActionCallback() {
                    @Override
                    public void doOnPositive() {
                        callLogOutWebService();
                    }

                    @Override
                    public void doOnNegative() {

                    }
                });

            }
        });

        mTermsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentObj = new Intent(SettingActivity.this, WebViewActivity.class);
                //intentObj.putExtra(Constants.InputTag.WEB_INTENT_KEY, Constants.InputTag.WEB_TERMS);
                intentObj.putExtra(Constants.InputTag.WEB_URL_INTENT_KEY, Constants.WebServices.BASE_URL + Constants.WebServices.TERMS_URL);
                startActivity(intentObj);
            }
        });

        mPrivacyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentObj = new Intent(SettingActivity.this, WebViewActivity.class);
                // intentObj.putExtra(Constants.InputTag.WEB_INTENT_KEY, Constants.InputTag.WEB_PRIVACY);
                intentObj.putExtra(Constants.InputTag.WEB_URL_INTENT_KEY, Constants.WebServices.BASE_URL + Constants.WebServices.PRIVACY_URL);
                startActivity(intentObj);
            }
        });

        mShareTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Here is the share content body";
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        mReminderAppCompatCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    addReminder();
                }
            }
        });

        mFeedbackTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentObj = new Intent(SettingActivity.this, FeedbackActivity.class);
                startActivity(intentObj);
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
        mUtility.stopLoader();
        if (taskID == Constants.TaskID.USER_LOGOUT) {
            onUserLogout();
        } else if (taskID == Constants.TaskID.DOWNLOAD_DAILIES) {
            mUtility.showSnackBarAlert(findViewById(R.id.container), "Sent you an email attached with dailies!!", false);
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
        mUtility.stopLoader();
        mUtility.showSnackBarAlert(findViewById(R.id.container), errorMessage, false);
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


    private void onUserLogout() {

        PreferenceConfiguration.setUserID("", getApplicationContext());
        PreferenceConfiguration.setUserTypeId(0, getApplicationContext());
        PreferenceConfiguration.setAuthToken("", getApplicationContext());
        PreferenceConfiguration.setPushToken("", getApplicationContext());
        PreferenceConfiguration.setParentID("", getApplicationContext());
        PreferenceConfiguration.setLocationPreference(false, SettingActivity.this);

        PreferenceConfiguration.setUserFirstName("", getApplicationContext());
        PreferenceConfiguration.setUserLastName("", getApplicationContext());
        PreferenceConfiguration.setUserEmail("", getApplicationContext());

        Intent loginActivityIntent = new Intent(SettingActivity.this,
                LoginActivity.class);
        loginActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(loginActivityIntent);
        finish();
    }

    private void callLogOutWebService() {
        mUser = new User();
        mUtility.startLoader(SettingActivity.this, R.drawable.image_for_rotation);
        mUser.userId = PreferenceConfiguration.getUserID(SettingActivity.this);
        mUser.deviceToken = "data";

        Log.e(TAG, "Passing Data: " + mUser.toString());
        Log.e(TAG, "++++++++++++++++");
        if (mUser.deviceToken == null || mUser.deviceToken.trim().isEmpty()) {
            mUser.deviceToken = "DUMMY";
        }
        mLoginManager.userLogout(mUser);
    }


    public void addReminder() {
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", cal.getTimeInMillis());
        intent.putExtra("allDay", false);
        intent.putExtra("rrule", "FREQ=DAILY");
        intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
        intent.putExtra("title", "A Reminder from MyDaily android app");
        startActivity(intent);
    }

    private void downloadDailies() {
        mUser = new User();
        mUtility.startLoader(SettingActivity.this, R.drawable.image_for_rotation);
        mUser.userId = PreferenceConfiguration.getUserID(SettingActivity.this);
        mUser.supervisor_id = PreferenceConfiguration.getUserID(SettingActivity.this);
        mUser.firstname = PreferenceConfiguration.getUserFirstName(SettingActivity.this);
        mUser.lastname = PreferenceConfiguration.getUserLastName(SettingActivity.this);
        mUser.email = PreferenceConfiguration.getUserEmail(SettingActivity.this);
        mSupervisorManager.downloadDailies(mUser);
    }
}
