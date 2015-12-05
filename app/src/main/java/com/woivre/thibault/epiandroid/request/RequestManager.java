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
import java.net.URL;
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

    //TODO REMOVE

//    public void GenericRequest(Tuple[] params,
//                                                                   AsyncHTTPRequest.RequestType type,
//                                                                   String URLAddress, Class<? extends EPIJSONObject> objType,
//                                                                   IUpdateViewOnPostExecute updateObj) throws Exception
//    {
//        AsyncHTTPRequest request = new AsyncHTTPRequest();
//
//        if (!Utils.isNetworkAvailable())
//            throw new EPINetworkException();
//
//        request.requestType = type;
//        request.URLAddress = URLAddress;
//        request.updateView = updateObj;
//
//        request.execute(params);
//    }

    public static EPIJSONObject[] ParseEPIJSON(String JSONData, Class<? extends EPIJSONObject> returnType, Boolean isArray)
    {
        EPIJSONObject[] objs = null;

        if (isResponseError(JSONData))
        {
            objs = new EPIJSONObject[1];
            objs[0] = new Gson().fromJson(JSONData, EPIError.class);
        }
        else if (isArray == true)
        {
            try
            {
                Class arrayType = Class.forName("[L" + returnType.getName() + ";");
                objs = (EPIJSONObject[]) new Gson().fromJson(JSONData, arrayType);
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
                return null;
            }
        }
        else if (isArray == false)
        {
            objs = new EPIJSONObject[1];
            objs[0] = new Gson().fromJson(JSONData, returnType);
        }

        return objs;
    }

    public static boolean isResponseError(String JSONData)
    {
        JsonParser parser = new JsonParser();

        Log.d("ISERROR", JSONData);

        try
        {
            if (parser.parse(JSONData).isJsonArray() || JSONData.equals("{}") || !parser.parse(JSONData).getAsJsonObject().has("error")) {
                return false;
            }
        } catch (JsonSyntaxException e) {
            return false;
        }
        return true;
    }

    public static void LoginRequest(String login, String password, IUpdateViewOnPostExecute updateView) throws Exception
    {
        if (!Utils.isNetworkAvailable())
        {
            throw new EPINetworkException();
        }

        AsyncHTTPRequest request = new AsyncHTTPRequest();

        request.TaskType = AsyncHTTPRequest.RequestType.POST;
        request.URLAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_login_url);
        request.UpdateViewParamType = EPIToken.class;
        request.UpdateView = updateView;
        request.IsArray = false;

        Tuple[] params = new Tuple[2];
        params[0] = new Tuple("login", login);
        params[1] = new Tuple("password", password);

        request.execute(params);
    }


    public static void MessagesRequest(String token, IUpdateViewOnPostExecute updateView) throws Exception
    {
        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        AsyncHTTPRequest request = new AsyncHTTPRequest();

        request.TaskType = AsyncHTTPRequest.RequestType.GET;
        request.URLAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_messages_url);
        request.UpdateViewParamType = EPIMessage.class;
        request.UpdateView = updateView;
        request.IsArray = true;

        Tuple[] params = new Tuple[1];
        params[0] = new Tuple("token", token);

        request.execute(params);
    }

    public static void AlertsRequest(String token, IUpdateViewOnPostExecute updateView) throws Exception
    {
        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        AsyncHTTPRequest request = new AsyncHTTPRequest();

        request.TaskType = AsyncHTTPRequest.RequestType.GET;
        request.URLAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_alerts_url);
        request.UpdateViewParamType = EPIAlert.class;
        request.UpdateView = updateView;
        request.IsArray = true;

        Tuple[] params = new Tuple[1];
        params[0] = new Tuple("token", token);

        request.execute(params);
    }

    public static void UserRequest(String token, String login, IUpdateViewOnPostExecute updateView) throws Exception
    {
        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        AsyncHTTPRequest request = new AsyncHTTPRequest();

        request.TaskType = AsyncHTTPRequest.RequestType.GET;
        request.URLAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_user_url);;
        request.UpdateViewParamType = EPIUser.class;
        request.UpdateView = updateView;
        request.IsArray = false;

        Tuple[] params = new Tuple[2];
        params[0] = new Tuple("token", token);
        params[1] = new Tuple("user", login);

        request.execute(params);
    }

    public static void PlanningRequest(String token, String start, String end, IUpdateViewOnPostExecute updateView) throws Exception
    {
        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        AsyncHTTPRequest request = new AsyncHTTPRequest();

        request.TaskType = AsyncHTTPRequest.RequestType.GET;
        request.URLAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_planning_url);
        request.UpdateViewParamType = EPIEventPlanning.class;
        request.UpdateView = updateView;
        request.IsArray = true;

        Tuple[] params = new Tuple[3];
        params[0] = new Tuple("token", token);
        params[1] = new Tuple("start", start);
        params[2] = new Tuple("end", end);

        request.execute(params);
    }

    public static void ValidateTokenRequest(String token,
                                                String scolaryear,
                                                String codemodule,
                                                String codeinstance,
                                                String codeacti, String codeevent,
                                                String tokenvalidationcode,
                                                IUpdateViewOnPostExecute updateView) throws Exception
    {
        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        AsyncHTTPRequest request = new AsyncHTTPRequest();

        request.TaskType = AsyncHTTPRequest.RequestType.POST;
        request.URLAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_validatetoken_url);
        request.UpdateViewParamType = EPIError.class;
        request.UpdateView = updateView;
        request.IsArray = false;


        Tuple[] params = new Tuple[7];
        params[0] = new Tuple("token", token);
        params[1] = new Tuple("scolaryear", scolaryear);
        params[2] = new Tuple("codemodule", codemodule);
        params[3] = new Tuple("codeinstance", codeinstance);
        params[4] = new Tuple("codeacti", codeacti);
        params[5] = new Tuple("codeevent", codeevent);
        params[6] = new Tuple("tokenvalidationcode", tokenvalidationcode);

        request.execute(params);
    }

    public static void RegisterEventRequest(String token,
                                            String scolaryear,
                                            String codemodule,
                                            String codeinstance,
                                            String codeacti,
                                            String codeevent,
                                            IUpdateViewOnPostExecute updateView) throws Exception
    {
        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        AsyncHTTPRequest request = new AsyncHTTPRequest();

        request.TaskType = AsyncHTTPRequest.RequestType.POST;
        request.URLAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_eventregister_url);
        request.UpdateViewParamType = EPIError.class;
        request.UpdateView = updateView;
        request.IsArray = false;

        Tuple[] params = new Tuple[6];
        params[0] = new Tuple("token", token);
        params[1] = new Tuple("scolaryear", scolaryear);
        params[2] = new Tuple("codemodule", codemodule);
        params[3] = new Tuple("codeinstance", codeinstance);
        params[4] = new Tuple("codeacti", codeacti);
        params[5] = new Tuple("codeevent", codeevent);

        request.execute(params);
    }

    public static void UnregisterEventRequest(String token,
                                                  String scolaryear,
                                                  String codemodule,
                                                  String codeinstance,
                                                  String codeacti,
                                                  String codeevent,
                                                  IUpdateViewOnPostExecute updateView) throws Exception
    {
        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        AsyncHTTPRequest request = new AsyncHTTPRequest();

        request.TaskType = AsyncHTTPRequest.RequestType.DELETE;
        request.URLAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_eventregister_url);
        request.UpdateViewParamType = EPIError.class;
        request.UpdateView = updateView;
        request.IsArray = false;

        Tuple[] params = new Tuple[6];
        params[0] = new Tuple("token", token);
        params[1] = new Tuple("scolaryear", scolaryear);
        params[2] = new Tuple("codemodule", codemodule);
        params[3] = new Tuple("codeinstance", codeinstance);
        params[4] = new Tuple("codeacti", codeacti);
        params[5] = new Tuple("codeevent", codeevent);

        request.execute(params);
    }

    public static void GradesRequest(String token, IUpdateViewOnPostExecute updateView) throws Exception
    {
        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        AsyncHTTPRequest request = new AsyncHTTPRequest();

        request.TaskType = AsyncHTTPRequest.RequestType.GET;
        request.URLAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_grades_url);
        request.UpdateViewParamType = EPIGrades.class;
        request.UpdateView = updateView;
        request.IsArray = false;

        Tuple[] params = new Tuple[1];
        params[0] = new Tuple("token", token);

        request.execute(params);
    }


    public static void AllModulesRequest(String token,
                                         Double scolaryear,
                                         String location,
                                         String course,
                                         IUpdateViewOnPostExecute updateView) throws Exception {

        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        AsyncHTTPRequest request = new AsyncHTTPRequest();

        request.TaskType = AsyncHTTPRequest.RequestType.GET;
        request.URLAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_allmodules_url);
        request.UpdateViewParamType = EPIAllModules.class;
        request.UpdateView = updateView;
        request.IsArray = false;

        Tuple[] params = new Tuple[4];
        params[0] = new Tuple("token", token);
        params[1] = new Tuple("scolaryear", scolaryear.toString());
        params[2] = new Tuple("location", location);
        params[3] = new Tuple("course", course);

        request.execute(params);
    }

    public static void ModuleRequest(String token,
                                     String scolaryear,
                                     String codemodule,
                                     String codeinstance,
                                     IUpdateViewOnPostExecute updateView) throws Exception
    {
        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        AsyncHTTPRequest request = new AsyncHTTPRequest();

        request.TaskType = AsyncHTTPRequest.RequestType.GET;
        request.URLAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_module_url);
        request.UpdateViewParamType = EPIModule.class;
        request.UpdateView = updateView;
        request.IsArray = false;

        Tuple[] params = new Tuple[4];
        params[0] = new Tuple("token", token);
        params[1] = new Tuple("scolaryear", scolaryear);
        params[2] = new Tuple("codemodule", codemodule);
        params[3] = new Tuple("codeinstance", codeinstance);

        request.execute(params);
    }

    public static void RegisterModuleRequest(String token,
                                             String scolaryear,
                                             String codemodule,
                                             String codeinstance,
                                             IUpdateViewOnPostExecute updateView) throws Exception
    {
        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        AsyncHTTPRequest request = new AsyncHTTPRequest();

        request.TaskType = AsyncHTTPRequest.RequestType.POST;
        request.URLAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_module_url);
        request.UpdateViewParamType = EPIError.class;
        request.UpdateView = updateView;
        request.IsArray = false;


        Tuple[] params = new Tuple[4];
        params[0] = new Tuple("token", token);
        params[1] = new Tuple("scolaryear", scolaryear);
        params[2] = new Tuple("codemodule", codemodule);
        params[3] = new Tuple("codeinstance", codeinstance);

        request.execute(params);
    }

    public static void UnregisterModuleRequest(String token,
                                               String scolaryear,
                                               String codemodule,
                                               String codeinstance,
                                               IUpdateViewOnPostExecute updateView) throws Exception
    {
        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        AsyncHTTPRequest request = new AsyncHTTPRequest();

        request.TaskType = AsyncHTTPRequest.RequestType.DELETE;
        request.URLAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_module_url);
        request.UpdateViewParamType = EPIError.class;
        request.UpdateView = updateView;
        request.IsArray = false;

        Tuple[] params = new Tuple[4];
        params[0] = new Tuple("token", token);
        params[1] = new Tuple("scolaryear", scolaryear);
        params[2] = new Tuple("codemodule", codemodule);
        params[3] = new Tuple("codeinstance", codeinstance);

        request.execute(params);
    }

    public static void UnregisterProjectRequest(String token,
                                                String scolaryear,
                                                String codemodule,
                                                String codeinstance,
                                                String codeacti,
                                                IUpdateViewOnPostExecute updateView) throws Exception
    {
        AsyncHTTPRequest request = new AsyncHTTPRequest();

        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        request.TaskType = AsyncHTTPRequest.RequestType.DELETE;
        request.URLAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_projectregister_url);
        request.UpdateViewParamType = EPIError.class;
        request.UpdateView = updateView;
        request.IsArray = false;

        Tuple[] params = new Tuple[5];
        params[0] = new Tuple("token", token);
        params[1] = new Tuple("scolaryear", scolaryear);
        params[2] = new Tuple("codemodule", codemodule);
        params[3] = new Tuple("codeinstance", codeinstance);
        params[4] = new Tuple("codeacti", codeacti);

        request.execute(params);
    }

    public static void RegisterProjectRequest(String token,
                                              String scolaryear,
                                              String codemodule,
                                              String codeinstance,
                                              String codeacti,
                                              IUpdateViewOnPostExecute updateView) throws Exception
    {
        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        AsyncHTTPRequest request = new AsyncHTTPRequest();

        request.TaskType = AsyncHTTPRequest.RequestType.POST;
        request.URLAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_projectregister_url);
        request.UpdateViewParamType = EPIError.class;
        request.UpdateView = updateView;
        request.IsArray = false;

        Tuple[] params = new Tuple[5];
        params[0] = new Tuple("token", token);
        params[1] = new Tuple("scolaryear", scolaryear);
        params[2] = new Tuple("codemodule", codemodule);
        params[3] = new Tuple("codeinstance", codeinstance);
        params[4] = new Tuple("codeacti", codeacti);

        request.execute(params);
    }

    public static void ProjectsRequest(String token, IUpdateViewOnPostExecute updateView) throws Exception
    {
        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        AsyncHTTPRequest request = new AsyncHTTPRequest();

        request.TaskType = AsyncHTTPRequest.RequestType.GET;
        request.URLAddress = ApplicationContextProvider.getContext().getString(R.string.epitech_api_url) + ApplicationContextProvider.getContext().getString(R.string.api_projects_url);
        request.UpdateViewParamType = EPIProject.class;
        request.UpdateView = updateView;
        request.IsArray = true;

        Tuple[] params = new Tuple[1];
        params[0] = new Tuple("token", token);

        request.execute(params);
    }

    public static void LoadImageFromUrl(String url, ILoadImageOnPostExecute loadImage) throws Exception{
        if (!Utils.isNetworkAvailable())
            throw new EPINetworkException();

        AsyncDrawableRequest request = new AsyncDrawableRequest();

        request.UpdateView = loadImage;

        request.execute(url);
    }
}
