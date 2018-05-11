package com.smartdata.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;


import com.smartdata.async.DownloadMediaFiles;
import com.smartdata.dataobject.DownloadedMedia;
import com.smartdata.interfaces.ServiceRedirection;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;

import java.util.ArrayList;

import media.MediaUtility;

/**
 * Created by Anurag Sethi on 27-05-2015.
 */
public class GridViewAdapter extends BaseAdapter {

    private Context contextObj;
    private int layoutResourceId;
    private ArrayList data = new ArrayList();
    private ArrayList dataType = new ArrayList();
    DownloadMediaFiles downloadMediaFilesObj;
    MediaUtility mediaUtilityObj;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param data     The array of the media file data.
     * @param dataType The array of the media file types
     */
    public GridViewAdapter(Context context, int resource, ArrayList data, ArrayList dataType, MediaUtility mediaUtilityObj) {
        this.contextObj = context;
        this.layoutResourceId = resource;
        this.data = data;
        this.dataType = dataType;
        this.mediaUtilityObj = mediaUtilityObj;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return data.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity) contextObj).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) row.findViewById(R.id.imageView1);
            holder.playicon = (ImageView) row.findViewById(R.id.playIcon);
            holder.progressBarObj = (ProgressBar) row.findViewById(R.id.progressBar1);
            holder.image.setVisibility(View.GONE);
            holder.playicon.setVisibility(View.GONE);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        holder.image.setTag(data.get(position));
        holder.image.setId(position);

        DownloadedMedia downloadedMediaObj = new DownloadedMedia();
        downloadedMediaObj.imageViewObj = holder.image;
        downloadedMediaObj.progressBarObj = holder.progressBarObj;
        downloadedMediaObj.playIconObj = holder.playicon;
        downloadedMediaObj.mediaTypeObj = this.dataType.get(position).toString();

        downloadMediaFilesObj = new DownloadMediaFiles(this.contextObj, data.get(position).toString(), this.mediaUtilityObj, downloadedMediaObj, Constants.Media.GALLERY_MEDIA_DISPLAY_WIDTH, Constants.Media.GALLERY_MEDIA_DISPLAY_HEIGHT);
        downloadMediaFilesObj.execute();
        return row;
    }

    public class ViewHolder {
        ImageView image;
        ImageView playicon;
        String mediaType;
        ProgressBar progressBarObj;
    }

}
