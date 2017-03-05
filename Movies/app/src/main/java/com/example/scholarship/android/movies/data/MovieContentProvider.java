package com.example.scholarship.android.movies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by stefanie on 05.03.17.
 */

public class MovieContentProvider extends ContentProvider {

    private static final int MOVIES = 100;
    private static final int MOVIE_WITH_ID = 101;

    private SQLiteOpenHelper mDatabaseHelper;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIE, MOVIES);
        sUriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIE + "/#", MOVIE_WITH_ID);
    }


    @Override
    public boolean onCreate() {
        mDatabaseHelper = new MovieDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

        switch (sUriMatcher.match(uri)) {

            case MOVIES:
                break;
            case MOVIE_WITH_ID:
                selection = MovieContract.Movie.COLUMN_MOVIE_ID + " = " + uri.getLastPathSegment();
                break;

            default:
                throw new UnsupportedOperationException("unknown uri to query: " + uri.toString());
        }
        Cursor cursor = db.query(MovieContract.Movie.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        Uri returnUri;

        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                long id = db.insert(MovieContract.Movie.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(MovieContract.Movie.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("unknown uri to insert: " + uri.toString());

        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        int deletedRows = 0;

        switch (sUriMatcher.match(uri)) {
            case MOVIE_WITH_ID:
                String id = uri.getLastPathSegment();
                deletedRows = db.delete(MovieContract.Movie.TABLE_NAME, MovieContract.Movie._ID + "=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("unknown uri to delete: " + uri.toString());
        }

        if (deletedRows > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        // will not be used within this scope
        throw new UnsupportedOperationException("updating not supported " + uri.toString());

    }
}
