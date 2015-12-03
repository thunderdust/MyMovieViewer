package com.example.weiranliu.mymovieviewer;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.Call;

/**
 * Created by weiran.liu on 12/2/2015.
 */
public interface MovieService {

    @GET("/movie/now_playing")
    Call<MovieList> loadMovies(@Query("api_key") String api_key);

    @GET("/movie/{id}")
    Call<Movie> getMovie(@Path("id") String id, @Query("api_key") String api_key);

    @GET("/movie/{id}/videos")
    Call<Movie> getTrailers(@Path("id") String id, @Query("api_key") String api_key);

    @GET("/movie/{id}/similar")
    Call<Movie> getSimilarMovies(@Path("id") String id, @Query("api_key") String api_key);







}
