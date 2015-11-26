package com.woivre.thibault.epiandroid.request;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.woivre.thibault.epiandroid.R;
import com.woivre.thibault.epiandroid.context.ApplicationContextProvider;
import com.woivre.thibault.epiandroid.objects.*;
import com.woivre.thibault.epiandroid.objects.EPIError;
import com.woivre.thibault.epiandroid.utils.Tuple;

import java.util.concurrent.ExecutionException;
import com.google.gson.Gson;

/**
 * Created by Thibault on 25/11/2015.
 */

/* CREATE ASYNTASK AND CONVERT TO JSON */

public class RequestManager {

    private static boolean isResponseError(String JSONData)
    {
        JsonObject JSONObj;

        JSONObj = new JsonParser().parse(JSONData).getAsJsonObject();
        if (JSONObj.has("error"))
        {
            return true;
        }
        return false;
    }

    public static EPIJSONObject LoginRequest(String login, String password) throws Exception
    {
        String JSONData;
        EPIJSONObject rObj = null;
        AsyncHTTPRequest request = new AsyncHTTPRequest();

        request.isPOST = true;
        request.urlAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_login_url);
        request.execute(new Tuple<String, String>("login", login), new Tuple<String, String>("password", password));
        try {
            JSONData = request.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new Exception();
        } catch (ExecutionException e) {
            e.printStackTrace();
            throw new Exception();
        }

        /*  HANDLING GSON DATA */

        Log.d("JSON", JSONData);

        if (isResponseError(JSONData)) {
            Log.d("INFO", "Has Error");
            rObj = new Gson().fromJson(JSONData, EPIError.class);
            Log.d("INFO", ((EPIError)rObj).toString());
        }
        else {
            //rval = gson.fromJson(JSONData, String.class);
        }
        return rObj;
    }
}
