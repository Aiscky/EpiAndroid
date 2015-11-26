package com.woivre.thibault.epiandroid.request;

import android.os.AsyncTask;
import android.util.Log;

import com.woivre.thibault.epiandroid.R;
import com.woivre.thibault.epiandroid.utils.Tuple;
import com.woivre.thibault.epiandroid.utils.Utils;
import com.woivre.thibault.epiandroid.context.ApplicationContextProvider;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AsyncHTTPRequest extends AsyncTask<Tuple<String, String>, Integer, String> {

    public String urlAddress = "";
    public boolean isPOST = false;

    /* ASYNCTAKS MEDTHODS */

    @Override
    protected String doInBackground(Tuple<String, String> ... params) {
        HttpURLConnection urlConnection;
        InputStream in;
        String rval;

        /* CREATING URL CONNECTION OBJ */

        try {
            urlConnection = getURLConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        /* SETTING URLPARAMETERS ON POST */

        String separator = "";
        StringBuilder urlParameters = new StringBuilder();

        for (Tuple<String, String> param : params)
        {
            urlParameters.append(separator);
            urlParameters.append(param.x);
            urlParameters.append("=");
            urlParameters.append(param.y);
            separator = "&";
        }

        Log.d("URLPARAMETER", urlParameters.toString());

        try {
            if (isPOST) {
                SettingPostParameters(urlConnection, urlParameters.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        /* GETTING RESPONSE */

        try {
            in = urlConnection.getInputStream();
            rval = Utils.convertInputStreamToString(in);
            Log.d("INFO", rval);
        } catch (IOException e) {
            e.printStackTrace();
            in = urlConnection.getErrorStream();
            try {
                rval = Utils.convertInputStreamToString(in);
                Log.d("JSON", rval);
                return rval;
            } catch (IOException e1) {
                e1.printStackTrace();
                return "";
            }
        }

        return rval;
    }

    /* SEND HTTP REQUEST */

    private void SettingPostParameters(HttpURLConnection urlConnection, String urlParameters) throws IOException
    {
        try {
            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }
    }

    /* GETTING URL CONNECTION */

    private HttpURLConnection getURLConnection() throws IOException
    {
        URL url;
        HttpURLConnection urlConnection;

        try {
            url = new URL(urlAddress);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            throw new IOException();
        }

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            //urlConnection.setChunkedStreamingMode(0);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }

        /* SETTING THE REQUEST METHOD */

        if (isPOST) {
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            Log.d("INFO", "Setting POST Method");
        }
        else {
            urlConnection.setRequestMethod("GET");
        }

        /* SETTING URLCONNECTION PARAMETERS */

        urlConnection.setConnectTimeout(30 * 1000);
        urlConnection.setReadTimeout(30 * 1000);

        return urlConnection;
    }
}
