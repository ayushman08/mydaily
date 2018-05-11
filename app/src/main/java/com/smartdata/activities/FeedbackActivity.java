package com.smartdata.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.smartdata.config.PreferenceConfiguration;
import com.smartdata.dataobject.AppInstance;
import com.smartdata.dataobject.FeedbackData;
import com.smartdata.dataobject.ForemanData;
import com.smartdata.dataobject.User;
import com.smartdata.dialogs.AlertDialogManager;
import com.smartdata.interfaces.DialogActionCallback;
import com.smartdata.interfaces.ServiceRedirection;
import com.smartdata.managers.LoginManager;
import com.smartdata.managers.SupervisorManager;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.Utility;


/**
 * Created by Anurag Sethi
 * The activity is used for handling the login screen actions
 */
public class FeedbackActivity extends AppActivity implements ServiceRedirection {

    private ActionBar mActionBar;
    private Utility mUtility;
    private AppCompatButton mSendButton;
    private EditText mDescriptionEditText;
    private TextView mFirstNameTextView, mLastNameTextView, mEmailTextView;
    private User mUser;
    private AlertDialogManager mAleaAlertDialogManager;
    private SupervisorManager mSupervisorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
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
        mUtility = new Utility(FeedbackActivity.this);
        mUtility.customActionBar(FeedbackActivity.this, true, R.string.feedback);
        mUser = new User();
        mSupervisorManager = new SupervisorManager(FeedbackActivity.this, this);
        mAleaAlertDialogManager = new AlertDialogManager();

    }

    /**
     * Binds the UI controls
     *
     * @return none
     */
    @Override
    public void bindControls() {
        mFirstNameTextView = (TextView) findViewById(R.id.firstName_tv);
        mLastNameTextView = (TextView) findViewById(R.id.lastName_tv);
        mEmailTextView = (TextView) findViewById(R.id.email_tv);

        mFirstNameTextView.setText(PreferenceConfiguration.getUserFirstName(FeedbackActivity.this));
        mLastNameTextView.setText(PreferenceConfiguration.getUserLastName(FeedbackActivity.this));
        mEmailTextView.setText(PreferenceConfiguration.getUserEmail(FeedbackActivity.this));

        mDescriptionEditText = (EditText) findViewById(R.id.feedback_description_et);
        mSendButton = (AppCompatButton) findViewById(R.id.sendButton);

        mDescriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 10) {
                    mSendButton.setBackgroundDrawable(ContextCompat.getDrawable(FeedbackActivity.this, R.drawable.button_active));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 10) {
                    mSendButton.setBackgroundDrawable(ContextCompat.getDrawable(FeedbackActivity.this, R.drawable.button_disable));
                }
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkAvailable(FeedbackActivity.this)) {
                    // call API
                    if (mDescriptionEditText.length() > 10) {
                        sendFeedback();
                    } else {
                        mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.description_length_validation), false);
                    }
                } else {
                    mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.no_network), true);
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
            if (taskID == Constants.TaskID.FEEDBACK) {
                Utility.showAlertDialog(FeedbackActivity.this, 0, R.string.your_feedback_has_been_submitted, R.string.Ok, 0, new DialogActionCallback() {
                    @Override
                    public void doOnPositive() {
                        finish();
                    }

                    @Override
                    public void doOnNegative() {

                    }
                });
            } else {
                mUtility.showSnackBarAlert(findViewById(R.id.container), mUser.getMessage(), false);
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

    private void sendFeedback() {

        if (mDescriptionEditText.getText().toString().length() > 0) {
            FeedbackData feedbackData = new FeedbackData();
            mUtility.startLoader(FeedbackActivity.this, R.drawable.image_for_rotation);
            feedbackData.email = PreferenceConfiguration.getUserEmail(FeedbackActivity.this);
            feedbackData.firstname = PreferenceConfiguration.getUserFirstName(FeedbackActivity.this);
            feedbackData.lastname = PreferenceConfiguration.getUserLastName(FeedbackActivity.this);
            feedbackData.message = mDescriptionEditText.getText().toString();
            mSupervisorManager.sendFeedbackData(feedbackData);

        } else {
            mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.description_validation), false);
        }
    }

}
