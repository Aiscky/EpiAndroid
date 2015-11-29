package com.woivre.thibault.epiandroid.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.woivre.thibault.epiandroid.R;
import com.woivre.thibault.epiandroid.objects.EPIEventPlanning;
import com.woivre.thibault.epiandroid.objects.EPIGrades;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Thibault on 29/11/2015.
 */
public class GradesAdapter extends BaseAdapter {
    Context mContext;
    List<EPIGrades.Grade> GradesList;
    ArrayList<EPIGrades.Grade> arrayList;
    LayoutInflater inflater;

    public GradesAdapter(Context context, List<EPIGrades.Grade> gradesList)
    {
        mContext = context;
        this.GradesList = gradesList;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<EPIGrades.Grade>();
        this.arrayList.addAll(gradesList);
    }

    @Override
    public int getCount() {
        return GradesList.size();
    }

    @Override
    public Object getItem(int position) {
        return GradesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder
    {
        TextView titlemodule;
        TextView title;
        TextView finalnote;
        TextView correcteur;
        TextView date;
        TextView comment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.gradeslist_item, null);

            holder = new ViewHolder();
            holder.titlemodule = (TextView)convertView.findViewById(R.id.grade_titlemodule);
            holder.title = (TextView)convertView.findViewById(R.id.grade_title);
            holder.finalnote = (TextView)convertView.findViewById(R.id.grade_finalnote);
            holder.correcteur = (TextView)convertView.findViewById(R.id.grade_correcteur);
            holder.date = (TextView)convertView.findViewById(R.id.grade_date);
            holder.comment = (TextView)convertView.findViewById(R.id.grade_comment);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

         /* MODIFYING VIEWHOLDER */

        EPIGrades.Grade grade = GradesList.get(position);
        Log.d("INFO", grade.toString());

        Log.d("TITLEMODULE", grade.titlemodule);
        Log.d("DATE", grade.date);

        if (grade.title != null)
        {
            holder.titlemodule.setText(grade.title);
        }
        if (grade.titlemodule != null)
        {
            holder.titlemodule.setText(grade.titlemodule);
        }
        if (grade.final_note != null)
        {
            holder.finalnote.setText(grade.final_note.toString());
        }
        if (grade.correcteur != null)
        {
            holder.correcteur.setText(grade.correcteur);
        }
        if (grade.date != null)
        {
            holder.date.setText(grade.date);
            holder.date.setVisibility(View.VISIBLE);
        }
        if (grade.comment != null)
        {
            holder.date.setText(grade.comment);
        }

        return convertView;
    }

    public void filter(String scolarYear)
    {
        this.GradesList = new ArrayList<EPIGrades.Grade>();

        for (EPIGrades.Grade grade : arrayList)
        {
            if (((Integer) grade.scolaryear.intValue()).toString().equals(scolarYear))
            {
                GradesList.add(grade);
            }
        }

        notifyDataSetChanged();
    }
}
