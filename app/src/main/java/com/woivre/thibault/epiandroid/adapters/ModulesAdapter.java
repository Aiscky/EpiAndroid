package com.woivre.thibault.epiandroid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.woivre.thibault.epiandroid.R;
import com.woivre.thibault.epiandroid.objects.EPIAllModules;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thibault on 29/11/2015.
 */
public class ModulesAdapter extends BaseAdapter {
    Context mContext;
    public List<EPIAllModules.Item> ModulesList;
    public ArrayList<EPIAllModules.Item> arrayList;
    LayoutInflater inflater;

    public ModulesAdapter(Context context, List<EPIAllModules.Item> modulesList)
    {
        mContext = context;
        ModulesList = modulesList;
        arrayList = new ArrayList<EPIAllModules.Item>();
        arrayList.addAll(modulesList);
        inflater = LayoutInflater.from(mContext);
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
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.moduleslist_item, null);
            holder = new ViewHolder();
            holder.title = (TextView)convertView.findViewById(R.id.module_title);
            holder.credits = (TextView)convertView.findViewById(R.id.module_credits);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        EPIAllModules.Item module = ModulesList.get(position);

        if (module.title != null)
        {
            holder.title.setText(module.title);
        }
        if (module.credits != null)
        {
            holder.credits.setText(module.credits);
        }

        return convertView;
    }
}
