package com.woivre.thibault.epiandroid.objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Thibault on 27/11/2015.
 */
public class EPIMessage extends EPIJSONObject {
    public String title;
    public User user = new User();
    public String content;
    public String date;

    private class User
    {
        String picture;
        String title;
        String url;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
