package com.gai.ewbbu.ewb.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gai.ewbbu.ewb.ui.ResourceFragment;
import com.gai.ewbbu.ewb.ui.TaskFragment;

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
