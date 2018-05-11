package com.smartdata.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Context;
import android.content.res.Resources;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.google.gson.Gson;
import com.smartdata.activities.SettingActivity;
import com.smartdata.dataobject.AppInstance;
import com.smartdata.helpers.TransparentProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.view.inputmethod.InputMethodManager;

import java.text.DecimalFormat;

import com.smartdata.interfaces.DialogActionCallback;
import com.smartdata.mydaily.R;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


/**
 * Created by Anurag Sethi
 * This class is used to define the common functions to be used in the application
 */
public class Utility {

    Context context;
    TransparentProgressDialog progressDialogObj;
    public TextView saveTextView;

    /**
     * Constructor
     *
     * @param contextObj The Context from where the method is called
     * @return none
     */
    public Utility(Context contextObj) {
        context = contextObj;
    }

    /**
     * The method validates email address
     *
     * @param email email address to validate
     * @return true if the email entered is valid
     */
    public boolean checkEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * This method will convert object to Json String
     *
     * @param obj Object whose data needs to be converted into JSON String
     * @return object data in JSONString
     */
    public String convertObjectToJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    /**
     * The method will start the loader till the AsynTask completes the assigned
     * task
     *
     * @param context                        The Context from where the method is called
     * @param image_for_rotation_resource_id resourceID of the image to be
     *                                       displayed as loader
     * @return none
     * @since 2014-08-20
     */
    public void startLoader(Context context, int image_for_rotation_resource_id) {
        progressDialogObj = new TransparentProgressDialog(this.context, image_for_rotation_resource_id);
        //AppInstance.logObj.printLog("startLoader=" + progressDialogObj);
        if(progressDialogObj!=null)
            progressDialogObj.show();
    }

    /**
     * The method will start the loader till the AsynTask completes the assigned
     * task
     *
     * @return none
     * @since 2014-08-20
     */
    public void stopLoader() {
        if (progressDialogObj != null) {
            if (progressDialogObj.isShowing()) {
                progressDialogObj.dismiss();
            }
        }
    }


