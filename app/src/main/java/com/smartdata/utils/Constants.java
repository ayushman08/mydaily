package com.smartdata.utils;

/**
 * Created by Anurag Sethi
 * The class handles the constants used in the application
 */
public class Constants {

    /**
     * Handles the SplashScreen constants
     */
    public static class SplashScreen {
        /**
         * The parameter is used to manage the splash screen delay
         */
        public static int SPLASH_DELAY_LENGTH = 3000;
    }


    /**
     * Handles webservice constants
     */
    public static class WebServices {
        //stagging server url
        //52.34.207.5:5072
        //52.39.212.226:5072
       /* public static String BASE_URL = "https://52.34.207.5:5072/";
        public static String WS_BASE_URL = "https://52.34.207.5:5072/api/";*/

        //live server url
        /*public static String BASE_URL = "https://www.getmydaily.com/";
        public static String WS_BASE_URL = "https://www.getmydaily.com/api/";*/

        //local url
        public static String BASE_URL = "http://172.10.21.161:5072/";
        public static String WS_BASE_URL = "http://172.10.21.161:5072/api/";

        public static final String WS_USER_AUTHENTICATION = "userLogin";
        public static final String WS_USER_FORGOT_PASSWORD = "forgotPassword";
        public static final String WS_USER_LOGOUT = "userLogOut";
        public static final String WS_FOREMAN_LIST = "getAllForemenList";//"getForemenList";

        public static final String WS_CREWS_LIST = "getCrewList";
        public static final String WS_JOB_LIST = "getJobList";
        public static final String WS_PRODUCT_LIST = "listAllBillableItem/{id}";
        public static final String WS_DELETE_CREW = "deleteCrewsById/{id}";
        public static final String WS_ADD_UPDATE_CREW = "addUpdateCrewsData";
        public static final String WS_ADD_JOB = "addNewJob";
        public static final String WS_FOREMAN_JOB_LIST = "getForemenAssignJob";
        public static final String WS_MEDIA_UPLOAD_TO_SERVER = "gallery/services/uploadFile";
        public static final String WS_MEDIA_DELETE_FROM_SERVER = "gallery/services/deleteFile";
        public static final String WS_MEDIA_DOWNLOAD_FROM_SERVER = "gallery/services/allFileList";
        public static final String WS_SWIPE_REFRESH_EXAMPLE_URL = "swipe/swipe/swipeRefreshMovieList";
        public static final String WS_JOB_DETAIL = "getJobInfoById/{id}";
        public static final String WS_MY_DAILIES = "listMyDailies";
        public static final String WS_ADD_MY_DAILIES = "addMyDailies";
        public static final String WS_ADD_MY_DAILIES_IMAGES = "addDailiesImages";
        public static final String WS_ADD_INCIDENT_IMAGES = "addIncidentImages";
        public static final String WS_GET_INCIDENT_LIST = "getIncidentList";
        public static final String WS_ADD_UPDATE_INCIDENT = "addUpdateincident";
        public static final String WS_DELETE_DAILY = "deleteDailiesByID/{id}";
        public static final String WS_DELETE_INCIDENT = "deleteIncidentId/{id}";
        public static final String WS_DELETE_JOB = "deleteJobById/{id}";
        public static final String WS_SUPERVISOR_LIST = "getAssignSupervisorList";
        public static final String WS_ADD_CREW_IMAGES = "addCrewsImages";
        public static final String WS_UPDATE_DAILY_STATUS = "updateDailiesStatus";
        public static String TERMS_URL = "admin/#/terms-and-condition";
        public static String PRIVACY_URL = "/admin/#/privacy";
        public static final String WS_DOWNLOAD_DAILIES = "downloadPdf";
        public static final String WS_FEEDBACK = "sendfeedback";
        public static final String WS_GET_BILLABLE_ITEM = "getBillableItem";

        public static final String GOOGLE_PLACE_API_KEY = "AIzaSyAxTzV8j14gQV-5JTOza-KWnDwwHWDuU6A";
        public static final String GOOGLE_PLACE_AUTOCOMPLETE_API = "https://maps.googleapis.com/maps/api/place/autocomplete/";
        public static final String GOOGLE_PLACE_DETAILS_API = "https://maps.googleapis.com/maps/api/place/details/";
    }

    /**
     * Handles the TaskIDs so as to differentiate the web service return values
     */

