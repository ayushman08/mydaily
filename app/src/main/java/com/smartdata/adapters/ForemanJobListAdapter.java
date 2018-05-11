package com.smartdata.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.smartdata.dataobject.ForemanJobDataList;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.JobListRowHolder;
import com.smartdata.utils.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ForemanJobListAdapter extends ArrayAdapter<ForemanJobDataList> {
    private LayoutInflater layoutInflater;
    private ArrayList<ForemanJobDataList> listData;
    private ForemanJobDataList mForemanJobDataList;
    private Utility mUtility;

    public ForemanJobListAdapter(Context context, int textViewResourceId,
                                 ArrayList<ForemanJobDataList> listData) {
        super(context, textViewResourceId, listData);
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
        mUtility = new Utility(context);
    }

    @SuppressLint("InflateParams")
    public View getView(final int position, View convertView, ViewGroup parent) {
        final JobListRowHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(
                    R.layout.foreman_job_list_item, null);
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
            if (mForemanJobDataList.getJob_detail().getDescription() != null) {
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
}