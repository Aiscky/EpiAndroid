package com.woivre.thibault.epiandroid.utils;

import android.net.ConnectivityManager;
import android.content.Context;
import android.net.NetworkInfo;

import com.woivre.thibault.epiandroid.context.ApplicationContextProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Thibault on 25/11/2015.
 */
public class Utils {
    public static String convertInputStreamToString(InputStream inputStream) throws IOException
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String inputLine;
        StringBuffer response = new StringBuffer();

        try {
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }
        return response.toString();
    }

    public static boolean isNetworkAvailable()
    {

        ConnectivityManager cm = (ConnectivityManager) (ApplicationContextProvider.getContext().getSystemService(ApplicationContextProvider.getContext().CONNECTIVITY_SERVICE));
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return (ni != null && ni.isConnectedOrConnecting());
    }
}
