package com.woivre.thibault.epiandroid.request;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import com.woivre.thibault.epiandroid.utils.Tuple;
import com.woivre.thibault.epiandroid.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncDrawableRequest extends AsyncTask<String, Integer, Drawable> {
    String urlAddr = "";

    @Override
    protected Drawable doInBackground(String ... url) {
        Drawable d;
        try {
            InputStream is = (InputStream) new URL(url[0]).getContent();
            d = Drawable.createFromStream(is, null);
        } catch (Exception e) {
            return null;
        }
        return d;
    }
}
