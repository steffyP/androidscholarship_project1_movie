package com.example.scholarship.android.movies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.scholarship.android.movies.data.MovieContract.Movie;

/**
 * Created by stefanie on 05.03.17.
 */

public class MovieDatabaseHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "movie.db";
    private final static int DATABASE_VERSION = 1;
    private Context mContext;

    public MovieDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + Movie.TABLE_NAME + " ( " +
                Movie._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Movie.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                Movie.COLUMN_POSTER_PATH + " TEXT, " +
                Movie.COLUMN_RELEASE_DATE + " TEXT, " +
                Movie.COLUMN_OVERVIEW + " TEXT, " +
                Movie.COLUMN_TITLE + " TEXT, " +
                Movie.COLUMN_POPULARITY + " REAL, " +
                Movie.COLUMN_POSTER_BLOB + " BLOB, " +
                Movie.COLUMN_REVIEWS_JSON_STRING + " TEXT, " +
                Movie.COLUMN_VIDEOS_JSON_STRING + " TEXT " +
                " );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
