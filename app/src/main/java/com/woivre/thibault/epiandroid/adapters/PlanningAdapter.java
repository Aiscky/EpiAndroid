package com.woivre.thibault.epiandroid.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.woivre.thibault.epiandroid.activities.LoginActivity;
import com.woivre.thibault.epiandroid.objects.EPIError;
import com.woivre.thibault.epiandroid.objects.EPIEventPlanning;
import com.woivre.thibault.epiandroid.objects.EPIJSONObject;
import com.woivre.thibault.epiandroid.request.IUpdateViewOnPostExecute;
import com.woivre.thibault.epiandroid.request.RequestManager;

import java.util.ArrayList;
import java.util.List;

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
        this.arrayList = new ArrayList<>();
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

        if (EPIEventsList.get(position).past != null && EPIEventsList.get(position).past)
        {
            convertView.findViewById(R.id.planningevent_item).setBackgroundColor(Color.parseColor("#BDC3C7"));
        }
        else
        {
            convertView.findViewById(R.id.planningevent_item).setBackgroundColor(Color.parseColor("#ced4d9"));
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
                EPIEventsList.get(position).allow_register)
        {
            holder.registeractivity.setVisibility(View.VISIBLE);
            holder.registeractivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try
                    {
                        EPIEventPlanning obj = EPIEventsList.get(position);

                        RequestManager.RegisterEventRequest(token,
                                obj.scolaryear,
                                obj.codemodule,
                                obj.codeinstance,
                                obj.codeacti,
                                obj.codeevent,
                                new UpdateOnRegister(holder, obj));
                    } catch (Exception e) {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
        else
        {
            holder.registeractivity.setVisibility(View.GONE);
        }

        if (EPIEventsList.get(position).allow_token != null &&
                EPIEventsList.get(position).allow_token &&
                EPIEventsList.get(position).module_registered != null &&
                EPIEventsList.get(position).module_registered)
        {
            holder.validatetoken.setVisibility(View.VISIBLE);
            holder.validatetoken.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    try
                    {
                        final EPIEventPlanning obj = EPIEventsList.get(position);

                        /* SETTING TOKEN ALERTDIALOG */

                        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(mContext);
                        alertDialog.setTitle("Enter Token");

                        final EditText input = new EditText(mContext);

                        alertDialog.setView(input);

                        alertDialog.setPositiveButton("Validate", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try
                                {
                                    RequestManager.ValidateTokenRequest(token,
                                            obj.scolaryear,
                                            obj.codemodule,
                                            obj.codeinstance,
                                            obj.codeacti,
                                            obj.codeevent,
                                            input.getText().toString(),
                                            new UpdateOnValidateToken(holder, obj));
                                }
                                catch (Exception e)
                                {
                                    Intent intent = new Intent(mContext, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    mContext.startActivity(intent);
                                }
                            }
                        });

                        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        alertDialog.show();
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

        if (EPIEventsList.get(position).allow_register != null &&
                EPIEventsList.get(position).allow_register) {
            holder.unregisteractivity.setVisibility(View.VISIBLE);
            holder.unregisteractivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EPIEventPlanning obj = EPIEventsList.get(position);

                    try {
                        RequestManager.UnregisterEventRequest(token,
                                obj.scolaryear,
                                obj.codemodule,
                                obj.codeinstance,
                                obj.codeacti,
                                obj.codeevent,
                                new UpdateOnUnregister(holder, obj));
                    } catch (Exception e) {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
        else
        {
            holder.unregisteractivity.setVisibility(View.GONE);
        }

        return convertView;
    }

    public class UpdateOnRegister implements IUpdateViewOnPostExecute
    {
        ViewHolder holder;
        EPIEventPlanning event;

        public UpdateOnRegister(ViewHolder holder, EPIEventPlanning event)
        {
            this.holder = holder;
            this.event = event;
        }

        @Override
        public void UpdateView(EPIJSONObject[] objs) {
            android.app.AlertDialog.Builder popupRegister = new android.app.AlertDialog.Builder(mContext)
                    .setTitle("Register");


            if (objs.length != 0 && objs[0] instanceof EPIError)
            {
                popupRegister.setMessage("Failed to register to " + event.acti_title)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
            else
            {
                popupRegister.setMessage("Registered successfully to " + event.acti_title)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                holder.unregisteractivity.setVisibility(View.VISIBLE);
            }
            holder.registeractivity.setVisibility(View.GONE);
        }
    }

    public class UpdateOnUnregister implements IUpdateViewOnPostExecute
    {
        ViewHolder holder;
        EPIEventPlanning event;

        public UpdateOnUnregister(ViewHolder holder, EPIEventPlanning event)
        {
            this.holder = holder;
            this.event = event;
        }

        @Override
        public void UpdateView(EPIJSONObject[] objs) {
            android.app.AlertDialog.Builder popupRegister = new android.app.AlertDialog.Builder(mContext)
                    .setTitle("Unregister");


            Log.d("INFO1", event.toString());
            if (objs.length != 0 && objs[0] instanceof EPIError)
            {
                popupRegister.setMessage("Failed to unregister from " + event.acti_title)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
            else
            {
                popupRegister.setMessage("Unregistered successfully to " + event.acti_title)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                holder.registeractivity.setVisibility(View.VISIBLE);
            }
            holder.unregisteractivity.setVisibility(View.GONE);
        }
    }

    public class UpdateOnValidateToken implements IUpdateViewOnPostExecute
    {
        ViewHolder holder;
        EPIEventPlanning event;

        public UpdateOnValidateToken(ViewHolder holder, EPIEventPlanning event)
        {
            this.holder = holder;
            this.event = event;
        }

        @Override
        public void UpdateView(EPIJSONObject[] objs) {
            android.app.AlertDialog.Builder popupRegister = new android.app.AlertDialog.Builder(mContext)
                    .setTitle("Validate Token");

            if (objs.length != 0 && objs[0] instanceof EPIError)
            {
                popupRegister.setMessage("Failed to validate token from " + event.acti_title)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
            else
            {
                popupRegister.setMessage("Validated successfully token from " + event.acti_title)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                holder.validatetoken.setVisibility(View.GONE);
            }
        }
    }

    public void filter()
    {
        this.EPIEventsList = new ArrayList<>();

        /* PREVENT ERROR ON NO ITEM FILTERING */

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
