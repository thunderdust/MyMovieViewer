package com.example.weiranliu.mymovieviewer.Adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.weiranliu.mymovieviewer.MovieRelatedClasses.Movie;
import com.example.weiranliu.mymovieviewer.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by weiran.liu on 12/21/2015.
 */
public class LocalMovieViewAdapter extends ArrayAdapter {

    private Context context;
    private int layoutResourceId;
    private ArrayList<Movie> data = new ArrayList<Movie>();
    private Picasso picasso;

    private final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";
    private final String IMAGE_FOLDER_PATH = "/MyMovieViewer/img";

    public LocalMovieViewAdapter(Context context, int layoutResourceId, ArrayList data){
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
        this.picasso = (new Picasso.Builder(context)).build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        ViewHolder holder = null;
        if (row==null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) row.findViewById(R.id.iv_movie_cover);
            holder.movieId = data.get(position).id;
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }
        String filePath = Environment.getExternalStorageDirectory().toString() + IMAGE_FOLDER_PATH + data.get(position).backdrop_path;
        Log.d("LOCAL MOVIE", "FILE PATH: " + filePath);
        File f = new File(filePath);
        Picasso.with(context).load(f).placeholder(R.drawable.place_holder).
                error(R.drawable.no_image).fit().centerCrop().into(holder.imageView);
        return row;
    }

    static class ViewHolder{
        ImageView imageView;
        int movieId;
    }
}
