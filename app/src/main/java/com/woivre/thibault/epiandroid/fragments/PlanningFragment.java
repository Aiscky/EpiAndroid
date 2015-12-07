package com.woivre.thibault.epiandroid.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.woivre.thibault.epiandroid.R;
import com.woivre.thibault.epiandroid.activities.LoginActivity;
import com.woivre.thibault.epiandroid.adapters.PlanningAdapter;
import com.woivre.thibault.epiandroid.objects.EPIError;
import com.woivre.thibault.epiandroid.objects.EPIEventPlanning;
import com.woivre.thibault.epiandroid.objects.EPIJSONObject;
import com.woivre.thibault.epiandroid.request.IUpdateViewOnPostExecute;
import com.woivre.thibault.epiandroid.request.RequestManager;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class PlanningFragment extends android.app.Fragment {

    String login;
    String password;
    String token;
    java.util.Date date;
    DatePickerDialog datePicker = null;
    View planningView;

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

        planningView = view;

        /* GETTING PLANNING DAY INFORMATIONS */

        date = new java.util.Date();

        try
        {
            String currentDay = new SimpleDateFormat("yyyy-MM-dd").format(date);
            RequestManager.PlanningRequest(token, currentDay, currentDay, new UpdatePlanningView(view));
        }
        catch (Exception e)
        {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        ((TextView)view.findViewById(R.id.planning_displayday)).setText(new SimpleDateFormat("dd MM yyyy").format(date));

        /* SETTING LISTVIEW */

        ((ListView)view.findViewById(R.id.planningevents_list)).setAdapter(new PlanningAdapter(this.getActivity(), new ArrayList<EPIEventPlanning>(), this.token));

        /* SET CLICKLISTENER */

        view.findViewById(R.id.prevday_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(Calendar.DATE, -1);
                date = c.getTime();

                try
                {
                    String currentDay = new SimpleDateFormat("yyyy-MM-dd").format(date);
                    RequestManager.PlanningRequest(token, currentDay, currentDay, new UpdatePlanningView(v.getRootView()));
                }
                catch (Exception e)
                {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

                v.getRootView().findViewById(R.id.planning_alert).setVisibility(View.GONE);
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

                try
                {
                    String currentDay = new SimpleDateFormat("yyyy-MM-dd").format(date);
                    RequestManager.PlanningRequest(token, currentDay, currentDay, new UpdatePlanningView(v.getRootView()));
                }
                catch (Exception e)
                {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

                v.getRootView().findViewById(R.id.planning_alert).setVisibility(View.GONE);
                ((TextView)v.getRootView().findViewById(R.id.planning_displayday)).setText(new SimpleDateFormat("dd MM yyyy").format(date));
            }
        });

        view.findViewById(R.id.planning_daypicker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();
                c.setTime(date);
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                if (datePicker == null) {
                    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar c = Calendar.getInstance();

                            c.set(year, monthOfYear, dayOfMonth);
                            date = c.getTime();

                            try
                            {
                                String currentDay = new SimpleDateFormat("yyyy-MM-dd").format(date);
                                RequestManager.PlanningRequest(token, currentDay, currentDay, new UpdatePlanningView(planningView));
                            }
                            catch (Exception e)
                            {
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }

                            planningView.findViewById(R.id.planning_alert).setVisibility(View.GONE);
                            ((TextView)planningView.findViewById(R.id.planning_displayday)).setText(new SimpleDateFormat("dd MM yyyy").format(date));
                        }
                    };

                    datePicker = new DatePickerDialog(getActivity(), onDateSetListener, year, month, day);
                }
                else
                {
                    datePicker.updateDate(year, month, day);
                }
                datePicker.show();
            }
        });

        /* SETTINGCHECKEDLISTENER */

        ((CheckBox)view.findViewById(R.id.planningevents_registeredcheckbox)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((PlanningAdapter) ((ListView) buttonView.getRootView().findViewById(R.id.planningevents_list)).getAdapter()).RegisteredFilter = isChecked;
                ((PlanningAdapter) ((ListView) buttonView.getRootView().findViewById(R.id.planningevents_list)).getAdapter()).filter();
            }
        });

        ((CheckBox)view.findViewById(R.id.planningevents_modulescheckbox)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((PlanningAdapter)((ListView) buttonView.getRootView().findViewById(R.id.planningevents_list)).getAdapter()).RegisteredFilter = isChecked;
                ((PlanningAdapter)((ListView) buttonView.getRootView().findViewById(R.id.planningevents_list)).getAdapter()).filter();
            }
        });

        return view;
    }



    public class UpdatePlanningView implements IUpdateViewOnPostExecute
    {
        View mView;

        public UpdatePlanningView(View view)
        {
            mView = view;
        }

        @Override
        public void UpdateView(EPIJSONObject[] objs) {
            if (objs.length != 0 && objs[0] instanceof EPIError)
            {
                //TODO MAKE ERROR
            }
            else if (objs.length == 0 || objs[0] instanceof EPIEventPlanning)
            {
                PlanningAdapter pAdapter = ((PlanningAdapter) ((ListView) mView.findViewById(R.id.planningevents_list)).getAdapter());

                ArrayList<EPIEventPlanning> tmp = new ArrayList<>();

                for (EPIJSONObject event : objs)
                {
                    tmp.add(((EPIEventPlanning) event));
                }

                Collections.sort(tmp, new EventDateComparator());

                pAdapter.arrayList.clear();
                pAdapter.EPIEventsList = tmp;
                pAdapter.arrayList.addAll(tmp);

                pAdapter.filter();
            }
        }
    }

    public class EventDateComparator implements Comparator<EPIEventPlanning>
    {
        @Override
        public int compare(EPIEventPlanning lhs, EPIEventPlanning rhs) {
            if (lhs.start == null)
                return -1;
            if (rhs.start == null)
                return 1;
            try {
                java.util.Date ldate = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").parse(lhs.start);
                java.util.Date rdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").parse(rhs.start);
                if (ldate.compareTo(rdate) == 0 && lhs.end != null && rhs.end != null)
                {
                    ldate = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").parse(lhs.end);
                    rdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").parse(rhs.end);
                    return ldate.compareTo(rdate);
                }
                return ldate.compareTo(rdate);
            } catch (ParseException e) {
                e.printStackTrace();
                return 1;
            }
        }
    }
}
