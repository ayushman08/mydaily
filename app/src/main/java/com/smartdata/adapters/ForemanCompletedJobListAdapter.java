package com.smartdata.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.smartdata.dataobject.ForemanJobDataList;
import com.smartdata.mydaily.R;
import com.smartdata.utils.JobListRowHolder;
import com.smartdata.utils.Utility;

import java.util.ArrayList;

public class ForemanCompletedJobListAdapter extends ArrayAdapter<ForemanJobDataList> {
    private LayoutInflater layoutInflater;
    private ArrayList<ForemanJobDataList> listData;
    private ArrayList<ForemanJobDataList> originalListData;
    private ForemanJobDataList mForemanJobDataList;
    private Utility mUtility;
    private ForemanListFilter mFilter = null;

    public ForemanCompletedJobListAdapter(Context context, int textViewResourceId,
                                          ArrayList<ForemanJobDataList> listData) {
        super(context, textViewResourceId, listData);
        this.listData = listData;
        this.originalListData = new ArrayList<>();
        this.originalListData.addAll(listData);
        layoutInflater = LayoutInflater.from(context);
        mUtility = new Utility(context);
    }

    public void setDataList(ArrayList<ForemanJobDataList> dataList) {
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
        final JobListRowHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(
                    R.layout.foreman_completed_job_list_item, null);
            holder = new JobListRowHolder();
            holder.userNameTextView = (TextView) convertView.findViewById(R.id.user_name_text_view);
            holder.userLocationTextView = (TextView) convertView.findViewById(R.id.user_location_text_view);
            holder.userJobFunctionTextView = (TextView) convertView.findViewById(R.id.user_job_text_view);
            holder.mJobIDTextView = (TextView) convertView.findViewById(R.id.job_id_text_view);
            convertView.setTag(holder);
        } else {
            holder = (JobListRowHolder) convertView.getTag();
        }
        mForemanJobDataList = listData.get(position);
        try {
            if (mForemanJobDataList.getJob_detail().getClient() != null) {
                holder.userNameTextView.setText(mForemanJobDataList.getJob_detail().getClient());
            } else {
                holder.userNameTextView.setText("");
            }

            if (mForemanJobDataList.getJob_detail().getJob_location() != null) {
                holder.userLocationTextView.setText(mForemanJobDataList.getJob_detail().getJob_location());
            } else {
                holder.userLocationTextView.setText("");
            }

            if (mForemanJobDataList.getJob_detail().getStart_date() != null) {
                holder.userJobFunctionTextView.setText(mUtility.changeDate(mForemanJobDataList.getJob_detail().getStart_date()));
                Log.e("date", mUtility.changeDate(mForemanJobDataList.getJob_detail().getStart_date()) + "formated date");
            } else {
                holder.userJobFunctionTextView.setText("");
            }
            if (mForemanJobDataList.getJob_detail().getJob_id() != null) {
                holder.mJobIDTextView.setText(mForemanJobDataList.getJob_detail().getJob_id());
            } else {
                holder.mJobIDTextView.setText("");
            }
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
                ArrayList<ForemanJobDataList> filteredItems = new ArrayList<>();

                for (int i = 0, l = originalListData.size(); i < l; i++) {
                    ForemanJobDataList foremanJobDataList = originalListData.get(i);
                    if (foremanJobDataList.getJob_detail().toString().toLowerCase().contains(constraint))
                        filteredItems.add(foremanJobDataList);
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

            listData = (ArrayList<ForemanJobDataList>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = listData.size(); i < l; i++)
                add(listData.get(i));
            notifyDataSetInvalidated();
        }
    }
}