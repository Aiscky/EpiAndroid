package com.woivre.thibault.epiandroid.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.woivre.thibault.epiandroid.R;
import com.woivre.thibault.epiandroid.activities.LoginActivity;
import com.woivre.thibault.epiandroid.adapters.MessagesAdapter;
import com.woivre.thibault.epiandroid.objects.EPIError;
import com.woivre.thibault.epiandroid.objects.EPIJSONObject;
import com.woivre.thibault.epiandroid.objects.EPIMessage;
import com.woivre.thibault.epiandroid.objects.EPIUser;
import com.woivre.thibault.epiandroid.request.EPINetworkException;
import com.woivre.thibault.epiandroid.request.ILoadImageOnPostExecute;
import com.woivre.thibault.epiandroid.request.IUpdateViewOnPostExecute;
import com.woivre.thibault.epiandroid.request.RequestManager;
import com.woivre.thibault.epiandroid.utils.Utils;

import org.w3c.dom.Document;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class WelcomeFragment extends android.app.Fragment {

    String login;
    String password;
    String token;

    public WelcomeFragment()
    {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = this.getArguments();
        login = b.getString(LoginActivity.LOGIN);
        password = b.getString(LoginActivity.PASSWORD);
        token = b.getString(LoginActivity.TOKEN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        /* SETTING MESSAGES LISTVIEW */

        ListView messagesListView = ((ListView) view.findViewById(R.id.welcome_messageslist));
        messagesListView.setAdapter(new MessagesAdapter(new ArrayList<EPIMessage>(), this.getActivity()));

        /* UPDATING MESSAGES LISTVIEW INFOS */

        try
        {
            RequestManager.MessagesRequest(this.token, new UpdateMessagesView(view));
        } catch (EPINetworkException e) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* FILLING INTERFACE */

        try
        {
            Log.d("Login", this.login);
            RequestManager.UserRequest(this.token, this.login, new UpdateUserInfoView(view));
        } catch (EPINetworkException e) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    /* Update class to update user info */

    class UpdateUserInfoView implements IUpdateViewOnPostExecute
    {
        View mView;

        public UpdateUserInfoView(View view)
        {
            this.mView = view;
        }

        @Override
        public void UpdateView(EPIJSONObject[] objs) {
            Log.d("INFO", "ENTER UPDATEVIEW");

            if (objs[0] instanceof EPIError)
            {
                //TODOERROR
            }
            else if (objs[0] instanceof EPIUser)
            {
                Log.d("INFO", "ENTER UPDATE");

                /* UPDATE PROFIL IMAGE */

                try
                {
                    RequestManager.LoadImageFromUrl(((EPIUser) objs[0]).picture, new UpdatePhotoView(mView));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                /* UPDATE USERINFOS VIEW */

                EPIUser user = ((EPIUser) objs[0]);

                ((TextView) mView.findViewById(R.id.welcome_login)).setText(login);
                ((TextView) mView.findViewById(R.id.welcome_gpa)).setText(user.gpa[0].gpa.toString());
                ((TextView) mView.findViewById(R.id.welcome_credits)).setText(((Integer) user.credits.intValue()).toString());
                ((TextView) mView.findViewById(R.id.welcome_logact)).setText(user.nsstat.active.toString());
                ((TextView) mView.findViewById(R.id.welcome_logidle)).setText(user.nsstat.idle.toString());
            }
        }
    }

    /* Update class to update messages */

    class UpdateMessagesView implements IUpdateViewOnPostExecute
    {
        View mView;

        public UpdateMessagesView(View view)
        {
            this.mView = view;
        }

        @Override
        public void UpdateView(EPIJSONObject[] objs) {
            if (objs.length != 0 && objs[0] instanceof EPIError) {
                //TODO ERROR
            }
            else
            {
                ListView messagesListView = ((ListView) this.mView.findViewById(R.id.welcome_messageslist));
                ArrayList<EPIMessage> messageListData = ((MessagesAdapter) messagesListView.getAdapter()).messagesList;

                for (EPIJSONObject msg : objs)
                {
                    messageListData.add(((EPIMessage) msg));
                }

                ((MessagesAdapter) messagesListView.getAdapter()).notifyDataSetChanged();
            }
        }
    }

    /* Update photo */

    class UpdatePhotoView implements ILoadImageOnPostExecute
    {
        View mView;

        public UpdatePhotoView(View view)
        {
            mView = view;
        }

        @Override
        public void LoadImage(Drawable d) {
            ((ImageView) mView.findViewById(R.id.welcome_photo)).setImageDrawable(d);
        }
    }
}
