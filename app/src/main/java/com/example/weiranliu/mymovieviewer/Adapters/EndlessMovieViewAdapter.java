package com.example.weiranliu.mymovieviewer.Adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.commonsware.cwac.endless.EndlessAdapter;
import com.example.weiranliu.mymovieviewer.MovieRelatedClasses.Movie;
import com.example.weiranliu.mymovieviewer.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by weiran.liu on 12/21/2015.
 */
public class EndlessMovieViewAdapter extends EndlessAdapter {


    private RotateAnimation rotate = null;
    private View pendingView = null;

    public EndlessMovieViewAdapter(Context ctx, int layoutResourceId, ArrayList data){
        super(new MovieViewAdapter(ctx, layoutResourceId, data));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected View getPendingView(ViewGroup parent) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item_layout, null);
        pendingView = item.findViewById(R.id.iv_movie_cover);
        ((ImageView)pendingView).setImageResource(R.drawable.place_holder);
        return (item);
    }

    @Override
    protected boolean cacheInBackground() {
        SystemClock.sleep(10000);       // pretend to do work
        return (getWrappedAdapter().getCount() < 30);
    }

    @Override
    protected void appendCachedData() {
        //MovieViewAdapter adapter = (MovieViewAdapter) getWrappedAdapter();
    }
}
