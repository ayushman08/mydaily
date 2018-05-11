package com.smartdata.managers;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.smartdata.dataobject.AppInstance;
import com.smartdata.dataobject.SwipeRefresh;
import com.smartdata.interfaces.ServiceRedirection;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.ResponseCodes;
import com.smartdata.utils.Utility;

import java.lang.reflect.Type;
import java.util.List;

import com.smartdata.interfaces.ApiInterface;
import com.smartdata.async.ApiClient;
import retrofit2.Call;
import retrofit2.Response;
import sdei.support.lib.interfaces.CallBack;


/**
 * Created by Anurag Sethi on 29-06-2015.
 */
public class SwipeRefreshManager implements CallBack {


    private Context context;
    private ServiceRedirection serviceRedirectionObj;
    private Utility utilObj;
    private CommunicationManager communicationManagerObj;
    int tasksID;

     //Retrofit Interface
    ApiInterface apiService;
    String authentication="12345"; // you should change Authentication according to your requirement
    
    
    /**
     * Constructor
     * @param contextObj  The Context from where the method is called
     * @param successRedirectionListener The listener interface for receiving action events
     * @return none
     */
    public SwipeRefreshManager(Context contextObj, ServiceRedirection successRedirectionListener) {
        context = contextObj;
        serviceRedirectionObj = successRedirectionListener;
        utilObj = new Utility(contextObj);
        
         apiService = ApiClient.createService(ApiInterface.class, authentication);
    }

    /**
     * Calls the Web Service of fetching movie list
     * @param swipeRefreshObj - data object containing the offset value
     * @return none
     */
    public void fetchMovies(SwipeRefresh swipeRefreshObj) {
        String jsonString = utilObj.convertObjectToJson(swipeRefreshObj);
        AppInstance.logObj.printLog("jsonString=" + jsonString);
        communicationManagerObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.SWIPE_REFRESH_TASK_ID;
       
     
         Call call = apiService.getSwipeRefresh(jsonString);
        communicationManagerObj.CallWebService(this,tasksID,call);
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


        if(tasksID == Constants.TaskID.SWIPE_REFRESH_TASK_ID) {
            if (data != null) {
                if(statusCode==ResponseCodes.Success) {
                    Type listType = new TypeToken<List<SwipeRefresh>>(){}.getType();
                    AppInstance.swipeRefreshListObj = (List<SwipeRefresh>)data.body();
                    serviceRedirectionObj.onSuccessRedirection(Constants.TaskID.SWIPE_REFRESH_TASK_ID);
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
