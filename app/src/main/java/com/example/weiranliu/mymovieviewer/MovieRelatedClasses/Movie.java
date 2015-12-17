package com.example.weiranliu.mymovieviewer.MovieRelatedClasses;

import java.util.ArrayList;

/**
 * Created by weiran.liu on 12/2/2015.
 */
public class Movie {

    public String title;
    public String backdrop_path;
    public boolean adult;
    //boolean video;
    public String overview;
    public int budget;
    public String original_language;
    public String release_date;
    public String status;
    public String tagline;
    public float vote_average;
    public int vote_count;
    public int id;
    public int runtime;
    public ArrayList<Genre> genres;
    public ArrayList<Language> spoken_languages;
}
