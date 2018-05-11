package com.smartdata.interfaces;

import com.smartdata.dataobject.CrewsData;
import com.smartdata.dataobject.FeedbackData;
import com.smartdata.dataobject.ForemanData;
import com.smartdata.dataobject.ForemanJobData;
import com.smartdata.dataobject.IncidentData;
import com.smartdata.dataobject.JobData;
import com.smartdata.dataobject.JobDataList;
import com.smartdata.dataobject.Media;
import com.smartdata.dataobject.MediaGallery;

import com.smartdata.dataobject.MyDailyData;
import com.smartdata.dataobject.MyDailyResponse;
import com.smartdata.dataobject.ProductData;
import com.smartdata.dataobject.ProductDataList;
import com.smartdata.dataobject.ResponseData;
import com.smartdata.dataobject.SupervisorAddJobData;
import com.smartdata.dataobject.SupervisorData;
import com.smartdata.dataobject.SupervisorJobDetails;
import com.smartdata.dataobject.User;

import com.smartdata.utils.Constants;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ashutoshm on 19/7/16.
 * The interface method implemented in the java(Manager) files
 */
public interface ApiInterface {


    /**
     * For post method  use @POST
     *
     * @return
     * @Body User model class data
     */

    @POST(Constants.WebServices.WS_USER_AUTHENTICATION)
    Call<User> loginUser(@Body User user);

    @POST(Constants.WebServices.WS_USER_FORGOT_PASSWORD)
    Call<User> forgotPassword(@Body User user);

    @POST(Constants.WebServices.WS_USER_LOGOUT)
    Call<User> userLogout(@Body User user);


    @POST(Constants.WebServices.WS_FOREMAN_LIST)
    Call<ForemanData> getForemanList(@Body ForemanData foremanData);

    @POST(Constants.WebServices.WS_FEEDBACK)
    Call<FeedbackData> feedback(@Body FeedbackData feedbackData);

    @POST(Constants.WebServices.WS_CREWS_LIST)
    Call<CrewsData> getCrewsList(@Body CrewsData crewsData);

    @POST(Constants.WebServices.WS_SUPERVISOR_LIST)
    Call<SupervisorData> getSupervisorList(@Body SupervisorData supervisorData);

    @POST(Constants.WebServices.WS_GET_INCIDENT_LIST)
    Call<IncidentData> getIncidentList(@Body IncidentData incidentData);

    @POST(Constants.WebServices.WS_ADD_UPDATE_CREW)
    Call<MyDailyResponse> addCrew(@Body CrewsData crewsData);

    @POST(Constants.WebServices.WS_JOB_LIST)
    Call<JobData> getJobList(@Body JobData jobData);

    @POST(Constants.WebServices.WS_MY_DAILIES)
    Call<MyDailyData> getMyDailies(@Body MyDailyData myDailyData);

    @POST(Constants.WebServices.WS_DOWNLOAD_DAILIES)
    Call<Void> downloadDailies(@Body User user);

    @POST(Constants.WebServices.WS_UPDATE_DAILY_STATUS)
    Call<MyDailyResponse> updateDailyStatus(@Body MyDailyData myDailyData);

    @POST(Constants.WebServices.WS_ADD_MY_DAILIES)
    Call<MyDailyResponse> addNewDaily(@Body MyDailyData myDailyData);


    @Multipart
    @POST(Constants.WebServices.WS_ADD_MY_DAILIES_IMAGES)
    Call<ResponseData> uploadDailiesImage(@Part MultipartBody.Part file, @Part("id")
            RequestBody id);

    @Multipart
    @POST(Constants.WebServices.WS_ADD_INCIDENT_IMAGES)
    Call<ResponseData> uploadIncidentImage(@Part MultipartBody.Part file, @Part("id")
            RequestBody id);

    @Multipart
    @POST(Constants.WebServices.WS_ADD_INCIDENT_IMAGES)
    Call<ResponseData> updateIncidentImage(@Part MultipartBody.Part file, @Part("id")
            RequestBody id, @Part("_id") RequestBody _id);

    @Multipart
    @POST(Constants.WebServices.WS_ADD_CREW_IMAGES)
    Call<ResponseData> uploadCrewImage(@Part MultipartBody.Part file, @Part("id")
            RequestBody id);

    @GET(Constants.WebServices.WS_PRODUCT_LIST)
    Call<ProductData> getProductList(@Path("id") String id);

    @POST(Constants.WebServices.WS_GET_BILLABLE_ITEM)
    Call<ProductData> getBillableItem(@Body ProductDataList productDataList);

    @GET(Constants.WebServices.WS_JOB_DETAIL)
    Call<SupervisorJobDetails> getJobDetails(@Path("id") String id);

    @DELETE(Constants.WebServices.WS_DELETE_CREW)
    Call<ResponseData> deleteCrew(@Path("id") String id);

    @DELETE(Constants.WebServices.WS_DELETE_DAILY)
    Call<ResponseData> deleteDaily(@Path("id") String id);

    @DELETE(Constants.WebServices.WS_DELETE_INCIDENT)
    Call<ResponseData> deleteIncident(@Path("id") String id);

    @DELETE(Constants.WebServices.WS_DELETE_JOB)
    Call<ResponseData> deleteJob(@Path("id") String id);

    @POST(Constants.WebServices.WS_ADD_JOB)
    Call<SupervisorAddJobData> addNewJob(@Body SupervisorAddJobData supervisorAddJobData);

    @POST(Constants.WebServices.WS_FOREMAN_JOB_LIST)
    Call<ForemanJobData> getForemanJobList(@Body ForemanJobData foremanJobData);

    @POST(Constants.WebServices.WS_MEDIA_UPLOAD_TO_SERVER)
    Call<String> mediaUploadWebService(@Body String filePath);


    @POST(Constants.WebServices.WS_MEDIA_DELETE_FROM_SERVER)
    Call<Media> mediaDeleteWebService(@Body Media mediaObj);

    @POST(Constants.WebServices.WS_ADD_UPDATE_INCIDENT)
    Call<MyDailyResponse> addNewIncident(@Body IncidentData incidentData);

    @POST(Constants.WebServices.WS_MEDIA_DOWNLOAD_FROM_SERVER)
    Call<MediaGallery> mediaDownloadWebService(@Body MediaGallery mediaGalleryObj);


    /**
     * For post method  use @POST
     *
     * @return
     * @Body User model class data
     */

    @POST(Constants.WebServices.WS_SWIPE_REFRESH_EXAMPLE_URL)
    Call<String> getSwipeRefresh(@Body String swipeRefresh);


}