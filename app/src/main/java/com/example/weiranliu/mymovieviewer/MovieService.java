package com.example.weiranliu.mymovieviewer;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by weiran.liu on 12/2/2015.
 */
public interface MovieService {


    @GET("/movie/now_playing")
    void loadShowingMovies(@Query("api_key") String api_key, @Query("page") int page, Callback<MovieList> cb);

    @GET("/movie/{id}")
    void getMovie(@Path("id") int id, @Query("api_key") String api_key, Callback<Movie> cb);

    @GET("/genre/movie/list")
    void getGenreList(@Query("api_key") String api_key, Callback<ArrayList<Genre>> cb);


}
