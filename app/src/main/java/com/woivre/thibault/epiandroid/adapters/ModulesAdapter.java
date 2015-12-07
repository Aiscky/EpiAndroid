package com.woivre.thibault.epiandroid.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.woivre.thibault.epiandroid.R;
import com.woivre.thibault.epiandroid.activities.LoginActivity;
import com.woivre.thibault.epiandroid.objects.EPIAllModules;
import com.woivre.thibault.epiandroid.objects.EPIError;
import com.woivre.thibault.epiandroid.objects.EPIJSONObject;
import com.woivre.thibault.epiandroid.objects.EPIModule;
import com.woivre.thibault.epiandroid.request.EPINetworkException;
import com.woivre.thibault.epiandroid.request.IUpdateViewOnPostExecute;
import com.woivre.thibault.epiandroid.request.RequestManager;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Thibault on 29/11/2015.
 */
public class ModulesAdapter extends BaseAdapter {
    Context mContext;
    public List<EPIAllModules.Item> ModulesList;
    public ArrayList<EPIAllModules.Item> arrayList;
    LayoutInflater inflater;
    public Boolean registeredFilter = false;
    public Double semesterFilter = null;
    String token;

    public ModulesAdapter(Context context, List<EPIAllModules.Item> modulesList, String token)
    {
        mContext = context;
        ModulesList = modulesList;
        arrayList = new ArrayList<EPIAllModules.Item>();
        arrayList.addAll(modulesList);
        inflater = LayoutInflater.from(mContext);
        this.token = token;
    }

    @Override
    public int getCount() {
        return ModulesList.size();
    }

    @Override
    public Object getItem(int position) {
        return ModulesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder
    {
        TextView title;
        TextView credits;
        TextView semester;
        TextView start;
        TextView end;
        TextView scolaryear;

        Button expand;
        Button register;
        Button unregister;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.moduleslist_item, null);
            holder = new ViewHolder();
            holder.title = (TextView)convertView.findViewById(R.id.module_title);
            holder.credits = (TextView)convertView.findViewById(R.id.module_credits);
            holder.semester = (TextView)convertView.findViewById(R.id.module_semester);
            holder.start = (TextView)convertView.findViewById(R.id.module_start);
            holder.end = (TextView)convertView.findViewById(R.id.module_end);
            holder.scolaryear = (TextView)convertView.findViewById(R.id.modules_scolaryear);
            holder.expand = (Button)convertView.findViewById(R.id.module_expand);
            holder.register = (Button)convertView.findViewById(R.id.module_register);
            holder.unregister = (Button)convertView.findViewById(R.id.module_unregister);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        final EPIAllModules.Item module = ModulesList.get(position);

        if (module.title != null)
        {
            holder.title.setText(module.title);
        }
        if (module.credits != null)
        {
            holder.credits.setText(module.credits);
        }
        if (module.semester != null)
        {
            holder.semester.setText(((Integer) module.semester.intValue()).toString());
        }
        if (module.begin != null)
        {
            holder.start.setText(module.begin);
        }
        else
        {
            holder.start.setText("NONE");
        }
        if (module.end != null)
        {
            holder.end.setText(module.end);
        }
        else
        {
            holder.end.setText("NONE");
        }
        if (module.scolaryear != null)
        {
            holder.scolaryear.setText(((Integer) module.scolaryear.intValue()).toString());
        }

        Date end_registrationdate = null;

        if (module.end_register != null)
        {
            try
            {
                end_registrationdate = new SimpleDateFormat("yyyy-MM-dd").parse(module.end_register);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (module.status != null && module.status.equals("ongoing") && end_registrationdate != null && new java.util.Date().before(end_registrationdate))
        {
            holder.unregister.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.unregister.setVisibility(View.GONE);
        }

        if (module.status != null && module.status.equals("notregistered") && end_registrationdate != null && new java.util.Date().before(end_registrationdate))
        {
            holder.register.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.register.setVisibility(View.GONE);
        }

        holder.expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("INFO", "Pressed expand");
                try
                {
                    RequestManager.ModuleRequest(token, ((Integer) module.scolaryear.intValue()).toString(), module.code, module.codeinstance, new UpdateOnExpand(holder, module, v.getRootView()));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mContext.startActivity(intent);
                }
            }
        });

