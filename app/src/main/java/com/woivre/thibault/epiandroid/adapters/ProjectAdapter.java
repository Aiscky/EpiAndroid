package com.woivre.thibault.epiandroid.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.woivre.thibault.epiandroid.R;
import com.woivre.thibault.epiandroid.objects.EPIAllModules;
import com.woivre.thibault.epiandroid.objects.EPIError;
import com.woivre.thibault.epiandroid.objects.EPIGrades;
import com.woivre.thibault.epiandroid.objects.EPIProject;
import com.woivre.thibault.epiandroid.request.EPINetworkException;
import com.woivre.thibault.epiandroid.request.RequestManager;

import java.util.ArrayList;
import java.util.List;

public class ProjectAdapter extends BaseAdapter {
    Context mContext;
    public List<EPIProject> ProjectsList;
    public ArrayList<EPIProject> arrayList;
    LayoutInflater inflater;
    public Integer RegisteredFilter = 0;
    String token;

    public ProjectAdapter(Context context, List<EPIProject> projectList, String token)
    {
        mContext = context;
        this.ProjectsList = projectList;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<EPIProject>();
        this.arrayList.addAll(projectList);
        this.token = token;
    }

    @Override
    public int getCount() {
        Log.d("SIZE", ((Integer) ProjectsList.size()).toString());
        return ProjectsList.size();
    }

    @Override
    public Object getItem(int position) {
        return ProjectsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder
    {
        TextView project;
        TextView title_module;
        Button register;
        Button unregister;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.projectlist_item, null);
            holder = new ViewHolder();
            holder.project = (TextView)convertView.findViewById(R.id.project_title);
            holder.title_module = (TextView)convertView.findViewById(R.id.module_title);
            holder.register = (Button) convertView.findViewById(R.id.project_register);
            holder.unregister = (Button) convertView.findViewById(R.id.project_unregister);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        if (ProjectsList.get(position).acti_title != null)
            holder.project.setText(ProjectsList.get(position).acti_title);

        if (ProjectsList.get(position).title_module != null)
            holder.title_module.setText(ProjectsList.get(position).title_module);

        if (ProjectsList.get(position).registered == 0) {
//            holder.register.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    try {
//                        EPIProject obj = ProjectsList.get(position);
//
//                        EPIError error = RequestManager.RegisterProjectRequest(token, obj.scolaryear, obj.codemodule, obj.codeinstance, obj.codeacti);
//                        if (error != null)
//                        {
//                            ((TextView)v.getRootView().findViewById(R.id.project_alert)).setText("Impossible to register to : " + obj.acti_title);
//                            ((TextView)v.getRootView().findViewById(R.id.project_alert)).setVisibility(View.VISIBLE);
//                            holder.register.setVisibility(View.GONE);
//                        }
//                        else
//                        {
//                            obj.registered = 1;
//                            ((TextView)v.getRootView().findViewById(R.id.project_alert)).setText("Registered to activity : " + obj.acti_title);
//                            ((TextView)v.getRootView().findViewById(R.id.project_alert)).setVisibility(View.VISIBLE);
//                            holder.unregister.setVisibility(View.VISIBLE);
//                            holder.register.setVisibility(View.GONE);
//                        }
//                    }
//                    catch (EPINetworkException e)
//                    {
//                        e.printStackTrace();
//                    }
//                    catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//                }
//            });
        }
        else {
            holder.register.setVisibility(View.GONE);
        }

//        holder.unregister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    EPIProject obj = ProjectsList.get(position);
//
//                    EPIError error = RequestManager.UnregisterProjectRequest(token, obj.scolaryear, obj.codemodule, obj.codeinstance, obj.codeacti);
//                    if (error != null) {
//                        holder.unregister.setVisibility(View.GONE);
//                    }
//                    else
//                    {
//                        obj.registered = 0;
//                        ((TextView) v.getRootView().findViewById(R.id.planning_alert)).setText("Unregistered to activity : " + obj.acti_title);
//                        ((TextView) v.getRootView().findViewById(R.id.planning_alert)).setVisibility(View.VISIBLE);
//                        holder.unregister.setVisibility(View.GONE);
//                        holder.register.setVisibility(View.VISIBLE);
//                    }
//                } catch (EPINetworkException e) {
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        return convertView;
    }

    public void filter()
    {
        this.ProjectsList = new ArrayList<EPIProject>();

        for (EPIProject event : arrayList)
        {
            if (RegisteredFilter == 1 || (RegisteredFilter == 0 && event.registered == 1))
            {
                this.ProjectsList.add(event);
            }
        }

        notifyDataSetChanged();
    }
}
