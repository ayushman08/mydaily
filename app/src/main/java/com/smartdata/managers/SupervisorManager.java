package com.smartdata.managers;

import android.content.Context;
import android.util.Log;

import com.smartdata.async.ApiClient;
import com.smartdata.config.PreferenceConfiguration;
import com.smartdata.dataobject.AppInstance;
import com.smartdata.dataobject.CrewsData;
import com.smartdata.dataobject.FeedbackData;
import com.smartdata.dataobject.ForemanData;
import com.smartdata.dataobject.ForemanJobData;
import com.smartdata.dataobject.IncidentData;
import com.smartdata.dataobject.JobData;
import com.smartdata.dataobject.MyDailyData;
import com.smartdata.dataobject.MyDailyResponse;
import com.smartdata.dataobject.ProductData;
import com.smartdata.dataobject.ProductDataList;
import com.smartdata.dataobject.ResponseData;
import com.smartdata.dataobject.SupervisorAddJobData;
import com.smartdata.dataobject.SupervisorData;
import com.smartdata.dataobject.SupervisorJobDetails;
import com.smartdata.dataobject.User;
import com.smartdata.interfaces.ApiInterface;
import com.smartdata.interfaces.ServiceRedirection;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.ResponseCodes;
import com.smartdata.utils.Utility;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import sdei.support.lib.interfaces.CallBack;

/**
 * Created by Anurag Sethi
 * The class will handle all the implementations related to the login operations
 */
public class SupervisorManager implements CallBack {

    Context context;
    Utility utilObj;
    CommunicationManager commObj;
    ServiceRedirection serviceRedirectionObj;
    int tasksID;
    //Retrofit Interface
    ApiInterface apiService;

    /**
     * Constructor
     *
     * @param contextObj                 The Context from where the method is called
     * @param successRedirectionListener The listener interface for receiving action events
     * @return none
     */
    public SupervisorManager(Context contextObj, ServiceRedirection successRedirectionListener) {
        context = contextObj;
        utilObj = new Utility(contextObj);
        serviceRedirectionObj = successRedirectionListener;
        apiService = ApiClient.createService(ApiInterface.class, PreferenceConfiguration.getAuthToken(context));
    }

    /**
     * Calls the Web Service of foreman list
     *
     * @param foremanData having the required data
     * @return none
     */
    public void getForemanList(ForemanData foremanData) {
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.GET_FOREMAN_LIST;
        Call call = apiService.getForemanList(foremanData);
        commObj.CallWebService(this, tasksID, call);
    }


    /**
     * Calls the Web Service of feedback
     *
     * @param feedbackData having the required data
     * @return none
     */
    public void sendFeedbackData(FeedbackData feedbackData) {
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.FEEDBACK;
        Call call = apiService.feedback(feedbackData);
        commObj.CallWebService(this, tasksID, call);
    }

    /**
     * Calls the Web Service of job list
     *
     * @param jobData having the required data
     * @return none
     */
    public void getJobList(JobData jobData) {
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.GET_JOB_LIST;
        Call call = apiService.getJobList(jobData);
        commObj.CallWebService(this, tasksID, call);
    }


    /**
     * Calls the Web Service of my dailies
     *
     * @param myDailyData having the required data
     * @return none
     */
    public void getMyDailies(MyDailyData myDailyData) {
        commObj = new CommunicationManager(this.context);
        System.out.println("QWERTY==> " + myDailyData);
        tasksID = Constants.TaskID.GET_MY_DAILIES;
        Call call = apiService.getMyDailies(myDailyData);
        commObj.CallWebService(this, tasksID, call);
    }


    /**
     * Calls the Web Service of my dailies
     *
     * @param user having the required data
     * @return none
     */
    public void downloadDailies(User user) {
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.DOWNLOAD_DAILIES;
        Call call = apiService.downloadDailies(user);
        commObj.CallWebService(this, tasksID, call);
    }

    /**
     * Calls the Web Service of my dailies
     *
     * @param myDailyData having the required data
     * @return none
     */
    public void updateDailyStatus(MyDailyData myDailyData) {
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.UPDATE_DAILY_STATUS;
        Call call = apiService.updateDailyStatus(myDailyData);
        commObj.CallWebService(this, tasksID, call);
    }


