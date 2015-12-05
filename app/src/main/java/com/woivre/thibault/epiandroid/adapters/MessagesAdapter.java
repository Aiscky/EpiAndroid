package com.woivre.thibault.epiandroid.adapters;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.woivre.thibault.epiandroid.R;
import com.woivre.thibault.epiandroid.activities.MainActivity;
import com.woivre.thibault.epiandroid.html.MovementCheck;
import com.woivre.thibault.epiandroid.objects.EPIMessage;

import java.util.ArrayList;

public class MessagesAdapter extends BaseAdapter {
    public ArrayList<EPIMessage> messagesList;
    Context mContext;
    LayoutInflater inflater;

    public MessagesAdapter(ArrayList<EPIMessage> messagesList, Context context)
    {
        this.messagesList = messagesList;
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return messagesList.size();
    }

    @Override
    public Object getItem(int position) {
        return messagesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder
    {
        TextView title;
        TextView content;
        TextView date;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null)
        {
            Log.d("INFO", "CREATING MESSAGE_ITEM_VIEW");

            convertView = inflater.inflate(R.layout.messages_item, null);
            holder = new ViewHolder();

            holder.title = ((TextView) convertView.findViewById(R.id.message_title));
            holder.content = ((TextView) convertView.findViewById(R.id.message_content));
            holder.date = ((TextView) convertView.findViewById(R.id.message_date));

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        EPIMessage currentMessage = messagesList.get(position);

        if (currentMessage.title != null)
        {
            currentMessage.title = currentMessage.title.replaceAll("<a href=\"", "<a href=\"http://intra.epitech.eu");
            holder.title.setText(Html.fromHtml(currentMessage.title));
            holder.title.setMovementMethod(new MovementCheck());
        }
        if (currentMessage.content != null)
        {
            currentMessage.content = currentMessage.content.replaceAll("<a href=\"", "<a href=\"http://intra.epitech.eu");
            holder.content.setText(Html.fromHtml(currentMessage.content));
            holder.title.setMovementMethod(new MovementCheck());
        }
        if (currentMessage.date != null)
        {
            holder.date.setText(currentMessage.date);
        }

        return convertView;
    }
}
