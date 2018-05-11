package com.smartdata.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.smartdata.dataobject.CrewsDataList;
import com.smartdata.dataobject.ForemanData;
import com.smartdata.dataobject.ForemanDataList;
import com.smartdata.dialogs.AlertDialogManager;
import com.smartdata.mydaily.R;
import com.smartdata.utils.ForemanListRowHolder;

import java.util.ArrayList;

public class ForemanListAdapter extends ArrayAdapter<ForemanDataList> {
    private LayoutInflater layoutInflater;
    private ArrayList<ForemanDataList> listData;
    private ArrayList<ForemanDataList> originalListData;
    private ForemanDataList mForemanDataList;

    private ForemanListFilter mFilter;

    public ForemanListAdapter(Context context, int textViewResourceId,
                              ArrayList<ForemanDataList> listData) {
        super(context, textViewResourceId, listData);
        this.listData = listData;
        this.originalListData = new ArrayList<>();
        this.originalListData.addAll(listData);

        layoutInflater = LayoutInflater.from(context);
    }

    public void setDataList(ArrayList<ForemanDataList> dataList) {
        this.listData = dataList;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ForemanListFilter();
        }
        return mFilter;
    }

    @SuppressLint("InflateParams")
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ForemanListRowHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(
                    R.layout.foreman_list_item, null);
            holder = new ForemanListRowHolder();
            holder.userNameTextView = (TextView) convertView.findViewById(R.id.user_name_text_view);
            holder.userLocationTextView = (TextView) convertView.findViewById(R.id.user_location_text_view);
            holder.userProfileImageView = (ImageView) convertView.findViewById(R.id.user_profile_image_view);
            holder.userJobFunctionTextView = (TextView) convertView.findViewById(R.id.user_job_text_view);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progress_bar);
            convertView.setTag(holder);
        } else {
            holder = (ForemanListRowHolder) convertView.getTag();
        }
        mForemanDataList = listData.get(position);
        try {
            holder.userNameTextView.setText(mForemanDataList.getFirstname() + " " + mForemanDataList.getLastname());
            holder.userLocationTextView.setText(mForemanDataList.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private class ForemanListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();

            FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<ForemanDataList> filteredItems = new ArrayList<>();

                for (int i = 0, l = originalListData.size(); i < l; i++) {
                    ForemanDataList foremanDataList = originalListData.get(i);
                    if (foremanDataList.toString().toLowerCase().contains(constraint))
                        filteredItems.add(foremanDataList);
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

            listData = (ArrayList<ForemanDataList>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = listData.size(); i < l; i++)
                add(listData.get(i));
            notifyDataSetInvalidated();
        }
    }
}