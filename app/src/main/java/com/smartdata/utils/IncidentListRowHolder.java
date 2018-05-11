package com.smartdata.utils;


import android.widget.ProgressBar;
import android.widget.TextView;

/*
Class to provide view references of incident list to incident adapter
*/

public class IncidentListRowHolder {
    public TextView incidentNameTextView, clientNameTextView, dateTextView, mDraftTextView;
    public RoundRectCornerImageView incidentImageView;
    public ProgressBar progressBar;
}