    /**
     * Calls the Web Service of my dailies
     *
     * @param myDailyData having the required data
     * @return none
     */
    public void addNewDaily(MyDailyData myDailyData) {
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.ADD_NEW_DAILY;
        Call call = apiService.addNewDaily(myDailyData);
        commObj.CallWebService(this, tasksID, call);
    }

    /**
     * Calls the Web Service of crew list
     *
     * @param crewsData having the required data
     * @return none
     */
    public void getCrewsList(CrewsData crewsData) {
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.GET_CREWS_LIST;
        Call call = apiService.getCrewsList(crewsData);
        commObj.CallWebService(this, tasksID, call);
    }


    /**
     * Calls the Web Service of crew list
     *
     * @param supervisorData having the required data
     * @return none
     */
    public void getSupervisorList(SupervisorData supervisorData) {
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.GET_SUPERVISOR_LIST;
        Call call = apiService.getSupervisorList(supervisorData);
        commObj.CallWebService(this, tasksID, call);
    }


    /**
     * Calls the Web Service of add crew
     *
     * @param crewsData having the required data
     * @return none
     */
    public void addCrew(CrewsData crewsData) {
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.ADD_CREW;
        Call call = apiService.addCrew(crewsData);
        commObj.CallWebService(this, tasksID, call);
    }


    /**
     * Calls the Web Service of job details
     *
     * @param id having the required data
     * @return none
     */
    public void getJobDetails(String id) {
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.JOB_DETAILS;
        Call call = apiService.getJobDetails(id);
        commObj.CallWebService(this, tasksID, call);
    }


    /**
     * Calls the Web Service of foreman production data list
     */
    public void getBillableItem(ProductDataList productDataList) {
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.GET_BILLABLE_ITEM;
        Call call = apiService.getBillableItem(productDataList);
        commObj.CallWebService(this, tasksID, call);
    }

    /**
     * Calls the Web Service of foreman production data list
     */
    public void getProductionList(String id) {
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.GET_PRODUCTION_LIST;
        Call call = apiService.getProductList(id);
        commObj.CallWebService(this, tasksID, call);
    }

    /**
     * Calls the Web Service to delete crew member
     */
    public void deleteCrew(String id) {
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.DELETE_CREW;
        Call call = apiService.deleteCrew(id);
        commObj.CallWebService(this, tasksID, call);
    }

    /**
     * Calls the Web Service of add new job
     *
     * @param supervisorAddJobData data having the required data to add new job
     * @return none
     */
    public void addNewJob(SupervisorAddJobData supervisorAddJobData) {
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.ADD_JOB;
        Call call = apiService.addNewJob(supervisorAddJobData);
        commObj.CallWebService(this, tasksID, call);
    }


    /**
     * Calls the Web Service of foreman job list
     *
     * @param foremanJobData data having the required data to get foreman assigned job list
     * @return none
     */
    public void getForemanJobList(ForemanJobData foremanJobData) {
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.GET_FOREMAN_JOB_LIST;
        Call call = apiService.getForemanJobList(foremanJobData);
        commObj.CallWebService(this, tasksID, call);
    }


    /**
     * Calls the Web Service of incident list
     *
     * @param incidentData having the required data
     * @return none
     */
    public void getIncidentList(IncidentData incidentData) {
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.GET_INCIDENT_LIST;
        Call call = apiService.getIncidentList(incidentData);
        commObj.CallWebService(this, tasksID, call);
    }


    /**
     * Calls the Web Service of add or update new incident
     *
     * @param incidentData having the required data
     * @return none
     */
    public void addNewIncident(IncidentData incidentData) {
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.ADD_NEW_INCIDENT;
        Call call = apiService.addNewIncident(incidentData);
        commObj.CallWebService(this, tasksID, call);
    }


    /**
     * Calls the Web Service to delete daily
     */
    public void deleteDaily(String id) {
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.DELETE_DAILY;
        Call call = apiService.deleteDaily(id);
        commObj.CallWebService(this, tasksID, call);
    }


    /**
     * Calls the Web Service to delete incident
     */
    public void deleteIncident(String id) {
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.DELETE_INCIDENT;
        Call call = apiService.deleteIncident(id);
        commObj.CallWebService(this, tasksID, call);
    }

