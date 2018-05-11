package com.smartdata.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.smartdata.dataobject.MyDailyDataList;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.JobListRowHolder;
import com.smartdata.utils.Utility;

import java.util.ArrayList;

public class SubmittalAdapter extends ArrayAdapter<MyDailyDataList> {
    private LayoutInflater layoutInflater;
    private ArrayList<MyDailyDataList> listData;
    private ArrayList<MyDailyDataList> originalListData;
    private MyDailyDataList mMyDailyDataList;
    private Utility mUtility = null;
    private Context mContext = null;
    private MyDailyListFilter mFilter;

    public SubmittalAdapter(Context context, int textViewResourceId,
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

    @SuppressLint({"InflateParams", "SetTextI18n"})
    public View getView(final int position, View convertView, ViewGroup parent) {
        final JobListRowHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(
                    R.layout.submittal_list_item, null);
            holder = new JobListRowHolder();
            holder.superwiserTextView  = (TextView) convertView.findViewById(R.id.superwiser_text_view);
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

            if (mMyDailyDataList.getForemanDetails() != null) {
                holder.userNameTextView.setText(mMyDailyDataList.getForemanDetails().getFirstname() + " " + mMyDailyDataList.getForemanDetails().getLastname());
            } else {
                holder.userNameTextView.setText("");
            }

            if (mMyDailyDataList.getSupervisorDetails() != null) {
//                String a  = mMyDailyDataList.getContractorDetails().getId();
                holder.superwiserTextView.setText(mMyDailyDataList.getContractorDetails().getFirstname()+ " " + mMyDailyDataList.getContractorDetails().getLastname());
            } else {
                holder.superwiserTextView.setText("");
            }


            if (mMyDailyDataList.getMyDailyDetails() != null) {
                holder.userLocationTextView.setText(mMyDailyDataList.getMyDailyDetails().getJob_id());
            } else {
                holder.userLocationTextView.setText("");
            }

            if (mMyDailyDataList.getFrom_date() != null) {
                holder.userJobFunctionTextView.setText(mUtility.changeDate(mMyDailyDataList.getFrom_date()));
            } else {
                holder.userJobFunctionTextView.setText("");
            }

            if (mMyDailyDataList.getDaily_number() != null) {
                holder.mJobIDTextView.setText("#" + mMyDailyDataList.getDaily_number() + "");
            } else {
                holder.mJobIDTextView.setText("");
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