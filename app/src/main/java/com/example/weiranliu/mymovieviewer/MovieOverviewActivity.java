package com.example.weiranliu.mymovieviewer;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class MovieOverviewActivity extends AppCompatActivity {

    private ImageView mMovieCover;
    private TextView mMovieTitle;
    private TextView mMovieTagline;
    private ImageButton mMovieDetailButton;
    private ImageButton mRelatedButton;
    private ImageButton mFavoriteButton;
    private TextView mMovieDescription;

    private Picasso picasso;
    private final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";

    private MovieService ms;
    private final String API_KEY = "672dddea9cff27c1f8b77648cceee804";
    private final String DEBUG_TAG = "Movie Overview";
    private int mMovieId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_overview);
        // Load movie data by passed id
        Intent i = getIntent();
        mMovieId = i.getIntExtra("id", 0);

        mMovieCover = (ImageView) findViewById(R.id.iv_movie_cover);
        mMovieTitle = (TextView) findViewById(R.id.tv_movie_title);
        mMovieTagline = (TextView) findViewById(R.id.tv_movie_tagline);
        mMovieDescription = (TextView) findViewById(R.id.tv_movie_description);

        Gson gson = (new GsonBuilder()).create();
        ms = (new RestAdapter.Builder()
                .setEndpoint("http://api.themoviedb.org/3/")
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("NETWORK"))
                .build()).create(MovieService.class);
        getMovieDetails(mMovieId);
        setButtons();

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
        mMovieTitle.setText(title);

        String tagLine = m.tagline;
        mMovieTagline.setText(tagLine);

        String description = m.overview;
        mMovieDescription.setText(description);

        this.picasso = (new Picasso.Builder(this)).build();
        String imageName = m.backdrop_path;
        Picasso.with(this).load(IMAGE_BASE_URL + imageName).placeholder(R.drawable.place_holder).
                error(R.drawable.error_loading).fit().centerCrop().into(mMovieCover);
    }

    private void setButtons(){

        mMovieDetailButton = (ImageButton)findViewById(R.id.btn_movie_details);
        mRelatedButton = (ImageButton) findViewById(R.id.btn_movie_related);
        mFavoriteButton = (ImageButton) findViewById(R.id.btn_movie_favorite);

        mMovieDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMovieDetail = new Intent(v.getContext(), MovieDetailActivity.class);
                toMovieDetail.putExtra("id", mMovieId);
                v.getContext().startActivity(toMovieDetail);
            }
        });

    }
}
