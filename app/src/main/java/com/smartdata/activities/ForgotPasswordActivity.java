package com.smartdata.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.smartdata.dataobject.AppInstance;
import com.smartdata.dataobject.User;
import com.smartdata.dialogs.AlertDialogManager;
import com.smartdata.interfaces.DialogActionCallback;
import com.smartdata.interfaces.ServiceRedirection;
import com.smartdata.managers.LoginManager;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.Utility;


/**
 * Created by Anurag Sethi
 * The activity is used for handling the login screen actions
 */
public class ForgotPasswordActivity extends AppActivity implements ServiceRedirection {

    private ActionBar mActionBar;
    private Utility mUtility;
    private AppCompatButton mSendButton;
    private EditText mEmailEditText;
    private LoginManager mLoginManager;
    private User mUser;
    private AlertDialogManager mAleaAlertDialogManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
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
        mUtility = new Utility(ForgotPasswordActivity.this);
        mUtility.customActionBar(ForgotPasswordActivity.this, true, R.string.forgot_password_label);
        mUser = new User();
        mLoginManager = new LoginManager(ForgotPasswordActivity.this, this);
        mAleaAlertDialogManager = new AlertDialogManager();
    }

    /**
     * Binds the UI controls
     *
     * @return none
     */
    @Override
    public void bindControls() {
        mEmailEditText = (EditText) findViewById(R.id.email);
        mSendButton = (AppCompatButton) findViewById(R.id.sendButton);

        mEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mSendButton.setBackgroundDrawable(ContextCompat.getDrawable(ForgotPasswordActivity.this, R.drawable.button_disable));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mUtility.checkEmail(mEmailEditText.getText().toString()) && count > 0) {
                    mSendButton.setBackgroundDrawable(ContextCompat.getDrawable(ForgotPasswordActivity.this, R.drawable.button_active));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendPasswordRequest();
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

        try {
            if (taskID == Constants.TaskID.FORGOT_PASSWORD_TASK_ID) {
                mUtility.stopLoader();
                mUser = AppInstance.userObj;


                if (mUser.getCode() == Constants.ResponseCodes.RESPONSE_SUCCESS) {
                    Utility.showAlertDialog(ForgotPasswordActivity.this, R.string.forgot_password_title, R.string.forgot_password_message, R.string.Ok, 0, new DialogActionCallback() {
                        @Override
                        public void doOnPositive() {

                            mEmailEditText.setText("");
                            //call the intent for the next activity
                            Intent intentObj = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                            startActivity(intentObj);
                            finish();
                        }

                        @Override
                        public void doOnNegative() {

                        }
                    });
                } else {
                    //   mUtility.showAlert(ForgotPasswordActivity.this, getResources().getString(R.string.forgot_password_label),
                    //     mUser.getMessage());
                    //Snackbar.make(findViewById(R.id.container), mUser.getMessage(), Snackbar.LENGTH_SHORT).show();
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
            // Snackbar.make(findViewById(R.id.container), errorMessage, Snackbar.LENGTH_SHORT).show();
            mUtility.showSnackBarAlert(findViewById(R.id.container), errorMessage, false);
       /* mUtility.showAlertDialog(ForgotPasswordActivity.this, R.string.forgot_password_title, R.string.forgot_password_message, R.string.Ok, 0, new DialogActionCallback() {
            @Override
            public void doOnPositive() {

            }

            @Override
            public void doOnNegative() {

            }
        });*/
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

    private void sendPasswordRequest() {
        if (mUtility.checkEmail(mEmailEditText.getText().toString())) {

            mUtility.startLoader(ForgotPasswordActivity.this, R.drawable.image_for_rotation);
            mUser.setEmail(mEmailEditText.getText().toString());
            mLoginManager.forgotPassword(mUser);

        } else {
            // Snackbar.make(findViewById(R.id.container), "Please enter valid email ID", Snackbar.LENGTH_SHORT).show();
            mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.email_validation_message), false);
                   /* View view = snack.getView();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    view.setLayoutParams(params);
                    snack.show();*/
        }
    }

}