    /**
     * Show a non-cancelable dialog box with a message, a positive and a
     * negative button.
     *
     * @param context               The context to show this dialog box. Can't be null.
     * @param titleStringId         A valid resource id of the text to be shown in the title bar
     *                              of dialog box. If you don't want to show a title, just pass -1
     *                              here.
     * @param messageStringId       A valid resource id of the message to display.
     * @param positiveButtonLabelId A valid resource id of the text to show on positive button.If you don't want to show a positiveButtonLabelId, just pass 0
     *                              here.
     * @param negativeButtonLabelId A valid resource id of the string to show on negative button.If you don't want to show a negativeButtonLabelId, just pass 0
     *                              here.
     * @param actionCallback        Callback interface for the positive and negative buttons for
     *                              if you want to perform some action on button clicks. Can be
     *                              null.
     * @throws Resources.NotFoundException if any of the resource not found.
     */
    public static void showAlertDialog(Context context, int titleStringId, int messageStringId, int positiveButtonLabelId,
                                       int negativeButtonLabelId, final DialogActionCallback actionCallback) throws Resources.NotFoundException {


        if ((context == null) || (context.getString(messageStringId) == null || context.getString(messageStringId).trim().isEmpty())) {

            return;
        }


        String title = null;
        String message = context.getString(messageStringId);
        String positiveButtonLabel = null;
        String negativeButtonLabel = null;
        if (titleStringId > 0) {
            title = context.getString(titleStringId);
        }

        if (positiveButtonLabelId > 0) {
            positiveButtonLabel = context.getString(positiveButtonLabelId);
        }
        if (negativeButtonLabelId > 0) {
            negativeButtonLabel = context.getString(negativeButtonLabelId);
        }


        if ((context == null) || (message == null || message.trim().isEmpty())) {
            return;
        }

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        if (title != null && !title.trim().isEmpty()) {
            alertDialog.setTitle(title);
        }

        alertDialog.setCancelable(false);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton(positiveButtonLabel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

                if (actionCallback != null) {
                    actionCallback.doOnPositive();
                }
            }

        });

        alertDialog.setNegativeButton(negativeButtonLabel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (actionCallback != null) {
                    actionCallback.doOnNegative();
                }
            }
        });

        alertDialog.show();

    }


    /**
     * The method will return the date and time in requested format
     *
     * @param selectedDateTime to be converted to requested format
     * @param requestedFormat  the format in which the provided datetime needs to
     *                         be changed
     * @param formatString     differentiate parameter to format date or time
     * @return formated date or time
     */
    public String formatDateTime(String selectedDateTime, String requestedFormat, String formatString) {

        if (formatString.equalsIgnoreCase("time")) {
            SimpleDateFormat ft = new SimpleDateFormat("hh:mm");
            Date dateObj = null;

            try {
                dateObj = ft.parse(selectedDateTime);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            long millis = dateObj.getTime();
            SimpleDateFormat simpleDateFormatObj = new SimpleDateFormat(requestedFormat);
            return simpleDateFormatObj.format(millis);

        }//if
        else if (formatString.equalsIgnoreCase("date")) {
            // SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US);
            ft.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date dateObj = null;

            try {
                dateObj = ft.parse(selectedDateTime);

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            SimpleDateFormat simpleDateFormatObj = new SimpleDateFormat(requestedFormat);
            return simpleDateFormatObj.format(dateObj);

        }
        return null;

    }

    /**
     * The method will return current time
     *
     * @return current time
     */
    public String getCurrentTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss a");
        String currentTime = sdf.format(c.getTime());

        return currentTime;
    }

    /**
     * The method will return current date
     *
     * @return current date
     */
    public String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);
        String currentDate = Integer.toString(day) + "-" + Integer.toString(month) + "-" + Integer.toString(year);
        return currentDate;
    }

    /**
     * The method show the error
     *
     * @param contextObj  context of the activity from where the method is called
     * @param message     message to be displayed
     * @param textViewObj object of the textView where the error message will be
     *                    shown
     * @param editTextObj object of the editText containing the error
     */
    public void showError(Context contextObj, String message, TextView textViewObj, EditText editTextObj) {
        if (message == null || message.equalsIgnoreCase("")) {
            textViewObj.setVisibility(View.GONE);
        } else {
            textViewObj.setVisibility(View.VISIBLE);
            textViewObj.setText(message);
            textViewObj.setTextColor(contextObj.getResources().getColor(R.color.error_text_color));
        }

    }

    /**
     * This method will convert json String to ArrayList
     *
     * @param jsonString string which needs to be converted to ArrayList
     * @return ArrayList of type String
     * @throws JSONException
     * @since 2014-08-13
     */
    private ArrayList<String> convertJsonStringToArray(String jsonString) throws JSONException {

        ArrayList<String> stringArray = new ArrayList<String>();

        JSONArray jsonArray = new JSONArray(jsonString);
        int jsonArrayLength = jsonArray.length();
        for (int i = 0; i < jsonArrayLength; i++) {
            stringArray.add(jsonArray.getString(i));
        }

        return stringArray;
    }

    /**
     * The method will save the data in shared preferences defined by
     * "sharedPrefName" and the key provided by "key" parameter
     *
     * @param sharedPrefName name of the container
     * @param mode           private
     * @param key            name of the key in which values are saved
     * @param value          data to be saved associated to the particular key
     * @return none
     * @since 2014-08-13
     */
    public void saveDataInSharedPreferences(String sharedPrefName, int mode, String key, String value) {
        SharedPreferences prefsObj = context.getSharedPreferences(sharedPrefName, mode);
        SharedPreferences.Editor editorObj = prefsObj.edit();
        editorObj.putString(key, value);
        editorObj.commit();
    }

    /**
     * The method will read the data in shared preferences defined by
     * "sharedPrefName" and the key provided by "key" parameter
     *
     * @param sharedPrefName name of the container
     * @param mode           private
     * @param key            name of the key in which values are saved
     * @return String
     */
    public String readDataInSharedPreferences(String sharedPrefName, int mode, String key) {
        SharedPreferences prefsObj = context.getSharedPreferences(sharedPrefName, mode);
        return prefsObj.getString(key, "");
    }

    /**
     * The method will remove the data stored in shared preferences defined by
     * "sharedPrefName" and the key provided by "key" parameter
     *
     * @param sharedPrefName name of the container
     * @param mode           private
     * @param key            name of the key in which values are saved
     * @param removeAll      if true will remove all the values stored in shared
     *                       preferences else remove as specified by key
     */
    public void removeKeyFromSharedPreferences(String sharedPrefName, int mode, String key, boolean removeAll) {

        SharedPreferences prefsObj = context.getSharedPreferences(sharedPrefName, mode);
        SharedPreferences.Editor editorObj = prefsObj.edit();
        if (removeAll) {
            editorObj.clear();
        } else {
            editorObj.remove(key);
        }
        editorObj.commit();
    }

    /**
     * show message to user using showToast
     *
     * @param mContext                   contains context of application
     * @param message                    contains text/message to show
     * @param durationForMessageToAppear 1 will show the message for short
     *                                   duration else long duration
     */
    public void showToast(Context mContext, String message, int durationForMessageToAppear) {
        if (durationForMessageToAppear == 1) {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        }

    }

    /**
     * This method will hide virtual keyboard if opened
     *
     * @param mContext contains context of application
     */
    /*public void hideVirtualKeyboard(Context mContext) {

        try {

            IBinder binder = ((Activity) mContext).getWindow().getCurrentFocus()
                    .getWindowToken();

            if (binder != null) {
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binder, 0);
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }*/
    public void hiddenInputMethod(Context mContext) {

        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(((Activity) mContext).getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

    /**
     * This method will show virtual keyboard where ever required
     *
     * @param mContext contains context of application
     */
    public void showVirtualKeyboard(Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * Format value up to 2 decimal places
     *
     * @param num - number to be formatted
     */
    public float formatValueUpTo2Decimals(double num) {

        try {

            DecimalFormat df = new DecimalFormat("#.##");

            String value = df.format(num);
            String decimalPlace = ",";

            if (value.contains(decimalPlace)) {
                value = value.replace(decimalPlace, ".");
            }

            return Float.parseFloat(value);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return (float) num;

    }

    /**
     * The method validates if GooglePlayServices are available or not
     *
     * @param context - contains the context of the activity from where it is called
     * @return true if GooglePlayServices exists else false
     */

    public boolean validateGooglePlayServices(Context context) {

        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int resultCode = api.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (api.isUserResolvableError(resultCode)) {
                api.getErrorDialog((Activity) context, Constants.GooglePlayService.PLAY_SERVICES_RESOLUTION_REQUEST, resultCode).show();
            } else {
                AppInstance.logObj.printLog(context.getResources().getString(R.string.device_not_supported));
            }
            return false;
        }
        return true;

    }

    public void customActionBar(AppCompatActivity appCompatActivity, boolean isHomeAsUp, int title) {
        final ActionBar abar = appCompatActivity.getSupportActionBar();
        View viewActionBar = appCompatActivity.getLayoutInflater().inflate(R.layout.custom_actionbar, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.title);
        textviewTitle.setText(title);
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(isHomeAsUp);
        abar.setHomeButtonEnabled(true);
    }


    public void setToolbar(final AppCompatActivity appCompatActivity, String title, String hint, boolean isSearch, boolean isDashboard,
                           ImageView newDailyImageView, ImageView clear_view, LinearLayout searchLinearLayout, Toolbar toolbar) {
        ImageView leftTitleView = null;
        toolbar = (Toolbar) appCompatActivity.findViewById(R.id.search_actionbar);
        appCompatActivity.setSupportActionBar(toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView titleView = (TextView) appCompatActivity.findViewById(R.id.title);
        titleView.setText(title);

        TextView cancelTextView = (TextView) appCompatActivity.findViewById(R.id.cancel_tv);
        saveTextView = (TextView) appCompatActivity.findViewById(R.id.save_tv);

        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appCompatActivity.finish();
            }
        });
        final EditText searchEditText = (EditText) appCompatActivity.findViewById(R.id.search_et);
        searchEditText.setText("");
        searchEditText.setHint(hint);

        if (isSearch) {
            leftTitleView = newDailyImageView;
            leftTitleView = (ImageView) appCompatActivity.findViewById(R.id.search_view);
            leftTitleView.setImageDrawable(ContextCompat.getDrawable(appCompatActivity, R.drawable.ic_action_save));
        } else {
            leftTitleView = (ImageView) appCompatActivity.findViewById(R.id.search_view);
            leftTitleView.setImageDrawable(ContextCompat.getDrawable(appCompatActivity, R.drawable.places_ic_search));
        }

        clear_view = (ImageView) appCompatActivity.findViewById(R.id.clear_view);

        final ImageView settingView = (ImageView) appCompatActivity.findViewById(R.id.action_settings);

        if (isDashboard)

        {
            if (isSearch) {
                leftTitleView.setVisibility(View.VISIBLE);
            } else {
                // leftTitleView.setVisibility(View.GONE);
            }
        } else

        {
            leftTitleView.setVisibility(View.GONE);
            settingView.setVisibility(View.GONE);
            cancelTextView.setVisibility(View.VISIBLE);
            saveTextView.setVisibility(View.VISIBLE);
        }

        final LinearLayout searchLinear = (LinearLayout) appCompatActivity.findViewById(R.id.searchLinear);
        searchLinear.setVisibility(View.GONE);

       /* clear_view.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                searchLinear.setVisibility(View.GONE);
                toolbar.setVisibility(View.VISIBLE);
                // searchEditText.clearFocus();
                // appCompatActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        });*/

     /*   leftTitleView.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                toolbar.setVisibility(View.GONE);
                searchLinear.setVisibility(View.VISIBLE);
                //searchEditText.requestFocus();
                // showVirtualKeyboard(appCompatActivity);
            }
        });*/
        settingView.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                Intent intentObj = new Intent(appCompatActivity, SettingActivity.class);
                appCompatActivity.startActivity(intentObj);
            }
        });
    }


    public String getSearchText(String searcText) {
        return searcText;
    }


    public void commitFragment(Fragment fragment, AppCompatActivity appCompatActivity) {

        FragmentManager fragmentManager = appCompatActivity.getSupportFragmentManager();

        fragmentManager.beginTransaction().setCustomAnimations(R.anim.fadein, R.anim
                .fadeout)
                .replace(R.id.content_frame, fragment, null).commit();
    }

    public void customSearchButton(AppCompatActivity appCompatActivity, Menu menu) {
        MenuInflater inflater = appCompatActivity.getMenuInflater();
        menu.clear();
        inflater.inflate(R.menu.menu_search_setting, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) appCompatActivity.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity

        searchView.setSearchableInfo(searchManager.getSearchableInfo(appCompatActivity.getComponentName()));
        searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default

        AutoCompleteTextView searchEditText = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(ContextCompat.getColor(appCompatActivity, R.color.black));
        searchEditText.setHintTextColor(ContextCompat.getColor(appCompatActivity, R.color.gray));
        searchEditText.setBackgroundResource(R.drawable.edittext_drawable);
        ImageView searchMagIcon = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        searchMagIcon.setImageResource(R.drawable.places_ic_search);
    }


    /*
    * Get device ID
    * */

    public static String getDeviceId(Activity activity) {

//        TelephonyManager telephonyManager = (TelephonyManager)activity.getSystemService(Context
// .TELEPHONY_SERVICE);
//        return telephonyManager.getDeviceId();

        return Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }


    public void showAlert(Context context, String title, String message) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);
        //if(status != null)
        // Setting alert dialog icon
        //alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        alertDialog.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    public void setItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i = 0; i < menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception) item.setVisible(visible);
        }
    }

    public void showSnackBarAlert(View view, String message, boolean isNetworkCheck) {

        TSnackbar snackbar;
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);

        if (isNetworkCheck) {

            snackbar = TSnackbar
                    .make(view, message, Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
            // Changing message text color
            snackbar.setActionTextColor(Color.RED);


        } else {
            snackbar = TSnackbar.make(view, message, Snackbar.LENGTH_SHORT);
        }
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);

        snackbar.show();


    }


    public static String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            Log.v("Location Url", strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("downloading error", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public String changeDate(String newDate) {
        String dateStr = "";
        try {
            newDate = newDate.substring(0, 10);
            DateFormat dateFormat = new SimpleDateFormat(Constants.InputTag.POST_DATE_FORMAT);
            Date date = dateFormat.parse(newDate);//You will get date object relative to server/client timezone wherever it is parsed
            DateFormat formatter = new SimpleDateFormat(Constants.InputTag.DATE_FORMAT); //If you need time just put specific format for time like 'HH:mm:ss'
            dateStr = formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    public String formatDate(String newDate, String requestDate, String responseDate) {
        String dateStr = "";
        try {
            DateFormat dateFormat = new SimpleDateFormat(requestDate);
            Date date = dateFormat.parse(newDate);//You will get date object relative to server/client timezone wherever it is parsed
            DateFormat formatter = new SimpleDateFormat(responseDate); //If you need time just put specific format for time like 'HH:mm:ss'
            dateStr = formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conMgr.getActiveNetworkInfo();

        if (info != null || info.isConnected()) {
            Log.v("NetworkInfo", "Connected State");
            return true;
        } else {
            Log.v("NetworkInfo", "Not Connected state");
            Log.v("Reason", info.getReason());
            return false;
        }
    }

}
