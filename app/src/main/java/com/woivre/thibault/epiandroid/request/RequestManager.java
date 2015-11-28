package com.woivre.thibault.epiandroid.request;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.woivre.thibault.epiandroid.R;
import com.woivre.thibault.epiandroid.context.ApplicationContextProvider;
import com.woivre.thibault.epiandroid.objects.*;
import com.woivre.thibault.epiandroid.objects.EPIError;
import com.woivre.thibault.epiandroid.utils.Tuple;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import com.google.gson.Gson;
import com.woivre.thibault.epiandroid.utils.Utils;

/**
 * Created by Thibault on 25/11/2015.
 */

/* CREATE ASYNTASK AND CONVERT TO JSON */

public class RequestManager {

    private static boolean isResponseError(String JSONData)
    {
        JsonParser parser = new JsonParser();

        try {
            if (parser.parse(JSONData).isJsonArray() || !parser.parse(JSONData).getAsJsonObject().has("error"))
                return false;
        } catch (JsonSyntaxException e) {
            return false;
        }
        return true;
    }

    public static EPIJSONObject LoginRequest(String login, String password) throws Exception
    {
        String JSONData;
        EPIJSONObject rObj = null;
        AsyncHTTPRequest request = new AsyncHTTPRequest();

        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        request.isPOST = true;
        request.urlAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_login_url);;

        Tuple[] params = new Tuple[2];
        params[0] = new Tuple("login", login);
        params[1] = new Tuple("password", password);

        request.execute(params);

        try {
            JSONData = request.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new Exception();
        }

        if (isResponseError(JSONData)) {
            rObj = new Gson().fromJson(JSONData, EPIError.class);
        }
        else {
            rObj = new Gson().fromJson(JSONData, EPIToken.class);
        }

        return rObj;
    }


    public static EPIJSONObject[] MessagesRequest(String token) throws Exception
    {
        String JSONData;
        EPIJSONObject[] rObjList = null;
        AsyncHTTPRequest request = new AsyncHTTPRequest();

        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        request.isPOST = true;
        request.urlAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_messages_url);;

        Tuple[] params = new Tuple[1];
        params[0] = new Tuple("token", token);

        request.execute(params);

        try {
            JSONData = request.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new Exception();
        }

        if (isResponseError(JSONData)) {
            EPIError error = new Gson().fromJson(JSONData, EPIError.class);
            rObjList = new EPIJSONObject[1];
            rObjList[0] = error;
        }
        else {
            JSONData = JSONData.substring(2);
            JSONData = "[{" + JSONData;
            Log.d("JSONDATA", JSONData);
//            //rObjList = (ArrayList<EPIJSONObject>) (ArrayList<?>) new Gson().fromJson(JSONData, new ArrayList<EPIMessage>().getClass());
            rObjList = new Gson().fromJson(JSONData, EPIMessage[].class);
        }

        return rObjList;
    }
}
