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

import java.util.ArrayList;
import java.util.List;
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

    public static <T extends EPIJSONObject> EPIJSONObject GenericRequest(String url, Tuple[] params, boolean isPOST, Class<T> returnType) throws Exception
    {
        String JSONData;
        EPIJSONObject rObj = null;
        AsyncHTTPRequest request = new AsyncHTTPRequest();

        request.isPOST = isPOST;
        request.urlAddress = url;
        request.execute(params);
        try {
            JSONData = request.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new Exception();
        }

        /*  HANDLING GSON DATA */


        if (isResponseError(JSONData)) {
            rObj = new Gson().fromJson(JSONData, EPIError.class);
        }
        else {
            rObj = new Gson().fromJson(JSONData, returnType);
        }

        return rObj;
    }

    public static EPIJSONObject LoginRequest(String login, String password) throws Exception
    {
        String url = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_login_url);

        Tuple[] params = new Tuple[2];
        params[0] = new Tuple("login", login);
        params[1] = new Tuple("password", password);
        EPIJSONObject rObj = GenericRequest(url, params, true, EPIToken.class);
        return rObj;
    }
}
