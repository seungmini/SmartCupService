package com.example.lageder.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import tabview.GraphFragment;
import tabview.ProfileFragment;
import tabview.StatusFragment;

/**
 * Created by Lageder on 2016-07-23.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ProfileFragment();
        }
        else if(position == 1){
            return new GraphFragment();
        }
        else if(position == 2){
            return new StatusFragment();
        }
        return null;
    }


    @Override
    public int getCount() {
        return 3;
    }
}
