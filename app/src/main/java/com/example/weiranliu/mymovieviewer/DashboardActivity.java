package com.example.weiranliu.mymovieviewer;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class DashboardActivity extends FragmentActivity implements FavoritedMovieFragment.OnFragmentInteractionListener, ShowingMovieFragment.OnFragmentInteractionListener {

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

        // Default page is showing movies
        mViewPager.setCurrentItem(0);
        getShowingMovies();
    }

    public void getShowingMovies() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://api.themoviedb.org/3")
                .addConverterFactory(GsonConverterFactory.create()).build();
        MovieService ms = retrofit.create(MovieService.class);
        Call<Movie> call = ms.getMovie("1", API_KEY);
        call.enqueue(new Callback<Movie>() {
                         @Override
                         public void onResponse(Response<Movie> response, Retrofit retrofit) {
                             int statusCode = response.code();
                             Log.d("DASHBOARD____________", Integer.toString(statusCode));
                             Movie m = response.body();
                         }

                         @Override
                         public void onFailure(Throwable t) {
                             Toast.makeText(DashboardActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                         }
                     }
        );


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
