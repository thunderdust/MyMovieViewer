package com.example.weiranliu.mymovieviewer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShowingMovieFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShowingMovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowingMovieFragment extends Fragment {

    // the fragment initialization parameters
    private static final String FRAGMENT_PAGE_COUNT = "page";
    private static final String FRAGMENT_TITLE = "title";
    private int mFragmentPageCount;
    private String mFragmentTitle;
    private OnFragmentInteractionListener mListener;

    private GridView mGridView;
    private MovieViewAdapter mAdapter;
    private ArrayList<Movie> mMovieList;
    private int mLoadedPageCount = 0;
    private MovieService ms;

    private final String API_KEY = "672dddea9cff27c1f8b77648cceee804";
    private final String DEBUG_TAG = "ShowingMovieFragment";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param page  Parameter 1.
     * @param title Parameter 2.
     * @return A new instance of fragment ShowingMovieFragment.
     */
    public static ShowingMovieFragment newInstance(int page, String title) {
        ShowingMovieFragment fragment = new ShowingMovieFragment();
        Bundle args = new Bundle();
        args.putInt(FRAGMENT_PAGE_COUNT, page);
        args.putString(FRAGMENT_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFragmentPageCount = getArguments().getInt(FRAGMENT_PAGE_COUNT, 0);
            mFragmentTitle = getArguments().getString(FRAGMENT_TITLE);
        }
        Gson gson = (new GsonBuilder()).create();
        ms = (new RestAdapter.Builder()
                .setEndpoint("http://api.themoviedb.org/3/")
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("NETWORK"))
                .build()).create(MovieService.class);
        // must execute before initiate movie view adapter to get the image URLs
        getShowingMovies(1);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_showing_movie, container, false);
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
        public void onShowingMovieFragmentInteraction(String s);
    }

    public void getMovieById(int id) {

        Callback<Movie> callback = new Callback<Movie>() {
            @Override
            public void success(Movie movie, Response response) {
                Log.d(DEBUG_TAG, "title:" + movie.title);
                Log.d(DEBUG_TAG, "backdrop:" + movie.backdrop_path);
                Log.d(DEBUG_TAG, "overview:" + movie.overview);
                //picasso.load(movie.backdrop_link);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DEBUG_TAG, error.getMessage());
            }
        };

        ms.getMovie(id, API_KEY, callback);
    }

    public void getShowingMovies(int page) {
        Callback<MovieList> cb = new Callback<MovieList>() {
            @Override
            public void success(MovieList movieList, Response response) {
                Log.d(DEBUG_TAG, "size:" + movieList.results.size());
                loadMovieGridView(movieList);
                mLoadedPageCount ++;
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DEBUG_TAG, error.getMessage());
            }
        };
        ms.loadShowingMovies(API_KEY, page, cb);
    }

    private void loadMovieGridView(MovieList ml) {

        Log.d(DEBUG_TAG, "loading movie gridviews.");
        mMovieList = new ArrayList<Movie>();
        for (Movie m : ml.results) {
            mMovieList.add(m);
        }
        mGridView = (GridView) getActivity().findViewById(R.id.gv_showing_movie);
        mAdapter = new MovieViewAdapter(getContext(), R.layout.movie_item_layout, mMovieList);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie item = (Movie) parent.getItemAtPosition(position);
                int movieId = item.id;
                Log.d(DEBUG_TAG, "Movie ID: " + movieId);
                Intent toMovieDetailIntent = new Intent(getActivity(), MovieDetailActivity.class);
                toMovieDetailIntent.putExtra("id", movieId);
                startActivity(toMovieDetailIntent);
            }
        });
    }
}
