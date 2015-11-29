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
import com.woivre.thibault.epiandroid.objects.EPIJSONObject;
import com.woivre.thibault.epiandroid.objects.EPIModule;
import com.woivre.thibault.epiandroid.request.EPINetworkException;
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

        Date end_registrationdate = null;

        if (module.end_register != null)
        {
            try {
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
                try {
                    EPIJSONObject obj = RequestManager.ModuleRequest(token, ((Integer) module.scolaryear.intValue()).toString(), module.code, module.codeinstance);
                    if (obj instanceof EPIError)
                    {
                        if (module.title != null)
                        {
                            ((TextView) v.getRootView().findViewById(R.id.modules_msg)).setText("Couldn't get " + module.title + " informations");
                        }
                    }
                    else
                    {
                        if (((EPIModule) obj).description != null)
                        {
                            ((TextView) v.getRootView().findViewById(R.id.module_description)).setText(((EPIModule) obj).description);
                        }
                        if (((EPIModule) obj).competence != null)
                        {
                            ((TextView) v.getRootView().findViewById(R.id.module_competence)).setText(((EPIModule) obj).competence);
                        }
                        if (((EPIModule) obj).student_grade != null && !((EPIModule) obj).student_grade.isEmpty())
                        {
                            ((TextView) v.getRootView().findViewById(R.id.module_grade)).setText(((EPIModule) obj).student_grade);
                        }
                        else
                        {
                            ((TextView) v.getRootView().findViewById(R.id.module_description)).setText("NONE");
                        }

                        v.getRootView().findViewById(R.id.modules_infoslayout).setVisibility(View.VISIBLE);
                        ((TextView) v.getRootView().findViewById(R.id.modules_msg)).setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        holder.register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EPIAllModules.Item module = ModulesList.get(position);
                try {
                    EPIError error = RequestManager.RegisterModuleRequest(token, ((Integer) module.scolaryear.intValue()).toString(), module.code, module.codeinstance);
                    if (error != null) {
                        ((TextView) v.getRootView().findViewById(R.id.modules_msg)).setText("Can't register to module : " + module.title);
                    } else {
                        ((TextView) v.getRootView().findViewById(R.id.modules_msg)).setText("Registered successfully to : " + module.title);
                        holder.unregister.setVisibility(View.VISIBLE);
                    }
                    ((TextView) v.getRootView().findViewById(R.id.modules_msg)).setVisibility(View.VISIBLE);
                    holder.register.setVisibility(View.GONE);
                    module.status = "ongoing";
                } catch (EPINetworkException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        holder.unregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EPIAllModules.Item module = ModulesList.get(position);
                    EPIError error = RequestManager.UnregisterModuleRequest(token, ((Integer) module.scolaryear.intValue()).toString(), module.code, module.codeinstance);
                    if (error != null)
                    {
                        ((TextView) v.getRootView().findViewById(R.id.modules_msg)).setText("Can't unregister from module : " + module.title);
                    }
                    else
                    {
                        ((TextView) v.getRootView().findViewById(R.id.modules_msg)).setText("Unregistered successfully from : " + module.title);
                        holder.register.setVisibility(View.VISIBLE);
                    }
                    ((TextView) v.getRootView().findViewById(R.id.modules_msg)).setVisibility(View.VISIBLE);
                    holder.unregister.setVisibility(View.GONE);
                    module.status = "notregistered";
                    Log.d("BONJOUR", module.toString());
                }
                catch (EPINetworkException e)
                {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return convertView;
    }

    public void filter()
    {
        ModulesList = new ArrayList<EPIAllModules.Item>();

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
