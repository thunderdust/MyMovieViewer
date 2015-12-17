package com.example.weiranliu.mymovieviewer.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.weiranliu.mymovieviewer.MovieRelatedClasses.Movie;
import com.example.weiranliu.mymovieviewer.MovieRelatedClasses.MovieList;
import com.example.weiranliu.mymovieviewer.MovieRelatedClasses.MovieService;
import com.example.weiranliu.mymovieviewer.MovieRelatedClasses.MovieViewAdapter;
import com.example.weiranliu.mymovieviewer.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class SimilarMoviesActivity extends AppCompatActivity {

    private Picasso picasso;
    private final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";

    private MovieService ms;
    private final String API_KEY = "672dddea9cff27c1f8b77648cceee804";
    private final String DEBUG_TAG = "Movie Overview";
    private int mMovieId;
    private ArrayList<Movie> mSimilarMovieList;
    private GridView mGridView;
    private MovieViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar_movies);

        // Load movie data by passed id
        Intent i = getIntent();
        mMovieId = i.getIntExtra("id", 0);
        setTitle("Similar Movies");

        Gson gson = (new GsonBuilder()).create();
        ms = (new RestAdapter.Builder()
                .setEndpoint("http://api.themoviedb.org/3/")
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("NETWORK"))
                .build()).create(MovieService.class);
        getSimilarMovies(mMovieId, 1);
    }

    public void getSimilarMovies(int id, int page) {
        Callback<MovieList> cb = new Callback<MovieList>() {
            @Override
            public void success(MovieList movieList, Response response) {
                mSimilarMovieList = new ArrayList<Movie>();
                for (Movie m: movieList.results){
                    mSimilarMovieList.add(m);
                }
                loadSimilarMovies(mSimilarMovieList);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DEBUG_TAG, error.getMessage());
            }
        };
        ms.getSimilarMovies(id, API_KEY, page, cb);
    }

    private void loadSimilarMovies(ArrayList<Movie> data){
        mGridView = (GridView) findViewById(R.id.gv_similar_movies);
        mAdapter = new MovieViewAdapter(this, R.layout.movie_item_layout, data);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie item = (Movie) parent.getItemAtPosition(position);
                int movieId = item.id;
                Log.d(DEBUG_TAG, "Movie ID: " + movieId);
                Intent toMovieOverviewIntent = new Intent(getBaseContext(), MovieOverviewActivity.class);
                toMovieOverviewIntent.putExtra("id", movieId);
                startActivity(toMovieOverviewIntent);
            }
        });
    }
}
