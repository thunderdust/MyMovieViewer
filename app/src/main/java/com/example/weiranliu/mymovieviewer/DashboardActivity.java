package com.example.weiranliu.mymovieviewer;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class DashboardActivity extends FragmentActivity {

    MovieViewerPagerAdapter mAdapter;
    ViewPager mViewPager;
    final int TAB_COUNT = 2;
    final String[] PAGE_NAMES = {"Now Showing", "My Favorite"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mAdapter = new MovieViewerPagerAdapter(getSupportFragmentManager());
        mViewPager =  (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position){
                getActionBar().setSelectedNavigationItem(position);
            }
        });

        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener(){
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft){
                // show the given tab
                mViewPager.setCurrentItem(tab.getPosition());
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // hide the given tab
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }
        };

        for (int i=0; i<TAB_COUNT; i++){
            actionBar.addTab(actionBar.newTab().setText(PAGE_NAMES[i]).setTabListener(tabListener));
        }
    }


}
