package com.example.weiranliu.mymovieviewer;

import java.util.ArrayList;

/**
 * Created by weiran.liu on 12/2/2015.
 */
public class Movie {

    String title;
    String backdrop_path;
    boolean adult;
    boolean video;
    String overview;
    int budget;
    String original_language;
    String release_date;
    String status;
    String tagline;
    float vote_average;
    int vote_count;
    int id;
    int runtime;
    ArrayList<Genre> genres;
    ArrayList<Language> spoken_languages;
}