    public static class TaskID {
        public static int LOGIN_TASK_ID = 100;
        public static int FORGOT_PASSWORD_TASK_ID = 101;
        public static int MEDIA_FILE_UPLOAD_TASK_ID = 102;
        public static int MEDIA_FILE_DELETE_TASK_ID = 103;
        public static int MEDIA_FILE_DOWNLOAD_URL_TASK_ID = 104;
        public static int SWIPE_REFRESH_TASK_ID = 105;
        public static int USER_LOGOUT = 106;
        public static int GET_FOREMAN_LIST = 107;
        public static int GET_JOB_LIST = 108;
        public static int GET_CREWS_LIST = 109;
        public static int GET_PRODUCTION_LIST = 110;
        public static int ADD_JOB = 111;
        public static int GET_FOREMAN_JOB_LIST = 112;
        public static int UPDATE_CREWS_LIST = 113;
        public static int DELETE_CREW = 114;
        public static int ADD_CREW = 115;
        public static int JOB_DETAILS = 116;
        public static int GET_MY_DAILIES = 117;
        public static int ADD_NEW_DAILY = 118;
        public static int GET_INCIDENT_LIST = 119;
        public static int ADD_NEW_INCIDENT = 120;
        public static int DELETE_DAILY = 121;
        public static int UPLOAD_DAILIES_IMAGE = 122;
        public static int UPLOAD_INCIDENT_IMAGE = 123;
        public static int DELETE_INCIDENT = 124;
        public static int GET_SUPERVISOR_LIST = 125;
        public static int UPLOAD_CREW_IMAGE = 126;
        public static int DELETE_JOB = 127;
        public static int UPDATE_DAILY_STATUS = 128;
        public static int DOWNLOAD_DAILIES = 129;
        public static int FEEDBACK = 130;
        public static int UPDATE_INCIDENT_IMAGE = 131;
        public static int GET_BILLABLE_ITEM = 132;
    }

    /**
     * Handles the ButtonTags so as to differentiate them in showAlertDialog()
     */

    public static class ButtonTags {

        public static String TAG_NETWORK_SERVICE_ENABLE = "network services";
        public static String TAG_LOCATION_SERVICE_ENABLE = "location services";
        public static final String FRAGMENT_INDEX = "FRAGMENT_INDEX";
        public static final String IMAGE_POSITION = "IMAGE_POSITION";
    }

    /**
     * Handles the JSON Parsing
     */
    public static class JsonParsing {
        public static int PARSING_JSON_FOR_MESSAGE_ID = 1;
        public static int PARSING_JSON_FOR_RESULT = 2;
    }

    /**
     * Handles the DebugLog constants
     */
    public static class DebugLog {
        /**
         * Will be the name of the project
         */
        public static String APP_TAG = "mydaily";
        /**
         * APP_MODE = 1 means Debug Mode
         * APP_MODE = 0 means Live Mode
         * Must change to 0 when going live
         */
        public static int APP_MODE = 1;
        /**
         * Name of the directory in which log file needs to be saved
         */
        public static String APP_ERROR_DIR_NAME = "mydaily";
        /**
         * Name of the log file
         */
        public static String APP_ERROR_LOG_FILE_NAME = "log.txt";
    }

    /**
     * Handles the constant for Google Play Services
     */
    public static class GooglePlayService {
        public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    }

