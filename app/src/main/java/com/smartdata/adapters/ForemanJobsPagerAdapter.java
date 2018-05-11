package com.smartdata.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.smartdata.fragments.ActiveForemanJobsFragment;
import com.smartdata.fragments.CompletedForemanJobsFragment;
import com.smartdata.mydaily.R;

/**
 * Created by Vishwanath Nahak on 7/11/2017.
 */

 /* PagerAdapter for supplying the ViewPager with the pages (fragments) to display. */
public class ForemanJobsPagerAdapter extends FragmentStatePagerAdapter {
    // Titles of the individual pages (displayed in tabs)
    private String[] PAGE_TITLES;

    // The fragments that are used as the individual pages
    private final Fragment[] PAGES = new Fragment[]{
            new ActiveForemanJobsFragment(),
            new CompletedForemanJobsFragment()
    };

    public ForemanJobsPagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        PAGE_TITLES = context.getResources().getStringArray(R.array.foreman_job_pager_item);
    }

    @Override
    public Fragment getItem(int position) {
        return PAGES[position];
    }

    @Override
    public int getCount() {
        return PAGES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return PAGE_TITLES[position];
    }

}