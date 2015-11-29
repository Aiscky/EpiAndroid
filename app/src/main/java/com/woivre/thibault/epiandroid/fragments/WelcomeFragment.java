package com.woivre.thibault.epiandroid.fragments;

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
import android.widget.TextView;

import com.woivre.thibault.epiandroid.R;
import com.woivre.thibault.epiandroid.activities.LoginActivity;
import com.woivre.thibault.epiandroid.objects.EPIError;
import com.woivre.thibault.epiandroid.objects.EPIJSONObject;
import com.woivre.thibault.epiandroid.objects.EPIMessage;
import com.woivre.thibault.epiandroid.objects.EPIUser;
import com.woivre.thibault.epiandroid.request.EPINetworkException;
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

    public WelcomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* GETTING DATA FROM API */

        Bundle b = this.getArguments();
        login = b.getString(LoginActivity.LOGIN);
        password = b.getString(LoginActivity.PASSWORD);
        token = b.getString(LoginActivity.TOKEN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        /* ADD CONTENT DYNAMICALLY TO VIEW */

        try {
            EPIJSONObject rObjUser = RequestManager.UserRequest(this.token, this.login);
            if (rObjUser instanceof EPIError) {
                //TODO Make EpiError
            }
            else {
                ImageView photoProfil = (ImageView)view.findViewById(R.id.photo_profil);
                Drawable myDrawable = RequestManager.LoadImageFromUrl(((EPIUser)rObjUser).picture);
                photoProfil.setImageDrawable(myDrawable);

                TextView login = (TextView)view.findViewById(R.id.login);
                login.setText(this.login);

                TextView log_act =(TextView)view.findViewById(R.id.log_act);
                log_act.setText(((EPIUser) rObjUser).nsstat.active.toString() + "h");

                TextView log_idle =(TextView)view.findViewById(R.id.log_idle);
                log_idle.setText(((EPIUser)rObjUser).nsstat.idle.toString() + "h");

                TextView gpa_value = (TextView)view.findViewById(R.id.gpa_value);
                gpa_value.setText("GPA : " + ((EPIUser) rObjUser).gpa[0].gpa);

                TextView credits_value =(TextView)view.findViewById(R.id.credits_value);
                credits_value.setText("Credits : " + ((EPIUser) rObjUser).credits.toString());

            }
        } catch (EPINetworkException e) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            EPIJSONObject[] rObjList = RequestManager.MessagesRequest(this.token);
            if (rObjList.length != 0 && rObjList[0] instanceof EPIError) {
                //TODO Make EpiError
            }
            else {
                LinearLayout messagesList = (LinearLayout)view.findViewById(R.id.messages_list);

                for (EPIJSONObject msg : rObjList)
                {
                    TextView newMsg = new TextView(this.getActivity());
                    newMsg.setText(((EPIMessage)msg).title);
                    messagesList.addView(newMsg);
                }
            }
        } catch (EPINetworkException e) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }
}
