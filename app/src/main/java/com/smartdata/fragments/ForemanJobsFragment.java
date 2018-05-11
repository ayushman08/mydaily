package com.smartdata.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartdata.activities.DashboardActivity;
import com.smartdata.activities.SettingActivity;
import com.smartdata.adapters.ForemanJobsPagerAdapter;
import com.smartdata.adapters.SubmittalPagerAdapter;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Utility;

public class ForemanJobsFragment extends BaseFragment {

    private final static String TAG = ForemanJobsFragment.class.getSimpleName();
    private View mView;
    // The ViewPager is responsible for sliding pages (fragments) in and out upon user input
    private ViewPager mViewPager;
    private Utility mUtility;

    //custom toolbar components
    private Toolbar mToolbar;
    private ImageView mSettingImageView, mClearImageView, mSearchImageView;
    private LinearLayout mSearchLinearLayout;
    private TextView mTitleTextView;
    private EditText mSearchIncidentEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_foreman_jobs, container, false);
        setToolbar();
        initViews();
        return mView;
    }


    private void initViews() {
        mUtility = new Utility(getActivity());
        mViewPager = (ViewPager) mView.findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(new ForemanJobsPagerAdapter(getFragmentManager(), getActivity()));
        TabLayout tabLayout = (TabLayout) mView.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public void setToolbar() {
        mToolbar = (Toolbar) mView.findViewById(R.id.search_actionbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mSettingImageView = (ImageView) mView.findViewById(R.id.action_settings);
        mSearchImageView = (ImageView) mView.findViewById(R.id.search_view);
        mClearImageView = (ImageView) mView.findViewById(R.id.clear_view);
        mSearchLinearLayout = (LinearLayout) mView.findViewById(R.id.searchLinear);
        mTitleTextView = (TextView) mView.findViewById(R.id.title);
        mTitleTextView.setText(getActivity().getResources().getString(R.string.my_jobs));
        mSearchIncidentEditText = (EditText) mView.findViewById(R.id.search_et);
        mSearchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mToolbar.setVisibility(View.GONE);
                mSearchLinearLayout.setVisibility(View.VISIBLE);
                mSearchIncidentEditText.requestFocus();
                mUtility.showVirtualKeyboard(getActivity());
            }
        });

        mClearImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchIncidentEditText.getText().toString().isEmpty()) {
                    mSearchLinearLayout.setVisibility(View.GONE);
                    mToolbar.setVisibility(View.VISIBLE);
                    mSearchIncidentEditText.clearFocus();
                    mUtility.hiddenInputMethod(getActivity());
                } else {
                    mSearchIncidentEditText.setText("");
                }
            }
        });

        mSettingImageView.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                Intent intentObj = new Intent(getActivity(), SettingActivity.class);
                startActivity(intentObj);
            }
        });

        mSearchIncidentEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                ForemanJobsPagerAdapter pagerAdapter = (ForemanJobsPagerAdapter) mViewPager.getAdapter();
                for (int i = 0; i < pagerAdapter.getCount(); i++) {

                    Fragment viewPagerFragment = (Fragment) mViewPager.getAdapter().instantiateItem(mViewPager, i);
                    if (viewPagerFragment != null && viewPagerFragment.isAdded()) {

                        if (viewPagerFragment instanceof CompletedForemanJobsFragment) {
                            CompletedForemanJobsFragment completedForemanJobsFragment = (CompletedForemanJobsFragment) viewPagerFragment;
                            if (completedForemanJobsFragment != null) {
                                completedForemanJobsFragment.onSearch(s.toString()); // Calling the method beginSearch of GroupsFragment
                            }
                        } else if (viewPagerFragment instanceof ActiveForemanJobsFragment) {
                            ActiveForemanJobsFragment activeForemanJobsFragment = (ActiveForemanJobsFragment) viewPagerFragment;
                            if (activeForemanJobsFragment != null) {
                                activeForemanJobsFragment.onSearch(s.toString()); // Calling the method beginSearch of ChatFragment
                            }
                        }
                    }
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
