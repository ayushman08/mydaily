package com.smartdata.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.smartdata.dataobject.ProductDataList;
import com.smartdata.mydaily.R;
import com.smartdata.utils.ForemanListRowHolder;

import java.util.ArrayList;

public class ProductionListAdapter extends ArrayAdapter<ProductDataList> {
    private LayoutInflater layoutInflater;
    private ArrayList<ProductDataList> listData;
    private ProductDataList mProductDataList;

    public ProductionListAdapter(Context context, int textViewResourceId,
                                 ArrayList<ProductDataList> listData) {
        super(context, textViewResourceId, listData);
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }

    @SuppressLint("InflateParams")
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ForemanListRowHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(
                    R.layout.product_selection_list_item, null);
            holder = new ForemanListRowHolder();
            holder.userNameTextView = (TextView) convertView.findViewById(R.id.product_name_text_view);

            convertView.setTag(holder);
        } else {
            holder = (ForemanListRowHolder) convertView.getTag();
        }
        mProductDataList = listData.get(position);
        try {
            holder.userNameTextView.setText(mProductDataList.getName() + "");
        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }

    public void setDataList(ArrayList<ProductDataList> dataList) {
        this.listData = dataList;
    }
}