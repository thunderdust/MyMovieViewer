package com.example.weiranliu.mymovieviewer.DatabaseComponents;

import android.provider.BaseColumns;

/**
 * Created by weiran.liu on 12/17/2015.
 */
public class MovieContract {

    public MovieContract(){}

    /* define table contents */
    public static abstract class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_NAME_MOVIE_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_IMAGE = "image";
        public static final String COLUMN_NAME_OVERVIEW = "overview";
        public static final String COLUMN_NAME_ADULT = "adult";
        public static final String COLUMN_NAME_BUDGET = "budget";
        public static final String COLUMN_NAME_LANGUAGE = "spoken_languages";
        public static final String COLUMN_NAME_RELEASE_DATE = "release_date";
        public static final String COLUMN_NAME_TAGLINE = "tagline";
        public static final String COLUMN_NAME_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_NAME_VOTE_COUNT = "vote_count";
        public static final String COLUMN_NAME_RUNTIME = "runtime";
        public static final String COLUMN_NAME_GENRES = "genres";
        public static final String COLUMN_NAME_SAVE_DATE = "save_date";
    }
}
