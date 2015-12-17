package com.example.weiranliu.mymovieviewer.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.example.weiranliu.mymovieviewer.Fragments.FavoritedMovieFragment;
import com.example.weiranliu.mymovieviewer.Adapters.MovieViewerPagerAdapter;
import com.example.weiranliu.mymovieviewer.R;
import com.example.weiranliu.mymovieviewer.Fragments.ShowingMovieFragment;


public class DashboardActivity extends FragmentActivity implements FavoritedMovieFragment.OnFragmentInteractionListener, ShowingMovieFragment.OnFragmentInteractionListener {

    MovieViewerPagerAdapter mAdapter;
    ViewPager mViewPager;
    final int TAB_COUNT = 2;
    final String[] PAGE_NAMES = {"Now Showing", "My Favorite"};
    final String DEBUG_TAG = "DashBoardActivity";

    private static final String API_KEY = "672dddea9cff27c1f8b77648cceee804";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mAdapter = new MovieViewerPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAdapter);
        // Default page is showing movies
        mViewPager.setCurrentItem(0);
    }

    @Override
    public void onFavoritedFragmentInteraction(String s) {
        // do stuff
    }

    @Override
    public void onShowingMovieFragmentInteraction(String s) {
        // do stuff
    }
}
