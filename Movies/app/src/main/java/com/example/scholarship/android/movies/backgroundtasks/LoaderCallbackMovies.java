package com.example.scholarship.android.movies.backgroundtasks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.example.scholarship.android.movies.api.Movie;

import java.util.List;

/**
 * Created by stefanie on 04.03.17.
 */

public class LoaderCallbackMovies implements LoaderManager.LoaderCallbacks<List<Movie>> {

    public static final String LOAD_IDENTIFIER = "load-identifier";
    public static final String LOAD_MOST_POPULAR = "load-most-popular";
    public static final String LOAD_TOP_RATED = "load-top-rated";
    public static final String LOAD_REVIEWS = "load-reviews";
    public static final String LOAD_TRAILERS = "load-trailers";
    public static final String SORT_CRITERIA_IDENTIFIER = "sort-criteria-identifier";

    public interface MovieLoaderCallback {
        void onPreExecute();
        void onPostExecute(List<Movie> movies);
    }

    private final Context mContext;
    private final MovieLoaderCallback mCallback;


    public LoaderCallbackMovies(Context context, MovieLoaderCallback callback) {
        mContext = context;
        mCallback = callback;
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        if(mCallback != null){
            mCallback.onPreExecute();
        }

        return new AsyncMovieLoader(mContext, args);
    }



    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        if(mCallback != null){
            mCallback.onPostExecute(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }
}
