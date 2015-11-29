package com.woivre.thibault.epiandroid.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.woivre.thibault.epiandroid.R;
import com.woivre.thibault.epiandroid.activities.LoginActivity;
import com.woivre.thibault.epiandroid.adapters.PlanningAdapter;
import com.woivre.thibault.epiandroid.objects.EPIError;
import com.woivre.thibault.epiandroid.objects.EPIEventPlanning;
import com.woivre.thibault.epiandroid.objects.EPIJSONObject;
import com.woivre.thibault.epiandroid.objects.EPIMessage;
import com.woivre.thibault.epiandroid.request.EPINetworkException;
import com.woivre.thibault.epiandroid.request.RequestManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PlanningFragment extends android.app.Fragment {

    String login;
    String password;
    String token;
    java.util.Date date;
    ListView EventsListView;
    ArrayList<EPIEventPlanning> EventsList;

    public PlanningFragment()
    {
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
        View view = inflater.inflate(R.layout.fragment_planning, container, false);

        /* GETTING PLANNING DAY INFORMATIONS */

        date = new java.util.Date();
        try {
            retrieveDayData();
        } catch (Exception e) {
            return view;
        }

        ((TextView)view.findViewById(R.id.planning_displayday)).setText(new SimpleDateFormat("dd MM yyyy").format(date));

        /* SETTING LISTVIEW */

        EventsListView = (ListView)view.findViewById(R.id.planningevents_list);
        EventsListView.setAdapter(new PlanningAdapter(this.getActivity(), EventsList, this.token));

        /* SET CLICKLISTENER */

        view.findViewById(R.id.prevday_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(Calendar.DATE, -1);
                date = c.getTime();
                try {
                    retrieveDayData();
                } catch (Exception e) {
                    return;
                }
                ((PlanningAdapter)EventsListView.getAdapter()).arrayList = EventsList;
                ((PlanningAdapter)EventsListView.getAdapter()).EPIEventsList = EventsList;
                ((PlanningAdapter)EventsListView.getAdapter()).filter();
                ((TextView)v.getRootView().findViewById(R.id.planning_alert)).setVisibility(View.GONE);
                ((TextView)v.getRootView().findViewById(R.id.planning_displayday)).setText(new SimpleDateFormat("dd MM yyyy").format(date));
            }
        });

        view.findViewById(R.id.nextday_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(Calendar.DATE, 1);
                date = c.getTime();
                try {
                    retrieveDayData();
                } catch (Exception e) {
                    return;
                }
                ((PlanningAdapter)EventsListView.getAdapter()).arrayList = EventsList;
                ((PlanningAdapter)EventsListView.getAdapter()).EPIEventsList = EventsList;
                ((PlanningAdapter)EventsListView.getAdapter()).filter();
                ((TextView)v.getRootView().findViewById(R.id.planning_alert)).setVisibility(View.GONE);
                ((TextView)v.getRootView().findViewById(R.id.planning_displayday)).setText(new SimpleDateFormat("dd MM yyyy").format(date));
            }
        });

        /* SETTINGCHECKEDLISTENER */

        ((CheckBox)view.findViewById(R.id.planningevents_registeredcheckbox)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((PlanningAdapter)EventsListView.getAdapter()).RegisteredFilter = isChecked;
                ((PlanningAdapter)EventsListView.getAdapter()).filter();
            }
        });

        ((CheckBox)view.findViewById(R.id.planningevents_modulescheckbox)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((PlanningAdapter)EventsListView.getAdapter()).ModulesFilter = isChecked;
                ((PlanningAdapter)EventsListView.getAdapter()).filter();
            }
        });

        return view;
    }

    /* DATA FONCTIONS */

    public void retrieveDayData() throws Exception
    {
        try {
            String currentDay;

            currentDay = new SimpleDateFormat("yyyy-MM-dd").format(date);
            EPIJSONObject[] rObj = RequestManager.PlanningRequest(token, currentDay, currentDay);
            if (rObj.length != 0 && rObj[0] instanceof EPIError)
            {  }
            else
            {
                ArrayList<EPIEventPlanning> eventsList = new ArrayList<EPIEventPlanning>();
                for (EPIJSONObject event : rObj)
                {
                    eventsList.add((EPIEventPlanning)event);
                }
                this.EventsList = eventsList;
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
