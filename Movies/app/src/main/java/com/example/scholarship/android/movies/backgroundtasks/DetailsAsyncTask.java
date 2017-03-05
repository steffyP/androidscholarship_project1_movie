package com.example.scholarship.android.movies.backgroundtasks;

import android.os.AsyncTask;

import com.example.scholarship.android.movies.api.MovieDbApiUtils;
import com.example.scholarship.android.movies.data.model.Movie;

/**
 * Created by stefanie on 05.03.17.
 */

public class DetailsAsyncTask extends AsyncTask<Movie, Void, Movie> {
    public interface AsyncCallback{
        void onPreExecute();
        void onPostExecute(Movie movie);
    }


    private final AsyncCallback mCallback;


    public DetailsAsyncTask(AsyncCallback callback){
        mCallback = callback;
    }

    @Override
    protected void onPreExecute() {
        if(mCallback != null){
            mCallback.onPreExecute();
        }
    }

    @Override
    protected Movie doInBackground(Movie... params) {
        Movie movie = params[0];
        if(movie != null){
            if(movie.getVideos() == null || movie.getReviews() == null) {
                movie = MovieDbApiUtils.getInstance().queryDetailsForMovie(movie);
            }
        }

        return movie;
    }

    @Override
    protected void onPostExecute(Movie movie) {
        if(mCallback != null){
            mCallback.onPostExecute(movie);
        }
    }
}
