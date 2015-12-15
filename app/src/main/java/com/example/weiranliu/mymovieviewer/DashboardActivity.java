package com.example.weiranliu.mymovieviewer;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;


public class DashboardActivity extends FragmentActivity implements FavoritedMovieFragment.OnFragmentInteractionListener, ShowingMovieFragment.OnFragmentInteractionListener {

    MovieViewerPagerAdapter mAdapter;
    ViewPager mViewPager;
    final int TAB_COUNT = 2;
    final String[] PAGE_NAMES = {"Now Showing", "My Favorite"};
    final String DEBUG_TAG = "DashBoardActivity";

    Picasso picasso;
    private static final String API_KEY = "672dddea9cff27c1f8b77648cceee804";
    MovieService ms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mAdapter = new MovieViewerPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAdapter);

        Gson gson = (new GsonBuilder()).create();

        ms = (new RestAdapter.Builder()
                .setEndpoint("http://api.themoviedb.org/3/")
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("NETWORK"))
                .build()).create(MovieService.class);

        picasso =  (new Picasso.Builder(this)).build();
        // Default page is showing movies
        mViewPager.setCurrentItem(0);
        getShowingMovies();
    }

    public void getMovieById(int id) {

        Callback<Movie> callback = new Callback<Movie>() {
            @Override
            public void success(Movie movie, Response response) {
                Log.d(DEBUG_TAG, "title:"+movie.title);
                Log.d(DEBUG_TAG, "backdrop:"+movie.backdrop_path);
                Log.d(DEBUG_TAG, "overview:"+movie.overview);
                //picasso.load(movie.backdrop_link);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DEBUG_TAG, error.getMessage());
            }
        };

        ms.getMovie(id, API_KEY, callback);
    }

    public void getShowingMovies(){
        Callback<MovieList> cb = new Callback<MovieList>() {
            @Override
            public void success(MovieList movieList, Response response) {
                Log.d(DEBUG_TAG, "size:"+ movieList.results.size());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DEBUG_TAG, error.getMessage());
            }
        };
        ms.loadShowingMovies(API_KEY, cb);
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
