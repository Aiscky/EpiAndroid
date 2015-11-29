package com.woivre.thibault.epiandroid.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.woivre.thibault.epiandroid.R;
import com.woivre.thibault.epiandroid.activities.LoginActivity;
import com.woivre.thibault.epiandroid.adapters.ProjectAdapter;
import com.woivre.thibault.epiandroid.objects.EPIAllModules;
import com.woivre.thibault.epiandroid.objects.EPIError;
import com.woivre.thibault.epiandroid.objects.EPIJSONObject;
import com.woivre.thibault.epiandroid.objects.EPIProject;
import com.woivre.thibault.epiandroid.request.EPINetworkException;
import com.woivre.thibault.epiandroid.request.RequestManager;

import java.util.ArrayList;

/**
 * Created by dylan on 29/11/2015.
 */
public class ProjectFragment extends android.app.Fragment {
    String login;
    String password;
    String token;
    ListView ProjectListView;
    ArrayList<EPIProject> ProjectList;

    public ProjectFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* GETTING DATA FROM API */

        Bundle b = this.getArguments();
        login = b.getString(LoginActivity.LOGIN);
        password = b.getString(LoginActivity.PASSWORD);
        token = b.getString(LoginActivity.TOKEN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_projects, container, false);

        try {
            retrieveDayData();
        } catch (Exception e) {
            return view;
        }

        ProjectListView = (ListView)view.findViewById(R.id.project_list);
        ProjectListView.setAdapter(new ProjectAdapter(this.getActivity(), ProjectList, token));

        ((CheckBox)view.findViewById(R.id.project_registeredcheckbox)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    ((ProjectAdapter) ProjectListView.getAdapter()).RegisteredFilter = 0;
                else
                    ((ProjectAdapter) ProjectListView.getAdapter()).RegisteredFilter = 1;
                ((ProjectAdapter) ProjectListView.getAdapter()).filter();
            }
        });
        return view;
    }

    public void retrieveDayData() throws Exception
    {
        try {
            EPIJSONObject[] rObj = RequestManager.ProjectsRequest(token);
            if (rObj.length != 0 && rObj[0] instanceof EPIError)
            {  }
            else
            {
                ArrayList<EPIProject> projectList = new ArrayList<EPIProject>();
                for (EPIJSONObject event : rObj)
                {
                    projectList.add((EPIProject)event);
                }
                this.ProjectList = projectList;
            }
        } catch (EPINetworkException e) {
            e.printStackTrace();
            throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception();
        }
    }
}
