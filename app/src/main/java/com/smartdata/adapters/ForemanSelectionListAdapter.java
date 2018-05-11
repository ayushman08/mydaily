package com.smartdata.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.smartdata.dataobject.ForemanDataList;
import com.smartdata.mydaily.R;
import com.smartdata.utils.ForemanListRowHolder;

import java.util.ArrayList;

public class ForemanSelectionListAdapter extends ArrayAdapter<ForemanDataList> {
    private LayoutInflater layoutInflater;
    private ArrayList<ForemanDataList> listData;
    private ForemanDataList mForemanDataList;

    public ForemanSelectionListAdapter(Context context, int textViewResourceId,
                                       ArrayList<ForemanDataList> listData) {
        super(context, textViewResourceId, listData);
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }

    @SuppressLint("InflateParams")
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ForemanListRowHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(
                    R.layout.foreman_selection_list_item, null);
            holder = new ForemanListRowHolder();
            holder.userNameTextView = (TextView) convertView.findViewById(R.id.user_name_text_view);
            holder.userLocationTextView = (TextView) convertView.findViewById(R.id.user_location_text_view);
            holder.userJobFunctionTextView = (TextView) convertView.findViewById(R.id.user_job_text_view);
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

    public void setDataList(ArrayList<ForemanDataList> dataList) {
        this.listData = dataList;
    }
}