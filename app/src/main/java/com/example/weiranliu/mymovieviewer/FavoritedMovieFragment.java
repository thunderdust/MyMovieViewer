package com.example.weiranliu.mymovieviewer;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private final String API_KEY = "672dddea9cff27c1f8b77648cceee804";

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
            mFragmentPageCount = getArguments().getInt(FRAGMENT_PAGE_COUNT, 1);
            mFragmentTitle = getArguments().getString(FRAGMENT_TITLE);
        }
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
        public void onFragmentInteraction(Uri uri);
    }

}
