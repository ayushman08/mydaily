package com.smartdata.managers;

import android.content.Context;

import com.smartdata.async.ApiClient;
import com.smartdata.config.PreferenceConfiguration;
import com.smartdata.dataobject.AppInstance;
import com.smartdata.dataobject.User;
import com.smartdata.interfaces.ApiInterface;
import com.smartdata.interfaces.ServiceRedirection;
import com.smartdata.utils.Constants;
import com.smartdata.utils.ResponseCodes;
import com.smartdata.utils.Utility;

import retrofit2.Call;
import retrofit2.Response;
import sdei.support.lib.interfaces.CallBack;

/**
 * Created by Anurag Sethi
 * The class will handle all the implementations related to the login operations
 */
public class ForemanManager implements CallBack {

    Context context;
    Utility utilObj;
    CommunicationManager commObj;
    ServiceRedirection serviceRedirectionObj;
    int tasksID;

    //Retrofit Interface
    ApiInterface apiService;
    // String authentication = "12345"; // you should change Authentication according to your requirement


    /**
     * Constructor
     *
     * @param contextObj                 The Context from where the method is called
     * @param successRedirectionListener The listener interface for receiving action events
     * @return none
     */
    public ForemanManager(Context contextObj, ServiceRedirection successRedirectionListener) {
        context = contextObj;
        utilObj = new Utility(contextObj);
        serviceRedirectionObj = successRedirectionListener;

        apiService = ApiClient.createService(ApiInterface.class, PreferenceConfiguration.getAuthToken(context));
    }


    /**
     * The interface method implemented in the java files
     *
     * @param data       the result returned by the web service
     * @param tasksID    the ID to differential multiple webservice calls
     * @param statusCode the statusCode returned by the web service
     * @param message    the message returned by the web service
     * @return none
     * @since 2014-08-28
     */


    @Override
    public void onResult(Response data, int tasksID, int statusCode, String message) {


    }
}
