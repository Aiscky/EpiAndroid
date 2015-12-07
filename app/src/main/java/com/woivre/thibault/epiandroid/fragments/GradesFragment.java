package com.woivre.thibault.epiandroid.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.woivre.thibault.epiandroid.R;
import com.woivre.thibault.epiandroid.activities.LoginActivity;
import com.woivre.thibault.epiandroid.adapters.GradesAdapter;
import com.woivre.thibault.epiandroid.objects.EPIError;
import com.woivre.thibault.epiandroid.objects.EPIGrades;
import com.woivre.thibault.epiandroid.objects.EPIJSONObject;
import com.woivre.thibault.epiandroid.request.IUpdateViewOnPostExecute;
import com.woivre.thibault.epiandroid.request.RequestManager;

import java.util.ArrayList;
import java.util.Collections;

public class GradesFragment extends android.app.Fragment {

    ListView GradesListView;
    String login;
    String password;
    String token;

    public static final String NO_SELECTION_SCOLARYEAR = "All years";
    public static final String NO_SELECTION_MODULE = "All modules";

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

        try
        {
            RequestManager.GradesRequest(this.token, new UpdateGradesView(view, getActivity()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        /* SPINNER SETTINGS */

        GradesListView = ((ListView)view.findViewById(R.id.grades_list));

        ((ListView)view.findViewById(R.id.grades_list)).setAdapter(new GradesAdapter(this.getActivity(), new ArrayList<EPIGrades.Grade>()));

        Spinner gradesModulesSpinner = (Spinner)view.findViewById(R.id.grades_modulesspinner);
        ArrayAdapter<String> spinnerModulesAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item);

        gradesModulesSpinner.setAdapter(spinnerModulesAdapter);

        /* SPINNER LISTENER SETUP */

        gradesModulesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((GradesAdapter) GradesListView.getAdapter()).ModulesFilter = ((String)parent.getItemAtPosition(position));
                ((GradesAdapter) GradesListView.getAdapter()).filter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }


    class UpdateGradesView implements IUpdateViewOnPostExecute
    {
        View mView;
        Context mContext;

        public UpdateGradesView(View view, Context context)
        {
            mView = view;
            mContext = context;
        }

        @Override
        public void UpdateView(EPIJSONObject[] objs) {
            if (objs.length != 0 && objs[0] instanceof EPIError) {
                new android.app.AlertDialog.Builder(mContext)
                        .setTitle("Error")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setMessage("Failed to retrieve grades")
                        .show();
            } else {
                ArrayList<EPIGrades.Grade> gradesList = new ArrayList<>();
                ArrayList<String> modules = new ArrayList<>();

                for (EPIGrades.Grade grade : ((EPIGrades)objs[0]).notes)
                {
                    gradesList.add(grade);
                    if (!modules.contains(grade.titlemodule))
                    {
                        modules.add(grade.titlemodule);
                    }
                }

                Collections.reverse(gradesList);
                Collections.reverse(modules);

                ((ArrayAdapter<String>) ((Spinner) mView.findViewById(R.id.grades_modulesspinner)).getAdapter()).add(new String(NO_SELECTION_MODULE));
                ((ArrayAdapter<String>) ((Spinner) mView.findViewById(R.id.grades_modulesspinner)).getAdapter()).addAll(modules);
                ((ArrayAdapter<String>) ((Spinner) mView.findViewById(R.id.grades_modulesspinner)).getAdapter()).notifyDataSetChanged();

                ((GradesAdapter) ((ListView) mView.findViewById(R.id.grades_list)).getAdapter()).GradesList = gradesList;
                ((GradesAdapter) ((ListView) mView.findViewById(R.id.grades_list)).getAdapter()).arrayList = new ArrayList<>();
                ((GradesAdapter) ((ListView) mView.findViewById(R.id.grades_list)).getAdapter()).arrayList.addAll(gradesList);
            }
        }
    }
}
