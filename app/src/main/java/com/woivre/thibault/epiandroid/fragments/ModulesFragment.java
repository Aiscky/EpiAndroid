package com.woivre.thibault.epiandroid.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.woivre.thibault.epiandroid.R;
import com.woivre.thibault.epiandroid.activities.LoginActivity;
import com.woivre.thibault.epiandroid.activities.MainActivity;
import com.woivre.thibault.epiandroid.adapters.ModulesAdapter;
import com.woivre.thibault.epiandroid.adapters.PlanningAdapter;
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


        /* GETTING ALL MODULES INFOS */

        ArrayList<EPIAllModules.Item> modulesData = new ArrayList<EPIAllModules.Item>();
        ArrayList<String> semesters = new ArrayList<String>();

        try {
            EPIJSONObject rObj = RequestManager.AllModulesRequest(token, 1999.0, location, course_code);
            if (rObj instanceof EPIError) {

            }
            else
            {
                for (EPIAllModules.Item item : ((EPIAllModules) rObj).items)
                {
                    modulesData.add(item);
                    if (!semesters.contains(((Integer) item.semester.intValue()).toString()))
                    {
                        semesters.add(((Integer) item.semester.intValue()).toString());
                    }
                }
            }
        } catch (EPINetworkException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* DYNAMICALLY FILL VIEW */

        ModulesListView = (ListView)view.findViewById(R.id.modules_list);
        ModulesListView.setAdapter(new ModulesAdapter(this.getActivity(), modulesData, this.token));

        /* SET SPINNER */

        final Spinner modulesSpinner = (Spinner)view.findViewById(R.id.modules_spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item);

        modulesSpinner.setAdapter(spinnerAdapter);
        spinnerAdapter.addAll(semesters);
        ((ArrayAdapter<String>)modulesSpinner.getAdapter()).notifyDataSetChanged();

        modulesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((ModulesAdapter) ((ListView) getView().findViewById(R.id.modules_list)).getAdapter()).semesterFilter = Double.parseDouble((String) parent.getItemAtPosition(position));
                ((ModulesAdapter) ((ListView) getView().findViewById(R.id.modules_list)).getAdapter()).filter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((ModulesAdapter) ((ListView) getView().findViewById(R.id.modules_list)).getAdapter()).semesterFilter = null;
                ((ModulesAdapter) ((ListView) getView().findViewById(R.id.modules_list)).getAdapter()).filter();
            }
        });

        /* SET CHECKBOX */

        ((CheckBox)view.findViewById(R.id.modules_registeredcheckbox)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((ModulesAdapter)ModulesListView.getAdapter()).registeredFilter = isChecked;
                ((ModulesAdapter)ModulesListView.getAdapter()).filter();
            }
        });

        /* SET BUTTONLISTENER */

        ((Button) view.findViewById(R.id.modules_collapse)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getRootView().findViewById(R.id.modules_infoslayout).setVisibility(View.GONE);
            }
        });

        return view;
    }
}
