package com.example.weiranliu.mymovieviewer;

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
    void loadShowingMovies(@Query("api_key") String api_key, Callback<MovieList> cb);

    @GET("/movie/{id}")
    void getMovie(@Path("id") int id, @Query("api_key") String api_key, Callback<Movie> cb);


//    @GET("/movie/{id}/videos")
//    Call<Movie> getTrailers(@Path("id") String id, @Query("api_key") String api_key);
//
//    @GET("/movie/{id}/similar")
//    Call<Movie> getSimilarMovies(@Path("id") String id, @Query("api_key") String api_key);
}
