package com.woivre.thibault.epiandroid.request;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
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

        Log.d("ISERROR", JSONData);

        try {
            if (parser.parse(JSONData).isJsonArray()  || !parser.parse(JSONData).getAsJsonObject().has("error"))
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
            rObjList = new Gson().fromJson(JSONData, EPIMessage[].class);
        }

        return rObjList;
    }

    public static EPIJSONObject[] AlertsRequest(String token) throws Exception
    {
        String JSONData;
        EPIJSONObject[] rObjList = null;
        AsyncHTTPRequest request = new AsyncHTTPRequest();

        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        request.isPOST = true;
        request.urlAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_alerts_url);

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
            rObjList = new Gson().fromJson(JSONData, EPIAlert[].class);
        }

        return rObjList;
    }

    public static EPIJSONObject UserRequest(String token, String login) throws Exception
    {
        String JSONData;
        EPIJSONObject rObj = null;
        AsyncHTTPRequest request = new AsyncHTTPRequest();

        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        request.isPOST = false;
        request.urlAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_user_url);

        Tuple[] params = new Tuple[2];
        params[0] = new Tuple("token", token);
        params[1] = new Tuple("user", login);

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
            JSONData = "{" + JSONData;
            rObj = new Gson().fromJson(JSONData, EPIUser.class);
        }

        return rObj;
    }

    public static EPIJSONObject[] PlanningRequest(String token, String start, String end) throws Exception
    {
        String JSONData;
        EPIJSONObject[] rObjList = null;
        AsyncHTTPRequest request = new AsyncHTTPRequest();

        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        request.isPOST = false;
        request.urlAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_planning_url);

        Tuple[] params = new Tuple[3];
        params[0] = new Tuple("token", token);
        params[1] = new Tuple("start", start);
        params[2] = new Tuple("end", end);

        request.execute(params);

        try {
            JSONData = request.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new Exception();
        }

        Log.d("JSON", JSONData);

        if (isResponseError(JSONData)) {
            EPIError error = new Gson().fromJson(JSONData, EPIError.class);
            rObjList = new EPIJSONObject[1];
            rObjList[0] = error;
        }
        else if (JSONData.startsWith("{") && JSONData.endsWith("}"))
        {
            rObjList = new EPIEventPlanning[0];
        }
        else
        {
            rObjList = new Gson().fromJson(JSONData, EPIEventPlanning[].class);
        }

        return rObjList;
    }

    public static EPIError ValidateTokenRequest(String token, String scolaryear, String codemodule, String codeinstance, String codeacti, String codeevent, String tokenvalidationcode) throws Exception
    {
        String JSONData;
        AsyncHTTPRequest request = new AsyncHTTPRequest();

        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        request.isPOST = true;
        request.urlAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_validatetoken_url);

        Tuple[] params = new Tuple[7];
        params[0] = new Tuple("token", token);
        params[1] = new Tuple("scolaryear", scolaryear);
        params[2] = new Tuple("codemodule", codemodule);
        params[3] = new Tuple("codeinstance", codeinstance);
        params[4] = new Tuple("codeacti", codeacti);
        params[5] = new Tuple("codeevent", codeevent);
        params[6] = new Tuple("tokenvalidationcode", tokenvalidationcode);

        request.execute(params);

        try {
            JSONData = request.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new Exception();
        }

        Log.d("JSON", JSONData);

        if (!JSONData.isEmpty()) {
            EPIError error = new EPIError();
            return error;
        }

        return null;
    }

    public static EPIError RegisterEventRequest(String token, String scolaryear, String codemodule, String codeinstance, String codeacti, String codeevent) throws Exception
    {
        String JSONData;
        AsyncHTTPRequest request = new AsyncHTTPRequest();

        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        request.isPOST = true;
        request.urlAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_eventregister_url);

        Tuple[] params = new Tuple[6];
        params[0] = new Tuple("token", token);
        params[1] = new Tuple("scolaryear", scolaryear);
        params[2] = new Tuple("codemodule", codemodule);
        params[3] = new Tuple("codeinstance", codeinstance);
        params[4] = new Tuple("codeacti", codeacti);
        params[5] = new Tuple("codeevent", codeevent);

        request.execute(params);

        try {
            JSONData = request.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new Exception();
        }

        if (!JSONData.isEmpty()) {
            EPIError error = new EPIError();
            return error;
        }

        return null;
    }

    public static EPIError UnregisterEventRequest(String token, String scolaryear, String codemodule, String codeinstance, String codeacti, String codeevent) throws Exception
    {
        String JSONData;
        AsyncHTTPRequest request = new AsyncHTTPRequest();

        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        request.isPOST = false;
        request.isDelete = true;
        request.urlAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_eventregister_url);

        Tuple[] params = new Tuple[6];
        params[0] = new Tuple("token", token);
        params[1] = new Tuple("scolaryear", scolaryear);
        params[2] = new Tuple("codemodule", codemodule);
        params[3] = new Tuple("codeinstance", codeinstance);
        params[4] = new Tuple("codeacti", codeacti);
        params[5] = new Tuple("codeevent", codeevent);

        request.execute(params);

        try {
            JSONData = request.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new Exception();
        }

        if (!JSONData.isEmpty()) {
            EPIError error = new EPIError();
            return error;
        }

        return null;
    }

    public static EPIJSONObject GradesRequest(String token) throws Exception
    {
        String JSONData;
        EPIJSONObject rObj = null;
        AsyncHTTPRequest request = new AsyncHTTPRequest();

        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        request.isPOST = false;
        request.urlAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_grades_url);

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
            rObj = new Gson().fromJson(JSONData, EPIError.class);
        }
        else {
            rObj = new Gson().fromJson(JSONData, EPIGrades.class);
        }

        return rObj;
    }


    public static EPIJSONObject AllModulesRequest(String token, Double scolaryear, String location, String course) throws Exception {
        String JSONData;
        EPIJSONObject rObj = null;
        AsyncHTTPRequest request = new AsyncHTTPRequest();

        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        request.isPOST = false;
        request.urlAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_allmodules_url);

        Tuple[] params = new Tuple[4];
        params[0] = new Tuple("token", token);
        params[1] = new Tuple("scolaryear", scolaryear.toString());
        params[2] = new Tuple("location", location);
        params[3] = new Tuple("course", course);

        request.execute(params);

        try {
            JSONData = request.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new Exception();
        }

        Log.d("JSON", JSONData);

        if (isResponseError(JSONData))
        {
            rObj = new Gson().fromJson(JSONData, EPIError.class);
        }
        else
        {
            JSONData = "{\"preload\" : [[" + JSONData;
            Log.d("JSON", JSONData);
            rObj = new Gson().fromJson(JSONData, EPIAllModules.class);
        }

        return rObj;
    }

    public static Drawable LoadImageFromUrl(String url) throws Exception{
        AsyncDrawableRequest request = new AsyncDrawableRequest();
        Drawable myDrawable;

        request.execute(url);
        try {
            myDrawable = request.get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception();
        }
        return myDrawable;
    }

    public static EPIJSONObject ModuleRequest(String token, String scolaryear, String codemodule, String codeinstance) throws Exception
    {
        String JSONData;
        EPIJSONObject rObj = null;
        AsyncHTTPRequest request = new AsyncHTTPRequest();

        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        request.isPOST = false;
        request.urlAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_module_url);

        Tuple[] params = new Tuple[4];
        params[0] = new Tuple("token", token);
        params[1] = new Tuple("scolaryear", scolaryear);
        params[2] = new Tuple("codemodule", codemodule);
        params[3] = new Tuple("codeinstance", codeinstance);

        request.execute(params);

        try
        {
            JSONData = request.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new Exception();
        }

        if (isResponseError(JSONData)) {
            rObj = new Gson().fromJson(JSONData, EPIError.class);
        }
        else
        {
            JSONData = "{" + JSONData;
            Log.d("JSONData", JSONData);
            rObj = new Gson().fromJson(JSONData, EPIModule.class);
        }

        return rObj;
    }

    public static EPIError RegisterModuleRequest(String token, String scolaryear, String codemodule, String codeinstance) throws Exception
    {
        String JSONData;
        AsyncHTTPRequest request = new AsyncHTTPRequest();

        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        request.isPOST = true;
        request.urlAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_module_url);

        Tuple[] params = new Tuple[4];
        params[0] = new Tuple("token", token);
        params[1] = new Tuple("scolaryear", scolaryear);
        params[2] = new Tuple("codemodule", codemodule);
        params[3] = new Tuple("codeinstance", codeinstance);

        request.execute(params);

        try {
            JSONData = request.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new Exception();
        }

        if (!JSONData.isEmpty()) {
            EPIError error = new EPIError();
            return error;
        }

        return null;
    }

    public static EPIError UnregisterModuleRequest(String token, String scolaryear, String codemodule, String codeinstance) throws Exception
    {
        String JSONData;
        AsyncHTTPRequest request = new AsyncHTTPRequest();

        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        request.isPOST = false;
        request.isDelete = true;
        request.urlAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_module_url);

        Tuple[] params = new Tuple[4];
        params[0] = new Tuple("token", token);
        params[1] = new Tuple("scolaryear", scolaryear);
        params[2] = new Tuple("codemodule", codemodule);
        params[3] = new Tuple("codeinstance", codeinstance);

        request.execute(params);

        try {
            JSONData = request.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new Exception();
        }

        if (!JSONData.isEmpty()) {
            EPIError error = new EPIError();
            return error;
        }

        return null;
    }

    public static EPIError UnregisterProjectRequest(String token, String scolaryear, String codemodule, String codeinstance, String codeacti) throws Exception
    {
        String JSONData;
        AsyncHTTPRequest request = new AsyncHTTPRequest();

        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        request.isPOST = false;
        request.isDelete = true;
        request.urlAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_projectregister_url);

        Tuple[] params = new Tuple[5];
        params[0] = new Tuple("token", token);
        params[1] = new Tuple("scolaryear", scolaryear);
        params[2] = new Tuple("codemodule", codemodule);
        params[3] = new Tuple("codeinstance", codeinstance);
        params[4] = new Tuple("codeacti", codeacti);

        request.execute(params);

        try {
            JSONData = request.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new Exception();
        }

        if (!JSONData.isEmpty()) {
            EPIError error = new EPIError();
            return error;
        }

        return null;
    }

    public static EPIError RegisterProjectRequest(String token, String scolaryear, String codemodule, String codeinstance, String codeacti) throws Exception
    {
        String JSONData;
        AsyncHTTPRequest request = new AsyncHTTPRequest();

        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        request.isPOST = true;
        request.urlAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_projectregister_url);

        Tuple[] params = new Tuple[5];
        params[0] = new Tuple("token", token);
        params[1] = new Tuple("scolaryear", scolaryear);
        params[2] = new Tuple("codemodule", codemodule);
        params[3] = new Tuple("codeinstance", codeinstance);
        params[4] = new Tuple("codeacti", codeacti);

        request.execute(params);

        try {
            JSONData = request.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new Exception();
        }

        if (!JSONData.isEmpty()) {
            EPIError error = new EPIError();
            return error;
        }

        return null;
    }

    public static EPIJSONObject[] ProjectsRequest(String token) throws Exception
    {
        String JSONData;
        EPIJSONObject[] rObjList = null;
        AsyncHTTPRequest request = new AsyncHTTPRequest();

        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        request.isPOST = false;
        request.urlAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_projects_url);

        Tuple[] params = new Tuple[1];
        params[0] = new Tuple("token", token);

        request.execute(params);

        try {
            JSONData = request.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new Exception();
        }

        Log.d("JSON", JSONData);

        if (isResponseError(JSONData)) {
            EPIError error = new Gson().fromJson(JSONData, EPIError.class);
            rObjList = new EPIJSONObject[1];
            rObjList[0] = error;
        }
        else if (JSONData.startsWith("{") && JSONData.endsWith("}"))
        {
            rObjList = new EPIProject[0];
        }
        else
        {
            rObjList = new Gson().fromJson(JSONData, EPIProject[].class);
        }

        return rObjList;
    }
}
