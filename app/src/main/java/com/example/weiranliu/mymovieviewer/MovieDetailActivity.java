package com.example.weiranliu.mymovieviewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class MovieDetailActivity extends AppCompatActivity {

    private MovieService ms;
    private final String API_KEY = "672dddea9cff27c1f8b77648cceee804";
    private final String DEBUG_TAG = "Movie Details";
    private int mMovieId;
    private HashMap<Integer, String> mMovieGenreMap;

    // UI Components
    private TextView mGenreTextView;
    private TextView mBudgetTextView;
    private TextView mLanguageTextView;
    private ImageView mTrailerImageView1;
    private ImageView mTrailerImageView2;
    private TextView mTrailerTitleTextView;
    private GridView mTrailerGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Intent movieIntent = getIntent();
        mMovieId = movieIntent.getIntExtra("id", 0);

        mGenreTextView = (TextView) findViewById(R.id.tv_movie_genre_content);
        mBudgetTextView = (TextView) findViewById(R.id.tv_movie_budget_content);
        mLanguageTextView = (TextView) findViewById(R.id.tv_movie_language_content);
        mTrailerTitleTextView = (TextView) findViewById(R.id.tv_movie_trailers);
        mTrailerGridView = (GridView) findViewById(R.id.gv_movie_trailers);

        Gson gson = (new GsonBuilder()).create();
        ms = (new RestAdapter.Builder()
                .setEndpoint("http://api.themoviedb.org/3/")
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("NETWORK"))
                .build()).create(MovieService.class);
        mMovieGenreMap = new HashMap<Integer, String>();
        //getMovieGenres();
        getMovieDetails(mMovieId);
    }

    public void getMovieGenres() {
        Callback<ArrayList<Genre>> cb = new Callback<ArrayList<Genre>>() {
            @Override
            public void success(ArrayList<Genre> genreList, Response response) {
                for (Genre g : genreList) {
                    mMovieGenreMap.put(g.id, g.name);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DEBUG_TAG, error.getMessage());
            }
        };
        ms.getGenreList(API_KEY, cb);
    }

    public void getMovieDetails(int id) {
        Callback<Movie> cb = new Callback<Movie>() {
            @Override
            public void success(Movie movie, Response response) {
                loadMovieDetails(movie);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DEBUG_TAG, error.getMessage());
            }
        };
        ms.getMovie(id, API_KEY, cb);
    }

    public void loadMovieDetails(Movie m) {

        String title = m.title;
        setTitle(title);
        // Initiate movie trailer views only if has video
        if (m.video) {
            mTrailerTitleTextView.setVisibility(View.VISIBLE);
            mTrailerGridView.setVisibility(View.VISIBLE);
            setUpTrailers();
        }

        // Update movie genres
        String genre = "";
        for (Genre g : m.genres) {
            genre += g.name;
            genre += ", ";
        }
        // Remove the last ", "
        genre = genre.substring(0, genre.length() - 2);
        mGenreTextView.setText(genre);

        // Update budget
        mBudgetTextView.setText("$" + m.budget);

        // Update language
        String languages = "";
        for (Language l : m.spoken_languages) {
            languages += l.name;
            languages += ", ";
        }
        // Remove the last ", "
        languages = languages.substring(0, languages.length() - 2);
        mLanguageTextView.setText(languages);
    }

    private void setUpTrailers(){

    }
}
