package com.example.weiranliu.mymovieviewer.Adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.weiranliu.mymovieviewer.Fragments.FavoritedMovieFragment;
import com.example.weiranliu.mymovieviewer.Fragments.ShowingMovieFragment;

/**
 * Created by weiran.liu on 11/30/2015.
 */
public class MovieViewerPagerAdapter extends FragmentStatePagerAdapter {

    private static final int PAGE_COUNT = 2;
    private static final String[] PAGE_NAMES = {"Now Showing", "My Favorite"};

    public MovieViewerPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ShowingMovieFragment.newInstance(0, PAGE_NAMES[0]);
            case 1:
                return FavoritedMovieFragment.newInstance(1, PAGE_NAMES[1]);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return PAGE_NAMES[position];
    }
}
