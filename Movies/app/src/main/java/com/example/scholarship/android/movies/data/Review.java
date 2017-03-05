package com.example.scholarship.android.movies.data;

import org.json.JSONObject;

/**
 * Created by stefanie on 05.03.17.
 */

class Review {
    private String id;
    private String content;
    private String author;


    public Review(JSONObject jsonObject){
        author = jsonObject.optString("author");
        id = jsonObject.optString("id");
        content = jsonObject.optString("content");
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }
}
