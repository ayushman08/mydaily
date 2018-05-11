package com.smartdata.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.smartdata.dataobject.IncidentDataList;
import com.smartdata.dataobject.MyDailyDataList;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.JobListRowHolder;
import com.smartdata.utils.Utility;

import java.util.ArrayList;

public class MyDailiesAdapter extends ArrayAdapter<MyDailyDataList> {
    private LayoutInflater layoutInflater;
    private ArrayList<MyDailyDataList> listData;
    private ArrayList<MyDailyDataList> originalListData;
    private MyDailyDataList mMyDailyDataList;
    private Utility mUtility = null;
    private Context mContext = null;
    private MyDailyListFilter mFilter;

    public MyDailiesAdapter(Context context, int textViewResourceId,
                            ArrayList<MyDailyDataList> listData) {
        super(context, textViewResourceId, listData);
        this.listData = listData;
        this.originalListData = new ArrayList<>();
        this.originalListData.addAll(listData);
        layoutInflater = LayoutInflater.from(context);
        mUtility = new Utility(context);
        this.mContext = context;
    }

    public void setDataList(ArrayList<MyDailyDataList> dataList) {
        this.listData = dataList;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new MyDailyListFilter();
        }
        return mFilter;
    }

    @SuppressLint("InflateParams")
    public View getView(final int position, View convertView, ViewGroup parent) {
        final JobListRowHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(
                    R.layout.my_dailies_list_item, null);
            holder = new JobListRowHolder();
            holder.userNameTextView = (TextView) convertView.findViewById(R.id.user_name_text_view);
            holder.userLocationTextView = (TextView) convertView.findViewById(R.id.user_location_text_view);
            holder.userJobFunctionTextView = (TextView) convertView.findViewById(R.id.user_job_text_view);
            holder.mJobIDTextView = (TextView) convertView.findViewById(R.id.job_id_text_view);
            holder.mDraftTextView = (TextView) convertView.findViewById(R.id.draft_text_view);
            holder.mRejectedTextView = (TextView) convertView.findViewById(R.id.reject_text_view);
            convertView.setTag(holder);
        } else {
            holder = (JobListRowHolder) convertView.getTag();
        }

        mMyDailyDataList = listData.get(position);
        try {
            if (mMyDailyDataList.getFrom_date() != null) {
                holder.userNameTextView.setText(mUtility.changeDate(mMyDailyDataList.getFrom_date()));
            } else {
                holder.userNameTextView.setText("");
            }

            if (mMyDailyDataList.getStatus() != null && mMyDailyDataList.getStatus().equals(Constants.RequestCodes.REQ_CODE_DAILY_REJECTED)) {
                holder.mRejectedTextView.setText(mMyDailyDataList.getStatus());
            } else {
                holder.mRejectedTextView.setText("");
            }

            if (mMyDailyDataList.getMyDailyDetails().getJob_id() != null) {
                holder.userLocationTextView.setText(mMyDailyDataList.getMyDailyDetails().getJob_id());
            } else {
                holder.userLocationTextView.setText("");
            }
            if (mMyDailyDataList.getDaily_number() != null) {
                holder.mJobIDTextView.setText("#" + mMyDailyDataList.getDaily_number() + "");
            } else {
                holder.mJobIDTextView.setText("");
            }

            if (mMyDailyDataList.is_draft()) {
                holder.mDraftTextView.setText(mContext.getResources().getString(R.string.action_draft));
            } else {
                holder.mDraftTextView.setText("");
            }
            if (mMyDailyDataList.getBillableItemses() != null) {
                if (mMyDailyDataList.getBillableItemses().size() > 0) {
                    if (mMyDailyDataList.getBillableItemses().get(0).getName() != null) {
                        holder.userJobFunctionTextView.setText(mMyDailyDataList.getBillableItemses().get(0).getName() + " | " + mMyDailyDataList.getBillableItemses().get(0).getQuantity());
                    } else {
                        holder.userJobFunctionTextView.setText("");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private class MyDailyListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();

            FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<MyDailyDataList> filteredItems = new ArrayList<>();

                for (int i = 0, l = originalListData.size(); i < l; i++) {
                    MyDailyDataList myDailyDataList = originalListData.get(i);
                    if (myDailyDataList.toString().toLowerCase().contains(constraint))
                        filteredItems.add(myDailyDataList);
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

            listData = (ArrayList<MyDailyDataList>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = listData.size(); i < l; i++)
                add(listData.get(i));
            notifyDataSetInvalidated();
        }
    }
}