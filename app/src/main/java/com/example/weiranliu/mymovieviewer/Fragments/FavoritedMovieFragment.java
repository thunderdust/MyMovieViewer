package com.example.weiranliu.mymovieviewer.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.example.weiranliu.mymovieviewer.Activities.MovieOverviewActivity;
import com.example.weiranliu.mymovieviewer.DatabaseComponents.MovieContract;
import com.example.weiranliu.mymovieviewer.DatabaseComponents.MovieDbHelper;
import com.example.weiranliu.mymovieviewer.MovieRelatedClasses.Genre;
import com.example.weiranliu.mymovieviewer.MovieRelatedClasses.Language;
import com.example.weiranliu.mymovieviewer.MovieRelatedClasses.Movie;
import com.example.weiranliu.mymovieviewer.MovieRelatedClasses.MovieViewAdapter;
import com.example.weiranliu.mymovieviewer.R;
import com.example.weiranliu.mymovieviewer.Utils.Toaster;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShowingMovieFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShowingMovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritedMovieFragment extends Fragment {

    // the fragment initialization parameters
    private static final String FRAGMENT_PAGE_COUNT = "page";
    private static final String FRAGMENT_TITLE = "title";
    private int mFragmentPageCount;
    private String mFragmentTitle;
    private OnFragmentInteractionListener mListener;

    private Toaster mToaster;
    private GridView mGridView;
    private MovieViewAdapter mAdapter;
    private ArrayList<Movie> mMovieList;
    private int mLoadedPageCount = 0;
    private MovieDbHelper mDbHelper;
    private SQLiteDatabase mMovieDB;
    // Fields to be loaded from DB
    private final String[] projection = {
            MovieContract.MovieEntry.COLUMN_NAME_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_NAME_TITLE,
            MovieContract.MovieEntry.COLUMN_NAME_IMAGE,
            MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_NAME_ADULT,
            MovieContract.MovieEntry.COLUMN_NAME_BUDGET,
            MovieContract.MovieEntry.COLUMN_NAME_GENRES,
            MovieContract.MovieEntry.COLUMN_NAME_LANGUAGE,
            MovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_NAME_RUNTIME,
            MovieContract.MovieEntry.COLUMN_NAME_TAGLINE,
            MovieContract.MovieEntry.COLUMN_NAME_SAVE_DATE,
            MovieContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE,
            MovieContract.MovieEntry.COLUMN_NAME_VOTE_COUNT
    };
    private final String API_KEY = "672dddea9cff27c1f8b77648cceee804";
    private final String DEBUG_TAG = "Favorited-Movie";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param page  Parameter 1.
     * @param title Parameter 2.
     * @return A new instance of fragment ShowingMovieFragment.
     */
    public static FavoritedMovieFragment newInstance(int page, String title) {
        FavoritedMovieFragment fragment = new FavoritedMovieFragment();
        Bundle args = new Bundle();
        args.putInt(FRAGMENT_PAGE_COUNT, page);
        args.putString(FRAGMENT_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(DEBUG_TAG, "ON Creating fragment");
        if (getArguments() != null) {
            mFragmentPageCount = getArguments().getInt(FRAGMENT_PAGE_COUNT, 1);
            mFragmentTitle = getArguments().getString(FRAGMENT_TITLE);
        }
        mDbHelper = new MovieDbHelper(getContext());
        mMovieDB = mDbHelper.getWritableDatabase();
        mMovieList = new ArrayList<Movie>();
        mToaster = new Toaster(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(DEBUG_TAG, "CREATING THE VIEW");
        // Inflate the layout for this fragment
        LinearLayout root = (LinearLayout) inflater.inflate(R.layout.fragment_favourited_movie, container, false);
        mGridView = (GridView) root.findViewById(R.id.gv_favourited_movies);
        // must load movies to mMovieList before load the list with adapter
        loadFavoritedMovies();
        loadMovieGridView();
        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFavoritedFragmentInteraction(String s);
    }

    private void loadFavoritedMovies() {

        Log.d(DEBUG_TAG, "Loading favourited movies");
        // sort by id ascending
        String sortOrder = MovieContract.MovieEntry._ID + " ASC";
        Cursor c = mMovieDB.query(
                MovieContract.MovieEntry.TABLE_NAME,  // The table to query
                projection,                           // The columns to return
                null,                                 // The columns for the WHERE clause
                null,                                 // The values for the WHERE clause
                null,                                 // don't group the rows
                null,                                 // don't filter by row groups
                sortOrder                             // The sort order
        );

        if (c.getCount() != 0) {
            Log.d(DEBUG_TAG, "Movie entry count: " + c.getCount());
            c.moveToFirst();
            while (!c.isLast()) {
                loadMovieFromDBToList(c, mMovieList);
                c.moveToNext();
            }
            // last row
            loadMovieFromDBToList(c, mMovieList);

        } else {
            Log.d(DEBUG_TAG, "NO MOVIES YET");
            mToaster.toastLong("No favourited movie yet.");
        }
    }

    private void loadMovieFromDBToList(Cursor c, ArrayList<Movie> list) {

        Movie m = new Movie();
        m.id = c.getInt(c.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_MOVIE_ID));
        m.title = c.getString(c.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_TITLE));
        m.backdrop_path = c.getString(c.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_IMAGE));
        int adultBoolean = c.getInt(c.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_ADULT));
        m.adult = (adultBoolean == 1) ? true : false;
        m.overview = c.getString(c.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW));
        m.budget = c.getInt(c.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_BUDGET));
        m.release_date = c.getString(c.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE));
        m.tagline = c.getString(c.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_TAGLINE));
        m.vote_average = c.getFloat(c.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE));
        m.vote_count = c.getInt(c.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_VOTE_COUNT));
        m.runtime = c.getInt(c.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_RUNTIME));
        // get genres
        String genres = c.getString(c.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_GENRES));
        String[] nameList = genres.split(" ");
        ArrayList<Genre> genreList = new ArrayList<Genre>();
        for (String genreName : nameList) {
            Genre newGen = new Genre(genreName);
            genreList.add(newGen);
        }
        m.genres = genreList;
        // get spoken languages
        String languages = c.getString(c.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_LANGUAGE));
        String[] langNames = languages.split(" ");
        ArrayList<Language> langList = new ArrayList<Language>();
        for (String langName : langNames) {
            Language l = new Language(langName);
            langList.add(l);
        }
        m.spoken_languages = langList;
        list.add(m);
    }

    private void loadMovieGridView() {

        mAdapter = new MovieViewAdapter(getContext(), R.layout.movie_item_layout, mMovieList);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie item = (Movie) parent.getItemAtPosition(position);
                int movieId = item.id;
                Log.d(DEBUG_TAG, "Movie ID: " + movieId);
                Intent toMovieOverviewIntent = new Intent(getActivity(), MovieOverviewActivity.class);
                toMovieOverviewIntent.putExtra("id", movieId);
                startActivity(toMovieOverviewIntent);
            }
        });
    }
}
