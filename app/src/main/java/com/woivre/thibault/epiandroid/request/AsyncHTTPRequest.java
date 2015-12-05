package com.woivre.thibault.epiandroid.request;

import android.os.AsyncTask;
import android.util.Log;

import com.woivre.thibault.epiandroid.objects.EPIJSONObject;
import com.woivre.thibault.epiandroid.utils.Tuple;
import com.woivre.thibault.epiandroid.utils.Utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncHTTPRequest extends AsyncTask<Tuple, Integer, String> {

    public String URLAddress = "";
    public RequestType TaskType;
    public IUpdateViewOnPostExecute UpdateView = null;
    public Class UpdateViewParamType;
    public Boolean IsArray;

    public enum RequestType
    {
        POST,
        GET,
        DELETE
    }

    /* ASYNCTAKS MEDTHODS */

    @Override
    protected void onPostExecute(String s) {


        EPIJSONObject[] objs = null;

        objs = RequestManager.ParseEPIJSON(s, UpdateViewParamType, IsArray);

        if (UpdateView != null)
        {
            UpdateView.UpdateView(objs);
        }
    }

    @Override
    protected String doInBackground(Tuple ... params) {
        HttpURLConnection urlConnection;
        InputStream in;
        String rval;

        /* CREATING PARAMETERS */

        String separator = "";
        StringBuilder urlParameters = new StringBuilder();

        for (Tuple param : params)
        {
            urlParameters.append(separator);
            urlParameters.append(param.x);
            urlParameters.append("=");
            urlParameters.append(param.y);
            separator = "&";
        }

        if (TaskType == RequestType.GET || TaskType == RequestType.DELETE) {
            URLAddress += "?" + urlParameters.toString();
        }

        /* CREATING URL CONNECTION OBJ */

        try {
            urlConnection = getURLConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        if (TaskType == RequestType.POST)
        {
            try {
                SettingPostParameters(urlConnection, urlParameters.toString());
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }

        Log.d("URL", URLAddress);
        Log.d("URLPARAMETER", urlParameters.toString());

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
            url = new URL(URLAddress);
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }

        /* SETTING THE REQUEST METHOD */

        switch (TaskType)
        {
            case POST:
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                break;
            case DELETE:
                urlConnection.setRequestMethod("DELETE");
                urlConnection.setDoOutput(true);
                break;
            case GET:
                urlConnection.setRequestMethod("GET");
                break;
        }

        /* SETTING URLCONNECTION PARAMETERS */

        urlConnection.setConnectTimeout(30 * 1000);
        urlConnection.setReadTimeout(30 * 1000);

        return urlConnection;
    }
}
