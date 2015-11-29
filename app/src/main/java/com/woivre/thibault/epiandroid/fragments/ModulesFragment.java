package com.woivre.thibault.epiandroid.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.woivre.thibault.epiandroid.R;
import com.woivre.thibault.epiandroid.activities.LoginActivity;

public class ModulesFragment extends android.app.Fragment {
    String login;
    String password;
    String token;

    public ModulesFragment() {
        // Required empty public constructor
    }

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
        View view = inflater.inflate(R.layout.fragment_modules, container, false);

        /* DYNAMICALLY FILL VIEW */


        return view;
    }
}
