package com.woivre.thibault.epiandroid.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.woivre.thibault.epiandroid.R;
import com.woivre.thibault.epiandroid.activities.LoginActivity;
import com.woivre.thibault.epiandroid.activities.MainActivity;
import com.woivre.thibault.epiandroid.adapters.ModulesAdapter;
import com.woivre.thibault.epiandroid.objects.EPIAllModules;
import com.woivre.thibault.epiandroid.objects.EPIError;
import com.woivre.thibault.epiandroid.objects.EPIJSONObject;
import com.woivre.thibault.epiandroid.objects.EPIUser;
import com.woivre.thibault.epiandroid.request.EPINetworkException;
import com.woivre.thibault.epiandroid.request.RequestManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ModulesFragment extends android.app.Fragment {
    String login;
    String password;
    String token;
    String location;
    String course_code;
    Double currentYear;
    ListView ModulesListView;

    public ModulesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = this.getArguments();
        login = b.getString(LoginActivity.LOGIN);
        password = b.getString(LoginActivity.PASSWORD);
        token = b.getString(LoginActivity.TOKEN);
        location = b.getString("LOCATION");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modules, container, false);

        /* GETTING USER INFOS */

        try {
            EPIJSONObject rObj = RequestManager.UserRequest(this.token, this.login);
            if (rObj instanceof EPIError) {
                //TODO Make EpiError
            }
            else
            {
                location = ((EPIUser) rObj).location;
                course_code = ((EPIUser) rObj).course_code;
            }
        } catch (EPINetworkException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        currentYear = Double.parseDouble( new SimpleDateFormat("yyyy").format(new java.util.Date()));
        ((TextView) view.findViewById(R.id.modules_scolaryear)).setText(((Integer) currentYear.intValue()).toString());

        Log.d("INFO", ((TextView) view.findViewById(R.id.modules_scolaryear)).getText().toString());

        /* GETTING ALL MODULES INFOS */

        ArrayList<EPIAllModules.Item> modulesData = new ArrayList<EPIAllModules.Item>();

        try {
            EPIJSONObject rObj = RequestManager.AllModulesRequest(token, currentYear, location, course_code);
            if (rObj instanceof EPIError) {
                //TODO Make EpiError
            }
            else
            {
                for (EPIAllModules.Item item : ((EPIAllModules) rObj).items)
                {
                    modulesData.add(item);
                }
            }
        } catch (EPINetworkException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* DYNAMICALLY FILL VIEW */

        ModulesListView = (ListView)view.findViewById(R.id.modules_list);
        ModulesListView.setAdapter(new ModulesAdapter(this.getActivity(), modulesData));

        /* SET BUTTONSLISTENER */

        ((Button) view.findViewById(R.id.modules_prev)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ((Button) view.findViewById(R.id.modules_prev)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    private void GetModulesFromYear()
    {
        ArrayList<EPIAllModules.Item> modulesData = new ArrayList<EPIAllModules.Item>();

        try {
            EPIJSONObject rObj = RequestManager.AllModulesRequest(token, currentYear, location, course_code);
            if (rObj instanceof EPIError) {
                //TODO Make EpiError
            }
            else
            {
                for (EPIAllModules.Item item : ((EPIAllModules) rObj).items)
                {
                    modulesData.add(item);
                }
            }
        } catch (EPINetworkException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ((ModulesAdapter) ModulesListView.getAdapter()).ModulesList = modulesData;
    }
}
