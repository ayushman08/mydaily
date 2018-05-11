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
import com.smartdata.dataobject.IncidentDataList;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.IncidentListRowHolder;
import com.smartdata.utils.RoundRectCornerImageView;
import com.smartdata.utils.Utility;

import java.util.ArrayList;

public class IncidentListAdapter extends ArrayAdapter<IncidentDataList> {
    private LayoutInflater layoutInflater;
    private ArrayList<IncidentDataList> listData;
    private ArrayList<IncidentDataList> originalListData;
    private IncidentDataList mIncidentDataList;
    private Utility mUtility;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private Context mContext = null;
    private IncidentListFilter mFilter;

    public IncidentListAdapter(Context context, int textViewResourceId,
                               ArrayList<IncidentDataList> listData) {
        super(context, textViewResourceId, listData);

        this.listData = listData;
        this.originalListData = new ArrayList<>();
        this.originalListData.addAll(listData);

        this.imageLoader = ImageLoader.getInstance();
        this.imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_insert_photo)
                .showImageForEmptyUri(R.drawable.ic_insert_photo)
                .showImageOnFail(R.drawable.ic_insert_photo).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        layoutInflater = LayoutInflater.from(context);
        mUtility = new Utility(context);
        this.mContext = context;
    }

    public void setDataList(ArrayList<IncidentDataList> dataList) {
        this.listData = dataList;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new IncidentListFilter();
        }
        return mFilter;
    }

    @SuppressLint("InflateParams")
    public View getView(final int position, View convertView, ViewGroup parent) {
        final IncidentListRowHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(
                    R.layout.incident_list_item, null);
            holder = new IncidentListRowHolder();
            holder.incidentNameTextView = (TextView) convertView.findViewById(R.id.incident_name_text_view);
            holder.clientNameTextView = (TextView) convertView.findViewById(R.id.client_name_text_view);
            holder.dateTextView = (TextView) convertView.findViewById(R.id.date_text_view);
            holder.mDraftTextView = (TextView) convertView.findViewById(R.id.draft_text_view);
            holder.incidentImageView = (RoundRectCornerImageView) convertView.findViewById(R.id.incident_image_view);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progress_bar);
            convertView.setTag(holder);
        } else {
            holder = (IncidentListRowHolder) convertView.getTag();
        }
        mIncidentDataList = listData.get(position);

        try {
            holder.incidentNameTextView.setText(mIncidentDataList.getDamage_Report());
            holder.clientNameTextView.setText(mIncidentDataList.getOwn_It());
            if (mIncidentDataList.getDid_It_Happen() != null) {
                holder.dateTextView.setText(mUtility.formatDate(mIncidentDataList.getDid_It_Happen(), Constants.InputTag.GET_DATE_FORMAT, Constants.InputTag.GET_INCIDENT_DATE_FORMAT));
            }
            if (mIncidentDataList.is_draft()) {
                holder.mDraftTextView.setText(mContext.getResources().getString(R.string.action_draft));
            } else {
                holder.mDraftTextView.setText("");
                holder.mDraftTextView.setVisibility(View.GONE);
            }
            if (mIncidentDataList != null) {
                if (mIncidentDataList.getIncidentImages().size() > 0) {
                    imageLoader.displayImage(Constants.WebServices.BASE_URL
                            + mIncidentDataList.getIncidentImages().get(0).getImage_path(), holder.incidentImageView, options, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            holder.progressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            holder.progressBar.setVisibility(View.GONE);
                            holder.incidentImageView.setImageResource(R.drawable.ic_insert_photo);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            holder.progressBar.setVisibility(View.GONE);
                            holder.incidentImageView.setPadding(1, 1, 1, 1);
                        }
                    });
                } else {
                    holder.incidentImageView.setImageResource(R.drawable.ic_insert_photo);
                }
            } else {
                holder.incidentImageView.setImageResource(R.drawable.ic_insert_photo);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return convertView;
    }

    private class IncidentListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();

            FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<IncidentDataList> filteredItems = new ArrayList<>();

                for (int i = 0, l = originalListData.size(); i < l; i++) {
                    IncidentDataList eventDetailsModel = originalListData.get(i);
                    if (eventDetailsModel.toString().toLowerCase().contains(constraint))
                        filteredItems.add(eventDetailsModel);
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

            listData = (ArrayList<IncidentDataList>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = listData.size(); i < l; i++)
                add(listData.get(i));
            notifyDataSetInvalidated();
        }
    }

}