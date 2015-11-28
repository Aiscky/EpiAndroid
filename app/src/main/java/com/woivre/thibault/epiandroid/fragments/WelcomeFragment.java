package com.woivre.thibault.epiandroid.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.woivre.thibault.epiandroid.R;
import com.woivre.thibault.epiandroid.activities.LoginActivity;
import com.woivre.thibault.epiandroid.objects.EPIError;
import com.woivre.thibault.epiandroid.objects.EPIJSONObject;
import com.woivre.thibault.epiandroid.objects.EPIMessage;
import com.woivre.thibault.epiandroid.request.EPINetworkException;
import com.woivre.thibault.epiandroid.request.RequestManager;

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
