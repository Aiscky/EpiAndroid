package com.woivre.thibault.epiandroid.request;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;

public class AsyncDrawableRequest extends AsyncTask<String, Integer, Drawable> {
    ILoadImageOnPostExecute UpdateView;

    @Override
    protected Drawable doInBackground(String ... url) {
        Drawable d;
        try
        {
            InputStream is = (InputStream) new URL(url[0]).getContent();
            d = Drawable.createFromStream(is, null);
        }
        catch (Exception e)
        {
            return null;
        }
        return d;
    }

    @Override
    protected void onPostExecute(Drawable drawable)
    {
        if (UpdateView != null) {
            UpdateView.LoadImage(drawable);
        }
    }
}
