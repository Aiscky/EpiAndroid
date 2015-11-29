package com.woivre.thibault.epiandroid.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.woivre.thibault.epiandroid.R;
import com.woivre.thibault.epiandroid.objects.EPIError;
import com.woivre.thibault.epiandroid.objects.EPIEventPlanning;
import com.woivre.thibault.epiandroid.request.EPINetworkException;
import com.woivre.thibault.epiandroid.request.RequestManager;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thibault on 28/11/2015.
 */

public class PlanningAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    public List<EPIEventPlanning> EPIEventsList = null;
    public ArrayList<EPIEventPlanning> arrayList;
    String token;
    public Boolean RegisteredFilter = false;
    public Boolean ModulesFilter = false;

    public PlanningAdapter(Context context, List<EPIEventPlanning> EPIEventsList, String token)
    {
        mContext = context;
        this.EPIEventsList = EPIEventsList;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<EPIEventPlanning>();
        this.arrayList.addAll(EPIEventsList);
        this.token = token;
    }

    public class ViewHolder
    {
        TextView title;
        TextView start;
        TextView end;
        TextView room;
        Button registeractivity;
        Button unregisteractivity;
        Button validatetoken;
    }

    @Override
    public int getCount() {
        return EPIEventsList.size();
    }

    @Override
    public Object getItem(int position) {
        return EPIEventsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.planninglist_item, null);

            holder.title = (TextView) convertView.findViewById(R.id.planningevent_title);
            holder.start = (TextView) convertView.findViewById(R.id.planningevent_start);
            holder.end = (TextView) convertView.findViewById(R.id.planningevent_end);
            holder.room = (TextView) convertView.findViewById(R.id.planningevent_room);
            holder.registeractivity = (Button) convertView.findViewById(R.id.planningevent_registeractivity);
            holder.unregisteractivity = (Button) convertView.findViewById(R.id.planningevent_unregisteractivity);
            holder.validatetoken = (Button) convertView.findViewById(R.id.planningevent_validatetoken);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        if (EPIEventsList.get(position).acti_title != null)
            holder.title.setText(EPIEventsList.get(position).acti_title);

        if (EPIEventsList.get(position).start != null)
            holder.start.setText(EPIEventsList.get(position).start);

        if (EPIEventsList.get(position).end != null)
            holder.end.setText(EPIEventsList.get(position).end);

        if (EPIEventsList.get(position).room != null && EPIEventsList.get(position).room.code != null)
            holder.room.setText(EPIEventsList.get(position).room.code);

        if (EPIEventsList.get(position).allow_register != null &&
                EPIEventsList.get(position).allow_register &&
                EPIEventsList.get(position).event_registered != null &&
                !EPIEventsList.get(position).event_registered)
        {
            holder.registeractivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try
                    {
                        EPIEventPlanning obj = EPIEventsList.get(position);

                        EPIError error = RequestManager.RegisterEventRequest(token, obj.scolaryear, obj.codemodule, obj.codeinstance, obj.codeacti, obj.codeevent);
                        if (error != null)
                        {
                            ((TextView)v.getRootView().findViewById(R.id.planning_alert)).setText("Impossible to register to : " + obj.acti_title);
                            ((TextView)v.getRootView().findViewById(R.id.planning_alert)).setVisibility(View.VISIBLE);
                            holder.registeractivity.setVisibility(View.GONE);
                        }
                        else
                        {
                            obj.allow_register = false;
                            obj.event_registered = true;
                            ((TextView)v.getRootView().findViewById(R.id.planning_alert)).setText("Registered to activity : " + obj.acti_title);
                            ((TextView)v.getRootView().findViewById(R.id.planning_alert)).setVisibility(View.VISIBLE);
                            holder.unregisteractivity.setVisibility(View.VISIBLE);
                            holder.registeractivity.setVisibility(View.GONE);
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
                }
            });
        }
        else
        {
            holder.registeractivity.setVisibility(View.GONE);
        }

        if (EPIEventsList.get(position).allow_token != null && EPIEventsList.get(position).allow_token == true)
        {
            holder.validatetoken.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try
                    {
                        EPIEventPlanning obj = EPIEventsList.get(position);
                        String tokenNumber = "00000000";

                        tokenNumber = ((EditText)v.getRootView().findViewById(R.id.planningevent_token)).getText().toString();
                        Log.d("TOKENNUMBER", tokenNumber);

                        EPIError error = RequestManager.ValidateTokenRequest(token, obj.scolaryear, obj.codemodule, obj.codeinstance, obj.codeacti, obj.codeevent, tokenNumber);
                        if (error != null)
                        {
                            ((TextView)v.getRootView().findViewById(R.id.planning_alert)).setText("Enable to Validate token for : " + obj.acti_title);
                            ((TextView)v.getRootView().findViewById(R.id.planning_alert)).setVisibility(View.VISIBLE);
                            holder.validatetoken.setVisibility(View.GONE);
                        }
                        else
                        {
                            obj.allow_token = false;
                            ((TextView)v.getRootView().findViewById(R.id.planning_alert)).setText("Validated token for : " + obj.acti_title);
                            ((TextView)v.getRootView().findViewById(R.id.planning_alert)).setVisibility(View.VISIBLE);
                            holder.validatetoken.setVisibility(View.GONE);
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
                }
            });
        }
        else
        {

           holder.validatetoken.setVisibility(View.GONE);
        }


        holder.unregisteractivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EPIEventPlanning obj = EPIEventsList.get(position);

                    EPIError error = RequestManager.UnregisterEventRequest(token, obj.scolaryear, obj.codemodule, obj.codeinstance, obj.codeacti, obj.codeevent);
                    if (error != null) {
                        holder.unregisteractivity.setVisibility(View.GONE);
                    }
                    else
                    {
                        obj.allow_register = true;
                        obj.event_registered = false;
                        ((TextView) v.getRootView().findViewById(R.id.planning_alert)).setText("Unregistered to activity : " + obj.acti_title);
                        ((TextView) v.getRootView().findViewById(R.id.planning_alert)).setVisibility(View.VISIBLE);
                        holder.unregisteractivity.setVisibility(View.GONE);
                        holder.registeractivity.setVisibility(View.VISIBLE);
                    }
                } catch (EPINetworkException e) {
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
        this.EPIEventsList = new ArrayList<EPIEventPlanning>();

        for (EPIEventPlanning event : arrayList)
        {
            if ((!RegisteredFilter || (RegisteredFilter && event.event_registered != null && event.event_registered)) &&
                    (!ModulesFilter || (ModulesFilter && event.module_registered != null && event.module_registered)))
            {
                this.EPIEventsList.add(event);
            }
        }

        notifyDataSetChanged();
    }
}
