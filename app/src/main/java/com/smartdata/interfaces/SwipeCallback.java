package com.smartdata.interfaces;

import android.widget.ListView;

/**
 * Created by Anurag Sethi on 09-07-2015.
 */
public interface SwipeCallback {

    void performSecondAction(int position);

    void performFirstAction(int position);

    void OnClickListView(int position);

    void onDismiss(ListView listView, int reverseSortedPositions);
}
