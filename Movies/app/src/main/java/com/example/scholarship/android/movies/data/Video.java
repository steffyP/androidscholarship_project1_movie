package com.example.scholarship.android.movies.data;

import org.json.JSONObject;

/**
 * Created by stefanie on 05.03.17.
 */

public class Video {


    private String id;
    private String key;
    private String name;
    private String site;
    private String size;
    private String type;

    public Video(JSONObject jsonObject){
        id = jsonObject.optString("id");
        key = jsonObject.optString("key");
        name = jsonObject.optString("name");
        site = jsonObject.optString("site");
        size = jsonObject.optString("size");
        type = jsonObject.optString("type");
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public String getSize() {
        return size;
    }

    public String getType() {
        return type;
    }
}
