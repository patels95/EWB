package com.patels95.sanam.ewb.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.patels95.sanam.ewb.ui.ResourceFragment;
import com.patels95.sanam.ewb.ui.TaskFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new TaskFragment();
        }
        else {
            return new ResourceFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