    /**
     * Handles the requestCodes
     */
    public static class RequestCodes {
        public static int RESULT_GALLERY_LOAD_IMAGE = 1;
        public static int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
        public static int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
        public static int MEDIA_TYPE_IMAGE = 1;
        public static int MEDIA_TYPE_VIDEO = 2;
        public static final int REQ_CODE_ACCESS_FINE_LOCATION = 301;
        public static final int REQ_CODE_JOB = 302;
        public static final int REQ_CODE_EDIT_CREWS = 303;
        public static final int REQ_CODE_ADD_CREWS = 304;
        public static final String REQ_CODE_COMPLETED = "Completed";
        public static final String REQ_CODE_PROGRESS = "Progress";
        public static final String REQ_CODE_PENDING = "Pending";
        public static final String REQ_CODE_ADD_CREW = "add crew";
        public static final String REQ_CODE_EDIT_CREW = "edit crew";
        public static final String REQ_CODE_DETAIL_CREW = "crew detail";
        public static final String REQ_CODE_DAILY_ACTIVE = "Active";
        public static final String REQ_CODE_DAILY_ACCEPTED = "Accepted";
        public static final String REQ_CODE_DAILY_REJECTED = "Rejected";
        public static final String REQ_CODE_CREW_DATA = "crew_data_key_intent";
        public static final String REQ_CODE_CREW_DETAILS = "crew_details_key_intent";
        public static final String REQ_CODE_CREW_IMAGE_DATA = "crew_image_data_key_intent";
        public final static String INCIDENT_DETAILS = "INCIDENT_DETAILS";
        public final static String INCIDENT_IMAGES = "INCIDENT_IMAGES";
        public final static String INCIDENT_JOB_DETAILS = "INCIDENT_JOB_DETAILS";
        public static final int REQ_CODE_GALLERY = 305;
        public static final int REQ_CODE_CAMERA = 306;
        public static final int REQ_PIC_CROP = 307;
        public static final int REQ_CODE_READ_WRITE_FILE = 308;
        public static final int REQ_CODE_NEW_INCIDENT = 309;
        public static final String REQ_CODE_DAILY_DATA = "daily_data_key_intent";
        public static final String REQ_CODE_CREW_DAILY_DATA = "crew_daily_data_key_intent";
        public static final String REQ_CODE_SUPERVISOR_DAILY_DATA = "supervisor_daily_data_key_intent";
        public static final String REQ_CODE_BILLABLE_DAILY_DATA = "billable_daily_data_key_intent";
        public static final String REQ_CODE_IMAGE_DAILY_DATA = "image_daily_data_key_intent";
        public static final String REQ_CODE_JOB_DAILY_DATA = "job_daily_data_key_intent";
        public static final String REQ_CODE_INCIDENT_DATA = "incident_data_key_intent";
        public static final String REQ_CODE_SUBMITTALS = "submittals_data_key_intent";
        public static final int REQ_CODE_EDIT_DAILY = 310;
        public static final int REQ_CODE_EDIT_INCIDENT = 311;
        public static final int REQ_CODE_EDIT_JOB = 312;
        public static final int REQ_CODE_ACCEPT_DAILY = 313;
        public static final int REQ_CODE_REJECT_DAILY = 314;
        public static final String REQ_IS_DRAFT = "isDraft";
        public static final String INCIDENT_IS_DRAFT = "incidentIsDraft";

    }

    /**
     * Handles the requestCodes
     */
    public static class ResponseCodes {

        public static int RESPONSE_SUCCESS = 200;
        public static int RESPONSE_NOT_FOUND = 404;
        public static int RESPONSE_FAILED = 402;

    }

    /**
     * Handles the Media constants
     */
    public static class Media {
        public static String IMAGE_DIRECTORY_NAME = "mydaily";

        /**
         * GALLERY_TYPE = 1 means that both images and video will be downloaded
         * GALLERY_TYPE = 2 means that only images will be downloaded
         * GALLERY_TYPE = 3 means that only videos will be downloaded
         */
        public static int GALLERY_TYPE = 1;

        public static int GALLERY_MEDIA_DISPLAY_WIDTH = 300;
        public static int GALLERY_MEDIA_DISPLAY_HEIGHT = 300;
    }

    /**
     * Handles the constants for GoogleAnalytics
     */
    public static class GoogleAnalytics {
        public static String trackingID = "";
    }

    public static class InputTag {
        public static final String DEVICE_TYPE = "Android";
        public static final String ROLE_CODE_SV = "SV";
        public static final String ROLE_CODE_FM = "FM";
        public static final String SUCCESS = "success";
        public static final String WEB_INTENT_KEY = "intent_key";
        public static final String WEB_TERMS = "terms";
        public static final String WEB_URL_INTENT_KEY = "intent_key";
        public static final String INTENT_KEY_FOREMAN_NAME = "FOREMAN_NAME";
        public static final String INTENT_KEY_FOREMAN_JOB = "FOREMAN_JOB";
        public static final String INTENT_KEY_FOREMAN_ID = "FOREMAN_ID";
        public final static String DATE_FORMAT = "dd MMM";
        public final static String POST_DATE_FORMAT = "yyyy-MM-dd";
        public final static String DAILY_POST_DATE_FORMAT = "dd-MM-yyyy";
        public final static String NEW_DAILY_DATE_FORMAT = "dd MMMM yyyy";
        public final static String GET_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";//yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
        public final static String GET_INCIDENT_DATE_FORMAT = "MM/dd/yyyy, HH:mm:ss";
        public final static String JOB_DETAILS = "JOB_DETAILS";
        public final static String JOB_ID = "JOB_ID";
        public final static String BILLABLE_ITEMS = "BILLABLE_ITEMS";
        public static String FILE_NAME = "mydaily.jpg";
        public static String MY_DAILY_FILE_NAME = "mydaily";
        public static String FILE = "/image.jpg";
        public static String DAILY_FILE = "/image";
        public static String DAILY_FILE_EXTENSION = ".jpg";
        public static String DATA = "data";
        public static final String STATUS_ONE = "1";
        public static final String STATUS_TWO = "2";
        public static final String STATUS_THREE = "3";
        public final static String LATITUDE = "latitude";
        public final static String LONGITUDE = "longitude";
    }
}
