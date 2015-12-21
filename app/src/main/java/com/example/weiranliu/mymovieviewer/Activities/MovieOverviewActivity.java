package com.example.weiranliu.mymovieviewer.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weiranliu.mymovieviewer.DatabaseComponents.MovieContract;
import com.example.weiranliu.mymovieviewer.DatabaseComponents.MovieDbHelper;
import com.example.weiranliu.mymovieviewer.MovieRelatedClasses.Genre;
import com.example.weiranliu.mymovieviewer.MovieRelatedClasses.Language;
import com.example.weiranliu.mymovieviewer.MovieRelatedClasses.Movie;
import com.example.weiranliu.mymovieviewer.MovieRelatedClasses.MovieService;
import com.example.weiranliu.mymovieviewer.R;
import com.example.weiranliu.mymovieviewer.Utils.Toaster;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private Movie mMovie;

    private MovieDbHelper mDbHelper;
    private SQLiteDatabase mMovieDB;
    private Toaster mToaster;
    private boolean mIsMovieFavorited;

    private final String IMAGE_FOLDER_PATH = "/MyMovieViewer/img/";

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


        mDbHelper = new MovieDbHelper(this);
        mMovieDB = mDbHelper.getWritableDatabase();
        mToaster = new Toaster(this);
        mIsMovieFavorited = false;

        setButtons();
    }

    public void getMovieDetails(int id) {
        Callback<Movie> cb = new Callback<Movie>() {
            @Override
            public void success(Movie movie, Response response) {
                mMovie = movie;
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
                error(R.drawable.no_image).fit().centerCrop().into(mMovieCover);
    }

    private void setButtons() {

        mMovieDetailButton = (ImageButton) findViewById(R.id.btn_movie_details);
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

        mRelatedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSimilarMovies = new Intent(v.getContext(), SimilarMoviesActivity.class);
                toSimilarMovies.putExtra("id", mMovieId);
                v.getContext().startActivity(toSimilarMovies);
            }
        });

        // Query to check if current movie already in favorited list
        final String CHECK_QUERY = "SELECT " + MovieContract.MovieEntry.COLUMN_NAME_MOVIE_ID +
        " FROM " + MovieContract.MovieEntry.TABLE_NAME + " WHERE " + MovieContract.MovieEntry
                .COLUMN_NAME_MOVIE_ID + "='" + mMovieId + "'";
        Cursor c = mMovieDB.rawQuery(CHECK_QUERY, null);
        if (c.getCount()>0) {
            // Already in favorite list
            mIsMovieFavorited = true;
            mFavoriteButton.setImageResource(R.drawable.favorited);
        } else {
            mIsMovieFavorited = false;
            mFavoriteButton.setImageResource(R.drawable.favorite);
        }


        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsMovieFavorited) {

                    if (mMovie != null) {
                        if (saveFavoritedMovieInDB(mMovie)) {
                            saveImage(mMovie.backdrop_path);
                            //new imageSaveTask().execute(mMovie.backdrop_path);
                            mIsMovieFavorited = true;
                            mToaster.toastLong(mMovie.title + " has been added to your favorite list.");
                            mFavoriteButton.setImageResource(R.drawable.favorited);
                        } else {
                            mToaster.toastLong("Failed to add " + mMovie.title + " as favorite movie.");
                        }
                    } else {
                        Log.e(DEBUG_TAG, "Current movie is NULL, unable to save");
                        mToaster.toastLong("Error: Movie item is not found.");
                    }
                } else {
                    // delete favorited movie
                    int deletedCount = mMovieDB.delete(MovieContract.MovieEntry
                            .TABLE_NAME, MovieContract.MovieEntry.COLUMN_NAME_MOVIE_ID + "=" +
                            mMovieId, null);
                    if (deletedCount>0) {
                        mIsMovieFavorited = false;
                        mFavoriteButton.setImageResource(R.drawable.favorite);
                        mToaster.toastShort("Canceled Favorite");
                    }
                }
            }
        });
    }

    private boolean saveFavoritedMovieInDB(Movie m) {

        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_NAME_MOVIE_ID, m.id);
        values.put(MovieContract.MovieEntry.COLUMN_NAME_TITLE, (m.title != null) ? m.title : "-");
        values.put(MovieContract.MovieEntry.COLUMN_NAME_IMAGE, m.backdrop_path);
        values.put(MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW, (m.overview != null) ? m.overview : "-");
        // Use 1,0 to represent boolean value
        values.put(MovieContract.MovieEntry.COLUMN_NAME_ADULT, (m.adult) ? 1 : 0);
        values.put(MovieContract.MovieEntry.COLUMN_NAME_BUDGET, m.budget);
        String languages = "";
        // Build language string
        for (Language l : m.spoken_languages) {
            languages += l.name;
            languages += " ";
        }
        // Remove last space
        languages = languages.substring(0, languages.length() - 1);
        values.put(MovieContract.MovieEntry.COLUMN_NAME_LANGUAGE, languages);
        values.put(MovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE, (m.release_date != null) ? m.release_date : "-");
        values.put(MovieContract.MovieEntry.COLUMN_NAME_TAGLINE, (m.tagline != null) ? m.tagline : "-");
        values.put(MovieContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE, m.vote_average);
        values.put(MovieContract.MovieEntry.COLUMN_NAME_VOTE_COUNT, m.vote_count);
        values.put(MovieContract.MovieEntry.COLUMN_NAME_RUNTIME, m.runtime);
        String genres = "";
        // Build genre string
        for (Genre g : m.genres) {
            genres += g.name;
            genres += " ";
        }
        // Remove last space
        genres = genres.substring(0, genres.length() - 1);
        values.put(MovieContract.MovieEntry.COLUMN_NAME_GENRES, genres);
        // Record save date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());
        values.put(MovieContract.MovieEntry.COLUMN_NAME_SAVE_DATE, currentDate);

        // insert new row, return the primary key value of the new row
        long newRowID;
        newRowID = mMovieDB.insert(
                MovieContract.MovieEntry.TABLE_NAME,
                null,
                values
        );
        if (newRowID == -1) {
            return false;
        } else {
            return true;
        }
    }

    private void saveImage(final String imagePath) {

        Target t = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {


                File imageFolder = new File(Environment.getExternalStorageDirectory().toString() +
                        IMAGE_FOLDER_PATH);
                Log.d(DEBUG_TAG, "FOLDER PATH: " + imageFolder.getPath());
                if (!imageFolder.exists()) {
                    imageFolder.mkdirs();
                }
                File outputFile = new File(imageFolder, imagePath);
                try {
                    outputFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(outputFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };

        picasso.with(this).load(IMAGE_BASE_URL + imagePath).into(t);
    }

    private class imageSaveTask extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... paths) {
            saveImage(paths[0]);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            Log.d(DEBUG_TAG, "Image save progress: " + progress[0]);
        }

        protected void onPostExecute(Void result) {
            Log.d(DEBUG_TAG, "Imaeg save completed");
        }
    }
}
