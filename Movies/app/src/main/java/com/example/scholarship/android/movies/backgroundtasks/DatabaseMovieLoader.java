package com.example.scholarship.android.movies.backgroundtasks;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.example.scholarship.android.movies.MainActivity;
import com.example.scholarship.android.movies.api.MovieDbApiUtils;
import com.example.scholarship.android.movies.data.Movie;
import com.example.scholarship.android.movies.data.MovieContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stefanie on 05.03.17.
 */

class DatabaseMovieLoader extends AsyncTaskLoader<List<Movie>> {


    private final Context mContext;

    public DatabaseMovieLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public List<Movie> loadInBackground() {
        Cursor cursor = mContext.getContentResolver().query(MovieContract.Movie.CONTENT_URI, null, null, null, null);
        if(cursor != null){
            ArrayList<Movie> movies = new ArrayList(cursor.getCount());
            while(cursor.moveToNext()){
                movies.add(new Movie(cursor));
            }
            return movies;
        }
        return null;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

}
