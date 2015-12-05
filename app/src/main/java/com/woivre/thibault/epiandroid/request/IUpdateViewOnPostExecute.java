package com.woivre.thibault.epiandroid.request;

import android.content.Context;

import com.woivre.thibault.epiandroid.objects.EPIJSONObject;

/**
 * Created by Thibault on 03/12/2015.
 */

public interface IUpdateViewOnPostExecute {
    void UpdateView(EPIJSONObject[] objs);
}
