package com.woivre.thibault.epiandroid.html;

import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by Thibault on 04/12/2015.
 */

public class MovementCheck extends LinkMovementMethod {

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        try
        {
            return super.onTouchEvent(widget, buffer, event);
        }
        catch (Exception e)
        {
            return true;
        }
    }
}