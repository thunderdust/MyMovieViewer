package com.example.weiranliu.mymovieviewer;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;




public class DashboardActivity extends FragmentActivity {

    MovieViewerPagerAdapter mAdapter;
    ViewPager mViewPager;
    final int TAB_COUNT = 2;
    final String[] PAGE_NAMES = {"Now Showing", "My Favorite"};
    private static final String API_KEY = "672dddea9cff27c1f8b77648cceee804";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mAdapter = new MovieViewerPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                getActionBar().setSelectedNavigationItem(position);
            }
        });
        // Default page is showing movies
        mViewPager.setCurrentItem(0);

        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
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

        for (int i = 0; i < TAB_COUNT; i++) {
            actionBar.addTab(actionBar.newTab().setText(PAGE_NAMES[i]).setTabListener(tabListener));
        }
    }

    public void getShowingMovies() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://api.themoviedb.org").addConverterFactory(GsonConverterFactory.create()).build();
        MovieService ms = retrofit.create(MovieService.class);
        Call<MovieList> call = ms.loadMovies(API_KEY);
        call.enqueue(new Callback<MovieList>() {
                         @Override
                         public void onResponse(Response<MovieList> response, Retrofit retrofit) {
                             int statusCode = response.code();
                             MovieList list = response.body();
                         }

                         @Override
                         public void onFailure(Throwable t) {
                             Toast.makeText(DashboardActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                         }
                     }
        );
    }
}