    /**
     * Calls the Web Service to delete incident
     */
    public void deleteJob(String id) {
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.DELETE_JOB;
        Call call = apiService.deleteJob(id);
        commObj.CallWebService(this, tasksID, call);
    }


    /**
     * Calls the Web Service of upload my dailies image
     *
     * @return none
     */
    public void uploadDailiesImage(MultipartBody.Part file, RequestBody id) {
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.UPLOAD_DAILIES_IMAGE;
        Call call = apiService.uploadDailiesImage(file, id);
        commObj.CallWebService(this, tasksID, call);
    }

    /**
     * Calls the Web Service of upload my dailies image
     *
     * @return none
     */
    public void uploadCrewImage(MultipartBody.Part file, RequestBody id) {
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.UPLOAD_CREW_IMAGE;
        Call call = apiService.uploadCrewImage(file, id);
        commObj.CallWebService(this, tasksID, call);
    }

    /**
     * Calls the Web Service of upload my incident image
     *
     * @return none
     */
    public void uploadIncidentImage(MultipartBody.Part file, RequestBody id) {
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.UPLOAD_INCIDENT_IMAGE;
        Call call = apiService.uploadIncidentImage(file, id);
        commObj.CallWebService(this, tasksID, call);
    }

    /**
     * Calls the Web Service of upload my incident image
     *
     * @return none
     */
    public void updateIncidentImage(MultipartBody.Part file, RequestBody id, RequestBody _id) {
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.UPDATE_INCIDENT_IMAGE;
        Call call = apiService.updateIncidentImage(file, id, _id);
        commObj.CallWebService(this, tasksID, call);
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

        if (tasksID == Constants.TaskID.GET_FOREMAN_LIST) {
            if (data != null) {
                if (statusCode == ResponseCodes.Success) {
                    AppInstance.foremanData = (ForemanData) data.body();
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } else {
                    serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
            }
        } else if (tasksID == Constants.TaskID.GET_JOB_LIST) {
            if (data != null) {
                if (statusCode == ResponseCodes.Success) {
                    AppInstance.jobData = (JobData) data.body();
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } else {
                    serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
            }
        } else if (tasksID == Constants.TaskID.GET_CREWS_LIST) {
            if (data != null) {
                if (statusCode == ResponseCodes.Success) {
                    AppInstance.crewsData = (CrewsData) data.body();
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } else {
                    serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
            }
        } else if (tasksID == Constants.TaskID.GET_PRODUCTION_LIST) {
            if (data != null) {
                if (statusCode == ResponseCodes.Success) {
                    AppInstance.productData = (ProductData) data.body();
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } else {
                    serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
            }
        } else if (tasksID == Constants.TaskID.GET_BILLABLE_ITEM) {
            if (data != null) {
                if (statusCode == ResponseCodes.Success) {
                    AppInstance.productData = (ProductData) data.body();
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } else {
                    serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
            }
        } else if (tasksID == Constants.TaskID.ADD_JOB) {
            if (data != null) {
                if (statusCode == ResponseCodes.Success) {
                    AppInstance.supervisorAddJobData = (SupervisorAddJobData) data.body();
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } else {
                    serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
            }
        } else if (tasksID == Constants.TaskID.GET_FOREMAN_JOB_LIST) {
            if (data != null) {
                if (statusCode == ResponseCodes.Success) {
                    AppInstance.foremanJobData = (ForemanJobData) data.body();
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } else {
                    serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
            }
        } else if (tasksID == Constants.TaskID.UPDATE_CREWS_LIST) {
            if (data != null) {
                if (statusCode == ResponseCodes.Success) {
                    AppInstance.crewsData = (CrewsData) data.body();
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } else {
                    serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
            }
        } else if (tasksID == Constants.TaskID.DELETE_CREW) {
            if (data != null) {
                if (statusCode == ResponseCodes.Success) {
                    AppInstance.responseData = (ResponseData) data.body();
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } else {
                    serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
            }
        } else if (tasksID == Constants.TaskID.ADD_CREW) {
            if (data != null) {
                if (statusCode == ResponseCodes.Success) {
                    AppInstance.myDailyResponse = (MyDailyResponse) data.body();
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } else {
                    serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
            }
        } else if (tasksID == Constants.TaskID.JOB_DETAILS) {
            if (data != null) {
                if (statusCode == ResponseCodes.Success) {
                    AppInstance.supervisorJobDetails = (SupervisorJobDetails) data.body();
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } else {
                    serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
            }
        } else if (tasksID == Constants.TaskID.GET_MY_DAILIES) {
            if (data != null) {
                if (statusCode == ResponseCodes.Success) {
                    AppInstance.myDailyData = (MyDailyData) data.body();
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } else {
                    serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
            }
        } else if (tasksID == Constants.TaskID.ADD_NEW_DAILY) {
            if (data != null) {
                if (statusCode == ResponseCodes.Success) {
                    AppInstance.myDailyResponse = (MyDailyResponse) data.body();
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } else {
                    serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
            }
        } else if (tasksID == Constants.TaskID.GET_INCIDENT_LIST) {
            if (data != null) {
                if (statusCode == ResponseCodes.Success) {
                    AppInstance.incidentData = (IncidentData) data.body();
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } else {
                    serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
            }
        } else if (tasksID == Constants.TaskID.ADD_NEW_INCIDENT) {
            if (data != null) {
                if (statusCode == ResponseCodes.Success) {
                    AppInstance.myDailyResponse = (MyDailyResponse) data.body();
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } else {
                    serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
            }
        } else if (tasksID == Constants.TaskID.DELETE_DAILY) {
            if (data != null) {
                if (statusCode == ResponseCodes.Success) {
                    AppInstance.responseData = (ResponseData) data.body();
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } else {
                    serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
            }
        } else if (tasksID == Constants.TaskID.UPLOAD_DAILIES_IMAGE) {
            if (data != null) {
                if (statusCode == ResponseCodes.Success) {
                    AppInstance.responseData = (ResponseData) data.body();
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } else {
                    serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
            }
        } else if (tasksID == Constants.TaskID.UPLOAD_INCIDENT_IMAGE) {
            if (data != null) {
                if (statusCode == ResponseCodes.Success) {
                    AppInstance.responseData = (ResponseData) data.body();
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } else {
                    serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
            }
        } else if (tasksID == Constants.TaskID.UPDATE_INCIDENT_IMAGE) {
            if (data != null) {
                if (statusCode == ResponseCodes.Success) {
                    AppInstance.responseData = (ResponseData) data.body();
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } else {
                    serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
            }
        } else if (tasksID == Constants.TaskID.DELETE_INCIDENT) {
            if (data != null) {
                if (statusCode == ResponseCodes.Success) {
                    AppInstance.responseData = (ResponseData) data.body();
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } else {
                    serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
            }
        } else if (tasksID == Constants.TaskID.GET_SUPERVISOR_LIST) {
            if (data != null) {
                if (statusCode == ResponseCodes.Success) {
                    AppInstance.supervisorData = (SupervisorData) data.body();
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } else {
                    serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
            }
        } else if (tasksID == Constants.TaskID.UPLOAD_CREW_IMAGE) {
            if (data != null) {
                if (statusCode == ResponseCodes.Success) {
                    AppInstance.responseData = (ResponseData) data.body();
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } else {
                    serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
            }
        } else if (tasksID == Constants.TaskID.DELETE_JOB) {
            if (data != null) {
                if (statusCode == ResponseCodes.Success) {
                    AppInstance.responseData = (ResponseData) data.body();
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } else {
                    serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
            }
        } else if (tasksID == Constants.TaskID.UPDATE_DAILY_STATUS) {
            if (data != null) {
                if (statusCode == ResponseCodes.Success) {
                    AppInstance.myDailyResponse = (MyDailyResponse) data.body();
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } else {
                    serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
            }
        } else if (tasksID == Constants.TaskID.DOWNLOAD_DAILIES) {
            if (data != null) {
                if (statusCode == ResponseCodes.Success) {
                    // AppInstance.myDailyResponse = (MyDailyResponse) data.body();
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } else {
                    serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
            }
        } else if (tasksID == Constants.TaskID.FEEDBACK) {
            if (data != null) {
                if (statusCode == ResponseCodes.Success) {
                    AppInstance.feedbackData = (FeedbackData) data.body();
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } else {
                    serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection(context.getResources().getString(R.string.invalid_message));
            }
        }

    }
}
