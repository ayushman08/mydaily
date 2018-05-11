package com.smartdata.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.smartdata.dataobject.CrewsDataList;
import com.smartdata.dataobject.JobDataList;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.ForemanListRowHolder;
import com.smartdata.utils.JobListRowHolder;
import com.smartdata.utils.Utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SupervisorJobListAdapter extends ArrayAdapter<JobDataList> {
    private LayoutInflater layoutInflater;
    private ArrayList<JobDataList> listData;
    private ArrayList<JobDataList> originalListData;
    private JobDataList jobDataList;
    private Utility mUtility;
    private JobListFilter mFilter;

    public SupervisorJobListAdapter(Context context, int textViewResourceId,
                                    ArrayList<JobDataList> listData) {
        super(context, textViewResourceId, listData);
        this.listData = listData;
        this.originalListData = new ArrayList<>();
        this.originalListData.addAll(listData);
        layoutInflater = LayoutInflater.from(context);
        mUtility = new Utility(context);
    }

    public void setDataList(ArrayList<JobDataList> dataList) {
        this.listData = dataList;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new JobListFilter();
        }
        return mFilter;
    }

    @SuppressLint("InflateParams")
    public View getView(final int position, View convertView, ViewGroup parent) {
        final JobListRowHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(
                    R.layout.supervisor_job_list_item, null);
            holder = new JobListRowHolder();
            holder.userNameTextView = (TextView) convertView.findViewById(R.id.user_name_text_view);
            holder.userLocationTextView = (TextView) convertView.findViewById(R.id.user_location_text_view);
            holder.userJobFunctionTextView = (TextView) convertView.findViewById(R.id.user_job_text_view);
            holder.mJobIDTextView = (TextView) convertView.findViewById(R.id.job_id_text_view);
            convertView.setTag(holder);
        } else {
            holder = (JobListRowHolder) convertView.getTag();
        }

        jobDataList = listData.get(position);
        try {
            if (jobDataList.getJobAssignTo().getClient() != null) {
                holder.userNameTextView.setText(jobDataList.getJobAssignTo().getClient() + "");
            } else {
                holder.userNameTextView.setText("");
            }
            if (jobDataList.getJobAssignTo().getJob_location() != null) {
                holder.userLocationTextView.setText(jobDataList.getJobAssignTo().getJob_location() + "");
            } else {
                holder.userLocationTextView.setText("");
            }
            if (jobDataList.getJobAssignTo().getJob_id() != null) {
                holder.mJobIDTextView.setText(jobDataList.getJobAssignTo().getJob_id() + "");
            } else {
                holder.mJobIDTextView.setText("");
            }
            if (jobDataList.getJobAssignTo().getStart_date() != null) {
                holder.userJobFunctionTextView.setText(mUtility.changeDate(jobDataList.getJobAssignTo().getStart_date()));
            } else {
                holder.userJobFunctionTextView.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private class JobListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();

            FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<JobDataList> filteredItems = new ArrayList<>();

                for (int i = 0, l = originalListData.size(); i < l; i++) {
                    JobDataList jobDataList = originalListData.get(i);
                    if (jobDataList.getJobAssignTo().toString().toLowerCase().contains(constraint))
                        filteredItems.add(jobDataList);
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

            listData = (ArrayList<JobDataList>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = listData.size(); i < l; i++)
                add(listData.get(i));
            notifyDataSetInvalidated();
        }
    }
}