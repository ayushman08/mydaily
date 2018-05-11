package com.smartdata.utils;


import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/*
Class to provide view references of attendees list to attendees adapter
*/

public class CrewsListRowHolder {
    public TextView userNameTextView, userLocationTextView, userJobFunctionTextView;
    public CircleImageView userProfileImageView;
    public EditText userNameEditText, userJobFunctionEditText;
    public ProgressBar progressBar;
}
