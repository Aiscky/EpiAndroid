package com.woivre.thibault.epiandroid.activities;

import android.app.FragmentManager;
import android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.woivre.thibault.epiandroid.R;
import com.woivre.thibault.epiandroid.fragments.FragmentFactory;
import com.woivre.thibault.epiandroid.fragments.WelcomeFragment;

//TODO Make Button in the title bar !

public class MainActivity extends AppCompatActivity {

    private String[] mEpiTitle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String token;
    private String login;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEpiTitle = getResources().getStringArray(R.array.mTitles);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mEpiTitle));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        /* GETTING EXTRAS */

        Bundle b = this.getIntent().getExtras();
        token = b.getString(LoginActivity.TOKEN);
        login = b.getString(LoginActivity.LOGIN);
        password = b.getString(LoginActivity.PASSWORD);

        /* SETTING WELCOME PAGE */

        this.selectItem(0);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        Fragment fragment;

        /* CREATE FRAGMENT */

        fragment = FragmentFactory.createFragmentFromPosition(position);

        /* ADD ARGUMENTS TO FRAGMENT */

        Bundle b = new Bundle();
        b.putString(LoginActivity.LOGIN, this.login);
        b.putString(LoginActivity.PASSWORD, this.password);
        b.putString(LoginActivity.TOKEN, this.token);
        fragment.setArguments(b);

        /* REPLACE FRAGMENT */

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        // Highlight the selected item, update the title, and close the drawer

        mDrawerList.setItemChecked(position, true);
        setTitle(mEpiTitle[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }
}
