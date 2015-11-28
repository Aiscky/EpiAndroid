package com.woivre.thibault.epiandroid.fragments;

import android.app.Fragment;

/**
 * Created by Thibault on 27/11/2015.
 */
public class FragmentFactory {
    public static Fragment createFragmentFromPosition(int position)
    {
        Fragment fragment;

        switch (position)
        {
            case 0:
                fragment = new WelcomeFragment();
                break;
            default:
                fragment = new WelcomeFragment();
                break;
        }
        return fragment;
    }
}