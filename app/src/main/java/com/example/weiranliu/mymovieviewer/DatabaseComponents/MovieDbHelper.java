package com.example.weiranliu.mymovieviewer.DatabaseComponents;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by weiran.liu on 12/17/2015.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MovieViewer.db";

    private static final String INT_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_TABLE =

            "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                    MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY," +
                    MovieContract.MovieEntry.COLUMN_NAME_MOVIE_ID + TEXT_TYPE + COMMA_SEP +
                    MovieContract.MovieEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    MovieContract.MovieEntry.COLUMN_NAME_IMAGE + TEXT_TYPE + COMMA_SEP +
                    MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW + TEXT_TYPE + COMMA_SEP +
                    MovieContract.MovieEntry.COLUMN_NAME_ADULT + INT_TYPE + COMMA_SEP +
                    MovieContract.MovieEntry.COLUMN_NAME_BUDGET + INT_TYPE + COMMA_SEP +
                    MovieContract.MovieEntry.COLUMN_NAME_GENRES + TEXT_TYPE + COMMA_SEP +
                    MovieContract.MovieEntry.COLUMN_NAME_LANGUAGE + TEXT_TYPE + COMMA_SEP +
                    MovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE + TEXT_TYPE + COMMA_SEP +
                    MovieContract.MovieEntry.COLUMN_NAME_RUNTIME + INT_TYPE + COMMA_SEP +
                    MovieContract.MovieEntry.COLUMN_NAME_TAGLINE + TEXT_TYPE + COMMA_SEP +
                    MovieContract.MovieEntry.COLUMN_NAME_SAVE_DATE + TEXT_TYPE + COMMA_SEP +
                    MovieContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE + REAL_TYPE + COMMA_SEP +
                    MovieContract.MovieEntry.COLUMN_NAME_VOTE_COUNT + INT_TYPE +
                    " )";
    private static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Simply drop old data and start over with online new data
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
