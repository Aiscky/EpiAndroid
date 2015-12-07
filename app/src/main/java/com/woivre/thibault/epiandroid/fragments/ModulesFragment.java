package com.woivre.thibault.epiandroid.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.woivre.thibault.epiandroid.request.IUpdateViewOnPostExecute;
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

        try
        {
            RequestManager.UserRequest(this.token, this.login, new UpdateUserInfos(getActivity(), view));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        /* DYNAMICALLY FILL VIEW */

        ModulesListView = (ListView)view.findViewById(R.id.modules_list);
        ModulesListView.setAdapter(new ModulesAdapter(this.getActivity(), new ArrayList<EPIAllModules.Item>(), this.token));

        /* SET SPINNER */

        final Spinner modulesSpinner = (Spinner)view.findViewById(R.id.modules_spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item);

        modulesSpinner.setAdapter(spinnerAdapter);

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
                v.getRootView().findViewById(R.id.modules_list).setVisibility(View.VISIBLE);
            }
        });

        /* GETTING ALL MODULES INFOS */

        try
        {
            RequestManager.AllModulesRequest(token, 1999.0, location, course_code, new UpdateModulesView(getActivity(), view));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        return view;
    }

    public class UpdateUserInfos implements IUpdateViewOnPostExecute
    {
        private Context mContext;
        private View mView;

        public UpdateUserInfos(Context context, View view)
        {
            mContext = context;
            mView = view;
        }

        @Override
        public void UpdateView(EPIJSONObject[] objs) {
            if (objs.length != 0 && objs[0] instanceof EPIError) {

            }
            else if (objs.length != 0 && objs[0] instanceof EPIUser)
            {
                location = ((EPIUser) objs[0]).location;
                course_code = ((EPIUser) objs[0]).course_code;
            }
        }
    }

    public class UpdateModulesView implements IUpdateViewOnPostExecute
    {
        Context mContext;
        View mView;

        public UpdateModulesView(Context context, View view)
        {
            mContext = context;
            mView = view;
        }

        @Override
        public void UpdateView(EPIJSONObject[] objs) {
            if (objs.length != 0 && objs[0] instanceof EPIError) {

            }
            else if (objs.length != 0 && objs[0] instanceof EPIAllModules)
            {
                ArrayList<String> semesters = new ArrayList<>();
                ArrayList<EPIAllModules.Item> modules = new ArrayList<>();

                for (EPIAllModules.Item item : ((EPIAllModules) objs[0]).items)
                {
                    modules.add(item);
                    if (!semesters.contains(((Integer) item.semester.intValue()).toString()))
                    {
                        semesters.add(((Integer) item.semester.intValue()).toString());
                    }
                }

                ((ModulesAdapter) ((ListView) mView.findViewById(R.id.modules_list)).getAdapter()).ModulesList = modules;
                ((ModulesAdapter) ((ListView) mView.findViewById(R.id.modules_list)).getAdapter()).arrayList.addAll(modules);
                ((ModulesAdapter) ((ListView) mView.findViewById(R.id.modules_list)).getAdapter()).notifyDataSetChanged();

                ((ArrayAdapter<String>) ((Spinner) mView.findViewById(R.id.modules_spinner)).getAdapter()).addAll(semesters);
                ((ArrayAdapter<String>) ((Spinner) mView.findViewById(R.id.modules_spinner)).getAdapter()).notifyDataSetChanged();
            }
        }
    }
}
