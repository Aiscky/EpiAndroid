package com.woivre.thibault.epiandroid.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.woivre.thibault.epiandroid.R;
import com.woivre.thibault.epiandroid.activities.LoginActivity;
import com.woivre.thibault.epiandroid.adapters.GradesAdapter;
import com.woivre.thibault.epiandroid.objects.EPIError;
import com.woivre.thibault.epiandroid.objects.EPIGrades;
import com.woivre.thibault.epiandroid.objects.EPIJSONObject;
import com.woivre.thibault.epiandroid.objects.EPIMessage;
import com.woivre.thibault.epiandroid.request.EPINetworkException;
import com.woivre.thibault.epiandroid.request.RequestManager;

import java.util.ArrayList;
import java.util.Collections;

public class GradesFragment extends android.app.Fragment {

    ListView GradesListView;
    String login;
    String password;
    String token;

    public GradesFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_grades, container, false);

        /* ADD CONTENT DYNAMICALLY TO VIEW */

        ArrayList<EPIGrades.Grade> gradesList = new ArrayList<EPIGrades.Grade>();
        ArrayList<String> scolaryears = new ArrayList<String>();

        try
        {
            EPIJSONObject objs = RequestManager.GradesRequest(this.token);
            if (objs != null && objs instanceof EPIError)
            {
                //Handle Error EPIError
            }
            else if (objs instanceof EPIGrades)
            {
                for (EPIGrades.Grade grade : ((EPIGrades) objs).notes)
                {
                    gradesList.add(grade);
                    if (!scolaryears.contains(((Integer) grade.scolaryear.intValue()).toString()))
                    {
                        scolaryears.add(((Integer) grade.scolaryear.intValue()).toString());
                    }
                }
                Collections.reverse(gradesList);
                Collections.reverse(scolaryears);
            }
        }
        catch (EPINetworkException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        /* HANDLING LISTVIEW */

        GradesListView = (ListView)view.findViewById(R.id.grades_list);
        GradesListView.setAdapter(new GradesAdapter(this.getActivity(), gradesList));

        final Spinner gradesSpinner = (Spinner)view.findViewById(R.id.grades_spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item);

        gradesSpinner.setAdapter(spinnerAdapter);
        spinnerAdapter.addAll(scolaryears);
        ((ArrayAdapter<Double>)gradesSpinner.getAdapter()).notifyDataSetChanged();

        /* SPINNER LISTENER SETUP */

        gradesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((GradesAdapter) GradesListView.getAdapter()).filter(((String)parent.getItemAtPosition(position)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

}