        holder.register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try
                {
                    RequestManager.RegisterModuleRequest(token, ((Integer) module.scolaryear.intValue()).toString(), module.code, module.codeinstance, new UpdateOnRegister(holder, mContext, module));
                } catch (Exception e) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mContext.startActivity(intent);
                }
            }
        });

        holder.unregister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try
                {
                    RequestManager.UnregisterModuleRequest(token, ((Integer) module.scolaryear.intValue()).toString(), module.code, module.codeinstance, new UpdateOnUnregister(holder, mContext, module));
                } catch (Exception e) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mContext.startActivity(intent);
                }
            }
        });

        return convertView;
    }

    public class UpdateOnRegister implements IUpdateViewOnPostExecute
    {
        ViewHolder holder;
        Context context;
        EPIAllModules.Item module;

        public UpdateOnRegister(ViewHolder holder, Context context, EPIAllModules.Item module)
        {
            this.holder = holder;
            this.context = context;
            this.module = module;
        }

        @Override
        public void UpdateView(EPIJSONObject[] objs) {
            if (objs.length != 0 && objs[0] instanceof EPIError)
            {
                new android.app.AlertDialog.Builder(mContext)
                        .setTitle("Error")
                        .setMessage("Failed to register to " + holder.title.getText().toString())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
            else
            {
                new android.app.AlertDialog.Builder(mContext)
                        .setTitle("Success")
                        .setMessage("Succeeded to register to " + holder.title.getText().toString())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();

                holder.register.setVisibility(View.GONE);
                holder.unregister.setVisibility(View.VISIBLE);
                module.status = "ongoing";
            }
        }
    }

    public class UpdateOnUnregister implements IUpdateViewOnPostExecute
    {
        ViewHolder holder;
        Context context;
        EPIAllModules.Item module;

        public UpdateOnUnregister(ViewHolder holder, Context context, EPIAllModules.Item module)
        {
            this.holder = holder;
            this.context = context;
            this.module = module;
        }

        @Override
        public void UpdateView(EPIJSONObject[] objs) {
            if (objs.length != 0 && objs[0] instanceof EPIError)
            {
                new android.app.AlertDialog.Builder(mContext)
                        .setTitle("Error")
                        .setMessage("Failed to unregister to " + holder.title.getText().toString())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
            else
            {
                new android.app.AlertDialog.Builder(mContext)
                        .setTitle("Success")
                        .setMessage("Succeeded to unregister to " + holder.title.getText().toString())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();

                holder.unregister.setVisibility(View.GONE);
                holder.register.setVisibility(View.VISIBLE);
                module.status = "notregistered";
            }
        }
    }

    public class UpdateOnExpand implements IUpdateViewOnPostExecute
    {
        ViewHolder holder;
        View view;
        EPIAllModules.Item module;

        public UpdateOnExpand(ViewHolder holder, EPIAllModules.Item module, View view)
        {
            this.holder = holder;
            this.view = view;
            this.module = module;
        }

        @Override
        public void UpdateView(EPIJSONObject[] objs) {
            if (objs.length != 0 && objs[0] instanceof EPIError)
            {
                new android.app.AlertDialog.Builder(mContext)
                        .setTitle("Error")
                        .setMessage("Couldn't retrieve " + holder.title.getText().toString() + " informations")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
            else
            {
                if (((EPIModule) objs[0]).title != null)
                {
                    ((TextView) view.getRootView().findViewById(R.id.modules_title)).setText(((EPIModule) objs[0]).title);
                }
                if (((EPIModule) objs[0]).description != null)
                {
                    ((TextView) view.getRootView().findViewById(R.id.module_description)).setText(((EPIModule) objs[0]).description);
                }
                if (((EPIModule) objs[0]).competence != null)
                {
                    ((TextView) view.getRootView().findViewById(R.id.module_competence)).setText(((EPIModule) objs[0]).competence);
                }
                if (((EPIModule) objs[0]).student_grade != null)
                {
                    ((TextView) view.getRootView().findViewById(R.id.module_grade)).setText(((EPIModule) objs[0]).student_grade);
                }
                else
                {
                    ((TextView) view.getRootView().findViewById(R.id.module_grade)).setText("NONE");
                }
                view.getRootView().findViewById(R.id.modules_infoslayout).setVisibility(View.VISIBLE);
                view.getRootView().findViewById(R.id.modules_list).setVisibility(View.GONE);
            }
        }
    }

    public void filter()
    {
        ModulesList = new ArrayList<>();

        for (EPIAllModules.Item item : arrayList)
        {
            if ((semesterFilter == null || (semesterFilter.equals(item.semester))) &&
                    (registeredFilter == false || (registeredFilter == true && item.status != null && !item.status.equals("notregistered"))))
            {
                ModulesList.add(item);
            }
        }

        this.notifyDataSetChanged();
    }
}
