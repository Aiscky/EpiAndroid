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

    public boolean isPOST = false;
    public String urlParameters = "";
    private final String USER_AGENT = "Mozilla/5.0";

    /* ASYNCTAKS MEDTHODS */

    @Override
    protected String doInBackground(Tuple<String, String> ... params) {
        HttpURLConnection urlConnection = null;
        InputStream in = null;

        /* CREATING URL CONNECTION OBJ */

        try {
            urlConnection = getURLConnection(ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_login_url));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        /* SET POST PARAMS */

        for (Tuple<String, String> param : params)
        {
            Log.d("PROPERTY", param.x + " " + param.y);
            urlConnection.setRequestProperty(param.x, param.y);
        }

        /* SENDING REQUEST */

        /*try {
            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
            wr.flush();
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        try {
            urlConnection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*try {
            Log.d("INFO", Integer.toString(urlConnection.getResponseCode()));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }*/

        /* GETTING RESPONSE */

        try {
            in = urlConnection.getInputStream();
            Log.d("INFO", Utils.convertInputStreamToString(in));
        } catch (IOException e) {
            e.printStackTrace();
            in = urlConnection.getErrorStream();
            try {
                Log.d("INFO", Utils.convertInputStreamToString(in));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return "";
        }

        return "";
    }

    /* GETTING URL CONNECTION */

    private HttpURLConnection getURLConnection(String URLName) throws IOException
    {
        URL url = null;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(URLName);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            throw new IOException();
        }

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setChunkedStreamingMode(0);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }

        /* SETTING THE REQUEST METHOD */

        if (isPOST == true) {
            //urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            Log.d("INFO", "Setting POST Method");
        }
        else {
            //urlConnection.setRequestMethod("GET");
        }

        /* SETTING URLCONNECTION PARAMETERS */

        urlConnection.setConnectTimeout(30 * 1000);
        urlConnection.setReadTimeout(30 * 1000);

        return urlConnection;
    }

    /* SEND HTTP REQUEST */

    private void updatePostRequest(HttpURLConnection urlConnection) throws IOException
    {
        try {
            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            if (isPOST)
            {
                wr.writeBytes(urlParameters);
            }
            wr.flush();
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }
    }

    /* GET HTTP RESPONSE */

    private String getRequestResponse(HttpURLConnection urlConnection) throws IOException
    {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(urlConnection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return null;
    }
}
