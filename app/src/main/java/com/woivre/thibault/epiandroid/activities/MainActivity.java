package com.woivre.thibault.epiandroid.activities;

import android.app.FragmentManager;
import android.app.Fragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.woivre.thibault.epiandroid.R;
import com.woivre.thibault.epiandroid.adapters.DrawerLayoutAdapter;
import com.woivre.thibault.epiandroid.fragments.GradesFragment;
import com.woivre.thibault.epiandroid.fragments.ModulesFragment;
import com.woivre.thibault.epiandroid.fragments.PlanningFragment;
import com.woivre.thibault.epiandroid.fragments.ProjectFragment;
import com.woivre.thibault.epiandroid.fragments.WelcomeFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String[] menuTitles;
    private ArrayList<Drawable> menuDrawables;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String token;
    private String login;
    private String password;

    private final int NB_INFOS_IMG_FROM_SPRITESHEET = 4;
    private final int NB_INFOS_IMG_FROM_DRAWABLE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitResources();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        DrawerLayoutAdapter menuAdapter = new DrawerLayoutAdapter(this, menuTitles, menuDrawables);

        mDrawerList.setAdapter(menuAdapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        /* GETTING EXTRAS */

        Bundle b = this.getIntent().getExtras();
        token = b.getString(LoginActivity.TOKEN);
        login = b.getString(LoginActivity.LOGIN);
        password = b.getString(LoginActivity.PASSWORD);

        /* SETTING WELCOME PAGE */

        this.selectItem(0);
    }

    private void InitResources()
    {
        menuTitles = getResources().getStringArray(R.array.menu_titles);
        TypedArray drawablesResources = getResources().obtainTypedArray(R.array.menu_icons);
        Drawable iconDrawable = null;

        menuDrawables = new ArrayList<Drawable>();

        for (int i = 0; i < drawablesResources.length(); ++i)
        {

        }

        drawablesResources.recycle();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    //TODO Make an history in certains cases

    private void selectItem(int position) {
        Fragment fragment;

        /* CREATE FRAGMENT */

        switch (position)
        {
            case 0:
                fragment = new WelcomeFragment();
                break;
            case 1:
                fragment = new PlanningFragment();
                break;
            case 2:
                fragment = new ProjectFragment();
                break;
            case 3:
                fragment = new ModulesFragment();
                break;
            case 4:
                fragment = new GradesFragment();
                break;
            default:
                fragment = new WelcomeFragment();
                break;
        }

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
        setTitle(menuTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }
}
