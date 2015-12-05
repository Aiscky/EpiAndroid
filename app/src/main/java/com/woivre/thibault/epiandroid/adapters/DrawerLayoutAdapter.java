package com.woivre.thibault.epiandroid.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.woivre.thibault.epiandroid.R;

import java.util.ArrayList;

public class DrawerLayoutAdapter extends BaseAdapter {
    Context mContext;
    String[] menuTitles;
    ArrayList<Drawable> menuDrawables;
    LayoutInflater inflater;

    public DrawerLayoutAdapter(Context context, String[] menuTitles, ArrayList<Drawable> menuDrawables)
    {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.menuTitles = menuTitles;
        this.menuDrawables = menuDrawables;
    }

    @Override
    public int getCount() {
        return menuTitles.length;
    }

    @Override
    public Object getItem(int position) {
        return menuTitles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //TODO HANDLE ICON AND DISPLAY

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            Log.d("INFO", "CREATING MENU_ITEM_VIEW");

            convertView = inflater.inflate(R.layout.menu_item, null);
            Log.d("INFO", menuTitles[position]);
            ((TextView) convertView.findViewById(R.id.menuitem_title)).setText(menuTitles[position]);
            //((ImageView) convertView.findViewById(R.id.menuitem_icon)).setImageDrawable(menuDrawables.get(position));
        }

        return convertView;
    }
}
