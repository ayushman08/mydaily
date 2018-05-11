package com.smartdata.async;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.smartdata.dataobject.DownloadedMedia;
import com.smartdata.utils.Utility;
import sdei.support.lib.communication.GetWebServiceData;

import media.MediaUtility;


/**
 * Created by Anurag Sethi on 27-05-2015.
 */
public class DownloadMediaFiles extends AsyncTask<DownloadedMedia, Void, Bitmap> {

    Context contextObj;
    String url;
    Utility utilObj;
    MediaUtility mediaUtilityObj;
    GetWebServiceData getWebServiceDataObj;
    ImageView imageViewObj = null;
    ImageView playIconObj = null;
    ProgressBar progressBarObj = null;
    DownloadedMedia downloadedMediaObj;
    String mediaTypeObj;
    int scaledWidth;
    int scaledHeight;


    public DownloadMediaFiles(Context context, String Url, MediaUtility mediaUtilityObj, DownloadedMedia downloadedMediaObj, int scaledWidth, int scaledHeight) {
        this.contextObj = context;
        this.url = Url;
        this.utilObj = new Utility(context);
        this.mediaUtilityObj = mediaUtilityObj;
        this.downloadedMediaObj = downloadedMediaObj;
        this.imageViewObj =  downloadedMediaObj.imageViewObj;
        this.playIconObj = downloadedMediaObj.playIconObj;
        this.progressBarObj = downloadedMediaObj.progressBarObj;
        this.mediaTypeObj = downloadedMediaObj.mediaTypeObj;
        this.getWebServiceDataObj = new GetWebServiceData(context);
        this.scaledWidth = scaledWidth;
        this.scaledHeight = scaledHeight;
    }


    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p/>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param downloadedMediaObj The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected Bitmap doInBackground(DownloadedMedia... downloadedMediaObj) {

        Bitmap bitmap = null;
        try {
             bitmap = getWebServiceDataObj.downloadBitmapFromUrl(url);
            bitmap = mediaUtilityObj.getResizedBitMap(bitmap, this.scaledWidth, this.scaledHeight);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        this.imageViewObj.setVisibility(View.VISIBLE);
        if(this.mediaTypeObj.equalsIgnoreCase("video")) {
            this.playIconObj.setVisibility(View.VISIBLE);
        }
        else {
            this.playIconObj.setVisibility(View.GONE);
        }

        this.progressBarObj.setVisibility(View.GONE);
        this.imageViewObj.setImageBitmap(bitmap);
    }
}
