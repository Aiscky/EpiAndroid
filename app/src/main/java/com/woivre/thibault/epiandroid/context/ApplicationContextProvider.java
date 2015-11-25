package com.woivre.thibault.epiandroid.context;

import android.app.Application;
import android.content.Context;

/**
 * Created by Thibault on 24/11/2015.
 */
public class ApplicationContextProvider extends Application {
    private static Context sContext;

    @Override
    public void onCreate()
    {
        super.onCreate();

        sContext = this.getApplicationContext();
    }

    public static Context getContext()
    {
        return sContext;
    }
}
