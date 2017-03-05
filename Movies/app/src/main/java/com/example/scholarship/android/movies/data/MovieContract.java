package com.example.scholarship.android.movies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by stefanie on 05.03.17.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.example.scholarship.android.movies";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_MOVIE = "movie";

    public static final class Movie implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();


        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_POSTER_PATH = "poster_path";


        public static final String COLUMN_OVERVIEW = "overview";

        public static final String COLUMN_RELEASE_DATE = "release_date";


        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_POPULARITY = "popularity";

        public static final String COLUMN_POSTER_BLOB = "poster_blob";

        public static final String COLUMN_VIDEOS_JSON_STRING = "videos_json_string";

        public static final String COLUMN_REVIEWS_JSON_STRING = "reviews_json_string";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
    }
}
