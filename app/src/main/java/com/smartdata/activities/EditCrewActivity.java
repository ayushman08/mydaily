package com.smartdata.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.smartdata.config.PreferenceConfiguration;
import com.smartdata.dataobject.AppInstance;
import com.smartdata.dataobject.CrewImageData;
import com.smartdata.dataobject.CrewsData;
import com.smartdata.dataobject.CrewsDataList;
import com.smartdata.dialogs.AlertDialogManager;
import com.smartdata.interfaces.ServiceRedirection;
import com.smartdata.managers.SupervisorManager;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.Utility;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * Created by Anurag Sethi
 * The activity is used for handling the login screen actions
 */
public class EditCrewActivity extends AppActivity implements ServiceRedirection {

    private ActionBar mActionBar;
    private Utility mUtility;
    private AlertDialogManager mAleaAlertDialogManager;
    private SupervisorManager mSupervisorManager;
    private CrewsDataList mCrewsDataList;
    private CrewImageData mCrewImageData;
    private AppCompatButton mUpdateAppCompatButton;
    private Uri mImageUri;
    private CircleImageView mProfileCircularImageView;
    private String path, filePath;
    private FloatingActionButton mSelectImageView;
    private int mReadPermissionCheck, mWritePermissionCheck, mCameraPermissonCheck;
    private EditText mFirstNameEditText, mLastNameEditText, mEmailEditText;
    private ProgressBar mProgressBar;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private File mFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_crews);
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
        mCrewsDataList = getIntent().getParcelableExtra(Constants.RequestCodes.REQ_CODE_CREW_DATA);
        mCrewImageData = getIntent().getParcelableExtra(Constants.RequestCodes.REQ_CODE_CREW_IMAGE_DATA);

        mUtility = new Utility(EditCrewActivity.this);
        mSupervisorManager = new SupervisorManager(EditCrewActivity.this, this);
        if (getIntent().getExtras().getString(Constants.RequestCodes.REQ_CODE_CREW_DETAILS).equals(Constants.RequestCodes.REQ_CODE_DETAIL_CREW)) {
            mUtility.customActionBar(EditCrewActivity.this, true, R.string.crews);
        } else {
            if (mCrewsDataList != null) {
                mUtility.customActionBar(EditCrewActivity.this, true, R.string.edit_crews);
            } else {
                mUtility.customActionBar(EditCrewActivity.this, true, R.string.add_crews);
            }
        }
        //mUtility.setToolbar(EditCrewActivity.this, getResources().getString(R.string.edit_crews), getResources().getString(R.string.submittals_search_hint), true, false);
        mAleaAlertDialogManager = new AlertDialogManager();

        mWritePermissionCheck = ContextCompat.checkSelfPermission(EditCrewActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        mReadPermissionCheck = ContextCompat.checkSelfPermission(EditCrewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        mCameraPermissonCheck = ContextCompat.checkSelfPermission(EditCrewActivity.this, Manifest.permission.CAMERA);

        this.mImageLoader = ImageLoader.getInstance();
        this.mImageLoader.init(ImageLoaderConfiguration.createDefault(EditCrewActivity.this));
        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.user)
                .showImageForEmptyUri(R.drawable.user)
                .showImageOnFail(R.drawable.user)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

    }

    /**
     * Binds the UI controls
     *
     * @return none
     */
    @Override
    public void bindControls() {
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProfileCircularImageView = (CircleImageView) findViewById(R.id.circularImageView);
        mFirstNameEditText = (EditText) findViewById(R.id.firstName_et);
        mLastNameEditText = (EditText) findViewById(R.id.lastName_et);
        mEmailEditText = (EditText) findViewById(R.id.email);
        mSelectImageView = (FloatingActionButton) findViewById(R.id.select_profile_iv);
        mUpdateAppCompatButton = (AppCompatButton) findViewById(R.id.updateButton);
        if (getIntent().getExtras().getString(Constants.RequestCodes.REQ_CODE_CREW_DETAILS).equals(Constants.RequestCodes.REQ_CODE_DETAIL_CREW)) {
            mUpdateAppCompatButton.setVisibility(View.GONE);
            mSelectImageView.setVisibility(View.GONE);
            mFirstNameEditText.setFocusable(false);
            mFirstNameEditText.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
            mFirstNameEditText.setClickable(false);

            mLastNameEditText.setFocusable(false);
            mLastNameEditText.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
            mLastNameEditText.setClickable(false);

            mEmailEditText.setFocusable(false);
            mEmailEditText.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
            mEmailEditText.setClickable(false);
        } else {
            mSelectImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mReadPermissionCheck != PackageManager.PERMISSION_GRANTED && mWritePermissionCheck != PackageManager.PERMISSION_GRANTED && mCameraPermissonCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(EditCrewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, Constants.RequestCodes.REQ_CODE_READ_WRITE_FILE);
                    } else {
                        setProfilePicDialog();
                    }
                }
            });
            if (mCrewsDataList != null) {
                mUpdateAppCompatButton.setText(getResources().getString(R.string.update));
                mUpdateAppCompatButton.setBackgroundDrawable(ContextCompat.getDrawable(EditCrewActivity.this, R.drawable.button_active));
            } else {
                mUpdateAppCompatButton.setText(getResources().getString(R.string.add_crew_member));
            }

            mLastNameEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mFirstNameEditText.getText().toString().length() > 0 && count > 0) {
                        mUpdateAppCompatButton.setBackgroundDrawable(ContextCompat.getDrawable(EditCrewActivity.this, R.drawable.button_active));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            mUpdateAppCompatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addCrew();
                }
            });
        }

        setInformation(mCrewsDataList, mCrewImageData);


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
            if (taskID == Constants.TaskID.ADD_CREW) {
                if (mFile != null) {
                    uploadCrewImage(mFile, AppInstance.myDailyResponse.getMyDailyResponseData().getCrew_id());

                } else {
                    this.finish();
                }
            } else if (taskID == Constants.TaskID.UPLOAD_CREW_IMAGE) {
                this.finish();
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


    public void setProfilePicDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(EditCrewActivity.this)
                .setTitle(R.string.selectProfilePicTitle)
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK && data != null) {
            return;
        }

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
                                mProfileCircularImageView.setImageBitmap(bm);
                                mFile = new File(resultUri.getPath());
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


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case Constants.RequestCodes.REQ_CODE_READ_WRITE_FILE:
                setProfilePicDialog();
                break;

            default:
                break;
        }
    }

    public void setInformation(CrewsDataList crewsDataList, CrewImageData crewImageData) {
        try {
            if (crewsDataList != null) {
                mFirstNameEditText.setText(crewsDataList.getFirstname());
                mLastNameEditText.setText(crewsDataList.getLastname());
                mEmailEditText.setText(crewsDataList.getEmail());
                if (getIntent().getExtras().getString(Constants.RequestCodes.REQ_CODE_CREW_DETAILS).equals(Constants.RequestCodes.REQ_CODE_DETAIL_CREW)) {
                    mFirstNameEditText.setFocusable(false);
                    mFirstNameEditText.setFocusableInTouchMode(false);
                    mFirstNameEditText.setClickable(false);
                    mFirstNameEditText.setLongClickable(false);

                    mLastNameEditText.setFocusable(false);
                    mLastNameEditText.setFocusableInTouchMode(false);
                    mLastNameEditText.setClickable(false);
                    mLastNameEditText.setLongClickable(false);

                    mEmailEditText.setFocusable(false);
                    mEmailEditText.setFocusableInTouchMode(false);
                    mEmailEditText.setClickable(false);
                    mEmailEditText.setLongClickable(false);
                }

                if (crewImageData != null) {
                    mImageLoader.displayImage(Constants.WebServices.BASE_URL + crewImageData.getImage_path(), mProfileCircularImageView, mOptions, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            mProgressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            mProgressBar.setVisibility(View.GONE);
                            mProfileCircularImageView.setImageResource(R.drawable.user);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            mProgressBar.setVisibility(View.GONE);
                        }
                    });
                }
            } else {
                mProfileCircularImageView.setImageResource(R.drawable.user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addCrew() {

        if (mFirstNameEditText.getText().toString().isEmpty()) {
            mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.firstname_validation_message), false);
        } else if (mLastNameEditText.getText().toString().isEmpty()) {
            mUtility.showSnackBarAlert(findViewById(R.id.container), getResources().getString(R.string.lastname_validation_message), false);
        } else {
            mUtility.startLoader(EditCrewActivity.this, R.drawable.image_for_rotation);
            CrewsData crewsData = new CrewsData();
            crewsData.firstname = mFirstNameEditText.getText().toString();
            crewsData.lastname = mLastNameEditText.getText().toString();
            if (mUtility.checkEmail(mEmailEditText.getText().toString())) {
                crewsData.email = mEmailEditText.getText().toString();
            }

            if (mCrewsDataList != null) {
                crewsData.id = mCrewsDataList.getId();
            }
            crewsData.parent_id = PreferenceConfiguration.getUserID(EditCrewActivity.this) + "";
            mSupervisorManager.addCrew(crewsData);
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    public void uploadCrewImage(File file, String id) {
        try {
            if (file != null) {
                mUtility.startLoader(EditCrewActivity.this, R.drawable.image_for_rotation);
                // create RequestBody instance from file
                RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                // MultipartBody.Part is used to send also the actual file name
                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file", file.getName(), fbody);

                RequestBody requestid = RequestBody.create(MediaType.parse("multipart/form-data"), id);

                mSupervisorManager.uploadCrewImage(body, requestid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*  private void cameraIntent() {
          Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
          File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
          intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
          startActivityForResult(intent, Constants.RequestCodes.REQ_CODE_CAMERA);
      }
  */
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imageFile = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
        //Uri photoURI = Uri.fromFile(createImageFile());
        mImageUri = FileProvider.getUriForFile(EditCrewActivity.this, getApplicationContext().getPackageName() + ".provider", imageFile);

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
                .start(EditCrewActivity.this);

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
//.setAspectRatio(1, 1)
        CropImage.activity(mImageUri)
                .start(EditCrewActivity.this);
    }
}
