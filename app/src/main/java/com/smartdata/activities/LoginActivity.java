package com.smartdata.activities;

import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartdata.config.PreferenceConfiguration;
import com.smartdata.dataobject.AppInstance;
import com.smartdata.dataobject.User;
import com.smartdata.interfaces.ServiceRedirection;

import com.smartdata.managers.LoginManager;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.Utility;


/**
 * Created by Anurag Sethi
 * The activity is used for handling the login screen actions
 */
public class LoginActivity extends AppActivity implements ServiceRedirection, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private ActionBar mActionBar;
    private AppCompatButton mLoginButton;
    private TextView mForgotPasswordTextView;
    private Utility mUtility;
    private EditText mEmailEditText, mPasswordEditText;
    private LinearLayout llContainer;
    private AppCompatCheckBox mAppCompatCheckBox;
    private LoginManager mLoginManager;
    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
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
        // mActionBar = getSupportActionBar();
        // mActionBar.setDisplayHomeAsUpEnabled(true);
        mUser = new User();
        mUtility = new Utility(LoginActivity.this);
        mUtility.customActionBar(LoginActivity.this, true, R.string.login);
        mLoginManager = new LoginManager(LoginActivity.this, this);
    }

    /**
     * Binds the UI controls
     *
     * @return none
     */
    @Override
    public void bindControls() {
        mEmailEditText = (EditText) findViewById(R.id.email);
        mPasswordEditText = (EditText) findViewById(R.id.password);
        mPasswordEditText.setTypeface(mEmailEditText.getTypeface());
        llContainer = (LinearLayout) findViewById(R.id.llContainer);
        mLoginButton = (AppCompatButton) findViewById(R.id.signInButton);
        mForgotPasswordTextView = (TextView) findViewById(R.id.tv_forgotPassword);

        mAppCompatCheckBox = (AppCompatCheckBox) findViewById(R.id.remember_checkbox);
        PreferenceConfiguration.setRememberUser(true, LoginActivity.this);
        mAppCompatCheckBox.setOnCheckedChangeListener(this);

        mPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mUtility.checkEmail(mEmailEditText.getText().toString()) && s.length() > 0) {
                    mLoginButton.setBackgroundDrawable(ContextCompat.getDrawable(LoginActivity.this, R.drawable.button_active));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mUtility.checkEmail(mEmailEditText.getText().toString()) || s.length() < 1) {
                    mLoginButton.setBackgroundDrawable(ContextCompat.getDrawable(LoginActivity.this, R.drawable.button_disable));
                }
            }
        });

        mForgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentObj = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intentObj);
                LoginActivity.this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });


        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });

        llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUtility.hiddenInputMethod(LoginActivity.this);
            }
        });
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.remember_checkbox) {
            PreferenceConfiguration.setRememberUser(isChecked, LoginActivity.this);
        }
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
            if (taskID == Constants.TaskID.LOGIN_TASK_ID) {
                mUtility.stopLoader();
                mUser = AppInstance.userObj;
                if (mUser.getCode() == Constants.ResponseCodes.RESPONSE_SUCCESS) {
                    if (mUser.getDataReturned().getRole_code().equals(Constants.InputTag.ROLE_CODE_FM)) {
                        PreferenceConfiguration.setUserTypeId(2, LoginActivity.this);
                    } else if (mUser.getDataReturned().getRole_code().equals(Constants.InputTag.ROLE_CODE_SV)) {
                        PreferenceConfiguration.setUserTypeId(1, LoginActivity.this);
                    }
                    PreferenceConfiguration.setUserID(mUser.getDataReturned().getId(), LoginActivity.this);
                    PreferenceConfiguration.setAuthToken(mUser.getDataReturned().getToken(), LoginActivity.this);
                    PreferenceConfiguration.setParentID(mUser.getDataReturned().getParent_id(), LoginActivity.this);
                    PreferenceConfiguration.setUserFirstName(mUser.getDataReturned().getFirstname(), LoginActivity.this);
                    PreferenceConfiguration.setUserLastName(mUser.getDataReturned().getLastname(), LoginActivity.this);
                    PreferenceConfiguration.setUserEmail(mUser.getDataReturned().getEmail(), LoginActivity.this);
                    //call the intent for the next activity
                    Intent intentObj = new Intent(LoginActivity.this, DashboardActivity.class);
                    intentObj.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentObj);
                    LoginActivity.this.finish();
                } else {
                    mUtility.showSnackBarAlert(findViewById(R.id.container), mUser.getMessage(), false);
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


    private void doLogin() {
        Intent intentObj = new Intent(LoginActivity.this, DashboardActivity.class);
        intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);

        if (!mUtility.checkEmail(mEmailEditText.getText().toString())) {
            mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.email_validation_message), false);
        } else if (mPasswordEditText.getText().toString().isEmpty()) {
            mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.paasword_validation_message), false);
        } else {
            mUser.email = mEmailEditText.getText().toString();
            mUser.password = mPasswordEditText.getText().toString();
            mUser.deviceType = Constants.InputTag.DEVICE_TYPE;
            mUser.deviceId = Utility.getDeviceId(this);
            mUser.deviceToken = PreferenceConfiguration.getPushToken(LoginActivity.this);
            // Log.e(TAG, "Device ID: " + mUser.deviceId);
            // Log.e(TAG, "FCM Token: " + mUser.deviceToken);

            if (mUser.deviceToken == null || mUser.deviceToken.trim().isEmpty()) {
                mUser.deviceToken = "DUMMY";
            }

            if (isNetworkAvailable(LoginActivity.this)) {
                mUtility.startLoader(LoginActivity.this, R.drawable.image_for_rotation);
                mLoginManager.authenticateLogin(mUser);
            } else {
                mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.no_network), true);
            }
        }

    }
}
