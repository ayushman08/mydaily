package com.smartdata.managers;

import android.content.Context;

import com.smartdata.dataobject.AppInstance;
import com.smartdata.dataobject.Media;
import com.smartdata.dataobject.MediaGallery;
import com.smartdata.interfaces.ServiceRedirection;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.ResponseCodes;
import com.smartdata.utils.Utility;
import java.util.ArrayList;

import com.smartdata.interfaces.ApiInterface;
import com.smartdata.async.ApiClient;
import retrofit2.Call;
import retrofit2.Response;
import sdei.support.lib.interfaces.CallBack;

/**
 * Created by Anurag Sethi on 13-05-2015.
 */
public class MediaManager implements CallBack {


    Context contextObj;
    Utility utilObj;
    CommunicationManager commObj;
    ServiceRedirection serviceRedirectionObj;
    int tasksID;

     //Retrofit Interface
    ApiInterface apiService;
    String authentication="12345"; // you should change Authentication according to your requirement
    
    /**
     * Constructor
     * @param context  The Context from where the method is called
     * @param successRedirectionListener The listener interface for receiving action events
     * @return none
     */
    public MediaManager(Context context, ServiceRedirection successRedirectionListener) {

        contextObj = context;
        utilObj = new Utility(contextObj);
        serviceRedirectionObj = successRedirectionListener;
        
        apiService = ApiClient.createService(ApiInterface.class, authentication);
    }

    /**
     * The method will upload the media file to server
     * @param filePath path of the media file to be uploaded
     */
    public void uploadMediaFile(String filePath) {

        commObj = new CommunicationManager(contextObj);
        tasksID = Constants.TaskID.MEDIA_FILE_UPLOAD_TASK_ID;
     
        Call call = apiService.mediaUploadWebService(filePath);
        commObj.CallWebService(this, tasksID, call);
    }

    /**
     * The method will delete the media file from the server
     * @param mediaObj mediaObject
     */
    public void deleteMedia(Media mediaObj) {
        String jsonString = utilObj.convertObjectToJson(mediaObj);
        commObj = new CommunicationManager(contextObj);
        tasksID = Constants.TaskID.MEDIA_FILE_DELETE_TASK_ID;
    
        Call call = apiService.mediaDeleteWebService(mediaObj);
        commObj.CallWebService(this, tasksID, call);
    }

    /**
     * The method will download all the saved media files on the server
     */
    public void downloadMediaFileUrls(MediaGallery mediaGalleryObj) {
        String jsonString = utilObj.convertObjectToJson(mediaGalleryObj);
        AppInstance.logObj.printLog("jsonString=" + jsonString);

        commObj = new CommunicationManager(contextObj);
        tasksID = Constants.TaskID.MEDIA_FILE_DOWNLOAD_URL_TASK_ID;

        Call call = apiService.mediaDownloadWebService(mediaGalleryObj);
        commObj.CallWebService(this, tasksID, call);
    }

    /**
     * The interface method implemented in the java files
     *
     * @param data the result returned by the web service
     * @param tasksID the ID to differential multiple webservice calls
     * @param statusCode the statusCode returned by the web service
     * @param message the message returned by the web service
     * @return none
     * @since 2014-08-28
     */

    @Override
    public void onResult(Response data, int tasksID, int statusCode, String message) {


        if(tasksID == Constants.TaskID.MEDIA_FILE_UPLOAD_TASK_ID) {
            if (data != null) {
                if(statusCode==ResponseCodes.Success) {
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                }else{
                    serviceRedirectionObj.onFailureRedirection(message);
                }
            }
            else{
                serviceRedirectionObj.onFailureRedirection(message);
            }
        }
        else if(tasksID == Constants.TaskID.MEDIA_FILE_DELETE_TASK_ID) {
            if (data != null) {
                if(statusCode==ResponseCodes.Success) {
                    AppInstance.mediaObj = (Media) data.body();
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                }else{
                    serviceRedirectionObj.onFailureRedirection(message);
                }
            }
            else{
                serviceRedirectionObj.onFailureRedirection(message);
            }
        }
        else if(tasksID == Constants.TaskID.MEDIA_FILE_DOWNLOAD_URL_TASK_ID) {
            if (data != null) {
                if(statusCode==ResponseCodes.Success) {
                    AppInstance.mediaGalleryObj.arrayListMedia = (ArrayList<Media>) data.body();
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                }else{
                    serviceRedirectionObj.onFailureRedirection(message);
                }
            }
            else{
                serviceRedirectionObj.onFailureRedirection(message);
            }
        }



    }

}
