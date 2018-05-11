package com.smartdata.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.smartdata.dataobject.CrewsDataList;
import com.smartdata.dataobject.IncidentDataList;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.CrewsListRowHolder;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CrewsListAdapter extends ArrayAdapter<CrewsDataList> {
    private LayoutInflater layoutInflater;
    private ArrayList<CrewsDataList> listData;
    private ArrayList<CrewsDataList> originalListData;
    private CrewsDataList mCrewsDataList;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private CrewsListFilter mFilter;

    public CrewsListAdapter(Context context, int textViewResourceId,
                            ArrayList<CrewsDataList> listData) {
        super(context, textViewResourceId, listData);
        this.listData = listData;
        this.originalListData = new ArrayList<>();
        this.originalListData.addAll(listData);

        this.imageLoader = ImageLoader.getInstance();
        this.imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.user)
                .showImageForEmptyUri(R.drawable.user)
                .showImageOnFail(R.drawable.user).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        layoutInflater = LayoutInflater.from(context);

    }

    public void setDataList(ArrayList<CrewsDataList> dataList) {
        this.listData = dataList;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new CrewsListFilter();
        }
        return mFilter;
    }

    @SuppressLint("InflateParams")
    public View getView(final int position, View convertView, ViewGroup parent) {
        final CrewsListRowHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(
                    R.layout.crews_list_item, null);
            holder = new CrewsListRowHolder();
            holder.userNameTextView = (TextView) convertView.findViewById(R.id.user_name_text_view);
            holder.userLocationTextView = (TextView) convertView.findViewById(R.id.user_location_text_view);
            holder.userProfileImageView = (CircleImageView) convertView.findViewById(R.id.user_profile_image_view);
            holder.userJobFunctionTextView = (TextView) convertView.findViewById(R.id.user_job_text_view);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progress_bar);
            convertView.setTag(holder);
        } else {
            holder = (CrewsListRowHolder) convertView.getTag();
        }
        mCrewsDataList = listData.get(position);

        try {
            holder.userNameTextView.setText(mCrewsDataList.getFirstname() + " " + mCrewsDataList.getLastname());
            holder.userLocationTextView.setText(mCrewsDataList.getEmail());


            if (mCrewsDataList.getCrewImageDatas() != null) {
                imageLoader.displayImage(Constants.WebServices.BASE_URL + mCrewsDataList.getCrewImageDatas().getImage_path(), holder.userProfileImageView, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        holder.progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        holder.progressBar.setVisibility(View.GONE);
                        holder.userProfileImageView.setImageResource(R.drawable.user);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        holder.progressBar.setVisibility(View.GONE);
                    }
                });
            } else {
                holder.userProfileImageView.setImageResource(R.drawable.user);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return convertView;
    }

    private class CrewsListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();

            FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<CrewsDataList> filteredItems = new ArrayList<>();

                for (int i = 0, l = originalListData.size(); i < l; i++) {
                    CrewsDataList crewsDataList = originalListData.get(i);
                    if (crewsDataList.toString().toLowerCase().contains(constraint))
                        filteredItems.add(crewsDataList);
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            } else {
                synchronized (this) {
                    result.values = originalListData;
                    result.count = originalListData.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            listData = (ArrayList<CrewsDataList>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = listData.size(); i < l; i++)
                add(listData.get(i));
            notifyDataSetInvalidated();
        }
    }
}