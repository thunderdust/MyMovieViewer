package com.example.weiranliu.mymovieviewer;


import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;

/**
 * Created by weiran.liu on 11/30/2015.
 */
public class MovieViewerPagerAdapter extends FragmentStatePagerAdapter{

    private static final int PAGE_COUNT = 2;
    private static final String[] PAGE_NAMES = {"Now Showing", "My Favorite"};
    public MovieViewerPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int i){
        Fragment f = new MovieViewerFragment();
        Bundle args = new Bundle();
        args.putInt(MovieViewerFragment.ARG_OBJECT, i+1);
        f.setArguments(args);
        return f;
    }

    @Override
    public int getCount(){
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position){
        return PAGE_NAMES[position];
    }
}
